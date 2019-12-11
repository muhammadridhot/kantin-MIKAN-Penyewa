package com.example.ridho.mikan_penyewa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipeDisableViewPager extends ViewPager {

    private boolean enabled;

    public SwipeDisableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.enabled){
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    public void setPagingEnable(boolean enabled){
        this.enabled = enabled;
    }
}
