package com.example.gastronome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.entity.User;
import com.example.gastronome.util.ToastUtil;

public class UpdatePWDActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_origin;
    private EditText et_new;
    private EditText et_again;
    private MyApplication app;
    private User user;
    private UserDBHelper mUserDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwdactivity);

        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("修改密码");
        iv_back.setOnClickListener(this);

        et_origin = findViewById(R.id.et_origin);
        et_new = findViewById(R.id.et_new);
        et_again = findViewById(R.id.et_again);

        findViewById(R.id.btn_save).setOnClickListener(this);

        //获取用户信息
        app = MyApplication.getInstance();
        user = app.user;

        //打开数据库读写连接
        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
            finish();
            break;
            case R.id.btn_save:
                if(!checkEdit()){
                    break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePWDActivity.this);
                builder.setMessage("确认修改？");
                builder.setPositiveButton("是",(dialog,which)->{
                    String pwd_origin = et_origin.getText().toString();
                    String pwd_new = et_new.getText().toString();
                    if(mUserDBHelper.updatePassword(user,pwd_origin,pwd_new) > 0){
                        ToastUtil.show(this, "修改成功,请重新登录！");
                        app.loginPassword = pwd_new;
                        Intent intent = new Intent(this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        ToastUtil.show(this, "原密码验证失败，请重试！");
                    }
                });
                builder.setNegativeButton("否",null);
                builder.create().show();
                break;
        }
    }

    private boolean checkEdit() {
        String password = et_origin.getText().toString();
        if (password.length() == 0) {
            ToastUtil.show(this,"请输入原密码！");
            return false;
        }
        password = et_new.getText().toString();
        if (password.length() == 0) {
            ToastUtil.show(this,"请输入新密码！");
            return false;
        }
        if (password.length() < 6) {
            ToastUtil.show(this,"密码应不少于6位！");
            return false;
        }
        String password_again = et_again.getText().toString();
        if (!(password.equals(password_again))) {
            ToastUtil.show(this,"密码前后输入不一致！");
            return false;
        }
        return true;
    }
}