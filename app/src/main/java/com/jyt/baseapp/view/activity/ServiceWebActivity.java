package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.WebBean;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.view.widget.NoScrollWebView;

import butterknife.BindView;


public class ServiceWebActivity extends BaseMCVActivity {
    @BindView(R.id.wv_detail)
    NoScrollWebView wvDetail;
    private PersonModel mPersonModel;
    @Override
    protected int getLayoutId() {
        return R.layout.personal_service_web_activity;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTextTitle("");
        setvMainBackgroundColor(R.color.bg_content);
        String code = getIntent().getStringExtra("code");
        wvDetail.getLayoutParams().height = 1607;
        wvDetail.requestLayout();
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mPersonModel.getWebData(code, new BeanCallback<BaseJson<WebBean>>() {
            @Override
            public void response(boolean success, BaseJson<WebBean> response, int id) {
                if (success && response.getCode()==200){
                    wvDetail.loadData(response.getData().getTitle(), "text/html; charset=UTF-8", "UTF-8");
                    WebSettings webSettings = wvDetail.getSettings();
                    webSettings.setJavaScriptEnabled(true);//允许使用js
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                    webSettings.setUseWideViewPort(true);//关键点
                    webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    webSettings.setDisplayZoomControls(false);
                    webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
                    webSettings.setAllowFileAccess(true); // 允许访问文件
                    webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
                    webSettings.setSupportZoom(true); // 支持缩放
                    webSettings.setLoadWithOverviewMode(true);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonModel.onDestroy();
    }
}
