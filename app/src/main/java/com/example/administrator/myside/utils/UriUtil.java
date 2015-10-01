package com.example.administrator.myside.utils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;

import com.example.administrator.myside.init.APP;

/**
 * Created by Administrator on 2015/9/11.
 */
public class UriUtil {

    /**
     * 获取drawable文件夹下面图片的uri
     * @param resId
     * @return
     */
    public static Uri getImageUri(int resId)
    {
        Resources resources= APP.context.getResources();
        Uri uri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"
                +resources.getResourcePackageName(resId)+"/"
                +resources.getResourceTypeName(resId)+"/"
                +resources.getResourceEntryName(resId));
        return uri;
    }
}
