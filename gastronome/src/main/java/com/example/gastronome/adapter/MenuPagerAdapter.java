package com.example.gastronome.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MenuPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentlist;

    public MenuPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public MenuPagerAdapter(@NonNull FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentlist = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentlist == null ? null : mFragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentlist == null ? null : mFragmentlist.size();
    }
}
