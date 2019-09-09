package com.chicken.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.CodeEnum;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.DateUtil;
import com.chicken.api.vo.UserRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Set;

/**
 * @author zhanglei
 * @date 2019-09-09 20:00
 */
@RestController
@RequestMapping("/mp")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RedisService redisService;

    /**
     * 获取大力丸排行榜
     *
     * @return
     */
    @RequestMapping(value = "/integralTopTen", method = RequestMethod.GET)
    @ResponseBody
    public Object integralTopTen() {

        Set<Object> userList = redisService.revRange(ContantUtil.USER_LIST, 0, 9);
        Iterator<Object> it = userList.iterator();
        JSONArray jsonArray = new JSONArray();
        while (it.hasNext()) {
            String str = it.next().toString();
            JSONObject jsonObject = new JSONObject();
            Object userInfo = redisService.get(ContantUtil.USER_INFO.concat(str));
            if (null != userInfo) {
                JSONObject json = JSON.parseObject(userInfo.toString());
                jsonObject.put("nickName", json.getString("nickName"));
                jsonObject.put("score", redisService.score(ContantUtil.USER_LIST, str));
                jsonArray.add(jsonObject);
            }
        }

        return CallResult.success(jsonArray.toArray());
    }


    /**
     * 获取今天已经获得积分数量
     *
     * @return
     */
    @RequestMapping(value = "/integralCount", method = RequestMethod.POST)
    @ResponseBody
    public Object integralCount(@RequestBody UserRequest request) {

        if (StringUtils.isBlank(request.getUserId())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        //当期那时间
        String now = DateUtil.getSpecifiedDay("yyyy-MM-dd", 0);

        Object gainScoreObj = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(request.getUserId()));
        Double gainScore = 0.0;
        if (null != gainScoreObj) {
            gainScore = Double.valueOf(gainScoreObj.toString());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("score", gainScore);

        return CallResult.success(jsonObject);
    }


}
