package com.park61.moduel.openmember;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.moduel.openmember.adapter.MyBabyListAdapter;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的宝宝列表界面
 */
public class MyBabyListActivity extends BaseActivity {
    private ListView lv_child;
    private TextView tv_cancle;
    private MyBabyListAdapter mAdapter;
    private List<BabyItem> babyList;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_mybaby_list);
    }

    @Override
    public void initView() {
        lv_child = (ListView) findViewById(R.id.lv_child);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
    }

    @Override
    public void initData() {
        babyList = new ArrayList<BabyItem>();
        mAdapter = new MyBabyListAdapter(mContext,babyList);
        lv_child.setAdapter(mAdapter);
        asyncGetUserChilds();
    }

    @Override
    public void initListener() {
        lv_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("child",babyList.get(position));
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 获取用户孩子列表
     */
    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String requestBodyData = ParamBuild.getUserChilds();
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getChildsLsner);
    }

    BaseRequestListener getChildsLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            finish();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray jay = jsonResult.optJSONArray("list");
            babyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
                babyList.add(b);
            }
            if (CommonMethod.isListEmpty(babyList)) {
                showShortToast("您还没有添加孩子信息!");
                finish();
                return;
            }
            mAdapter.notifyDataSetChanged();
        }
    };
}
