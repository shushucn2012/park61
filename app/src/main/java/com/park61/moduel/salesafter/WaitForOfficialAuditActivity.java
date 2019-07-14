package com.park61.moduel.salesafter;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.salesafter.bean.ApplySalesAfterInfo;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * 等待官方审核的界面
 * @author Lucia
 *
 */
public class WaitForOfficialAuditActivity extends BaseActivity implements OnClickListener {

	private TextView type_value, reason_value, refund_amount, regoods_amount,
			detail_value;
	private int returnGoodsNum;//退回商品总数
	private double grfAmount;////退货金额：商品金额+运费
	private String remark;//退货备注
	private String grfReason;//退货原因
	private String typeValue;//售后类型
	private String givebackPicUrl;//图片地址
	private TextView sales_after_policy,sales_after_number_value, request_time_value;;
	private ImageView photo0, photo1, photo2;
	private ArrayList<String> grfPicUrls = new ArrayList<String>();// 图片地址集合
	private Long soId;// 订单id
	private String grfCode;// 退货单编码
	private long grfApplyTime;// 退货申请时间
	private long pmInfoId;//商品id
	@Override
	public void setLayout() {
		setContentView(R.layout.activity_sales_after_wait_for_audit);

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
		sales_after_policy = (TextView) findViewById(R.id.sales_after_policy);
		
		photo0 = (ImageView) findViewById(R.id.photo0);
		photo1 = (ImageView) findViewById(R.id.photo1);
		photo2 = (ImageView) findViewById(R.id.photo2);
	}

	@Override
	public void initData() {
		pmInfoId = getIntent().getLongExtra("pmInfoId",0l);
		soId = getIntent().getLongExtra("soId", 1L);
		remark = getIntent().getStringExtra("remark");
		grfReason = getIntent().getStringExtra("grfReason");
		returnGoodsNum = getIntent().getIntExtra("returnGoodsNum", returnGoodsNum);		
//		grfAmount = getIntent().getFloatExtra("grfAmount", grfAmount);
		grfAmount = (double) getIntent().getExtras().get("grfAmount");
		givebackPicUrl = getIntent().getStringExtra("givebackPicUrl");
		detail_value.setText(remark);
		reason_value.setText(grfReason);
		regoods_amount.setText(""+returnGoodsNum);
		refund_amount.setText(""+grfAmount);
		
		grfPicUrls = getIntent().getStringArrayListExtra("grfPicUrls");
		logout("=============待审核==============="+grfPicUrls);
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
		asyncQueryReturnApply();
	}


	@Override
	public void initListener() {
		photo0.setOnClickListener(this);
		photo1.setOnClickListener(this);
		photo2.setOnClickListener(this);
		sales_after_policy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, SalesAfterPolicyActivity.class));
				
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
	
	/**
	 * 退货申请查询
	 */
	private void asyncQueryReturnApply() {
		String wholeUrl = AppUrl.host + AppUrl.RETURN_QUERY;
		String requestBodyData = ParamBuild.queryByIdV2(soId,pmInfoId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				queryListener);
	}

	BaseRequestListener queryListener = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			dismissDialog();
			showShortToast(errorMsg);
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			ApplySalesAfterInfo item = gson.fromJson(jsonResult.toString(),
					ApplySalesAfterInfo.class);// 把Json数据解析成对象
			grfCode = item.getGrfCode();
			logout("=======售后单号============"+grfCode);
			grfApplyTime = item.getGrfApplyTime();
			sales_after_number_value.setText(grfCode);
			long time = Long.valueOf(getIntent().getLongExtra("grfApplyTime",
					grfApplyTime));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			request_time_value.setText(sdf.format(new Date(time)));
		}
	};

}
