package com.park61.moduel.grouppurchase;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.grouppurchase.adapter.FightGroupOpenDetailsAdapter;
import com.park61.moduel.grouppurchase.bean.FightGroupDetailBean;
import com.park61.moduel.grouppurchase.bean.FightGroupOpenDetails;
import com.park61.moduel.order.TradeOrderDetailActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.textview.SecTimeTextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼团详情界面
 */
public class FightGroupDetailsActivity extends BaseActivity implements SecTimeTextView.OnTimeEnd {
    private FightGroupOpenDetailsAdapter mAdapter;
    private List<FightGroupOpenDetails> mList;
    private FightGroupDetailBean item;
    private FightGroupOpenDetails openItem;
    private ImageView icon, img_arrow;
    private SecTimeTextView tv_opentime;
    private TextView tv_goods_name, tv_old_goods_price, tv_fight_num,tv_price, tv_num,
            tv_personnum, tv_msg, tv_description, tv, tv_state;
    private Button btn_open_detail, btn_close_detail, btn_order_detail, btn_group, btn_re_fihgtgroup,btn_regroup,
            btn_gohome,btn_orderdetail;
    private ImageView img1, img2, img3;
    private View bottom_btn, area_personnum, success_area, fail_area, area_opentime, area_num,btn_area_succes;
    private ListView listview;
    private String countDown;
    private Long openId;
    private Long soId;
    private int personNum;
    private int joinedNum;
    private int status;//1-未成团 2-已成团
    private int type;//1-开团人 2-参团人
    private int num;
    private String from;
    private Long userId;
    private Long joinUserId;
    private String statusName;
    private boolean isJoined;//是否参与拼团

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_fightgroup_detail);
    }

    @Override
    public void initView() {
        icon = (ImageView) findViewById(R.id.icon);
        img_arrow = (ImageView) findViewById(R.id.img_arrow);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_old_goods_price = (TextView) findViewById(R.id.tv_old_goods_price);
        tv_fight_num = (TextView) findViewById(R.id.tv_fight_num);
        tv_price  = (TextView) findViewById(R.id.tv_price);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_opentime = (SecTimeTextView) findViewById(R.id.tv_opentime);
        tv_personnum = (TextView) findViewById(R.id.tv_personnum);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv = (TextView) findViewById(R.id.tv);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_description = (TextView) findViewById(R.id.tv_description);
        btn_open_detail = (Button) findViewById(R.id.btn_open_detail);
        btn_close_detail = (Button) findViewById(R.id.btn_close_detail);
        btn_order_detail = (Button) findViewById(R.id.btn_order_detail);
        btn_group = (Button) findViewById(R.id.btn_group);
        btn_regroup = (Button) findViewById(R.id.btn_regroup);
        btn_re_fihgtgroup = (Button) findViewById(R.id.btn_re_fihgtgroup);
        btn_gohome = (Button) findViewById(R.id.btn_gohome);
        btn_orderdetail = (Button) findViewById(R.id.btn_orderdetail);
        listview = (ListView) findViewById(R.id.listview);
        bottom_btn = findViewById(R.id.bottom_btn);
        area_personnum = findViewById(R.id.area_personnum);
        success_area = findViewById(R.id.success_area);
        fail_area = findViewById(R.id.fail_area);
        area_opentime = findViewById(R.id.area_opentime);
        area_num = findViewById(R.id.area_num);
        btn_area_succes = findViewById(R.id.btn_area_succes);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
    }

    @Override
    public void initData() {
        userId = GlobalParam.currentUser.getId();
        soId = getIntent().getLongExtra("soId", -1l);
        openId = getIntent().getLongExtra("opneId", -1l);
        logout("====FightGroupDetailsActivity=====" + openId);
        from = getIntent().getStringExtra("from");
//        statusName = getIntent().getStringExtra("statusName");
        asyncGetFightGroupDeatil();
    }

    @Override
    public void initListener() {
        btn_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToHome();
            }
        });
        btn_orderdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, TradeOrderDetailActivity.class);
                it.putExtra("orderid", soId);
                startActivity(it);
            }
        });
        btn_regroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GroupPurchaseActivity.class);
                startActivity(intent);
            }
        });
        btn_re_fihgtgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GroupPurchaseActivity.class);
                startActivity(intent);
            }
        });
        btn_open_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_arrow.setVisibility(View.VISIBLE);
                listview.setVisibility(View.VISIBLE);
                btn_close_detail.setVisibility(View.VISIBLE);
                btn_open_detail.setVisibility(View.GONE);
            }
        });
        btn_close_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_arrow.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                btn_close_detail.setVisibility(View.GONE);
                btn_open_detail.setVisibility(View.VISIBLE);
            }
        });
        tv_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, FightGroupDescriptionActivity.class);
                startActivity(it);
            }
        });
        btn_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, TradeOrderDetailActivity.class);
                it.putExtra("orderid", soId);
                startActivity(it);
            }
        });
        area_personnum.setOnClickListener(new View.OnClickListener() {//邀请参团
            @Override
            public void onClick(View view) {
                String shareUrl = String.format(AppUrl.FIGHTGROUP_SHARE_URL, openId + "");
                String picUrl = item.getPic();
                String title = getString(R.string.app_name);
                String description = item.getName() + "\n拼团详情";
                showShareDialog(shareUrl, picUrl, title, description);
            }
        });
        btn_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GroupPurchaseGoodsDetailActivity.class);
                intent.putExtra("goodsId", Long.parseLong(item.getPmInfoId()));
                logout("==拼团详情==" + item.getPmInfoId());
