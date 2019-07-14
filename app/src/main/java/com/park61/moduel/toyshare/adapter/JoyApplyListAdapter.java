package com.park61.moduel.toyshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.acts.MipcaActivityCapture;
import com.park61.moduel.pay.PayGoodsConfirmActivity;
import com.park61.moduel.toyshare.TSApplyDetailsActivity;
import com.park61.moduel.toyshare.TSMyApplyToyListActivity;
import com.park61.moduel.toyshare.TSToReturnActivity;
import com.park61.moduel.toyshare.ToyDetailsActivity;
import com.park61.moduel.toyshare.ToyShareActivity;
import com.park61.moduel.toyshare.bean.JoyApplyItem;
import com.park61.moduel.toyshare.bean.JoyShareItem;

import java.util.List;

public class JoyApplyListAdapter extends BaseAdapter {

    private List<JoyApplyItem> mList;
    private Context mContext;
    private LayoutInflater factory;
    private OnScanLsner mOnScanLsner;

    public JoyApplyListAdapter(Context _context, List<JoyApplyItem> _list) {
        this.mList = _list;
        this.mContext = _context;
        factory = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public JoyApplyItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = factory.inflate(R.layout.toyapply_list_item, null);
            holder = new ViewHolder();
            holder.actinfo_area = convertView.findViewById(R.id.actinfo_area);
            holder.img_act = (ImageView) convertView.findViewById(R.id.img_act);
            holder.tv_order_state = (TextView) convertView.findViewById(R.id.tv_order_state);
            holder.tv_act_title = (TextView) convertView.findViewById(R.id.tv_act_title);
            holder.tv_state_tip = (TextView) convertView.findViewById(R.id.tv_state_tip);
            holder.btn_scan_get = (Button) convertView.findViewById(R.id.btn_scan_get);
            holder.btn_apply_back = (Button) convertView.findViewById(R.id.btn_apply_back);
            holder.btn_apply_again = (Button) convertView.findViewById(R.id.btn_apply_again);
            holder.btn_pay = (Button) convertView.findViewById(R.id.btn_pay);
            holder.btns = new Button[4];
            holder.btns[0] = holder.btn_scan_get;
            holder.btns[1] = holder.btn_apply_back;
            holder.btns[2] = holder.btn_apply_again;
            holder.btns[3] = holder.btn_pay;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final JoyApplyItem item = mList.get(position);
        holder.tv_order_state.setText(item.getStatusName());
        ImageManager.getInstance().displayImg(holder.img_act, item.getToyShareBoxSeriesDTO().getIntroductionImg());
        holder.tv_act_title.setText(item.getToyShareBoxSeriesDTO().getName());

        if (item.getStatus().equals("1")) {//1:待领取
            holder.tv_state_tip.setText("快带你的玩具回家吧");
            showOnlyBtn(holder.btn_scan_get, holder.btns);
        } else if (item.getStatus().equals("2")) {//2:使用中
            holder.tv_state_tip.setText("已经玩了 " + item.getUseTimeString());
            showOnlyBtn(holder.btn_apply_back, holder.btns);
        } else if (item.getStatus().equals("3")) {//3:申请归还
            holder.tv_state_tip.setText("一共玩了 " + item.getUseTimeString());
            showOnlyBtn(null, holder.btns);
        } else if (item.getStatus().equals("4")) {//4:待支付
            holder.tv_state_tip.setText("一共玩了 " + item.getUseTimeString());
            showOnlyBtn(holder.btn_pay, holder.btns);
        } else if (item.getStatus().equals("5")) {//5:已完成
            holder.tv_state_tip.setText("一共玩了 " + item.getUseTimeString());
            showOnlyBtn(holder.btn_apply_again, holder.btns);
        } else if (item.getStatus().equals("-1")) {//-1已取消
            holder.tv_state_tip.setText("申请取消");
            showOnlyBtn(holder.btn_apply_again, holder.btns);
        }
        holder.actinfo_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TSApplyDetailsActivity.class);
                it.putExtra("TOY_APPLY", item);
                mContext.startActivity(it);
            }
        });
        holder.btn_scan_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnScanLsner != null) {
                    GlobalParam.CurJoy = item.getToyShareBoxSeriesDTO();
                    GlobalParam.CUR_TOYSHARE_APPLY_ID = item.getId();
                    mOnScanLsner.onScan();
                }
            }
        });
        holder.btn_apply_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, TSToReturnActivity.class);
                it.putExtra("TOY_APPLY", item);
                mContext.startActivity(it);
            }
        });
        holder.btn_apply_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, ToyDetailsActivity.class);
                it.putExtra("id", item.getToyShareBoxSeriesDTO().getId());
                CommonMethod.startOnlyNewActivity(mContext, ToyDetailsActivity.class, it);
            }
        });
        holder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, PayGoodsConfirmActivity.class);
                it.putExtra("orderId", item.getOrderId());
                TSMyApplyToyListActivity.NEED_FRESH = true;
                mContext.startActivity(it);
            }
        });
        return convertView;
    }

    private void showOnlyBtn(Button showBtn, Button[] btns) {
        if (showBtn == null) {
            for (int i = 0; i < btns.length; i++) {
                btns[i].setVisibility(View.GONE);
            }
            return;
        }
        for (int i = 0; i < btns.length; i++) {
            if (showBtn.getId() != btns[i].getId()) {
                btns[i].setVisibility(View.GONE);
            } else {
                btns[i].setVisibility(View.VISIBLE);
            }
        }
    }

    class ViewHolder {
        View actinfo_area;
        ImageView img_act;
        TextView tv_order_state;
        TextView tv_act_title;
        TextView tv_state_tip;
        Button btn_scan_get, btn_apply_back, btn_apply_again, btn_pay;
        Button[] btns;
    }

    public interface OnScanLsner {
        void onScan();
    }

    public void setLsner(OnScanLsner lsner) {
        mOnScanLsner = lsner;
    }

}
