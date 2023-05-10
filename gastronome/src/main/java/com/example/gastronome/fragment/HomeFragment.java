package com.example.gastronome.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gastronome.R;
import com.example.gastronome.adapter.HomePagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    public static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
//    private TextView tv_txt;
//    private List<Fragment> mFragmentList;
//    private List<String> titleList;
    private ViewPager vp_home;
    private PagerTabStrip pts_home;
    private TabLayout tl_home;

    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParam1 = ARG_PARAM1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vp_home = view.findViewById(R.id.vp_home);
        //pts_home = view.findViewById(R.id.pts_home);
        tl_home = view.findViewById(R.id.tl_home);
        //初始化翻页视图
        initHomeViewPager();

    }


    private void initHomeViewPager() {
        //初始化翻页视图
        String[] categoryArray = getResources().getStringArray(R.array.categories);

        //设置翻页标签栏的文本大
        //pts_home.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
        //设置适配器
        HomePagerAdapter adapter = new HomePagerAdapter(getChildFragmentManager(),categoryArray);
        vp_home.setAdapter(adapter);
        vp_home.setCurrentItem(0);
        tl_home.setupWithViewPager(vp_home);
    }
}