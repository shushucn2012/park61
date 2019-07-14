package com.park61.moduel.grouppurchase;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DateTool;
import com.park61.common.tool.FU;
import com.park61.common.tool.HtmlParserTool;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.acts.StoreTasteActivity;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.sales.BrandShowActivity;
import com.park61.moduel.sales.GoodsDetailsActivity;
import com.park61.moduel.sales.OrderConfirmV3Activity;
import com.park61.moduel.sales.adapter.ColorGvAdapter;
import com.park61.moduel.sales.adapter.GoodsDetailsComtFiller;
import com.park61.moduel.sales.bean.Product;
import com.park61.moduel.sales.bean.ProductDetail;
import com.park61.moduel.sales.bean.ProductEvaluate;
import com.park61.moduel.sales.bean.SubProduct;
import com.park61.moduel.shoppincart.TradeCartActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.pw.FightGroupDetailPopWin;
import com.park61.widget.pw.GoodsDetailsDeAddrPopWin;
import com.park61.widget.pw.GoodsPromotionsPopWin;
import com.park61.widget.textview.GroupTimeTextView;
import com.park61.widget.viewpager.GoodsBanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼团商品详情页面
 */
public class GroupPurchaseGoodsDetailActivity extends BaseActivity implements GroupTimeTextView.OnTimeEnd {
    private int PAGE_NUM = 1;// 评论列表页码
    private final int PAGE_SIZE = 5;// 评论列表每页条数
    private boolean isEnd;// 评论列表是否到最后一页了
    public boolean needRefreshComts = true;// 是否需要刷新评论数据，避免切换时刷新
    private float y;
    private int buyNum = 1;// 购买数量
    private ColorGvAdapter mColorGvAdapter, mSizeGvAdapter;
    private String nextAction;// 下一步动作 tocart 去购物车；totrade 去结算；

    private RadioGroup mtabGroup;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ObservableScrollView scrollView;
    private View stopView, area_choose_colorandsize, area_choose_deaddr, right_img, area_taste, area_mj, line,area_brand,
            area_currentprice, area_fightgroup_declare, area_share, area_oldprice, area_msg,area_bye_num;
    private View[] stickArray = new View[3];
    private ObservableScrollView.Callbacks mCallbacks;
    private LinearLayout details_content;
    private LinearLayout comt_content;
    private LinearLayout ask_content;
    private GridView gv_color, gv_size;
    private LinearLayout linear_comment;
    private Button btn_add, btn_reduce, btn_confirm;
    private TextView tv_goods_name, tv_currentprice, tv_oldsprice,
            tv_original_num_price, tv_current_num_price, tv_goods_price_cover,
            tv_old_price, tv_speed_point, tv_server_point, tv_brand, tv_mj_title, tv_title, tv_save_amount,
            tv_description_point, tv_buy_num, tv_deaddr, tv_ishas, tv_num, tv_description;
    private ImageView img_main_product, img_close_cover,
            img_goods_cover;
    private WebView webview, webview_problem;
    private GoodsBanner banner;// 轮播图控件
    private GoodsDetailsDeAddrPopWin mGoodsDetailsDeAddrPopWin;
    private GoodsPromotionsPopWin mGoodsPromotionsPopWin;
    private FightGroupDetailPopWin mFightGroupDetailPopWin;

