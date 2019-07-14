package com.park61.widget.rvheader;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.park61.R;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.dreamhouse.DreamDetailActivity;
import com.park61.moduel.dreamhouse.DreamHouseMainActivity;
import com.park61.moduel.dreamhouse.bean.DreamItemInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * RecyclerView的HeaderView，简单的展示HotHeader
 */
public class ShopDateHeader extends RelativeLayout {

    private Context mContext;
    private View root;
    private DisplayImageOptions options;
    private boolean isShowMore;

    private ImageView img_area0;
    private ImageView img_area1;
    private ImageView img_area2;
    private ImageView img_area3;
    private TextView tv_title0;
    private TextView tv_title1;
    private TextView tv_title2;
    private TextView tv_title3;

    public ShopDateHeader(Context context, boolean isShowMore) {
        super(context);
        this.isShowMore = isShowMore;
        this.mContext = context;
        init(context);
    }

    public ShopDateHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShopDateHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(final Context context) {
        root = inflate(context, R.layout.shop_main_date_layout, this);
        LinearLayout area_more = (LinearLayout) root.findViewById(R.id.area_more);
        img_area0 = (ImageView) root.findViewById(R.id.img_area0);
        img_area1 = (ImageView) root.findViewById(R.id.img_area1);
        img_area2 = (ImageView) root.findViewById(R.id.img_area2);
        img_area3 = (ImageView) root.findViewById(R.id.img_area3);
        tv_title0 = (TextView) root.findViewById(R.id.tv_title0);
        tv_title1 = (TextView) root.findViewById(R.id.tv_title1);
        tv_title2 = (TextView) root.findViewById(R.id.tv_title2);
        tv_title3 = (TextView) root.findViewById(R.id.tv_title3);

        if (isShowMore) {
            area_more.setVisibility(VISIBLE);
        } else {
            area_more.setVisibility(GONE);
        }

        area_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, DreamHouseMainActivity.class));
            }
        });
//        String url0 = "http://ghpprod.oss-cn-qingdao.aliyuncs.com/activity/20160914202610290_524.png";
//        ImageManager.getInstance().displayImg(img_area0, url0, R.drawable.img_default_h);
//
//        String url1 = "http://ghpprod.oss-cn-qingdao.aliyuncs.com/activity/20160912155505395_879.jpg";
//        ImageManager.getInstance().displayImg(img_area1, url1, R.drawable.img_default_h);
//
//        String url2 = "http://ghpprod.oss-cn-qingdao.aliyuncs.com/activity/20161207114910403_285.jpg";
//        ImageManager.getInstance().displayImg(img_area2, url2, R.drawable.img_default_h);
//
//        String url3 = "http://ghpprod.oss-cn-qingdao.aliyuncs.com/activity/20161224092715624_63.jpg";
//        ImageManager.getInstance().displayImg(img_area3, url3, R.drawable.img_default_h);
    }

    public void refreshData(final List<DreamItemInfo> dList) {
        tv_title0.setText(dList.get(0).getTitle());
        ImageManager.getInstance().displayImg(img_area0, dList.get(0).getCoverPic(), R.drawable.img_default_h);
        tv_title1.setText(dList.get(1).getTitle());
        ImageManager.getInstance().displayImg(img_area1, dList.get(1).getCoverPic(), R.drawable.img_default_h);
        if (dList.size() > 2) {
            tv_title2.setText(dList.get(2).getTitle());
            ImageManager.getInstance().displayImg(img_area2, dList.get(2).getCoverPic(), R.drawable.img_default_h);
        }
        if (dList.size() > 3) {
            tv_title3.setText(dList.get(3).getTitle());
            ImageManager.getInstance().displayImg(img_area3, dList.get(3).getCoverPic(), R.drawable.img_default_h);
        }

        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                long requirementId = 0;
                switch (v.getId()) {
                    case R.id.img_area0:
                        requirementId = dList.get(0).getId();
                        break;
                    case R.id.img_area1:
                        requirementId = dList.get(1).getId();
                        break;
                    case R.id.img_area2:
                        if (dList.size() > 2) {
                            requirementId = dList.get(2).getId();
                        }
                        break;
                    case R.id.img_area3:
                        if (dList.size() > 3) {
                            requirementId = dList.get(3).getId();
                        }
                        break;
                }

                Intent it = new Intent(mContext, DreamDetailActivity.class);
                it.putExtra("requirementId", requirementId);
                mContext.startActivity(it);
            }
        };

        img_area0.setOnClickListener(listener);
        img_area1.setOnClickListener(listener);
        img_area2.setOnClickListener(listener);
        img_area3.setOnClickListener(listener);
    }

}