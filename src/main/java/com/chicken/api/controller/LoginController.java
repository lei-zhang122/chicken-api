package com.chicken.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.AccountDetail;
import com.chicken.api.model.AccountUser;
import com.chicken.api.model.UserInvite;
import com.chicken.api.model.WechatUser;
import com.chicken.api.service.*;
import com.chicken.api.util.*;
import com.chicken.api.vo.UserRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author zhanglei
 * @date 2019-09-07 10:25
 */
@Controller
@RequestMapping("/mp")
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    AccountUserService accountUserService;

    @Autowired
    UserInviteService userInviteService;

    @Autowired
    RedisService redisService;

    @Autowired
    AccountDetailService accountDetailService;

    /**
     * 用户登录
     * openid = 邀请码
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestBody UserRequest userRequest) {

        if (StringUtils.isBlank(userRequest.getCode())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        //JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(userRequest.getCode());

        String openid = "999";//SessionKeyOpenId.getString("openid");

        WechatUser user = this.wechatUserService.selectByOpenId(openid);
        //插入到用户表
        if (null == user) {
            //插入记录
            WechatUser wechatUser = new WechatUser();
            wechatUser.setCreateTime(new Date());
            wechatUser.setOpenid(openid);
            wechatUser.setStatus("1");
            wechatUser.setRegSource(userRequest.getRegSource());
            wechatUser.setInviteNum(userRequest.getOpenid());
            wechatUserService.insert(wechatUser);
            logger.info("注册用户，插入到用户表，用户id={}，openid={}，邀请码={}，code={}", wechatUser.getId(), openid, userRequest.getOpenid(), userRequest.getCode());

            //插入到邀请表
            if (StringUtils.isNotBlank(userRequest.getOpenid())) {

                //根据openid查询用户id
                Object obj = redisService.get(ContantUtil.OPEN_ID.concat(userRequest.getOpenid()));
                if (null != obj) {
                    UserInvite userInvite = new UserInvite();
                    userInvite.setUserId(Integer.valueOf(obj.toString()));
                    userInvite.setInviteStatus("1");
                    userInvite.setInviteTime(new Date());
                    userInvite.setInviteUserId(wechatUser.getId());
                    userInvite.setCreateTime(new Date());
                    userInvite.setStatus("1");
                    userInviteService.insert(userInvite);
                    logger.info("注册用户，插入到邀请表，用户id={},被邀请用户id={}", obj.toString(), wechatUser.getId());

                    //插入到好友集合
                    redisService.setSortSet(ContantUtil.FRIEND_RANKING_LIST.concat(userRequest.getOpenid()), wechatUser.getId().toString(), 0.0);

                    //保存自己属于哪个集合
                    redisService.set(ContantUtil.USER_OWNER_SET.concat(openid),ContantUtil.FRIEND_RANKING_LIST.concat(userRequest.getOpenid()));


                    String now = DateUtil.getSpecifiedDay("yyyy-MM-dd", 0);
                    //查询今天已经邀请总数
                    Object inviteCount = redisService.get(ContantUtil.USER_INVITE_COUNT.concat(now).concat(":").concat(user.getOpenid()));
                    //查询邀请的分
                    Object inviteScore = redisService.get(ContantUtil.INVITE_SCORE);
                    if (null == inviteCount) {
                        //插入到明细
                        insetDetail(Double.valueOf(inviteScore.toString()), Integer.valueOf(obj.toString()));
                        //跟新邀请次数
                        redisService.increment(ContantUtil.USER_INVITE_COUNT.concat(now).concat(":").concat(user.getOpenid()), 1);
                    } else {
                        //查询可邀请总数
                        Object inviteOneDay = redisService.get(ContantUtil.USER_INVITE_ONE_DAY);
                        //如果可邀请数大于已邀请数
                        if (Integer.valueOf(inviteCount.toString()) < Integer.valueOf(inviteOneDay.toString())) {
                            //插入到明细
                            insetDetail(Double.valueOf(inviteScore.toString()), Integer.valueOf(obj.toString()));
                            //跟新邀请次数
                            redisService.increment(ContantUtil.USER_INVITE_COUNT.concat(now).concat(":").concat(user.getOpenid()), 1);
                        }
                    }
                }
            }

            //插入到账户表
            AccountUser accountUser = new AccountUser();
            accountUser.setUserId(wechatUser.getId());
            accountUser.setStatus("1");
            accountUser.setConsumeCount(0.0);
            accountUser.setBalance(0.0);
            accountUser.setAttentCount(0.0);
            accountUser.setCreateTime(new Date());
            accountUserService.insert(accountUser);
            logger.info("注册用户，插入到账户表，用户id={}", wechatUser.getId());


            //插入到排行榜集合
            redisService.setSortSet(ContantUtil.USER_RANKING_LIST, wechatUser.getId().toString(), 0.0);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("inviteNum", wechatUser.getInviteNum());
            jsonObject.put("nickName", wechatUser.getNickName());
            jsonObject.put("openid", wechatUser.getOpenid());
            return returnResult(wechatUser.getId(), openid, jsonObject);


        } else {
            //插入到缓存
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickName", user.getNickName());
            jsonObject.put("inviteNum", user.getInviteNum());
            jsonObject.put("openid", user.getOpenid());
            return returnResult(user.getId(), openid, jsonObject);
        }

    }

    private CallResult returnResult(Integer userId, String openid, JSONObject json) {

        redisService.set(ContantUtil.USER_INFO.concat(userId.toString()), json);

        //插入到缓存 openid，id
        redisService.set(ContantUtil.OPEN_ID.concat(openid), userId);

        return CallResult.success(json);
    }

    /**
     * 插入明细，更新数据
     *
     * @param score
     * @param userId
     */
    @Async
    public void insetDetail(Double score, Integer userId) {

        //更新账户信息
        AccountUser accountUser = this.accountUserService.selectByPrimaryKey(userId);
        accountUser.setAttentCount(accountUser.getAttentCount() + score);
        accountUser.setBalance(accountUser.getBalance() + score);
        accountUserService.updateByPrimaryKey(accountUser);
        logger.info("用户揍小鸡，用户id{}，用户打卡得分{}", userId, score);

        //修改排行榜分值
        redisService.incrScore(ContantUtil.USER_RANKING_LIST, accountUser.getUserId().toString(), score);

        //记录明细
        AccountDetail signed = new AccountDetail();
        signed.setUserId(userId);
        signed.setCreateTime(new Date());
        signed.setDetailFlag(3);
        signed.setStatus("1");
        signed.setDetailType("邀请好友");
        signed.setScoreCount(accountUser.getBalance());
        signed.setScore(score);
        signed.setRemark("用户邀请好友");
        this.accountDetailService.insert(signed);
        logger.info("邀请好友，用户id{},获得积分{},插入到流水表", userId, score);
    }


    /**
     * 修改用户昵称
     *
     * @return
     */
    @RequestMapping(value = "/updateNickName", method = RequestMethod.POST)
    @ResponseBody
    public Object updateNickName(@RequestBody UserRequest userRequest) {

        if (StringUtils.isBlank(userRequest.getNickName()) || StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        WechatUser wechatUser = this.wechatUserService.selectByOpenId(userRequest.getOpenid());
        if(null != wechatUser){
            wechatUser.setNickName(userRequest.getNickName());
            if(StringUtils.isNotBlank(userRequest.getAvatar())){
                wechatUser.setAvatar(userRequest.getAvatar());
            }
            wechatUserService.updateByPrimaryKey(wechatUser);
            logger.info("修改昵称，用户id={},修改昵称为={}",wechatUser.getId(),wechatUser.getNickName());

            //修改用户信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("openid", wechatUser.getOpenid());
            jsonObject.put("inviteNum", wechatUser.getInviteNum());
            jsonObject.put("nickName", wechatUser.getNickName());
            jsonObject.put("avatar", wechatUser.getAvatar());
            redisService.set(ContantUtil.USER_INFO.concat(wechatUser.getId().toString()), jsonObject);
            return CallResult.success();
        }

        return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(),CodeEnum.NO_FIND_USER.getMsg());
    }

    /**
     * 修改用户昵称
     *
     * @return
     */
    @RequestMapping(value = "/updateAvatar", method = RequestMethod.POST)
    @ResponseBody
    public Object updateAvatar(@RequestBody UserRequest userRequest) {

        if (StringUtils.isBlank(userRequest.getAvatar()) || StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        WechatUser wechatUser = this.wechatUserService.selectByOpenId(userRequest.getOpenid());
        if(null != wechatUser){
            wechatUser.setAvatar(userRequest.getAvatar());
            wechatUserService.updateByPrimaryKey(wechatUser);
            logger.info("修改头像，用户id={},修改头像为={}",wechatUser.getId(),wechatUser.getAvatar());

            //修改用户信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("openid", wechatUser.getOpenid());
            jsonObject.put("nickName", wechatUser.getNickName());
            jsonObject.put("avatar", wechatUser.getAvatar());
            jsonObject.put("inviteNum", wechatUser.getInviteNum());
            redisService.set(ContantUtil.USER_INFO.concat(wechatUser.getId().toString()), jsonObject);
            return CallResult.success();
        }

        return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(),CodeEnum.NO_FIND_USER.getMsg());
    }
}
