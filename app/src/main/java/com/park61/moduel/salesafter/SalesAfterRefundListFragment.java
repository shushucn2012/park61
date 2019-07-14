package com.park61.moduel.salesafter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseFragment;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.order.MyOrderActivty;
import com.park61.moduel.salesafter.adapter.TradeOrderGrfListAdapter;
import com.park61.moduel.salesafter.bean.ApplySalesAfterInfo;
import com.park61.moduel.salesafter.bean.GrfOrderInfoBean;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SalesAfterRefundListFragment extends BaseFragment {
    private int grfStatus;// 退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
    private int auditResults;// 审核结果：0、通过，1、不通过
    private int waybillType;// 退货类型：0：快递退货 1：退回到自提点
    private int isReturnGoods;// 是否寄回实物：0、寄回，1、不寄回
    private long soId;// 订单id
    private long pmInfoId;//商品id
    private long grfApplyTime;// 退货申请时间
    private long updateTime;//撤销售后申请时间
    private String closeReason;//售后关闭原因;
    private String grfCode;// 退货单编码
    private int returnGoodsNum;// 退回商品总数
    private float grfAmount;// 退货金额：商品金额+运费
    private String remark;// 退货备注
    private String grfReason;// 退货原因
    private long refundWay;// 退款方式
    private String deliveryCompanyName;// 快递公司名称
    private String waybillCode;// 快递单号
    private String storePhone;//门店电话
    private String storeAddress;//门店地址
    private String givebackPicUrl;// 图片地址
    private ArrayList<String> grfPicUrls;//图片地址集合
    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;
    private boolean isEnd;
    private ArrayList<GrfOrderInfoBean> lists;
    private PullToRefreshListView mPullRefreshListView;
    private TradeOrderGrfListAdapter mAdapter;
    private View area_error_tip;
    private ListView actualListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_myorders, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initView() {
        area_error_tip = parentView.findViewById(R.id.area_error_tip);
        mPullRefreshListView = (PullToRefreshListView) parentView.findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullRefreshListView, parentActivity);
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM = 1;
                isEnd = false;
                asyncGetGrfOrderList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isEnd) {
                    showShortToast("已经是最后一页了");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            listStopLoadView();
                        }
                    }, 500);
                    return;
                }
                PAGE_NUM++;
                asyncGetGrfOrderList();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        lists = new ArrayList<GrfOrderInfoBean>();
        mAdapter = new TradeOrderGrfListAdapter(parentActivity, lists);
        actualListView.setAdapter(mAdapter);

    }

    /**
     * 请求退货订单数据
     */
    private void asyncGetGrfOrderList() {
        area_error_tip.setVisibility(View.GONE);
        String wholeUrl = AppUrl.host + AppUrl.QUERY_GRF_LIST;
        String requestBodyData = ParamBuild.getGrforderList(PAGE_NUM, PAGE_SIZE);
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
            listStopLoadView();
            showShortToast(errorMsg);
            if (PAGE_NUM > 1) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<GrfOrderInfoBean> currentPageList = new ArrayList<GrfOrderInfoBean>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
//                showShortToast("没有符合条件的数据！");
                area_error_tip.setVisibility(View.VISIBLE);
                lists.clear();
                mAdapter.notifyDataSetChanged();
                isEnd = true;
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                lists.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= jsonResult.optInt("totalPage")) {
                isEnd = true;
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject orderJot = actJay.optJSONObject(i);
                GrfOrderInfoBean g = gson.fromJson(orderJot.toString(),
                        GrfOrderInfoBean.class);
                soId = g.getSoId();
                lists.add(g);
            }
            lists.addAll(currentPageList);
            mAdapter.notifyDataSetChanged();
        }
    };

    protected void listStopLoadView() {
        mPullRefreshListView.onRefreshComplete();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalParam.AfterSaleListNeedRefresh) {
            asyncGetGrfOrderList();
            GlobalParam.AfterSaleListNeedRefresh = false;
        }
    }

    @Override
    public void initData() {
        asyncGetGrfOrderList();
    }

    @Override
    public void initListener() {
        actualListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GrfOrderInfoBean grfOrder = lists.get(position - 1);
                soId = grfOrder.getSoId();
                pmInfoId = grfOrder.getItems().get(0).getPmInfoId();
                asyncQueryReturnApply();
            }
        });
    }

    /**
     * 退货申请查询
     */
    private void asyncQueryReturnApply() {
        String wholeUrl = AppUrl.host + AppUrl.RETURN_QUERY;
        String requestBodyData = ParamBuild.queryByIdV2(soId, pmInfoId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, queryListener);
    }

    BaseRequestListener queryListener = new JsonRequestListener() {

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
            ApplySalesAfterInfo item = gson.fromJson(jsonResult.toString(), ApplySalesAfterInfo.class);// 把Json数据解析成对象
            grfStatus = item.getGrfStatus();
            auditResults = item.getAuditResults();
            waybillType = item.getWaybillType();
            isReturnGoods = item.getIsReturnGoods();
            storePhone = item.getStorePhone();
            storeAddress = item.getStoreAddress();
            grfApplyTime = item.getGrfApplyTime();
            grfCode = item.getGrfCode();
            remark = item.getRemark();
            returnGoodsNum = item.getReturnGoodsNum();
            if (!FU.isNumEmpty(item.getActualRefundAmount() + "")) {
                grfAmount = item.getActualRefundAmount();
            } else {
                grfAmount = item.getGrfAmount();
            }
            grfReason = item.getGrfReason();
            deliveryCompanyName = item.getDeliveryCompanyName();
            waybillCode = item.getWaybillCode();
            refundWay = item.getRefundWay();
            grfPicUrls = new ArrayList<String>();
            updateTime = item.getUpdateTime();
            closeReason = item.getCloseReason();
            for (int i = 0; i < item.getPics().size(); i++) {
                givebackPicUrl = item.getPics().get(i).getGivebackPicUrl();
                grfPicUrls.add(givebackPicUrl);
            }
            // 退款状态: 0、申请退货，1、退货审核，2、提交货品，3、货品入库，4、系统打款，5、订单关闭
            if (grfStatus == 0) {//0、申请退货,等待官方审核的界面
                Intent intent1 = new Intent(parentActivity, AuditStatuActivity.class);
                intent1.putExtra("grfApplyTime", grfApplyTime);
                intent1.putExtra("grfCode", grfCode);
                intent1.putExtra("remark", remark);
                intent1.putExtra("returnGoodsNum", returnGoodsNum);
                intent1.putExtra("grfAmount", grfAmount);
                intent1.putExtra("grfReason", grfReason);
                intent1.putExtra("grfPicUrls", grfPicUrls);
                startActivity(intent1);
            } else if (grfStatus == 1) {//1、退货审核，官方已审核等待商品寄出
                Intent intent2 = new Intent(parentActivity, WaitForSendGoodsActivity.class);
                intent2.putExtra("grfId", item.getItems().get(0).getGrfId());
                intent2.putExtra("pmInfoId", pmInfoId);
                intent2.putExtra("soId", soId);
                intent2.putExtra("auditResults", auditResults);
                intent2.putExtra("grfApplyTime", grfApplyTime);
                intent2.putExtra("grfCode", grfCode);
                intent2.putExtra("remark", remark);
                intent2.putExtra("returnGoodsNum", returnGoodsNum);
                intent2.putExtra("grfAmount", grfAmount);
                intent2.putExtra("grfReason", grfReason);
                intent2.putExtra("grfPicUrls", grfPicUrls);
                intent2.putExtra("waybillType", waybillType);
                intent2.putExtra("storePhone", storePhone);
                intent2.putExtra("storeAddress", storeAddress);
                startActivity(intent2);
            } else if (grfStatus == 2) {//2、提交货品,商品已寄出等待商家签收
                Intent intent3 = new Intent(parentActivity, WaitForMerchanSignActivity.class);
                intent3.putExtra("auditResults", auditResults);
                intent3.putExtra("grfApplyTime", grfApplyTime);
                intent3.putExtra("grfCode", grfCode);
                intent3.putExtra("remark", remark);
                intent3.putExtra("returnGoodsNum", returnGoodsNum);
                intent3.putExtra("grfAmount", grfAmount);
                intent3.putExtra("grfReason", grfReason);
                intent3.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent3.putExtra("waybillCode", waybillCode);
                intent3.putExtra("grfPicUrls", grfPicUrls);
                intent3.putExtra("waybillType", waybillType);
                intent3.putExtra("storePhone", storePhone);
                intent3.putExtra("storeAddress", storeAddress);
                startActivity(intent3);
            } else if (grfStatus == 3) {//3、货品入库,商家已签收等待付款
                Intent intent4 = new Intent(parentActivity, MerchantSignActivity.class);
                intent4.putExtra("isReturnGoods", isReturnGoods);
                intent4.putExtra("grfApplyTime", grfApplyTime);
                intent4.putExtra("grfCode", grfCode);
                intent4.putExtra("remark", remark);
                intent4.putExtra("returnGoodsNum", returnGoodsNum);
                intent4.putExtra("grfAmount", grfAmount);
                intent4.putExtra("grfReason", grfReason);
                intent4.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent4.putExtra("waybillCode", waybillCode);
                intent4.putExtra("grfPicUrls", grfPicUrls);
                intent4.putExtra("waybillType", waybillType);
                intent4.putExtra("storePhone", storePhone);
                intent4.putExtra("storeAddress", storeAddress);
                startActivity(intent4);
            } else if (grfStatus == 4) {//4、系统打款
                Intent intent5 = new Intent(parentActivity, RefundFinishActivity.class);
                intent5.putExtra("grfAmount", grfAmount);
                intent5.putExtra("refundWay", refundWay);
                startActivity(intent5);
            } else if (grfStatus == 5) {//5、订单关闭
                Intent intent = new Intent(parentActivity, SalesClosedActivity.class);
                intent.putExtra("updateTime", updateTime);
                intent.putExtra("closeReason", closeReason);
                intent.putExtra("isReturnGoods", isReturnGoods);
                intent.putExtra("grfApplyTime", grfApplyTime);
                intent.putExtra("grfCode", grfCode);
                intent.putExtra("remark", remark);
                intent.putExtra("returnGoodsNum", returnGoodsNum);
                intent.putExtra("grfAmount", grfAmount);
                intent.putExtra("grfReason", grfReason);
                intent.putExtra("deliveryCompanyName", deliveryCompanyName);
                intent.putExtra("waybillCode", waybillCode);
                intent.putExtra("grfPicUrls", grfPicUrls);
                startActivity(intent);
            }
        }
    };

    @Override
    public void showShortToast(String text) {
        if (5 == ((MyOrderActivty) parentActivity).getCurPaperIndex() + 1) {
            super.showShortToast(text);
        }
    }

    @Override
    public void showDialog() {
        if (5 == ((MyOrderActivty) parentActivity).getCurPaperIndex() + 1) {
            super.showDialog();
        }
    }

}
