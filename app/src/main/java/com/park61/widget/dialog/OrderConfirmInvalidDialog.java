package com.park61.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.park61.R;
import com.park61.moduel.sales.adapter.InvalidGoodsListAdapter;
import com.park61.moduel.sales.bean.InvalidGoodsBean;

import java.util.ArrayList;

/**
 * 订单确认失效dialog
 */
public class OrderConfirmInvalidDialog {
    private Dialog dialog;
    private LayoutInflater inflater;
    private View view;
    private TextView tv_title;
    private ListView lv_listview;
    private Button btn_cancle,btn_add_cart;
    private InvalidGoodsListAdapter mAdapter;
    private ArrayList<InvalidGoodsBean> invalidList;

    public OrderConfirmInvalidDialog(Context context,ArrayList<InvalidGoodsBean> _list){
        this.invalidList = _list;
        dialog = new Dialog(context, R.style.dialog);
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.orderconfirm_invalid_dialog,null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        lv_listview = (ListView) view.findViewById(R.id.lv_listview);
        btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        btn_add_cart = (Button) view.findViewById(R.id.btn_add_cart);

        mAdapter = new InvalidGoodsListAdapter(context,invalidList);
        lv_listview.setAdapter(mAdapter);
        //在每次listView的adapter发生变化后，要调用setListViewHeightBasedOnChildren(listView)更新界面
        setListViewHeightBasedOnChildren(lv_listview);

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        WindowManager m = ((Activity)context).getWindowManager();
        Display d = m.getDefaultDisplay();// 获取屏幕宽、高用
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = (int) (d.getWidth()*0.8);
        dialogWindow.setAttributes(params);
    }
    public void showDialog(View.OnClickListener listenerLeft,View.OnClickListener listenerRight){
        btn_add_cart.setOnClickListener(listenerRight);
        if(listenerLeft==null){
            listenerLeft = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialog();
                }
            };
        }
        btn_cancle.setOnClickListener(listenerLeft);
        setCancelable(true);
        dialog.show();
    }
    public void dismissDialog() {
        dialog.dismiss();
    }
    public boolean isShow(){
        if(dialog.isShowing()){
            return true;
        }else{
            return false;
        }
    }
    public void setCancelable(boolean is){
        dialog.setCancelable(is);
    }

    /**
     *动态控制listView的高度
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        //获取listview的适配器
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        if(listAdapter.getCount()<2){//item数量小于2时动态listView的高度
            params.height = totalHeight
                    + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }

        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);

    }
}
