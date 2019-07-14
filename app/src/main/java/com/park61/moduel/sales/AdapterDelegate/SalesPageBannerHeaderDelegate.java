package com.park61.moduel.sales.AdapterDelegate;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.park61.R;
import com.park61.common.set.Log;
import com.park61.moduel.acts.bean.BannerItem;
import com.park61.moduel.sales.bean.GoodsCombine;
import com.park61.widget.myrv.base.ItemViewDelegate;
import com.park61.widget.myrv.base.ViewHolder;
import com.park61.widget.viewpager.MyBanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubei on 2017/5/19.
 */

public class SalesPageBannerHeaderDelegate implements ItemViewDelegate<GoodsCombine> {

    private Context mContext;
    private MyBanner mp;

    public SalesPageBannerHeaderDelegate(Context c) {
        mContext = c;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.salestop_banner_layout;
    }

    @Override
    public boolean isForViewType(GoodsCombine item, int position) {
        Log.out("item.getTemplete().getTempleteCode()=============="+item.getTemplete().getTempleteCode()+"and position============="+position);
        return item.getTemplete().getTempleteCode().equals("banner_one");
    }

    @Override
    public void convert(ViewHolder holder, GoodsCombine item, int position) {
        ViewPager top_viewpager = holder.getView(R.id.top_viewpager);
        LinearLayout top_vp_dot = holder.getView(R.id.top_vp_dot);;
        JSONArray bannerJay = null;
        try {
            bannerJay = new JSONArray(item.getTemplete().getTempleteData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (bannerJay != null && bannerJay.length() > 0) {//如果数组不为空
            List<BannerItem> bannerlList = new ArrayList<>();
            for (int j = 0; j < bannerJay.length(); j++) {
                JSONObject bannerItemJot = bannerJay.optJSONObject(j);
                BannerItem bItem = new BannerItem();
                bItem.setImg(bannerItemJot.optString("bannerPositionPic"));
                bItem.setUrl(bannerItemJot.optString("bannerPositionWebsite"));
                bItem.setDescription(bannerItemJot.optString("name"));
                bannerlList.add(bItem);
            }
            if(mp == null) {
                mp = new MyBanner(mContext, top_viewpager, top_vp_dot);
                mp.init(bannerlList);
            }
        }
    }
}
