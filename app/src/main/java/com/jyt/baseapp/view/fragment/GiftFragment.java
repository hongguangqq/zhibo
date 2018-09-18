package com.jyt.baseapp.view.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.GiftAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.GiftBean;
import com.jyt.baseapp.itemDecoration.SpacesItemDecoration;
import com.jyt.baseapp.model.BandModel;
import com.jyt.baseapp.model.impl.BandModelImpl;
import com.jyt.baseapp.view.widget.CircleImageView;
import com.jyt.baseapp.view.widget.MyRecycleView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author LinWei on 2018/5/7 17:00
 */
public class GiftFragment extends BaseFragment {
    @BindView(R.id.tv_gift_time)
    TextView mTvTime;
    @BindView(R.id.iv_gift_send)
    CircleImageView mIvSend;
    @BindView(R.id.tv_gift_send)
    TextView mTvSend;
    @BindView(R.id.ll_gift_send)
    LinearLayout mLlSend;
    @BindView(R.id.tv_gift_money)
    TextView mTvMoney;
    @BindView(R.id.iv_gift_receiver)
    CircleImageView mIvReceiver;
    @BindView(R.id.tv_gift_receiver)
    TextView mTvReceiver;
    @BindView(R.id.ll_gift_receiver)
    LinearLayout mLlReceiver;
    @BindView(R.id.rv_gift_content)
    MyRecycleView mRvContent;
    @BindView(R.id.trl_gift_load)
    TwinklingRefreshLayout mTrlLoad;



    private GiftAdapter mAdapter;
    private BandModel mBandModel;
    private List<GiftBean> mGiftList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    protected void firstInit() {
        init();
        initSetting();
    }


    private void init(){

        mGiftList = new ArrayList<>();
        mAdapter = new GiftAdapter();
        mTvMoney.setTypeface(App.getInstace().getTypeface());
        mAdapter.setDataList(mGiftList);
        mBandModel = new BandModelImpl();
        mBandModel.onStart(getActivity());

    }

    private void initSetting(){
        mTrlLoad.setEnableLoadmore(false);//不可上拉加载更多
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvContent.addItemDecoration(new SpacesItemDecoration(0,4));
        mRvContent.setAdapter(mAdapter);
        mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mBandModel.getBangList(6, 0, 10, new BeanCallback<BaseJson<List<GiftBean>>>() {
                    @Override
                    public void response(boolean success, BaseJson<List<GiftBean>> response, int id) {
                        if (success){
                            if (response.getCode()==200 ){
                                if (response.getData()!=null && response.getData().size()!=0){
                                    mGiftList = response.getData();

                                }
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //取出第一条数据
                                if (mGiftList.size()>1){
                                    GiftBean firstBean = mGiftList.remove(0);
                                    mAdapter.notifyData(mGiftList);
                                    mTrlLoad.finishRefreshing();
                                    mTvSend.setText(firstBean.getFromNickName());
                                    mTvReceiver.setText(firstBean.getNickName());
                                    Glide.with(App.getContext()).load(firstBean.getFromImg()).error(R.mipmap.timg).into(mIvSend);
                                    Glide.with(App.getContext()).load(firstBean.getHeadImg()).error(R.mipmap.timg).into(mIvReceiver);
                                    mTvMoney.setText(firstBean.getPrice()+"");
                                }

                            }
                        }, 1000);

                    }
                });
            }
        });
        mTrlLoad.startRefresh();

    }





}
