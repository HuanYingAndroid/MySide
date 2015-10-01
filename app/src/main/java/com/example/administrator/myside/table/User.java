package com.example.administrator.myside.table;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/9/7.
 */
public class User extends BmobObject {
    private String userId;//用户id
    private String name;//用户名称
    private String email;//用户邮箱
    private String password;//密码
    private String character;//个性签名
    private String portraitUri;//用户头像
    private boolean isImageChange;//用户头像是否更改
    private boolean isOnLine;//在线标志
    private String token;

    public User(){
    }

    public User(String name,String character)
    {
        this.name=name;
        this.character = character;
    }

    public User(boolean isImageChange,boolean isOnLine)
    {
        this.isImageChange=isImageChange;
        this.isOnLine=isOnLine;
    }
    public User(String userId,String name,String portraitUri)
    {
        this.userId=userId;
        this.name=name;
        this.portraitUri=portraitUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public boolean isOnLine() {
        return isOnLine;
    }

    public void setIsOnLine(boolean isOnLine) {
        this.isOnLine = isOnLine;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isImageChange() {
        return isImageChange;
    }

    public void setIsImageChange(boolean isImageChange) {
        this.isImageChange = isImageChange;
    }
}
