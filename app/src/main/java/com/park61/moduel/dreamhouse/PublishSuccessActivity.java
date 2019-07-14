package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.ActDetailsActivity;
import com.park61.moduel.dreamhouse.adapter.AddDreamRecGamesListAdapter;
import com.park61.moduel.dreamhouse.bean.AddDreamRecommendGames;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 发布成功页面
 */
public class PublishSuccessActivity extends BaseActivity {
    private Button btn_finish, btn_detail;
    private ListView lv_recommend_act;
    private AddDreamRecGamesListAdapter mAdapter;
    private ArrayList<AddDreamRecommendGames> mList;
    private Long requirementId;//梦想id

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_dreamhouse_publish_success);
    }

    @Override
    public void initView() {
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_detail = (Button) findViewById(R.id.btn_detail);
        lv_recommend_act = (ListView) findViewById(R.id.lv_recommend_act);
        ViewInitTool.setListEmptyView(mContext,lv_recommend_act,"暂无数据",R.drawable.quexing,
                null,100,95);
    }

    @Override
    public void initData() {
        requirementId = getIntent().getLongExtra("requirementId",0l);
        mList = new ArrayList<AddDreamRecommendGames>();
        mAdapter = new AddDreamRecGamesListAdapter(mContext,mList);
        lv_recommend_act.setAdapter(mAdapter);
        asyncRecommendGames();
    }

    @Override
    public void initListener() {
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,DreamDetailActivity.class);
                intent.putExtra("requirementId",requirementId);
                startActivity(intent);
            }
        });
        lv_recommend_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(mContext,ActDetailsActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                intent.putExtra("isToComt", true);
                intent.putExtra("reqPredId",mList.get(position).getRequirementPredictionId());
                logout("========发布成功====reqPredId======="+mList.get(position).getRequirementPredictionId());
                startActivity(intent);
            }
        });
    }
    /**
     * 加入梦想成功后游戏推荐
     */
    protected void asyncRecommendGames(){
        String wholeUrl = AppUrl.host + AppUrl.RECOMMENDED_ACT_BYREQ;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("requirementId",requirementId);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }
    BaseRequestListener listener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
//            showShortToast(errorMsg);
        }
        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ArrayList<AddDreamRecommendGames> currentList = new ArrayList<AddDreamRecommendGames>();
            JSONArray jay = jsonResult.optJSONArray("list");
            for(int i=0;i<jay.length();i++){
                JSONObject jot = jay.optJSONObject(i);
                AddDreamRecommendGames item = gson.fromJson(jot.toString(),AddDreamRecommendGames.class);
                currentList.add(item);
            }
            mList.addAll(currentList);
            mAdapter.notifyDataSetChanged();
        }
    };
}
