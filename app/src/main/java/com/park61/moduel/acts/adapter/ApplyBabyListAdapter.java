package com.park61.moduel.acts.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.common.tool.FU;
import com.park61.moduel.me.bean.BabyItem;

public class ApplyBabyListAdapter extends BaseAdapter {

	private List<BabyItem> mList;
	private Context mContext;
	private LayoutInflater factory;
	private String price = "";
	private ArrayList<Integer> selectedPos;
	private CheckedBabyCallBack mCheckedBabyCallBack;

	public ApplyBabyListAdapter(Context _context, List<BabyItem> _list, String price) {
		this.mList = _list;
		this.mContext = _context;
		this.price = price;
		selectedPos = new ArrayList<Integer>();
		factory = LayoutInflater.from(mContext);
	}

	public void setActPrice(String price){
		this.price = price;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public BabyItem getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public ArrayList<Integer> getSelectedPos() {
		return selectedPos;
	}

	public int countAddChild() {
		int count = 0;
		for (int i = 0; i < mList.size(); i++) {
			if (mList.get(i).getId() == -1l)
				count++;
		}
		return count;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = factory.inflate(R.layout.apply_babylist_item, null);
		EditText tv_child_name = (EditText) convertView.findViewById(R.id.tv_child_name);
		TextView tv_child_price = (TextView) convertView.findViewById(R.id.tv_child_price);
		CheckBox chk_choose = (CheckBox) convertView.findViewById(R.id.chk_choose);
		final BabyItem babyItem = mList.get(position);
		tv_child_name.setText(babyItem.getPetName());
		if(babyItem.getId() != -1l){
			tv_child_name.setEnabled(false);
		}else{
			tv_child_name.setEnabled(true);
			tv_child_name.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

				@Override
				public void afterTextChanged(Editable editable) {
					babyItem.setName(editable.toString().trim());
				}
			});
		}
		Log.out("price======"+price);
		tv_child_price.setText(FU.zeroToMF(price));
		if(selectedPos.contains(position)){
			chk_choose.setChecked(true);
		}
		final Integer pos = position;
		chk_choose.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					/*if (selectedPos.size() >= 4 && isChecked) {
						Toast.makeText(mContext, "宝宝最多4名", Toast.LENGTH_SHORT).show();
						buttonView.setChecked(false);
					} else {*/
						selectedPos.add(pos);
						mCheckedBabyCallBack.onCheckedBaby(selectedPos.size());
					//}
				} else {
					selectedPos.remove(pos);
					mCheckedBabyCallBack.onCheckedBaby(selectedPos.size());
				}
			}
		});
		return convertView;
	}

	public interface CheckedBabyCallBack {
		public void onCheckedBaby(int babyNum);
	}

	public void setCheckedBabyCallBack(CheckedBabyCallBack callBack){
		mCheckedBabyCallBack = callBack;
	}

}
