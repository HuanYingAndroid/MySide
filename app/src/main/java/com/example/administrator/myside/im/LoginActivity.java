package com.example.administrator.myside.im;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myside.R;
import com.example.administrator.myside.constants.GlobalConstants;
import com.example.administrator.myside.sharedpreferences.UserPreferences;
import com.example.administrator.myside.table.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2015/9/8.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewById(R.id.userwrapper)
    TextInputLayout userInput;
    @ViewById(R.id.pwdwrapper)
    TextInputLayout pwdInput;
    @ViewById(R.id.mark_imageView)
    ImageView mark_imageView;
    @ViewById(R.id.mark_layout)
    RelativeLayout mark_layout;
    @ViewById(R.id.login)
    Button login;
    @ViewById(R.id.register)
    TextView register;

    private boolean isFind = false;//查询标志
    private boolean isMark=false;
    private UserPreferences preferences;
    private String username;
    private String pwd;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if(msg.what==7)
            {
                changeTitle(R.string.isLogining,LoginActivity.this);
                getOnlineUsers();
                userInput.setErrorEnabled(false);
            }else if (msg.what == 8) {
                if (!isFind) {
                    closeDialog();
                    userInput.setError("用户没有注册,请先注册");
                }
            }else if (msg.what == 9) {
                closeDialog();
                userInput.setError(result);
            }else if (msg.what == 10) {
                if (isFind && GlobalConstants.currentUser.getToken().length() > 0) {
                    userInput.setErrorEnabled(false);
                    preferences.saveUserName(username, isMark);
                    GlobalConstants.currentUser.setIsOnLine(true);
                    GlobalConstants.currentUser.update(LoginActivity.this, GlobalConstants.currentUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity_.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.scale_enter, R.anim.scale_exit);
                            finish();
                            closeDialog();
                        }
                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }else {
                    closeDialog();
                    pwdInput.setError("密码错误");
                }
            } else if (msg.what == 11) {
                if (!isFind) {
                    closeDialog();
                    pwdInput.setError("密码错误");
                }
            } else if (msg.what == 12) {
                closeDialog();
                userInput.setError(result);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 对用户名进行回显
     * 以及初始化控件
     */
    @AfterViews
    public void setName()
    {
        preferences=new UserPreferences(this);
        Map<String,Object> map=preferences.getPreferences();
        userInput.getEditText().setText(String.valueOf(map.get("username")));
        if((Boolean)map.get("ismark"))
        {
            mark_imageView.setImageResource(R.drawable.mark_pres);
        }else
        {
            mark_imageView.setImageResource(R.drawable.mark);
        }
    }

    /**
     * 处理记住用户名的点击事件
     */
    @Click(R.id.mark_layout)
    public void setMark()
    {
        if(!isMark)
        {
            mark_imageView.setImageResource(R.drawable.mark_pres);
            isMark=true;
        }else
        {
            mark_imageView.setImageResource(R.drawable.mark);
            isMark=false;
        }
    }

    /**
     * 处理注册的点击事件
     */
    @Click(R.id.register)
    public void register()
    {
        if (!isAvilable()) {
            Toast.makeText(this, R.string.notAvailable, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
            startActivity(intent);
            overridePendingTransition(R.anim.scale_enter, R.anim.scale_exit);
            finish();
        }
    }

    /**
     * 处理注册的点击事件
     */
    @Click(R.id.login)
    public void login()
    {
        username = userInput.getEditText().getText().toString();
        pwd = pwdInput.getEditText().getText().toString();
        if (!isAvilable()) {
            Toast.makeText(this, R.string.notAvailable, Toast.LENGTH_SHORT).show();
        } else {
            userInput.setErrorEnabled(true);
            pwdInput.setErrorEnabled(true);
            if (username == null || username.trim().equals("")) {
                userInput.setError("请先输入用户名");
            }else if (pwd == null || pwd.trim().equals("")) {
                pwdInput.setError("请先输入密码");
            }else
            {
                if(!isFind)
                {
                    createDialog(R.string.isChecking,this);
                    findByName(username);
                }
            }
        }
    }



    /**
     * 根据用户名和密码查询用户
     * @param name
     * @param password
     */
    synchronized public void find(final String name,final String password) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("name", name).addWhereEqualTo("password", password);
                query.findObjects(LoginActivity.this, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        if (list.size() == 1) {
                            isFind=true;
                            GlobalConstants.currentUser =list.get(0);
                            handler.sendMessage(handler.obtainMessage(10));
                        }else
                        {
                            isFind=false;
                            handler.sendMessage(handler.obtainMessage(11));
                        }

                    }

                    @Override
                    public void onError(int i, String s) {
                        isFind = false;
                        handler.sendMessage(handler.obtainMessage(12,s));
                    }
                });
            }
        }).start();

    }
    /**
     * 根据用户名查找用户
     * @param name
     */
    synchronized public void findByName(final String name)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<User> query=new BmobQuery<>();
                query.addWhereEqualTo("name", name);
                query.findObjects(LoginActivity.this, new FindListener<User>() {
                    boolean result=false;
                    @Override
                    public void onSuccess(List<User> list) {
                        if(list.size()==1)
                        {
                            isFind=true;
                            handler.sendMessage(handler.obtainMessage(7));
                        }else
                        {
                            isFind=false;
                            handler.sendMessage(handler.obtainMessage(8));
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        isFind=false;
                        handler.sendMessage(handler.obtainMessage(9,s));
                    }
                });
            }
        }).start();
    }

    /**
     * 获取所有在线好友
     */
    public void getOnlineUsers()
    {
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("isOnLine", true);
        query.addWhereNotEqualTo("name",username);
        query.findObjects(LoginActivity.this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                GlobalConstants.onlineUserList = list;
                find(username, pwd);
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
