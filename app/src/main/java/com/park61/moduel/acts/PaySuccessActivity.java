package com.park61.moduel.acts;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.me.bean.ApplyActItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

public class PaySuccessActivity extends BaseActivity {

	private Long applyId;// 订单id
	private String applyIds;//订单ids（用于小课）
	private ApplyActItem curApply;
	private ImageView img_act;
	private TextView tv_total_money, tv_act_title, tv_act_addr, tv_apply_num, tv_act_price;
	private Button btn_finish, btn_backtohome, btn_share, btn_comt;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_pay_success);
	}

	@Override
	public void initView() {
		btn_finish = (Button) findViewById(R.id.btn_finish);
		/*img_act = (ImageView) findViewById(R.id.img_act);
		tv_act_title = (TextView) findViewById(R.id.tv_act_title);
		tv_act_addr = (TextView) findViewById(R.id.tv_act_addr);
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		tv_apply_num = (TextView) findViewById(R.id.tv_apply_num);
		tv_act_price = (TextView) findViewById(R.id.tv_act_price);

		btn_backtohome = (Button) findViewById(R.id.btn_backtohome);
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_comt = (Button) findViewById(R.id.btn_comt);*/
	}

	@Override
	public void initData() {
		/*applyId = getIntent().getLongExtra("applyId", 0l);
		applyIds = getIntent().getStringExtra("applyIds");
		curApply = (ApplyActItem) getIntent().getSerializableExtra("curApply");
		if (curApply == null) {
			if(!TextUtils.isEmpty(applyIds)){
				asyncGetApplysInfo();
			} else {
				asyncGetApplyInfo();
			}
		} else {
			fillData();
		}*/
	}

	/**
	 * 获取订单详情
	 */
	protected void asyncGetApplyInfo() {
		String wholeUrl = AppUrl.host + AppUrl.GET_ORDER_DETAILS;
		String requestBodyData = ParamBuild.getApplyInfo(applyId);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getApplyInfolistener);
	}

	/**
	 * 获取订单(用于小课)详情
	 */
	protected void asyncGetApplysInfo() {
		String wholeUrl = AppUrl.host + AppUrl.GET_ORDER_DETAILS;
		String requestBodyData = ParamBuild.getApplysInfo(applyIds);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, getApplyInfolistener);
	}

	BaseRequestListener getApplyInfolistener = new JsonRequestListener() {

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
			curApply = gson.fromJson(jsonResult.toString(), ApplyActItem.class);
			fillData();
		}
	};

	/**
	 * 填充数据
	 */
	public void fillData() {
		ImageManager.getInstance().displayImg(img_act, curApply.getActCover());
		tv_act_title.setText(curApply.getActTitle());
		tv_act_addr.setText(curApply.getActAddress());
		tv_total_money.setText("￥" + curApply.getTotalPrice());
		tv_apply_num.setText(curApply.getApplyAdultsNumber() + "个成人    "
				+ curApply.getApplyChildrenNumber() + "个孩子");
		tv_act_price.setText("￥" + curApply.getApplyPrice());
	}

	@Override
	public void initListener() {
		/*btn_backtohome.setOnClickListener(toMainClicked);
		btn_share.setOnClickListener(onShareClicked);
		btn_comt.setOnClickListener(onComtClicked);*/
		btn_finish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backToHome();
			}
		});
	}

	OnClickListener toMainClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			backToHome();
		}
	};

	OnClickListener onShareClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String shareUrl = String.format(AppUrl.ACT_SHARE_URL, curApply.getActId());
			String picUrl = curApply.getActCover();
			String title = getString(R.string.app_name);
			String description = curApply.getActTitle() + "\n 游戏详情";
			showShareDialog(shareUrl, picUrl, title, description);
		}
	};

	OnClickListener onComtClicked = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent it = new Intent(mContext, ActDetailsActivity.class);
			it.putExtra("id", Long.parseLong(curApply.getActId()));
			it.putExtra("isToComt", true);
			startActivity(it);
		}
	};

}
