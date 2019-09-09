package com.chicken.api.model;

import java.util.Date;

public class Dictionary {
    private Integer id;

    private String dictType;

    private String dictName;

    private String dictContent;

    private Integer dictOrder;

    private String dictDetail;

    private String status;

    private Integer createUser;

    private Date createTime;

    private Date editTime;

    private String differentFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType == null ? null : dictType.trim();
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    public String getDictContent() {
        return dictContent;
    }

    public void setDictContent(String dictContent) {
        this.dictContent = dictContent == null ? null : dictContent.trim();
    }

    public Integer getDictOrder() {
        return dictOrder;
    }

    public void setDictOrder(Integer dictOrder) {
        this.dictOrder = dictOrder;
    }

    public String getDictDetail() {
        return dictDetail;
    }

    public void setDictDetail(String dictDetail) {
        this.dictDetail = dictDetail == null ? null : dictDetail.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getDifferentFlag() {
        return differentFlag;
    }

    public void setDifferentFlag(String differentFlag) {
        this.differentFlag = differentFlag == null ? null : differentFlag.trim();
    }
}