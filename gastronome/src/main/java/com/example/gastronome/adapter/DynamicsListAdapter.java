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

import java.util.List;

public class DynamicsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Dynamics> mDynamicsList;

    public DynamicsListAdapter(Context mContext, List<Dynamics> mDynamicsList) {
        this.mContext = mContext;
        this.mDynamicsList = mDynamicsList;
    }

    @Override
    public int getCount() {
        return mDynamicsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDynamicsList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamics,null);
            holder.iv_author_head = convertView.findViewById(R.id.iv_author_head);
            holder.tv_author_name = convertView.findViewById(R.id.tv_author_name);
            holder.tv_publish_date = convertView.findViewById(R.id.tv_publish_date);
            holder.tv_work_name = convertView.findViewById(R.id.tv_work_name);
            holder.tv_work_description = convertView.findViewById(R.id.tv_work_description);
            holder.iv_work_photo = convertView.findViewById(R.id.iv_work_photo);
            holder.tv_share_count = convertView.findViewById(R.id.tv_share_count);
            holder.tv_comment_count = convertView.findViewById(R.id.tv_comment_count);
            holder.tv_like_count = convertView.findViewById(R.id.tv_like_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Dynamics dynamics = mDynamicsList.get(position);
        holder.iv_author_head.setImageURI(Uri.parse(dynamics.author.picPath));
        holder.tv_author_name.setText(dynamics.author.name);
        holder.tv_publish_date.setText(dynamics.work.date);
        holder.tv_work_name.setText(dynamics.work.name);
        holder.tv_work_description.setText(dynamics.work.description);
        holder.iv_work_photo.setImageURI(Uri.parse(dynamics.work.picPath));
        holder.tv_share_count.setText(Integer.toString(dynamics.shareCount));
        holder.tv_comment_count.setText(Integer.toString(dynamics.commentCount));
        holder.tv_like_count.setText(Integer.toString(dynamics.likeCount));
        return convertView;
    }

    public final class ViewHolder{
        public ImageView iv_author_head;
        public TextView tv_author_name;
        public TextView tv_publish_date;
        public TextView tv_work_name;
        public TextView tv_work_description;
        public ImageView iv_work_photo;
        public TextView tv_share_count;
        public TextView tv_comment_count;
        public TextView tv_like_count;
    }
}
