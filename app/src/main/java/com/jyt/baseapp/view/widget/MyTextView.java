package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jyt.baseapp.App;

/**
 * @author LinWei on 2018/5/22 13:59
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
//        setTypeface(App.getInstace().getTypeface());
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setTypeface(App.getInstace().getTypeface());
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setTypeface(App.getInstace().getTypeface());
    }
}
