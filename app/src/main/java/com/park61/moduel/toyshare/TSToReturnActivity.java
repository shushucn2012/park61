package com.park61.moduel.toyshare;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.common.set.AppUrl;
import com.park61.common.set.GlobalParam;
import com.park61.common.set.ParamBuild;
import com.park61.common.tool.ImageManager;
import com.park61.moduel.toyshare.bean.JoyApplyItem;
import com.park61.net.base.Request;
import com.park61.net.request.interfa.BaseRequestListener;
import com.park61.net.request.interfa.JsonRequestListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shubei on 2017/6/21.
 */

public class TSToReturnActivity extends BaseActivity {

    private JoyApplyItem mJoyApplyItem;
    private ImageView btn_add_box, btn_reduce_box, btn_reduce_part, btn_add_part, img_toy_box, img_toy_part;
    private TextView tv_return_box_num, tv_return_part_num, tv_box_num, tv_part_num, tv_toy_name;
    private Button btn_confirm;

    private int boxNum, partNum;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_ts_toreturn);
    }

    @Override
    public void initView() {
        img_toy_box = (ImageView) findViewById(R.id.img_toy_box);
        img_toy_part = (ImageView) findViewById(R.id.img_toy_part);

        btn_reduce_box = (ImageView) findViewById(R.id.btn_reduce_box);
        btn_add_box = (ImageView) findViewById(R.id.btn_add_box);
        tv_return_box_num = (TextView) findViewById(R.id.tv_return_box_num);

        btn_reduce_part = (ImageView) findViewById(R.id.btn_reduce_part);
        btn_add_part = (ImageView) findViewById(R.id.btn_add_part);
        tv_return_part_num = (TextView) findViewById(R.id.tv_return_part_num);

        tv_box_num = (TextView) findViewById(R.id.tv_box_num);
        tv_part_num = (TextView) findViewById(R.id.tv_part_num);
        tv_toy_name = (TextView) findViewById(R.id.tv_toy_name);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initData() {
        setPagTitle("申请归还");
        mJoyApplyItem = (JoyApplyItem) getIntent().getSerializableExtra("TOY_APPLY");

        ImageManager.getInstance().displayImg(img_toy_box, mJoyApplyItem.getToyShareBoxSeriesDTO().getIntroductionImg());
        ImageManager.getInstance().displayImg(img_toy_part, mJoyApplyItem.getToyShareBoxSeriesDTO().getIntroductionImg());
        tv_box_num.setText(mJoyApplyItem.getToyShareBoxSeriesDTO().getBoxNum() + "");
        tv_part_num.setText(mJoyApplyItem.getToyShareBoxSeriesDTO().getFitNum() + "");
        tv_toy_name.setText(mJoyApplyItem.getToyShareBoxSeriesDTO().getName());
    }

    @Override
    public void initListener() {
        btn_add_box.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (boxNum < mJoyApplyItem.getToyShareBoxSeriesDTO().getBoxNum()) {//mJoyApplyItem.getToyShareBoxSeriesDTO().getBoxNum()
                    boxNum++;
                    updateBoxNumView();
                }
            }
        });
        btn_reduce_box.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (boxNum > 0) {
                    boxNum--;
                    updateBoxNumView();
                }
            }
        });
        btn_add_part.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (partNum < mJoyApplyItem.getToyShareBoxSeriesDTO().getFitNum()) {//mJoyApplyItem.getToyShareBoxSeriesDTO().getFitNum()
                    partNum++;
                    updatePartNumView();
                }
            }
        });
        btn_reduce_part.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (partNum > 0) {
                    partNum--;
                    updatePartNumView();
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncReturnBoxApply();
            }
        });
    }

    public void updateBoxNumView() {
        tv_return_box_num.setText(boxNum + "");
    }

    public void updatePartNumView() {
        tv_return_part_num.setText(partNum + "");
    }

    private void asyncReturnBoxApply() {
        String wholeUrl = AppUrl.host + AppUrl.TOYBOXRETURN;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", mJoyApplyItem.getId());
        map.put("lostBoxNum", boxNum);
        map.put("lostFitNum", partNum);
        String requestBodyData = ParamBuild.buildParams(map);
        netRequest.startRequest(wholeUrl, Request.Method.POST, requestBodyData, 0, tLsner);
    }

    BaseRequestListener tLsner = new JsonRequestListener() {

        @Override
        public void onStart(int requestId) {
            showDialog();
        }

        @Override
        public void onError(int requestId, String errorCode, String errorMsg) {
            dismissDialog();
            showShortToast(errorMsg);
        }

        @Override
        public void onSuccess(int requestId, String url, JSONObject jsonResult) {
            dismissDialog();
            Intent it = new Intent(mContext, TSApplySuccessActivity.class);
            it.putExtra("tip", "申请归还成功");
            it.putExtra("details", "预计支付￥" + mJoyApplyItem.getPayAmount());
            it.putExtra("bottom_tip", "实际费用按具体归还时间计算为准");
            GlobalParam.CurJoy = mJoyApplyItem.getToyShareBoxSeriesDTO();
            startActivity(it);
            finish();
        }
    };
}
