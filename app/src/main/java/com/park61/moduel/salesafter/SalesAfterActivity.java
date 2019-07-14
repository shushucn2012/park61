package com.park61.moduel.salesafter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.AliyunUploadUtils.OnUploadListFinish;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.salesafter.adapter.InputPictureAdapter;
import com.park61.moduel.salesafter.bean.GrfReason;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.SelectReasonDialog;
import com.park61.widget.wxpicselect.bean.Bimp;
import com.park61.widget.wxpicselect.bean.ImageBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 售后申请界面
 * 
 * @author Lucia
 * 
 */
public class SalesAfterActivity extends BaseActivity implements OnClickListener {

	private TextView regoods_reason, sales_after_policy, type, refund_amount,amount;
	private EditText detail_value,regoods_num;
	private Button choice_reason_btn, choice_type_btn, btn_commit;
	private GridView gv_input_pic;
	private Bitmap tianjiaBmp;// 添加图片
	private ArrayList<Bitmap> inputPicList = new ArrayList<Bitmap>();
	private ArrayList<String> inputPicPathList = new ArrayList<String>();
	private ArrayList<String> urlList = new ArrayList<String>();
	private InputPictureAdapter adapter;// 图片展示gridview适配器
	protected static final int REQ_GET_PIC = 0;// 去获取图片
	protected static final int REQ_SHOW_BIG_PIC = 1;// 去显示图片
	// 记录点击完成后回传的选中的imagebean
	private ArrayList<ImageBean> selectedImgBeans = new ArrayList<ImageBean>();

	private long soId;// 订单id
//	private String afterType;// 售后类型
	private int grfType;// 退货类型：1到9
	private double grfAmount;// 退款金额
	private int orderItemNum;// 退货件数
	private String remark;// 退货备注
	private String grfReason;// 退回原因
	private String typeValue;// 售后类型
	private ArrayList<String> grfPicUrls= new ArrayList<String>();// 图片地址集合
	private String givebackPicUrl;// 图片地址

	private long pmInfoId;//商品订单id
	private int pmNum;//退货商品数量
	private int maxBackNum;//最大退货数量
	private ArrayList<GrfReason> reasonList = new ArrayList<GrfReason>();
	private int selectPos;

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_sales_after_main);

	}

	@Override
	public void initView() {
		regoods_reason = (TextView) findViewById(R.id.regoods_reason_value);
		sales_after_policy = (TextView) findViewById(R.id.sales_after_policy);

		type = (TextView) findViewById(R.id.type_value);
		refund_amount = (TextView) findViewById(R.id.refund_amount_value);
		regoods_num = (EditText) findViewById(R.id.regoods_amount_value);
		detail_value = (EditText) findViewById(R.id.detail_value);
		amount = (TextView) findViewById(R.id.amount);
		choice_reason_btn = (Button) findViewById(R.id.choice_reason_btn);
		choice_type_btn = (Button) findViewById(R.id.choice_type_btn);
		btn_commit = (Button) findViewById(R.id.btn_commit);

		gv_input_pic = (GridView) findViewById(R.id.gv_input_pic);
	}

	@Override
	public void initData() {
		tianjiaBmp = ImageManager.getInstance().readResBitMap(
				R.drawable.tianjiazhaopian,mContext);
		inputPicList.add(tianjiaBmp);
		adapter = new InputPictureAdapter(inputPicList, mContext);
		gv_input_pic.setAdapter(adapter);

		soId = getIntent().getLongExtra("soId", -1L);
		logout("=======包裹id======="+soId);
		pmInfoId = getIntent().getLongExtra("pmInfoId",0l);
//		grfAmount = (double) getIntent().getExtras().get("amount");
		orderItemNum = getIntent().getIntExtra("orderItemNum",orderItemNum);
//		refund_amount.setText(FU.strDbFmt(grfAmount+""));
//		regoods_num.setText("" + orderItemNum);
//		amount.setText(FU.strDbFmt(grfAmount+""));

	}

	@Override
	public void initListener() {
		gv_input_pic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == inputPicList.size() - 1) {
					Intent it = new Intent(mContext, PhotoAlbumActivity.class);
					Bimp.tempSelectBitmap.clear();
					Bimp.tempSelectBitmap.addAll(selectedImgBeans);
					startActivityForResult(it, REQ_GET_PIC);
				} else {
					Intent it = new Intent(mContext, ShowBigPicActivity.class);
					it.putExtra("big_pic", inputPicPathList.get(position));
					it.putExtra("position", position);
					startActivityForResult(it, REQ_SHOW_BIG_PIC);
				}
			}

		});
		choice_type_btn.setOnClickListener(this);
		choice_reason_btn.setOnClickListener(this);
