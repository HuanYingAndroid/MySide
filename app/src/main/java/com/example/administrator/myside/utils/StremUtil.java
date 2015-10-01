package com.example.administrator.myside.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2015/9/8.
 */
public class StremUtil {

    /**
     * 读取输入流中的数据
     * @param in
     * @return
     */
    public static byte[] read(InputStream in) throws Exception
    {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        while ((len=in.read(buffer))!=-1)
        {
            out.write(buffer,0,len);
        }
        in.close();
        return out.toByteArray();
    }
}
