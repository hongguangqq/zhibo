package com.jyt.baseapp.view.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.util.SoftInputUtil;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by chenweiqi on 2017/11/3.
 */

public class SearchView extends LinearLayout {
    @BindView(R.id.input_text)
    EditText inputText;
    @BindView(R.id.text_hintText)
    TextView textHintText;
    @BindView(R.id.img_clear)
    ImageView imgClear;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.v_hintContent)
    LinearLayout vHintContent;
    @BindView(R.id.v_mainView)
    RelativeLayout vMainView;

    private static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final int DEFAULT_IMG_SEARCH = R.mipmap.icon_seach;
    private static final int DEFAULT_HINT_COLOR = 0x868686;
    private static final int DEFAULT_TEXT_COLOR = 0x000000;




    OnSearchViewTextChangedListener onSearchViewTextChangedListener;
    private int markWidth = 0;
    private int mBgColor = DEFAULT_BG_COLOR;
    private int mIvSearch = DEFAULT_IMG_SEARCH;
    private int mHintColor = DEFAULT_HINT_COLOR;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private String mHintText;


    public SearchView(Context context) {
        this(context, null);
        init(context);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        mBgColor = a.getColor(R.styleable.SearchView_bg_color,DEFAULT_BG_COLOR);
        mIvSearch = a.getResourceId(R.styleable.SearchView_logo,R.mipmap.icon_seach);
        mHintColor = a.getColor(R.styleable.SearchView_hintColor,DEFAULT_HINT_COLOR);
        mTextColor = a.getColor(R.styleable.SearchView_TextColor,DEFAULT_TEXT_COLOR);
        mHintText = a.getString(R.styleable.SearchView_hint);
        vMainView.setBackgroundColor(mBgColor);
        inputText.setTextColor(mTextColor);
        imgSearch.setImageDrawable(getResources().getDrawable(mIvSearch));
        textHintText.setTextColor(mHintColor);
        if (mHintText==null){
            mHintText = "搜索";
        }
        textHintText.setText(mHintText);

    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.widget_search_view, this, true);
        ButterKnife.bind(this);

        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                markWidth = vHintContent.getWidth();
                int vleft = (getWidth()-markWidth)/2;
                vHintContent.setX(vleft);
                removeOnLayoutChangeListener(this);
            }
        });

        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (mDownListener!=null){
                    mDownListener.over();
                }
                return false;
            }
        });
    }


    @OnTextChanged(value = R.id.input_text,callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onInputTextChanged(CharSequence s){
        imgClear.setVisibility(s.length()==0?GONE:VISIBLE);

        if (onSearchViewTextChangedListener!=null){
            onSearchViewTextChangedListener.onTextChanged(s.toString());
        }
    }

    @OnClick(R.id.v_mainView)
    public void onHintLayoutClick() {
        int form = (int) vHintContent.getX();
        int to = DensityUtil.dp2px(getContext(), 10);
        ObjectAnimator anim =  ObjectAnimator.ofFloat(vHintContent, "X", form, to).setDuration(200);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textHintText.setVisibility(GONE);
                inputText.setVisibility(VISIBLE);
                inputText.requestFocus();
                SoftInputUtil.showSoftKeyboard(getContext(),inputText);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();

    }

    @OnClick(R.id.img_clear)
    public void onClearClick(){
        inputText.setText("");
    }

    public void setOnSearchViewTextChangedListener(OnSearchViewTextChangedListener onSearchViewTextChangedListener) {
        this.onSearchViewTextChangedListener = onSearchViewTextChangedListener;
    }

    public interface OnSearchViewTextChangedListener{
        void onTextChanged(String text);
    }

    public String getText(){
        return inputText.getText().toString();
    }

    public interface OnSearchKeyDownListener{
        void over();
    }
    private OnSearchKeyDownListener mDownListener;
    public void setOnSearchKeyDownListener(OnSearchKeyDownListener listener){
        mDownListener = listener;
    }
}
