package com.chicken.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.AccountHit;
import com.chicken.api.model.AccountUser;
import com.chicken.api.service.AccountHitService;
import com.chicken.api.service.AccountUserService;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.*;
import com.chicken.api.vo.HitChickenRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zhanglei
 * @date 2019-09-07 12:00
 */
@RestController
@RequestMapping("/mp")
public class HItChickenController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    AccountHitService accountHitService;

    @Autowired
    AccountUserService accountUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    HttpServletRequest request;


    @RequestMapping(value = "/hitChicken", method = RequestMethod.POST)
    @ResponseBody
    public Object hitChicken(@RequestBody HitChickenRequest hitChickenRequest) {

        String sessionId = request.getHeader("sessionId");
        if (!isLogin(sessionId)) {
            return CallResult.fail(CodeEnum.LOGIN_OUT_TIME.getCode(), CodeEnum.LOGIN_OUT_TIME.getMsg());
        }

        /*try {
            String data = RSAEncrypt.decrypt(hitChickenRequest.getData(), RSAEncrypt.PRIVATE_KEY_STRING);
            JSONObject jsonObject = JSON.parseObject(data);
            hitChickenRequest = jsonObject.toJavaObject(HitChickenRequest.class);
            logger.info("揍小鸡，成功解析数据{}", hitChickenRequest.toString());
        } catch (Exception e) {
            return CallResult.fail(CodeEnum.DECRYPT_EXCEPTION.getCode(), CodeEnum.DECRYPT_EXCEPTION.getMsg());
        }*/

        if (StringUtils.isBlank(hitChickenRequest.getOpenid()) || StringUtils.isBlank(hitChickenRequest.getScore()) || StringUtils.isBlank(hitChickenRequest.getHitOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Object userId = redisService.get(ContantUtil.OPEN_ID.concat(hitChickenRequest.getOpenid()));
        if (null == userId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            hitChickenRequest.setUserId(userId.toString());
        }

        Object hitUserId = redisService.get(ContantUtil.OPEN_ID.concat(hitChickenRequest.getHitOpenid()));
        if (null == hitUserId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            hitChickenRequest.setHitUserId(hitUserId.toString());
        }

        //每天最大分值
        Object maxScore = redisService.get(ContantUtil.MAX_SOCRE_DAY);

        //当期那时间
        String now = DateUtil.getSpecifiedDay("yyyy-MM-dd", 0);

        //已经获得的积分
        Object gainScoreObj = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(hitChickenRequest.getUserId()));
        Double gainScore = 0.0;
        if (null != gainScoreObj) {
            gainScore = Double.valueOf(gainScoreObj.toString());
        }
        Double diffValue = Double.valueOf(maxScore.toString()) - gainScore;

        //不再获得积分
        if (diffValue <= 0) {
            Object score = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(hitChickenRequest.getUserId()));
            return returnResult(score.toString());
        }

        //差值大于得分 插入记录
        if (diffValue > Double.valueOf(hitChickenRequest.getScore())) {

            //更新缓存
            redisService.set(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(hitChickenRequest.getUserId()), Double.valueOf(hitChickenRequest.getScore()) + gainScore);

            //插入分值
            insetDetail(Double.valueOf(hitChickenRequest.getScore()), hitChickenRequest.getUserId(), hitChickenRequest.getOpenid(), hitChickenRequest.getHitUserId());
        } else {
            //更新缓存
            redisService.set(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(hitChickenRequest.getUserId()), gainScore + diffValue);

            //插入分值
            insetDetail(diffValue, hitChickenRequest.getUserId(), hitChickenRequest.getOpenid(), hitChickenRequest.getHitUserId());
        }


        gainScoreObj = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(hitChickenRequest.getUserId()));

        return returnResult(gainScoreObj.toString());
    }

    /**
     * 插入明细
     *
     * @param score
     * @param userId
     * @param openid    揍小鸡用户
     * @param hitUserId 被打用户
     */
    public void insetDetail(Double score, String userId, String openid, String hitUserId) {

        //更新账户信息
        AccountUser accountUser = this.accountUserService.selectByUserId(Integer.valueOf(userId));
        accountUser.setAttentCount(accountUser.getAttentCount() + Double.valueOf(score));
        accountUser.setBalance(accountUser.getBalance() + Double.valueOf(score));
        accountUserService.updateByPrimaryKey(accountUser);
        logger.info("用户揍小鸡，用户id{}，用户打卡得分{}", userId, score);

        //修改排行榜分值
        redisService.incrScore(ContantUtil.USER_RANKING_LIST, accountUser.getUserId().toString(), score);

        //修改自己排行榜的分
        redisService.incrScore(ContantUtil.FRIEND_RANKING_LIST.concat(openid), accountUser.getUserId().toString(), score);

        //修改好友排行榜分值
        Object myFriend = redisService.get(ContantUtil.USER_OWNER_SET.concat(openid));
        if (null != myFriend) {
            redisService.incrScore(myFriend.toString(), accountUser.getUserId().toString(), score);
        }


        //插入记录
        insertHitDetail(Integer.valueOf(userId), score, accountUser.getAttentCount(), Integer.valueOf(hitUserId));
    }


    /**
     * 插入到揍小鸡记录
     *
     * @param userId
     * @param score
     * @param count
     */
    public void insertHitDetail(Integer userId, Double score, Double count, Integer hitUserId) {
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
