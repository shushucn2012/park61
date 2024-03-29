package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * 商城搜索界面
 */
public class DreamActTempSearchActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_search;
    private EditText edit_sousuo;
    private ImageView img_del;
    private String keyword;//搜索关键字

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_sales_search);
    }

    @Override
    public void initView() {
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        img_del = (ImageView) findViewById(R.id.img_del);
        tv_search = (TextView) findViewById(R.id.tv_search);
    }

    @Override
    public void initData() {
        edit_sousuo.setHint("输入游戏名称");
        keyword = getIntent().getStringExtra("KEY_WORD");
        if (!TextUtils.isEmpty(keyword)) {
            edit_sousuo.setText(keyword);
        }
    }

    @Override
    public void initListener() {
        img_del.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        edit_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_sousuo.getText().toString())) {
                    img_del.setVisibility(View.GONE);
                } else {
                    img_del.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent();
                keyword = edit_sousuo.getText().toString().trim();
                intent.putExtra("KEY_WORD", keyword);
                setResult(RESULT_OK, intent);
                //CommonMethod.startOnlyNewActivity(mContext, DreamChooseActTempActivity.class, intent);
                finish();
                break;
            case R.id.img_del:
                edit_sousuo.setText("");
                img_del.setVisibility(View.GONE);
                break;
        }
    }


}
