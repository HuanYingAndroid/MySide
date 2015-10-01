package com.example.administrator.myside.im;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myside.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

/**
 * Created by Administrator on 2015/9/12.
 * 创建父activity
 */
@EActivity(R.layout.activity_main)
@Fullscreen
public class BaseActivity extends Activity {
    public static Dialog loadingDialog;
    public static boolean isLoading=false;
    private static TextView dialogTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ExitApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(!isAvilable())
        {
            Toast.makeText(this,R.string.notAvailable,Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 加载对话框
     * @param resId 显示的文字的资源ID
     * @param context
     */
    public static void createDialog(int resId,Context context)
    {
        isLoading=true;
        if(loadingDialog !=null)
        {
            loadingDialog.dismiss();
            isLoading=false;
        }
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.activity_progress_layout, null);
        dialogTitle=(TextView)v.findViewById(R.id.tipTextView);
        dialogTitle.setText(resId);

        loadingDialog=new Dialog(context,R.style.loading_dialog);
        loadingDialog.setCancelable(true);
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadingDialog.show();
    }

    /**
     * 更改对话框标题
     * @param resId
     */
    public  static  void changeTitle(int resId,Context context)
    {
        if(loadingDialog!=null || isLoading)
        {
            dialogTitle.setText(resId);
        }else
        {
            createDialog(resId,context);
        }
    }
    /**
     * 关闭对话框
     */
    public static void closeDialog()
    {
        if(loadingDialog!=null || isLoading)
        {
            loadingDialog.dismiss();
        }
    }

    public boolean isAvilable()
    {
        ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if(info!=null && info.isAvailable())
        {
            return true;
        }else
        {
            return false;
        }
    }

}