    private Long goodsId;
    private Long promotionId;
    private ProductDetail productDetail;// 商品概要信息
    private Product productWebData;// 商品文描信息
    private List<SubProduct> colorList, sizeList, relationList;
    private ProductEvaluate point;// 商品评分信息
    private List<ProductEvaluate> comtList;// 评价列表
    private List<String> bannerPicUrlList;// 图片列表
    private String goodsName;
    private String goodsPrice;
    private String goodsOldPrice;
    private String isFightGroup;
    private Long fightGroupOpenId;
    private String currentUnifyPrice;
    private static int type;
    private String url;
    private View area_countdown;
    private GroupTimeTextView tv_countdown;
    private String countDown;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_grouppurchase_goodsdetail);
    }

    @Override
    public void initView() {
        initActTabs();
        // 初始化上下拉刷新控件
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        ViewInitTool.initPullToRefresh(mPullRefreshScrollView, mContext);
        // 初始化滑动黏贴控件
        scrollView = (ObservableScrollView) mPullRefreshScrollView.getRefreshableView();
        initObservableScrollView();

        stopView = findViewById(R.id.stopView);
        area_fightgroup_declare = findViewById(R.id.area_fightgroup_declare);
        details_content = (LinearLayout) findViewById(R.id.details_content);
        comt_content = (LinearLayout) findViewById(R.id.comt_content);
        ask_content = (LinearLayout) findViewById(R.id.ask_content);

        gv_color = (GridView) findViewById(R.id.gv_color);
        gv_color.setFocusable(false);
        gv_size = (GridView) findViewById(R.id.gv_size);
        gv_size.setFocusable(false);

        linear_comment = (LinearLayout) findViewById(R.id.linear_comment);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);

        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
        tv_goods_price_cover = (TextView) findViewById(R.id.tv_goods_price_cover);
        tv_old_price = (TextView) findViewById(R.id.tv_old_price);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_currentprice = (TextView) findViewById(R.id.tv_currentprice);
        tv_oldsprice = (TextView) findViewById(R.id.tv_oldsprice);
        tv_original_num_price = (TextView) findViewById(R.id.tv_original_num_price);
        tv_current_num_price = (TextView) findViewById(R.id.tv_current_num_price);
        area_msg = findViewById(R.id.area_msg);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_save_amount = (TextView) findViewById(R.id.tv_save_amount);

        tv_speed_point = (TextView) findViewById(R.id.tv_speed_point);
        tv_server_point = (TextView) findViewById(R.id.tv_server_point);
        tv_description_point = (TextView) findViewById(R.id.tv_description_point);
        img_main_product = (ImageView) findViewById(R.id.img_main_product);
        img_close_cover = (ImageView) findViewById(R.id.img_close_cover);
        img_goods_cover = (ImageView) findViewById(R.id.img_goods_cover);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_reduce = (Button) findViewById(R.id.btn_reduce);
        tv_buy_num = (TextView) findViewById(R.id.tv_buy_num);
        area_oldprice = findViewById(R.id.area_oldprice);

        area_countdown = findViewById(R.id.area_countdown);
        tv_countdown = (GroupTimeTextView) findViewById(R.id.tv_countdown);

        webview = (WebView) findViewById(R.id.webview);
        webview_problem = (WebView) findViewById(R.id.webview_problem);

        area_choose_colorandsize = findViewById(R.id.area_choose_colorandsize);
        area_choose_deaddr = findViewById(R.id.area_choose_deaddr);
        tv_deaddr = (TextView) findViewById(R.id.tv_deaddr);
        tv_ishas = (TextView) findViewById(R.id.tv_ishas);
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        right_img = findViewById(R.id.right_img);
        area_taste = findViewById(R.id.area_taste);
        area_mj = findViewById(R.id.area_mj);
        tv_mj_title = (TextView) findViewById(R.id.tv_mj_title);
        line = findViewById(R.id.line);
        area_currentprice = findViewById(R.id.area_currentprice);
        area_share = findViewById(R.id.area_share);
        area_bye_num = findViewById(R.id.area_bye_num);
        area_brand = findViewById(R.id.area_brand);
        initTopBanner();
    }

    /**
     * 初始化轮播图
     */
    private void initTopBanner() {
        ViewPager vp = (ViewPager) findViewById(R.id.top_viewpager);
        LinearLayout ll = (LinearLayout) findViewById(R.id.top_vp_dot);
        banner = new GoodsBanner(mContext, vp, ll);
    }

    /**
     * 初始化标签
     */
    private void initActTabs() {
        mtabGroup = (RadioGroup) findViewById(R.id.mtab_group);
        mtabGroup.check(R.id.rb_goodsdetails);
        mtabGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                y = scrollView.getScrollY();
                int checkedIndex = 0;
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (checkedId == group.getChildAt(i).getId()) {
                        checkedIndex = i;
                    }
                }
                showStick(checkedIndex);
                swithPage(checkedIndex);
            }
        });
        stickArray[0] = findViewById(R.id.stick0);
        stickArray[1] = findViewById(R.id.stick1);
        stickArray[2] = findViewById(R.id.stick2);
        stickArray[0].setVisibility(View.VISIBLE);
    }

    /**
     * 切换详情和游戏评价
     */
    public void swithPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                details_content.setVisibility(View.VISIBLE);
                comt_content.setVisibility(View.GONE);
                ask_content.setVisibility(View.GONE);
                scrollView.scrollTo(0, (int) y);
                mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                break;
            case 1:
                details_content.setVisibility(View.GONE);
                comt_content.setVisibility(View.VISIBLE);
                ask_content.setVisibility(View.GONE);
                mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                ViewInitTool.initPullToRefresh(mPullRefreshScrollView, mContext);
                initComtList();
                break;
            case 2:
                details_content.setVisibility(View.GONE);
                comt_content.setVisibility(View.GONE);
                ask_content.setVisibility(View.VISIBLE);
                webview_problem.loadUrl(" file:///android_asset/problem.html ");
                webview_problem.setVisibility(View.VISIBLE);
                mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.DISABLED);
                break;
        }
    }

    /**
     * 变化标签组下方红杠
     */
    private void showStick(int which) {
        stickArray[which].setVisibility(View.VISIBLE);
        for (int i = 0; i < stickArray.length; i++) {
            if (i != which) {
                stickArray[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 初始化黏贴滑动控件
     */
    public void initObservableScrollView() {
        mCallbacks = new ObservableScrollView.Callbacks() {

            @Override
            public void onUpOrCancelMotionEvent() {
            }

            @Override
            public void onScrollChanged(int scrollY) {
                ((RelativeLayout) GroupPurchaseGoodsDetailActivity.this
                        .findViewById(R.id.stickyView)).setTranslationY(Math
                        .max(stopView.getTop(), scrollY));
            }

            @Override
            public void onDownMotionEvent() {
            }
        };
        ViewInitTool.initObservableScrollView(scrollView, mCallbacks);
    }


    @Override
    public void initData() {
        fightGroupOpenId = getIntent().getLongExtra("opneId", -1l);
        goodsId = getIntent().getLongExtra("goodsId", -1l);
        logout("=====拼团商品详情页面======" + goodsId);
        promotionId = getIntent().getLongExtra("promotionId", -1l);
        goodsName = getIntent().getStringExtra("goodsName");
        tv_goods_name.setText(goodsName);
        goodsPrice = getIntent().getStringExtra("goodsPrice");
        goodsOldPrice = getIntent().getStringExtra("goodsOldPrice");
        tv_old_price.setText("￥" + goodsOldPrice);
        String personNum = getIntent().getStringExtra("personNum");
        colorList = new ArrayList<SubProduct>();
        mColorGvAdapter = new ColorGvAdapter(colorList, mContext);
        gv_color.setAdapter(mColorGvAdapter);

        sizeList = new ArrayList<SubProduct>();
        mSizeGvAdapter = new ColorGvAdapter(sizeList, mContext);
        gv_size.setAdapter(mSizeGvAdapter);

        relationList = new ArrayList<SubProduct>();

        // 初始化评论列表
        comtList = new ArrayList<ProductEvaluate>();

        asyncActStoreTaste();
        asyncGetGoodsTopInfo(null);
        asyncGetGoodsWebData();
        asyncGetGoodsPoints();
    }

    @Override
    public void initListener() {
        tv_countdown.setOnTimeEndLsner(this);
        area_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,BrandShowActivity.class);
                intent.putExtra("brandTitle",productDetail.getProductBrand().getBrandName());
                intent.putExtra("productBrandId",productDetail.getProductBrand().getId());
                startActivity(intent);
            }
        });
        area_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                if (productDetail == null) {// 防止异常
                    return;
                }
                nextAction = "totrade";
                area_choose_colorandsize.setVisibility(View.VISIBLE);
            }
        });
        area_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareUrl = String.format(AppUrl.host+url, goodsId + "");
                logout("=======shareUrl====="+AppUrl.host+url);
                String picUrl = productDetail.getPmInfo().getProduct().getProductPicUrl();
                String title = getString(R.string.app_name);
                String description = productDetail.getPmInfo().getProduct().getProductCname() + "\n拼团商品详情";
                showShareDialog(shareUrl, picUrl, title, description);
            }
        });
        area_fightgroup_declare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, FightGroupDescriptionActivity.class);
                startActivity(it);
            }
        });
        area_taste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StoreTasteActivity.class);
                intent.putExtra("pmInfoId", goodsId);
                startActivity(intent);
            }
        });
        right_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                mNaviShopPopWin = new NaviShopPopWin(mContext, itemClickedLsner, null, true);
