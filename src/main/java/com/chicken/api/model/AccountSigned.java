package com.chicken.api.model;

import java.util.Date;

public class AccountSigned {
    private Integer id;

    private Integer detailFlag;

    private String detailType;

    private Double score;

    private Double scoreCount;

    private Date signedTime;

    private String remark;

    private String status;

    private Date createTime;

    private Integer userId;

    private Integer paramA;

    private Integer paramB;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDetailFlag() {
        return detailFlag;
    }

    public void setDetailFlag(Integer detailFlag) {
        this.detailFlag = detailFlag;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType == null ? null : detailType.trim();
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Double scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getSignedTime() {
        return signedTime;
    }

    public void setSignedTime(Date signedTime) {
        this.signedTime = signedTime;
    }

    public Integer getParamA() {
        return paramA;
    }

    public void setParamA(Integer paramA) {
        this.paramA = paramA;
    }

    public Integer getParamB() {
        return paramB;
    }

    public void setParamB(Integer paramB) {
        this.paramB = paramB;
    }
}