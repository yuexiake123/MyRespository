package com.example.gastronome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.gastronome.entity.User;
import com.example.gastronome.fragment.AccountFragment;
import com.example.gastronome.fragment.DynamicsFragment;
import com.example.gastronome.fragment.HomeFragment;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private TextView tv_title;
    private RadioGroup rg_main;
    private FragmentContainerView fcv_home;
    private HomeFragment homeFragment;
    private DynamicsFragment dynamicsFragment;
    private AccountFragment accountFragment;
    private User user;
    private ImageView iv_head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_title = findViewById(R.id.tv_title);
        rg_main = findViewById(R.id.rg_main);
        fcv_home = findViewById(R.id.fcv_home);
        iv_head = findViewById(R.id.iv_head);

        //创建Fragment
        homeFragment = new HomeFragment();
        dynamicsFragment = new DynamicsFragment();
        accountFragment = new AccountFragment();

        //显示首页
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fcv_home,HomeFragment.class,null).commit();

        rg_main.setOnCheckedChangeListener(this);


        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        user = app.user;

        //显示用户头像
        showHead();
    }

    private void showHead() {
        String picturePath = user.picPath;
        iv_head.setImageURI(Uri.parse(picturePath));
        if(picturePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            iv_head.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        Fragment fragment = null;
        switch (checkedId){
            case R.id.rb_home:
                tv_title.setText("首页");
                fragment = homeFragment;
                break;
            case R.id.rb_dynamics:
                tv_title.setText("动态");
                fragment = dynamicsFragment;
                break;
            case R.id.rb_account:
                tv_title.setText("我的");
                fragment = accountFragment;
                break;
        }
        //切换fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fcv_home,fragment,null).commit();
    }
}