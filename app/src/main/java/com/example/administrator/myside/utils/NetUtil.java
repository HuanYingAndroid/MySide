package com.example.administrator.myside.utils;

import android.net.Uri;

import com.example.administrator.myside.constants.AppConstants;
import com.example.administrator.myside.table.User;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2015/9/8.
 * 网络工具类
 */
public class NetUtil {
    private static final String APPKEY = "RC-App-Key";
    private static final String NONCE = "RC-Nonce";
    private static final String TIMESTAMP = "RC-Timestamp";
    private static final String SIGNATURE = "RC-Signature";

    /**
     * 创建带有签名的HttpURLConnection
     * @param appKey
     * @param appSecret
     * @param uri
     * @return
     * @throws Exception
     */
    public static HttpURLConnection CreatePostHttpConnection(String appKey, String appSecret, String uri) throws Exception {
        String nonce = String.valueOf(Math.random() * 1000000);//随机数
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        StringBuilder toSign = new StringBuilder(appSecret).append(nonce).append(timestamp);
        String sign = CodeUtil.hexSHA1(toSign.toString());

        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        conn.setRequestProperty(APPKEY, appKey);
        conn.setRequestProperty(NONCE, nonce);
        conn.setRequestProperty(TIMESTAMP, timestamp);
        conn.setRequestProperty(SIGNATURE, sign);
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        return conn;
    }

    /**
     * 根据用户信息获取，用户token
     * 采用post方式向融云发送请求
     * @param user
     * @return
     */
    public static String getToken(User user) throws Exception
    {
        String uri="https://api.cn.ronghub.com/user/getToken.json";
        StringBuilder builder=new StringBuilder();
        builder.append("userId=").append(user.getUserId()).append("&")
                .append("name=").append(user.getName()).append("&")
                .append("portraitUri=").append(user.getPortraitUri());
        byte[] entity=builder.toString().getBytes();
        HttpURLConnection conn=CreatePostHttpConnection(AppConstants.APP_KEY, AppConstants.APP_SECRET, uri);
        conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
        OutputStream out=conn.getOutputStream();
        out.write(entity);
        if(conn.getResponseCode()==200)
        {
            return JsonParseUtil.ParseTokenJson(conn.getInputStream());
        }
        return null;
    }


    /**
     * 根据图片的
     * @param path
     * @return
     */
    public static Uri getImageFileUri(String path) {

        return null;
    }
}
