package com.park61.moduel.acts;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.moduel.acts.adapter.CourseChooseBabyListAdapter;
import com.park61.moduel.me.AddBabyActivity;
import com.park61.moduel.me.BabyInfoActivity;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/7/22.
 */

public class CourseChooseBabyActivity extends BaseActivity {

    private static final int REQ_ADD_BABY = 0;
    private static final int REQ_MODIFY_BABY = 1;
    private List<BabyItem> babyList;
    private List<BabyItem> chosenList = new ArrayList<>();
    private CourseChooseBabyListAdapter adapter;
    private String childsIds = "";
    private Boolean needChooseLast = false;

    private ListView lv_child;
    private ImageView img_new_baby;
    private Button btn_confirm;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_course_choose_baby);
    }

    @Override
    public void initView() {
        lv_child = (ListView) findViewById(R.id.lv_child);
        img_new_baby = (ImageView) findViewById(R.id.img_new_baby);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {
        childsIds = getIntent().getStringExtra("childsIds");
        if (TextUtils.isEmpty(childsIds)) {
            childsIds = "";
        }
        babyList = new ArrayList<>();
        adapter = new CourseChooseBabyListAdapter(mContext, babyList);
        lv_child.setAdapter(adapter);
        asyncGetUserChilds();
    }

    @Override
    public void initListener() {
        lv_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (babyList.get(position).isChosen()) {
                    babyList.get(position).setChosen(false);
                } else {
                    babyList.get(position).setChosen(true);
                }
                adapter.notifyDataSetChanged();
            }
        });
        img_new_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, AddBabyActivity.class), REQ_ADD_BABY);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnData = new Intent();
                for (int i = 0; i < babyList.size(); i++) {
                    if (babyList.get(i).isChosen()) {
                        chosenList.add(babyList.get(i));
                    }
                }
                if (chosenList.size() <= 0) {
                    showShortToast("请选择报名人");
                    return;
                }
                returnData.putExtra("chosenList", (Serializable) chosenList);
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
        adapter.setOnEditClickedLsner(new CourseChooseBabyListAdapter.OnEditClickedLsner() {
            @Override
            public void onClicked(int pos) {
                Intent it = new Intent(mContext, BabyInfoActivity.class);
                it.putExtra("baby_info", babyList.get(pos));
                startActivityForResult(it, REQ_MODIFY_BABY);
            }
        });
    }

    /**
     * 获取用户孩子列表
     */
    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String requestBodyData = ParamBuild.getUserChilds();
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
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray jay = jsonResult.optJSONArray("list");
            babyList.clear();
            String[] chosenIds = childsIds.split(",");
            for (int i = 0; i < jay.length(); i++) {
                BabyItem b = gson.fromJson(jay.optJSONObject(i).toString(), BabyItem.class);
                for (String id : chosenIds) {
                    if (b.getId() == FU.paseLong(id)) {
                        b.setChosen(true);
                    }
                }
                babyList.add(b);
            }
            if (needChooseLast) {
                babyList.get(babyList.size() - 1).setChosen(true);
                needChooseLast = false;
            }
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_ADD_BABY) {
            needChooseLast = true;
            asyncGetUserChilds();
        } else if (resultCode == RESULT_OK && requestCode == REQ_MODIFY_BABY) {
            needChooseLast = false;
            asyncGetUserChilds();
        }
    }
}
