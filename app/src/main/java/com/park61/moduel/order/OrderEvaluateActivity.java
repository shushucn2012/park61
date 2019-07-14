package com.park61.moduel.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.AliyunUploadUtils;
import com.park61.common.tool.AliyunUploadUtils.OnUploadListFinish;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.child.ShowBigPicActivity;
import com.park61.moduel.order.bean.GoodsInfoBean;
import com.park61.moduel.order.bean.OrderInfoBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.ProductRatingBar;
import com.park61.widget.wxpicselect.AlbumActivity;
import com.park61.widget.wxpicselect.bean.Bimp;
import com.park61.widget.wxpicselect.bean.ImageBean;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品订单评价界面
 */
public class OrderEvaluateActivity extends BaseActivity {

	protected static final int REQ_GET_PIC = 0;
	protected static final int REQ_SHOW_BIG_PIC = 1;
	private OrderInfoBean orderInfo;
	private long soId;

	private GoodsItemConstruction[] goodsArray;
	Bitmap tianjiaBmp;
	private GoodsItemConstruction tempCurGoodsItem;
	private ArrayList<String> urlList = new ArrayList<String>();

	private class GoodsItemConstruction
	{
		final int index;
		public ArrayList<Bitmap> inputPicList = new ArrayList<Bitmap>();
		public ArrayList<String> inputPicPathList = new ArrayList<String>();
		//public ArrayList<String> urlList = new ArrayList<String>();
		public EditText et;
		public ProductRatingBar rb;
		//private GridView gv_input_pic;
		public InputPicAdapter ipAdapter;
		public GoodsItemConstruction(int index) {
			this.index = index;
		}

	}

	private ProductRatingBar ratingbar_attitude,ratingbar_speed;
	private CheckBox cb_anonymous;
	private Button btn_commit;


	// 记录点击完成后回传的选中的imagebean
	private ArrayList<ImageBean> selectedImgBeans = new ArrayList<ImageBean>();

	@Override
	public void setLayout() {
		setContentView(R.layout.activity_order_evaluate);
	}

	@Override
	public void initView() {
		ratingbar_attitude = (ProductRatingBar) findViewById(R.id.ratingbar_service_attitude);
		ratingbar_speed = (ProductRatingBar) findViewById(R.id.ratingbar_delivery_speed);
		cb_anonymous = (CheckBox) findViewById(R.id.cb_anonymous);
		btn_commit = (Button) findViewById(R.id.btn_evaluate);
	}

