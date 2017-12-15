package com.hrmp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Ly on 2017/5/9.
 */

public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private int startX = 0;
    private int startY = 0;

    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getParent().requestDisallowInterceptTouchEvent(true);// 请求父控件,不要拦截我的事件,
                    startX = (int) ev.getX();
                    startY = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int endX = (int) ev.getX();
                    int endY = (int) ev.getY();

                    int dx = endX - startX;
                    int dy = endY - startY;

                    if (dx > 0) {// 向右滑动
                        if (getCurrentItem() == 0) {// 当前item是第一个
                            getParent().requestDisallowInterceptTouchEvent(false);// 父控件可以拦截事件
                        }
                    } else {
                        if (getCurrentItem() == getAdapter().getCount() - 1) {// 当前item是最后一个
                            getParent().requestDisallowInterceptTouchEvent(false);// 父控件可以拦截事件
                        }
                    }
                    break;
                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
}
