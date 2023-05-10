package com.example.gastronome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gastronome.adapter.DynamicsListAdapter;
import com.example.gastronome.database.CategoryDBHelper;
import com.example.gastronome.database.CollectDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.Dynamics;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;
import com.example.gastronome.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class MyCollectActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private User mUser;
    private WorkDBHelper mWorkDBHelper;
    private UserDBHelper mUserDBHelper;
    private CollectDBHelper mCollectDBHelper;
    private ListView lv_collect;
    private List<Dynamics> dynamicsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("我的收藏");
        iv_back.setOnClickListener(this);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;

        //打开数据库读写连接
        mWorkDBHelper = WorkDBHelper.getInstance(this);
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        mCollectDBHelper = CollectDBHelper.getInstance(this);
        mCollectDBHelper.openReadLink();
        mCollectDBHelper.openWriteLink();

        lv_collect = findViewById(R.id.lv_collect);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUserDBHelper.closeLink();
        mWorkDBHelper.closeLink();
        mCollectDBHelper.closeLink();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新
        initDynamics();
    }

    private void initDynamics() {
        List<Integer> widList = mCollectDBHelper.getWidListByUid(mUser.id);
        List<Work> workList = mWorkDBHelper.getWorkListById(widList);
        dynamicsList = new ArrayList<>();
        for(Work work:workList){
            Dynamics dynamics = new Dynamics();
            dynamics.author = mUserDBHelper.getUserById(work.uid);
            dynamics.work = work;
            dynamicsList.add(dynamics);
        }

        DynamicsListAdapter dynamicsListAdapter = new DynamicsListAdapter(this, dynamicsList);
        lv_collect.setAdapter(dynamicsListAdapter);
        lv_collect.setOnItemClickListener(this);
        lv_collect.setOnItemLongClickListener(this);
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
        Intent intent = new Intent(this, BrowseDetailActivity.class);
        intent.putExtra("work_id",dynamicsList.get(position).work.id);
        intent.putExtra("author_id",dynamicsList.get(position).work.uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认取消此收藏？");
        builder.setPositiveButton("是",(dialog,which)->{
            if( mCollectDBHelper.deleteCollect(mUser.id,dynamicsList.get(position).work.id)> 0){
                ToastUtil.show(this, "删除成功！");
                //刷新
                initDynamics();
            }else{
                ToastUtil.show(this, "ERROR！");
            }
        });
        builder.setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}