//		btn_commit.setOnClickListener(this);
		regoods_reason.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (!TextUtils.isEmpty(regoods_reason.getText().toString()) &&
						!TextUtils.isEmpty(regoods_num.getText().toString())) {
					btn_commit.setBackgroundColor(getResources().getColor(R.color.com_orange));
					btn_commit.setOnClickListener(btnCommitClickListener);
				} else {
					btn_commit.setBackgroundColor(getResources().getColor(R.color.btn_bg_grey));
					btn_commit.setOnClickListener(null);
				}
			}
		});
		regoods_num.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (!TextUtils.isEmpty(regoods_reason.getText().toString()) &&
						!TextUtils.isEmpty(regoods_num.getText().toString())) {
					btn_commit.setBackgroundColor(getResources().getColor(R.color.com_orange));
					btn_commit.setOnClickListener(btnCommitClickListener);
				} else {
					btn_commit.setBackgroundColor(getResources().getColor(R.color.btn_bg_grey));
					btn_commit.setOnClickListener(null);
				}
				pmNum = FU.paseInt(regoods_num.getText().toString());
				if (!validateData()) {
					return;
				}
				asyncGrfAmount();
			}
		});

		// 退货查询入口
		sales_after_policy.setOnClickListener(this);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK || data == null) {
			return;
		}
		if (requestCode == REQ_GET_PIC) {
			selectedImgBeans.clear();
			selectedImgBeans.addAll(Bimp.tempSelectBitmap);
			inputPicList.clear();
			inputPicPathList.clear();
			for (int i = 0; i < selectedImgBeans.size(); i++) {
				inputPicList.add(i, selectedImgBeans.get(i).getBitmap());
				inputPicPathList.add(selectedImgBeans.get(i).getPath());
			}
			// 把“+”直接加在最后，adapter里面会判断隐藏的
			inputPicList.add(tianjiaBmp);
			adapter = new InputPictureAdapter(inputPicList, mContext);
			gv_input_pic.setAdapter(adapter);
		} else if (requestCode == REQ_SHOW_BIG_PIC) {
			int position = data.getIntExtra("position", -1);
			inputPicList.remove(position);
			inputPicPathList.remove(position);
			selectedImgBeans.remove(position);
			adapter = new InputPictureAdapter(inputPicList, mContext);
			gv_input_pic.setAdapter(adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.choice_type_btn:// 选择售后类型的点击事件
//			final SelectSalesAfterTypeDialog typeDialog = new SelectSalesAfterTypeDialog(
//					mContext);
//			typeDialog.showDialog("售后类型", "请根据你的实际情况选择相应的售后类型", "取消", "确定",
//					null, new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							typeValue = typeDialog.getSelectItem();
//							type.setText(typeValue);
//							typeDialog.dismissDialog();
//						}
//					});
//			break;

		case R.id.choice_reason_btn:// 选择退货原因的点击事件
			asyncGetGrfTypeList();
			break;

//		case R.id.btn_commit:
//			if (!validateData()) {
//				return;
//			}
//			// 图片为空
//			if (inputPicList.size() == 1) {
//				asyncReturnApply(soId, grfType);
//				return;
//			}
//
//			// 图片不为空时,异步压缩再上传
//			new CompressNUploadTask().execute();
//
//			break;
		case R.id.sales_after_policy:
			startActivity(new Intent(mContext, SalesAfterPolicyActivity.class));
			break;
		}

	}
	OnClickListener btnCommitClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!validateData()) {
				return;
			}
			// 图片为空
			if (inputPicList.size() == 1) {
				asyncReturnApply(soId, grfType);
				return;
			}

			// 图片不为空时,异步压缩再上传
			new CompressNUploadTask().execute();

		}
	};

	protected boolean validateData() {
		remark = detail_value.getText().toString().replace("请输入其他相关说明", "");
		if(FU.paseInt(regoods_num.getText().toString())>orderItemNum){
			showShortToast("退货件数不能超过购买件数");
			return false;
		}
		if (TextUtils.isEmpty(grfReason)) {
			showShortToast("请选择退货原因");
			return false;
		}
		if(TextUtils.isEmpty(regoods_num.getText().toString())){
			showShortToast("请填写退货件数");
			return false;
		}
		if(FU.paseInt(regoods_num.getText().toString())<=0){
			showShortToast("退货件数必须大于0");
			return false;
		}
		return true;
	}

	/**
	 * 退货申请
	 * 
	 * @param soId
	 *            订单id
	 * @param grfType
	 *            退货类型：1到9
	 */
	private void asyncReturnApply(long soId, int grfType) {
//		String wholeUrl = AppUrl.host + AppUrl.RETURN_APPLY_FOR;
		String wholeUrl = AppUrl.host + AppUrl.GRF_APPlYV2;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < urlList.size(); i++) {
			logout("urlList ====== " + urlList.get(i));			
			sb.append(urlList.get(i));
			
			givebackPicUrl = urlList.get(i);
			grfPicUrls.add(givebackPicUrl);
			
			logout("======urlList.get(i)======="+urlList.get(i));
			if (i != urlList.size() - 1)
				sb.append(";");
		}
