package com.chicken.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.*;
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
import java.text.SimpleDateFormat;
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
public class ConsumeController extends BaseController {

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

    @Autowired
    UserInviteService userInviteService;

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

        JSONArray jsonArray = new JSONArray(result);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",jsonArray.toArray());
        jsonObject.put("count",1);
        return CallResult.success(jsonObject);
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
        accountSigned.setParamA((Integer.valueOf(vals[0])-1)*Integer.valueOf(vals[1]));
        accountSigned.setParamB(Integer.valueOf(vals[1]));
        List<AccountSigned> signeds = this.accountSignedService.selectByAccountSigned(accountSigned);
        if (signeds.size() > 0) {
            for (AccountSigned s : signeds) {
                ConsumeRquest consumeRquest = new ConsumeRquest();
                consumeRquest.setOperateTime(DateUtil.currentYYYYMMDDHHmmssWithSymbol(s.getSignedTime()));
                consumeRquest.setCreateTime(s.getSignedTime());
                consumeRquest.setRemark(s.getRemark());
                consumeRquest.setScore(s.getScore().intValue()+"");
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
        accountHit.setParamA((Integer.valueOf(vals[0])-1)*Integer.valueOf(vals[1]));
        accountHit.setParamB(Integer.valueOf(vals[1]));
        List<AccountHit> signeds = this.accountHitService.selectByAccountHitPage(accountHit);
        if (signeds.size() > 0) {
            for (AccountHit s : signeds) {
                ConsumeRquest consumeRquest = new ConsumeRquest();
                consumeRquest.setCreateTime(s.getSignedTime());
                consumeRquest.setOperateTime(DateUtil.currentYYYYMMDDHHmmssWithSymbol(s.getSignedTime()));
                consumeRquest.setRemark(s.getRemark());
                consumeRquest.setScore(s.getScore().intValue()+"");
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

        try {
            String[] vals = getPageNumAndPageSize(request);
            AccountDetail accountDetail = new AccountDetail();
            accountDetail.setUserId(userId);
            accountDetail.setParamA((Integer.valueOf(vals[0])-1)*Integer.valueOf(vals[1]));
            accountDetail.setParamB(Integer.valueOf(vals[1]));
            List<Map> signeds = this.accountDetailService.selectByAccountDetailPage(accountDetail);
            if (signeds.size() > 0) {
                for (Map s : signeds) {
                    String str = null;
                    if (s.get("detail_flag").toString().equals("4")) {
                        str = "兑换商品-"+s.get("good_name")+"";
                    } else {
                        str = s.get("remark").toString();
                    }
                    String[] a = s.get("score").toString().split("\\.");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ConsumeRquest consumeRquest = new ConsumeRquest();
                    consumeRquest.setCreateTime(format.parse(s.get("create_time").toString()));
                    consumeRquest.setOperateTime(DateUtil.currentYYYYMMDDHHmmssWithSymbol(s.get("create_time").toString()));
                    consumeRquest.setRemark(str);
                    consumeRquest.setScore(a[0]);
                    consumeRquest.setType(s.get("detail_type").toString());
                    consumeRquestList.add(consumeRquest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        //查询好友信息
        Integer pageNum = Integer.valueOf(userRequest.getCurrentPage());
        Integer pageSize = pageNum * 10;

        if (pageNum == 1) {
            pageNum = 0;
        } else {
            pageNum = (pageNum - 1) * 10;
        }

        JSONObject jsonObject = new JSONObject();

        //查询揍过我的鸡
        AccountHit accountHit = new AccountHit();
        accountHit.setHitUserId(Integer.valueOf(userRequest.getUserId()));
        PageInfo<Map> lists = this.accountHitService.selectHitUserName(accountHit, pageNum, pageSize);
        JSONArray byHitChicken = new JSONArray();
        if (lists.getList().size() > 0) {
            for (Map m : lists.getList()) {
                String hitOpenId = m.get("hit_openid").toString();
                if (hitOpenId.equals(userRequest.getOpenid())) {
                    continue;
                }
                JSONObject obj = new JSONObject();
                obj.put("nickName", m.get("hit_nick_name"));
                obj.put("avatar", m.get("hit_avatar"));
                obj.put("openid", hitOpenId);
                obj.put("score",m.get("balance").toString());
                obj.put("rank",Long.valueOf(redisService.rank(ContantUtil.USER_RANKING_LIST, m.get("id").toString())) + 1);
                byHitChicken.add(obj);
            }
        }

        jsonObject.put("byHitChicken", byHitChicken);


        //查询用户5个
        JSONArray userInfo = new JSONArray();
        List<Map> userPageInfo = this.wechatUserService.selectTopFive();
        if (userPageInfo.size() > 0) {
            for (Map m : userPageInfo) {
                String openid  = m.get("openid").toString();
                if (openid.equals(userRequest.getOpenid())) {
                    continue;
                }
                JSONObject obj = new JSONObject();
                obj.put("nickName", StringUtils.isBlank(m.get("nick_name")+"")?"":m.get("nick_name")+"");
                obj.put("avatar", StringUtils.isBlank(m.get("avatar")+"")?"":m.get("avatar")+"");
                obj.put("openid", openid);
                obj.put("score",m.get("balance").toString());
                obj.put("rank",Long.valueOf(redisService.rank(ContantUtil.USER_RANKING_LIST, m.get("id").toString())) + 1);
                userInfo.add(obj);
            }
        }

        jsonObject.put("userInfo", userInfo);

        //查询邀请用户
        UserInvite userInvite = new UserInvite();
        userInvite.setStatus("1");
        userInvite.setUserId(Integer.valueOf(userRequest.getUserId()));
        lists = this.userInviteService.selectHitUserName(userInvite, pageNum, pageSize);
        JSONArray friend = new JSONArray();
        if (lists.getList().size() > 0) {
            for (Map m : lists.getList()) {
                JSONObject obj = new JSONObject();
                obj.put("nickName", m.get("invite_nick_name"));
                obj.put("avatar", m.get("invite_avatar"));
                obj.put("openid", m.get("invite_openid"));
                obj.put("score",m.get("balance").toString());
                obj.put("rank",Long.valueOf(redisService.rank(ContantUtil.USER_RANKING_LIST, m.get("invite_user_id").toString())) + 1);
                friend.add(obj);
            }
        }
        jsonObject.put("friend", friend);
        return CallResult.success(jsonObject);

    }
}
