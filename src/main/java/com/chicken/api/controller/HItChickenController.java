package com.chicken.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.AccountHit;
import com.chicken.api.model.AccountUser;
import com.chicken.api.service.AccountHitService;
import com.chicken.api.service.AccountUserService;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.CodeEnum;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.DateUtil;
import com.chicken.api.vo.HitChickenRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zhanglei
 * @date 2019-09-07 12:00
 */
@RestController
@RequestMapping("/mp")
public class HItChickenController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AccountHitService accountHitService;

    @Autowired
    AccountUserService accountUserService;

    @Autowired
    RedisService redisService;


    @RequestMapping(value = "/hitChicken", method = RequestMethod.POST)
    @ResponseBody
    public Object hitChicken(@RequestBody HitChickenRequest request) {

        if (StringUtils.isBlank(request.getOpenid()) || StringUtils.isBlank(request.getScore()) || StringUtils.isBlank(request.getHitOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Object userId = redisService.get(ContantUtil.OPEN_ID.concat(request.getOpenid()));
        if (null == userId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            request.setUserId(userId.toString());
        }

        Object hitUserId = redisService.get(ContantUtil.OPEN_ID.concat(request.getHitOpenid()));
        if (null == hitUserId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            request.setHitUserId(hitUserId.toString());
        }

        //每天最大分值
        Object maxScore = redisService.get(ContantUtil.MAX_SOCRE_DAY);

        //当期那时间
        String now = DateUtil.getSpecifiedDay("yyyy-MM-dd", 0);

        //已经获得的积分
        Object gainScoreObj = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(request.getUserId()));
        Double gainScore = 0.0;
        if (null != gainScoreObj) {
            gainScore = Double.valueOf(gainScoreObj.toString());
        }
        Double diffValue = Double.valueOf(maxScore.toString()) - gainScore;

        //不再获得积分
        if (diffValue <= 0) {
            Object score = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(request.getUserId()));
            return returnResult(score.toString());
        }

        //差值大于得分 插入记录
        if (diffValue > Double.valueOf(request.getScore())) {

            //更新缓存
            redisService.set(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(request.getUserId()), Double.valueOf(request.getScore()) + gainScore);

            //插入分值
            insetDetail(Double.valueOf(request.getScore()), request.getUserId(),request.getOpenid(),request.getHitUserId());
        } else {
            //更新缓存
            redisService.set(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(request.getUserId()), gainScore + diffValue);

            //插入分值
            insetDetail(diffValue, request.getUserId(),request.getOpenid(),request.getHitUserId());
        }


        gainScoreObj = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(request.getUserId()));

        return returnResult(gainScoreObj.toString());
    }

    public void insetDetail(Double score, String userId,String openid,String hitUserId) {

        //更新账户信息
        AccountUser accountUser = this.accountUserService.selectByPrimaryKey(Integer.valueOf(userId));
        accountUser.setAttentCount(accountUser.getAttentCount() + Double.valueOf(score));
        accountUser.setBalance(accountUser.getBalance() + Double.valueOf(score));
        accountUserService.updateByPrimaryKey(accountUser);
        logger.info("用户揍小鸡，用户id{}，用户打卡得分{}", userId, score);

        //修改排行榜分值
        redisService.incrScore(ContantUtil.USER_RANKING_LIST,accountUser.getUserId().toString(),score);

        //修改好友排行榜分值
        Object myFriend = redisService.get(ContantUtil.USER_OWNER_SET.concat(openid));
        if(null != myFriend){
            redisService.incrScore(myFriend.toString(),accountUser.getUserId().toString(),score);
        }

        //插入记录
        insertHitDetail(Integer.valueOf(userId), score, accountUser.getAttentCount(),Integer.valueOf(hitUserId));
    }


    /**
     * 插入到揍小鸡记录
     * @param userId
     * @param score
     * @param count
     */
    public void insertHitDetail(Integer userId, Double score, Double count,Integer hitUserId) {
        AccountHit accountHit = new AccountHit();
        accountHit.setCreateTime(new Date());
        accountHit.setDetailFlag(2);
        accountHit.setDetailType("揍小鸡");
        accountHit.setRemark("揍小鸡");
        accountHit.setUserId(userId);
        accountHit.setScore(score);
        accountHit.setSignedTime(new Date());
        accountHit.setStatus("1");
        accountHit.setScoreCount(count);
        accountHit.setHitUserId(hitUserId);
        accountHitService.insert(accountHit);
        logger.info("用户揍小鸡，用户id{},打卡时间{}", userId, DateUtil.getNow());
    }

    private CallResult returnResult(String isSigned) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("score", isSigned);
        return CallResult.success(jsonObject);
    }
}