//                Long fightGroupOpenId = Long.parseLong(String.valueOf(item.getId()));
//                intent.putExtra("openId", fightGroupOpenId);
                intent.putExtra("opneId", openId);
                intent.putExtra("goodsName", item.getName());
                intent.putExtra("goodsPrice", item.getFightGroupPrice() + "");
                intent.putExtra("goodsOldPrice", item.getOldPrice() + "");
                startActivity(intent);
            }
        });
        tv_opentime.setOnTimeEndLsner(this);
    }

    private void asyncGetFightGroupDeatil() {
        String wholeUrl = AppUrl.host + AppUrl.FIGHT_GROUP_DETAIL;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("opneId", openId.intValue());
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
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            item = gson.fromJson(jsonResult.toString(), FightGroupDetailBean.class);
            setDate();
        }
    };

    private void setDate() {
        mList = item.getFightGroupOpenDetails();
        ImageManager.getInstance().displayImg(icon, item.getPic());
        tv_goods_name.setText(item.getName());
        tv_old_goods_price.setText("原价：￥" + item.getOldPrice());
        tv_old_goods_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        status = item.getStatus();
        personNum = item.getPersonNum();
        if (personNum == 1) {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.GONE);
            img3.setVisibility(View.GONE);
        } else if (personNum == 2) {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.GONE);
        } else {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.VISIBLE);
            img3.setVisibility(View.VISIBLE);
        }
        int size = item.getFightGroupOpenDetails().size();
        if (size == 1) {
            ImageManager.getInstance().displayCircleImg(img1, item.getFightGroupOpenDetails().get(0).getPictureUrl());
        } else if (size == 2) {
            ImageManager.getInstance().displayCircleImg(img1, item.getFightGroupOpenDetails().get(0).getPictureUrl());
            ImageManager.getInstance().displayCircleImg(img2, item.getFightGroupOpenDetails().get(1).getPictureUrl());
        } else if (size >= 3) {
            ImageManager.getInstance().displayCircleImg(img1, item.getFightGroupOpenDetails().get(0).getPictureUrl());
            ImageManager.getInstance().displayCircleImg(img2, item.getFightGroupOpenDetails().get(1).getPictureUrl());
            ImageManager.getInstance().displayCircleImg(img3, item.getFightGroupOpenDetails().get(2).getPictureUrl());
        }
        joinedNum = item.getJoinedNum();
        tv_fight_num.setText(personNum + "人团：");
        tv_price.setText(FU.formatPrice(item.getFightGroupPrice()));
        type = item.getFightGroupOpenDetails().get(0).getType();
        for (int i = 0; i < mList.size(); i++) {
            joinUserId = Long.parseLong(item.getFightGroupOpenDetails().get(i).getJoinUserId());
            if(joinUserId.equals(userId)){
                isJoined = true;
                break;
            }else{
                isJoined = false;
            }
        }
