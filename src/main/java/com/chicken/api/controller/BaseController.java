package com.chicken.api.controller;

import com.chicken.api.service.RedisService;
import com.chicken.api.util.ContantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;

/**
 * @author zhanglei
 * @date 2019-09-15 22:26
 */
public abstract class BaseController extends ApplicationObjectSupport {

    @Autowired
    RedisService redisService;

    public boolean isLogin(String sessionId) {
        Object obj = redisService.get(ContantUtil.IS_LOGIN.concat(sessionId));
        if (null == obj) {
            return false;
        }
        return true;
    }
}
