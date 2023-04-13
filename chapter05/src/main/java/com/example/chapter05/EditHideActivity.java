package com.example.chapter05;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.chapter05.util.ViewUtil;

public class EditHideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hide);
        EditText et_phone=findViewById(R.id.et_phone);
        EditText et_password=findViewById(R.id.et_password);

        et_phone.addTextChangedListener(new HideTextWatcher(et_phone,11));
        et_password.addTextChangedListener(new HideTextWatcher(et_password,6));
    }

    //定义一个编辑框监听器，在输入文本达到指定长度时自动隐藏输入法
    private class HideTextWatcher implements TextWatcher {
        //声明一个编辑框对象
        private EditText mView;
        //声明一个最大长度变量
        private int mMaxLength;
        public HideTextWatcher(EditText v, int maxLength) {
            this.mView=v;
            this.mMaxLength=maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //在输入框的输入文本变化后触发
        @Override
        public void afterTextChanged(Editable s) {
            //获得已输入的文本字符串
            String str=s.toString();
            //输入文本达到11位（如手机号码），或达到6位（如登录密码）时关闭输入法
            if(str.length() == mMaxLength){
                //隐藏输入法键盘
                ViewUtil.hindOneInputMethod(EditHideActivity.this,mView);
            }
        }
    }
}