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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gastronome.database.CategoryDBHelper;
import com.example.gastronome.database.CollectDBHelper;
import com.example.gastronome.database.CommentDBHelper;
import com.example.gastronome.database.LikeDBHelper;
import com.example.gastronome.database.ShareDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;
import com.example.gastronome.util.PermissionUtil;
import com.example.gastronome.util.ToastUtil;
import com.example.gastronome.view.FlowLayout;

import java.util.List;

public class MyWorkDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String[] PERMISSIONS_EXTERNAL_STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static final int REQUEST_CODE_STORAGE = 1;

    private WorkDBHelper mWorkDBHelper;
    private CategoryDBHelper mCategoryDBHelper;
    private CollectDBHelper mCollectDBHelper;
    private LikeDBHelper mLikeDBHelper;
    private ShareDBHelper mShareDBHelper;
    private CommentDBHelper mCommentDBHelper;
    private User user;
    private Work work;
    private ImageView iv_author_head;
    private TextView tv_author_name;
    private TextView tv_author_area;
    private ImageView iv_photo;
    private EditText et_name;
    private EditText et_description;
    private EditText et_time;
    private EditText et_burdening;
    private EditText et_ingredient;
    private EditText et_step;
    private FlowLayout fl_tag;
    private List<String> selectedTagList;
    private TextView tv_like_count;
    private TextView tv_collect_count;
    private TextView tv_comment_count;
    private TextView tv_share_count;
    private ImageView iv_share;

    private ActivityResultLauncher<Intent> register;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_detail);
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("作品详情");
        iv_back.setOnClickListener(this);

        iv_author_head = findViewById(R.id.iv_author_head);
        tv_author_name = findViewById(R.id.tv_author_name);
        tv_author_area = findViewById(R.id.tv_author_area);

        iv_photo = findViewById(R.id.iv_photo);
        et_name = findViewById(R.id.et_name);
        et_description = findViewById(R.id.et_description);
        et_time = findViewById(R.id.et_time);
        et_burdening = findViewById(R.id.et_burdening);
        et_ingredient = findViewById(R.id.et_ingredient);
        et_step = findViewById(R.id.et_step);
        fl_tag = findViewById(R.id.fl_tag);

        tv_like_count = findViewById(R.id.tv_like_count);
        tv_collect_count = findViewById(R.id.tv_collect_count);
        tv_comment_count = findViewById(R.id.tv_comment_count);
        tv_share_count = findViewById(R.id.tv_share_count);
        iv_share = findViewById(R.id.iv_share);

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

        mCollectDBHelper = CollectDBHelper.getInstance(this);
        mCollectDBHelper.openReadLink();
        mCollectDBHelper.openWriteLink();

        mLikeDBHelper = LikeDBHelper.getInstance(this);
        mLikeDBHelper.openReadLink();
        mLikeDBHelper.openWriteLink();

        mShareDBHelper = ShareDBHelper.getInstance(this);
        mShareDBHelper.openReadLink();
        mShareDBHelper.openWriteLink();

        mCommentDBHelper = CommentDBHelper.getInstance(this);
        mCommentDBHelper.openReadLink();
        mCommentDBHelper.openWriteLink();

        // 从上一个页面传来的意图中获取快递包裹
        int work_id = getIntent().getIntExtra("work_id",0);
        work = mWorkDBHelper.getWorkById(work_id);
        picturePath = work.picPath;//避免空指针
        Log.d("Jun",work.toString());
        showData();

        iv_photo.setOnClickListener(this);
        updatePhoto();

        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.iv_comment).setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWorkDBHelper.closeLink();
        mCategoryDBHelper.closeLink();
    }

    private void updatePhoto() {
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result != null){
                    //Log.d("Jun","filePathColumn isn't null");
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

    private void showData() {
        //头像
        String picPath = user.picPath;
        iv_author_head.setImageURI(Uri.parse(picPath));
        if (picPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            iv_author_head.setImageBitmap(bitmap);
        }
        tv_author_name.setText(user.name);
        tv_author_area.setText(user.area);
        //图片
        picPath = work.picPath;
        iv_photo.setImageURI(Uri.parse(picPath));
        if (picPath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            iv_photo.setImageBitmap(bitmap);
        }
        //其他信息
        et_name.setText(work.name);
        et_description.setText(work.description);
        et_time.setText(work.time);
        et_burdening.setText(work.burdening);
        et_ingredient.setText(work.ingredient);
        et_step.setText(work.step);
        //标签
        selectedTagList = mCategoryDBHelper.getCategoryByWid(work.id);
        printSelectedTag();
        //获取标签
        String[] tagArray = getResources().getStringArray(R.array.tags);
        LayoutInflater mlayoutInflater = LayoutInflater.from(this);
        for (int i = 0; i < tagArray.length; i++) {
            TextView view = (TextView) mlayoutInflater.inflate(R.layout.item_flow, fl_tag, false);
            final String str = tagArray[i];
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkIsSelected(str)) {
                        v.setBackgroundResource(R.drawable.shape_flow);
                        selectedTagList.remove(str);
                        printSelectedTag();
                    } else {
                        //尚未添加
                        v.setBackgroundResource(R.drawable.shape_flow_selected);
                        selectedTagList.add(str);
                        printSelectedTag();
                    }
                }
            });
            view.setText(tagArray[i]);
            if(checkIsSelected(tagArray[i])){
                view.setBackgroundResource(R.drawable.shape_flow_selected);
            }
            fl_tag.addView(view);
        }


        //点赞
        int mLikeCount = work.like;
        tv_like_count.setText(Integer.toString(mLikeCount));

        //收藏
        int mCollectCount = mCollectDBHelper.getCollectCountByWid(work.id);
        tv_collect_count.setText(Integer.toString(mCollectCount));

        //分享
        int mShareCount = mShareDBHelper.getShareCountByWid(work.id);
        tv_share_count.setText(Integer.toString(mShareCount));
