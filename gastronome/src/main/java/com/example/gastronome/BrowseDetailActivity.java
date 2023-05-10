package com.example.gastronome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gastronome.database.CategoryDBHelper;
import com.example.gastronome.database.CollectDBHelper;
import com.example.gastronome.database.CommentDBHelper;
import com.example.gastronome.database.LikeDBHelper;
import com.example.gastronome.database.ShareDBHelper;
import com.example.gastronome.database.SubDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;
import com.example.gastronome.util.DateUtil;
import com.example.gastronome.util.ToastUtil;
import com.example.gastronome.view.FlowLayout;

import java.util.Calendar;
import java.util.List;

public class BrowseDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private UserDBHelper mUserDBHelper;
    private WorkDBHelper mWorkDBHelper;
    private CategoryDBHelper mCategoryDBHelper;
    private SubDBHelper mSubDBHelper;
    private LikeDBHelper mLikeDBHelper;
    private CollectDBHelper mCollectDBHelper;
    private ShareDBHelper mShareDBHelper;
    private CommentDBHelper mCommentDBHelper;
    private ImageView iv_author_head;
    private TextView tv_author_name;
    private TextView tv_author_area;
    private TextView tv_date;
    private Button btn_subscribe;
    private ImageView iv_photo;
    private TextView tv_name;
    private TextView tv_description;
    private TextView tv_time;
    private TextView tv_burdening;
    private TextView tv_ingredient;
    private TextView tv_step;
    private FlowLayout fl_tag;
    private ImageView iv_like;
    private TextView tv_like_count;
    private ImageView iv_collect;
    private TextView tv_collect_count;
    private ImageView iv_share;
    private TextView tv_share_count;
    private ImageView iv_comment;
    private TextView tv_comment_count;
    private User mUser;
    private int work_id;
    private int author_id;
    private int mLikeCount;
    private int mCollectCount;
    private int mShareCount;
    private int mCommentCount;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_detail);
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("作品详情");
        iv_back.setOnClickListener(this);

        iv_author_head = findViewById(R.id.iv_author_head);
        tv_author_name = findViewById(R.id.tv_author_name);
        tv_author_area = findViewById(R.id.tv_author_area);
        findViewById(R.id.ll_author_card).setOnClickListener(this);
        btn_subscribe = findViewById(R.id.btn_subscribe);

        tv_date = findViewById(R.id.tv_date);
        iv_photo = findViewById(R.id.iv_photo);
        tv_name = findViewById(R.id.tv_name);
        tv_description = findViewById(R.id.tv_description);
        tv_time = findViewById(R.id.tv_time);
        tv_burdening = findViewById(R.id.tv_burdening);
        tv_ingredient = findViewById(R.id.tv_ingredient);
        tv_step = findViewById(R.id.tv_step);
        fl_tag = findViewById(R.id.fl_tag);
        iv_like = findViewById(R.id.iv_like);
        tv_like_count = findViewById(R.id.tv_like_count);
        iv_collect = findViewById(R.id.iv_collect);
        tv_collect_count = findViewById(R.id.tv_collect_count);
        iv_share = findViewById(R.id.iv_share);
        tv_share_count = findViewById(R.id.tv_share_count);
        iv_comment = findViewById(R.id.iv_comment);
        tv_comment_count = findViewById(R.id.tv_comment_count);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;
        // 从上一个页面传来的意图中获取快递包裹
        work_id = getIntent().getIntExtra("work_id",0);
        author_id = getIntent().getIntExtra("author_id",0);

        //打开数据库读写连接
        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        mWorkDBHelper = WorkDBHelper.getInstance(this);
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        mCategoryDBHelper = CategoryDBHelper.getInstance(this);
        mCategoryDBHelper.openReadLink();
        mCategoryDBHelper.openWriteLink();

        mSubDBHelper = SubDBHelper.getInstance(this);
        mSubDBHelper.openReadLink();
        mSubDBHelper.openWriteLink();

        mLikeDBHelper = LikeDBHelper.getInstance(this);
        mLikeDBHelper.openReadLink();
        mLikeDBHelper.openWriteLink();

        mCollectDBHelper = CollectDBHelper.getInstance(this);
        mCollectDBHelper.openReadLink();
        mCollectDBHelper.openWriteLink();

        mShareDBHelper = ShareDBHelper.getInstance(this);
        mShareDBHelper.openReadLink();
        mShareDBHelper.openWriteLink();

        mCommentDBHelper = CommentDBHelper.getInstance(this);
        mCommentDBHelper.openReadLink();
        mCommentDBHelper.openWriteLink();

        btn_subscribe.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        iv_collect.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_comment.setOnClickListener(this);

        showData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //从评论页面返回时刷新评论数
        mCommentCount = mCommentDBHelper.getCommentCountByWid(work_id);
        tv_comment_count.setText(Integer.toString(mCommentCount));
        //避免刷新时出现的重复添加tag