	@Override
	public void initData() {
		soId = getIntent().getLongExtra("soId", soId);
		logout("========OrderEvaluateActivity===orderId========"+soId);

		orderInfo = (OrderInfoBean) getIntent().getSerializableExtra("orderInfo");
		if(orderInfo == null){
			showShortToast("订单异常!");
			this.finish();
			return;
		}

		tianjiaBmp = ImageManager.getInstance().readResBitMap(R.drawable.ic_evaluate_camera,mContext);
		goodsArray = new GoodsItemConstruction[orderInfo.items.size()];
		for(int i = 0; i < goodsArray.length;i++){
			goodsArray[i] = new GoodsItemConstruction(i);
			goodsArray[i].inputPicList.add(tianjiaBmp);
		}
		setViewData();

	}
	OnClickListener btnCommitClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int img_num = validateEvaluate();
			if(img_num>0){
				// 图片不为空时,异步压缩再上传
				new CompressNUploadTask().execute();
			} else if(img_num == 0){
				asyncEvaluateOrder();
			} else {
				showShortToast("有商品评价为空！");
			}

		}
	};

	@Override
	public void initListener() {
		btn_commit.setOnClickListener(null);
	}

	private void setViewData(){
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		LinearLayout lay_goods = (LinearLayout) this.findViewById(R.id.lay_goods);
		lay_goods.removeAllViews();
		for(int i = 0; i < goodsArray.length; i++){
			GoodsItemConstruction goodsItem = goodsArray[i];
			GoodsInfoBean goodsInfo = orderInfo.items.get(i);
			View view_goods_item =  layoutInflater.inflate(R.layout.item_order_evaluate, null);
			goodsItem.et = (EditText) view_goods_item.findViewById(R.id.edit_input_word);
			goodsItem.rb = (ProductRatingBar) view_goods_item.findViewById(R.id.ratingbar_orrelated);
			ImageManager.getInstance().displayImg((ImageView) view_goods_item.findViewById(R.id.iv_goods_icon),goodsInfo.productPicUrl);
			goodsItem.ipAdapter = new InputPicAdapter(goodsItem);
			GridView gv_input_pic = (GridView) view_goods_item.findViewById(R.id.gv_input_pic);
			gv_input_pic.setAdapter(goodsItem.ipAdapter);
			gv_input_pic.setOnItemClickListener(new PicOnItemClickListener(goodsItem));
			goodsItem.et.addTextChangedListener(new TextWatcher() {


				@Override
				public void afterTextChanged(Editable s) {

					if(validateContent()){
						btn_commit.setBackgroundResource(R.drawable.btn_goods_evaluate_selector);
						btn_commit.setOnClickListener(btnCommitClickListener);
					}else{
						btn_commit.setBackgroundColor(0xffcccccc);
						btn_commit.setOnClickListener(null);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}
			});
			lay_goods.addView(view_goods_item);
		}
	}


	private class InputPicAdapter extends BaseAdapter {

		GoodsItemConstruction goodsItem;

		public InputPicAdapter(GoodsItemConstruction goodsItem) {
			super();
			this.goodsItem = goodsItem;
		}

		@Override
		public int getCount() {
			return goodsItem.inputPicList.size();
		}

		@Override
		public Bitmap getItem(int position) {
			return goodsItem.inputPicList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.gridview_inputpic_item, null);
			}
			ImageView img_input = (ImageView) convertView
					.findViewById(R.id.img_input);
			img_input.setImageBitmap(goodsItem.inputPicList.get(position));
			if (position == 9) {
				img_input.setVisibility(View.GONE);
			}
			return convertView;
		}
	}

	private class PicOnItemClickListener implements OnItemClickListener{

		GoodsItemConstruction goodsItem;

		public PicOnItemClickListener(GoodsItemConstruction goodsItem) {
			super();
			this.goodsItem = goodsItem;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			tempCurGoodsItem = goodsItem;
			if (position == goodsItem.inputPicList.size() - 1) {
				Intent it = new Intent(mContext, AlbumActivity.class);
				Bimp.tempSelectBitmap.clear();
				Bimp.tempSelectBitmap.addAll(selectedImgBeans);
				startActivityForResult(it, REQ_GET_PIC);
			} else {
				Intent it = new Intent(mContext, ShowBigPicActivity.class);
				it.putExtra("big_pic", goodsItem.inputPicPathList.get(position));
				it.putExtra("position", position);
				startActivityForResult(it, REQ_SHOW_BIG_PIC);
			}
		}

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK || data == null || tempCurGoodsItem == null)
			return;
		if (requestCode == REQ_GET_PIC) {
			selectedImgBeans.clear();
			selectedImgBeans.addAll(Bimp.tempSelectBitmap);
			tempCurGoodsItem.inputPicList.clear();
			tempCurGoodsItem.inputPicPathList.clear();
			for (int i = 0; i < selectedImgBeans.size(); i++) {
				tempCurGoodsItem.inputPicList.add(i, selectedImgBeans.get(i).getBitmap());
				tempCurGoodsItem.inputPicPathList.add(selectedImgBeans.get(i).getPath());
			}
			// 把“+”直接加在最后，adapter里面会判断隐藏的
			tempCurGoodsItem.inputPicList.add(tianjiaBmp);
			tempCurGoodsItem.ipAdapter.notifyDataSetChanged();;
		} else if (requestCode == REQ_SHOW_BIG_PIC) {
			int position = data.getIntExtra("position", -1);
			tempCurGoodsItem.inputPicList.remove(position);
			tempCurGoodsItem.inputPicPathList.remove(position);
			selectedImgBeans.remove(position);
			tempCurGoodsItem.ipAdapter.notifyDataSetChanged();
		}
		tempCurGoodsItem =null;
	}


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
			for(GoodsItemConstruction goodsItem:goodsArray){
				for (String wholePath : goodsItem.inputPicPathList) {
					File temp = new File(Environment.getExternalStorageDirectory()
							.getPath() + "/GHPCacheFolder/Format");// 自已缓存文件夹
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
			}
			return resizedPathList;
		}

		@Override
		protected void onPostExecute(ArrayList<String> resizedPathList) {
			dismissDialog();
			new AliyunUploadUtils(mContext).uploadPicList(
					resizedPathList, new OnUploadListFinish() {

						@Override
						public void onError(String path) {
							showShortToast("上传失败！");
						}

						@Override
						public void onSuccess(List<String> urllist) {
							urlList.clear();
							urlList.addAll(urllist);
							asyncEvaluateOrder();
						}
					});
		}
	}


	/**
	 * 请求提交商品评价
	 */
	private void asyncEvaluateOrder() {
		String wholeUrl = AppUrl.host + AppUrl.PRODUCT_EVALUATE;
		String requestBodyData = evaluateOrder();
		netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
				listener);
	}

	public String evaluateOrder() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", soId);
