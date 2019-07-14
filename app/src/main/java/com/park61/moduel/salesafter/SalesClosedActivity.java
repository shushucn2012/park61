package com.park61.moduel.salesafter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;

/**
 * 售后关闭界面
 * @author Lucia
 *
 */
public class SalesClosedActivity extends BaseActivity {
	private TextView reason_value, refund_amount, regoods_amount,
	detail_value,sales_after_number_value,close_time_value,request_time_value,close_reason_value;
	private int returnGoodsNum;//退回商品总数
	private float grfAmount;////退货金额：商品金额+运费
	private long grfApplyTime;//申请时间
	private long updateTime;//撤销售后申请时间
	private String closeReason;//售后关闭原因;
	private TextView sales_after_policy;
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_sales_after_cancle);

	}

	@Override
	public void initView() {
		reason_value = (TextView) findViewById(R.id.refund_reason_value);
		refund_amount = (TextView) findViewById(R.id.refund_amount_value);
		regoods_amount = (TextView) findViewById(R.id.regoods_amount_value);
		detail_value = (TextView) findViewById(R.id.detail_value);
		sales_after_number_value = (TextView) findViewById(R.id.sales_after_number_value);
		close_time_value = (TextView) findViewById(R.id.close_time_value);
		close_reason_value = (TextView) findViewById(R.id.close_reason_value);
		request_time_value = (TextView) findViewById(R.id.request_time_value);
		sales_after_policy = (TextView) findViewById(R.id.sales_after_policy);

	}

	@Override
	public void initData() {
		reason_value.setText(getIntent().getStringExtra("grfReason"));
		detail_value.setText(getIntent().getStringExtra("remark"));
		regoods_amount.setText(""+getIntent().getIntExtra("returnGoodsNum", returnGoodsNum));		
		refund_amount.setText(""+getIntent().getFloatExtra("grfAmount", grfAmount));		
		sales_after_number_value.setText(getIntent().getStringExtra("grfCode"));
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		grfApplyTime=Long.valueOf(getIntent().getLongExtra("grfApplyTime", grfApplyTime));
		request_time_value.setText(sdf.format(new Date(grfApplyTime)));
		updateTime = getIntent().getLongExtra("updateTime",updateTime);
		close_time_value.setText(sdf.format(new Date(updateTime)));
		closeReason = getIntent().getStringExtra("closeReason");
		close_reason_value.setText(closeReason);
	}

	@Override
	public void initListener() {
		sales_after_policy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SalesAfterPolicyActivity.class));
				
			}
		});

	}

}
