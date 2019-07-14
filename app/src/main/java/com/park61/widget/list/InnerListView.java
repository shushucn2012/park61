package com.park61.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

public class InnerListView extends ListView {
	
	ScrollView parentScrollView;

	public InnerListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InnerListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void setParentScrollView(ScrollView scrollView){
		this.parentScrollView = scrollView;
	}

	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
                setParentScrollAble(false);//当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview 停住不能滚动

        case MotionEvent.ACTION_MOVE:
                
                break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
               setParentScrollAble(true);//当手指松开时，让父ScrollView重新拿到onTouch权限
        
        break;
            default:
                break;
     
		}
        return super.onInterceptTouchEvent(ev);
    
	}



    private void setParentScrollAble(boolean flag) {
    	if(parentScrollView != null){
    		parentScrollView.requestDisallowInterceptTouchEvent(!flag);//这里的parentScrollView就是listview外面的那个scrollview
    	}

    }
}
