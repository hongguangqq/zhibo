package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author LinWei on 2018/5/8 14:18
 */
public class MyRecycleView extends RecyclerView {

    public int startx,starty,endx,endy;
    private boolean isTop;


    public MyRecycleView(Context context) {
        super(context);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN://点击时的坐标
                getParent().requestDisallowInterceptTouchEvent(true);
                startx= (int) ev.getRawX();
                starty= (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE://移动时的坐标
                endx= (int) ev.getRawX();
                endy= (int) ev.getRawY();
                if (Math.abs(endx-startx)>Math.abs(endy-starty)){//左右滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else {//上下滑动
                    getParent().requestDisallowInterceptTouchEvent(true);

                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return super.onTouchEvent(e);
    }

    public boolean canScrollHorizontally(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }

    public void setTop(boolean isTop){
        this.isTop = isTop;
    }
}
