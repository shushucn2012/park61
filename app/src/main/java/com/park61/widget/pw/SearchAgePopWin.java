package com.park61.widget.pw;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.park61.R;
import com.park61.common.tool.DevAttr;
import com.park61.moduel.firsthead.adapter.TypeCateAdapter;
import com.park61.moduel.firsthead.bean.AgeCate;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询年龄
 *
 * @author super
 */
public class SearchAgePopWin extends PopupWindow {
    private View toolView;
    private ListView lv_small_cate;
    private Button btn_commit, btn_reset;
    private OnBigCateSelectLsner mOnSelectLsner;

    private Context mContext;
    private List<AgeCate> ageCateList;
    private TypeCateAdapter sadapter;
private List<Integer> value=new ArrayList<>();
    StringBuffer sb=new StringBuffer();
    public SearchAgePopWin(Context context, List<AgeCate> bList, OnBigCateSelectLsner lsner) {
        super(context);
        mContext = context;
        this.ageCateList = bList;
        this.mOnSelectLsner = lsner;
        LayoutInflater inflater = LayoutInflater.from(context);
        toolView = inflater.inflate(R.layout.pw_agefilter_layout, null);
        // 初始化视图
        lv_small_cate = (ListView) toolView.findViewById(R.id.lv_small_cate);
        btn_commit = (Button) toolView.findViewById(R.id.btn_commit);
        btn_reset = (Button) toolView.findViewById(R.id.btn_reset);

        for (int i = 0; i < ageCateList.size(); i++) {
            if(i==0){
                ageCateList.get(0).setSelected(true);
            }else {
                ageCateList.get(i).setSelected(false);
            }

        }

        sadapter = new TypeCateAdapter(mContext, ageCateList);
        lv_small_cate.setAdapter(sadapter);

        lv_small_cate.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (ageCateList.get(position).isSelected()) {
                    ageCateList.get(position).setSelected(false);
                } else {
                    if(position==0){
                        ageCateList.get(0).setSelected(true);
                        ageCateList.get(1).setSelected(false);
                        ageCateList.get(2).setSelected(false);
                        ageCateList.get(3).setSelected(false);
                        ageCateList.get(4).setSelected(false);
                    }else {
                        if(ageCateList.get(0).isSelected()){
                            ageCateList.get(0).setSelected(false);
                            ageCateList.get(position).setSelected(true);
                        }else {
                            ageCateList.get(position).setSelected(true);
                        }
                    }
                }
                sadapter.notifyDataSetChanged();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sb.length()!=0){
                    sb.delete(0,sb.length()-1);
                }
                if(ageCateList.get(0).isSelected()){
                    sb.append("");
                    sb.append(",");
                }
                if(ageCateList.get(1).isSelected()){
                    sb.append("1");
                    sb.append(",");
                }
                if(ageCateList.get(2).isSelected()){
                    sb.append("0");
                    sb.append(",");
                }
                if(ageCateList.get(3).isSelected()){
                    sb.append("2");
                    sb.append(",");
                }
                if(ageCateList.get(4).isSelected()){
                    sb.append("3");
                    sb.append(",");
                }
                String s=sb.toString();
                if(s.endsWith(",")){
                    s=s.substring(0,s.length()-1);
                }
                if(s.startsWith(",")){
                    s=s.substring(1,s.length());
                }
                mOnSelectLsner.onSelect(s);
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < ageCateList.size(); i++) {
                    ageCateList.get(i).setSelected(false);
                }
                sadapter.notifyDataSetChanged();
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(toolView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(DevAttr.getScreenWidth(mContext));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight((int) (DevAttr.getScreenHeight(mContext) * 0.4));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(null);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        toolView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
        this.setOutsideTouchable(true);
    }

    public interface OnBigCateSelectLsner {
        void onSelect(String pos);
    }

    public String getSelectedTags() {
        String s = "";
        for (int i = 0; i < ageCateList.size(); i++) {
            if (ageCateList.get(i).isSelected()) {
                s += ageCateList.get(i).getValue() + ",";
            }
        }
        if (!TextUtils.isEmpty(s)) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

}
