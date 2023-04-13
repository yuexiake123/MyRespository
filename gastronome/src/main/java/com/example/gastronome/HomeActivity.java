package com.example.gastronome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tv_title = findViewById(R.id.tv_title);


        findViewById(R.id.rb_first).setOnClickListener(this);
        findViewById(R.id.rb_dynamics).setOnClickListener(this);
        findViewById(R.id.rb_account).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rb_first:
                tv_title.setText("首页");
                break;
            case R.id.rb_dynamics:
                tv_title.setText("关注");
                break;
            case R.id.rb_account:
                tv_title.setText("我的");
                break;
        }
    }
}