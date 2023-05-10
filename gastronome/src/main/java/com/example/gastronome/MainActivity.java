package com.example.gastronome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gastronome.adapter.MenuPagerAdapter;
import com.example.gastronome.entity.User;
import com.example.gastronome.fragment.AccountFragment;
import com.example.gastronome.fragment.DynamicsFragment;
import com.example.gastronome.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private TextView tv_title;
    private ImageView iv_head;
    private ViewPager vp_main;
    private BottomNavigationView bnv_menu;
    private List<Fragment> mFragmentList;
    private MenuPagerAdapter menuPagerAdapter;
    private User user;
    private DrawerLayout dl_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_title = findViewById(R.id.tv_title);
        iv_head = findViewById(R.id.iv_head);
        iv_head.setOnClickListener(this);
        findViewById(R.id.iv_plus).setOnClickListener(this);

        vp_main = findViewById(R.id.vp_main);
        bnv_menu = findViewById(R.id.bnv_menu);

        dl_drawer = findViewById(R.id.dl_drawer);

        initMenuFragment();

        //设置适配器
        menuPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(),mFragmentList);
        vp_main.setAdapter(menuPagerAdapter);
        //设置ViewPager的滑动监听器
        vp_main.addOnPageChangeListener(this);
        //设置BottomNavigationView的点击监听器
        bnv_menu.setOnNavigationItemSelectedListener(this);


        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        user = app.user;

        //显示顶部栏信息
        //showHead();

//        if(savedInstanceState == null){//第一次时创建，避免重复创建
//            Bundle bundle = new Bundle();
//            bundle.putString(HomeFragment.ARG_PARAM1,"HomeFragment");
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(R.id.fcv_main, HomeFragment.class,bundle)
//                    .setReorderingAllowed(true)
//                    .addToBackStack(null)
//                    .commit();
//        }
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fcv_main,HomeFragment.class,null)
//                .commit();

    }

    //用户可见时调用
    @Override
    protected void onResume() {
        super.onResume();
        //更新用户信息
        showUserData();
    }

    private void initMenuFragment() {
        //创建Fragment
        HomeFragment homeFragment = new HomeFragment();
        DynamicsFragment dynamicsFragment = new DynamicsFragment();
        AccountFragment accountFragment = new AccountFragment();
        //初始化MenuFragment列表
        mFragmentList = new ArrayList<>();
        mFragmentList.add(homeFragment);
        mFragmentList.add(dynamicsFragment);
        mFragmentList.add(accountFragment);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                tv_title.setText("首页");
                bnv_menu.setSelectedItemId(R.id.menu_home);
                break;
            case 1:
                tv_title.setText("动态");
                bnv_menu.setSelectedItemId(R.id.menu_dynamics);
                break;
            case 2:
                tv_title.setText("我的");
                bnv_menu.setSelectedItemId(R.id.menu_account);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_home:
                tv_title.setText("首页");
                vp_main.setCurrentItem(0);
                break;
            case R.id.menu_dynamics:
                tv_title.setText("动态");
                vp_main.setCurrentItem(1);
                break;
            case R.id.menu_account:
                tv_title.setText("我的");
                vp_main.setCurrentItem(2);
                break;
        }
        return true;
    }

    private void showUserData() {
        String picturePath = user.picPath;
        iv_head.setImageURI(Uri.parse(picturePath));
        if(picturePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            iv_head.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_plus:
                intent = new Intent(this, AddWorkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.iv_head:
                //打开侧边栏
                dl_drawer.openDrawer(Gravity.LEFT);
                //注册与显示侧边栏信息
                showDrawerData();
                break;
            //以下为侧边栏:
            case R.id.ll_drawer_myDynamics:
                intent = new Intent(this, MyDynamicsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ll_drawer_myCollect:
                intent = new Intent(this, MyCollectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ll_drawer_mySubscription:
                intent = new Intent(this, MySubActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ll_drawer_myFun:
                intent = new Intent(this, MyFunActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private void showDrawerData() {
        ImageView iv_drawer_head = findViewById(R.id.iv_drawer_head);
        TextView tv_drawer_name = findViewById(R.id.tv_drawer_name);
        String picturePath = user.picPath;
        iv_head.setImageURI(Uri.parse(picturePath));
        if(picturePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            iv_drawer_head.setImageBitmap(bitmap);
        }
        tv_drawer_name.setText(user.name);
        findViewById(R.id.ll_drawer_myDynamics).setOnClickListener(this);
        findViewById(R.id.ll_drawer_myCollect).setOnClickListener(this);
        findViewById(R.id.ll_drawer_mySubscription).setOnClickListener(this);
        findViewById(R.id.ll_drawer_myFun).setOnClickListener(this);
    }

}