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

public class BrowseGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Dynamics> mDynamicsList;

    public BrowseGridAdapter(Context mContext, List<Dynamics> mDynamicsList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_browse,null);
            holder.iv_author_head = convertView.findViewById(R.id.iv_author_head);
            holder.tv_author_name = convertView.findViewById(R.id.tv_author_name);
            holder.tv_work_name = convertView.findViewById(R.id.tv_work_name);
            holder.iv_work_photo = convertView.findViewById(R.id.iv_work_photo);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Dynamics dynamics = mDynamicsList.get(position);
        holder.iv_work_photo.setImageURI(Uri.parse(dynamics.work.picPath));
        holder.tv_work_name.setText(dynamics.work.name);
        holder.iv_author_head.setImageURI(Uri.parse(dynamics.author.picPath));
        holder.tv_author_name.setText(dynamics.author.name);
        return convertView;
    }

    public final class ViewHolder{
        public ImageView iv_work_photo;
        public TextView tv_work_name;
        public ImageView iv_author_head;
        public TextView tv_author_name;
    }
}
