package com.example.chapter03;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_result;
    //第一个操作数
    private String firstNum="";
    //运算符
    private String operator="";
    //第二个操作数
    private String secondNum="";
    //当前的计算结果
    private String result="";
    //显示的文本内容
    private String showText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        //从布局文件中获取名叫tv_result的文本视图
        tv_result=findViewById(R.id.tv_result);
        //下面给每个按钮控件都注册点击监听器
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_multiply).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_seven).setOnClickListener(this);
        findViewById(R.id.btn_eight).setOnClickListener(this);
        findViewById(R.id.btn_nine).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_four).setOnClickListener(this);
        findViewById(R.id.btn_five).setOnClickListener(this);
        findViewById(R.id.btn_six).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_one).setOnClickListener(this);
        findViewById(R.id.btn_two).setOnClickListener(this);
        findViewById(R.id.btn_three).setOnClickListener(this);
        findViewById(R.id.btn_reciprocal).setOnClickListener(this);
        findViewById(R.id.btn_zero).setOnClickListener(this);
        findViewById(R.id.btn_dot).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.ib_sqrt).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String inputText;
        //如果是开根号按钮
        if(view.getId()==R.id.ib_sqrt){
            inputText="√";
        }else{
            inputText=((TextView) view).getText().toString();
        }
        switch (view.getId()){
            //点击了清楚按钮
            case R.id.btn_clear:
                clear();
                break;
            //点击了取消按钮
            case R.id.btn_cancel:
                break;
            //点击了加减乘除
            case R.id.btn_plus:
            case R.id.btn_minus:
            case R.id.btn_multiply:
            case R.id.btn_divide:
                operator=inputText;
                refreshText(showText+operator);
                break;
            //点击了等号按钮
            case R.id.btn_equal:
                double calculate_result=calculateFour();
                refreshOperate(String.valueOf(calculate_result));
                refreshText(showText+"="+result);
                break;
            //点击了开根号按钮
            case R.id.ib_sqrt:
                double sqrt_result=Math.sqrt(Double.parseDouble(firstNum));
                refreshOperate(String.valueOf(sqrt_result));
                refreshText(showText+"√="+result);
                break;
            //点击了求倒数按钮
            case R.id.btn_reciprocal:
                double reciprocal_result=1.0/Double.parseDouble(firstNum);
                refreshOperate(String.valueOf(reciprocal_result));
                refreshText(showText+"/="+result);
                break;
            //点击了其他按钮
            default:
                //上次运算结果已经出来了
                if(result.length()>0&&operator.equals("")){
                    clear();
                }
                //无运算符，则继续拼接第一个操作数
                if(operator.equals("")){
                    firstNum=firstNum+inputText;
                }else{
                    //有运算符，则继续拼接第二个操作数
                    secondNum=secondNum+inputText;
                }
                //整数不需要前面的0
                if(showText.equals("0")&&!inputText.equals(".")){
                    refreshText(inputText);
                }else{
                    refreshText(showText+inputText);
                }
                break;
        }

    }

    private double calculateFour() {
        switch (operator){
            case "+":
                return Double.parseDouble(firstNum)+Double.parseDouble(secondNum);
            case "-":
                return Double.parseDouble(firstNum)-Double.parseDouble(secondNum);
            case "x":
                return Double.parseDouble(firstNum)*Double.parseDouble(secondNum);
            default:
                return Double.parseDouble(firstNum)/Double.parseDouble(secondNum);
        }
    }

    private void clear() {
        refreshOperate("");
        refreshText("");

    }

    //刷新运算结果
    private  void refreshOperate(String new_result){
        result=new_result;
        firstNum=result;
        secondNum="";
        operator="";
    }

    //刷新文本显示
    private void refreshText(String text){
        showText=text;
        tv_result.setText(showText);
    }
}