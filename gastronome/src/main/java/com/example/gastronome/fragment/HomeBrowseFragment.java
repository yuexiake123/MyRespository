package com.example.gastronome.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.gastronome.BrowseDetailActivity;
import com.example.gastronome.MyWorkDetailActivity;
import com.example.gastronome.R;
import com.example.gastronome.adapter.BrowseGridAdapter;
import com.example.gastronome.database.CategoryDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.Dynamics;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HomeBrowseFragment extends Fragment implements AdapterView.OnItemClickListener {


    private String mTag;
    private String mParam2;
    private GridView gv_browse;
    private WorkDBHelper mWorkDBHelper;
    private CategoryDBHelper mCategoryDBHelper;
    private UserDBHelper mUserDBHelper;
    private List<Dynamics> dynamicsList;

    public HomeBrowseFragment() {
        // Required empty public constructor
    }

    public HomeBrowseFragment(String tag) {
        // Required empty public constructor
        this.mTag = tag;
    }

    public static HomeBrowseFragment newInstance(String tag) {
        HomeBrowseFragment fragment = new HomeBrowseFragment(tag);
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gv_browse = view.findViewById(R.id.gv_browse);

        //打开数据库读写连接
        mUserDBHelper = UserDBHelper.getInstance(getActivity());
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        mWorkDBHelper = WorkDBHelper.getInstance(getActivity());
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        mCategoryDBHelper = CategoryDBHelper.getInstance(getActivity());
        mCategoryDBHelper.openReadLink();
        mCategoryDBHelper.openWriteLink();

        List<Integer> widList = mCategoryDBHelper.getWidByTag(mTag);
        List<Work> workList = mWorkDBHelper.getWorkListById(widList);
        dynamicsList = new ArrayList<>();
        for(Work work:workList){
            Dynamics dynamics = new Dynamics();
            dynamics.author = mUserDBHelper.getUserById(work.uid);
            dynamics.work = work;
            dynamicsList.add(dynamics);
        }
//        for(Dynamics dynamics:dynamicsList){
//            Log.d("Jun",dynamics.work.name);
//        }

        BrowseGridAdapter adapter = new BrowseGridAdapter(getActivity(), dynamicsList);
        gv_browse.setAdapter(adapter);
        gv_browse.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BrowseDetailActivity.class);
        intent.putExtra("work_id",dynamicsList.get(position).work.id);
        intent.putExtra("author_id",dynamicsList.get(position).work.uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}