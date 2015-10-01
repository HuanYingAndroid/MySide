package com.example.administrator.myside.im;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.myside.R;
import com.example.administrator.myside.constants.GlobalConstants;
import com.example.administrator.myside.table.User;
import com.example.administrator.myside.utils.MatchUtil;
import com.example.administrator.myside.utils.NetUtil;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Administrator on 2015/9/9.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    @ViewById(R.id.mailwrapper)
    TextInputLayout mailwrapper;
    @ViewById(R.id.userwrapper)
    TextInputLayout userwrapper;
    @ViewById(R.id.pwdwrapper)
    TextInputLayout pwdwrapper;
    @ViewById(R.id.pwdagainwrapper)
    TextInputLayout pwdagainwrapper;
    @ViewById(R.id.characterwrapper)
    TextInputLayout characterwrapper;

    private boolean isFind=true;
    private String email;
    private String username;
    private String pwd;
    private String pwdagain;
    private String character;
    private  Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if(msg.what==7)
            {
                closeDialog();
                userwrapper.setError("用户已存在，换一个名字试试");
            }else if(msg.what==8)
            {
                changeTitle(R.string.isRegister, RegisterActivity.this);
                save(email, username, pwd,character);
            } else if (msg.what == 9) {
                closeDialog();
                userwrapper.setError(result);
            }else if(msg.what==10)
            {
                if(result!=null && !result.equals(""))
                {
                    GlobalConstants.currentUser.setToken(result);
                    GlobalConstants.currentUser.save(RegisterActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            BaseActivity.closeDialog();
                            Toast.makeText(RegisterActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity_.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.scale_enter, R.anim.scale_exit);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            BaseActivity.closeDialog();
                            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                            isFind=true;
                        }
                    });
                } else
                {
                    BaseActivity.closeDialog();
                    Toast.makeText(RegisterActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
                    isFind=true;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    /**
     * 注册
     * @param v
     */
    public void register(View v) {
        email = mailwrapper.getEditText().getText().toString();
        username = userwrapper.getEditText().getText().toString();
        pwd = pwdwrapper.getEditText().getText().toString();
        pwdagain = pwdagainwrapper.getEditText().getText().toString();
        character=characterwrapper.getEditText().getText().toString();
        handleRegister(email, username, pwd, pwdagain,character);
    }

    /**
     * 处理注册
     * @param username
     * @param pwd
     * @param pwdagain
     */
    private void handleRegister(String email, String username, String pwd, String pwdagain,String character) {
        mailwrapper.setErrorEnabled(true);
        userwrapper.setErrorEnabled(true);
        pwdagainwrapper.setErrorEnabled(true);
        pwdagainwrapper.setErrorEnabled(true);
        characterwrapper.setErrorEnabled(true);
        if(email==null || email.trim().equals(""))
        {
            mailwrapper.setError("亲,留个邮箱呗!!!");
        }else if(!MatchUtil.regexMatch(GlobalConstants.mailRegex, email))
        {
            mailwrapper.setError("您的邮箱不太对劲哦-_-");
        }else if(username==null || username.trim().equals(""))
        {
            mailwrapper.setError("");
            userwrapper.setError("名字还是要取一个吧......");
        }else if(username.contains(" "))
        {
            userwrapper.setError("名字不能有空格，你可以试试下划线");
        }else if(username.length()>10)
        {
            userwrapper.setError("名字太长了，最长不能超过10");
        }else if (pwd==null || pwd.trim().equals(""))
        {
            userwrapper.setError("");
            pwdwrapper.setError("密码都不留，太任性了吧，这样不好.....");
        }else if(pwd.length()>20||pwd.length()<6)
        {
            pwdwrapper.setError("密码长度不对啊，密码为6~20位字母或数字组成");
        }else if(!MatchUtil.regexMatch(GlobalConstants.passwordRegex,pwd))
        {
            pwdwrapper.setError("密码格式不对啊，密码为6~20位字母或数字组成");
        }else if (pwdagain==null || pwdagain.trim().equals(""))
        {
            pwdwrapper.setError("");
            pwdagainwrapper.setError("重要的事情要做两遍，再次输入密码是必须的");
        }else if(!pwdagain.equals(pwd)) {
            pwdagainwrapper.setError("做事还是得靠谱点，两次输入的密码不一致");
        }else if(character==null || character.trim().equals(""))
        {
            pwdagainwrapper.setError("");
            characterwrapper.setError("你好像忘了设置你的个性签名哦");
        }else {
            mailwrapper.setErrorEnabled(false);
            userwrapper.setErrorEnabled(false);
            pwdagainwrapper.setErrorEnabled(false);
            pwdagainwrapper.setErrorEnabled(false);
            characterwrapper.setErrorEnabled(false);
            if(isFind)
            {
                createDialog(R.string.isChecking,this);
                findByName(username);
            }

        }
    }


    /**
     * 根据用户名查找用户
     * @param name
     */
    public void findByName(final String name)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BmobQuery<User> query=new BmobQuery<>();
                query.addWhereEqualTo("name", name);
                query.findObjects(RegisterActivity.this, new FindListener<User>() {
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
     * 用户注册
     * @param name
     * @param password
     */
    public void save(String email,String name,String password,String character)
    {
        GlobalConstants.currentUser.setUserId(name);
        GlobalConstants.currentUser.setName(name);
        GlobalConstants.currentUser.setPortraitUri(GlobalConstants.portraitUri);
        GlobalConstants.currentUser.setEmail(email);
        GlobalConstants.currentUser.setPassword(password);
        GlobalConstants.currentUser.setCharacter(character);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tokenstr = NetUtil.getToken(GlobalConstants.currentUser);
                    handler.sendMessage(handler.obtainMessage(10,tokenstr));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onBackPressed() {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity_.class);
        startActivity(intent);
        overridePendingTransition(R.anim.scale_enter,R.anim.scale_exit);
        super.onBackPressed();
    }

}
