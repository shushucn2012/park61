package com.park61.moduel.firsthead;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.firsthead.adapter.SearchAboutAdapter;
import com.park61.moduel.firsthead.bean.AutoWordBean;
import com.park61.moduel.firsthead.bean.HotWords;
import com.park61.moduel.firsthead.bean.SearchHotBean;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;
import com.park61.widget.AutoLinefeedLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shubei on 2017/6/14.
 */
public class TopicSearchActivity extends BaseActivity  implements View.OnClickListener{
    private AutoLinefeedLayout view_wordwrap, view_wordwrap_history;
    private ImageView img_del;
//    private TextView tv_cancel;
   private LinearLayout ll_searchcontent,area_delete_his;
    private LinearLayout ll_spinner;
    private List<HotWords> hotList = new ArrayList<>();
    private List<HotWords> hisList = new ArrayList<>();
    private ListView spinner1;
    private TextView tv_sousuo;
    private EditText edit_sousuo;
    private ImageView iv_back;
    private TextView tv_hottitle;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_topic_search);
    }

    @Override
    public void initView() {
        tv_hottitle=(TextView) findViewById(R.id.tv_hottitle);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        img_del = (ImageView) findViewById(R.id.img_del);
        view_wordwrap = (AutoLinefeedLayout) findViewById(R.id.view_wordwrap);
        view_wordwrap_history = (AutoLinefeedLayout) findViewById(R.id.view_wordwrap_history);
        ll_searchcontent=(LinearLayout)findViewById(R.id.ll_searchcontent);
        ll_spinner=(LinearLayout)findViewById(R.id.ll_spinner);
        spinner1=(ListView)findViewById(R.id.spinner1);
        tv_sousuo=(TextView)findViewById(R.id.tv_sousuo);
        edit_sousuo = (EditText) findViewById(R.id.edit_sousuo);
        tv_sousuo.setOnClickListener(this);
        area_delete_his=(LinearLayout)findViewById(R.id.area_delete_his);
        area_delete_his.setOnClickListener(this);
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void initData() {

        asyncHotSearch();
        asyncHisSearch();

    }

    @Override
    public void initListener() {
//        tv_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        edit_sousuo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(edit_sousuo.getText().toString())) {
                    img_del.setVisibility(View.GONE);
                    ll_searchcontent.setVisibility(View.VISIBLE);
                    ll_spinner.setVisibility(View.GONE);
                } else {
                    ll_searchcontent.setVisibility(View.GONE);
                    img_del.setVisibility(View.VISIBLE);
                    ll_spinner.setVisibility(View.VISIBLE);
                    ansyncAutoWord();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });

        edit_sousuo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent it = new Intent(mContext, TopicSearchResultActivity.class);
                    String keyword = edit_sousuo.getText().toString().trim();
                    it.putExtra("keyword", keyword);
                    startActivity(it);
                }
                return false;
            }
        });
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_sousuo.setText("");
                img_del.setVisibility(View.GONE);
            }
        });
    }

    private void setHis() {
        for (int i = 0; i < hisList.size(); i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 40)));
            ll.setGravity(Gravity.CENTER);

            TextView textview = new TextView(mContext);
            String words = hisList.get(i).getKeyword();
            final String keywords = hisList.get(i).getKeyword();
            if (words.length() > 20) {
                words = words.substring(0, 20) + "...";
            }
            textview.setText(words);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textview.setTextColor(getResources().getColor(R.color.g333333));
            textview.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            textview.setPadding(DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5), DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5));
            ll.addView(textview);

            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, SearchReaultActivity.class);
                    it.putExtra("keyword", keywords);
                    startActivity(it);
//                    finish();
                }
            });
            View view = new View(mContext);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(DevAttr.dip2px(mContext, 12), DevAttr.dip2px(mContext, 12));
            view.setLayoutParams(linearParams2);
            ll.addView(view);
            view_wordwrap_history.addView(ll);
        }
    }

    private void setHot() {
        for (int i = 0; i < hotList.size(); i++) {
            LinearLayout ll = new LinearLayout(mContext);
            ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DevAttr.dip2px(mContext, 40)));
            ll.setGravity(Gravity.CENTER);

            TextView textview = new TextView(mContext);
            String words = hotList.get(i).getKeyword();
            final String keywords = hotList.get(i).getKeyword();
            if (words.length() > 20) {
                words = words.substring(0, 20) + "...";
            }
            textview.setText(words);
            textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textview.setTextColor(getResources().getColor(R.color.g333333));
            textview.setBackgroundResource(R.drawable.rec_gray_stroke_gray_solid_corner30);
            textview.setPadding(DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5), DevAttr.dip2px(mContext, 10), DevAttr.dip2px(mContext, 5));
            ll.addView(textview);

            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, SearchReaultActivity.class);
                    it.putExtra("keyword", keywords);
                    startActivity(it);
