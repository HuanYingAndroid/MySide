package com.example.administrator.myside.adapter;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myside.R;
import com.example.administrator.myside.constants.GlobalConstants;
import com.example.administrator.myside.interfaces.MyOnItemClickListener;
import com.example.administrator.myside.interfaces.MyOnItemLongClickListener;
import com.example.administrator.myside.table.User;
import com.example.administrator.myside.utils.NetUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/9/15.
 */
public class OnlineUserAdapter extends RecyclerView.Adapter<OnlineUserAdapter.ViewHolder> {
    private List<User> onlineUserList;
    private int activityItem;
   //private File imagecacheDir;//图片缓存的目录
    private MyOnItemClickListener mitemClick;
    private MyOnItemLongClickListener mitemLongClick;
    public OnlineUserAdapter(List<User> onlineUserList,int activityItem)
    {
        this.onlineUserList=onlineUserList;
        this.activityItem=activityItem;
        //this.imagecacheDir=imagecacheDir;
    }

    /**
     * 设置Item点击监听
     * @param mitemClick
     */
    public void setOnItemCilckListener(MyOnItemClickListener mitemClick){
        this.mitemClick=mitemClick;
    }

    /**
     * 设置Item长按监听
     * @param mitemLongClick
     */
    public void setOnItemLongCilckListener(MyOnItemLongClickListener mitemLongClick){
        this.mitemLongClick=mitemLongClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(activityItem,parent,false);
        return new ViewHolder(itemView,mitemClick,mitemLongClick);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        User user=onlineUserList.get(position);
        viewHolder.userName.setText(user.getName());
        viewHolder.userCharator.setText(user.getCharacter());
        viewHolder.userImage.setImageURI(Uri.parse("android.resource://com.example.administrator.myside/drawable/user_icon"));
       // asyncImageLoad(viewHolder.userImage, currentUser.getPortraitUri());
        viewHolder.itemView.setTag(user);
    }

    /**
     *采用同步线程，进行图片的异步加载
     * @param userImage
     * @param portraitUri
     */
    private void asyncImageLoad(ImageView userImage, String portraitUri) {
        AsyncImageTask itask=new AsyncImageTask(userImage);
        itask.execute(portraitUri);
    }

    /**
     * 实现AsyncTask
     */
    private final class AsyncImageTask extends AsyncTask<String,Integer,Uri>
    {

        private ImageView userImage;
        public AsyncImageTask(ImageView userImage)
        {
            this.userImage=userImage;
        }
        @Override
        protected Uri doInBackground(String... params) {//子线程中运行
           return NetUtil.getImageFileUri(params[0]);
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);
        }
    }

    @Override
    public int getItemCount() {
        return onlineUserList.size();
    }

    public void refresh(List<User> datalist)
    {
        onlineUserList=datalist;
        notifyDataSetChanged();
        GlobalConstants.onlineUserList=datalist;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
    {

        private ImageView userImage;
        private TextView userName;
        private TextView userCharator;
        private MyOnItemClickListener mitemClick;
        private MyOnItemLongClickListener mitemLongClick;
        public ViewHolder(View itemView,MyOnItemClickListener mitemClick,MyOnItemLongClickListener mitemLongClick) {
            super(itemView);
            this.mitemClick=mitemClick;
            this.mitemLongClick=mitemLongClick;
            userImage=(ImageView)itemView.findViewById(R.id.user_image);
            userName=(TextView)itemView.findViewById(R.id.user_name);
            userCharator=(TextView)itemView.findViewById(R.id.user_chracter);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mitemClick!=null)
            {
                mitemClick.onItemClick(v,getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mitemLongClick!=null)
            {
                mitemLongClick.onItemLongClick(v,getLayoutPosition());
            }
            return true;
        }
    }

}
