package com.example.gastronome.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.gastronome.R;
import com.example.gastronome.fragment.HomeBrowseFragment;

import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private String[] mCategoryArray;


    public HomePagerAdapter(@NonNull FragmentManager fm,
                            String[] categoryArray) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mCategoryArray = categoryArray;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return HomeBrowseFragment.newInstance(mCategoryArray[position]);
    }

    @Override
    public int getCount() {
        return mCategoryArray.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryArray[position];
    }
}
