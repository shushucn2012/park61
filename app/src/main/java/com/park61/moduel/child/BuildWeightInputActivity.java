package com.park61.moduel.child;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.DateTool;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

public class BuildWeightInputActivity extends BaseActivity {

	public static final int MODE_ADD = 0;
	public static final int MODE_UPDATE = 1;

	private Button btn_save;
	private TextView tv_page_title, tv_weight_on, tv_now_date;
	private HorizontalScrollView ruler;
	private LinearLayout rulerlayout;

	private String mode;
	private Long recId;
	private Long childId;
	private int beginValue;
	private String gDate = "";
	private double oldWeight;
	private double minWeight;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_build_weight_input);
	}

	@Override
	public void initView() {
		btn_save = (Button) findViewById(R.id.btn_save);
		tv_page_title = (TextView) findViewById(R.id.tv_page_title);
		ruler = (HorizontalScrollView) findViewById(R.id.birthruler);
		rulerlayout = (LinearLayout) findViewById(R.id.ruler_layout);
		tv_weight_on = (TextView) findViewById(R.id.tv_weight_on);
		tv_now_date = (TextView) findViewById(R.id.tv_now_date);
		ruler.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				Log.e("", "" + Math.ceil(ruler.getScrollX() / 20) / 10);
				String weightStr = beginValue
						+ Math.ceil(ruler.getScrollX() / 20) / 10 + "";
				tv_weight_on.setText(weightStr + " kg");
				switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							String weightStr = beginValue
									+ Math.ceil(ruler.getScrollX() / 20) / 10
									+ "";
							tv_weight_on.setText(weightStr + " kg");
						}
					}, 1000);
					break;
				}
				return false;
			}

		});
	}

	@Override
	public void initData() {
		mode = getIntent().getStringExtra("mode");
		recId = getIntent().getLongExtra("recId", -1l);
		childId = getIntent().getLongExtra("childId", -1l);
		gDate = getIntent().getStringExtra("gDate");
		oldWeight = getIntent().getDoubleExtra("oldWeight", 0.0);
		minWeight = getIntent().getDoubleExtra("minWeight", 0.0);
		if ((MODE_ADD + "").equals(mode)) {// 添加
			tv_page_title.setText("添加体重数据");
			tv_now_date.setText("今天");
			asyncGetGrowingDataRg();
		} else {// 修改
			tv_page_title.setText("修改体重数据");
			tv_now_date.setText(DateTool.L2SEndDay(gDate));
			tv_weight_on.setText(oldWeight + " kg");
			beginValue = (int) (minWeight - 10);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					constructRuler();
				}
			}, 250);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					int len = (int) ((oldWeight - beginValue) * 10 * 20);
					logout("len======" + len);
					ruler.smoothScrollTo(len, 0);
				}
			}, 500);
		}
	}

	@Override
	public void initListener() {
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String newData = "";
				Intent backData = new Intent();
				newData = tv_weight_on.getText().toString().replace("kg", "")
						.trim();
				if (TextUtils.isEmpty(newData)) {
					showShortToast("未填写数据！");
					return;
				}
				backData.putExtra("recId", recId);
				backData.putExtra("new_data", newData);
				backData.putExtra("growingDate", DateTool.getSystemDate());
				setResult(RESULT_OK, backData);
				finish();
			}
		});
	}

	/**
	 * 获取体格数据正常范围
	 */
	private void asyncGetGrowingDataRg() {
		String wholeUrl = AppUrl.host + AppUrl.GET_GROWING_DATA_RG;
		String date = "";
		if ((MODE_ADD + "").equals(mode)) {// 添加取当前时间
			date = DateTool.getSystemDate();
		} else {// 修改取传过来的记录时间
			date = gDate;
		}
		String requestBodyData = ParamBuild.getGrowingDataRg(childId, date);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				getRangeLsner);
	}

	BaseRequestListener getRangeLsner = new JsonRequestListener() {

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
			String weightMinStr = jsonResult.optString("weightMin");
			if (!TextUtils.isEmpty(weightMinStr))
				beginValue = (int) (Double.parseDouble(weightMinStr) - 10);
			else
				beginValue = 10;
			if (beginValue < 2)
				beginValue = 2;
			tv_weight_on.setText(beginValue + " kg");
			constructRuler();
		}
	};

	/**
	 * 拼接米尺
	 */
	private void constructRuler() {
		View leftview = (View) LayoutInflater.from(this).inflate(
				R.layout.blankhrulerunit, null);
		int screenWidth = ruler.getWidth();
		leftview.setLayoutParams(new LayoutParams(screenWidth / 2,
				LayoutParams.MATCH_PARENT));
		rulerlayout.addView(leftview);
		for (int i = 0; i < 30; i++) {
			View view = (View) LayoutInflater.from(this).inflate(
					R.layout.hrulerunit, null);
			view.setLayoutParams(new LayoutParams(200,
					LayoutParams.MATCH_PARENT));
			TextView tv = (TextView) view.findViewById(R.id.hrulerunit);
			tv.setText(String.valueOf(beginValue + i));
			rulerlayout.addView(view);
		}
		View rightview = (View) LayoutInflater.from(this).inflate(
				R.layout.blankhrulerunit, null);
		rightview.setLayoutParams(new LayoutParams(screenWidth / 2,
				LayoutParams.MATCH_PARENT));
		rulerlayout.addView(rightview);
	}
}