//                int offX = DevAttr.dip2px(mContext, 12);
//                int offY = DevAttr.dip2px(mContext, -18);
//                mNaviShopPopWin.showAsDropDown(right_img, offX, offY);
                String shareUrl = AppUrl.host.replace("service", "") + "getProductDetail.do?pmInfoId=" + goodsId;
                String picUrl = bannerPicUrlList.get(0);
                String title = mContext.getString(R.string.app_name);
                String description = goodsName;
                showShareDialog(shareUrl, picUrl, title, description);
            }
        });
        img_close_cover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                area_choose_colorandsize.setVisibility(View.GONE);
            }
        });
        area_choose_deaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoodsDetailsDeAddrPopWin = new GoodsDetailsDeAddrPopWin(mContext, new GoodsDetailsDeAddrPopWin.OnShopSelectLsner() {
                    @Override
                    public void onShopSelect(Long id, String name) {
                        //tv_deaddr.setText(name);
                        asyncGetGoodsTopInfo(id);
                        mGoodsDetailsDeAddrPopWin.dismiss();
                    }
                }, findViewById(R.id.cover));
                mGoodsDetailsDeAddrPopWin.showAtLocation(
                        findViewById(R.id.root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!productDetail.isBuy()) {
                    showShortToast("您未选中任何商品！");
                    return;
                }
                SubProduct chosenPm = null;
                Long chosenPmInfoId = -1l;
                if (productDetail.getPmInfo().getProduct().getProductType() != 0) {
                    if ((mColorGvAdapter.getCount() > 0 || mSizeGvAdapter.getCount() > 0)
                            && (mColorGvAdapter.getSelectedPos() > -1 && mSizeGvAdapter.getSelectedPos() > -1)) {
                        chosenPm = getSelectedPminfoId();
                    }
                    if (chosenPm == null) {
                        showShortToast("您未选中任何商品！");
                        return;
                    }
                    chosenPmInfoId = chosenPm.getPmInfoId();
                } else {
                    chosenPmInfoId = productDetail.getPmInfo().getId();
                }
                chosenPm = getSelectedPminfoId();
                if (getSelectedPminfoId() != null && buyNum > chosenPm.getAvailableStock()) {
                    showShortToast("库存不足！");
                    return;
                }

                if (chosenPm != null && chosenPm.getPromId() != null) {
                    promotionId = chosenPm.getPromId();
                }

                if (nextAction.equals("tocart")) {
                    asyncAddToCart(chosenPmInfoId, promotionId, buyNum);
                } else {
                    area_choose_colorandsize.setVisibility(View.GONE);
//                    Intent it = new Intent(mContext, OrderConfirmV2Activity.class);
                    Intent it = new Intent(mContext, OrderConfirmV3Activity.class);
                    it.putExtra("from", "detail");
                    it.putExtra("pmInfoId", chosenPmInfoId);
                    it.putExtra("promotionId", promotionId);
                    it.putExtra("buyNum", buyNum);
                    it.putExtra("fightGroupOpenId", fightGroupOpenId);
                    if (productDetail.getFightGroup() == null) {
                        isFightGroup = "false";
                    } else {
                        isFightGroup = "true";
                    }
                    it.putExtra("isFightGroup", isFightGroup);
                    startActivity(it);
                }
            }
        });
        gv_color.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (getNoCanChooseList(colorList).contains(position)) {
                    return;
                }
                mColorGvAdapter.selectItem(position);
                int currColorIndex = colorList.get(position).getColorRelationIndex();

                // 将可选的尺码显示可选
                List<Integer> canChooseSizeIndexList = getCanChooseSizeIndexList(currColorIndex);
                for (SubProduct subProduct : sizeList) {
                    if (canChooseSizeIndexList.contains(subProduct.getSizeRelationIndex())) {
                        subProduct.setFlag(true);
                    } else {
                        subProduct.setFlag(false);
                    }
                }
                mSizeGvAdapter.notifyDataSetChanged();

                int sPos = getFirstCanChooseSizePos();
                mSizeGvAdapter.selectItem(sPos);
                // 列表数据变了，所以价格也要变
                setGoodsPrice(position, sPos);
                banner.setCurrentImtem(position);
                ImageManager.getInstance().displayImg(img_goods_cover, bannerPicUrlList.get(position));
            }
        });
        gv_size.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (getNoCanChooseList(sizeList).contains(position)) {
                    return;
                }
                mSizeGvAdapter.selectItem(position);
                setGoodsPrice(mColorGvAdapter.getSelectedPos(), position);
            }
        });
        area_currentprice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                if (productDetail == null) {// 防止异常
                    return;
                }
                nextAction = "totrade";
                area_choose_colorandsize.setVisibility(View.VISIBLE);
                area_bye_num.setVisibility(View.GONE);
            }
        });
        area_oldprice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                if (productDetail == null) {// 防止异常
                    return;
                }
