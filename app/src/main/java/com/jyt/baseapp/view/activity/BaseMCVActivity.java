package com.jyt.baseapp.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.annotation.ActivityAnnotation;
import com.jyt.baseapp.model.BaseModel;
import com.jyt.baseapp.util.FinishActivityManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/5/10.
 */

public abstract class BaseMCVActivity extends AppCompatActivity {

    FinishActivityManager manager = FinishActivityManager.getManager();


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_function)
    TextView textFunction;
    @BindView(R.id.v_actionBar)
    RelativeLayout vActionBar;
    @BindView(R.id.v_main)
    LinearLayout vMain;
    @BindView(R.id.img_function)
    ImageView imgFunction;

    RefreshViewBroadcast refreshViewBroadcast;

    List<BaseModel> models = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);

        //绑定baseActivity内的控件
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
//            e.printStackTrace();
        }


        View contentView = getContentView();
        if (contentView != null) {
            vMain.addView(contentView);
        }
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            vMain.addView(LayoutInflater.from(getContext()).inflate(layoutId, vMain, false));
        }

        //绑定子类控件
        ButterKnife.bind(this);


        getAnnotation();

        if (!TextUtils.isEmpty(getTitle())) {
            textTitle.setText(getTitle());
        }
        manager.addActivity(this);

        refreshViewBroadcast = new RefreshViewBroadcast();
        IntentFilter intentFilter = new IntentFilter("REFRESH_VIEW");
        registerReceiver(refreshViewBroadcast,intentFilter);

        createModels(models);

        allModelsStart(getContext());
    }

    private void getAnnotation() {
        if (textTitle == null || imgBack == null || textFunction == null) {
            return;
        }

        ActivityAnnotation annotation = this.getClass().getAnnotation(ActivityAnnotation.class);
        System.out.println(annotation);
        if (annotation == null) {
            return;
        }
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Object invoke = null;
            try {
                invoke = method.invoke(annotation);
                if (method.getName().equals("showBack")) {
                    imgBack.setVisibility(((boolean) invoke) ? View.VISIBLE : View.GONE);
                } else if (method.getName().equals("title")) {
                    textTitle.setText((CharSequence) invoke);
                } else if (method.getName().equals("showFunctionText")) {
                    textFunction.setVisibility(((boolean) invoke) ? View.VISIBLE : View.GONE);
                } else if (method.getName().equals("functionText")) {
                    textFunction.setText((CharSequence) invoke);
                } else if (method.getName().equals("showActionBar")) {
                    vActionBar.setVisibility((boolean) invoke ? View.VISIBLE : View.GONE);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void createModels(List<BaseModel> models){

    }

    public void allModelsStart(Context context){
        if (models !=null)
            for (BaseModel model:
                    models
                    ) {
                model.onStart(context);
            }
    }
    public void allModelsDestroy(){
        if (models !=null)
            for (BaseModel model:
                    models
                    ) {
                model.onDestroy();
            }
    }

    public void setTextTitle(String text) {
        textTitle.setText(text);
    }

    public void showBackBtn() {
        imgBack.setVisibility(View.VISIBLE);
    }

    public void hideBackBtn() {
        imgBack.setVisibility(View.INVISIBLE);

    }

    public void setFunctionText(String text) {
        textFunction.setText(text);
    }

    public void showFunctionText() {
        textFunction.setVisibility(View.VISIBLE);
    }

    public void showFunctionImage(){
        imgFunction.setVisibility(View.VISIBLE);
    }

    public void hideFunctionImage(){
        imgFunction.setVisibility(View.GONE);
    }

    public void hideFunctionText() {
        textFunction.setVisibility(View.INVISIBLE);
    }

    public void setvActionBarColor(int color){
        vActionBar.setBackgroundColor(getResources().getColor(color));
    }

    public void setvMainBackground(int id){
        vMain.setBackground(getResources().getDrawable(id));
    }

    public void setvMainBackgroundColor(int id){
        vMain.setBackgroundColor(getResources().getColor(id));
    }

    @OnClick(R.id.img_back)
    public void onBackClick() {
        if (mOnBackListener!=null){
            mOnBackListener.back();
            return;
        }
        onBackPressed();
    }

    public void setFunctionImage(int resId){
        imgFunction.setImageDrawable(getResources().getDrawable(resId));
    }

    @OnClick({R.id.text_function,R.id.img_function})
    public void onFunctionClick() {
        if (mOnNextListener!=null){
            mOnNextListener.next();
        }
    }

    /**
     * 隐藏ActionBar
     */
    public void HideActionBar() {
        vActionBar.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.finishActivity(this);
        allModelsDestroy();
        unregisterReceiver(refreshViewBroadcast);
    }

    public Context getContext() {
        return this;
    }

    public Activity getActivity(){
        return this;
    }

    public void refreshFragment(int index){

    }

    abstract protected int getLayoutId();

    abstract protected View getContentView();

    protected void onReceiveRefreshViewBroadcast(Context context, Intent intent) {

    }

    class RefreshViewBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    public interface OnNextListener{
        void next();
    }
    private OnNextListener mOnNextListener;
    public void setOnNextListener( OnNextListener listener){
        this.mOnNextListener = listener;
    }

    public interface OnBackListener{
        void back();
    }
    private OnBackListener mOnBackListener;
    public void setOnBackListener( OnBackListener listener){
        this.mOnBackListener = listener;
    }
}
