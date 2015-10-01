package com.example.administrator.myside.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/10.
 */
public class MatchUtil {

    /**
     * 正则表达式的验证
     * @param regex 正则表达式
     * @param src 待验证的数据
     * @return
     */
    public static boolean regexMatch(String regex, String src)
    {
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(src);
        return  matcher.matches();
    }
}
