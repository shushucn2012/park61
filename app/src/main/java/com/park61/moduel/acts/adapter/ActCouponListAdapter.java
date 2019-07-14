package com.park61.moduel.acts.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.DateTool;
import com.park61.moduel.coupon.bean.CouponUser;

import java.util.List;

public class ActCouponListAdapter extends BaseAdapter {

    private List<CouponUser> mList;
    private Context mContext;

    private List<Long> selectPositions;

    //private List<Long> selectChilds;
    //private double totalPrice;
    //private double leftPrice;
    //private double classCouponPrice;

    public ActCouponListAdapter(Context _context, List<CouponUser> _list,
                                List<Long> couponUseList, double totalPrice, double classCouponPrice) {
        this.mList = _list;
        this.mContext = _context;
        selectPositions = couponUseList;
        //selectChilds = ActCouponSelectChildCache.selectChilds;
        //Log.out("totalPrice======" + totalPrice);
        //Log.out("classCouponPrice======" + classCouponPrice);
        //this.totalPrice = totalPrice;
        //this.classCouponPrice = classCouponPrice;
        //this.leftPrice = getLeftAmount();
    }

    public void selectItem(Long couponId) {
        //Log.out("leftPrice======" + leftPrice);
        //double curCouponPrice = getCouponPriceById(couponId);
        if (!selectPositions.contains(couponId)) {
            /*if (leftPrice - curCouponPrice > 0) {
                selectPositions.add(couponId);
                leftPrice = leftPrice - curCouponPrice;
            } else if (leftPrice - curCouponPrice == 0) {
                selectPositions.add(couponId);
                leftPrice = leftPrice - curCouponPrice;
            } else if (leftPrice - curCouponPrice < 0 && leftPrice > 0) {
                selectPositions.add(couponId);
                leftPrice = 0;
            } else {
                Toast.makeText(mContext, "所选优惠券已超出总金额！", Toast.LENGTH_SHORT).show();
            }*/
            selectPositions.add(couponId);
        } else {
            selectPositions.remove(couponId);
            //leftPrice = leftPrice + curCouponPrice;
        }
        notifyDataSetChanged();
    }

    public String getSelectItems() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectPositions.size(); i++) {
            sb.append(selectPositions.get(i));
            if (i != selectPositions.size() - 1)
                sb.append(",");
        }
        return sb.toString();
    }

   /* public double getLeftAmount() {
        double mleft = totalPrice;
        Log.out("mleft======" + mleft);
        Log.out("selectPositions======" + selectPositions);
        if (CommonMethod.isListEmpty(selectPositions)) {
            return totalPrice;
        }
        for (int i = 0; i < selectPositions.size(); i++) {
            double curCouponPrice = getCouponPriceById(selectPositions.get(i));
            Log.out("curCouponPricein======" + curCouponPrice);
            mleft = mleft - curCouponPrice;
            Log.out("mleftin======" + mleft);
        }
        if (mleft < 0) {
            mleft = 0;
        }
        return mleft;
    }*/

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CouponUser getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.couponlist_ofact_item, null);
            holder = new ViewHolder();
            holder.tv_baby_name = (TextView) convertView.findViewById(R.id.tv_baby_name);
            holder.tv_coupon_price = (TextView) convertView.findViewById(R.id.tv_coupon_price);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_limit_date = (TextView) convertView.findViewById(R.id.tv_limit_date);
            holder.img_chosen = (ImageView) convertView.findViewById(R.id.img_chosen);
            holder.item_bottom_line = convertView.findViewById(R.id.item_bottom_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CouponUser item = mList.get(position);
        holder.tv_baby_name.setVisibility(View.GONE);
        holder.tv_coupon_price.setText("￥" + item.getCoupon().getRuleValue2());
        holder.tv_title.setText(Html.fromHtml(item.getCoupon().getTitle()));
        if (item.getLimitType() == 1) {
            holder.tv_limit_date.setText("有效期："
                    + DateTool.L2SEndDay(item.getCoupon().getLimitStart())
                    + " 至 "
                    + DateTool.L2SEndDay(item.getCoupon().getLimitEnd()));
        } else {
            holder.tv_limit_date.setText("不限期");
        }
        if (item.getCoupon() != null && item.getCoupon().getLimitType() == 2) {
            holder.tv_limit_date.setText("有效期：领取后" + item.getCoupon().getLimitDay() + "天");
        }
        if (item.getCoupon().getType() == 6) {
            holder.tv_coupon_price.setText("课程券");
        }
        if (selectPositions.contains(item.getId())) {
            holder.img_chosen.setImageResource(R.drawable.xuanze_focus);
        } else {
            holder.img_chosen.setImageResource(R.drawable.xuanze_default);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView img_chosen;
        TextView tv_baby_name;
        TextView tv_coupon_price;
        TextView tv_title;
        TextView tv_limit_date;
        View item_bottom_line;
    }

    /*public double getCouponPriceById(long couponId) {
        for (CouponUser coupon : mList) {
            if (coupon.getId() == couponId) {
                Log.out("coupon.getId()======" + coupon.getId());
                Log.out("couponId======" + couponId);
                if (coupon.getCoupon().getType() != 6) {
                    Log.out("couponId11111======" + couponId);
                    return coupon.getCoupon().getRuleValue2();
                } else {
                    Log.out("couponId22222======" + couponId);
                    return classCouponPrice;
                }
            }
        }
        return 0.00;
    }*/
}
