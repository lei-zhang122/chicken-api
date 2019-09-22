package com.chicken.api.vo;

/**
 * @author zhanglei
 * @date 2019-09-07 10:32
 */
public class HitChickenRequest {

    private String userId;

    private String score;

    private String hitCreateTime;

    private String openid;

    private String hitOpenid;

    private String hitUserId;

    private String data;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHitCreateTime() {
        return hitCreateTime;
    }

    public void setHitCreateTime(String hitCreateTime) {
        this.hitCreateTime = hitCreateTime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHitOpenid() {
        return hitOpenid;
    }

    public void setHitOpenid(String hitOpenid) {
        this.hitOpenid = hitOpenid;
    }

    public String getHitUserId() {
        return hitUserId;
    }

    public void setHitUserId(String hitUserId) {
        this.hitUserId = hitUserId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HitChickenRequest{" +
                "userId='" + userId + '\'' +
                ", score='" + score + '\'' +
                ", hitCreateTime='" + hitCreateTime + '\'' +
                ", openid='" + openid + '\'' +
                ", hitOpenid='" + hitOpenid + '\'' +
                ", hitUserId='" + hitUserId + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
