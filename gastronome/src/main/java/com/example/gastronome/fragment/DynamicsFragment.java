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
import android.widget.ListView;

import com.example.gastronome.BrowseDetailActivity;
import com.example.gastronome.MyApplication;
import com.example.gastronome.MyWorkDetailActivity;
import com.example.gastronome.R;
import com.example.gastronome.adapter.DynamicsListAdapter;
import com.example.gastronome.database.CategoryDBHelper;
import com.example.gastronome.database.CommentDBHelper;
import com.example.gastronome.database.ShareDBHelper;
import com.example.gastronome.database.SubDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.Dynamics;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;

import java.util.ArrayList;
import java.util.List;


public class DynamicsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView lv_dynamics;
    private UserDBHelper mUserDBHelper;
    private WorkDBHelper mWorkDBHelper;
    private SubDBHelper mSubDBHelper;
    private ShareDBHelper mShareDBHelper;
    private CommentDBHelper mCommentDBHelper;
    private User mUser;
    private List<Dynamics> dynamicsList;

    public DynamicsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dynamics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_dynamics = view.findViewById(R.id.lv_dynamics);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;

        //打开数据库读写连接
        mUserDBHelper = UserDBHelper.getInstance(getActivity());
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        mWorkDBHelper = WorkDBHelper.getInstance(getActivity());
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        mSubDBHelper = SubDBHelper.getInstance(getActivity());
        mSubDBHelper.openReadLink();
        mSubDBHelper.openWriteLink();

        mShareDBHelper = ShareDBHelper.getInstance(getActivity());
        mShareDBHelper.openReadLink();
        mShareDBHelper.openWriteLink();

        mCommentDBHelper = CommentDBHelper.getInstance(getActivity());
        mCommentDBHelper.openReadLink();
        mCommentDBHelper.openWriteLink();


        //动态
        dynamicsList = new ArrayList<>();
        //分享的作品
        List<Integer> shareWidList = mShareDBHelper.getWidListByUid(mUser.id);
        List<Work> shareWorkList = mWorkDBHelper.getWorkListById(shareWidList);
        for(Work w:shareWorkList){
            Dynamics dynamics = new Dynamics();
            dynamics.author = mUserDBHelper.getUserById(w.uid);
            dynamics.work = w;
            dynamics.shareCount = mShareDBHelper.getShareCountByWid(w.id);
            dynamics.commentCount = mCommentDBHelper.getCommentCountByWid(w.id);
            dynamics.commentCount = mWorkDBHelper.getWorkCountByUid(w.id);
            dynamicsList.add(dynamics);
        }

        DynamicsListAdapter adapter = new DynamicsListAdapter(getActivity(), dynamicsList);
        lv_dynamics.setAdapter(adapter);
        lv_dynamics.setOnItemClickListener(this);

        //关注用户的作品
        List<Integer> subscribedIdList = mSubDBHelper.getSuidListByUid(mUser.id);
        List<Work> workList = mWorkDBHelper.getWorkListByUId(subscribedIdList);
//        for(Work w:workList){
//            Log.d("Jun",w.name);
//        }
        for(Work w:workList){
            Dynamics dynamics = new Dynamics();
            dynamics.author = mUserDBHelper.getUserById(w.uid);
            dynamics.work = w;
            dynamics.shareCount = mShareDBHelper.getShareCountByWid(w.id);
            dynamics.commentCount = mCommentDBHelper.getCommentCountByWid(w.id);
            dynamics.commentCount = mWorkDBHelper.getWorkCountByUid(w.id);
            dynamicsList.add(dynamics);
        }
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