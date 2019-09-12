package com.chicken.api.vo;

/**
 * @author zhanglei
 * @date 2019-09-07 10:32
 */
public class GoodInfoRequest {

    private String userId;

    private String goodId;

    private String score;

    private String openid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
