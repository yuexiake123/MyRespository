package com.example.gastronome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_account;
    private EditText et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_account = findViewById(R.id.et_account);
        et_pwd = findViewById(R.id.et_pwd);
        Button  btn_login_action=findViewById(R.id.btn_login_action);

        btn_login_action.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        String account=et_account.getText().toString();
        String pwd=et_pwd.getText().toString();
        Log.d("Jun","111");
       // login(account,pwd);
        Intent intent = new Intent(this,HomeActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void login(String account, String pwd) {
        if(account == null || account.length()<=0){
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
        }
        if(pwd == null || pwd.length()<=0){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
        }
    }
}