//        if(mShareDBHelper.checkIsShared(user.id,work.id)){
//            //已分享
//            iv_share.setImageResource(R.drawable.ic_outline_reply_24_selected);
//        }

        //评论
        int mCommentCount = mCommentDBHelper.getCommentCountByWid(work.id);
        tv_comment_count.setText(Integer.toString(mCommentCount));

    }

    private void printSelectedTag() {
        for(String str : selectedTagList){
            Log.d("Jun",str);
        }
        Log.d("Jun","--------------");
    }

    private boolean checkIsSelected(String tag) {
        for(String str : selectedTagList){
            if(str.equals(tag)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        AlertDialog.Builder builder;
        switch (view.getId()){
            case R.id.iv_back:
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
                builder = new AlertDialog.Builder(this);
                builder.setMessage("确认修改？");
                builder.setPositiveButton("是",(dialog,which)->{
                    Work tmpWork = new Work();
                    tmpWork.id = work.id;
                    tmpWork.name = et_name.getText().toString();
                    tmpWork.description = et_description.getText().toString();
                    tmpWork.time = et_time.getText().toString();
                    tmpWork.burdening = et_burdening.getText().toString();
                    tmpWork.ingredient = et_ingredient.getText().toString();
                    tmpWork.step = et_step.getText().toString();
                    tmpWork.picPath = picturePath;
                    if(mWorkDBHelper.updateWork(tmpWork) > 0){
                        //保存分类标签
                        mCategoryDBHelper.updateCategoryByWid(work.id,selectedTagList);
                        ToastUtil.show(this, "修改成功！");
                        finish();
                    }else{
                        ToastUtil.show(this, "ERROR！");
                    }
                });
                builder.setNegativeButton("否",null);
                builder.create().show();
                break;
            case R.id.btn_delete:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("确认删除？");
                builder.setPositiveButton("是",(dialog,which)->{
                    if(mWorkDBHelper.deleteWorkById(work.id) > 0 && mCategoryDBHelper.deleteCategoryByWid(work.id) > 0){
                        //删除收藏映射
                        mCollectDBHelper.deleteCollectByWid(work.id);
                        //删除点赞映射
                        mLikeDBHelper.deleteLikeByWid(work.id);
                        //删除转发映射
                        mShareDBHelper.deleteShareByWid(work.id);
                        ToastUtil.show(this, "删除成功！");
                        finish();
                    }else{
                        ToastUtil.show(this, "ERROR！");
                    }
                });
                builder.setNegativeButton("否",null);
                builder.create().show();
                break;
            case R.id.iv_comment:
                intent = new Intent(this, CommentDetailActivity.class);
                intent.putExtra("author_id",user.id);
                intent.putExtra("work_id",work.id);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}

