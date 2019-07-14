package com.park61.moduel.acts;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.CommonMethod;

import java.util.List;

/**
 * Created by shushucn2012 on 2016/7/29.
 */
public class MySimpleChoosePage extends BaseActivity{

    private ListView lv_choice;
    private TextView tv_page_title;

    private List<String> mList;
    private List<String> mList2;
    private String chosenItemStr, chosenItemStr2;
    private MyChoiceAdapter adapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_mysimple_choose);
    }

    @Override
    public void initView() {
        tv_page_title = (TextView) findViewById(R.id.tv_page_title);
        lv_choice = (ListView) findViewById(R.id.lv_choice);
    }

    @Override
    public void initData() {
        mList = getIntent().getStringArrayListExtra("LIST_DATA");
        mList2 = getIntent().getStringArrayListExtra("LIST_DATA2");
        tv_page_title.setText(getIntent().getStringExtra("PAGE_TITLE"));
        chosenItemStr = getIntent().getStringExtra("CHOSEN_ITEM");
        adapter = new MyChoiceAdapter();
        lv_choice.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        lv_choice.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chosenItemStr = mList.get(i);
                if(!CommonMethod.isListEmpty(mList2)){
                    chosenItemStr2 = mList2.get(i);
                }
                adapter.notifyDataSetChanged();
                Intent returnData = new Intent();
                returnData.putExtra("RETURN_DATA", chosenItemStr);
                returnData.putExtra("RETURN_DATA2", chosenItemStr2);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
    }

    public class MyChoiceAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public String getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.mysimple_choose_item, null);
            TextView tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            TextView tv_item_remark = (TextView) convertView.findViewById(R.id.tv_item_remark);
            ImageView chk_item = (ImageView) convertView.findViewById(R.id.chk_item);
            tv_item_name.setText(mList.get(position));
            if(!CommonMethod.isListEmpty(mList2)){
                tv_item_remark.setVisibility(View.VISIBLE);
                tv_item_remark.setText(mList2.get(position));
            }
            if(chosenItemStr.contains(mList.get(position))){
                chk_item.setImageResource(R.drawable.xuanze_focus);
            }else{
                chk_item.setImageResource(R.drawable.xuanze_default2);
            }
            return convertView;
        }
    }


}
