package com.chicken.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.RSAEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanglei
 * @date 2019-09-22 15:30
 */
@RestController
@RequestMapping("/mp")
public class CommonController {

    @Autowired
    RedisService redisService;


    /**
     * 获取商品列表
     *
     * @return
     */
    @RequestMapping(value = "/commonInterface", method = RequestMethod.GET)
    @ResponseBody
    public Object commonInterface() {

        JSONObject jsonObject = new JSONObject();

        Object hitProbability = this.redisService.get(ContantUtil.HIT_PROBABILY);
        if(null != hitProbability){
            jsonObject.put("hitRate",hitProbability);
        }
        Object gainProbability = this.redisService.get(ContantUtil.GAIN_PROBABILY);
        if(null != gainProbability){
            jsonObject.put("gainRate",gainProbability);
        }
        Object scorelist = this.redisService.get(ContantUtil.SCORE_LIST);
        if(null != hitProbability){
            String[] val = scorelist.toString().split(",");
            jsonObject.put("scoreList",val);
        }

        jsonObject.put("publicKey", RSAEncrypt.PUBLIC_KEY_STRING);

        return CallResult.success(jsonObject);
    }
}
