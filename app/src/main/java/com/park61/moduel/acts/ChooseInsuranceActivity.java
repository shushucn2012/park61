package com.park61.moduel.acts;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * Created by shushucn2012 on 2016/7/28.
 */
public class ChooseInsuranceActivity extends BaseActivity{

    private TextView tv_name, tv_price;
    private ImageView chk_no, chk_yes;
    private boolean isNeed;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_choose_insurance);
    }

    @Override
    public void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_price= (TextView) findViewById(R.id.tv_price);
        chk_no = (ImageView) findViewById(R.id.chk_no);
        chk_yes = (ImageView) findViewById(R.id.chk_yes);
    }

    @Override
    public void initData() {
        tv_name.setText(getIntent().getStringExtra("name"));
        tv_price.setText(getIntent().getStringExtra("price"));
        isNeed = getIntent().getBooleanExtra("IS_NEED", false);
        if(isNeed){
            chk_no.setImageResource(R.drawable.xuanze_default2);
            chk_yes.setImageResource(R.drawable.xuanze_focus);
        }else{
            chk_no.setImageResource(R.drawable.xuanze_focus);
            chk_yes.setImageResource(R.drawable.xuanze_default2);
        }
    }

    @Override
    public void initListener() {
        chk_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chk_no.setImageResource(R.drawable.xuanze_focus);
                chk_yes.setImageResource(R.drawable.xuanze_default2);
                Intent returnData = new Intent();
                returnData.putExtra("IS_NEED", false);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
        chk_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chk_no.setImageResource(R.drawable.xuanze_default2);
                chk_yes.setImageResource(R.drawable.xuanze_focus);
                Intent returnData = new Intent();
                returnData.putExtra("IS_NEED", true);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }
}
