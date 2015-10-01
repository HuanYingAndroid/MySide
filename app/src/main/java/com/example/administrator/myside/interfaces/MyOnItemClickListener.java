package com.example.administrator.myside.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2015/9/20.
 */
public interface MyOnItemClickListener {

    /**
     * RecycleView的条目点击事件
     * @param view
     * @param position
     */
    public void onItemClick(View view,int position);
}
