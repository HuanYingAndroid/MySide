package com.example.administrator.myside.im;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuView;
import com.example.administrator.myside.R;
import com.example.administrator.myside.constants.GlobalConstants;
import com.example.administrator.myside.fragment.UserTabFragment;
import com.example.administrator.myside.fragment.UserTabFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

@EActivity(R.layout.activity_main)
@Fullscreen
public class MainActivity extends AppCompatActivity {
    @ViewById(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.toolbar_title)
    TextView toolbar_title;
    @ViewById(R.id.navigation_button)
    MaterialMenuView navigation_button;
    @ViewById(R.id.navagation)
    NavigationView navagation;
    @ViewById(R.id.navi_username)
    TextView navi_username;
    @ViewById(R.id.navi_state)
    TextView navi_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initServer();
        setDefaultFragment();
    }

    /**
     * 建立服务器的连接
     */
    public void initServer()
    {
        String token=GlobalConstants.currentUser.getToken();
        //建立服务器的连接
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            //Connect Token 失效的状态处理，需要重新获取 Token
            public void onTokenIncorrect() {

            }

            //连接成功
            @Override
            public void onSuccess(String s) {
                Toast.makeText(getApplication(), "登录成功", Toast.LENGTH_SHORT).show();
            }

            //连接失败
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Toast.makeText(getApplication(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置初始Fragment
     */
    public void setDefaultFragment()
    {
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        UserTabFragment userTabFragment=new UserTabFragment_();
        transaction.add(R.id.fragmentframe, userTabFragment, "userTabFragment");
        transaction.commit();
    }


    @AfterViews
    public void initViews() {

        initToolbar();
        initNavigation();
    }

    /**
     * 初始化NavigationView
     */
    public void initNavigation()
    {
        navi_username.setText(GlobalConstants.currentUser.getName());
        navi_state.setText(GlobalConstants.currentUser.isOnLine() ? "在线" : "离线");
        navagation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            private MenuItem mPreMenuItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (mPreMenuItem != null) mPreMenuItem.setChecked(false);
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                mPreMenuItem = menuItem;
                toolbar_title.setText(mPreMenuItem.getTitle());
                return true;
            }
        });
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("");
            navigation_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
