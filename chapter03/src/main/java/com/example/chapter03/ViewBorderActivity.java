package com.example.chapter03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chapter03.util.Utils;

public class ViewBorderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_border);
        TextView tv_code=findViewById(R.id.tv_code);
        //获取tv_code的布局参数（含宽高）
        ViewGroup.LayoutParams params=tv_code.getLayoutParams();
        //修改布局参数中的宽度数值，默认px，需要把dp数值转换为px数值
        params.width= Utils.dip2px(this,300);
        //设置tv_code的布局参数
        tv_code.setLayoutParams(params);
    }
}