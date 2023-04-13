package com.example.chapter05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RadioHorizontalActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_horizontal);
        RadioGroup rg_gender=findViewById(R.id.rg_gender);
        tv_result = findViewById(R.id.tv_result);
        rg_gender.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch(i){
            case R.id.rb_male:
                tv_result.setText("帅气男孩");
                break;
            case R.id.rb_female:
                tv_result.setText("漂亮女孩");
                break;
        }
    }
}