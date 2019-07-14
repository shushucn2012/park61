package com.park61.moduel.child;

import org.json.JSONObject;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.child.fragment.BuildFragment;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BabyBuildActivity extends BaseActivity {

	public final static int BUILD_TYPE_HEIGHT = 1; //身高
	public final static int BUILD_TYPE_WEIGHT = 0; //体重
	
	private Button right_img;
	private RadioGroup rg_tab_build;
	
	private BabyItem showingBb;// 当前显示的宝宝
	FragmentManager fragmentManager;
	BuildFragment fragmentHeight;
	BuildFragment fragmentWeight;
	int build_type = BUILD_TYPE_HEIGHT;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_baby_build);
	}

	@Override
	public void initView() {
		right_img = (Button) findViewById(R.id.right_img);
		rg_tab_build = (RadioGroup) findViewById(R.id.rg_tab);
		

	}

	@Override
	public void initData() {
		showingBb = (BabyItem) getIntent().getSerializableExtra("showingBb");
		fragmentHeight = new BuildFragment(1, showingBb);
		//fragmentWeight = new BuildFragment(0, showingBb);
		fragmentManager = this.getSupportFragmentManager();
		fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentHeight).commit();

	}

	@Override
	public void initListener() {
		right_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent(mContext, build_type == 1 ? BuildHeightInputActivity.class:BuildWeightInputActivity.class);
				it.putExtra("mode", BuildHeightInputActivity.MODE_ADD + "");
				it.putExtra("childId", showingBb.getId());
				startActivityForResult(it, BuildHeightInputActivity.MODE_ADD);
			}
		});
		
		rg_tab_build.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_tab_height:
					build_type = 1;
					FragmentTransaction ft = fragmentManager.beginTransaction();
					ft.show(fragmentHeight);
					if(fragmentWeight != null){
						ft.hide(fragmentWeight);
					}
					ft.commit();
					//fragment = new BuildFragment(1, showingBb);
					//fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
					break;

				default:
					build_type = 0;
					FragmentTransaction ft0 = fragmentManager.beginTransaction();
					ft0.hide(fragmentHeight);
					if(fragmentWeight == null){
						fragmentWeight = new BuildFragment(0, showingBb);
						ft0.add(R.id.fragment_container, fragmentWeight);
					}
					ft0.show(fragmentWeight);
					ft0.commit();
					break;
				}
			}
		});
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK || data == null)
			return;
		String backData = data.getStringExtra("new_data");
		String growingDate = data.getStringExtra("growingDate");
		
		if(build_type == BUILD_TYPE_HEIGHT){
			if (requestCode == BuildHeightInputActivity.MODE_ADD) {// 添加
				asyncAddGrowingRec(null, backData, growingDate);
			} else if (requestCode == BuildHeightInputActivity.MODE_UPDATE) {// 修改
				Long id = data.getLongExtra("recId", -1l);
				asyncUpdateGrowingRec(id, null, backData, null);
			}
		}else{
			if (requestCode == BuildHeightInputActivity.MODE_ADD) {// 添加
				asyncAddGrowingRec(backData, null, growingDate);
			} else if (requestCode == BuildHeightInputActivity.MODE_UPDATE) {// 修改
				Long id = data.getLongExtra("recId", -1l);
				asyncUpdateGrowingRec(id, backData, null, null);
			}
		}
		
	}
	
	/**
	 * 添加体格数据
	 */
	private void asyncAddGrowingRec(String weight, String height, String date) {
		String wholeUrl = AppUrl.host + AppUrl.ADD_GROWING_REC;
		String requestBodyData = ParamBuild.addGrowingRec(showingBb.getId(),
				weight, height, date);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				addRecLsner);
	}
	
	/**
	 * 修改体格数据
	 */
	private void asyncUpdateGrowingRec(Long id, String weight, String height,
			String date) {
		String wholeUrl = AppUrl.host + AppUrl.UPDATE_GROWING_REC;
		String requestBodyData = ParamBuild.updateGrowingRec(id, weight,
				height, date);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 1,
				addRecLsner);
	}

	BaseRequestListener addRecLsner = new JsonRequestListener() {

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
			if (requestId == 0)
				showShortToast("提交成功！");
			else
				showShortToast("修改成功！");
			GlobalParam.GrowingMainNeedRefresh = true;
			if(build_type == 0){
				fragmentWeight.refreshRecList();
			}else{
				fragmentHeight.refreshRecList();
			}
			
		}
	};


}
