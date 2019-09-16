package com.chicken.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanglei
 * @date 2019-09-16 20:40
 */
@RestController
@RequestMapping("/mp")
public class WechatMsgPushController {

    @Autowired
    RedisService redisService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 功能描述
     *
     * @param openId  小程序openId
     * @param formId  小程序formId
     * @param title   通知标题
     * @param content 通知内容
     * @return boolean
     * @author youzi
     * @date 2018/12/14
     */
    private boolean pushNoticeUtil(String openId, String formId, String title, String content) {
        logger.info("pushNoticeUtil方法开始");
        //缓存access_token
        Object ac = redisService.get(ContantUtil.ACCESS_TOKEN);
        String access_token = "";
        if (null == ac) {
            JSONObject jsonObject = WechatUtil.getAccessToken();
            if (jsonObject.get("expires_in") != null && jsonObject.get("expires_in").toString() != ""
                    && Integer.parseInt(jsonObject.get("expires_in").toString()) == 7200) {
                redisService.setByTime(ContantUtil.ACCESS_TOKEN, jsonObject.get("access_token"), 2, TimeUnit.HOURS);
                access_token = jsonObject.getString("access_token");
            }
        } else {
            access_token = ac.toString();
        }

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("touser", openId);
        // DINING_TEMPLATE 模板Id  微信公众平台添加模板时生成的ID
        jsonObject1.put("template_id", ContantUtil.DINING_TEMPLATE);
        jsonObject1.put("form_id", formId);
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("value", title);
        jsonObject2.put("keyword1", jsonObject3);
        jsonObject3 = new JSONObject();
        jsonObject3.put("value", content);
        jsonObject2.put("keyword2", jsonObject3);
        jsonObject1.put("data", jsonObject2);
        boolean pushResult = WechatUtil.setPush(jsonObject1.toString(), access_token);
        logger.info("pushNoticeUtil方法结束：推送结果" + pushResult);
        return pushResult;
    }
}
