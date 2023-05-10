package com.example.gastronome;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.entity.User;
import com.example.gastronome.util.PermissionUtil;
import com.example.gastronome.util.ToastUtil;

public class AccountDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] PERMISSIONS_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE_STORAGE = 1;

    private ImageView iv_head;
    private EditText et_name;
    private Spinner sp_area;
    private User user;
    private String[] cityArray;
    private String selectedCity;
    private ActivityResultLauncher<Intent> register;
    private String picturePath;
    private UserDBHelper mUserDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("账号信息");
        iv_back.setOnClickListener(this);

        iv_head = findViewById(R.id.iv_head);
        et_name = findViewById(R.id.et_name);
        sp_area = findViewById(R.id.sp_area);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        user = app.user;

        showUserData();

        iv_head.setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_alter_pwd).setOnClickListener(this);

        headAlter();
        areaAlter();

        //打开数据库读写连接
        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

    }

    private void areaAlter() {
        cityArray = getResources().getStringArray(R.array.cities);
        // 声明一个下拉列表的数组适配器
        ArrayAdapter<String> citySelectAdapter = new ArrayAdapter<>(this, R.layout.item_city_selector, cityArray);
        // 设置下拉框的标题。对话框模式才显示标题，下拉模式不显示标题
        sp_area.setPrompt("请选择:");
        sp_area.setAdapter(citySelectAdapter);
        //获取当前位置
        for(int i=0; i< 35;i++){
            if(cityArray[i].equals(user.area)){
                selectedCity = user.area;
                sp_area.setSelection(i);
                break;
            }
        }
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
    }

    private void headAlter() {
        picturePath = user.picPath;
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
                    //Log.d("Jun","cursor is ok " + picturePath);
                    // 使用地址获取图片
                    iv_head.setImageURI(Uri.parse(picturePath));
                    if(picturePath != null){
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        iv_head.setImageBitmap(bitmap);
                    }else{
                        Log.d("Jun","picturePath is null");
                    }
                }else{
                    Log.d("Jun","result is null");
                }
            }
        });
    }

    private void showUserData() {
        //头像
        picturePath = user.picPath;
        iv_head.setImageURI(Uri.parse(picturePath));
        if(picturePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            iv_head.setImageBitmap(bitmap);
        }
        //其他信息
        et_name.setText(user.name);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_back:
                //关闭当前页面
                finish();
                break;
            case R.id.iv_head:
                //获取存储访问权限
                PermissionUtil.checkPermission(this,PERMISSIONS_EXTERNAL_STORAGE,REQUEST_CODE_STORAGE);
                //选择本地图片
                intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                register.launch(intent);
                break;
            case R.id.btn_save:
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountDetailActivity.this);
                builder.setMessage("确认修改？");
                builder.setPositiveButton("是",(dialog,which)->{
                    user.name = et_name.getText().toString();
                    user.area = selectedCity;
                    user.picPath = picturePath;
                    if(mUserDBHelper.updateUser(user) > 0){
                        ToastUtil.show(this, "修改成功！");
                        //将更新的用户信息保存到全局
                        MyApplication app = MyApplication.getInstance();
                        app.user = user;
                        finish();
                    }else{
                        ToastUtil.show(this, "ERROR！");
                    }
                });
                builder.setNegativeButton("否",null);
                builder.create().show();
                break;
            case R.id.btn_alter_pwd:
                intent = new Intent(this,UpdatePWDActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}