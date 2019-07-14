package com.park61.moduel.me;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.moduel.dreamhouse.adapter.DreamFlagGvAdapter;
import com.park61.moduel.dreamhouse.bean.DreamFlagBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/7/6.
 */

public class AuthChooseLabelActivity extends BaseActivity {

    private GridView gv_label;
    private TextView tv_confirm;
    private EditText edit_sousuo;
    private ImageView img_del;

    private List<DreamFlagBean> flagBeanList;//所有标签列表
    private List<DreamFlagBean> changeList;//变化列表
    private List<DreamFlagBean> chosenFlagList;//已选列表
    private DreamFlagGvAdapter mDreamFlagGvAdapter;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_auth_chooselabel);
    }

    @Override
    public void initView() {
        gv_label = (GridView) findViewById(R.id.gv_label);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        img_del = (ImageView) findViewById(R.id.img_del);
    }

    @Override
    public void initData() {
        chosenFlagList = new ArrayList<>();
        flagBeanList = new ArrayList<>();
        changeList = new ArrayList<>();
        mDreamFlagGvAdapter = new DreamFlagGvAdapter(changeList, mContext);
        gv_label.setAdapter(mDreamFlagGvAdapter);
        asyncGetTypeList();
    }

    @Override
    public void initListener() {
        gv_label.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chosenFlagList.size() >= 1) {
                    showShortToast("只能选择一个标签");
                    return;
                }
                chosenFlagList.add(changeList.get(position));
                mDreamFlagGvAdapter.selectItem(position);
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenFlagList.size() == 0) {
                    showShortToast("请选择标签");
                    return;
                }
                Intent returnData = new Intent();
                returnData.putExtra("flagBean", chosenFlagList.get(0));
                setResult(RESULT_OK, returnData);
                finish();
            }
        });
        edit_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_sousuo.getText().toString())) {
                    img_del.setVisibility(View.GONE);
                } else {
                    img_del.setVisibility(View.VISIBLE);
                }
                changeList.clear();
                if (TextUtils.isEmpty(s.toString())) {//为空时直接显示全部
                    changeList.addAll(flagBeanList);//开始搜索时变化列表等于全部
                    mDreamFlagGvAdapter.notifyDataSetChanged();
                } else {
                    List<Integer> list = new ArrayList<Integer>();
                    for (int i = 0; i < flagBeanList.size(); i++) {
                        if (flagBeanList.get(i).getName().contains(s.toString())) {//如果此项包含关键字就加进来
                            changeList.add(flagBeanList.get(i));
                        }
                    }
                    mDreamFlagGvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sousuo.setText("");
                img_del.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获取标签
     */
    protected void asyncGetTypeList() {
        String wholeUrl = AppUrl.host + AppUrl.domainList;
        String requestBodyData = "";
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listListener);
    }

    BaseRequestListener listListener = new JsonRequestListener() {
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
            for (int i = 0; i < jay.length(); i++) {
                DreamFlagBean dreamFlagBean = gson.fromJson(jay.optJSONObject(i).toString(), DreamFlagBean.class);
                dreamFlagBean.setName("#" + dreamFlagBean.getName() + "#");
                flagBeanList.add(dreamFlagBean);
            }
            changeList.addAll(flagBeanList);
            mDreamFlagGvAdapter.notifyDataSetChanged();
        }
    };
}
