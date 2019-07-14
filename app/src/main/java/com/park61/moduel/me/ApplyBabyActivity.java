package com.park61.moduel.me;

import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.me.adapter.ApplyBabyListAdapter;
import com.park61.moduel.me.bean.ApplyBabyName;

import java.util.List;

/**
 * 报名儿童界面
 */
public class ApplyBabyActivity  extends BaseActivity{
    private ApplyBabyListAdapter mAdapter;
    private List<ApplyBabyName> mList;
    private ListView listview;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_applybaby);
    }

    @Override
    public void initView() {
        listview = (ListView) findViewById(R.id.listview);
    }

    @Override
    public void initData() {
        mList = (List<ApplyBabyName>) getIntent().getSerializableExtra("mList");
        mAdapter = new ApplyBabyListAdapter(mContext,mList);
        listview.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {

    }
}
