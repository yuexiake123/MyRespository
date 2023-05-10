package com.example.gastronome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gastronome.adapter.DynamicsListAdapter;
import com.example.gastronome.database.CollectDBHelper;
import com.example.gastronome.database.CommentDBHelper;
import com.example.gastronome.database.ShareDBHelper;
import com.example.gastronome.database.SubDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.Dynamics;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;
import com.example.gastronome.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class UserHomepageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView iv_user_head;
    private TextView tv_user_name;
    private TextView tv_user_area;
    private TextView tv_user_date;
    private Button btn_subscribe;
    private TextView tv_dynamics_count;
    private TextView tv_collect_count;
    private TextView tv_subscribe_count;
    private TextView tv_fun_count;
    private ListView lv_dynamics;
    private User mUser;
    private int user_id;
    private User user;
    private List<Work> workList;
    private List<Dynamics> dynamicsList;
    private UserDBHelper mUserDBHelper;
    private WorkDBHelper mWorkDBHelper;
    private CollectDBHelper mCollectDBHelper;
    private SubDBHelper mSubDBHelper;
    private ShareDBHelper mShareDBHelper;
    private CommentDBHelper mCommentDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_homepage);

        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("用户主页");
        iv_back.setOnClickListener(this);

        iv_user_head = findViewById(R.id.iv_user_head);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_area = findViewById(R.id.tv_user_area);
        tv_user_date = findViewById(R.id.tv_user_date);
        btn_subscribe = findViewById(R.id.btn_subscribe);
        btn_subscribe.setOnClickListener(this);
        tv_dynamics_count = findViewById(R.id.tv_dynamics_count);
        tv_collect_count = findViewById(R.id.tv_collect_count);
        tv_subscribe_count = findViewById(R.id.tv_subscribe_count);
        tv_fun_count = findViewById(R.id.tv_fun_count);
        findViewById(R.id.ll_dynamics).setOnClickListener(this);
        findViewById(R.id.ll_collect).setOnClickListener(this);
        findViewById(R.id.ll_subscribe).setOnClickListener(this);
        findViewById(R.id.ll_fun).setOnClickListener(this);
        lv_dynamics = findViewById(R.id.lv_dynamics);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;

        //打开数据库连接
        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        mWorkDBHelper = WorkDBHelper.getInstance(this);
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        mCollectDBHelper = CollectDBHelper.getInstance(this);
        mCollectDBHelper.openReadLink();
        mCollectDBHelper.openWriteLink();

        mSubDBHelper = SubDBHelper.getInstance(this);
        mSubDBHelper.openReadLink();
        mSubDBHelper.openWriteLink();

        mShareDBHelper = ShareDBHelper.getInstance(this);
        mShareDBHelper.openReadLink();
        mShareDBHelper.openWriteLink();

        mCommentDBHelper = CommentDBHelper.getInstance(this);
        mCommentDBHelper.openReadLink();
        mCommentDBHelper.openWriteLink();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserData();
    }

    private void showUserData() {

        // 从上一个页面传来的意图中获取快递包裹
        user_id = getIntent().getIntExtra("user_id",0);
        user = mUserDBHelper.getUserById(user_id);

        //头像
        String picturePath = user.picPath;
        iv_user_head.setImageURI(Uri.parse(picturePath));
        if(picturePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            iv_user_head.setImageBitmap(bitmap);
        }
        //其他信息
        tv_user_name.setText(user.name);
        tv_user_area.setText(user.area);
        tv_user_date.setText(user.date);

        //关注按钮
        if(mSubDBHelper.checkIsSubscribed(mUser.id,user.id)){
            //已订阅
            btn_subscribe.setText("已关注");
            btn_subscribe.setBackgroundResource(R.drawable.shape_subscribed);
        }

        //数量
        int dynamicsCount = mWorkDBHelper.getWorkCountByUid(user.id);
        //Log.d("Jun","myDynamicsCount:"+Integer.toString(myDynamicsCount));
        tv_dynamics_count.setText(Integer.toString(dynamicsCount));

        int collectCount = mCollectDBHelper.getCollectCountByUid(user.id);
        tv_collect_count.setText(Integer.toString(collectCount));

        int subCount = mSubDBHelper.getSubCountByUid(user.id);
        tv_subscribe_count.setText(Integer.toString(subCount));

        int funCount = mSubDBHelper.getSubCountBySuid(user.id);
        tv_fun_count.setText(Integer.toString(funCount));

        //动态
        dynamicsList = new ArrayList<>();

        //分享的作品
        List<Integer> shareWidList = mShareDBHelper.getWidListByUid(user.id);
        List<Work> shareWorkList = mWorkDBHelper.getWorkListById(shareWidList);
        for(Work w:shareWorkList){
            Dynamics dynamics = new Dynamics();
            dynamics.author = mUserDBHelper.getUserById(w.uid);
            dynamics.work = w;
            dynamics.shareCount = mShareDBHelper.getShareCountByWid(w.id);
            dynamics.commentCount = mCommentDBHelper.getCommentCountByWid(w.id);
            dynamics.commentCount = w.like;
            dynamicsList.add(dynamics);
        }

        //自己的作品
        workList = mWorkDBHelper.getWorkByUid(user.id);
        for(Work w: workList){
            Dynamics dynamics = new Dynamics();
            dynamics.author = user;
            dynamics.work = w;
            dynamics.shareCount = mShareDBHelper.getShareCountByWid(w.id);
            dynamics.commentCount = mCommentDBHelper.getCommentCountByWid(w.id);
            dynamics.commentCount = w.like;
            dynamicsList.add(dynamics);
        }

        DynamicsListAdapter adapter = new DynamicsListAdapter(this, dynamicsList);
        lv_dynamics.setAdapter(adapter);
        lv_dynamics.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //Intent intent;
        switch (view.getId()){
            case R.id.iv_back:
                //关闭当前页面
                finish();
                break;
            case R.id.btn_subscribe:
                if(mSubDBHelper.checkIsSubscribed(mUser.id,user_id)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("取消关注？");
                    builder.setPositiveButton("是",(dialog,which)->{
                        if(mSubDBHelper.deleteSubscription(mUser.id,user_id) > 0){
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
                    if(mSubDBHelper.addSubscription(mUser.id,user_id) > 0){
                        btn_subscribe.setText("已关注");
                        btn_subscribe.setBackgroundResource(R.drawable.shape_subscribed);
                        ToastUtil.show(this,"您成功关注了" + tv_user_name.getText().toString() + "!");
                    }
                }
                break;
            case R.id.ll_dynamics:
                ToastUtil.show(this, "在下面哦！");
                break;
            case R.id.ll_collect:
            case R.id.ll_subscribe:
            case R.id.ll_fun:
                ToastUtil.show(this, "您没有权限查看！");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(this,BrowseDetailActivity.class);
        intent.putExtra("work_id",dynamicsList.get(position).work.id);
        intent.putExtra("author_id",dynamicsList.get(position).author.id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}