//        num = personNum - joinedNum;
        num = personNum - size;
        tv_num.setText(num + "");
        tv_personnum.setText("还差" + num + "人");
        countDown = item.getCountDown();
        if (countDown.equals("00:00:00")) {
            area_opentime.setVisibility(View.GONE);
            tv_opentime.setVisibility(View.GONE);
        } else {
            String[] ss = countDown.split(":");
            String h = ss[0];
            String m = ss[1];
            String s = ss[2];
            if (h.substring(0, 1).equals("0")) {
                h = h.substring(1);
            }
            if (m.substring(0, 1).equals("0")) {
                m = m.substring(1);
            }
            if (s.substring(0, 1).equals("0")) {
                s = s.substring(1);
            }
            tv_opentime.setTimes(new long[]{0, Long.parseLong(h), Long.parseLong(m), Long.parseLong(s)});
            if (!tv_opentime.isRun()) {
                tv_opentime.run();
            }
        }
        if (status == 1) {//status=1,未成团
            area_num.setVisibility(View.VISIBLE);
            if ((personNum > joinedNum) && countDown.equals("00:00:00")) {
                fail_area.setVisibility(View.VISIBLE);
                success_area.setVisibility(View.GONE);
                btn_group.setVisibility(View.GONE);
                area_personnum.setVisibility(View.GONE);
                btn_re_fihgtgroup.setVisibility(View.VISIBLE);
                bottom_btn.setVisibility(View.VISIBLE);
                tv_personnum.setVisibility(View.GONE);
                area_opentime.setVisibility(View.GONE);
                tv_state.setText("呜呜...还差");
                tv_num.setText(num + "");
                tv.setText("人,拼团失败了");
            } else if ((personNum > joinedNum) && !countDown.equals("00:00:00")) {
                success_area.setVisibility(View.GONE);
                fail_area.setVisibility(View.GONE);
                btn_re_fihgtgroup.setVisibility(View.GONE);
                area_opentime.setVisibility(View.VISIBLE);
                tv_state.setText("还差");
                tv_num.setText(num + "");
                tv.setText("人,邀请小伙伴一起入团吧");
                if (!isJoined) {//未参与拼团
                    btn_group.setVisibility(View.VISIBLE);
                    btn_group.setText("我要参团");
                    bottom_btn.setVisibility(View.GONE);
                } else {//参与拼团
                    btn_group.setVisibility(View.GONE);
                    bottom_btn.setVisibility(View.VISIBLE);
                    area_personnum.setVisibility(View.VISIBLE);
                }
            }
        } else {//status=2,已成团
            if(isJoined){
                btn_area_succes.setVisibility(View.VISIBLE);
                btn_regroup.setVisibility(View.GONE);
                tv_state.setText(personNum+"人团，成功拼团!");
            }else{
                btn_area_succes.setVisibility(View.GONE);
                btn_regroup.setVisibility(View.VISIBLE);
                tv_state.setText("人数已满啦！可以重新开个团哦");
            }
            success_area.setVisibility(View.VISIBLE);
            fail_area.setVisibility(View.GONE);
            btn_group.setVisibility(View.GONE);
            bottom_btn.setVisibility(View.GONE);
            area_opentime.setVisibility(View.GONE);
            tv_num.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
//            tv_state.setText("人数已满啦！可以重新开个团哦");
        }
        mAdapter = new FightGroupOpenDetailsAdapter(mContext, mList);
        listview.setAdapter(mAdapter);
        if (from != null) {//来源"pay"
            String shareUrl = String.format(AppUrl.FIGHTGROUP_SHARE_URL, openId + "");
            String picUrl = item.getPic();
            String title = getString(R.string.app_name);
            String description = item.getName() + "\n拼团详情";
            showShareDialog(shareUrl, picUrl, title, description);
        }
        if(status == 1&&!countDown.equals("00:00:00")){//已付款，未成团
            String shareUrl = String.format(AppUrl.FIGHTGROUP_SHARE_URL, openId + "");
            String picUrl = item.getPic();
            String title = getString(R.string.app_name);
            String description = item.getName() + "\n拼团详情";
            showShareDialog(shareUrl, picUrl, title, description);
        }
    }

    @Override
    public void onEnd() {
        area_opentime.setVisibility(View.GONE);
        tv_opentime.setVisibility(View.GONE);
        area_num.setVisibility(View.VISIBLE);
        fail_area.setVisibility(View.VISIBLE);
        success_area.setVisibility(View.GONE);
        btn_group.setVisibility(View.GONE);
        area_personnum.setVisibility(View.GONE);
        btn_re_fihgtgroup.setVisibility(View.VISIBLE);
        bottom_btn.setVisibility(View.VISIBLE);
        area_personnum.setVisibility(View.GONE);
        btn_re_fihgtgroup.setVisibility(View.VISIBLE);
        area_opentime.setVisibility(View.GONE);
        tv_state.setText("呜呜...还差");
        tv_num.setText(num + "");
        tv.setText("人,拼团失败了");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                asyncGetFightGroupDeatil();
//                dismissDialog();
//            }
//        }, 3 * 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv_opentime.setRun(false);
        tv_opentime.setOnTimeEndLsner(null);
    }
}
