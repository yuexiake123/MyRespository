package com.example.getnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.getnow.fragment.AccountFragment;
import com.example.getnow.fragment.DetailFragment;
import com.example.getnow.fragment.DynamicsFragment;
import com.example.getnow.fragment.HomeFragment;
import com.example.getnow.fragment.RoomFragment;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_main;
    private TextView tv_title;

    private HomeFragment homeFragment;
    private DynamicsFragment dynamicsFragment;
    private AccountFragment accountFragment;
    private DetailFragment detailFragment;
    private ImageView iv_plus;
    private RoomFragment roomFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建Fragment
        homeFragment = new HomeFragment();
        dynamicsFragment = new DynamicsFragment();
        accountFragment = new AccountFragment();

        detailFragment = new DetailFragment();
        roomFragment = new RoomFragment();

        rg_main = findViewById(R.id.rg_main);
        tv_title = findViewById(R.id.tv_title);
        iv_plus = findViewById(R.id.iv_plus);
        rg_main.setOnCheckedChangeListener(this);


        //显示首页
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_main,homeFragment).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        Fragment fragment = null;
        switch (checkedId){
            case R.id.rb_home:
                tv_title.setText("首页");
                //fragment = homeFragment;
                //fragment = detailFragment;
                fragment = roomFragment;
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
        fragmentManager.beginTransaction().replace(R.id.fl_main,fragment).commit();
    }
}