//        fl_tag.removeAllViews();
//        showData();
    }

    private void showData() {

        User author = mUserDBHelper.getUserById(author_id);
        Work work = mWorkDBHelper.getWorkById(work_id);

        //头像
        String picPath = author.picPath;
        iv_author_head.setImageURI(Uri.parse(picPath));
        if (picPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            iv_author_head.setImageBitmap(bitmap);
        }
        tv_author_name.setText(author.name);
        tv_author_area.setText(author.area);

        //关注按钮
        if(mSubDBHelper.checkIsSubscribed(mUser.id,author.id)){
            //已订阅
            btn_subscribe.setText("已关注");
            btn_subscribe.setBackgroundResource(R.drawable.shape_subscribed);
        }

        //作品
        picPath = work.picPath;
        iv_photo.setImageURI(Uri.parse(picPath));
        if (picPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            iv_photo.setImageBitmap(bitmap);
        }
        //其他信息
        tv_date.setText(work.date);
        tv_name.setText(work.name);
        tv_description.setText(work.description);
        tv_time.setText(work.time);
        tv_burdening.setText(work.burdening);
        tv_ingredient.setText(work.ingredient);
        tv_step.setText(work.step);
        //标签
        List<String> selectedTagList = mCategoryDBHelper.getCategoryByWid(work.id);
        //获取标签
        String[] tagArray = getResources().getStringArray(R.array.tags);
        LayoutInflater mlayoutInflater = LayoutInflater.from(this);
        for (int i = 0; i < tagArray.length; i++) {
            TextView view = (TextView) mlayoutInflater.inflate(R.layout.item_flow, fl_tag, false);
            view.setText(tagArray[i]);
            for(String str : selectedTagList){
                if(str.equals(tagArray[i])){
                    view.setBackgroundResource(R.drawable.shape_flow_selected);
                }
            }
            fl_tag.addView(view);
        }

        //点赞
        mLikeCount = work.like;
        tv_like_count.setText(Integer.toString(mLikeCount));
        if(mLikeDBHelper.checkIsLiked(mUser.id,work.id)){
            //已点赞
            iv_like.setImageResource(R.drawable.dianzan_select);
        }

        //收藏
        mCollectCount = mCollectDBHelper.getCollectCountByWid(work.id);
        tv_collect_count.setText(Integer.toString(mCollectCount));
        if(mCollectDBHelper.checkIsCollected(mUser.id,work.id)){
            //已收藏
            iv_collect.setImageResource(R.drawable.collect_select);
        }

        //分享
        mShareCount = mShareDBHelper.getShareCountByWid(work.id);
        tv_share_count.setText(Integer.toString(mShareCount));
        if(mShareDBHelper.checkIsShared(mUser.id,work.id)){
            //已分享
            iv_share.setImageResource(R.drawable.ic_outline_reply_24_selected);
        }

        //评论
        mCommentCount = mCommentDBHelper.getCommentCountByWid(work.id);
        tv_comment_count.setText(Integer.toString(mCommentCount));
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_author_card:
                intent = new Intent(this, UserHomepageActivity.class);
                intent.putExtra("user_id",author_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_subscribe:
                if(mUser.id == author_id){
                    ToastUtil.show(this,"不可以关注自己哦");
                    break;
                }
                if(mSubDBHelper.checkIsSubscribed(mUser.id,author_id)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("取消关注？");
                    builder.setPositiveButton("是",(dialog,which)->{
                        if(mSubDBHelper.deleteSubscription(mUser.id,author_id) > 0){
                            btn_subscribe.setText("+关注");
                            btn_subscribe.setBackgroundResource(R.drawable.shape_subscribe_btn);
                            ToastUtil.show(this,"已取消关注");
                        }else{
                            ToastUtil.show(this,"失败，请重试！");
                        }
                    });
                    builder.setNegativeButton("否",null);
                    builder.create().show();
                }else{
                    if(mSubDBHelper.addSubscription(mUser.id,author_id) > 0){
                        btn_subscribe.setText("已关注");
                        btn_subscribe.setBackgroundResource(R.drawable.shape_subscribed);
                        ToastUtil.show(this,"您成功关注了" + tv_author_name.getText().toString() + "!");
                    }
                }
                break;
            case R.id.iv_like:
                if(mLikeDBHelper.checkIsLiked(mUser.id,work_id)){
                    //已点赞
                    mLikeCount--;
                    if(mLikeDBHelper.deletelike(mUser.id,work_id) > 0 && mWorkDBHelper.changeLike(work_id,mLikeCount) > 0){
                        tv_like_count.setText(Integer.toString(mLikeCount));
                        iv_like.setImageResource(R.drawable.dianzan);
                        ToastUtil.show(this,"已取消点赞！");
                    }
                }else{
                    //未点赞
                    ++mLikeCount;
                    if(mLikeDBHelper.addlike(mUser.id,work_id) > 0 && mWorkDBHelper.changeLike(work_id,mLikeCount) > 0){
                        tv_like_count.setText(Integer.toString(mLikeCount));
                        iv_like.setImageResource(R.drawable.dianzan_select);
                        ToastUtil.show(this,"感谢您的喜欢！");
                    }
                }
                break;
            case R.id.iv_collect:
                if(mCollectDBHelper.checkIsCollected(mUser.id,work_id)){
                    //已收藏
                    mCollectCount--;
                    if(mCollectDBHelper.deleteCollect(mUser.id,work_id) > 0){
                        tv_collect_count.setText(Integer.toString(mCollectCount));
                        iv_collect.setImageResource(R.drawable.collect);
                        ToastUtil.show(this,"已取消收藏！");
                    }
                }else{
                    //未点赞
                    ++mCollectCount;
                    if(mCollectDBHelper.addCollect(mUser.id,work_id) > 0){
                        tv_collect_count.setText(Integer.toString(mCollectCount));
                        iv_collect.setImageResource(R.drawable.collect_select);
                        ToastUtil.show(this,"感谢您的认可！");
                    }
                }
                break;
            case R.id.iv_share:
                if(mShareDBHelper.checkIsShared(mUser.id,work_id)){
                    //已分享
                    mShareCount--;
                    if(mShareDBHelper.deleteShare(mUser.id,work_id) > 0){
                        tv_share_count.setText(Integer.toString(mShareCount));
                        iv_share.setImageResource(R.drawable.ic_outline_reply_24);
                        ToastUtil.show(this,"已取消分享！");
                    }
                }else{
                    //获取当前时间
                    calendar = Calendar.getInstance();
                    //未分享
                    ++mShareCount;
                    String date = DateUtil.getDate(calendar);
                    if(mShareDBHelper.addShare(mUser.id,work_id,date) > 0){
                        tv_share_count.setText(Integer.toString(mShareCount));
                        iv_share.setImageResource(R.drawable.ic_outline_reply_24_selected);
                        ToastUtil.show(this,"感谢分享！");
                    }
                }
                break;
            case R.id.iv_comment:
                intent = new Intent(this, CommentDetailActivity.class);
                intent.putExtra("author_id",author_id);
                intent.putExtra("work_id",work_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}