package com.example.gastronome;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gastronome.database.CategoryDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;
import com.example.gastronome.util.DateUtil;
import com.example.gastronome.util.PermissionUtil;
import com.example.gastronome.util.ToastUtil;
import com.example.gastronome.view.FlowLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddWorkActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String[] PERMISSIONS_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE_STORAGE = 1;

    private List<String> selectedTag =new ArrayList<>();
    private FlowLayout fl_tag;

    private ImageView iv_photo;
    private ActivityResultLauncher<Intent> register;
    private String picturePath;
    private EditText et_name;
    private EditText et_description;
    private EditText et_time;
    private EditText et_burdening;
    private EditText et_ingredient;
    private EditText et_step;
    private Calendar calendar;
    private User user;
    private WorkDBHelper mWorkDBHelper;
    private CategoryDBHelper mCategoryDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work);

        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("添加作品");
        iv_back.setOnClickListener(this);

        iv_photo = findViewById(R.id.iv_photo);
        iv_photo.setOnClickListener(this);
        addPhoto();
        //分类标签
        initTag();

        et_name = findViewById(R.id.et_name);
        et_description = findViewById(R.id.et_description);
        et_time = findViewById(R.id.et_time);
        et_burdening = findViewById(R.id.et_burdening);
        et_ingredient = findViewById(R.id.et_ingredient);
        et_step = findViewById(R.id.et_step);
        findViewById(R.id.btn_save).setOnClickListener(this);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        user = app.user;

        //打开数据库读写连接
        mWorkDBHelper = WorkDBHelper.getInstance(this);
        mWorkDBHelper.openReadLink();
        mWorkDBHelper.openWriteLink();

        mCategoryDBHelper = CategoryDBHelper.getInstance(this);
        mCategoryDBHelper.openReadLink();
        mCategoryDBHelper.openWriteLink();

    }

    private void addPhoto() {
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
                    iv_photo.setImageURI(Uri.parse(picturePath));
                    if(picturePath != null){
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        iv_photo.setImageBitmap(bitmap);
                    }else{
                        //Log.d("Jun","picturePath is null");
                    }
                }else{
                    //Log.d("Jun","result is null");
                }
            }
        });
    }

    private void printSelectedTag() {
        for(String str : selectedTag){
            Log.d("Jun",str);
        }
        Log.d("Jun","--------------");
    }

    private boolean checkSelected(String string) {
        for(String str : selectedTag){
            if(str.equals(string)){
                return true;
            }
        }
        return false;
    }

    private void initTag() {
        fl_tag = findViewById(R.id.fl_tag);
        //获取标签
        String[] tagArray = getResources().getStringArray(R.array.tags);

        LayoutInflater mlayoutInflater = LayoutInflater.from(this);

        for (int i = 0; i < tagArray.length; i++) {
//            Button button = new Button(this);
//            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            button.setLayoutParams(lp);
//            button.setText(mValus[i]);
//            mFlowLayout.addView(button);

            TextView view = (TextView) mlayoutInflater.inflate(R.layout.item_flow,fl_tag,false);
            final String str = tagArray[i];
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkSelected(str)){
                        v.setBackgroundResource(R.drawable.shape_flow);
                        selectedTag.remove(str);
                        printSelectedTag();
                    } else{
                        //尚未添加
                        v.setBackgroundResource(R.drawable.shape_flow_selected);
                        selectedTag.add(str);
                        printSelectedTag();
                    }
                }
            });
            view.setText(tagArray[i]);
            fl_tag.addView(view);
        }
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_back:
                //关闭当前页面
                finish();
                break;
            case R.id.iv_photo:
                //获取存储访问权限
                PermissionUtil.checkPermission(this,PERMISSIONS_EXTERNAL_STORAGE,REQUEST_CODE_STORAGE);
                //选择本地图片
                intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                register.launch(intent);
                break;
            case R.id.btn_save:
                if(! checkEdit()){
                    break;
                }
                //获取当前时间
                calendar = Calendar.getInstance();
                //构造Work实例
                Work work = new Work();
                work.uid = user.id;
                work.name = et_name.getText().toString();
                work.description = et_description.getText().toString();
                work.time = et_time.getText().toString();
                work.burdening = et_burdening.getText().toString();
                work.ingredient = et_ingredient.getText().toString();
                work.step = et_step.getText().toString();
                work.picPath = picturePath;
                work.like = 0;
                work.date = DateUtil.getDate(calendar);
                Log.d("Jun",work.toString());
                if(mWorkDBHelper.save(work) > 0){
                    int wid = mWorkDBHelper.getLastWorkId();
                    //Log.d("Jun",Integer.toString(wid));
                    //保存分类标签
                    mCategoryDBHelper.save(wid,selectedTag);
                    ToastUtil.show(this, "发布成功！");
                    finish();
                }else{
                    ToastUtil.show(this, "ERROR！");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorkDBHelper.closeLink();
        mCategoryDBHelper.closeLink();
    }

    private boolean checkEdit() {
        if(picturePath == null){
            ToastUtil.show(this,"请选择图片！");
            return false;
        }
        String editText = et_name.getText().toString();
        if(editText.length() == 0){
            ToastUtil.show(this,"请输入菜谱名称！");
            return false;
        }
        editText = et_burdening.getText().toString();
        if(editText.length() == 0){
            ToastUtil.show(this,"请输入食材！");
            return false;
        }
        editText = et_step.getText().toString();
        if(editText.length() == 0){
            ToastUtil.show(this,"请输入制作步骤！");
            return false;
        }
        if(selectedTag == null){
            ToastUtil.show(this,"请选择分类标签！");
            return false;
        }
        return true;
    }
}