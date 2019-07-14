package com.park61.moduel.firsthead;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.moduel.firsthead.adapter.FirstHeadAdapter;
import com.park61.moduel.firsthead.bean.AgeCate;
import com.park61.moduel.firsthead.bean.BigCate;
import com.park61.moduel.firsthead.bean.FirstHeadBean;
import com.park61.moduel.firsthead.bean.MediaBean;
import com.park61.moduel.firsthead.bean.SearchImpointBean;
import com.park61.moduel.firsthead.bean.SearchImpointContentBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.pw.SearchAgePopWin;
import com.park61.widget.pw.SearchOrderPopWin;
import com.park61.widget.pw.SearchTypePopWin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nieyu on 2017/11/13.
 */

public class SearchReaultActivity extends BaseActivity implements View.OnClickListener {

    private int PAGE_NUM = 0;// 页码
    private final int PAGE_SIZE = 10;// 每页条数

    private TextView tv_type, tv_age, tv_order, tv_sousuo, edit_sousuo;
    private View area_type, area_age, area_order;
    private SearchTypePopWin mSearchTypePopWin;
    private SearchOrderPopWin mSearchOrderPopWin;
    private SearchAgePopWin mSearchAgePopWin;
    private ImageView img_type_xiala, img_age_xiala, img_order_xiala, iv_back, iv_empty;
    private FirstHeadAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private LRecyclerView rv_firsthead;

