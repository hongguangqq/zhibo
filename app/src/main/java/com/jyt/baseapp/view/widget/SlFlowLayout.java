package com.jyt.baseapp.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.nex3z.flowlayout.FlowLayout;

/**
 * @author LinWei on 2018/5/6 13:46
 */
public class SlFlowLayout extends FlowLayout {
    public SlFlowLayout(Context context) {
        super(context);
    }

    public SlFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
