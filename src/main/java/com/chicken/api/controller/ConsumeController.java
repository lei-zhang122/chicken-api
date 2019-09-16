package com.chicken.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.AccountDetail;
import com.chicken.api.model.AccountHit;
import com.chicken.api.model.AccountSigned;
import com.chicken.api.model.WechatUser;
import com.chicken.api.service.*;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.CodeEnum;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.DateUtil;
import com.chicken.api.vo.ConsumeRquest;
import com.chicken.api.vo.UserRequest;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhanglei
 * @date 2019-09-12 16:27
 */
@RestController
@RequestMapping("/mp")
public class ConsumeController extends BaseController{

    @Autowired
    AccountDetailService accountDetailService;

    @Autowired
    AccountHitService accountHitService;

    @Autowired
    AccountSignedService accountSignedService;

    @Autowired
    RedisService redisService;

    @Autowired
    WechatUserService wechatUserService;

    @Autowired
    HttpServletRequest request;

    /**
     * 我的交易信息
     *
     * @param userRequest
     * @return
     */
    @RequestMapping(value = "/consumeList", method = RequestMethod.POST)
    @ResponseBody
    public Object consumeList(@RequestBody UserRequest userRequest) {

        String sessionId = request.getHeader("sessionId");
        if (!isLogin(sessionId)) {
            return CallResult.fail(CodeEnum.LOGIN_OUT_TIME.getCode(), CodeEnum.LOGIN_OUT_TIME.getMsg());
        }

        if (StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Object userId = redisService.get(ContantUtil.OPEN_ID.concat(userRequest.getOpenid()));
        if (null == userId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            userRequest.setUserId(userId.toString());
        }

        //查询打卡明细
        List<ConsumeRquest> consumeRquestList = new ArrayList<>();
        consumeRquestList = selectBySigned(userRequest, Integer.valueOf(userId.toString()), consumeRquestList);

        consumeRquestList = selectByHit(userRequest, Integer.valueOf(userId.toString()), consumeRquestList);

        consumeRquestList = selectByDetail(userRequest, Integer.valueOf(userId.toString()), consumeRquestList);

        List result = consumeRquestList.stream().sorted(Comparator.comparing(ConsumeRquest::getCreateTime).reversed()).collect(Collectors.toList());

        JSONArray jsonArray=new JSONArray(result);
        return CallResult.success(jsonArray.toArray());
    }


    /**
     * 查询打卡记录
     *
     * @param request
     * @param userId
     * @param consumeRquestList
     * @return
     */
    private List<ConsumeRquest> selectBySigned(UserRequest request, Integer userId, List<ConsumeRquest> consumeRquestList) {

        String[] vals = getPageNumAndPageSize(request);
        AccountSigned accountSigned = new AccountSigned();
        accountSigned.setUserId(userId);
        PageInfo<AccountSigned> signeds = this.accountSignedService.selectByAccountSigned(accountSigned, Integer.valueOf(vals[0]), Integer.valueOf(vals[1]));
        if (signeds.getList().size() > 0) {
            for (AccountSigned s : signeds.getList()) {
                ConsumeRquest consumeRquest = new ConsumeRquest();
                consumeRquest.setOperateTime(DateUtil.currentYYYYMMDDHHmmssWithSymbol(s.getSignedTime()));
                consumeRquest.setCreateTime(s.getSignedTime());
                consumeRquest.setRemark(s.getRemark());
                consumeRquest.setScore(s.getScore().toString());
                consumeRquest.setType("打卡");
                consumeRquestList.add(consumeRquest);
            }
        }
        return consumeRquestList;
    }


    /**
     * 查询揍小鸡记录
     *
     * @param request
     * @param userId
     * @param consumeRquestList
     * @return
     */
    private List<ConsumeRquest> selectByHit(UserRequest request, Integer userId, List<ConsumeRquest> consumeRquestList) {

        String[] vals = getPageNumAndPageSize(request);
        AccountHit accountHit = new AccountHit();
        accountHit.setUserId(userId);
        PageInfo<AccountHit> signeds = this.accountHitService.selectByAccountHit(accountHit, Integer.valueOf(vals[0]), Integer.valueOf(vals[1]));
        if (signeds.getList().size() > 0) {
            for (AccountHit s : signeds.getList()) {
                ConsumeRquest consumeRquest = new ConsumeRquest();
                consumeRquest.setCreateTime(s.getSignedTime());
                consumeRquest.setOperateTime(DateUtil.currentYYYYMMDDHHmmssWithSymbol(s.getSignedTime()));
                consumeRquest.setRemark(s.getRemark());
                consumeRquest.setScore(s.getScore().toString());
                consumeRquest.setType("揍小鸡");
                consumeRquestList.add(consumeRquest);
            }
        }
        return consumeRquestList;
    }

    /**
     * 查询其他记录
     *
     * @param request
     * @param userId
     * @param consumeRquestList
     * @return
     */
    private List<ConsumeRquest> selectByDetail(UserRequest request, Integer userId, List<ConsumeRquest> consumeRquestList) {

        String[] vals = getPageNumAndPageSize(request);
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setUserId(userId);
        PageInfo<AccountDetail> signeds = this.accountDetailService.selectByAccountDetail(accountDetail, Integer.valueOf(vals[0]), Integer.valueOf(vals[1]));
        if (signeds.getList().size() > 0) {
            for (AccountDetail s : signeds.getList()) {
                ConsumeRquest consumeRquest = new ConsumeRquest();
                consumeRquest.setCreateTime(s.getCreateTime());
                consumeRquest.setOperateTime(DateUtil.currentYYYYMMDDHHmmssWithSymbol(s.getCreateTime()));
                consumeRquest.setRemark(s.getRemark());
                consumeRquest.setScore(s.getScore().toString());
                consumeRquest.setType(s.getDetailType());
                consumeRquestList.add(consumeRquest);
            }
        }
        return consumeRquestList;
    }

    private String[] getPageNumAndPageSize(UserRequest request) {
        Integer pageNum = ContantUtil.DEFAULT_PAGE_NUM;
        if (null != request.getCurrentPage() && !"0".equals(request.getCurrentPage())) {
            pageNum = Integer.valueOf(request.getCurrentPage());
        }

        Integer pageSize = ContantUtil.DEFAULT_PAGE_SIZE;
        if (null != request.getPageSize() && !"0".equals(request.getPageSize())) {
            pageSize = Integer.valueOf(request.getPageSize());
        }

        String[] val = new String[2];
        val[0] = pageNum + "";
        val[1] = pageSize + "";
        return val;
    }


    /**
     * 揍过的鸡
     *
     * @param userRequest
     * @return
     */
    @RequestMapping(value = "/operateList", method = RequestMethod.POST)
    @ResponseBody
    public Object operateList(@RequestBody UserRequest userRequest) {

        String sessionId = request.getHeader("sessionId");
        if (!isLogin(sessionId)) {
            return CallResult.fail(CodeEnum.LOGIN_OUT_TIME.getCode(), CodeEnum.LOGIN_OUT_TIME.getMsg());
        }

        if (StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Object userId = redisService.get(ContantUtil.OPEN_ID.concat(userRequest.getOpenid()));
        if (null == userId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            userRequest.setUserId(userId.toString());
        }

        JSONObject jsonObject = new JSONObject();

        //查询我揍过的鸡
        AccountHit accountHit = new AccountHit();
        accountHit.setUserId(Integer.valueOf(userRequest.getUserId()));
        PageInfo<Map> lists = this.accountHitService.selectHitUserName(accountHit, ContantUtil.DEFAULT_PAGE_NUM,ContantUtil.DEFAULT_PAGE_SIZE);
        JSONArray hitChicken = new JSONArray();
        if (lists.getList().size() > 0) {
            for(Map m:lists.getList()){
                JSONObject obj = new JSONObject();
                obj.put("nickName",m.get("hit_nick_name"));
                obj.put("avatar",m.get("hit_avatar"));
                hitChicken.add(obj);
            }
        }

        jsonObject.put("hitChicken",hitChicken);

        //查询揍过我的鸡
        accountHit = new AccountHit();
        accountHit.setHitUserId(Integer.valueOf(userRequest.getUserId()));
        lists = this.accountHitService.selectHitUserName(accountHit, ContantUtil.DEFAULT_PAGE_NUM,ContantUtil.DEFAULT_PAGE_SIZE);
        JSONArray byHitChicken = new JSONArray();
        if (lists.getList().size() > 0) {
            for(Map m:lists.getList()){
                JSONObject obj = new JSONObject();
                obj.put("nickName",m.get("nick_name"));
                obj.put("avatar",m.get("avatar"));
                byHitChicken.add(obj);
            }
        }

        jsonObject.put("byHitChicken",byHitChicken);


        //查询用户5个
        JSONArray userInfo = new JSONArray();
        List<WechatUser> userPageInfo = this.wechatUserService.selectTopFive();
        if (userPageInfo.size() > 0) {
            for(WechatUser m:userPageInfo){
                JSONObject obj = new JSONObject();
                obj.put("nickName",m.getNickName());
                obj.put("avatar",m.getAvatar());
                userInfo.add(obj);
            }
        }

        jsonObject.put("userInfo",userInfo);

        return CallResult.success(jsonObject);

    }

}