    private String keyword;
    private long level1CateId = -1;
    private String level2CateId = "";
    private String adapterAges, sortType;
    private int esPageIndex = 1;
    private List<AgeCate> oList = new ArrayList<AgeCate>();
    private List<BigCate> bList = new ArrayList<BigCate>();
    private List<AgeCate> aList = new ArrayList<AgeCate>();
    private List<FirstHeadBean> mlsit = new ArrayList<>();;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_searchresult);
    }

    @Override
    public void initView() {
        iv_empty = (ImageView) findViewById(R.id.iv_empty);
        area_type = findViewById(R.id.area_type);
        area_age = findViewById(R.id.area_age);
        area_order = findViewById(R.id.area_order);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_order = (TextView) findViewById(R.id.tv_order);
        img_type_xiala = (ImageView) findViewById(R.id.img_type_xiala);
        img_age_xiala = (ImageView) findViewById(R.id.img_age_xiala);
        img_order_xiala = (ImageView) findViewById(R.id.img_order_xiala);
        rv_firsthead = (LRecyclerView) findViewById(R.id.rv_firsthead);
        rv_firsthead.setLayoutManager(new LinearLayoutManager(this));
        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);
        edit_sousuo = (TextView) findViewById(R.id.edit_sousuo);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    @Override
    public void initData() {
        int level = getIntent().getIntExtra("level", -1);
        if (level > -1) {
            if (level == 1) {
                level1CateId = getIntent().getIntExtra("id", -1);
            } else if (level == 2) {
                level2CateId = getIntent().getIntExtra("id", -1) + "";
            }
        }
        keyword = getIntent().getStringExtra("keyword");
        edit_sousuo.setText(keyword);
        adapter = new FirstHeadAdapter(SearchReaultActivity.this, mlsit);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rv_firsthead.setAdapter(mLRecyclerViewAdapter);
        asyncImpointData();
    }

    @Override
    public void initListener() {
        area_type.setOnClickListener(this);
        area_order.setOnClickListener(this);
        area_age.setOnClickListener(this);
        edit_sousuo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rv_firsthead.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGE_NUM = 0;
                esPageIndex = 1;
                asyncImpointData();
            }
        });
        rv_firsthead.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                PAGE_NUM++;
                asyncImpointData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.area_age) {
            if (mSearchTypePopWin == null) {
                asyncGetBigType();
            } else {
                showSearchTypePW();
            }
        } else if (view.getId() == R.id.area_type) {
            if (mSearchAgePopWin == null) {
                asyncGetAgeType();
            } else {
                showSearchAgePW();
            }
        } else if (view.getId() == R.id.area_order) {
            if (mSearchOrderPopWin == null) {
                asyncGetOrderType();
            } else {
                showSearchOrderPW();
            }
        } else if (view.getId() == R.id.iv_back) {
            finish();
        } else if (view.getId() == R.id.edit_sousuo) {
            finish();
        }
    }

    /**
     * 请求搜索数据
     */
    public void asyncImpointData() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.IMPORTSEARCHHOT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword", keyword);
        if (level1CateId != -1) {
            esPageIndex = 1;
            //PAGE_NUM = 0;
            map.put("level1CateId", level1CateId);
        }
        if (level2CateId != "") {
            esPageIndex = 1;
            //PAGE_NUM = 0;
            map.put("level2CateIds", level2CateId);
        }
        if (!TextUtils.isEmpty(adapterAges)) {
            esPageIndex = 1;
            //PAGE_NUM = 0;
            map.put("contentTypes", adapterAges);
        }
        if (!TextUtils.isEmpty(sortType)) {
            esPageIndex = 1;
            //PAGE_NUM = 0;
            map.put("sortType", sortType);
        }
        map.put("pageIndex", PAGE_NUM);
        map.put("pageSize", PAGE_SIZE);
        map.put("esPageIndex", esPageIndex);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, firstimpointlistenter);
    }

    BaseRequestListener firstimpointlistenter = new JsonRequestListener() {
        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            rv_firsthead.refreshComplete(PAGE_SIZE);
            showShortToast(errorMsg);
            if (PAGE_NUM > 0) {// 如果是加载更多，失败时回退页码
                PAGE_NUM--;
            }
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            rv_firsthead.refreshComplete(PAGE_SIZE);
            if (PAGE_NUM == 0) {
                mlsit.clear();
                rv_firsthead.setNoMore(false);
            }
            if (PAGE_NUM >= jsonResult.optInt("pageCount") - 1) {
                rv_firsthead.setNoMore(true);
            }
            SearchImpointBean searchImpointBean = gson.fromJson(jsonResult.toString(), SearchImpointBean.class);
            if (searchImpointBean != null && searchImpointBean.getRows() != null) {
                for (int i = 0; i < searchImpointBean.getRows().size(); i++) {
                    List<MediaBean> mediaBeenlist = new ArrayList<>();
                    if (searchImpointBean != null && searchImpointBean.getRows() != null) {
                        FirstHeadBean firstHeadBean = new FirstHeadBean();
                        SearchImpointContentBean searchImpointContentBean = searchImpointBean.getRows().get(i);
                        firstHeadBean.setComposeType(searchImpointContentBean.getCoverType());
                        firstHeadBean.setContentType(searchImpointContentBean.getContentType());
                        firstHeadBean.setIssuerName(searchImpointContentBean.getAuthorName());
                        firstHeadBean.setCommentNum(searchImpointContentBean.getCommentNum());
                        firstHeadBean.setItemReadNum(searchImpointContentBean.getViewNum());
                        firstHeadBean.setItemTitle(searchImpointContentBean.getTitle());
                        if (0 == searchImpointContentBean.getCoverType()) {
                            MediaBean mediaBean = new MediaBean();
                            mediaBean.setMediaUrl(searchImpointContentBean.getCoverImg1());
                            mediaBeenlist.add(mediaBean);
                            firstHeadBean.setComposeType(2);
                        } else if (1 == searchImpointContentBean.getCoverType()) {
                            MediaBean mediaBean = new MediaBean();
                            mediaBean.setMediaUrl(searchImpointContentBean.getCoverImg1());
                            mediaBeenlist.add(mediaBean);
                            firstHeadBean.setComposeType(1);
                        } else if (2 == searchImpointContentBean.getCoverType()) {
                            firstHeadBean.setComposeType(3);
                            MediaBean mediaBean1 = new MediaBean();
                            mediaBean1.setMediaUrl(searchImpointContentBean.getCoverImg1());
                            mediaBeenlist.add(mediaBean1);

                            MediaBean mediaBean2 = new MediaBean();
                            mediaBean2.setMediaUrl(searchImpointContentBean.getCoverImg2());
                            mediaBeenlist.add(mediaBean2);

                            MediaBean mediaBean3 = new MediaBean();
                            mediaBean3.setMediaUrl(searchImpointContentBean.getCoverImg3());
                            mediaBeenlist.add(mediaBean3);
                        }
                        firstHeadBean.setClassifyType(searchImpointContentBean.getContentType());
                        firstHeadBean.setItemMediaList(mediaBeenlist);
                        firstHeadBean.setViewNum(searchImpointContentBean.getViewNum());
                        firstHeadBean.setItemId((long) searchImpointContentBean.getId());
                        firstHeadBean.setItemtype("search");
                        firstHeadBean.setItemCommentNum(searchImpointContentBean.getCommentNum());
                        mlsit.add(firstHeadBean);
                    }
                }
                if (PAGE_NUM == 0 && searchImpointBean.getRows().size() == 0) {
                    rv_firsthead.setVisibility(View.GONE);
                    iv_empty.setVisibility(View.VISIBLE);
                } else {
                    iv_empty.setVisibility(View.GONE);
                    rv_firsthead.setVisibility(View.VISIBLE);
                }
                esPageIndex = searchImpointBean.getEsPageIndex();
                adapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 获取排序类型
     */
    public void asyncGetOrderType() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.ORDERSEARCHHOT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "park_seach_type");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, ordertypelisenter);
    }

    BaseRequestListener ordertypelisenter = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("list");
            oList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                oList.add(gson.fromJson(actJay.optJSONObject(i).toString(), AgeCate.class));
            }
            mSearchOrderPopWin = new SearchOrderPopWin(mContext, oList, 0, new SearchOrderPopWin.OnBigCateSelectLsner() {
                @Override
                public void onSelect(int pos) {
                    mSearchOrderPopWin.dismiss();
                    sortType = oList.get(pos).getValue();
                    tv_order.setText(oList.get(pos).getLabel());
                    PAGE_NUM = 0;
                    esPageIndex = 1;
                    asyncImpointData();
                }
            });

            mSearchOrderPopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);

            findViewById(R.id.cover).setVisibility(View.VISIBLE);
            tv_order.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
            img_order_xiala.setImageResource(R.drawable.icon_xiangshang);

            mSearchOrderPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    findViewById(R.id.cover).setVisibility(View.GONE);
                    tv_order.setTextColor(mContext.getResources().getColor(R.color.g333333));
                    img_order_xiala.setImageResource(R.drawable.xiala);
                }
            });
        }
    };

    /**
     * 请求大类
     */
    public void asyncGetBigType() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.CATEGORYSEARCHHOT;
        Map<String, Object> map = new HashMap<String, Object>();
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, shaixuanlisenter);
    }

    BaseRequestListener shaixuanlisenter = new JsonRequestListener() {

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
            JSONArray actJay = jsonResult.optJSONArray("list");
            bList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                bList.add(gson.fromJson(actJay.optJSONObject(i).toString(), BigCate.class));
            }
            mSearchTypePopWin = new SearchTypePopWin(mContext, bList, 0, new SearchTypePopWin.OnBigCateSelectLsner() {
                @Override
                public void onSelect(int pos) {
                    PAGE_NUM = 0;
                    esPageIndex = 1;
                    esPageIndex = 1;
                    mSearchTypePopWin.initSmallCateList(bList.get(pos).getListContentCategory(), 0);
                }

                @Override
                public void lever2(int p) {
                    esPageIndex = 1;
                }
            });
            mSearchTypePopWin.initSmallCateList(bList.get(0).getListContentCategory(), 0);
            mSearchTypePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 0);
            tv_age.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
            img_age_xiala.setImageResource(R.drawable.icon_xiangshang);
            mSearchTypePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    esPageIndex = 1;
                    findViewById(R.id.cover).setVisibility(View.GONE);
                    tv_age.setTextColor(mContext.getResources().getColor(R.color.g333333));
                    img_age_xiala.setImageResource(R.drawable.xiala);
                }
            });
            mSearchTypePopWin.getBtnConfirm().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    esPageIndex = 1;
                    mSearchTypePopWin.dismiss();
                    level1CateId = mSearchTypePopWin.getSelectedBigCate();
                    level2CateId = mSearchTypePopWin.getSelectedTags();
                    asyncImpointData();
                }
            });
        }
    };

    /**
     * 请求内容类型 图文 话题 视频 专题 音频
     */
    public void asyncGetAgeType() {
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.ORDERSEARCHHOT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("needAll", true);
        map.put("type", "park_content_type");
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, typelisenter);
    }

    BaseRequestListener typelisenter = new JsonRequestListener() {
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
            JSONArray actJay = jsonResult.optJSONArray("list");
            aList.clear();
            for (int i = 0; i < actJay.length(); i++) {
                aList.add(gson.fromJson(actJay.optJSONObject(i).toString(), AgeCate.class));
            }
            mSearchAgePopWin = new SearchAgePopWin(mContext, aList, new SearchAgePopWin.OnBigCateSelectLsner() {
                @Override
                public void onSelect(String pos) {
                    mSearchAgePopWin.dismiss();
                    adapterAges = pos;
                    PAGE_NUM = 0;
                    esPageIndex = 1;
                    asyncImpointData();
                }
            });
            mSearchAgePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);
            findViewById(R.id.cover).setVisibility(View.VISIBLE);
            tv_type.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
            img_type_xiala.setImageResource(R.drawable.icon_xiangshang);
            mSearchAgePopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    esPageIndex = 1;
                    findViewById(R.id.cover).setVisibility(View.GONE);
                    tv_type.setTextColor(mContext.getResources().getColor(R.color.g333333));
                    img_type_xiala.setImageResource(R.drawable.xiala);
                }
            });
        }
    };

    public void showSearchOrderPW() {
        mSearchOrderPopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);
        findViewById(R.id.cover).setVisibility(View.VISIBLE);
        tv_order.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        img_order_xiala.setImageResource(R.drawable.icon_xiangshang);
    }

    public void showSearchTypePW() {
        mSearchTypePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 0);
        findViewById(R.id.cover).setVisibility(View.VISIBLE);
        tv_age.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        img_age_xiala.setImageResource(R.drawable.icon_xiangshang);
    }

    public void showSearchAgePW() {
        mSearchAgePopWin.showAsDropDown(findViewById(R.id.search_bar), 0, 1);
        findViewById(R.id.cover).setVisibility(View.VISIBLE);
        tv_type.setTextColor(mContext.getResources().getColor(R.color.color_text_red_deep));
        img_type_xiala.setImageResource(R.drawable.icon_xiangshang);
    }

}
