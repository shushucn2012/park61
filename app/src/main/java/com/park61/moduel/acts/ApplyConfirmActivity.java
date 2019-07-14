package com.park61.moduel.acts;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.adapter.ApplyBabyListAdapter;
import com.park61.moduel.acts.adapter.ApplyBabyListAdapter.CheckedBabyCallBack;
import com.park61.moduel.acts.adapter.ApplySessionGvAdapter;
import com.park61.moduel.acts.bean.ActCouponSelectChildCache;
import com.park61.moduel.acts.bean.ActItem;
import com.park61.moduel.acts.bean.ActivitySessionVo;
import com.park61.moduel.me.bean.BabyItem;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 游戏报名页面
 */
public class ApplyConfirmActivity extends BaseActivity implements CheckedBabyCallBack {

    private ListView lv_babylist;
    private ArrayList<BabyItem> applyBabyList = new ArrayList<BabyItem>();
    private ArrayList<BabyItem> checkedList;// 获取选择的孩子列表
    private ApplyBabyListAdapter adapter;
    private ApplySessionGvAdapter mApplySessionGvAdapter;

    private int elderNum = 0;
    private int babyNum = 0;
    private String price = "";
    private String adultPrice = "";
    private ActItem curAct;
    private ActivitySessionVo activitySessionVo;

