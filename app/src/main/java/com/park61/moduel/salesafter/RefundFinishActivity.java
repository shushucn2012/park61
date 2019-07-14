package com.park61.moduel.salesafter;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.park61.BaseActivity;
import com.park61.R;

/**
 * 退款完成的界面（退货状态：4、系统打款）
 * 
 * @author Lucia
 * 
 */
public class RefundFinishActivity extends BaseActivity {
	private TextView refund_amount_value, refund_reason_value;
	private float grfAmount;// //退货金额：商品金额+运费
	private long refundWay;// 退款方式
	private TextView sales_after_policy;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_sales_after_finish);

	}

	@Override
	public void initView() {
		refund_amount_value = (TextView) findViewById(R.id.refund_amount_value);
		refund_reason_value = (TextView) findViewById(R.id.refund_reason_value);
		sales_after_policy = (TextView) findViewById(R.id.sales_after_policy);

	}

	@Override
	public void initData() {
		grfAmount = getIntent().getFloatExtra("grfAmount", grfAmount);
		refundWay = getIntent().getLongExtra("refundWay", refundWay);
		refund_amount_value.setText("" + grfAmount);
		if (refundWay == 1) {
			refund_reason_value.setText("余额支付");
		} else if (refundWay == 2) {
			refund_reason_value.setText("支付宝支付");
		} else if (refundWay == 3) {
			refund_reason_value.setText("微信支付");
		}
	}

	@Override
	public void initListener() {
		sales_after_policy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext,
						SalesAfterPolicyActivity.class));

			}
		});

	}

}
