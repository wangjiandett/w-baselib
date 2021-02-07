package com.moa.rxdemo.mvp.bean;

import java.io.Serializable;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2020/9/4 11:10
 */
public class UserInfoResponse implements Serializable {

    // 登录时返回的token
    public String authorization;

    public int id;
    public String type;
    public String organizationId;
    public String teacher;
    public String mobile;
    public String sysUser;
    public String openId;
    public String organizationName;
    public String userId;
    public String nick;
    public String avatar;

    public UserInfoResponse(String authorization) {
        this.authorization = authorization;
    }
}