    private Button btn_add, btn_reduce, btn_add_child, btn_confirm;
    private TextView tv_total_money, tv_adult_price;
    private GridView gv_session;
    private EditText et_elder_num;
    private Long reqPredId;//梦想预报名id

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_apply_confirm);
    }

    @Override
    public void initView() {
        lv_babylist = (ListView) findViewById(R.id.lv_babylist);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_reduce = (Button) findViewById(R.id.btn_reduce);
        et_elder_num = (EditText) findViewById(R.id.et_elder_num);
        tv_adult_price = (TextView) findViewById(R.id.tv_adult_price);
        btn_add_child = (Button) findViewById(R.id.btn_add_child);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        gv_session = (GridView) findViewById(R.id.gv_session);
    }

    @Override
    public void initData() {
        reqPredId = getIntent().getLongExtra("reqPredId",-1l);
        curAct = (ActItem) getIntent().getSerializableExtra("actdetails");
        int sessionPos = getIntent().getIntExtra("sessionPos", -1);
        activitySessionVo = (ActivitySessionVo) getIntent().getSerializableExtra("activitySessionVo");
        if (sessionPos == -1) {
            for (int i = 0; i < activitySessionVo.getActSessionList().size(); i++) {
                if (activitySessionVo.getActSessionList().get(i).isApply()) {
                    sessionPos = i;
                    break;
                }
            }
        }

        if(activitySessionVo.getActSessionList().get(sessionPos).getIsProm() == 0) {//非促销
            price = activitySessionVo.getActSessionList().get(sessionPos).getActPrice();
            adultPrice = activitySessionVo.getActSessionList().get(sessionPos).getAdultPrice();
        }else if (activitySessionVo.getActSessionList().get(sessionPos).getIsProm() == 1){//促销
            price = activitySessionVo.getActSessionList().get(sessionPos).getChildPromPrice();
            adultPrice = activitySessionVo.getActSessionList().get(sessionPos).getAdultPromPrice();
        }
        tv_adult_price.setText(FU.zeroToMF(adultPrice));

        mApplySessionGvAdapter = new ApplySessionGvAdapter(activitySessionVo.getActSessionList(), mContext);
        gv_session.setAdapter(mApplySessionGvAdapter);
        mApplySessionGvAdapter.selectItem(sessionPos);

        applyBabyList = new ArrayList<BabyItem>();
        adapter = new ApplyBabyListAdapter(mContext, applyBabyList, price);
        adapter.setCheckedBabyCallBack(this);
        lv_babylist.setAdapter(adapter);

        asyncGetUserChilds();
    }

    @Override
    public void initListener() {
        gv_session.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!activitySessionVo.getActSessionList().get(position).isApply()) {
                    return;
                }
                mApplySessionGvAdapter.selectItem(position);

                if(activitySessionVo.getActSessionList().get(position).getIsProm() == 0) {//非促销
                    price = activitySessionVo.getActSessionList().get(position).getActPrice();
                    adultPrice = activitySessionVo.getActSessionList().get(position).getAdultPrice();
                }else if (activitySessionVo.getActSessionList().get(position).getIsProm() == 1){//促销
                    price = activitySessionVo.getActSessionList().get(position).getChildPromPrice();
                    adultPrice = activitySessionVo.getActSessionList().get(position).getAdultPromPrice();
                }
                adapter.setActPrice(price);
                tv_adult_price.setText(FU.zeroToMF(adultPrice));

                countTotal();
            }
        });
        btn_add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int maxNum = activitySessionVo.getActSessionList().get(mApplySessionGvAdapter.getSelectedPos())
                        .getActLowQuotaLimit().intValue();
                if (elderNum < maxNum) {
                    elderNum++;
                    updateElderNumView();
                } else {
                    showShortToast("人数超过限制");
                }
            }
        });
        btn_reduce.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (elderNum > 0) {
                    elderNum--;
                    updateElderNumView();
                }
            }
        });
        btn_add_child.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int maxNum = activitySessionVo.getActSessionList().get(mApplySessionGvAdapter.getSelectedPos())
                        .getActLowQuotaLimit().intValue();
                if (adapter.countAddChild() >= maxNum)// 最大添加
                    return;
                if (adapter.getSelectedPos().size() < maxNum) {
                    applyBabyList.add(new BabyItem(-1l, ""));
                    adapter.notifyDataSetChanged();
                } else {
                    showShortToast("人数超过限制");
                }
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!validate())
                    return;
                int pos = mApplySessionGvAdapter.getSelectedPos();
                logout("id======"+activitySessionVo.getActSessionList().get(pos).getId());
                logout("can======"+ViewInitTool.getCanUseCouponByAct(activitySessionVo.getActSessionList().get(pos)));
                ActCouponSelectChildCache.canUseCouponAct.put(
                        activitySessionVo.getActSessionList().get(pos).getId(),
                        ViewInitTool.getCanUseCouponByAct(activitySessionVo.getActSessionList().get(pos)));
                asyncConfirmApply();
            }
        });
        et_elder_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                elderNum = FU.paseInt(s.toString());
                countTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 报名验证
     */
    protected boolean validate() {
        // 获取选择的孩子列表
        checkedList = new ArrayList<BabyItem>();
        for (int i = 0; i < applyBabyList.size(); i++) {
            if (adapter.getSelectedPos().contains(i))
                checkedList.add(applyBabyList.get(i));
        }
        /*if (checkedList.size() == 0) {
            showShortToast("您没有选择孩子！");
            return false;
        }*/
        for (BabyItem item : checkedList) {
            if (TextUtils.isEmpty(item.getName()) && TextUtils.isEmpty(item.getPetName())) {
                showShortToast("您没有输入孩子的名称！");
                return false;
            }
        }
        return true;
    }

    public void updateElderNumView() {
        et_elder_num.setText(elderNum + "");
        countTotal();
    }

    @Override
    public void onCheckedBaby(int babyNum) {
        this.babyNum = babyNum;
        countTotal();
    }

    /**
     * 计算并显示总价
     */
    public void countTotal() {
        double childTotal = FU.mul(FU.paseDb(babyNum + ""), Double.parseDouble(price));
        double adultTotal = FU.mul(FU.paseDb(elderNum + ""), Double.parseDouble(adultPrice));
        double total = FU.add(childTotal, adultTotal);
        tv_total_money.setText("￥" + FU.strDbFmt(total));
    }

    /**
     * 获取用户孩子列表
     */
    private void asyncGetUserChilds() {
        String wholeUrl = AppUrl.host + AppUrl.GET_USER_CHILDS_END;
        String requestBodyData = ParamBuild.getUserChilds();
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, listener);
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
            applyBabyList.clear();
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                BabyItem b = gson.fromJson(jot.toString(), BabyItem.class);
                applyBabyList.add(b);
            }
            adapter.notifyDataSetChanged();
        }
    };

    /**
     * 确认报名
     */
    private void asyncConfirmApply() {
        String wholeUrl = AppUrl.host + AppUrl.CONFIRM_APPLY_END;
        String totalMoney = tv_total_money.getText().toString().replace("￥", "");
        Long actId = activitySessionVo.getActSessionList()
                .get(mApplySessionGvAdapter.getSelectedPos()).getId();
        String requestBodyData = ParamBuild.confirmApply(reqPredId,actId, totalMoney,
                elderNum + "", curAct.getDistance(), GlobalParam.currentUser.getName(), GlobalParam.currentUser.getMobile(),
                checkedList);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, applylistener);
    }

    BaseRequestListener applylistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            if (errorMsg != null) {
                String s = errorMsg.substring(errorMsg.length() - 1, errorMsg.length());
                if (s.equals("@"))
                    errorMsg = errorMsg.substring(0, errorMsg.length() - 1);
            }
            showShortToast(errorMsg.replace("@", "\n"));
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Long applyId = jsonResult.optLong("applyId");
            String actBussinessType = jsonResult.optString("actBussinessType");
            if ("4".equals(actBussinessType)) {//亲子游
                Intent it = new Intent(mContext, FamilyTripOrderInputActivity.class);
                it.putExtra("applyId", applyId);
                it.putExtra("childList", checkedList);
                startActivity(it);
            } else {
                Intent it = new Intent(mContext, ActOrderConfirmActivity.class);
                it.putExtra("applyId", applyId);
                it.putExtra("childList", checkedList);
                startActivity(it);
            }
            finish();
        }
    };

}
