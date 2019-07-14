package com.park61.moduel.toyshare;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.ExitAppUtils;
import com.park61.common.tool.FU;
import com.park61.common.tool.ImageManager;
import com.park61.common.tool.ShareTool;

/**
 * Created by shubei on 2017/6/19.
 */

public class TSGetSuccessActivity extends BaseActivity {

    private ImageView img_success, img_toy, img_fx_pyqs, img_more_share;
    private Button right_img;
    private TextView tv_toy_name;
    private View area_share_one, area_share_more;
    private String applyId;
    private String seriesId;
    private String title;
    private String coverUrl;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_get_success);
    }

    @Override
    public void initView() {
        img_success = (ImageView) findViewById(R.id.img_success);
        right_img = (Button) findViewById(R.id.right_img);
        img_toy = (ImageView) findViewById(R.id.img_toy);
        tv_toy_name = (TextView) findViewById(R.id.tv_toy_name);
        img_more_share = (ImageView) findViewById(R.id.img_more_share);
        img_fx_pyqs = (ImageView) findViewById(R.id.img_fx_pyqs);
        area_share_one = findViewById(R.id.area_share_one);
        area_share_more = findViewById(R.id.area_share_more);
    }

    @Override
    public void initData() {
        String state = getIntent().getStringExtra("state");
        if (state.equals("0")) {
            setPagTitle("领取成功");
            img_success.setImageResource(R.drawable.lqchenggong);
        } else {
            setPagTitle("领取失败");
            img_success.setImageResource(R.drawable.beiqiangzoula);
        }

        applyId = getIntent().getStringExtra("applyId");
        seriesId = getIntent().getStringExtra("seriesId ");
        title = getIntent().getStringExtra("title");
        coverUrl = getIntent().getStringExtra("coverUrl");

        if (GlobalParam.CurJoy != null) {
            ImageManager.getInstance().displayImg(img_toy, GlobalParam.CurJoy.getIntroductionImg());
            tv_toy_name.setText(GlobalParam.CurJoy.getName());
        } else {
            ImageManager.getInstance().displayImg(img_toy, coverUrl);
            tv_toy_name.setText(title);
        }
    }

    @Override
    public void initListener() {
        if (GlobalParam.CurJoy != null) {
            String url = "http://m.61park.cn/toy/toMyToyShareDetail.do?toyShareId=" + GlobalParam.CurJoy.getId();
            initShareViewAndData(url,
                    GlobalParam.CurJoy.getIntroductionImg(),
                    mContext.getResources().getString(R.string.app_name),
                    GlobalParam.CurJoy.getName());
        } else {
            String url = "http://m.61park.cn/toy/toMyToyShareDetail.do?toyShareId=" + seriesId;
            initShareViewAndData(url,
                    coverUrl,
                    mContext.getResources().getString(R.string.app_name),
                    title);
        }
        right_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Activity activity : ExitAppUtils.getInstance().getActList()) {
                    if (activity instanceof ToyDetailsActivity || activity instanceof TSApplyDetailsActivity) {
                        activity.finish();
                    }
                }
                TSMyApplyToyListActivity.NEED_FRESH = true;
                finish();
            }
        });
        img_fx_pyqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareTool.shareToWeiXin(mContext, 1);
            }
        });
        img_more_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area_share_one.setVisibility(View.GONE);
                area_share_more.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initShareViewAndData(String shareUrl, String sharePic, String shareTitle, String shareDescription) {
        ShareTool.init(shareUrl, sharePic, shareTitle, shareDescription);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalParam.CurJoy != null) {
                    asyncVoteShares(4, 2, FU.paseLong(GlobalParam.CurJoy.getId() + ""), GlobalParam.CUR_TOYSHARE_APPLY_ID);
                } else {
                    asyncVoteShares(4, 2, FU.paseLong(seriesId + ""), FU.paseLong(applyId + ""));
                }
                switch (v.getId()) {
                    case R.id.img_fx_pyq:
                        ShareTool.shareToWeiXin(mContext, 1);
                        break;
                    case R.id.img_fx_wx:
                        ShareTool.shareToWeiXin(mContext, 0);
                        break;
                    case R.id.img_fx_qq:
                        ShareTool.shareToQQ(mContext);
                        break;
                    case R.id.img_fx_qzone:
                        ShareTool.shareToQzone(mContext);
                        break;
                }
            }
        };
        findViewById(R.id.img_fx_pyq).setOnClickListener(clickListener);
        findViewById(R.id.img_fx_wx).setOnClickListener(clickListener);
        findViewById(R.id.img_fx_qq).setOnClickListener(clickListener);
        findViewById(R.id.img_fx_qzone).setOnClickListener(clickListener);
    }
}
