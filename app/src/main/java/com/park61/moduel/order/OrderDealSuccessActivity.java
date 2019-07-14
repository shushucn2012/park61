package com.park61.moduel.order;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.park61.BaseActivity;
import com.park61.R;
import com.park61.moduel.order.bean.OrderBean;

/**
 * 确认收货后交易成功界面
 */
public class OrderDealSuccessActivity extends BaseActivity {
    private Button btn_evaluate,btn_orderdetail;
    private OrderBean orderInfo;
    private long soId;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_order_deal_sucess);
    }

    @Override
    public void initView() {
        btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
        btn_orderdetail = (Button) findViewById(R.id.btn_orderdetail);
    }

    @Override
    public void initData() {
        soId = getIntent().getLongExtra("soId", soId);
        orderInfo = (OrderBean) getIntent().getSerializableExtra("orderInfo");
        if(orderInfo == null){
            showShortToast("订单异常!");
            this.finish();
            return;
        }
    }

    @Override
    public void initListener() {
        btn_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TradeOrderEvaluateActivity.class);
                intent.putExtra("soId", soId);
                intent.putExtra("orderInfo", orderInfo);
                startActivity(intent);
            }
        });
        btn_orderdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, TradeOrderDetailActivity.class);
                it.putExtra("orderid", soId);
                startActivity(it);
            }
        });
    }
}
