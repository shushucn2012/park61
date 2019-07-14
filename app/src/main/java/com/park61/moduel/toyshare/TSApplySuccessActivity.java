package com.park61.moduel.toyshare;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.GlobalParam;
import com.park61.common.tool.CommonMethod;
import com.park61.common.tool.ExitAppUtils;
import com.park61.common.tool.FU;
import com.park61.common.tool.ShareTool;

/**
 * Created by shubei on 2017/6/19.
 */

public class TSApplySuccessActivity extends BaseActivity {

    private TextView tv_tip, tv_details, tv_bottom_tip;
    private Button btn_finish, btn_continue;
    private String shareApplyId, tip;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_apply_success);
    }

    @Override
    public void initView() {
        setPagTitle("申请成功");
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_details = (TextView) findViewById(R.id.tv_details);
        tv_bottom_tip = (TextView) findViewById(R.id.tv_bottom_tip);
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_continue = (Button) findViewById(R.id.btn_continue);
    }

    @Override
    public void initData() {
        tip = getIntent().getStringExtra("tip");
        tv_tip.setText(tip);
        tv_details.setText(getIntent().getStringExtra("details"));
        tv_bottom_tip.setText(getIntent().getStringExtra("bottom_tip"));
        shareApplyId = getIntent().getStringExtra("shareApplyId");
        if (tip != null && tip.contains("归还")) {
            btn_continue.setText("我的申领");
        } else {
            btn_continue.setText("我的申领");
        }
    }

    @Override
    public void initListener() {
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tip != null && tip.contains("归还")) {
                    for (Activity activity : ExitAppUtils.getInstance().getActList()) {
                        if (activity instanceof TSApplyDetailsActivity) {
                            activity.finish();
                        }
                    }
                    TSMyApplyToyListActivity.NEED_FRESH = true;
                    finish();
                } else {
                    for (Activity activity : ExitAppUtils.getInstance().getActList()) {
                        if (activity instanceof TSApplyDetailsActivity) {
                            activity.finish();
                        }
                    }
                    Intent it = new Intent(mContext, TSMyApplyToyListActivity.class);
                    CommonMethod.startOnlyNewActivity(mContext, TSMyApplyToyListActivity.class, it);
                    finish();
                }
            }
        });
        String url = "http://m.61park.cn/toy/toMyToyShareDetail.do?toyShareId=" + GlobalParam.CurJoy.getId();
        initShareViewAndData(url,
                GlobalParam.CurJoy.getIntroductionImg(),
                mContext.getResources().getString(R.string.app_name),
                GlobalParam.CurJoy.getName());
    }

    private void initShareViewAndData(String shareUrl, String sharePic, String shareTitle, String shareDescription) {
        ShareTool.init(shareUrl, sharePic, shareTitle, shareDescription);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncVoteShares(4, 2, FU.paseLong(GlobalParam.CurJoy.getId() + ""), FU.paseLong(shareApplyId));
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
