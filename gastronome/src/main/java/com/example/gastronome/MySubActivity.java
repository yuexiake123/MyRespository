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

import com.example.gastronome.adapter.UserListAdapter;
import com.example.gastronome.database.SubDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.entity.User;

import com.example.gastronome.util.ToastUtil;

import java.util.List;

public class MySubActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private User mUser;
    private SubDBHelper mSubDBHelper;
    private UserDBHelper mUserDBHelper;
    private List<User> subList;
    private ListView lv_subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sub);
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("我的关注");
        iv_back.setOnClickListener(this);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;

        mSubDBHelper = SubDBHelper.getInstance(this);
        mSubDBHelper.openReadLink();
        mSubDBHelper.openWriteLink();

        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        lv_subscription = findViewById(R.id.lv_subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubDBHelper.closeLink();
        mUserDBHelper.closeLink();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新
        initSubList();
    }

    private void initSubList() {

        List<Integer> subscribedIdList = mSubDBHelper.getSuidListByUid(mUser.id);
        subList = mUserDBHelper.getUserListById(subscribedIdList);

        UserListAdapter adapter = new UserListAdapter(this, subList);
        lv_subscription.setAdapter(adapter);
        lv_subscription.setOnItemClickListener(this);
        lv_subscription.setOnItemLongClickListener(this);
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
        Intent intent = new Intent(this, UserHomepageActivity.class);
        intent.putExtra("user_id",subList.get(position).id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认取消关注？");
        builder.setPositiveButton("是",(dialog,which)->{
            if( mSubDBHelper.deleteSubscription(mUser.id,subList.get(position).id)> 0){
                ToastUtil.show(this, "已取消关注！");
                //刷新
                initSubList();
            }else{
                ToastUtil.show(this, "ERROR！");
            }
        });
        builder.setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}