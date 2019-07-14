package com.park61.moduel.sales;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.FU;
import com.park61.common.tool.ViewInitTool;
import com.park61.moduel.sales.adapter.BrandListAdapter;
import com.park61.moduel.sales.adapter.GoodsListAdapter;
import com.park61.moduel.sales.adapter.SearchGoodsListAdapter;
import com.park61.moduel.sales.bean.BrandBean;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.moduel.sales.bean.ProductLimit;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城筛选界面
 */
public class SalesSrceeningActivity extends BaseActivity implements View.OnClickListener {

    private int PAGE_NUM = 1;
    private final int PAGE_SIZE = 10;

    private SearchGoodsListAdapter mAdapter;
    private GoodsListAdapter adapter;
    private BrandListAdapter brandListAdapter;
    private GridView gv_brand;
    private List<BrandBean> brandList;
    private List<BrandBean> smallbrandList;
    private List<ProductLimit> dataList;
    private List<GoodsCombine> goodsCombList;

    private boolean isStyleList = true;//是否按列表显示
    private String keyword;//搜索关键字
    private int sort;// 排序算法：1-按相关性排序，2-按销量倒序，3-按价格升序，4-按价格倒序
    private Long categoryId;// 类目id，0表示全部分类
    private String brandId;// 品牌id，""表示全部品牌，多个以,隔开
    private String priceRange;// 价格区间，""表示无，"10,100"表示10到100元，"10,"表示大于10，",100"表示小于100

    private TextView tv_zonghe, tv_xiaoliang, tv_price, tv_shaixuan, tv_keyword;
    private Button reset_btn, confirm_btn;
    private ImageView img_price_arrow, img_filter, img_down_brand, img_up_brand, img_style_two, img_style_one;
    private View area_price_brand, serach_area, area_screening, cover_searchgoods;
    private EditText edit_lowest, edit_highest;
    private PullToRefreshListView mPullToRefreshListView;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_sales_srceening);
    }

    @Override
    public void initView() {
        gv_brand = (GridView) findViewById(R.id.gv_brand);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        img_price_arrow = (ImageView) findViewById(R.id.img_price_arrow);
        img_filter = (ImageView) findViewById(R.id.img_filter);
        img_down_brand = (ImageView) findViewById(R.id.img_down_brand);
        img_up_brand = (ImageView) findViewById(R.id.img_up_brand);
        img_style_two = (ImageView) findViewById(R.id.img_style_two);
        img_style_one = (ImageView) findViewById(R.id.img_style_one);
        area_price_brand = findViewById(R.id.area_price_brand);
        cover_searchgoods = findViewById(R.id.cover_searchgoods);
        serach_area = findViewById(R.id.serach_area);
        tv_zonghe = (TextView) findViewById(R.id.tv_zonghe);
        tv_zonghe.setTextColor(getResources().getColor(R.color.com_red));
        tv_xiaoliang = (TextView) findViewById(R.id.tv_xiaoliang);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_shaixuan = (TextView) findViewById(R.id.tv_shaixuan);
        tv_keyword = (TextView) findViewById(R.id.tv_keyword);
        area_screening = findViewById(R.id.area_screening);
        edit_lowest = (EditText) findViewById(R.id.edit_lowest);
        edit_highest = (EditText) findViewById(R.id.edit_highest);

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        ViewInitTool.initPullToRefresh(mPullToRefreshListView, this);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshPage();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                PAGE_NUM++;
                asyncGoodsByCondition();
            }
        });
    }

    @Override
    public void initData() {
        keyword = getIntent().getStringExtra("KEY_WORD");
        categoryId = getIntent().getLongExtra("CATE_ID", 0);
        brandId = getIntent().getStringExtra("BRAND_ID");

        if (!TextUtils.isEmpty(keyword)) {
            tv_keyword.setText(keyword);
        }

        brandList = new ArrayList<>();
        brandListAdapter = new BrandListAdapter(mContext, brandList);
        gv_brand.setAdapter(brandListAdapter);

        goodsCombList = new ArrayList<GoodsCombine>();

        dataList = new ArrayList<>();
        mAdapter = new SearchGoodsListAdapter(mContext, dataList);
        adapter = new GoodsListAdapter(mContext, goodsCombList);
        mPullToRefreshListView.setAdapter(mAdapter);
        asyncGoodsByCondition();
    }

    @Override
    public void initListener() {
        serach_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, SalesSearchActivity.class);
                it.putExtra("KEY_WORD", keyword);
                CommonMethod.startOnlyNewActivity(mContext, SalesSearchActivity.class, it);
            }
        });
