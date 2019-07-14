package com.park61.widget;

import java.util.ArrayList;
import java.util.List;

import com.park61.R;
import com.park61.widget.dialog.DoubleDialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyRatingBar extends LinearLayout {

    private int point;// 分数默认0，最高5
    private ImageView hua1, hua2, hua3, hua4, hua5;

    public MyRatingBar(Context context) {
        super(context);
    }

    public MyRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.star_layout, this, true);
        hua1 = (ImageView) findViewById(R.id.hua1);
        hua2 = (ImageView) findViewById(R.id.hua2);
        hua3 = (ImageView) findViewById(R.id.hua3);
        hua4 = (ImageView) findViewById(R.id.hua4);
        hua5 = (ImageView) findViewById(R.id.hua5);
        hua1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (point < 1) {// 如果分数为0，点击则点亮该星
                    hua1.setImageResource(R.drawable.pingjia_focus);
                    point = 1;
                } else if (point == 1) {// 如果分数为1，代表该星显示，则置灰
                    hua1.setImageResource(R.drawable.pingjia_default);
                    point = 0;
                } else if (point > 1) {// 如果分数大于1，代表多星显示，则之后的星全部置灰
                    hua2.setImageResource(R.drawable.pingjia_default);
                    hua3.setImageResource(R.drawable.pingjia_default);
                    hua4.setImageResource(R.drawable.pingjia_default);
                    hua5.setImageResource(R.drawable.pingjia_default);
                    point = 1;
                }
            }
        });
        hua2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (point < 2) {// 如果分数为0，点击则点亮该星
                    hua1.setImageResource(R.drawable.pingjia_focus);
                    hua2.setImageResource(R.drawable.pingjia_focus);
                    point = 2;
                } else if (point == 2) {// 如果分数为2，代表该星及之前显示，则置灰该星
                    hua2.setImageResource(R.drawable.pingjia_default);
                    point = 1;
                } else if (point > 2) {// 如果分数大于2，代表多星显示，则该星之后的全部置灰
                    hua3.setImageResource(R.drawable.pingjia_default);
                    hua4.setImageResource(R.drawable.pingjia_default);
                    hua5.setImageResource(R.drawable.pingjia_default);
                    point = 2;
                }
            }
        });
        hua3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (point < 3) {// 如果分数为0，点击则点亮该星
                    hua1.setImageResource(R.drawable.pingjia_focus);
                    hua2.setImageResource(R.drawable.pingjia_focus);
                    hua3.setImageResource(R.drawable.pingjia_focus);
                    point = 3;
                } else if (point == 3) {// 如果分数为3，代表该星及之前显示，则置灰该星
                    hua3.setImageResource(R.drawable.pingjia_default);
                    point = 2;
                } else if (point > 3) {// 如果分数大于3，代表多星显示，则该星之后的全部置灰
                    hua4.setImageResource(R.drawable.pingjia_default);
                    hua5.setImageResource(R.drawable.pingjia_default);
                    point = 3;
                }
            }
        });
        hua4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (point < 4) {// 如果分数为0，点击则点亮该星
                    hua1.setImageResource(R.drawable.pingjia_focus);
                    hua2.setImageResource(R.drawable.pingjia_focus);
                    hua3.setImageResource(R.drawable.pingjia_focus);
                    hua4.setImageResource(R.drawable.pingjia_focus);
                    point = 4;
                } else if (point == 4) {// 如果分数为4，代表该星及之前显示，则置灰该星
                    hua4.setImageResource(R.drawable.pingjia_default);
                    point = 3;
                } else if (point > 4) {// 如果分数大于3，代表多星显示，则该星之后的全部置灰
                    hua5.setImageResource(R.drawable.pingjia_default);
                    point = 4;
                }
            }
        });
        hua5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (point < 5) {// 如果分数为0，点击则点亮该星
                    hua1.setImageResource(R.drawable.pingjia_focus);
                    hua2.setImageResource(R.drawable.pingjia_focus);
                    hua3.setImageResource(R.drawable.pingjia_focus);
                    hua4.setImageResource(R.drawable.pingjia_focus);
                    hua5.setImageResource(R.drawable.pingjia_focus);
                    point = 5;
                } else if (point == 5) {// 如果分数为5，代表该星及之前显示，则置灰该星
                    hua5.setImageResource(R.drawable.pingjia_default);
                    point = 4;
                }
            }
        });
    }

    /**
     * 获取评分
     */
    public int getPoint() {
        return point;
    }

    /**
     * 设置分数，并不可打分
     */
    public void setPoint(int point) {
        List<ImageView> huaList = new ArrayList<ImageView>();
        huaList.add(hua1);
        huaList.add(hua2);
        huaList.add(hua3);
        huaList.add(hua4);
        huaList.add(hua5);

        Double pointDouble = point / 2.0;

        if (pointDouble > 0 && pointDouble < 1) {
            huaList.get(0).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(1).setImageResource(R.drawable.pingjia_default);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 1 && pointDouble < 1.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_default);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 1.5 && pointDouble < 2) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 2 && pointDouble < 2.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 2.5 && pointDouble < 3) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 3 && pointDouble < 3.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 3.5 && pointDouble < 4) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 4 && pointDouble < 4.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_focus);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 4.5 && pointDouble < 5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_focus);
            huaList.get(4).setImageResource(R.drawable.pingjiabanxing);
        } else if (pointDouble == 5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_focus);
            huaList.get(4).setImageResource(R.drawable.pingjia_focus);
        }
        for (ImageView imageView : huaList) {
            imageView.setOnClickListener(null);
        }
    }

    /**
     * 设置分数，并不可打分
     */
    public void setPoint(double point) {
        List<ImageView> huaList = new ArrayList<ImageView>();
        huaList.add(hua1);
        huaList.add(hua2);
        huaList.add(hua3);
        huaList.add(hua4);
        huaList.add(hua5);

        Double pointDouble = point / 2.0;

        if (pointDouble > 0 && pointDouble < 1) {
            huaList.get(0).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(1).setImageResource(R.drawable.pingjia_default);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 1 && pointDouble < 1.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_default);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 1.5 && pointDouble < 2) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 2 && pointDouble < 2.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_default);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 2.5 && pointDouble < 3) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 3 && pointDouble < 3.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_default);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 3.5 && pointDouble < 4) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjiabanxing);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 4 && pointDouble < 4.5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_focus);
            huaList.get(4).setImageResource(R.drawable.pingjia_default);
        } else if (pointDouble >= 4.5 && pointDouble < 5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_focus);
            huaList.get(4).setImageResource(R.drawable.pingjiabanxing);
        } else if (pointDouble == 5) {
            huaList.get(0).setImageResource(R.drawable.pingjia_focus);
            huaList.get(1).setImageResource(R.drawable.pingjia_focus);
            huaList.get(2).setImageResource(R.drawable.pingjia_focus);
            huaList.get(3).setImageResource(R.drawable.pingjia_focus);
            huaList.get(4).setImageResource(R.drawable.pingjia_focus);
        }
        for (ImageView imageView : huaList) {
            imageView.setOnClickListener(null);
        }
    }
}
