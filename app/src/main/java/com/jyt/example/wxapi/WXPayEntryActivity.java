package com.jyt.example.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jyt.baseapp.App;
import com.jyt.baseapp.helper.WeChartHelper;
import com.jyt.baseapp.util.L;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 需在android manifest 上配置 android:exported="true"
 * 处理微信支付
 * Created by chenweiqi on 2017/12/12.
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    WeChartHelper weChartHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weChartHelper = new WeChartHelper();
        weChartHelper.init(this, App.weiXin_AppKey);
        weChartHelper.registerToWx();
        IWXAPI api = weChartHelper.getWxApi();
        api.handleIntent(getIntent(),this);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            L.e("onPayFinish,errCode="+resp.errCode);
            boolean payResult = false;
           if (resp.errCode == BaseResp.ErrCode.ERR_OK){
               payResult = true;
           }else {

           }
            Intent intent = new Intent();
            intent.putExtra(WeChartHelper.BROADCAST_TYPE, WeChartHelper.BROADCAST_TYPE_PAY);
            intent.putExtra("data",payResult);
            intent.setAction(WeChartHelper.ACTION_WECHART_RECEIVE);
            sendBroadcast(intent);
            finish();
        }
    }
}
