package com.chicken.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.service.AccountUserService;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.CodeEnum;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.vo.UserRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhanglei
 * @date 2019-09-12 15:31
 */
@RestController
@RequestMapping("/mp")
public class UserIndexController extends BaseController {

    @Autowired
    AccountUserService accountUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    HttpServletRequest request;

    /**
     * 首页信息
     *
     * @param userRequest
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public Object index(@RequestBody UserRequest userRequest) {

        String sessionId = request.getHeader("sessionId");
        if (!isLogin(sessionId)) {
            return CallResult.fail(CodeEnum.LOGIN_OUT_TIME.getCode(), CodeEnum.LOGIN_OUT_TIME.getMsg());
        }

        if (StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }


        //根据openid查询用户id
        Object obj = redisService.get(ContantUtil.OPEN_ID.concat(userRequest.getOpenid()));
        if (null == obj) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        }

        /*//查询当前积分
        AccountUser accountUser = accountUserService.selectByPrimaryKey(Integer.valueOf(obj.toString()));
        if(null == accountUser){
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        }*/

        JSONObject result = new JSONObject();
        result.put("score", redisService.score(ContantUtil.USER_RANKING_LIST, obj.toString()));

        //查询当前排行
        result.put("ranking", Long.valueOf(redisService.rank(ContantUtil.USER_RANKING_LIST, obj.toString())) + 1);

        //查询用户信息
        Object userInfojson = redisService.get(ContantUtil.USER_INFO.concat(obj.toString()));
        if (null != userInfojson) {
            JSONObject userInfo = JSON.parseObject(userInfojson.toString());
            result.put("nickName", userInfo.getString("nickName"));
        }

        //获取用户已经签到多少天
        Object total = redisService.get(ContantUtil.TOTAL_KEY.concat(obj.toString()));
        if (null == total) {
            result.put("signedDays", 0);
        } else {
            result.put("signedDays", total);
        }
        return CallResult.success(result);
    }


    /**
     * 获取我的好友排行榜
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/myFriendRanking", method = RequestMethod.POST)
    @ResponseBody
    public Object myFriendRanking(@RequestBody UserRequest request) {

        if (StringUtils.isBlank(request.getOpenid()) || StringUtils.isBlank(request.getCurrentPage()) || StringUtils.isBlank(request.getType())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Integer pageNum = Integer.valueOf(request.getCurrentPage());
        Integer pageSize = pageNum * 10;

        if (pageNum == 1) {
            pageNum = 0;
        } else {
            pageNum = (pageNum - 1) * 10;
        }

        JSONObject result = new JSONObject();

        if (request.getType().equals("1")) {

            //获得好友排行榜
            Set<Object> a = redisService.revRange(ContantUtil.FRIEND_RANKING_LIST.concat(request.getOpenid()), pageNum, pageSize);
            Iterator<Object> it = a.iterator();
            JSONArray jsonArray = new JSONArray();
            while (it.hasNext()) {
                String str = it.next().toString();
                JSONObject jsonObject = new JSONObject();
                Object userInfo = redisService.get(ContantUtil.USER_INFO.concat(str));
                if (null != userInfo) {
                    JSONObject json = JSON.parseObject(userInfo.toString());
                    jsonObject.put("score", redisService.score(ContantUtil.FRIEND_RANKING_LIST.concat(request.getOpenid()), str));
                    jsonObject.put("nickName", json.getString("nickName"));
                    jsonObject.put("openid", json.getString("openid"));
                    jsonObject.put("rank", Long.valueOf(redisService.rank(ContantUtil.FRIEND_RANKING_LIST.concat(request.getOpenid()), str)) + 1);
                    jsonObject.put("avatar", json.getString("avatar"));
                    jsonArray.add(jsonObject);
                }
                result.put("friendRanking", jsonArray.toArray());
            }
        } else if (request.getType().equals("2")) {

            //获得总排行榜
            Set<Object> userList = redisService.revRange(ContantUtil.USER_RANKING_LIST, pageNum, pageSize);
            Iterator<Object> it = userList.iterator();
            JSONArray count = new JSONArray();
            while (it.hasNext()) {
                String str = it.next().toString();
                JSONObject jsonObject = new JSONObject();
                Object userInfo = redisService.get(ContantUtil.USER_INFO.concat(str));
                if (null != userInfo) {
                    JSONObject json = JSON.parseObject(userInfo.toString());
                    jsonObject.put("avatar", json.getString("avatar"));
                    jsonObject.put("openid", json.getString("openid"));
                    jsonObject.put("score", redisService.score(ContantUtil.USER_RANKING_LIST, str));
                    jsonObject.put("rank", Long.valueOf(redisService.rank(ContantUtil.USER_RANKING_LIST, str)) + 1);
                    jsonObject.put("nickName", json.getString("nickName"));
                    count.add(jsonObject);
                }
            }

            result.put("integralRanking", count.toArray());


        }
        return CallResult.success(result);
    }

    /**
     * 获取7天签到得分
     *
     * @return
     */
    @RequestMapping(value = "/signedScore", method = RequestMethod.GET)
    @ResponseBody
    public Object signedScore() {
        JSONObject jsonObject = new JSONObject();
        Object oneday = redisService.get("d:oneday");
        jsonObject.put("oneday",oneday);
        Object twoday = redisService.get("d:twoday");
        jsonObject.put("twoday",twoday);
        Object threeday = redisService.get("d:threeday");
        jsonObject.put("threeday",threeday);
        Object fourday = redisService.get("d:fourday");
        jsonObject.put("fourday",fourday);
        Object fiveday = redisService.get("d:fiveday");
        jsonObject.put("fiveday",fiveday);
        Object sixday = redisService.get("d:sixday");
        jsonObject.put("sixday",sixday);
        Object sevenday = redisService.get("d:sevenday");
        jsonObject.put("sevenday",sevenday);

        return CallResult.success(jsonObject);
    }

}
