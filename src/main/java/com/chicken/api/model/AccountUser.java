package com.chicken.api.model;

import java.util.Date;

public class AccountUser {
    private Integer id;

    private Integer userId;

    private Double attentCount;

    private Double consumeCount;

    private Double balance;

    private String status;

    private Date createTime;

    private Integer goodsCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAttentCount() {
        return attentCount;
    }

    public void setAttentCount(Double attentCount) {
        this.attentCount = attentCount;
    }

    public Double getConsumeCount() {
        return consumeCount;
    }

    public void setConsumeCount(Double consumeCount) {
        this.consumeCount = consumeCount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }
}