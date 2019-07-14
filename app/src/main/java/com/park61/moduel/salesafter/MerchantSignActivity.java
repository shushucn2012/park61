package com.park61.moduel.salesafter;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.ShowBigPicActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 商家已签收等待系统付款的界面（退货状态：3、货品入库）
 * 
 * @author Lucia
 * 
 */
public class MerchantSignActivity extends BaseActivity implements
		OnClickListener {
	private ImageView choice_img;
	private ImageView photo0, photo1, photo2;
	private TextView type_value, reason_value, refund_amount, regoods_amount,
			detail_value, sales_after_number_value, request_time_value,
			regoods_express_value, express_number_value;
	private long grfApplyTime;// 退货申请时间
	private String grfCode;// 退货单编码
	private int returnGoodsNum;// 退回商品总数
	private float grfAmount;// //退货金额：商品金额+运费
	private String remark;// 退货备注
	private String grfReason;// 退货原因
	private int isReturnGoods;// 是否寄回实物：0、寄回，1、不寄回
	private String deliveryCompanyName;// 快递公司名称
	private String waybillCode;// 快递单号
	private ArrayList<String> grfPicUrls = new ArrayList<String>();// 图片地址集合
	private TextView sales_after_policy;

	private int waybillType;
	private String storePhone,storeAddress;
	private TextView regoods_express,express_number;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_sales_after_sign);

	}

	@Override
	public void initView() {
		type_value = (TextView) findViewById(R.id.sales_type_value);
		reason_value = (TextView) findViewById(R.id.refund_reason_value);
		refund_amount = (TextView) findViewById(R.id.refund_amount_value);
		regoods_amount = (TextView) findViewById(R.id.regoods_amount_value);
		detail_value = (TextView) findViewById(R.id.detail_value);
		sales_after_number_value = (TextView) findViewById(R.id.sales_after_number_value);
		request_time_value = (TextView) findViewById(R.id.request_time_value);
		regoods_express_value = (TextView) findViewById(R.id.regoods_express_value);
		express_number_value = (TextView) findViewById(R.id.express_number_value);
		sales_after_policy = (TextView) findViewById(R.id.sales_after_policy);

		regoods_express = (TextView) findViewById(R.id.regoods_express);
		express_number = (TextView) findViewById(R.id.express_number);

		choice_img = (ImageView) findViewById(R.id.choice_img);
		photo0 = (ImageView) findViewById(R.id.photo0);
		photo1 = (ImageView) findViewById(R.id.photo1);
		photo2 = (ImageView) findViewById(R.id.photo2);
	}

	@Override
	public void initData() {
		grfApplyTime = getIntent().getLongExtra("grfApplyTime", grfApplyTime);
		grfCode = getIntent().getStringExtra("grfCode");
		remark = getIntent().getStringExtra("remark");
		grfReason = getIntent().getStringExtra("grfReason");
		returnGoodsNum = getIntent().getIntExtra("returnGoodsNum",
				returnGoodsNum);
		grfAmount = getIntent().getFloatExtra("grfAmount", grfAmount);
		deliveryCompanyName = getIntent().getStringExtra("deliveryCompanyName");
		waybillCode = getIntent().getStringExtra("waybillCode");

		waybillType = getIntent().getIntExtra("waybillType", waybillType);
		storePhone = getIntent().getStringExtra("storePhone");
		storeAddress  = getIntent().getStringExtra("storeAddress");

		reason_value.setText(grfReason);
		detail_value.setText(remark);
		regoods_amount.setText("" + returnGoodsNum);
		refund_amount.setText("" + grfAmount);
		sales_after_number_value.setText(grfCode);
		long time = Long.valueOf(grfApplyTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		request_time_value.setText(sdf.format(new Date(time)));
		if(waybillType==1){//到店自提
			regoods_express.setText("门店电话");
			express_number.setText("门店地址");
			regoods_express_value.setText(storePhone);
			express_number_value.setText(storeAddress);
		}else {//物流
			regoods_express.setText("退货快递");
			express_number.setText("快递单号");
			regoods_express_value.setText(deliveryCompanyName);
			express_number_value.setText(waybillCode);
		}

		grfPicUrls = getIntent().getStringArrayListExtra("grfPicUrls");
		if (grfPicUrls.size() == 1) {
			ImageManager.getInstance().displayImg(photo0, grfPicUrls.get(0));
		} else if (grfPicUrls.size() == 2) {
			ImageManager.getInstance().displayImg(photo0, grfPicUrls.get(0));
			ImageManager.getInstance().displayImg(photo1, grfPicUrls.get(1));
		} else if (grfPicUrls.size() == 3) {
			ImageManager.getInstance().displayImg(photo0, grfPicUrls.get(0));
			ImageManager.getInstance().displayImg(photo1, grfPicUrls.get(1));
			ImageManager.getInstance().displayImg(photo2, grfPicUrls.get(2));
		}
	}

	@Override
	public void initListener() {
		photo0.setOnClickListener(this);
		photo1.setOnClickListener(this);
		photo2.setOnClickListener(this);
		sales_after_policy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext,
						SalesAfterPolicyActivity.class));

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo0:
			Intent it = new Intent(mContext, ShowBigPicActivity.class);
			it.putExtra("picUrl", grfPicUrls.get(0));//当前位置图片的url
			it.putExtra("urlList", grfPicUrls);//图片的url集合
			startActivity(it);
			break;

		case R.id.photo1:
			Intent it1 = new Intent(mContext, ShowBigPicActivity.class);
			it1.putExtra("picUrl", grfPicUrls.get(1));//当前位置图片的url
			it1.putExtra("urlList", grfPicUrls);//图片的url集合
			startActivity(it1);
			break;

		case R.id.photo2:
			Intent it2 = new Intent(mContext, ShowBigPicActivity.class);
			it2.putExtra("picUrl", grfPicUrls.get(2));//当前位置图片的url
			it2.putExtra("urlList", grfPicUrls);//图片的url集合
			startActivity(it2);
			break;

		default:
			break;
		}

	}

}
