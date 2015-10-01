package com.example.administrator.myside.utils;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Administrator on 2015/9/8.
 */
public class JsonParseUtil {

    /**
     * 解析获取到的token json数据
     * @return
     */
    public static String ParseTokenJson(InputStream in)throws Exception{
        String json=new String(StremUtil.read(in));
        JSONObject object=new JSONObject(json);
        return object.getString("token");
    }
}
