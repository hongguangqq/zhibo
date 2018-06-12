package com.jyt.baseapp.util;

import android.content.Context;
import android.os.Handler;

/**
 * Created by chenweiqi on 2017/11/6.
 */

public class CountDownUtil {

    static final int defaultCount = 30;
    static final int defaultDelay = 1000;

    Handler handler;
    int count;//s
    int delay;//ms

    int useCount;

    Context context;
    TimerRunnable timerRunnable;
    CountDownCallback countDownCallback;

    public CountDownUtil(Context context) {
        this(context,defaultCount,defaultDelay);
    }

    public CountDownUtil(Context context, int count_s, int delay_ms) {
        this.count = count_s;
        this.delay = delay_ms;
        this.context = context;
        handler = new Handler(context.getMainLooper());
    }

    public void start(){
        useCount = count;
        handler.post(timerRunnable = new TimerRunnable());
    }

    public void stop(){
        if (timerRunnable!=null)
            handler.removeCallbacks(timerRunnable);
    }

    public void setCountDownCallback(CountDownCallback countDownCallback) {
        this.countDownCallback = countDownCallback;
    }

    class TimerRunnable implements Runnable {

        @Override
        public void run() {
            if (useCount>=0){

                handler.postDelayed(timerRunnable = new TimerRunnable(),delay);
            }else {
                handler.removeCallbacks(timerRunnable);
            }

            if (countDownCallback!=null)
                countDownCallback.countDownCallback(useCount==-1,useCount);

            useCount--;

        }
    }

    public interface CountDownCallback{
        void countDownCallback(boolean finish, int currentCount);
    }
}
