package com.park61.widget.pw;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.child.bean.ShowItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

public class GmInputPopWin extends BaseActivity {

	private View top_area;
	private EditText edit_comt;
	private Button btn_send;
	private String contentStr;
	private ShowItem showItem;
	private Long replyId;

	@Override
	public void setLayout() {
		setContentView(R.layout.gm_comt_input_layout);
	}

	@Override
	public void initView() {
		top_area = findViewById(R.id.top_area);
		edit_comt = (EditText) findViewById(R.id.edit_comt);
		btn_send = (Button) findViewById(R.id.btn_send);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				edit_comt.requestFocus();
				InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				im.showSoftInput(edit_comt, 0);
			}
		}, 500);
	}

	@Override
	public void initData() {
		showItem = (ShowItem) getIntent().getSerializableExtra("growItem");
		replyId = (Long) getIntent().getLongExtra("replyId", -1l);
	}

	@Override
	public void initListener() {
		top_area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit_comt.getWindowToken(), 0);
				finish();
			}
		});
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				contentStr = edit_comt.getText().toString().trim();
				if (TextUtils.isEmpty(contentStr)) {
					showShortToast("您还未填写评论！");
					return;
				}
				if (replyId != null && replyId != -1l) {
					asyncCommitGrowComt(replyId, replyId);
				} else {
					asyncCommitGrowComt(null, null);
				}
			}
		});
	}

	/**
	 * 请求提交成长评论
	 */
	private void asyncCommitGrowComt(Long parentId, Long rootId) {
		String wholeUrl = AppUrl.host + AppUrl.ADD_GROW_COMMENT;
		String requestBodyData = ParamBuild.comtGrow(showItem.getId(),
				parentId, rootId, contentStr, showItem.getType());
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				comtActListener);
	}

	BaseRequestListener comtActListener = new JsonRequestListener() {

		@Override
		public void onStart(int requestId) {
			showDialog();
		}

		@Override
		public void onError(int requestId, String errorCode, String errorMsg) {
			dismissDialog();
			showShortToast(errorMsg);
			finish();
		}

		@Override
		public void onSuccess(int requestId, String url, JSONObject jsonResult) {
			dismissDialog();
			showShortToast("评论成功！");
			GlobalParam.GrowingMainNeedRefresh = true;
			finish();
		}
	};
}
