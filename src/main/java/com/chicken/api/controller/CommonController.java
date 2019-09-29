package com.chicken.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.Dictionary;
import com.chicken.api.service.DictionaryService;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.RSAEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhanglei
 * @date 2019-09-22 15:30
 */
@RestController
@RequestMapping("/mp")
public class CommonController {

    @Autowired
    RedisService redisService;

    @Autowired
    DictionaryService dictionaryService;


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


    /**
     * 获取活动规则
     *
     * @return
     */
    @RequestMapping(value = "/activeRules", method = RequestMethod.GET)
    @ResponseBody
    public Object activeRules() {

        Dictionary dictionary = new Dictionary();
        dictionary.setDictType("hdgz");
        dictionary.setStatus("1");
        List<Dictionary> list = this.dictionaryService.selectByDictionary(dictionary);
        JSONArray wanRules = new JSONArray();
        JSONArray exchangeRules = new JSONArray();
        for (Dictionary d : list) {
            String[] content = d.getDictContent().split("@");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", d.getDictName());
            jsonObject.put("content", content);
            if(d.getDifferentFlag().equals("1")){
                wanRules.add(jsonObject);
            }else{
                exchangeRules.add(jsonObject);
            }
        }
        JSONObject result = new JSONObject();
        result.put("wanRules",wanRules);
        result.put("exchangeRules",exchangeRules);
        return CallResult.success(result);
    }
}
