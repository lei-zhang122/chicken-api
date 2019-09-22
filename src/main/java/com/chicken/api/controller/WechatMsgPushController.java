package com.chicken.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.CodeEnum;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.WechatUtil;
import com.chicken.api.vo.UserRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 保存fromId
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveFromId", method = RequestMethod.POST)
    @ResponseBody
    public Object saveFromId(@RequestBody UserRequest request) {

        if (StringUtils.isBlank(request.getOpenid()) || StringUtils.isBlank(request.getFormId())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        String[] formids = request.getFormId().split(",");
        for (int i = 0; i < formids.length; i++) {
            redisService.leftPush(ContantUtil.FROMID_INFO.concat(request.getOpenid()), formids[i]);
        }

        return CallResult.success();
    }


    /**
     * 发送push
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/pushNotice", method = RequestMethod.POST)
    @ResponseBody
    public Object pushNotice(@RequestBody UserRequest request) {

        if (StringUtils.isBlank(request.getOpenid()) || StringUtils.isBlank(request.getContent())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        //获取FormId
        Object formId = redisService.rightPop(ContantUtil.FROMID_INFO.concat(request.getOpenid()));
        if (null == formId) {
            return CallResult.fail(CodeEnum.FORMID_IS_NULL.getCode(), CodeEnum.FORMID_IS_NULL.getMsg());
        }

        logger.info("pushNoticeUtil方法开始，传入参数：openid={},formId={}", request.getOpenid(), request.getFormId());
        if (!redisService.pushNoticeUtil(request.getOpenid(), formId.toString(), request.getTitle(), request.getContent())) {
            return CallResult.fail(CodeEnum.PUSH_FAIL.getCode(), CodeEnum.PUSH_FAIL.getMsg());
        }

        return CallResult.success();
    }


}
