package com.example.gastronome;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.entity.User;
import com.example.gastronome.util.DateUtil;
import com.example.gastronome.util.PermissionUtil;
import com.example.gastronome.util.ToastUtil;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] PERMISSIONS_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE_STORAGE = 1;

    private EditText et_account;
    private EditText et_pwd;
    private EditText et_pwd_again;
    private EditText et_name;
    private Spinner sp_area;
    private TextView tv_photo;
    private ImageView iv_photo;
    private ActivityResultLauncher<Intent> register;

    //声明一个用户表数据库的帮助器对象
    private UserDBHelper mUserDBHelper;
    private Calendar calendar;
    private String picturePath;
    private String[] cityArray;
    private String selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_account = findViewById(R.id.et_account);
        et_pwd = findViewById(R.id.et_pwd);
        et_pwd_again = findViewById(R.id.et_pwd_again);
        et_name = findViewById(R.id.et_name);
        sp_area = findViewById(R.id.sp_area);
        tv_photo = findViewById(R.id.tv_photo);
        iv_photo = findViewById(R.id.iv_photo);
        cityArray = getResources().getStringArray(R.array.cities);

        iv_photo.setOnClickListener(this);
        findViewById(R.id.btn_register_action).setOnClickListener(this);

        // 声明一个下拉列表的数组适配器
        ArrayAdapter<String> citySelectAdapter = new ArrayAdapter<>(this, R.layout.item_city_selector, cityArray);
        // 设置下拉框的标题。对话框模式才显示标题，下拉模式不显示标题
        sp_area.setPrompt("请选择:");
        sp_area.setAdapter(citySelectAdapter);
        // 设置下拉框显示第一项
        //sp_area.setSelection(0);
        // 给下拉框设置选择监听器，一旦用户选中某一项，就触发监听器的onItemSelected方法
        //地区选择监听器
        sp_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCity = cityArray[position];
                sp_area.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //设为未知
                //selectedCity = cityArray[0];
            }
        });

        //避免空指针的初始化图片
        //picturePath = "/storage/emulated/0/baidu/searchbox/downloads/plus.jpg";
        //选择本地图片的回调函数
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result != null){
                    Log.d("Jun","filePathColumn isn't null");
                    Intent intent = result.getData();
                    Uri uri = intent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    // 获取列的指针
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    // 根据指针获取图片路径
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.d("Jun","cursor is ok :" + picturePath);
                    // 使用地址获取图片
                    iv_photo.setImageURI(Uri.parse(picturePath));
                    if(picturePath != null){
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        iv_photo.setImageBitmap(bitmap);
                    }else{
                        Log.d("Jun","picturePath is null");
                    }
                }else{
                    Log.d("Jun","result is null");
                }
            }
        });

        //打开数据库读写连接
        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_photo:
                //获取存储访问权限
                PermissionUtil.checkPermission(this,PERMISSIONS_EXTERNAL_STORAGE,REQUEST_CODE_STORAGE);
                //选择本地图片
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                register.launch(intent);
                Log.d("Jun","Jump");
                break;
            case R.id.btn_register_action:
                //验证注册信息
                if( !checkRegister()){
                    break;
                }
                //获取当前时间
                calendar = Calendar.getInstance();
                //构造User实例
                User user = new User();
                user.account = et_account.getText().toString();
                user.password = et_pwd.getText().toString();
                user.name = et_name.getText().toString();
                user.area = selectedCity;
                user.picPath = picturePath;
                user.date=DateUtil.getDate(calendar);
                Log.d("Jun",user.toString());
                if(mUserDBHelper.save(user) > 0){
                    ToastUtil.show(this, "注册成功！");
                    //跳转到登录页面
                    Intent intent_login = new Intent(this, LoginActivity.class);
                    intent_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //将当前登录信息保存到全局
                    MyApplication app = MyApplication.getInstance();
                    app.loginAccount = et_account.getText().toString();
                    app.loginPassword = et_pwd.getText().toString();
                    // 创建一个新包裹,保存登录信息
//                    Bundle bundle = new Bundle();
//                    bundle.putString("account", et_account.getText().toString());
//                    bundle.putString("password", et_pwd.getText().toString());
//                    intent_login.putExtras(bundle);
                    startActivity(intent_login);
                    ToastUtil.show(this, "点击登录！");
                }else{
                    ToastUtil.show(this, "ERROR！");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭数据库连接
        mUserDBHelper.closeLink();
    }

    //检查注册信息
    private boolean checkRegister() {
        String account = et_account.getText().toString();
        if (account.length() == 0 ) {
            ToastUtil.show(this,"请输入账号！");
            return false;
        }
        if (account.length() < 6) {
            ToastUtil.show(this,"账号应不少于6位！");
            return false;
        }

        String password = et_pwd.getText().toString();
        if (password.length() == 0) {
            ToastUtil.show(this,"请输入密码！");
            return false;
        }
        if (password.length() < 6) {
            ToastUtil.show(this,"密码应不少于6位！");
            return false;
        }
        String password_again = et_pwd_again.getText().toString();
        if (!(password.equals(password_again))) {
            ToastUtil.show(this,"密码前后输入不一致！");
            return false;
        }
        String name = et_name.getText().toString();
        if (name.length() == 0) {
            ToastUtil.show(this,"请输入昵称！");
            return false;
        }

        if(picturePath == null){
            ToastUtil.show(this,"请选择一个头像！");
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtil.checkGrant(grantResults)) {
            Log.d("Jun", "存储权限获取成功");
        } else {
            ToastUtil.show(this, "获取存储读写权限失败！");
            jumpToSettings();
        }
    }

    private void jumpToSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        tv_photo.setHint("点击更换头像");
    }

}