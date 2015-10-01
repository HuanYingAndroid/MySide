package com.example.administrator.myside.constants;

import com.example.administrator.myside.R;
import com.example.administrator.myside.table.User;
import com.example.administrator.myside.utils.UriUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/9.
 */
public class GlobalConstants {
    public static String portraitUri= String.valueOf(UriUtil.getImageUri(R.drawable.user_icon));
    public static User currentUser =new User(false,false);//自己
    public static User friendUser =new User();//朋友
    public static List<User> onlineUserList=new ArrayList<>();//当前用户的在线好友列表
    //正则表达式
    public static String passwordRegex = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]"; //密码必须有字母和数字组合
    public static String mailRegex ="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"; //验证邮箱
}
