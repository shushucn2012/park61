package com.park61.moduel.shoppincart;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.OrderConfirmV3Activity;
import com.park61.moduel.shoppincart.adapter.TradeCartDeleteListAdapter;
import com.park61.moduel.shoppincart.adapter.TradeCartListAdapter;
import com.park61.moduel.shoppincart.adapter.TradeCartListAdapter.AddBuyNumLsner;
import com.park61.moduel.shoppincart.adapter.TradeCartListAdapter.ReduceBuyNumLsner;
import com.park61.moduel.shoppincart.adapter.TradeCartListAdapter.SelectBagLsner;
import com.park61.moduel.shoppincart.adapter.TradeCartListAdapter.SelectProductLsner;
import com.park61.moduel.shoppincart.bean.DeleteItem;
import com.park61.moduel.shoppincart.bean.GoodsItem;
import com.park61.moduel.shoppincart.bean.TradeCartInfo;
import com.park61.net.base.Request.Method;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class TradeCartActivity extends BaseActivity implements AddBuyNumLsner, ReduceBuyNumLsner,
        OnClickListener, OnCheckedChangeListener, SelectBagLsner, SelectProductLsner {


    private PullToRefreshListView mPullRefreshListView,mPullRefreshListViewDelete;
    private TextView tv_update, tv_total_amount, tv_save_amount;
    private Button btn_commit, btn_delete;
    private CheckBox selectall_cb,selectall_cb_delete;
    private View area_total,area_delete,area_goods;
    private ListView actualListView,actualListViewDelete;

    private TradeCartListAdapter adapter;
    private TradeCartDeleteListAdapter dAdapter;
    private ArrayList<GoodsItem> mList;
    private ArrayList<GoodsItem> dList;
    private int cartItemNum;
    private String totalAmount, totalSaveAmount;
    private int index;//记录listview第一个可见项的序号
    private int top;//记录listview第一个可见项的偏移高度
    private ArrayList<DeleteItem> deleteItems;//失效商品list

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_shoppingcart);
    }

    @Override
    public void initView() {
        tv_update = (TextView) findViewById(R.id.tv_update);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        tv_save_amount = (TextView) findViewById(R.id.tv_save_amount);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        selectall_cb = (CheckBox) findViewById(R.id.selectall_cb);
        selectall_cb_delete = (CheckBox) findViewById(R.id.selectall_cb_delete);
        area_total = findViewById(R.id.area_total);
        area_delete = findViewById(R.id.area_delete);
        area_goods = findViewById(R.id.area_goods);
        //购物车选择商品列表
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        actualListView = mPullRefreshListView.getRefreshableView();
        //购物车删除商品列表
        mPullRefreshListViewDelete = (PullToRefreshListView) findViewById(R.id.pull_refresh_list_delete);
        actualListViewDelete = mPullRefreshListViewDelete.getRefreshableView();
    }

    @Override
    public void initData() {
//        //确认订单页面传递过来的失效商品list
//        deleteItems = (ArrayList)getIntent().getParcelableArrayListExtra("deleteItems");

        mList = new ArrayList<GoodsItem>();
        adapter = new TradeCartListAdapter(mContext, mList);
        actualListView.setAdapter(adapter);
        dList = new ArrayList<GoodsItem>();
        dAdapter = new TradeCartDeleteListAdapter(mContext, dList);
        actualListViewDelete.setAdapter(dAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(CommonMethod.isListEmpty(deleteItems)){//失效商品list不为空，获取购物车列表
//            asyncGetTradeCart();
//        }else{//失效商品list不为空，先批量删除再获取购物车列表
//            asyncDeleteProductBatch();
//        }
        asyncGetTradeCart();
    }

    @Override
    public void initListener() {
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                asyncGetTradeCart();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
        tv_update.setOnClickListener(this);
        selectall_cb.setOnCheckedChangeListener(this);
        selectall_cb_delete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int checkedState = isChecked?1:0;
                for(int i=0;i<dList.size();i++){
                    dList.get(i).setIsChecked(checkedState);
                    for(int j=0 ;j<dList.get(i).getItems().size();j++){
                        dList.get(i).getItems().get(j).setIsChecked(checkedState);
                    }
                }
                dAdapter.notifyDataSetChanged();
            }
        });
        btn_commit.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        actualListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            /*** 滚动状态改变时调用*/
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 保存当前第一个可见的item的索引和偏移量
                    index = actualListView.getFirstVisiblePosition();
                    View v = actualListView.getChildAt(0);
                    top = (v == null) ? 0 : v.getTop();
                }
            }

            /*** 滚动时调用*/
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update:
                if (tv_update.getText().toString().equals("编辑")) {
                    tv_update.setText("完成");
                    area_delete.setVisibility(View.VISIBLE);
                    area_goods.setVisibility(View.GONE);
                } else {
                    tv_update.setText("编辑");
                    area_delete.setVisibility(View.GONE);
                    area_goods.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_delete:
                if (CommonMethod.isListEmpty(dAdapter.getSelectItems())) {
                    showShortToast("请勾选商品！");
                    return;
                }
                dDialog.showDialog("提示", "确定删除购物车商品吗？", "确定", "取消",
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dDialog.dismissDialog();
                                asyncDeleteProductBatch();
                            }
                        }, null);
                break;
            case R.id.btn_commit:
                if (cartItemNum > 0) {
                    Intent it = new Intent(mContext, OrderConfirmV3Activity.class);
                    it.putExtra("from", "cart");
                    startActivity(it);
                } else {
                    showShortToast("您没有勾选商品！");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 查询购物车
     */
    private void asyncGetTradeCart() {
        String wholeUrl = AppUrl.host + AppUrl.GET_TRADE_CRAT;
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
            if("0000000002".equals(errorCode)){//登录失效直接关闭
                finish();
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            mPullRefreshListView.onRefreshComplete();
            TradeCartInfo info = gson.fromJson(jsonResult.toString(),
                    TradeCartInfo.class);
            TradeCartInfo infoDelte = gson.fromJson(jsonResult.toString(),
                    TradeCartInfo.class);
            mList.clear();
            mList.addAll(info.getBags());
            adapter = new TradeCartListAdapter(mContext, mList);
            adapter.setAddBuyNumLsner(TradeCartActivity.this);
            adapter.setReduceBuyNumLsner(TradeCartActivity.this);
            adapter.setSelectBagLsner(TradeCartActivity.this);
            adapter.setSelectProductLsner(TradeCartActivity.this);
            actualListView.setAdapter(adapter);

            dList.clear();
            dList.addAll(infoDelte.getBags());
            dAdapter = new TradeCartDeleteListAdapter(mContext, dList);
            actualListViewDelete.setAdapter(dAdapter);

            String tip = "好冷清呀~您的购物车是空的~赶紧添加一个吧~";
            ViewInitTool.setListEmptyView(mContext, actualListView, tip, R.drawable.quexing, null, 100, 95);
            ViewInitTool.setListEmptyView(mContext, actualListViewDelete, tip, R.drawable.quexing, null, 100, 95);

            totalAmount = info.getTotalAmount() + "";
            totalSaveAmount = info.getTotalSaveAmount() + "";
            cartItemNum = info.getCartItemNum();
            fillData();
        }
    };

    protected void fillData() {
        //根据上次保存的index和偏移量恢复上次的位置
        actualListView.setSelectionFromTop(index, top);
        tv_total_amount.setText(FU.formatBigPrice(totalAmount));
        tv_save_amount.setText("为您节省" + FU.formatPrice(totalSaveAmount));
        btn_commit.setText("结算");
    }

    /**
     * 勾选购物车
     *
     * @param type       0-购物车 1-购物袋 2-单个商品
     * @param isChecked  0-未选中 1-选中
     * @param itemId     购物车明细id，type=2时必填
     * @param merchantId 商家id，type=1时必填
     */
    private void asyncCheckCartItems(int type, int isChecked, String itemId, long merchantId) {
        String wholeUrl = AppUrl.host + AppUrl.CHECK_CART_ITEMS;
        String requestBodyData = ParamBuild.checkCartItems(type, isChecked,
                itemId, merchantId);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                selectListener);
    }

    BaseRequestListener selectListener = new JsonRequestListener() {

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
            asyncGetTradeCart();
        }
    };

    /**
     * 批量删除购物车商品
     */
    private void asyncDeleteProductBatch() {
        String wholeUrl = AppUrl.host + AppUrl.DELETE_PRODUCT_BATCH;
        String requestBodyData;
        requestBodyData = ParamBuild.delBatchProduct(dAdapter.getSelectItems());
//        if(!CommonMethod.isListEmpty(deleteItems)){//失效商品list不为空
//            requestBodyData = ParamBuild.delBatchProduct(deleteItems);
//        }else{
//            requestBodyData = ParamBuild.delBatchProduct(dAdapter.getSelectItems());
//        }
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0, deLlistener);
    }

    BaseRequestListener deLlistener = new JsonRequestListener() {

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
//            if(CommonMethod.isListEmpty(deleteItems)) {//失效商品list为空
//                showShortToast("删除成功");
//            }
            showShortToast("删除成功");
            asyncGetTradeCart();
        }
    };

    /**
     * 更新购物车商品的数量
     */
    private void asyncUpdateProductNum(long pmInfoId, int num) {
        String wholeUrl = AppUrl.host + AppUrl.UPDATE_PRODUCT_NUM;
        String requestBodyData = ParamBuild.updateProductNum(pmInfoId, num);
        netRequest.startRequest(wholeUrl, Method.POST, requestBodyData, 0,
                updateListener);
    }

    BaseRequestListener updateListener = new JsonRequestListener() {

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
            asyncGetTradeCart();
        }
    };

    /**
     * 全选监听
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            asyncCheckCartItems(0, 1, null, 0L);
        } else {
            asyncCheckCartItems(0, 0, null, 0L);
        }
    }

    /**
     * 加号监听
     */
    @Override
    public void addBuyNum(long pmInfoId, int num) {
        asyncUpdateProductNum(pmInfoId, num);
    }

    /**
     * 减号监听
     */
    @Override
    public void reduceBuyNum(long pmInfoId, int num) {
        asyncUpdateProductNum(pmInfoId, num);
    }

    /**
     * 商品checked监听
     */
    @Override
    public void selectProduct(int position, String itemId, boolean isSelect) {
        if (isSelect) {
            asyncCheckCartItems(2, 1, itemId, 0L);
        } else {
            asyncCheckCartItems(2, 0, itemId, 0L);
        }
    }

    /**
     * 小分类checked监听
     */
    @Override
    public void selectBag(int position, long merchantId, boolean isSelect) {
        if (isSelect) {
            asyncCheckCartItems(1, 1, null, merchantId);
        } else {
            asyncCheckCartItems(1, 0, null, merchantId);
        }
    }

}
