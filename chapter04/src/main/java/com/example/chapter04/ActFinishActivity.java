package com.example.chapter04;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ActFinishActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_finish);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.iv_back||view.getId()==R.id.btn_finish){
            //结束当前活动页面
            finish();
        }
    }
}