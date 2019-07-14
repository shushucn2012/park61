package com.park61.moduel.firsthead;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.CommonMethod;
import com.park61.databinding.ActivityToyNumBinding;

/**
 * Created by chenlie on 2018/1/4.
 *
 */

public class ToyNumActivity extends BaseActivity {

    ActivityToyNumBinding binding;
    public static final int REQUEST_CODE = 0x0011;
    public static final int RESULT_CODE = 0x0012;
    private int num;
    private int position;
    private int inputNum;

    @Override
    public void setLayout() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_toy_num);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        num = getIntent().getIntExtra("num", -1);
        position = getIntent().getIntExtra("position", -1);
        inputNum = getIntent().getIntExtra("tempNum", -1);
        binding.toyInputNum.setText(inputNum + "");
        binding.toyInputNum.setSelection(String.valueOf(inputNum).length());
    }

    @Override
    public void initListener() {
        binding.setConfirmNum(v->{
            CommonMethod.hideSoftKeyboard(binding.toyInputNum);

            if(inputNum < num){
                binding.toyInputNum.setText(num+"");
                Toast.makeText(mContext, "至少购买"+num+"件", Toast.LENGTH_SHORT).show();
            }else{
                Intent it = new Intent();
                it.putExtra("position", position);
                it.putExtra("tempNum", inputNum);
                setResult(RESULT_CODE, it);
                finish();
            }
        });

        binding.toyInputNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if(!TextUtils.isEmpty(input)){
                    inputNum = Integer.valueOf(input);

                    //首位为0的两位数，设置为0
                    if(input.length()==2 && inputNum < 10){
                        binding.toyInputNum.setText(inputNum+"");
                    }

                    if(inputNum > 99){
                        inputNum = 99;
                        binding.toyInputNum.setText("99");
                        Toast.makeText(mContext, "最多购买99件哦", Toast.LENGTH_SHORT).show();
                    }
                    binding.toyInputNum.setSelection(String.valueOf(inputNum).length());
                }
            }
        });
    }
}
