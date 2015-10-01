package com.example.administrator.myside.init;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015/8/19.
 */
public class APP extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        context=this;
        //初始化融云
        RongIM.init(this);
        //初始化Bmob
        Bmob.initialize(this,"503d27fc9afe5f2599436894a306ffd6");
    }
}
