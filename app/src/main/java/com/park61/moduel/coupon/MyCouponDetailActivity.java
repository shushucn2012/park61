package com.park61.moduel.coupon;

import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.moduel.coupon.bean.CouponUser;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

public class MyCouponDetailActivity extends BaseActivity {
	private TextView tv_number_value, tv_title_value, tv_source_value,
			tv_face_value, tv_range_value, tv_releasedate_value,
			tv_limitdate_value, tv_remark;
	private String ruleValue2;
	private long id;
	private String date;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_coupon_detail);

	}

	@Override
	public void initView() {
		tv_number_value = (TextView) findViewById(R.id.tv_number_value);
		tv_title_value = (TextView) findViewById(R.id.tv_title_value);
		tv_source_value = (TextView) findViewById(R.id.tv_source_value);
		tv_face_value = (TextView) findViewById(R.id.tv_face_value);
		tv_range_value = (TextView) findViewById(R.id.tv_range_value);
		tv_releasedate_value = (TextView) findViewById(R.id.tv_releasedate_value);
		tv_limitdate_value = (TextView) findViewById(R.id.tv_limitdate_value);
		tv_remark = (TextView) findViewById(R.id.tv_remark);

	}

	@Override
	public void initData() {
		id = this.getIntent().getLongExtra("id", 0);
		asyncCouponDetail(id);

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}
	/**
	 * 通过优惠券id查询优惠券详情
	 * @param id
	 */
	private void asyncCouponDetail(long id) {
		String wholeUrl = AppUrl.host + AppUrl.COUPON_DETAIL;
		String requestBodyData = ParamBuild.getCouponDetailById(id);
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
			CouponUser item = gson.fromJson(jsonResult.toString(),CouponUser.class);
			if (item.getCoupon().getLimitType() == 1) {
				date =  DateTool.L2SEndDay(item.getCoupon().getLimitStart())
						+ "至" + DateTool.L2SEndDay(item.getCoupon().getLimitEnd());
			} else if (item.getCoupon().getLimitType() == 2) {
				date = item.getCoupon().getLimitDay()+"天"+"";
			}else if(item.getCoupon().getLimitType() == 3){
				date = "不限期";
			}
			if (item.getCoupon().getType()==3||item.getCoupon().getType()==4) {
				ruleValue2 = item.getCoupon().getRuleValue2()+"元"+"";
			}else if (item.getCoupon().getType()==1) {
				ruleValue2 = (int)(item.getCoupon().getRuleValue2())+"天"+"";
			}
			tv_remark.setText(item.getCoupon().getRemarks());
			tv_limitdate_value.setText(date);
			tv_releasedate_value.setText(DateTool.L2SEndDay(item.getCoupon().getReleaseDate())+"");
			tv_range_value.setText(item.getCoupon().getUseRangeName());
			tv_face_value.setText(ruleValue2);
			tv_source_value.setText(item.getCouponRule().getName());
			tv_title_value.setText(item.getCoupon().getTitle());
			tv_number_value.setText(item.getCode());

		}
	};	

}
