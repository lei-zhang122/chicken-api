package com.chicken.api.vo;

/**
 * @author zhanglei
 * @date 2019-09-07 10:32
 */
public class SignedRequest {

    private String userId;

    private String openid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
