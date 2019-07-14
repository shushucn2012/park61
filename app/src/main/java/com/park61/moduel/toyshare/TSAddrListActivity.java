package com.park61.moduel.toyshare;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.toyshare.adapter.JoyAddrListAdapter;
import com.park61.moduel.toyshare.bean.TSAddrBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/6/22.
 */

public class TSAddrListActivity extends BaseActivity {

    private int id;
    private List<TSAddrBean> addrList;
    private JoyAddrListAdapter adapter;

    private ListView lv_addr;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_addrlist);
    }

    @Override
    public void initView() {
        setPagTitle("选择地址");
        lv_addr = (ListView) findViewById(R.id.lv_addr);
    }

    @Override
    public void initData() {
        addrList = new ArrayList<>();
        String jayStr = getIntent().getStringExtra("ADDRLIST");
        JSONArray addrJay = null;
        try {
            addrJay = new JSONArray(jayStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < addrJay.length(); i++) {
            TSAddrBean bean = gson.fromJson(addrJay.optJSONObject(i).toString(), TSAddrBean.class);
            addrList.add(bean);
        }
        adapter = new JoyAddrListAdapter(mContext, addrList);
        lv_addr.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        lv_addr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("index", position);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}