//		map.put("orderId", orderInfo.id);		
		map.put("serviceEvaluate", ratingbar_attitude.getPoint());
		map.put("speedEvaluate", ratingbar_speed.getPoint());
		map.put("isAnonymous", cb_anonymous.isChecked() ? 0 : 1);

		StringBuilder productIds = new StringBuilder();
		StringBuilder pmInfoIds = new StringBuilder();
		StringBuilder productEvaluates = new StringBuilder();
		StringBuilder contents = new StringBuilder();
		StringBuilder productEvaluatePics = new StringBuilder();
		for(int i = 0 ; i < goodsArray.length ;i++){
			GoodsItemConstruction goodsItem = goodsArray[i];
			if(i > 0){
				productIds.append(",");
				pmInfoIds.append(",");
				productEvaluates.append(",");
				contents.append(",");
			}
			productIds.append(orderInfo.items.get(i).productId);
			pmInfoIds.append(orderInfo.items.get(i).pmInfoId);
			productEvaluates.append(goodsItem.rb.getPoint());
			String temp_contents = goodsItem.et.getText().toString();
			if(temp_contents == null){
				temp_contents = "";
			}
			temp_contents.replaceAll(",", "，");
			contents.append(temp_contents);
		}
		map.put("productIds", productIds.toString());
		map.put("pmInfoIds", pmInfoIds.toString());
		map.put("productEvaluates", productEvaluates.toString());
		map.put("contents", contents.toString());

		int count = goodsArray[0].inputPicPathList.size();
		int array_index = 0;

		for(int i = 0 ; i < urlList.size(); i++)
		{
			if(i < count){
				if(i > 0){
					productEvaluatePics.append("=");
				}
			}else{
				array_index++;
				count += goodsArray[array_index].inputPicPathList.size();
				productEvaluatePics.append(",");
			}
			productEvaluatePics.append(urlList.get(i));
		}
		//if(urlList.size() > 0){
		map.put("productEvaluatePics", productEvaluatePics.toString());
		//}

		return ParamBuild.buildParams(map);
	}

	BaseRequestListener listener = new JsonRequestListener() {

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
			showShortToast("评价成功！");
			Intent i = new Intent();
			i.putExtra("orderId", orderInfo.getId()+"");
			GlobalParam.TradeOrderListNeedRefresh = true;
			OrderEvaluateActivity.this.setResult(RESULT_OK,i);
			OrderEvaluateActivity.this.finish();
		}
	};

	private int validateEvaluate(){
		int imgnum = 0;
		for(GoodsItemConstruction goodsItem : goodsArray){
			if (TextUtils.isEmpty(goodsItem.et.getText().toString())) {
				return -1;
			}
			imgnum += goodsItem.inputPicPathList.size();
		}
		return imgnum;

	}

	private boolean validateContent(){
		for(GoodsItemConstruction goodsItem : goodsArray){
			if (TextUtils.isEmpty(goodsItem.et.getText().toString())) {
				return false;
			}
		}
		return true;

	}

}
