package com.example.gastronome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gastronome.adapter.DynamicsListAdapter;
import com.example.gastronome.adapter.ReplyListAdapter;
import com.example.gastronome.database.CommentDBHelper;
import com.example.gastronome.database.UserDBHelper;
import com.example.gastronome.database.WorkDBHelper;
import com.example.gastronome.entity.Comment;
import com.example.gastronome.entity.Dynamics;
import com.example.gastronome.entity.Reply;
import com.example.gastronome.entity.User;
import com.example.gastronome.entity.Work;
import com.example.gastronome.util.DateUtil;
import com.example.gastronome.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_comment;
    private Button btn_publish;
    private ListView lv_comment;
    private User mUser;
    private int work_id;
    private int author_id;
    private CommentDBHelper mCommentDBHelper;
    private UserDBHelper mUserDBHelper;
    private List<Reply> replyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("评论详情");
        iv_back.setOnClickListener(this);

        et_comment = findViewById(R.id.et_comment);
        btn_publish = findViewById(R.id.btn_publish);
        btn_publish.setOnClickListener(this);
        lv_comment = findViewById(R.id.lv_comment);

        //获取用户信息
        MyApplication app = MyApplication.getInstance();
        mUser = app.user;

        // 从上一个页面传来的意图中获取快递包裹
        work_id = getIntent().getIntExtra("work_id",0);
        author_id = getIntent().getIntExtra("author_id",0);


        //打开数据库读写连接
        mCommentDBHelper = CommentDBHelper.getInstance(this);
        mCommentDBHelper.openReadLink();
        mCommentDBHelper.openWriteLink();

        mUserDBHelper = UserDBHelper.getInstance(this);
        mUserDBHelper.openReadLink();
        mUserDBHelper.openWriteLink();

        initComment();
    }

    private void initComment() {
        replyList = new ArrayList<>();
        List<Comment> commentList = mCommentDBHelper.getCommentListByWid(work_id);
        for(Comment c:commentList){
            Reply reply = new Reply();
            reply.comment = c;
            reply.from_user = mUserDBHelper.getUserById(c.from_uid);
            //reply.to_user = mUserDBHelper.getUserById();
            replyList.add(reply);
        }

        ReplyListAdapter adapter = new ReplyListAdapter(this,replyList);
        lv_comment.setAdapter(adapter);
//        DynamicsListAdapter dynamicsListAdapter = new DynamicsListAdapter(this, dynamicsList);
//        lv_dynamics.setAdapter(dynamicsListAdapter);
//        lv_dynamics.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        //Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_publish:
                String content = et_comment.getText().toString();
                if(content.length() == 0){
                    ToastUtil.show(this,"请编辑评论内容！");
                    break;
                }
                //获取当前时间
                Calendar calendar = Calendar.getInstance();
                Comment comment = new Comment();
                comment.wid = work_id;
                comment.from_uid = mUser.id;
                comment.to_uid = author_id;
                comment.parent_id = -1;
                comment.content = content;
                comment.date = DateUtil.getDate(calendar);
                if(mCommentDBHelper.addComment(comment) > 0){
                    ToastUtil.show(this,"评论成功！");
                    //刷新
                    initComment();
                }else{
                    ToastUtil.show(this,"ERROR！");
                }
                break;
        }
    }
}