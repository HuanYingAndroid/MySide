package com.example.administrator.myside.im;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.administrator.myside.R;
import com.example.administrator.myside.constants.GlobalConstants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2015/8/19.
 */
@EActivity(R.layout.activity_conversation)
@Fullscreen
public class ConversationActivity extends AppCompatActivity {
    @ViewById(R.id.con_toolbar)
    Toolbar toolbar;
    @ViewById(R.id.con_title)
    TextView con_title;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @AfterViews
    public void init()
    {
        initToolbar();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("消息");
            con_title.setText(GlobalConstants.friendUser.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