//                nextAction = "tocart";
//                area_choose_colorandsize.setVisibility(View.VISIBLE);
                Intent it = new Intent(mContext,GoodsDetailsActivity.class);
                it.putExtra("goodsId", goodsId);
                it.putExtra("promotionId", promotionId);
                it.putExtra("goodsName", goodsName);
                it.putExtra("goodsOldPrice", goodsOldPrice);
                it.putExtra("goodsPrice", goodsPrice);
                startActivity(it);
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buyNum < 200) {
                    buyNum++;
                    updateBuyNumView();
                } else {
                    showShortToast("每人购买数量不超过200件");
                }
            }
        });
        btn_reduce.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (buyNum > 1) {
                    buyNum--;
                    updateBuyNumView();
                }
            }
        });
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (isEnd) {
                    showShortToast("已经是最后一页了");
                    mPullRefreshScrollView.onRefreshComplete();
                } else {
                    PAGE_NUM++;
                    asyncGetGoodsComts();
                }
            }
        });
        area_mj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoodsPromotionsPopWin = new GoodsPromotionsPopWin(mContext,
                        findViewById(R.id.cover), productDetail.getDiscountActivity());
                mGoodsPromotionsPopWin.showAtLocation(
                        findViewById(R.id.root), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0, 0); //设置layout在PopupWindow中显示的位置
            }
        });
    }

    /**
     * 加入购物车
     */
    protected void asyncAddToCart(long pmInfoId, long promotionId, int num) {
        String wholeUrl = AppUrl.host + AppUrl.TRADE_CART_ADD_PRODUCT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmInfoId", pmInfoId);
        if (promotionId != 0)
            map.put("promotionId", promotionId);
        map.put("num", num);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                addToCartLsner);
    }

    BaseRequestListener addToCartLsner = new JsonRequestListener() {

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
            showShortToast("成功加入购物车！");
            area_choose_colorandsize.setVisibility(View.GONE);
        }
    };

    /**
     * 获取不可点击的颜色或者尺码位置列表
     */
    public List<Integer> getNoCanChooseList(List<SubProduct> sList) {
        List<Integer> canList = new ArrayList<Integer>();
        for (int i = 0; i < sList.size(); i++) {
            if (!sList.get(i).isFlag()) {
                canList.add(i);
            }
        }
        return canList;
    }

    /**
     * 修改购买数量
     */
    public void updateBuyNumView() {
        tv_buy_num.setText(buyNum + "");
    }

    public View.OnClickListener itemClickedLsner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_home:
                    backToHome();
                    break;
                case R.id.view_cart:
                    Intent it = new Intent(mContext, TradeCartActivity.class);
                    startActivity(it);
                    break;
                case R.id.view_conactor:
                    break;
            }
        }
    };

    private void initComtList() {
        // 获取评论首页数据
        if (needRefreshComts) {
            PAGE_NUM = 1;
            isEnd = false;
            asyncGetGoodsComts();
            // 刷新后置否
            needRefreshComts = false;
        } else {
            scrollView.scrollTo(0, (int) y);
        }
    }

    /**
     * 到店体验游戏列表
     */
    private void asyncActStoreTaste() {
        String wholeUrl = AppUrl.host + AppUrl.ACT_STATE;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pmId", goodsId.toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, listener);
    }

    BaseRequestListener listener = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            JSONArray actJay = jsonResult.optJSONArray("list");
            if (actJay == null || actJay.length() < 1) {
                area_taste.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            } else {
                area_taste.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 获取商品详情头部
     */
    protected void asyncGetGoodsTopInfo(Long cityId) {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_TOPINFO_NEW_END;
        String requestBodyData = ParamBuild.getGoodsDetails(goodsId,
                promotionId, cityId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                getGoodsTopInfoLsner);
    }

    BaseRequestListener getGoodsTopInfoLsner = new JsonRequestListener() {

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
            productDetail = gson.fromJson(jsonResult.toString(), ProductDetail.class);
            if(productDetail.getFightGroup()!=null){
                setDataToView();
            }else{
                showShortToast("拼团已结束");
                finish();
            }
        }
    };

    protected void setDataToView() {
        try {
//            tv_goods_name.setText(productDetail.getPmInfo().getProduct().getProductCname());
//            tv_currentprice.setText("￥"+productDetail.getPmPrice().getCurrentUnifyPrice());
//            tv_old_price.setText("￥"+productDetail.getPmPrice().getMarketPrice());
            tv_old_price.setText("￥" + goodsOldPrice);
            tv_currentprice.setText(FU.formatPrice(productDetail.getFightGroup().getFightGroupPrice()));
            tv_current_num_price.setText(FU.formatBigPrice(productDetail.getFightGroup().getFightGroupPrice())+"/1件");
            tv_num.setText(productDetail.getFightGroup().getPersonNum() + "人拼团");
            tv_title.setText(productDetail.getFightGroup().getPersonNum() + "人拼团");
            currentUnifyPrice = productDetail.getPmPrice().getCurrentUnifyPrice() + "";
            tv_oldsprice.setText(FU.formatPrice(productDetail.getPmPrice().getCurrentUnifyPrice()));
            tv_original_num_price.setText(FU.formatBigPrice(productDetail.getPmPrice().getCurrentUnifyPrice())+"/1件");

            url = productDetail.getFightGroup().getUrl();
            logout("=======url======="+url);
            tv_save_amount.setText("再省￥" + productDetail.getFightGroup().getSaveMoney() + "元");
            int personNum = productDetail.getFightGroup().getPersonNum();
            String sendTime = productDetail.getFightGroup().getSendTime();
            int num = personNum -1;
            tv_description.setText("支付开团并邀请" + num + "人参团，将于" +
                    DateTool.L2SEndDay(sendTime) + "日发货，拼团人数不足自动退款");
            long time = productDetail.getFightGroup().getInvalidTime()-System.currentTimeMillis();
            countDown = CommonMethod.formatMss(time);
            logout("========countDown========"+countDown);
            if(countDown.equals("0:00:00:00")){
                area_countdown.setVisibility(View.GONE);
                tv_countdown.setVisibility(View.GONE);
            }else{
                String[] ss = countDown.split(":");
                String d = ss[0];
                String h = ss[1];
                String m = ss[2];
                String s = ss[3];
                if (d.substring(0, 1).equals("0")) {
                    d = h.substring(1);
                }
                if (h.substring(0, 1).equals("0")) {
                    h = h.substring(1);
                }
                if (m.substring(0, 1).equals("0")) {
                    m = m.substring(1);
                }
                if (s.substring(0, 1).equals("0")) {
                    s = s.substring(1);
                }
                tv_countdown.setTimes(new long[]{Long.parseLong(d), Long.parseLong(h), Long.parseLong(m), Long.parseLong(s)});
                if (!tv_countdown.isRun()) {
                    tv_countdown.run();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (productDetail.getProductBrand() != null) {// 品牌数据
            ImageManager.getInstance().displayImg(img_main_product, productDetail.getProductBrand().getBrandLogoUrl());
            tv_brand.setText(productDetail.getProductBrand().getBrandName());
        }
        if (productDetail.getProductDeliverRegionVo() != null) {
            tv_deaddr.setText(productDetail.getProductDeliverRegionVo().getShowDeliverRegionName());
        }
        if (productDetail.isBuy()) {
            tv_ishas.setText("现货");
            area_oldprice.setBackgroundColor(mContext.getResources().getColor(R.color.sallow_red));
            area_oldprice.setEnabled(true);
            if (productDetail.getFightGroup() == null) {
                tv_description.setVisibility(View.GONE);
//                area_msg.setVisibility(View.VISIBLE);
                area_currentprice.setEnabled(false);
                area_currentprice.setBackgroundColor(mContext.getResources().getColor(R.color.text_red_alpha));
            } else {
//                area_msg.setVisibility(View.GONE);
                area_currentprice.setEnabled(true);
                area_currentprice.setBackgroundColor(mContext.getResources().getColor(R.color.com_orange));
            }
        } else {
            tv_ishas.setText("无货");
            area_currentprice.setBackgroundColor(mContext.getResources().getColor(R.color.text_red_alpha));
            area_currentprice.setEnabled(false);
            area_oldprice.setBackgroundColor(mContext.getResources().getColor(R.color.sallow_red_alpha));
            area_oldprice.setEnabled(false);
        }
        if (!CommonMethod.isListEmpty(productDetail.getColorProductList())) {
            colorList.clear();
            colorList.addAll(productDetail.getColorProductList());
            mColorGvAdapter.notifyDataSetChanged();
        }
        if (!CommonMethod.isListEmpty(productDetail.getSizeProductList())) {
            sizeList.clear();
            sizeList.addAll(productDetail.getSizeProductList());
            mSizeGvAdapter.notifyDataSetChanged();
        }
        if (!CommonMethod.isListEmpty(productDetail.getRelationProductList())) {
            relationList.clear();
            relationList.addAll(productDetail.getRelationProductList());
        }
        // 将可选的颜色显示可选
        List<Integer> canChooseColorIndexList = getCanChooseColorIndexList();
        for (SubProduct subProduct : colorList) {
            if (canChooseColorIndexList.contains(subProduct.getColorRelationIndex())) {
                subProduct.setFlag(true);
            } else {
                subProduct.setFlag(false);
            }
        }
        mColorGvAdapter.notifyDataSetChanged();
        // 选中第一个可选的颜色
        int firstCanChooseColorPos = getFirstCanChooseColorPos();
        mColorGvAdapter.selectItem(firstCanChooseColorPos);
        if (firstCanChooseColorPos > -1) {// 有可选颜色时才去设置可选尺寸
            int currColorIndex = colorList.get(firstCanChooseColorPos).getColorRelationIndex();
            // 将可选的尺码显示可选
            List<Integer> canChooseSizeIndexList = getCanChooseSizeIndexList(currColorIndex);
            for (SubProduct subProduct : sizeList) {
                if (canChooseSizeIndexList.contains(subProduct.getSizeRelationIndex())) {
                    subProduct.setFlag(true);
                } else {
                    subProduct.setFlag(false);
                }
            }
            mSizeGvAdapter.notifyDataSetChanged();
        }
        // 选中第一个可选的尺码
        int firstCanChooseSizePos = getFirstCanChooseSizePos();
        mSizeGvAdapter.selectItem(firstCanChooseSizePos);
        setGoodsPrice(firstCanChooseColorPos, firstCanChooseSizePos);
        // 初始化banner图片
        bannerPicUrlList = new ArrayList<String>();
        if (productDetail.getPmInfo().getProduct().getProductType() == 0) {
            String mUrl = productDetail.getPmInfo().getProduct().getProductPicUrl();
            if (!TextUtils.isEmpty(mUrl)) {
                bannerPicUrlList.add(mUrl);
            }
        }
        for (SubProduct sp : colorList) {
            if (!TextUtils.isEmpty(sp.getPicUrl())) {
                bannerPicUrlList.add(sp.getPicUrl());
            }
        }
        removeDuplicateUrl(bannerPicUrlList);
        if (bannerPicUrlList.size() > 0) {
            banner.init(bannerPicUrlList);
            ImageManager.getInstance().displayImg(img_goods_cover, bannerPicUrlList.get(0));
        }

        //显示促销信息
        if (!CommonMethod.isListEmpty(productDetail.getDiscountActivity())) {
            tv_mj_title.setText(productDetail.getDiscountActivity().get(0).getTitle());
            area_mj.setVisibility(View.VISIBLE);
        } else {
            area_mj.setVisibility(View.GONE);
        }
    }

    /**
     * 设置统一价格
     */
    private void setGoodsPrice(int colorPos, int sizePos) {
        if (colorPos < 0 || sizePos < 0)
            return;
        if (colorPos > colorList.size() - 1 || sizePos > sizeList.size() - 1)
            return;
        int curColorRelationIndex = colorList.get(colorPos)
                .getColorRelationIndex();
        int curSizeRelationIndex = sizeList.get(sizePos).getSizeRelationIndex();
        for (SubProduct s : relationList) {
            if (s.getColorRelationIndex() == curColorRelationIndex
                    && s.getSizeRelationIndex() == curSizeRelationIndex) {
                if (s.getCurrentUnifyPrice() != null) {
//                    tv_currentprice.setText("￥" + s.getCurrentUnifyPrice());
//                    tv_goods_price_cover.setText("￥" + s.getCurrentUnifyPrice());
                    tv_currentprice.setText(FU.formatPrice(productDetail.getFightGroup().getFightGroupPrice()));
                    tv_goods_price_cover.setText(FU.formatPrice(productDetail.getFightGroup().getFightGroupPrice()));
                }
                if (s.getMarketPrice() == null) {
                    if (s.getCurrentUnifyPrice() != null) {
                        tv_old_price.setText("￥" + s.getCurrentUnifyPrice());
                    }
                } else {
                    tv_old_price.setText("￥" + s.getMarketPrice());
                }
                if (s.getPromPrice() != null) {
                    tv_currentprice.setText(FU.formatPrice(s.getPromPrice()));
                    tv_goods_price_cover.setText(FU.formatPrice(s.getPromPrice()));
                }
                break;
            }
        }
    }

    /**
     * 删除url列表中重复项
     */
    public static void removeDuplicateUrl(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
    }

    /**
     * 获取可用颜色序号集合
     */
    public List<Integer> getCanChooseColorIndexList() {
        List<Integer> indexList = new ArrayList<Integer>();
        for (SubProduct subProduct : relationList) {
            if (subProduct.isFlag())
                indexList.add(subProduct.getColorRelationIndex());
        }
        return indexList;
    }

    /**
     * 获取颜色对应的可用尺码序号集合
     */
    public List<Integer> getCanChooseSizeIndexList(int currColorIndex) {
        List<Integer> indexList = new ArrayList<Integer>();
        for (SubProduct subProduct : relationList) {
            if (currColorIndex == subProduct.getColorRelationIndex()) {
                indexList.add(subProduct.getSizeRelationIndex());
            }
        }
        return indexList;
    }

    /**
     * 获取第一个可选的颜色
     */
    public int getFirstCanChooseColorPos() {
        for (int i = 0; i < colorList.size(); i++) {
            if (colorList.get(i).isFlag()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取第一个可选的尺码
     */
    public int getFirstCanChooseSizePos() {
        for (int i = 0; i < sizeList.size(); i++) {
            if (sizeList.get(i).isFlag()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从关系表中取选择的商品id
     */
    public SubProduct getSelectedPminfoId() {
        int selectColorPos = mColorGvAdapter.getSelectedPos();
        int selectSizePos = mSizeGvAdapter.getSelectedPos();
        if (selectColorPos < 0 || selectSizePos < 0)
            return null;
        int curColorRelationIndex = colorList.get(selectColorPos).getColorRelationIndex();
        int curSizeRelationIndex = sizeList.get(selectSizePos).getSizeRelationIndex();
        for (SubProduct s : relationList) {
            if (s.getColorRelationIndex() == curColorRelationIndex
                    && s.getSizeRelationIndex() == curSizeRelationIndex) {
                return s;
            }
        }
        return null;
    }

    /**
     * 获取商品评分
     */
    protected void asyncGetGoodsPoints() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_POINTS;
        String requestBodyData = ParamBuild.getGoodsDetails(goodsId, null, null);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                getGoodsPointsLsner);
    }

    BaseRequestListener getGoodsPointsLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            point = gson.fromJson(jsonResult.toString(), ProductEvaluate.class);
            setPoints();
        }
    };

    protected void setPoints() {
        tv_description_point.setText(point.getConformityScore() + "");
        tv_server_point.setText(point.getServeScore() + "");
        tv_speed_point.setText(point.getDeliveryScore() + "");
    }

    /**
     * 获取商品web详情
     */
    protected void asyncGetGoodsWebData() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_DETAILS_END;
        String requestBodyData = ParamBuild.getGoodsDetails(goodsId, null, null);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0,
                getGoodsWebDataLsner);
    }

    BaseRequestListener getGoodsWebDataLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            productWebData = gson.fromJson(jsonResult.toString(), Product.class);
            setWebData();
        }
    };

    private void setWebData() {
        String htmlDetailsStr = "";
        try {
            htmlDetailsStr = HtmlParserTool.replaceImgStr(productWebData.getProductDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
        webview.loadDataWithBaseURL(null, htmlDetailsStr, "text/html", "utf-8", null);
    }

    /**
     * 获取商品评价列表
     */
    protected void asyncGetGoodsComts() {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_COMMENTS_END;
        String requestBodyData = ParamBuild.getGoodsComts(goodsId, PAGE_NUM, PAGE_SIZE);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, comtslistener);
    }

    BaseRequestListener comtslistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            listStopLoadView();
            showShortToast(errorMsg);
            if(PAGE_NUM > 1){//如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            listStopLoadView();
            ArrayList<ProductEvaluate> currentPageList = new ArrayList<ProductEvaluate>();
            JSONArray actJay = jsonResult.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (actJay == null || actJay.length() <= 0)) {
                showShortToast("没有评论数据！");
                comtList.clear();
                isEnd = true;
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                comtList.clear();
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM == jsonResult.optInt("totalPage")) {
                isEnd = true;
            }
            for (int i = 0; i < actJay.length(); i++) {
                JSONObject actJot = actJay.optJSONObject(i);
                ProductEvaluate p = gson.fromJson(actJot.toString(),
                        ProductEvaluate.class);
                currentPageList.add(p);
            }
            comtList.addAll(currentPageList);
            GoodsDetailsComtFiller.buildListInLinear(linear_comment, comtList, mContext);
        }
    };

    protected void listStopLoadView() {
        mPullRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onEnd() {
        area_countdown.setVisibility(View.GONE);
        tv_countdown.setVisibility(View.GONE);
        GlobalParam.GroupPurchaseActivityNeedRefresh = true;
        showShortToast("拼团结束");
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        tv_countdown.setRun(false);
        tv_countdown.setOnTimeEndLsner(null);
    }
}
