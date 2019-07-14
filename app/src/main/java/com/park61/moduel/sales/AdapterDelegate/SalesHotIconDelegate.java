package com.park61.moduel.sales.AdapterDelegate;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.sales.BrandClassifyActivity;
import com.park61.moduel.sales.SalesSrceeningActivity;
import com.park61.moduel.sales.bean.BrandBean;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by shubei on 2017/5/19.
 */
public class SalesHotIconDelegate implements ItemViewDelegate<GoodsCombine>{

    @Override
    public int getItemViewLayoutId() {
        return R.layout.salespage_hoticon_layout;
    }

    @Override
    public boolean isForViewType(GoodsCombine item, int position) {
        return item.getTemplete().getTempleteCode().equals("hot_recommend");
    }

    @Override
    public void convert(ViewHolder holder, GoodsCombine item, int position) {
    }

}
