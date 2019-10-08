package com.chicken.api.vo;

/**
 * @author zhanglei
 * @date 2019-09-09 20:20
 */
public class UserRequest {

    private String userId;

    private String openid;

    private String code;

    private String invite;

    private String nickName;

    private String currentPage;

    private String pageSize;

    private String avatar;

    private String regSource;

    private String type;

    private String formId;

    private String title;

    private String content;

    private String orderNum;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRegSource() {
        return regSource;
    }

    public void setRegSource(String regSource) {
        this.regSource = regSource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "userId='" + userId + '\'' +
                ", openid='" + openid + '\'' +
                ", code='" + code + '\'' +
                ", invite='" + invite + '\'' +
                ", nickName='" + nickName + '\'' +
                ", currentPage='" + currentPage + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", avatar='" + avatar + '\'' +
                ", regSource='" + regSource + '\'' +
                ", type='" + type + '\'' +
                ", formId='" + formId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", orderNum='" + orderNum + '\'' +
                '}';
    }
}
