package com.example.administrator.myside.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2015/9/20.
 */
public interface MyOnItemLongClickListener {
    /**
     * RecycleView的条目点长按事件
     * @param view
     * @param position
     */
    public void onItemLongClick(View view,int position);
}
