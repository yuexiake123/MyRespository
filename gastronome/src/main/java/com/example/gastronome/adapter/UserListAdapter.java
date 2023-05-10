package com.example.gastronome.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gastronome.R;
import com.example.gastronome.entity.Dynamics;
import com.example.gastronome.entity.User;

import java.util.List;

public class UserListAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> mUserList;

    public UserListAdapter(Context mContext, List<User> mUserList) {
        this.mContext = mContext;
        this.mUserList = mUserList;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            //获取item_dynamics
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user,null);
            holder.iv_user_head = convertView.findViewById(R.id.iv_user_head);
            holder.tv_user_name = convertView.findViewById(R.id.tv_user_name);
            holder.tv_user_area = convertView.findViewById(R.id.tv_user_area);
            holder.tv_user_date = convertView.findViewById(R.id.tv_user_date);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        User user = mUserList.get(position);
        holder.iv_user_head.setImageURI(Uri.parse(user.picPath));
        holder.tv_user_name.setText(user.name);
        holder.tv_user_area.setText(user.area);
        holder.tv_user_date.setText(user.date);
        return convertView;
    }

    public final class ViewHolder{
        public ImageView iv_user_head;
        public TextView tv_user_name;
        public TextView tv_user_area;
        public TextView tv_user_date;

    }
}
