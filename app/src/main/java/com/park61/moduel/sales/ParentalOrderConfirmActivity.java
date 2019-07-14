package com.park61.moduel.sales;

import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.address.AddressActivity;
import com.park61.moduel.address.bean.AddressDetailItem;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.moduel.sales.bean.ParentalOrderCfmBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.AddAddressDialog;
import com.park61.widget.dialog.CanRefreshProgressDialog;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 亲子活动商品订单结算页
 * createby super 20180109
 */
public class ParentalOrderConfirmActivity extends BaseActivity {

    private static final int REQ_CHOOSE_ADDR = 0;
    private String pmInfoIdList;//商品idList,逗号分隔
    private String pmInfoCountList;//商品数量List,逗号分隔
    private int partyId, linkedBusinessId;//活动报名id
    private AddressDetailItem chosenAddress;
    private long selectAddrId;

    private ParentalOrderCfmBean myParentalOrderCfmBean;
    private CanRefreshProgressDialog cpDialog;
    private ImageView img_icon1, img_icon2, img_icon3, img_goods;
    private TextView tv_num, tv_dots, cur_price, src_price, goods_num, goods_title, tv_color, tv_size, tv_goods_money, tv_postage_money,
            tv_zongji_money, tv_total_money, tv_browse_name, tv_browse_number, tv_browse_address;
    private View one_goods_area, bags_area, area_addr, area_null_addr, area_details_addr;
    private Button btn_pay;
    private EditText et_order_remark;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_parental_order_confirm);
    }

    @Override
    public void initView() {
        setPagTitle("确认订单");
        cpDialog = new CanRefreshProgressDialog(mContext, new CanRefreshProgressDialog.OnRefreshClickedLsner() {

            @Override
            public void OnRefreshClicked() {
                asyncGetConfirmInfo();
            }
        });
        tv_goods_money = (TextView) findViewById(R.id.tv_goods_money);
        tv_postage_money = (TextView) findViewById(R.id.tv_postage_money);
        tv_zongji_money = (TextView) findViewById(R.id.tv_zongji_money);
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        img_icon1 = (ImageView) findViewById(R.id.img_icon1);
        img_icon2 = (ImageView) findViewById(R.id.img_icon2);
        img_icon3 = (ImageView) findViewById(R.id.img_icon3);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_dots = (TextView) findViewById(R.id.tv_dots);
        bags_area = findViewById(R.id.bags_area);
        one_goods_area = findViewById(R.id.one_goods_area);
        cur_price = (TextView) findViewById(R.id.cur_price);
        src_price = (TextView) findViewById(R.id.src_price);
        goods_num = (TextView) findViewById(R.id.goods_num);
        goods_title = (TextView) findViewById(R.id.goods_title);
        tv_color = (TextView) findViewById(R.id.tv_color);
        tv_size = (TextView) findViewById(R.id.tv_size);
        img_goods = (ImageView) findViewById(R.id.img_goods);
        area_null_addr = findViewById(R.id.area_null_addr);
        area_details_addr = findViewById(R.id.area_details_addr);
        tv_browse_name = (TextView) findViewById(R.id.tv_browse_name);
        tv_browse_number = (TextView) findViewById(R.id.tv_browse_number);
        tv_browse_address = (TextView) findViewById(R.id.tv_browse_address);
        area_addr = findViewById(R.id.area_addr);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        et_order_remark = (EditText) findViewById(R.id.et_order_remark);
    }

    @Override
    public void initData() {
        partyId = getIntent().getIntExtra("partyId", -1);
        pmInfoIdList = getIntent().getStringExtra("pmInfoIdList");
        pmInfoCountList = getIntent().getStringExtra("pmInfoCountList");
        linkedBusinessId = getIntent().getIntExtra("linkedBusinessId", -1);
        //String jsonResultStr = FilesManager.getFromAssets(mContext, "parental_order_cfm_json");
        //myParentalOrderCfmBean = new Gson().fromJson(jsonResultStr, ParentalOrderCfmBean.class);
        //setDataToView();
        asyncGetConfirmInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //asyncGetAllAddress();
    }

    @Override
    public void initListener() {
        bags_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ParentalPkgGoodsListActivity.class);
                it.putExtra("orderList", (Serializable) myParentalOrderCfmBean.getListPmInfo());
                mContext.startActivity(it);
            }
        });
       /* one_goods_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
                intent.putExtra("goodsId", myParentalOrderCfmBean.getListPmInfo().get(0).getId());
                intent.putExtra("goodsName", myParentalOrderCfmBean.getListPmInfo().get(0).getProductCname());
                intent.putExtra("goodsPrice", myParentalOrderCfmBean.getListPmInfo().get(0).getCurrentUnifyPrice() + "");
                mContext.startActivity(intent);
            }
        });*/
        area_addr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddressActivity.class);
                startActivityForResult(intent, REQ_CHOOSE_ADDR);
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (area_null_addr.getVisibility() == View.VISIBLE) {
                    final AddAddressDialog dialog = new AddAddressDialog(mContext);
                    dialog.showDialog(null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismissDialog();
                            Intent intent = new Intent(mContext, AddressActivity.class);
                            startActivityForResult(intent, REQ_CHOOSE_ADDR);
                        }
                    });
                } else {
                    asyncSubmit();
                }
            }
        });
    }

    /**
     * 提交订单去支付
     */
    protected void asyncSubmit() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.soSubmit;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("orderRemark", et_order_remark.getText().toString().trim());
        map.put("uuid", UUID.randomUUID());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, submitLsner);
    }

    BaseRequestListener submitLsner = new JsonRequestListener() {

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
            Long orderId = Long.parseLong(jsonResult.optString("data"));
            Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
            it.putExtra("orderId", orderId);
            startActivity(it);
            finish();
        }
    };

    /**
     * 获取结算详情
     */
    protected void asyncGetConfirmInfo() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.account;
        Map<String, Object> map = new HashMap<String, Object>();
        if (linkedBusinessId > 0) {
            map.put("linkedBusinessId", linkedBusinessId);
        }
        if (selectAddrId > 0) {
            map.put("goodReceiverId", selectAddrId);
        }
        map.put("partyId", partyId);
        map.put("buyFrom", "party");
        map.put("pmInfoIdList", pmInfoIdList);
        map.put("pmInfoCountList", pmInfoCountList);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getGoodsConfirmInfoLsner);
    }

    BaseRequestListener getGoodsConfirmInfoLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            cpDialog.showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            showShortToast(errorMsg);
            cpDialog.showRefreshBtn();
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            cpDialog.dialogDismiss();
            myParentalOrderCfmBean = gson.fromJson(jsonResult.toString(), ParentalOrderCfmBean.class);
            setDataToView();
        }
    };

    private void setDataToView() {
        tv_goods_money.setText(FU.RENMINBI_UNIT + FU.strDbFmt(myParentalOrderCfmBean.getProductAmount()));
        tv_postage_money.setText(FU.RENMINBI_UNIT + FU.strDbFmt(myParentalOrderCfmBean.getOrderDeliveryFee()));
        tv_zongji_money.setText(FU.RENMINBI_UNIT + FU.strDbFmt(myParentalOrderCfmBean.getOrderAmount()));
        tv_total_money.setText(FU.RENMINBI_UNIT + FU.strDbFmt(myParentalOrderCfmBean.getOrderAmount()));

        if (myParentalOrderCfmBean.getListPmInfo().size() == 1) {
            one_goods_area.setVisibility(View.VISIBLE);
            bags_area.setVisibility(View.GONE);
            cur_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(myParentalOrderCfmBean.getListPmInfo().get(0).getCurrentUnifyPrice()));
            src_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 设置中划线
            src_price.setText(FU.RENMINBI_UNIT + FU.strDbFmt(myParentalOrderCfmBean.getListPmInfo().get(0).getMarketPrice()));
            goods_num.setText("X" + myParentalOrderCfmBean.getListPmInfo().get(0).getCount() + "");
            goods_title.setText(myParentalOrderCfmBean.getListPmInfo().get(0).getProductCname());
            tv_color.setText(TextUtils.isEmpty(myParentalOrderCfmBean.getListPmInfo().get(0).getProductColor()) ? "" : "颜色:" + myParentalOrderCfmBean.getListPmInfo().get(0).getProductColor());
            tv_size.setText(TextUtils.isEmpty(myParentalOrderCfmBean.getListPmInfo().get(0).getProductSize()) ? "" : "尺寸:" + myParentalOrderCfmBean.getListPmInfo().get(0).getProductSize());
            ImageManager.getInstance().displayImg(img_goods, myParentalOrderCfmBean.getListPmInfo().get(0).getProductPicUrl());
        } else {
            bags_area.setVisibility(View.VISIBLE);
            one_goods_area.setVisibility(View.GONE);
            tv_num.setText("共" + getGoodsCount() + "件");
            if (myParentalOrderCfmBean.getListPmInfo().size() == 2) {
                tv_dots.setVisibility(View.GONE);
                img_icon1.setVisibility(View.VISIBLE);
                img_icon2.setVisibility(View.VISIBLE);
                img_icon3.setVisibility(View.GONE);
                ImageManager.getInstance().displayImg(img_icon1, myParentalOrderCfmBean.getListPmInfo().get(0).getProductPicUrl());
                ImageManager.getInstance().displayImg(img_icon2, myParentalOrderCfmBean.getListPmInfo().get(1).getProductPicUrl());
            } else if (myParentalOrderCfmBean.getListPmInfo().size() == 3) {
                tv_dots.setVisibility(View.GONE);
                img_icon1.setVisibility(View.VISIBLE);
                img_icon2.setVisibility(View.VISIBLE);
                img_icon3.setVisibility(View.VISIBLE);
                ImageManager.getInstance().displayImg(img_icon1, myParentalOrderCfmBean.getListPmInfo().get(0).getProductPicUrl());
                ImageManager.getInstance().displayImg(img_icon2, myParentalOrderCfmBean.getListPmInfo().get(1).getProductPicUrl());
                ImageManager.getInstance().displayImg(img_icon3, myParentalOrderCfmBean.getListPmInfo().get(2).getProductPicUrl());
            } else if (myParentalOrderCfmBean.getListPmInfo().size() > 3) {
                tv_dots.setVisibility(View.GONE);
                img_icon1.setVisibility(View.VISIBLE);
                img_icon2.setVisibility(View.VISIBLE);
                img_icon3.setVisibility(View.VISIBLE);
                ImageManager.getInstance().displayImg(img_icon1, myParentalOrderCfmBean.getListPmInfo().get(0).getProductPicUrl());
                ImageManager.getInstance().displayImg(img_icon2, myParentalOrderCfmBean.getListPmInfo().get(1).getProductPicUrl());
                ImageManager.getInstance().displayImg(img_icon3, myParentalOrderCfmBean.getListPmInfo().get(2).getProductPicUrl());
            }
        }
        setAddr();
    }

    public int getGoodsCount() {
        int count = 0;
        for (int i = 0; i < myParentalOrderCfmBean.getListPmInfo().size(); i++) {
            count += myParentalOrderCfmBean.getListPmInfo().get(i).getCount();
        }
        return count;
    }

    public void setAddr() {
        if (myParentalOrderCfmBean.getGoodReceiverVO() == null) {
            area_null_addr.setVisibility(View.VISIBLE);
            area_details_addr.setVisibility(View.GONE);
        } else {
            area_null_addr.setVisibility(View.GONE);
            area_details_addr.setVisibility(View.VISIBLE);
            chosenAddress = myParentalOrderCfmBean.getGoodReceiverVO();
            tv_browse_name.setText("联系人：" + chosenAddress.getGoodReceiverName());
            tv_browse_number.setText(chosenAddress.getGoodReceiverMobile());

            String raddrStr = chosenAddress.getGoodReceiverProvinceName()
                    + chosenAddress.getGoodReceiverCityName()
                    + chosenAddress.getGoodReceiverCountyName()
                    + (TextUtils.isEmpty(chosenAddress.getGoodReceiverTownName()) ? "" : chosenAddress.getGoodReceiverTownName())
                    + chosenAddress.getGoodReceiverAddress();

            tv_browse_address.setText(raddrStr);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CHOOSE_ADDR) {
            if (resultCode == RESULT_OK && data != null) {
                selectAddrId = data.getLongExtra("addrId", -1);
            } else {
                selectAddrId = -1;
            }
            asyncGetConfirmInfo();
//            if (resultCode == RESULT_OK && data != null) {
//                area_null_addr.setVisibility(View.GONE);
//                area_details_addr.setVisibility(View.VISIBLE);
//                String rname = data.getStringExtra("rname");
//                String rphone = data.getStringExtra("rphone");
//                String raddr = data.getStringExtra("raddr");
//                tv_browse_name.setText("联系人：" + rname);
//                tv_browse_number.setText(rphone);
//                tv_browse_address.setText(raddr);
//            }
        }
    }

