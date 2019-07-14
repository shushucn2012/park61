package com.park61.moduel.dreamhouse;

import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.dreamhouse.adapter.SameDreamListAdapter;
import com.park61.moduel.dreamhouse.bean.SameDreamInfo;

import java.util.ArrayList;

/**
 * 同梦人列表界面
 */
public class SameDreamActivity extends BaseActivity {
    private ListView lv_listview;
    private SameDreamListAdapter mAdapter;
    private ArrayList<SameDreamInfo> mList;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dreamhouse_same_dream);
    }

    @Override
    public void initView() {
        lv_listview = (ListView) findViewById(R.id.lv_listview);
    }

    @Override
    public void initData() {
        mList = (ArrayList)getIntent().getParcelableArrayListExtra("predictionList");
        mAdapter = new SameDreamListAdapter(mContext,mList);
        lv_listview.setAdapter(mAdapter);
        if(mList.size()<1){
            ViewInitTool.setListEmptyView(mContext,lv_listview,"暂无数据",
                    R.drawable.quexing,null,100,95);
        }
    }

    @Override
    public void initListener() {

    }
}
