package com.example.gastronome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.entity.User;
import com.example.gastronome.util.ToastUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_account;
    private EditText et_pwd;

    //声明一个用户表数据库的帮助器对象
    private UserDBHelper mUserDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_account = findViewById(R.id.et_account);
        et_pwd = findViewById(R.id.et_pwd);
        findViewById(R.id.btn_login_action).setOnClickListener(this);

        //检查是否存在登录信息
        checkLoginMessage();

        //打开数据库读写连接
        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();
    }

    private void checkLoginMessage() {
        MyApplication app = MyApplication.getInstance();
        if(app.loginAccount != null ){
            et_account.setText(app.loginAccount);
            et_pwd.setText(app.loginPassword);
        }
    }

    @Override
    public void onClick(View view) {
        String account=et_account.getText().toString();
        String password=et_pwd.getText().toString();
        if(!(checkLogin(account,password))){
            return;
        }
        User user = mUserDBHelper.loginVerify(account,password);
        if(user != null){
            //Log.d("Jun","user is ok");
            //将当前用户信息保存到全局
            MyApplication app = MyApplication.getInstance();
            app.user = user;
            app.loginAccount = account;
            app.loginPassword = password;
            //跳转
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ToastUtil.show(this,"欢迎" + user.name + "来到美食家！");
        }else{
            ToastUtil.show(this, "账号或密码错误，请重试！");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭数据库连接
        mUserDBHelper.closeLink();
    }

    private boolean checkLogin(String account, String pwd) {
        if(account == null || account.length()<=0){
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd == null || pwd.length()<=0){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}