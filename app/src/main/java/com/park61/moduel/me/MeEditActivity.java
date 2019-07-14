package com.park61.moduel.me;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.moduel.me.bean.RelationItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

public class MeEditActivity extends BaseActivity {

    private EditText edit_info;
    private Button btn_save;
    private ListView lv_relation;
    private List<RelationItem> relationList;
    private int selectedPos = -1;
    private RelationListAdapter adapter;
    private boolean isRelation = false;// 是否是选择关系
    private String oldData;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_me_edit);
    }

    @Override
    public void initView() {
        edit_info = (EditText) findViewById(R.id.edit_info);
        btn_save = (Button) findViewById(R.id.btn_save);
        lv_relation = (ListView) findViewById(R.id.lv_relation);
    }

    @Override
    public void initData() {
        oldData = getIntent().getStringExtra("current_data");
        if (!"未填写".equals(oldData))
            edit_info.setText(oldData);
        if (getIntent().getBooleanExtra("relation", false)) {// 如果是选择亲属关系，则显示列表隐藏输入框
            isRelation = true;
            edit_info.setVisibility(View.GONE);
            lv_relation.setVisibility(View.VISIBLE);
            relationList = new ArrayList<RelationItem>();
            adapter = new RelationListAdapter();
            lv_relation.setAdapter(adapter);
            asyncGetRelationList();
        }
    }

    @Override
    public void initListener() {
        btn_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String newData = "";
                Intent backData = new Intent();
                if (!isRelation) {
                    newData = edit_info.getText().toString();
                    if (TextUtils.isEmpty(newData)) {
                        showShortToast("未填写数据！");
                        return;
                    }
                } else {
                    if (selectedPos == -1) {
                        showShortToast("未选择关系！");
                        return;
                    }
                    newData = relationList.get(selectedPos).getRelationName();
                    backData.putExtra("relationId", relationList.get(selectedPos).getId());
                }
                backData.putExtra("new_data", newData);
                setResult(RESULT_OK, backData);
                hideKeyboard();
                finish();
            }
        });
        lv_relation.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedPos = position;
                adapter.notifyDataSetChanged();
            }
        });
        left_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });
    }

    private class RelationListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return relationList.size();
        }

        @Override
        public RelationItem getItem(int position) {
            return relationList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.relation_list_item, null);
            }
            TextView rname = (TextView) convertView
                    .findViewById(R.id.tv_relation_name);
            ImageView img_choosse = (ImageView) convertView
                    .findViewById(R.id.img_choosse);
            View bottom_line = convertView.findViewById(R.id.bottom_line);
            if (position == relationList.size() - 1)
                bottom_line.setVisibility(View.GONE);
            else
                bottom_line.setVisibility(View.VISIBLE);
            if (position == selectedPos)
                img_choosse.setImageResource(R.drawable.xuanze_focus);
            else
                img_choosse.setImageResource(R.drawable.xuanze_default);
            rname.setText(getItem(position).getRelationName());
            return convertView;
        }
    }

    /**
     * 获取用户孩子关系列表
     */
    private void asyncGetRelationList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_RELATION_LIST_END;
        netRequest.startRequest(wholeUrl, Method.POST, "", 0, listener);
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
            for (int i = 0; i < jay.length(); i++) {
                String id = jay.optJSONObject(i).optString("id");
                String relationName = jay.optJSONObject(i).optString(
                        "relationName");
                relationList.add(new RelationItem(id, relationName));
                if (relationName.equals(oldData))//已有关系显示为选中
                    selectedPos = i;
            }
            adapter.notifyDataSetChanged();
        }
    };

}
