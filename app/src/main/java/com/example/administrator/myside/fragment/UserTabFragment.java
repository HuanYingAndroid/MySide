package com.example.administrator.myside.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.myside.R;
import com.example.administrator.myside.adapter.OnlineUserAdapter;
import com.example.administrator.myside.constants.GlobalConstants;
import com.example.administrator.myside.interfaces.MyOnItemClickListener;
import com.example.administrator.myside.interfaces.MyOnItemLongClickListener;
import com.example.administrator.myside.table.User;
import com.tuesda.walker.circlerefresh.CircleRefreshLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.BackgroundExecutor;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


/**
 * Created by Administrator on 2015/9/22.
 */
@EFragment(R.layout.activity_usertab_fragement)
public class UserTabFragment extends Fragment implements MyOnItemClickListener,MyOnItemLongClickListener {
    @ViewById(R.id.refresh_layout)
    CircleRefreshLayout refresh_layout;
    @ViewById(R.id.onlineView)
    RecyclerView onlineView;
    @ViewById(R.id.usertab)
    TabLayout usertab_layout;

    private List<User> userList=new ArrayList<>();
    private OnlineUserAdapter adapter;

    @AfterViews
    public void initViews() {
        initRefresh();
        initRecycleView();
        initTabLayout();
    }

    /**
     * 初始化onlineView
     */
    public void initRecycleView()
    {
        userList= GlobalConstants.onlineUserList;
        onlineView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new OnlineUserAdapter(userList,R.layout.activity_useritem);
        onlineView.setAdapter(adapter);
        onlineView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemCilckListener(this);
        adapter.setOnItemLongCilckListener(this);
    }

    /**
     * 初始化refresh_layout
     */

    public void initRefresh()
    {
        refresh_layout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
            @Override
            public void completeRefresh() {
                Toast.makeText(getActivity(), R.string.refresh_success, Toast.LENGTH_SHORT).show();
                BackgroundExecutor.cancelAll("getOnlineUsers", true);
            }

            @Override
            public void refreshing() {
                getOnlineUsers();
                Toast.makeText(getActivity(), R.string.isrefreshing, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化TabLayout
     */
    public void initTabLayout()
    {
        usertab_layout.addTab(usertab_layout.newTab().setText("消息列表"));
        usertab_layout.addTab(usertab_layout.newTab().setText("好友列表"),true);
        usertab_layout.addTab(usertab_layout.newTab().setText("同城好友"));
        usertab_layout.addTab(usertab_layout.newTab().setText("好友列表"));
        usertab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 在主线程中实现刷新
     * @param datalist
     */
    @UiThread(delay = 1500)
    public void refresh(List<User> datalist)
    {
        userList=datalist;
        adapter.refresh(datalist);
        refresh_layout.finishRefreshing();
    }

    /**
     * 获取所有在线好友
     */
    @Background(id = "getOnlineUsers")
    public void getOnlineUsers()
    {
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("isOnLine", true);
        query.addWhereNotEqualTo("name", GlobalConstants.currentUser.getName());
        query.findObjects(getActivity(), new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                refresh(list);
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        GlobalConstants.friendUser=userList.get(position);
        if(RongIM.getInstance()!=null)
        {
            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                @Override
                public UserInfo getUserInfo(String s) {
                    return new UserInfo(s,
                            GlobalConstants.currentUser.getName(),
                            Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png"));
                }
            },true);
            RongIM.getInstance().startPrivateChat(getActivity(),
                    GlobalConstants.friendUser.getUserId(),
                    GlobalConstants.friendUser.getName());//在该方法中采用隐式意图调用ConversationActivity
        }

    }

    @Override
    public void onItemLongClick(View view, int position) {
        GlobalConstants.friendUser=userList.get(position);
        Toast.makeText(getActivity(),GlobalConstants.friendUser.getCharacter(),Toast.LENGTH_SHORT).show();
    }
}
