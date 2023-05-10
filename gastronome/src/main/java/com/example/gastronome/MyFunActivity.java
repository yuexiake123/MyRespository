package com.example.gastronome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gastronome.adapter.UserListAdapter;
import com.example.gastronome.database.SubDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.entity.User;

import java.util.List;

public class MyFunActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private User mUser;
    private SubDBHelper mSubDBHelper;
    private UserDBHelper mUserDBHelper;
    private ListView lv_fun;
    private List<User> funList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fun);
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("我的粉丝");
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

        lv_fun = findViewById(R.id.lv_fun);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFunList();
    }

    private void initFunList() {
        List<Integer> uidList = mSubDBHelper.getUidListBySuid(mUser.id);
        funList = mUserDBHelper.getUserListById(uidList);

        UserListAdapter adapter = new UserListAdapter(this, funList);
        lv_fun.setAdapter(adapter);
        lv_fun.setOnItemClickListener(this);
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
        intent.putExtra("user_id",funList.get(position).id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}