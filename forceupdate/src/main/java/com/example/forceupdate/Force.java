package com.example.forceupdate;

public class Force {
    private String appId;
    private int appCode;
    private Flag flag;
    private String title;
    private String message;
    private String updateLink;

    public enum Flag{
        NOT_SHOW,SHOW_CANCELABLE,SHOW_NOT_CANCELABLE
    }

    public Force(String appId, int appCode, Flag flag, String title, String message, String updateLink) {
        this.appId = appId;
        this.appCode = appCode;
        this.flag = flag;
        this.title = title;
        this.message = message;
        this.updateLink = updateLink;
    }

    public Force() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getAppCode() {
        return appCode;
    }

    public void setAppCode(int appCode) {
        this.appCode = appCode;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUpdateLink() {
        return updateLink;
    }

    public void setUpdateLink(String updateLink) {
        this.updateLink = updateLink;
    }
}
