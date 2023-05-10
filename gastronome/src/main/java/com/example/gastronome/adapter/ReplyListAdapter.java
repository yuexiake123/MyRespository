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
import com.example.gastronome.entity.Reply;
import com.example.gastronome.entity.User;

import java.util.List;

public class ReplyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Reply> mReplyList;

    public ReplyListAdapter(Context mContext, List<Reply> replyList) {
        this.mContext = mContext;
        this.mReplyList = replyList;
    }

    @Override
    public int getCount() {
        return mReplyList.size();
    }

    @Override
    public Object getItem(int position) {
        return mReplyList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_reply,null);
            holder.iv_user_head = convertView.findViewById(R.id.iv_user_head);
            holder.tv_comment_date = convertView.findViewById(R.id.tv_comment_date);
            holder.tv_user_name = convertView.findViewById(R.id.tv_user_name);
            holder.tv_comment_content = convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        Reply reply = mReplyList.get(position);
        holder.iv_user_head.setImageURI(Uri.parse(reply.from_user.picPath));
        holder.tv_comment_date.setText(reply.comment.date);
        holder.tv_user_name.setText(reply.from_user.name + ":");
        holder.tv_comment_content.setText(reply.comment.content);
        return convertView;
    }

    public final class ViewHolder{
        public ImageView iv_user_head;
        public TextView tv_comment_date;
        public TextView tv_user_name;
        public TextView tv_comment_content;

    }
}
