package com.example.chapter05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private TimePicker tp_time;
    private TextView tv_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_time).setOnClickListener(this);
        tp_time = findViewById(R.id.tp_time);
        tp_time.setIs24HourView(true);
        tv_time = findViewById(R.id.tv_time);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:
                String desc=String.format("您选择的时间是%d时%d分",tp_time.getHour(),tp_time.getMinute());
                tv_time.setText(desc);
                break;
            case R.id.btn_time:
                //获取一个日历实例，里面包含了当前的时分秒
                Calendar calendar=Calendar.getInstance();
                //构建一个时间对话框，该对话框已经集成了时间选择器
                TimePickerDialog dialog=new TimePickerDialog(this,this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true);
                dialog.show();
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        String desc=String.format("您选择的时间是%d时%d分",hourOfDay,minute);
        tv_time.setText(desc);
    }
}