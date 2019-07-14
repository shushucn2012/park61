package com.park61.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.park61.R;

/**
 * 自定义倒计时文本控件  
 *
 * @author super
 */
public class GroupTimeTextView extends TextView implements Runnable {

    Paint mPaint; // 画笔,包含了画几何图形、文本等的样式和颜色信息
    private long[] times;
    private long mday, mhour, mmin, msecond;// 天，小时，分钟，秒
    private boolean run = false; // 是否启动了
    private OnTimeEnd mOnTimeEnd;
    private String strTime = "";

    public GroupTimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle(); // 一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public GroupTimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);
        array.recycle(); // 一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public GroupTimeTextView(Context context) {
        super(context);
    }

    public long[] getTimes() {
        return times;
    }

    public String getLeftTimeStr() {
        if(TextUtils.isEmpty(strTime)){
            strTime = "00:00:00";
        }
        return strTime;
    }

    public void setTimes(long[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];
    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 23;
                    mday--;
                }
            }
        }
        if (msecond == 0l && mmin == 0l && mhour == 0l && mday==0l) {// 为0的时候停止
            setRun(false);
        }
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        // 标示已经启动
        run = true;
        ComputeTime();
        String d,h, m, s;
        d = mday+"";
        if (mhour < 10) {
            h = "0" + mhour;
        } else {
            h = mhour + "";
        }
        if (mmin < 10) {
            m = "0" + mmin;
        } else {
            m = mmin + "";
        }
        if (msecond < 10) {
            s = "0" + msecond;
        } else {
            s = msecond + "";
        }
        strTime = d +"天"+h + ":" + m + ":" + s;
        this.setText(Html.fromHtml(strTime));
        if (run) {
            postDelayed(this, 1000);
        } else {
            if (mOnTimeEnd != null) {
                mOnTimeEnd.onEnd();
            }
        }
    }

    public interface OnTimeEnd {
        public void onEnd();
    }

    public void setOnTimeEndLsner(OnTimeEnd onTimeEnd) {
        this.mOnTimeEnd = onTimeEnd;
    }
}