//    /**
//     * 获取用户所有的地址列表(不区分城市)
//     */
//    private void asyncGetAllAddress() {
//        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.getAddressByUserId;
//        String requestBodyData = "";
//        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
//    }
//
//    BaseRequestListener listener = new JsonRequestListener() {
//
//        @Override
//        public void onStart(int requestId) {
//            showDialog();
//        }
//
//        @Override
//        public void onError(int requestId, String errorCode, String errorMsg) {
//            dismissDialog();
//            showShortToast(errorMsg);
//        }
//
//        @Override
//        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
//            dismissDialog();
//            JSONArray jay = jsonResult.optJSONArray("list"); // 获取名为list的Json数组
//            List<AddressDetailItem> mList = new ArrayList<>();
//            for (int i = 0; i < jay.length(); i++) { // 遍历Json数组
//                mList.add(new Gson().fromJson(jay.optJSONObject(i).toString(), AddressDetailItem.class));
//            }
//            if (CommonMethod.isListEmpty(mList)) {
//                area_null_addr.setVisibility(View.VISIBLE);
//                area_details_addr.setVisibility(View.GONE);
//            } else {
//                area_null_addr.setVisibility(View.GONE);
//                area_details_addr.setVisibility(View.VISIBLE);
//                chosenAddress = mList.get(0);//默认第一个
//                for (int i = 0; i < mList.size(); i++) {
//                    if (mList.get(i).getIsDefault() == 1) {//如果有设置的默认地址
//                        chosenAddress = mList.get(i);
//                    }
//                }
//                tv_browse_name.setText("联系人："+chosenAddress.getGoodReceiverName());
//                tv_browse_number.setText(chosenAddress.getGoodReceiverMobile());
//
//                String raddrStr = chosenAddress.getGoodReceiverProvinceName()
//                        + chosenAddress.getGoodReceiverCityName()
//                        + chosenAddress.getGoodReceiverCountyName()
//                        + (TextUtils.isEmpty(chosenAddress.getGoodReceiverTownName()) ? "" : chosenAddress.getGoodReceiverTownName())
//                        + chosenAddress.getGoodReceiverAddress();
//
//                tv_browse_address.setText(raddrStr);
//            }
//        }
//    };

}