//		String requestBodyData = ParamBuild.applyReturn(soId, grfType, remark,sb.toString());
		String requestBodyData = ParamBuild.applyReturnV2(soId, grfType, remark,sb.toString(),
				pmInfoId,pmNum);
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				applyListener);
	}

	BaseRequestListener applyListener = new JsonRequestListener() {

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
			showShortToast("申请已提交，等待审核！");
			GlobalParam.TradeOrderDetailNeedRefresh = true;	
			GlobalParam.MyOrderNeedRefresh = true;
			Intent intent = new Intent(mContext,
					WaitForOfficialAuditActivity.class);
			intent.putExtra("pmInfoId",pmInfoId);
			intent.putExtra("soId", soId);
//			intent.putExtra("grfAmount", grfAmount);
			intent.putExtra("grfAmount", Double.parseDouble(refund_amount.getText().toString()));
			intent.putExtra("grfReason", grfReason);
//			intent.putExtra("returnGoodsNum", orderItemNum);
			intent.putExtra("returnGoodsNum", Integer.parseInt(regoods_num.getText().toString()));
			intent.putExtra("remark", detail_value.getText().toString());
			intent.putExtra("grfPicUrls", grfPicUrls);
			startActivity(intent);
			finish();
		}
	};

	/**
	 * 压缩再上传
	 */
	private class CompressNUploadTask extends
			AsyncTask<String, Integer, ArrayList<String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog();
		}

		@Override
		protected ArrayList<String> doInBackground(String... params) {
			// 准备将上传的已压缩图片的文件路径
			ArrayList<String> resizedPathList = new ArrayList<String>();
			for (String wholePath : inputPicPathList) {
				// 自已缓存文件夹
				File temp = new File(Environment.getExternalStorageDirectory()
						.getPath() + "/GHPCacheFolder/Format");
				if (!temp.exists()) {
					temp.mkdirs();
				}
				String filePath = temp.getAbsolutePath() + "/"
						+ Calendar.getInstance().getTimeInMillis() + ".jpg";
				File tempFile = new File(filePath);
				// 图像保存到文件中
				try {
					Bimp.compressBmpToFile(tempFile, wholePath);
				} catch (Exception e) {
					e.printStackTrace();
					showShortToast("图片压缩失败！");
					finish();
				}
				// 将压缩后的地址放入集合
				resizedPathList.add(filePath);
			}
			return resizedPathList;
		}

		@Override
		protected void onPostExecute(ArrayList<String> resizedPathList) {
			dismissDialog();
		    new AliyunUploadUtils(mContext).uploadPicList(
					resizedPathList, new OnUploadListFinish() {

						@Override
						public void onSuccess(List<String> urllist) {
							urlList.clear();
							urlList.addAll(urllist);
							asyncReturnApply(soId, grfType);
						}

						@Override
						public void onError(String path) {
							showShortToast("上传失败！");
						}
					});
		}

	}

	/**
	 * 退货金额
     */
	private void asyncGrfAmount(){
		if(FU.paseInt(regoods_num.getText().toString())>orderItemNum){
			showShortToast("退货件数不能超过购买件数");
			return ;
		}
		String wholeUrl = AppUrl.host + AppUrl.GRF_AMOUNT;
		String requestBodyData = ParamBuild.getGrfAmount(grfType,pmNum
				,pmInfoId,soId);
		netRequest.startRequest(wholeUrl,Method.POST,requestBodyData,0,grfAmountlistener);
	}
	BaseRequestListener grfAmountlistener = new JsonRequestListener() {

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
			refund_amount.setText(jsonResult.optString("grfAmount")+"");
			maxBackNum = Integer.parseInt(jsonResult.optString("maxBackNum"));
			logout("========金额==========="+jsonResult.optString("grfAmount"));
		}
	};
	/**
	 * 退货类型列表
	 */
	private void asyncGetGrfTypeList(){
		String wholeUrl = AppUrl.host + AppUrl.GRF_TYPE;
		String requestBodyData = "";
		netRequest.startRequest(wholeUrl,Method.POST,requestBodyData,0,grfTypeListener);
	}
	BaseRequestListener grfTypeListener = new JsonRequestListener() {
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
			JSONArray jay = jsonResult.optJSONArray("list");
			reasonList.clear();
			for(int i = 0; i < jay.length(); i++){
				JSONObject jot = jay.optJSONObject(i);
				GrfReason item = gson.fromJson(jot.toString(),GrfReason.class);
				reasonList.add(item);
			}
			final SelectReasonDialog dialog = new SelectReasonDialog(selectPos,mContext,reasonList);
			dialog.showDialog("退货原因", "请根据你的实际情况选择相应的退货原因", "取消", "确定", null,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							grfReason = dialog.getSelectItem();
							regoods_reason.setText(grfReason);
							selectPos = dialog.getSelectId();
							logout("=====申请售后========="+selectPos);
							grfType = dialog.getType();
							logout("=====退货原因======"+grfReason+grfType);
							dialog.dismissDialog();
							if(FU.paseInt(regoods_num.getText().toString())>0){
								asyncGrfAmount();
							}

						}
					});
		}
	};

}
