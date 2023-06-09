package com.example.chapter08;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.chapter08.util.ToastUtil;

public class SpinnerDropdownActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // 定义下拉列表需要显示的文本数组
    private final static String[] starArray = {"水星", "金星", "地球", "火星", "木星", "土星"};
    private Spinner sp_dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_dropdown);
        sp_dropdown = findViewById(R.id.sp_dropdown);
        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this,R.layout.item_select,starArray);
        sp_dropdown.setAdapter(startAdapter);
        // 设置下拉框默认显示第一项
        sp_dropdown.setSelection(0);
        // 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        sp_dropdown.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.show(this,"您选择的是" + starArray[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}