//                    finish();
                }
            });
            View view = new View(mContext);
            LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(DevAttr.dip2px(mContext, 12), DevAttr.dip2px(mContext, 12));
            view.setLayoutParams(linearParams2);
            ll.addView(view);
            view_wordwrap.addView(ll);

        }
        if(hotList.size()==0){
            view_wordwrap.setVisibility(View.GONE);
            tv_hottitle.setVisibility(View.GONE);
        }else {
            edit_sousuo.setHint(hotList.get(0).getKeyword());
            view_wordwrap.setVisibility(View.VISIBLE);
            tv_hottitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int viewid=view.getId();
        if(viewid==R.id.tv_sousuo){
            Intent intent=new Intent(TopicSearchActivity.this, SearchReaultActivity.class);
            if(TextUtils.isEmpty(edit_sousuo.getText().toString().trim())){
                if(CommonMethod.isListEmpty(hotList))//热点列表为空时不执行
                    return;
                intent.putExtra("keyword", hotList.get(0).getKeyword());
            }else {
                intent.putExtra("keyword", edit_sousuo.getText().toString().trim());
            }
            startActivity(intent);
        }else if(viewid==R.id.area_delete_his){
            asyncDeletRecod();
        }else if(viewid==R.id.iv_back){
            finish();
        }
    }
    public void asyncHotSearch(){
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.SEARCHHOT;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/search/hot";
        Map<String, Object> map = new HashMap<String, Object>();
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, hotsearchlistener);
    }
    BaseRequestListener hotsearchlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
//            if (PAGE_NUM == 1) {
            showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);

        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray dateJay = jsonResult.optJSONArray("list");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                for (int j = 0; j < dateJay.length(); j++) {
                    SearchHotBean   searchHotBean=  gson.fromJson(dateJay.optJSONObject(j).toString(), SearchHotBean.class);
                    HotWords hotWords=new HotWords();
                    hotWords.setKeyword(searchHotBean.getKeyword());
                    hotList.add(hotWords);
                }
            }
            setHot();
        }
    };


    public void asyncHisSearch(){
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.SEARCHHIS;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/search/history";
        Map<String, Object> map = new HashMap<String, Object>();
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, hissearchlistener);
    }

    BaseRequestListener hissearchlistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
//            if (PAGE_NUM == 1) {
            showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
//            ViewInitTool.listStopLoadView(mPullRefreshListView);
           // showShortToast(errorMsg);

        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            JSONArray dateJay = jsonResult.optJSONArray("list");
            if (dateJay != null && dateJay.length() > 0) {//如果数组不为空
                for (int j = 0; j < dateJay.length(); j++) {
                    SearchHotBean   searchHotBean= gson.fromJson(dateJay.optJSONObject(j).toString(), SearchHotBean.class);
                    HotWords hotWords=new HotWords();
                    hotWords.setKeyword(searchHotBean.getKeyword());
                    hisList.add(hotWords);
                }
            }
            setHis();
        }
    };

    public void asyncDeletRecod(){
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.DELSEARCHHOT;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/search/history/clear";
        Map<String, Object> map = new HashMap<String, Object>();
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, deletrecodelistener);

    }
    BaseRequestListener deletrecodelistener = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
//            if (PAGE_NUM == 1) {
            showDialog();
//            }
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
//            ViewInitTool.listStopLoadView(mPullRefreshListView);
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
//            hotList.clear();
            view_wordwrap_history.setVisibility(View.GONE);
//            view_wordwrap_history.invalidate();
            Toast.makeText(TopicSearchActivity.this,"已删除",Toast.LENGTH_LONG).show();
//            asyncHisSearch();
        }
    };
    public void ansyncAutoWord(){
        String wholeUrl = AppUrl.NEWHOST_HEAD + AppUrl.AUTOSEARCHHOT;
//        String wholeUrl = "http://192.168.100.13:8080/mockjsdata/9/service/search/autoInput";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword",edit_sousuo.getText().toString());
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, autolisenter);

    }
    BaseRequestListener autolisenter = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
//            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
//            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
//            dismissDialog();
            // 建立数据源
            final AutoWordBean autoWordBean=  gson.fromJson(jsonResult.toString(), AutoWordBean.class);
            SearchAboutAdapter adapter=new SearchAboutAdapter(TopicSearchActivity.this, autoWordBean.getRows(),edit_sousuo.getText().toString());
            spinner1.setAdapter(adapter);
            spinner1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent=new Intent(TopicSearchActivity.this, SearchReaultActivity.class);
                    intent.putExtra("keyword", autoWordBean.getRows().get(i).getKeyword());
                    startActivity(intent);
                }
            });

        }
    };
}
