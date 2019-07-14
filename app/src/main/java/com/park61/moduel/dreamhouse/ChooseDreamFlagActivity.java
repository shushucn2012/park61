package com.park61.moduel.dreamhouse;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.park61.widget.gridview.GridViewForScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布梦想-选择梦想标签页面
 * Created by shushucn2012 on 2017/1/11.
 */
public class ChooseDreamFlagActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_mostflag_tip, tv_chosen_flag0, tv_chosen_flag1, tv_chosen_flag2;
    private View area_chosen_flag0, area_chosen_flag1, area_chosen_flag2;
    private ImageView img_del_flag0, img_del_flag1, img_del_flag2;
    private Button btn_finish;

    private GridViewForScrollView gv0, gv1;
    private DreamFlagGvAdapter mDreamFlagGvAdapter0;

    private List<DreamFlagBean> flagBeanList0, flagBeanList1;
    private List<DreamFlagBean> chosenFlagList;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_choose_dream_flag);
    }

    @Override
    public void initView() {
        tv_mostflag_tip = (TextView) findViewById(R.id.tv_mostflag_tip);
        tv_chosen_flag0 = (TextView) findViewById(R.id.tv_chosen_flag0);
        tv_chosen_flag1 = (TextView) findViewById(R.id.tv_chosen_flag1);
        tv_chosen_flag2 = (TextView) findViewById(R.id.tv_chosen_flag2);
        area_chosen_flag0 = findViewById(R.id.area_chosen_flag0);
        area_chosen_flag1 = findViewById(R.id.area_chosen_flag1);
        area_chosen_flag2 = findViewById(R.id.area_chosen_flag2);
        gv0 = (GridViewForScrollView) findViewById(R.id.gv0);
        gv1 = (GridViewForScrollView) findViewById(R.id.gv1);
        img_del_flag0 = (ImageView) findViewById(R.id.img_del_flag0);
        img_del_flag1 = (ImageView) findViewById(R.id.img_del_flag1);
        img_del_flag2 = (ImageView) findViewById(R.id.img_del_flag2);
        btn_finish = (Button) findViewById(R.id.btn_finish);
    }

    @Override
    public void initData() {
        chosenFlagList = new ArrayList<>();
        flagBeanList0 = new ArrayList<>();
      /*  flagBeanList0.add(new DreamFlagBean(0, "小课"));
        flagBeanList0.add(new DreamFlagBean(1, "启育"));
        flagBeanList0.add(new DreamFlagBean(2, "手工"));
        flagBeanList0.add(new DreamFlagBean(3, "儿童剧"));
        flagBeanList0.add(new DreamFlagBean(4, "玩具"));
        flagBeanList0.add(new DreamFlagBean(5, "才艺"));*/
        mDreamFlagGvAdapter0 = new DreamFlagGvAdapter(flagBeanList0, mContext);
        gv0.setAdapter(mDreamFlagGvAdapter0);

       /* flagBeanList1 = new ArrayList<>();
        flagBeanList1.add(new DreamFlagBean(0, "小课1"));
        flagBeanList1.add(new DreamFlagBean(1, "启育1"));
        flagBeanList1.add(new DreamFlagBean(2, "手工1"));
        flagBeanList1.add(new DreamFlagBean(3, "儿童剧1"));
        flagBeanList1.add(new DreamFlagBean(4, "玩具1"));
        flagBeanList1.add(new DreamFlagBean(5, "才艺1"));
        mDreamFlagGvAdapter1 = new DreamFlagGvAdapter(flagBeanList1, mContext);
        gv1.setAdapter(mDreamFlagGvAdapter1);*/

        asyncGetRequirementTypeList();
    }

    @Override
    public void initListener() {
        gv0.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chosenFlagList.size() >= 1) {
                    showShortToast("只能选择一个标签");
                    return;
                }
                chosenFlagList.add(flagBeanList0.get(position));
                mDreamFlagGvAdapter0.selectItem(position);

                tv_mostflag_tip.setVisibility(View.GONE);
                if (chosenFlagList.size() == 1) {
                    area_chosen_flag0.setVisibility(View.VISIBLE);
                    tv_chosen_flag0.setText(chosenFlagList.get(0).getName());
                } else if (chosenFlagList.size() == 2) {
                    area_chosen_flag0.setVisibility(View.VISIBLE);
                    area_chosen_flag1.setVisibility(View.VISIBLE);
                    tv_chosen_flag0.setText(chosenFlagList.get(0).getName());
                    tv_chosen_flag1.setText(chosenFlagList.get(1).getName());
                } else if (chosenFlagList.size() == 3) {
                    area_chosen_flag0.setVisibility(View.VISIBLE);
                    area_chosen_flag1.setVisibility(View.VISIBLE);
                    area_chosen_flag2.setVisibility(View.VISIBLE);
                    tv_chosen_flag0.setText(chosenFlagList.get(0).getName());
                    tv_chosen_flag1.setText(chosenFlagList.get(1).getName());
                    tv_chosen_flag2.setText(chosenFlagList.get(2).getName());
                }
            }
        });
        /*gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chosenFlagList.size() >= 3) {
                    return;
                }
                chosenFlagList.add(flagBeanList1.get(position));
                mDreamFlagGvAdapter1.selectItem(position);

                tv_mostflag_tip.setVisibility(View.GONE);
                if (chosenFlagList.size() == 1) {
                    area_chosen_flag0.setVisibility(View.VISIBLE);
                    tv_chosen_flag0.setText(chosenFlagList.get(0).getName());
                } else if (chosenFlagList.size() == 2) {
                    area_chosen_flag0.setVisibility(View.VISIBLE);
                    area_chosen_flag1.setVisibility(View.VISIBLE);
                    tv_chosen_flag0.setText(chosenFlagList.get(0).getName());
                    tv_chosen_flag1.setText(chosenFlagList.get(1).getName());
                } else if (chosenFlagList.size() == 3) {
                    area_chosen_flag0.setVisibility(View.VISIBLE);
                    area_chosen_flag1.setVisibility(View.VISIBLE);
                    area_chosen_flag2.setVisibility(View.VISIBLE);
                    tv_chosen_flag0.setText(chosenFlagList.get(0).getName());
                    tv_chosen_flag1.setText(chosenFlagList.get(1).getName());
                    tv_chosen_flag2.setText(chosenFlagList.get(2).getName());
                }
            }
        });*/
        img_del_flag0.setOnClickListener(this);
        img_del_flag1.setOnClickListener(this);
        img_del_flag2.setOnClickListener(this);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenFlagList.size() == 0) {
                    showShortToast("您未选择标签");
                    return;
                }
                Intent returnData = new Intent();
                returnData.putExtra("FLAG_ID", chosenFlagList.get(0).getId());
                returnData.putExtra("FLAG_NAME", chosenFlagList.get(0).getName());
                setResult(RESULT_OK , returnData);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_del_flag0:
                chosenFlagList.get(0).setChosen(false);
                mDreamFlagGvAdapter0.notifyDataSetChanged();
                //mDreamFlagGvAdapter1.notifyDataSetChanged();
                area_chosen_flag0.setVisibility(View.GONE);
                chosenFlagList.remove(0);
                break;
            case R.id.img_del_flag1:
                if (chosenFlagList.size() == 1) {
                    chosenFlagList.get(0).setChosen(false);
                    mDreamFlagGvAdapter0.notifyDataSetChanged();
                    //mDreamFlagGvAdapter1.notifyDataSetChanged();
                    area_chosen_flag1.setVisibility(View.GONE);
                    chosenFlagList.remove(0);
                } else if (chosenFlagList.size() > 1) {
                    chosenFlagList.get(1).setChosen(false);
                    mDreamFlagGvAdapter0.notifyDataSetChanged();
                    //mDreamFlagGvAdapter1.notifyDataSetChanged();
                    area_chosen_flag1.setVisibility(View.GONE);
                    chosenFlagList.remove(1);
                }
                break;
            case R.id.img_del_flag2:
                chosenFlagList.get(chosenFlagList.size() - 1).setChosen(false);
                mDreamFlagGvAdapter0.notifyDataSetChanged();
                //mDreamFlagGvAdapter1.notifyDataSetChanged();
                area_chosen_flag2.setVisibility(View.GONE);
                chosenFlagList.remove(chosenFlagList.size() - 1);
                break;
        }
        if (chosenFlagList.size() == 0) {
            tv_mostflag_tip.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取标签
     */
    protected void asyncGetRequirementTypeList() {
        String wholeUrl = AppUrl.host + AppUrl.GET_REQUIREMENT_TYPE_LIST;
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
                flagBeanList0.add(dreamFlagBean);
            }
            mDreamFlagGvAdapter0.notifyDataSetChanged();
        }
    };
}
