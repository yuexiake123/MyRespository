package com.example.gastronome.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gastronome.AccountDetailActivity;
import com.example.gastronome.AddWorkActivity;
import com.example.gastronome.MyApplication;
import com.example.gastronome.MyCollectActivity;
import com.example.gastronome.MyDynamicsActivity;
import com.example.gastronome.MyFunActivity;
import com.example.gastronome.MySubActivity;
import com.example.gastronome.R;
import com.example.gastronome.StartActivity;
import com.example.gastronome.database.CollectDBHelper;
import com.example.gastronome.database.ShareDBHelper;
import com.example.gastronome.database.SubDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.User;


public class AccountFragment extends Fragment implements View.OnClickListener {

    private User mUser;
    private ImageView iv_user_head;
    private TextView tv_user_name;
    private TextView tv_user_area;
    private TextView tv_user_date;
    private TextView tv_dynamics_count;
    private TextView tv_collect_count;
    private TextView tv_subscribe_count;
    private TextView tv_fun_count;


    public AccountFragment() {
        // Required empty public constructor
    }

    //fragment对用户不可见时调用
    @Override
    public void onStop() {
        super.onStop();
    }

    //fragment对用户可见时调用
    @Override
    public void onStart() {
        super.onStart();
        //从用户界面返回后更新显示
        showUserCard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ll_card).setOnClickListener(this);
        iv_user_head = view.findViewById(R.id.iv_user_head);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_user_area = view.findViewById(R.id.tv_user_area);
        tv_user_date = view.findViewById(R.id.tv_user_date);
        tv_dynamics_count = view.findViewById(R.id.tv_dynamics_count);
        tv_collect_count = view.findViewById(R.id.tv_collect_count);
        tv_subscribe_count = view.findViewById(R.id.tv_subscribe_count);
        tv_fun_count = view.findViewById(R.id.tv_fun_count);
        view.findViewById(R.id.btn_add_work).setOnClickListener(this);
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        view.findViewById(R.id.ll_dynamics).setOnClickListener(this);
        view.findViewById(R.id.ll_collect).setOnClickListener(this);
        view.findViewById(R.id.ll_subscribe).setOnClickListener(this);
        view.findViewById(R.id.ll_fun).setOnClickListener(this);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;

        //showUserCard();

    }

    private void showUserCard() {
        //头像
        String picturePath = mUser.picPath;
        iv_user_head.setImageURI(Uri.parse(picturePath));
        if(picturePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            iv_user_head.setImageBitmap(bitmap);
        }
        //其他信息
        tv_user_name.setText(mUser.name);
        tv_user_area.setText(mUser.area);
        tv_user_date.setText(mUser.date);

        //动态数
        WorkDBHelper mWorkDBHelper = WorkDBHelper.getInstance(getActivity());
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        CollectDBHelper mCollectDBHelper = CollectDBHelper.getInstance(getActivity());
        mCollectDBHelper.openReadLink();
        mCollectDBHelper.openWriteLink();

        SubDBHelper mSubDBHelper = SubDBHelper.getInstance(getActivity());
        mSubDBHelper.openReadLink();
        mSubDBHelper.openWriteLink();

        ShareDBHelper mShareDBHelper = ShareDBHelper.getInstance(getActivity());
        mShareDBHelper.openReadLink();
        mShareDBHelper.openWriteLink();

        int myDynamicsCount = mWorkDBHelper.getWorkCountByUid(mUser.id) + mShareDBHelper.getShareCountByUid(mUser.id);
        //Log.d("Jun","myDynamicsCount:"+Integer.toString(myDynamicsCount));
        tv_dynamics_count.setText(Integer.toString(myDynamicsCount));

        int myCollectCount = mCollectDBHelper.getCollectCountByUid(mUser.id);
        tv_collect_count.setText(Integer.toString(myCollectCount));

        int mySubCount = mSubDBHelper.getSubCountByUid(mUser.id);
        tv_subscribe_count.setText(Integer.toString(mySubCount));

        int myFunCount = mSubDBHelper.getSubCountBySuid(mUser.id);
        tv_fun_count.setText(Integer.toString(myFunCount));

        //关闭数据库连接
        mWorkDBHelper.closeLink();
        mCollectDBHelper.closeLink();
        mSubDBHelper.closeLink();
        mShareDBHelper.closeLink();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.ll_card:
                intent = new Intent(getActivity(), AccountDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_add_work:
                intent = new Intent(getActivity(), AddWorkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                intent = new Intent(getActivity(), StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ll_dynamics:
                intent = new Intent(getActivity(), MyDynamicsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ll_collect:
                intent = new Intent(getActivity(), MyCollectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ll_subscribe:
                intent = new Intent(getActivity(), MySubActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.ll_fun:
                intent = new Intent(getActivity(), MyFunActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}