//        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent it = new Intent(mContext, GoodsDetailsActivity.class);
//                it.putExtra("goodsId", dataList.get(position - 1).getPmInfoid());
//                it.putExtra("goodsName", dataList.get(position - 1).getName());
//                it.putExtra("goodsPrice", dataList.get(position - 1).getSalesPrice() + "");
//                it.putExtra("goodsOldPrice", dataList.get(position - 1).getOldPrice() + "");
//                it.putExtra("promotionId", dataList.get(position - 1).getPromotionId());
//                mContext.startActivity(it);
//            }
//        });
        gv_brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int chosenNum = 0;
                for (int i = 0; i < brandList.size(); i++) {
                    if (brandList.get(i).isChosen()) {
                        chosenNum++;
                    }
                }

                if (brandList.get(position).isChosen()) {
                    brandList.get(position).setChosen(false);
                } else {
                    if (chosenNum >= 5) {
                        showShortToast("最多只支持5个品牌筛选");
                        return;
                    }
                    brandList.get(position).setChosen(true);
                }
                brandListAdapter.notifyDataSetChanged();
            }
        });

        tv_zonghe.setOnClickListener(this);
        tv_xiaoliang.setOnClickListener(this);
        tv_price.setOnClickListener(this);
        area_screening.setOnClickListener(this);
        cover_searchgoods.setOnClickListener(this);

        reset_btn.setOnClickListener(this);
        confirm_btn.setOnClickListener(this);

        img_down_brand.setOnClickListener(this);
        img_up_brand.setOnClickListener(this);
        img_style_two.setOnClickListener(this);
        img_style_one.setOnClickListener(this);
    }

    /**
     * 刷新页面
     */
    public void refreshPage() {
        PAGE_NUM = 1;
        asyncGoodsByCondition();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_zonghe:
                lightOneViewAndGrayOthers(tv_zonghe);
                sort = 1;
                refreshPage();
                break;
            case R.id.tv_xiaoliang:
                lightOneViewAndGrayOthers(tv_xiaoliang);
                sort = 2;
                refreshPage();
                break;
            case R.id.tv_price:
                lightOneViewAndGrayOthers(tv_price);
                if (sort == 3) {
                    sort = 4;
                    img_price_arrow.setImageResource(R.drawable.jiageshaixuan_up);
                } else {
                    sort = 3;
                    img_price_arrow.setImageResource(R.drawable.jiageshaixuan_down);
                }
                refreshPage();
                break;
            case R.id.area_screening:
                //lightOneViewAndGrayOthers(tv_shaixuan);
                if (area_price_brand.getVisibility() == View.GONE) {
                    area_price_brand.setVisibility(View.VISIBLE);
                    cover_searchgoods.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);
                } else {
                    area_price_brand.setVisibility(View.GONE);
                    cover_searchgoods.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.cover_searchgoods:
                area_price_brand.setVisibility(View.GONE);
                cover_searchgoods.setVisibility(View.GONE);
                mPullToRefreshListView.setVisibility(View.VISIBLE);
                break;
            case R.id.reset_btn:
                //重置价格区间条件
                edit_lowest.setText("");
                edit_highest.setText("");
                priceRange = "";
                //重置品牌条件
                for (int i = 0; i < brandList.size(); i++) {
                    brandList.get(i).setChosen(false);
                }
                brandListAdapter.notifyDataSetChanged();
                brandId = "";
                //展示内容
                area_price_brand.setVisibility(View.GONE);
                cover_searchgoods.setVisibility(View.GONE);
                mPullToRefreshListView.setVisibility(View.VISIBLE);
                tv_shaixuan.setTextColor(getResources().getColor(R.color.g333333));
                img_filter.setImageResource(R.drawable.screening_filter);
                refreshPage();
                break;
            case R.id.confirm_btn:
                //------如果最低价高于最高价，则把他们的位置对调 start------//
                String edit_lowest_value = edit_lowest.getText().toString().trim();
                String edit_highest_value = edit_highest.getText().toString().trim();
                if (!TextUtils.isEmpty(edit_lowest_value)
                        && !TextUtils.isEmpty(edit_highest_value)) {
                    if (FU.paseDb(edit_lowest_value) >
                            FU.paseDb(edit_highest_value)) {
                        edit_lowest.setText(edit_highest_value);
                        edit_highest.setText(edit_lowest_value);
                    }
                }
                //------如果最低价高于最高价，则把他们的位置对调 end------//
                //设置价格区间条件
                String lowPrice = edit_lowest.getText().toString().trim();
                String highPrice = edit_highest.getText().toString().trim();
                if (TextUtils.isEmpty(lowPrice) && TextUtils.isEmpty(highPrice)) {
                    priceRange = "";
                } else {
                    priceRange = lowPrice + "," + highPrice;
                }
                //设置品牌条件
                brandId = "";
                for (int i = 0; i < brandList.size(); i++) {
                    if (brandList.get(i).isChosen()) {
                        brandId += brandList.get(i).getId() + ",";
                    }
                }
                if (brandId.length() > 0) {
                    brandId = brandId.substring(0, brandId.length() - 1);
                }
                //展示内容
                area_price_brand.setVisibility(View.GONE);
                cover_searchgoods.setVisibility(View.GONE);
                mPullToRefreshListView.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(priceRange) && TextUtils.isEmpty(brandId)) {
                    tv_shaixuan.setTextColor(getResources().getColor(R.color.g333333));
                    img_filter.setImageResource(R.drawable.screening_filter);
                } else {
                    tv_shaixuan.setTextColor(getResources().getColor(R.color.com_red));
                    img_filter.setImageResource(R.drawable.screening_filter_red);
                }
                refreshPage();
                break;
            case R.id.img_down_brand:
                if (brandList.size() > 15) {
                    img_down_brand.setVisibility(View.GONE);
                    img_up_brand.setVisibility(View.VISIBLE);
                    brandListAdapter = new BrandListAdapter(mContext, brandList);
                    gv_brand.setAdapter(brandListAdapter);
                }
                break;
            case R.id.img_up_brand:
                img_down_brand.setVisibility(View.VISIBLE);
                img_up_brand.setVisibility(View.GONE);
                brandListAdapter = new BrandListAdapter(mContext, smallbrandList);
                gv_brand.setAdapter(brandListAdapter);
                break;
            case R.id.img_style_two:
                img_style_two.setVisibility(View.GONE);
                img_style_one.setVisibility(View.VISIBLE);
                isStyleList = false;
                refreshPage();
                break;
            case R.id.img_style_one:
                img_style_two.setVisibility(View.VISIBLE);
                img_style_one.setVisibility(View.GONE);
                isStyleList = true;
                refreshPage();
                break;
        }
    }

    /**
     * 获取筛选出的商品
     */
    protected void asyncGoodsByCondition() {
        String wholeUrl = AppUrl.host + AppUrl.UNIFORM_SEARCH;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword", keyword);
        map.put("sort", sort);
        map.put("brandId", brandId);
        map.put("categoryId", categoryId);
        map.put("priceRange", priceRange);
        map.put("curPage", PAGE_NUM);
        map.put("currentPage", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, lsner);
    }

    BaseRequestListener lsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
            ViewInitTool.listStopLoadView(mPullToRefreshListView);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            ViewInitTool.listStopLoadView(mPullToRefreshListView);
            ArrayList<ProductLimit> currentPageList = new ArrayList<>();
            JSONObject pageDataJot = jsonResult.optJSONObject("searchProductVoPageVO");
            JSONArray jay = pageDataJot.optJSONArray("list");
            // 第一次查询的时候没有数据，则提示没有数据，页面置空
            if (PAGE_NUM == 1 && (jay == null || jay.length() <= 0)) {
                dataList.clear();
                goodsCombList.clear();
                mAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                ViewInitTool.setListEmptyTipByDefaultPic(mContext, mPullToRefreshListView.getRefreshableView(), "暂无数据");
                ViewInitTool.setPullToRefreshViewEnd(mPullToRefreshListView);
                return;
            }
            // 首次加载清空所有项列表,并重置控件为可下滑
            if (PAGE_NUM == 1) {
                dataList.clear();
                ViewInitTool.setPullToRefreshViewBoth(mPullToRefreshListView);
            }
            // 如果当前页已经是最后一页，则列表控件置为不可下滑
            if (PAGE_NUM >= pageDataJot.optInt("totalPage")) {
                ViewInitTool.setPullToRefreshViewEnd(mPullToRefreshListView);
            }
            for (int i = 0; i < jay.length(); i++) {
                JSONObject jot = jay.optJSONObject(i);
                ProductLimit pl = new ProductLimit();
                pl.setPmInfoid(jot.optLong("pmInfoId"));
                pl.setName(jot.optString("productCname"));
                pl.setImg(jot.optString("productPicUrl"));
                try {
                    pl.setOldPrice(new java.math.BigDecimal(jot.optDouble("marketPrice")));
                    pl.setSalesPrice(new java.math.BigDecimal(jot.optDouble("currentUnifyPrice")));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                currentPageList.add(pl);
            }
            dataList.addAll(currentPageList);
            if (!isStyleList) {
                setGoodsToCombList();
                if (PAGE_NUM == 1) {
                    adapter = new GoodsListAdapter(mContext, goodsCombList);
                    mPullToRefreshListView.setAdapter(adapter);
                } else if (PAGE_NUM > 1) {
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (PAGE_NUM == 1) {
                    mAdapter = new SearchGoodsListAdapter(mContext, dataList);
                    mPullToRefreshListView.setAdapter(mAdapter);
                } else if (PAGE_NUM > 1) {
                    mAdapter.notifyDataSetChanged();
                }
            }
            if (CommonMethod.isListEmpty(brandList)) {//第一次的时候加载，之后不加载，避免不停刷新
                brandList.clear();
                JSONArray brandJay = jsonResult.optJSONArray("searchBrandVoList");
                for (int i = 0; i < brandJay.length(); i++) {
                    JSONObject brandJot = brandJay.optJSONObject(i);
                    BrandBean bb = new BrandBean();
                    bb.setId(brandJot.optLong("id"));
                    bb.setBrandName(brandJot.optString("name"));
                    brandList.add(bb);
                }
                if (brandList.size() > 15) {
                    img_down_brand.setVisibility(View.VISIBLE);
                    smallbrandList = brandList.subList(0, 15);
                    brandListAdapter = new BrandListAdapter(mContext, smallbrandList);
                } else {
                    img_down_brand.setVisibility(View.INVISIBLE);
                    brandListAdapter = new BrandListAdapter(mContext, brandList);
                }
                if (brandList.size() == 1) {
                    brandList.get(0).setChosen(true);
                }
                gv_brand.setAdapter(brandListAdapter);
            }
        }
    };

    /**
     * 筛选条件中
     * 点亮点击的按钮，置灰其他的按钮
     */
    public void lightOneViewAndGrayOthers(View clickedView) {
        if (clickedView.getId() == R.id.tv_zonghe) {
            tv_zonghe.setTextColor(getResources().getColor(R.color.com_red));
            tv_xiaoliang.setTextColor(getResources().getColor(R.color.g333333));
            tv_price.setTextColor(getResources().getColor(R.color.g333333));
            img_price_arrow.setImageResource(R.drawable.jiageshaixuan);
//            tv_shaixuan.setTextColor(getResources().getColor(R.color.g333333));
//            img_filter.setImageResource(R.drawable.screening_filter);
        } else if (clickedView.getId() == R.id.tv_xiaoliang) {
            tv_zonghe.setTextColor(getResources().getColor(R.color.g333333));
            tv_xiaoliang.setTextColor(getResources().getColor(R.color.com_red));
            tv_price.setTextColor(getResources().getColor(R.color.g333333));
            img_price_arrow.setImageResource(R.drawable.jiageshaixuan);
//            tv_shaixuan.setTextColor(getResources().getColor(R.color.g333333));
//            img_filter.setImageResource(R.drawable.screening_filter);
        } else if (clickedView.getId() == R.id.tv_price) {
            tv_zonghe.setTextColor(getResources().getColor(R.color.g333333));
            tv_xiaoliang.setTextColor(getResources().getColor(R.color.g333333));
            tv_price.setTextColor(getResources().getColor(R.color.com_red));
//            tv_shaixuan.setTextColor(getResources().getColor(R.color.g333333));
//            img_filter.setImageResource(R.drawable.screening_filter);
        } else if (clickedView.getId() == R.id.tv_shaixuan) {
            tv_zonghe.setTextColor(getResources().getColor(R.color.g333333));
            tv_xiaoliang.setTextColor(getResources().getColor(R.color.g333333));
            tv_price.setTextColor(getResources().getColor(R.color.g333333));
            img_price_arrow.setImageResource(R.drawable.jiageshaixuan);
//            tv_shaixuan.setTextColor(getResources().getColor(R.color.com_red));
//            img_filter.setImageResource(R.drawable.screening_filter_red);
        }
    }

    /**
     * 把商品列表的数据填充到商品联合bean列表
     */
    public void setGoodsToCombList() {
        goodsCombList.clear();
        for (int i = 0; i < dataList.size(); i = i + 2) {
            GoodsCombine comb = new GoodsCombine();
            if (dataList.get(i) != null)
                comb.setGoodsLeft(dataList.get(i));
            if (i + 1 < dataList.size()
                    && dataList.get(i + 1) != null)
                comb.setGoodsRight(dataList.get(i + 1));
            goodsCombList.add(comb);
        }
    }
}
