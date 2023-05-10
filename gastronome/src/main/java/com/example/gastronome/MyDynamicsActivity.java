package com.example.gastronome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gastronome.adapter.DynamicsListAdapter;
import com.example.gastronome.database.CategoryDBHelper;
import com.example.gastronome.database.CollectDBHelper;
import com.example.gastronome.database.CommentDBHelper;
import com.example.gastronome.database.LikeDBHelper;
import com.example.gastronome.database.ShareDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.Dynamics;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;
import com.example.gastronome.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MyDynamicsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private User mUser;
    private WorkDBHelper mWorkDBHelper;
    private CollectDBHelper mCollectDBHelper;
    private LikeDBHelper mLikeDBHelper;
    private CategoryDBHelper mCategoryDBHelper;
    private ShareDBHelper mShareDBHelper;
    private UserDBHelper mUserDBHelper;
    private CommentDBHelper mCommentDBHelper;
    private ListView lv_dynamics;
    private List<Dynamics> dynamicsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamics);
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("我的动态");
        iv_back.setOnClickListener(this);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;

        //打开数据库读写连接
        mWorkDBHelper = WorkDBHelper.getInstance(this);
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        mCategoryDBHelper = CategoryDBHelper.getInstance(this);
        mCategoryDBHelper.openReadLink();
        mCategoryDBHelper.openWriteLink();

        mCollectDBHelper = CollectDBHelper.getInstance(this);
        mCollectDBHelper.openReadLink();
        mCollectDBHelper.openWriteLink();

        mLikeDBHelper = LikeDBHelper.getInstance(this);
        mLikeDBHelper.openReadLink();
        mLikeDBHelper.openWriteLink();

        mShareDBHelper = ShareDBHelper.getInstance(this);
        mShareDBHelper.openReadLink();
        mShareDBHelper.openWriteLink();

        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        mCommentDBHelper = CommentDBHelper.getInstance(this);
        mCommentDBHelper.openReadLink();
        mCommentDBHelper.openWriteLink();


        lv_dynamics = findViewById(R.id.lv_dynamics);

        //显示动态
        //initDynamics();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新
        initDynamics();
    }

    private void initDynamics() {
        dynamicsList = new ArrayList<>();
        //分享的作品
        List<Integer> shareWidList = mShareDBHelper.getWidListByUid(mUser.id);
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
        List<Work> workList = mWorkDBHelper.getWorkByUid(mUser.id);
        for(Work w:workList){
            Dynamics dynamics = new Dynamics();
            dynamics.author = mUser;
            dynamics.work = w;
            dynamics.shareCount = mShareDBHelper.getShareCountByWid(w.id);
            dynamics.commentCount = mCommentDBHelper.getCommentCountByWid(w.id);
            dynamics.commentCount = w.like;
            dynamicsList.add(dynamics);
        }

        DynamicsListAdapter dynamicsListAdapter = new DynamicsListAdapter(this, dynamicsList);
        lv_dynamics.setAdapter(dynamicsListAdapter);
        lv_dynamics.setOnItemClickListener(this);
        lv_dynamics.setOnItemLongClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorkDBHelper.closeLink();
        mCategoryDBHelper.closeLink();
    }

    @Override
    public void onClick(View view) {
        //Intent intent;
        switch (view.getId()){
            case R.id.iv_back:
                //关闭当前页面
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if(dynamicsList.get(position).author.id == mUser.id){
            //是该用户的作品，进入编辑页面
            Intent intent = new Intent(this,MyWorkDetailActivity.class);
            intent.putExtra("work_id",dynamicsList.get(position).work.id);
            //intent.putExtra("author_id",dynamicsList.get(position).work.uid);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else{
            //转发的作品，进入详情浏览页面
            Intent intent = new Intent(this, BrowseDetailActivity.class);
            intent.putExtra("work_id",dynamicsList.get(position).work.id);
            intent.putExtra("author_id",dynamicsList.get(position).work.uid);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(dynamicsList.get(position).author.id == mUser.id){
            //是该用户的作品，删除？
            builder.setMessage("确认删除？");
            builder.setPositiveButton("是",(dialog,which)->{
                if(mWorkDBHelper.deleteWorkById(dynamicsList.get(position).work.id) > 0 && mCategoryDBHelper.deleteCategoryByWid(dynamicsList.get(position).work.id) > 0){
                    //删除收藏映射
                    mCollectDBHelper.deleteCollectByWid(dynamicsList.get(position).work.id);
                    //删除点赞映射
                    mLikeDBHelper.deleteLikeByWid(dynamicsList.get(position).work.id);
                    //删除转发映射
                    mShareDBHelper.deleteShareByWid(dynamicsList.get(position).work.id);
                    ToastUtil.show(this, "删除成功！");
                    //刷新
                    initDynamics();
                }else{
                    ToastUtil.show(this, "ERROR！");
                }
            });
        }else{
            //转发的作品，取消转发
            builder.setMessage("删除本条转发？");
            builder.setPositiveButton("是",(dialog,which)->{
                mShareDBHelper.deleteShare(mUser.id,dynamicsList.get(position).work.id);
                ToastUtil.show(this, "删除成功！");
                //刷新
                initDynamics();
            });
        }
        builder.setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}