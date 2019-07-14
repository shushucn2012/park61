package com.park61.moduel.sales;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.login.LoginV2Activity;
import com.park61.moduel.sales.adapter.ColorGvAdapter;
import com.park61.moduel.sales.bean.ProductDetail;
import com.park61.moduel.sales.bean.SubProduct;
import com.park61.moduel.sales.fragment.FragmentGoodsComment;
import com.park61.moduel.sales.fragment.FragmentGoodsDetails;
import com.park61.moduel.sales.fragment.FragmentGoodsTop;
import com.park61.moduel.sales.fragment.UpDownPageTransformer;
import com.park61.moduel.shoppincart.TradeCartActivity;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.dialog.CanRefreshProgressDialog;
import com.park61.widget.viewpager.GoodsDetailsPagerSlidingTabStrip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsDetailsActivity extends BaseActivity {

    private ViewPager pager;
    private GoodsDetailsPagerSlidingTabStrip tabs;
    private DisplayMetrics dm;
    private GridView gv_color, gv_size;
    private TextView tv_goods_price_cover, tv_buy_num, tv_cart_num;
    private View area_choose_colorandsize, area_cart, right_img;
    private Button btn_pay, btn_add, btn_reduce, btn_to_cart, btn_confirm;
    private ImageView img_close_cover, img_goods_cover;
    public ImageView img_to_top;
    private CanRefreshProgressDialog cpDialog;

    private final String[] BUTION_NAME = {"商品", "详情", "评价"};
    private int buyNum = 1;// 购买数量
    public Long goodsId;
    public Long promotionId;
    public String goodsName;
    public String goodsPrice;
    public String goodsOldPrice;
    public ProductDetail productDetail;// 商品概要信息
    private List<SubProduct> colorList, sizeList, relationList;
    private ColorGvAdapter mColorGvAdapter, mSizeGvAdapter;
    public List<String> bannerPicUrlList;// 图片列表
    private String nextAction;// 下一步动作 tocart 去购物车；totrade 去结算；

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_goodsdetails_v2);
    }

    @Override
    public void initView() {
        dm = getResources().getDisplayMetrics();
        pager = (ViewPager) findViewById(R.id.pager);
        tabs = (GoodsDetailsPagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pager.setPageTransformer(true, null);
                    for (int i = 0; i < pager.getChildCount(); i++) {
                        pager.getChildAt(i).setAlpha(1);
                        pager.getChildAt(i).setTranslationX(0);
                        pager.getChildAt(i).setTranslationY(0);
                    }
                }
            }
        });

        cpDialog = new CanRefreshProgressDialog(mContext, new CanRefreshProgressDialog.OnRefreshClickedLsner() {

            @Override
            public void OnRefreshClicked() {
                asyncGetGoodsTopInfo(null);
            }
        });

        tv_goods_price_cover = (TextView) findViewById(R.id.tv_goods_price_cover);
        area_choose_colorandsize = findViewById(R.id.area_choose_colorandsize);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_to_cart = (Button) findViewById(R.id.btn_to_cart);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_reduce = (Button) findViewById(R.id.btn_reduce);
        img_close_cover = (ImageView) findViewById(R.id.img_close_cover);
        img_goods_cover = (ImageView) findViewById(R.id.img_goods_cover);
        gv_color = (GridView) findViewById(R.id.gv_color);
        gv_color.setFocusable(false);
        gv_size = (GridView) findViewById(R.id.gv_size);
        gv_size.setFocusable(false);
        area_choose_colorandsize = findViewById(R.id.area_choose_colorandsize);
        area_cart = findViewById(R.id.area_cart);
        tv_buy_num = (TextView) findViewById(R.id.tv_buy_num);
        tv_cart_num = (TextView) findViewById(R.id.tv_cart_num);
        right_img = findViewById(R.id.right_img);
        img_to_top = (ImageView) findViewById(R.id.img_to_top);
    }

    @Override
    public void initData() {
        goodsId = getIntent().getLongExtra("goodsId", -1l);
        promotionId = getIntent().getLongExtra("promotionId", -1l);
        goodsName = getIntent().getStringExtra("goodsName");
        goodsPrice = getIntent().getStringExtra("goodsPrice");
        goodsOldPrice = getIntent().getStringExtra("goodsOldPrice");

        tv_goods_price_cover.setText(FU.formatPrice(goodsPrice));

        colorList = new ArrayList<SubProduct>();
        mColorGvAdapter = new ColorGvAdapter(colorList, mContext);
        gv_color.setAdapter(mColorGvAdapter);

        sizeList = new ArrayList<SubProduct>();
        mSizeGvAdapter = new ColorGvAdapter(sizeList, mContext);
        gv_size.setAdapter(mSizeGvAdapter);

        relationList = new ArrayList<SubProduct>();

        asyncGetGoodsTopInfo(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalParam.userToken != null)
            asyncGetCartItemNum();
    }

    @Override
    public void initListener() {
        img_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setPageTransformer(true, new UpDownPageTransformer());
                pager.setCurrentItem(0, true);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:" + R.id.pager + ":" + 0);
                ((FragmentGoodsTop) fragment).scrollView.scrollTo(0, 0);
            }
        });
        img_close_cover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                area_choose_colorandsize.setVisibility(View.GONE);
            }
        });
        right_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String shareUrl = AppUrl.host.replace("service", "") + "getProductDetail.do?pmInfoId=" + goodsId;
                String picUrl = bannerPicUrlList.get(0);
                String title = mContext.getString(R.string.app_name);
                String description = goodsName;
                showShareDialog(shareUrl, picUrl, title, description);
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
                    Intent it = new Intent(mContext, OrderConfirmV3Activity.class);
                    it.putExtra("from", "detail");
                    it.putExtra("pmInfoId", chosenPmInfoId);
                    it.putExtra("promotionId", promotionId);
                    it.putExtra("buyNum", buyNum);
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
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:" + R.id.pager + ":" + 0);
                ((FragmentGoodsTop) fragment).banner.setCurrentImtem(position);
                ImageManager.getInstance().displayImg(img_goods_cover,bannerPicUrlList.get(position));
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
        btn_pay.setOnClickListener(new View.OnClickListener() {

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
            }
        });
        btn_to_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (GlobalParam.userToken == null) {// 没有登录则跳去登录
                    startActivity(new Intent(mContext, LoginV2Activity.class));
                    return;
                }
                if (productDetail == null) {// 防止异常
                    return;
                }
                nextAction = "tocart";
                area_choose_colorandsize.setVisibility(View.VISIBLE);
            }
        });
        area_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, TradeCartActivity.class));
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
    }

    public ViewPager getThisViewPager() {
        return pager;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return BUTION_NAME[position];
        }

        @Override
        public int getCount() {
            return BUTION_NAME.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new FragmentGoodsTop();
            } else if (position == 1) {
                fragment = new FragmentGoodsDetails();
            } else if (position == 2) {
                fragment = new FragmentGoodsComment();
            }
            return fragment;
        }
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        tabs.setFadeEnabled(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        tabs.setScrollOffset(0);
        tabs.setTabPaddingLeftRight(DevAttr.dip2px(mContext, 10));
        tabs.setDividerPadding(0);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight(1);
        tabs.setUnderlineColor(getResources().getColor(R.color.colorLineSales));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.com_orange));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setSelectedTextColor(getResources().getColor(R.color.com_orange));
        // 设置正常Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColor(getResources().getColor(R.color.g333333));
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }


    /**
     * 获取商品详情头部
     */
    public void asyncGetGoodsTopInfo(Long cityId) {
        String wholeUrl = AppUrl.host + AppUrl.GET_GOODS_TOPINFO_NEW_END;
        String requestBodyData = ParamBuild.getGoodsDetails(goodsId, promotionId, cityId);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, getGoodsTopInfoLsner);
    }

    BaseRequestListener getGoodsTopInfoLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            //showDialog();
            cpDialog.showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            //dismissDialog();
            cpDialog.showRefreshBtn();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            //dismissDialog();
            cpDialog.dialogDismiss();
            productDetail = gson.fromJson(jsonResult.toString(), ProductDetail.class);
            setDataToView();

            pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            tabs.setViewPager(pager);
            setTabsValue();
            tabs.setFadeEnabled(false);
            pager.setOffscreenPageLimit(2);
            pager.setCurrentItem(0);
        }
    };

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
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, addToCartLsner);
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
            asyncGetCartItemNum();
        }
    };

    /**
     * 获取购物车勾选的商品数量
     */
    protected void asyncGetCartItemNum() {
        String wholeUrl = AppUrl.host + AppUrl.GET_CART_ITEM_NUM;
        netRequest.startRequest(wholeUrl, Request.Method.POST, "", 0, getCartItemNumLsner);
    }

    BaseRequestListener getCartItemNumLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            int num = jsonResult.optInt("data");
            if (num == 0) {
                tv_cart_num.setVisibility(View.GONE);
            } else {
                tv_cart_num.setVisibility(View.VISIBLE);
                tv_cart_num.setText(num + "");
            }
        }
    };

    protected void setDataToView() {
        if(productDetail.getPmInfo() == null){
            showShortToast("商品已下架");
            finish();
            return;
        }
        goodsName = productDetail.getPmInfo().getProduct().getProductCname();
        if (productDetail.isBuy()) {
//            btn_to_cart.setBackgroundColor(mContext.getResources().getColor(R.color.btn_addtocart_bg));
//            btn_to_cart.setEnabled(true);
//            btn_pay.setBackgroundColor(mContext.getResources().getColor(R.color.com_orange));
//            btn_pay.setEnabled(true);
            btn_confirm.setBackgroundColor(mContext.getResources().getColor(R.color.com_orange));
            btn_confirm.setEnabled(true);
        } else {
//            btn_to_cart.setBackgroundColor(mContext.getResources().getColor(R.color.list_divider));
//            btn_to_cart.setEnabled(false);
//            btn_pay.setBackgroundColor(mContext.getResources().getColor(R.color.list_divider));
//            btn_pay.setEnabled(false);
            btn_confirm.setBackgroundColor(mContext.getResources().getColor(R.color.list_divider));
            btn_confirm.setEnabled(false);
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
            ImageManager.getInstance().displayImg(img_goods_cover, bannerPicUrlList.get(0));
        }
    }

    /**
     * 设置价格
     */
    private void setGoodsPrice(int colorPos, int sizePos) {
        if (colorPos < 0 || sizePos < 0)
            return;
        if (colorPos > colorList.size() - 1 || sizePos > sizeList.size() - 1)
            return;
        int curColorRelationIndex = colorList.get(colorPos).getColorRelationIndex();
        int curSizeRelationIndex = sizeList.get(sizePos).getSizeRelationIndex();
        for (SubProduct s : relationList) {
            if (s.getColorRelationIndex() == curColorRelationIndex
                    && s.getSizeRelationIndex() == curSizeRelationIndex) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:" + R.id.pager + ":" + 0);
                if (s.getCurrentUnifyPrice() != null) {
                    if (fragment != null) {
                        ((FragmentGoodsTop) fragment).tv_goods_price.setText(FU.formatBigPrice(s.getCurrentUnifyPrice()));
                    }
                    tv_goods_price_cover.setText(FU.formatPrice(s.getCurrentUnifyPrice()));
                }
                if (s.getMarketPrice() == null) {
                    if (s.getCurrentUnifyPrice() != null) {
                        if (fragment != null) {
                            ((FragmentGoodsTop) fragment).tv_old_price.setText(FU.formatPrice(s.getCurrentUnifyPrice()));
                        }
                    }
                } else {
                    if (fragment != null) {
                        ((FragmentGoodsTop) fragment).tv_old_price.setText(FU.formatPrice(s.getMarketPrice()));
                    }
                }
                if (s.getPromPrice() != null) {
                    if (fragment != null) {
                        ((FragmentGoodsTop) fragment).tv_goods_price.setText(FU.formatBigPrice(s.getPromPrice()));
                    }
                    tv_goods_price_cover.setText(FU.formatPrice(s.getPromPrice()));
                }
                break;
            }
        }
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

    public View.OnClickListener itemClickedLsner = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_home:
                    backToHome();
                    break;
                case R.id.view_share:
                    String shareUrl = AppUrl.host.replace("service", "") + "getProductDetail.do?pmInfoId=" + goodsId;
                    String picUrl = bannerPicUrlList.get(0);
                    String title = mContext.getString(R.string.app_name);
                    String description = goodsName;
                    ((BaseActivity) mContext).showShareDialog(shareUrl, picUrl, title, description);
                    break;
                case R.id.view_conactor:
                    break;
            }
        }
    };

    /**
     * 修改购买数量
     */
    public void updateBuyNumView() {
        tv_buy_num.setText(buyNum + "");
    }

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
}