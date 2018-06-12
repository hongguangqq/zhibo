package com.jyt.baseapp.view.fragment;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.App;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.GiftBean;
import com.jyt.baseapp.model.BandModel;
import com.jyt.baseapp.model.impl.BandModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.MyTextView;
import com.jyt.baseapp.view.widget.SlFlowLayout;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * @author LinWei on 2018/5/7 10:05
 */
public class ListFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.trl_list_load)
    TwinklingRefreshLayout mTrlLoad;
    @BindView(R.id.iv_list_no1)
    ImageView mIvNo1;
    @BindView(R.id.tv_list_na1)
    TextView mTvNa1;
    @BindView(R.id.tv_list_no1)
    MyTextView mTvNo1;
    @BindView(R.id.rl_list_no1)
    RelativeLayout mRlNo1;
    @BindView(R.id.iv_list_no2)
    ImageView mIvNo2;
    @BindView(R.id.tv_list_na2)
    TextView mTvNa2;
    @BindView(R.id.tv_list_no2)
    MyTextView mTvNo2;
    @BindView(R.id.rl_list_no2)
    RelativeLayout mRlNo2;
    @BindView(R.id.iv_list_no3)
    ImageView mIvNo3;
    @BindView(R.id.tv_list_na3)
    TextView mTvNa3;
    @BindView(R.id.tv_list_no3)
    MyTextView mTvNo3;
    @BindView(R.id.rl_list_no3)
    RelativeLayout mRlNo3;
    @BindView(R.id.tv_list_day)
    TextView mTvDay;
    @BindView(R.id.tv_list_week)
    TextView mTvWeek;
    @BindView(R.id.sl_list_content)
    SlFlowLayout mSlContent;
    Unbinder unbinder;

    private int mDayCode;
    private int mWeekCode;
    private List<GiftBean> mGiftList;
    private int mImgWidth;
    private OnRankingListener mOnRankingListener;
    private boolean isNoFirst;//不是初次进入
    private BandModel mBandModel;
    private Handler mHandler =new Handler();
    private int mCode;


    public ListFragment(int code) {
        if (code==3){
            //土豪
            mDayCode = 3;
            mWeekCode = 4;
        }else if (code==1){
            //女神
            mDayCode = 1;
            mWeekCode = 2;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void firstInit() {
        init();
        initSetting();
        initListener();
    }


    private void init() {
        mGiftList = new ArrayList<>();
        mBandModel  = new BandModelImpl();
        mTvNo1.setTypeface(App.getInstace().getTypeface());
        mTvNo2.setTypeface(App.getInstace().getTypeface());
        mTvNo3.setTypeface(App.getInstace().getTypeface());
        mImgWidth = BaseUtil.getScannerWidth()/3;
        mOnRankingListener = new OnRankingListener();


    }

    private void initSetting(){
        mTrlLoad.setEnableLoadmore(false);//不可上拉加载更多
        mTrlLoad.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mBandModel.getGiftData(mCode, 0, 50, new BeanCallback<BaseJson<List<GiftBean>>>() {
                    @Override
                    public void response(boolean success, BaseJson<List<GiftBean>> response, int id) {
                        if (success){
                            if (response.getCode()==200){
                                if (response.getData()!=null && response.getData().size()!=0){
                                    mGiftList = response.getData();
                                }
                            }
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setRankingData(mGiftList);
                                mTrlLoad.finishRefreshing();
                            }
                        },1500);

                    }
                });
            }
        });
        getDataByDay(1);

    }

    private void initListener(){
        mTvDay.setOnClickListener(this);
        mTvWeek.setOnClickListener(this);
    }


    /**
     * 设置第三名以后的排名
     * @param data
     */
    public void setRankingData(List data){
        mSlContent.removeAllViews();
        for (int i = 3; i < data.size(); i++) {
            RelativeLayout layout = (RelativeLayout) View.inflate(getActivity(),R.layout.holder_ranking,null);
            ImageView iv = (ImageView) layout.findViewById(R.id.iv_ranking_headpic);
            MyTextView tv_no = (MyTextView) layout.findViewById(R.id.tv_ranking_no);
            TextView tv_na = (TextView) layout.findViewById(R.id.tv_ranking_na);
            iv.setBackground(getResources().getDrawable(R.mipmap.timg));
            tv_no.setText((i+4)+"");
            tv_no.setTypeface(App.getInstace().getTypeface());
            layout.setTag(i);
            layout.setOnClickListener(mOnRankingListener);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(mImgWidth,BaseUtil.dip2px(111));
            mSlContent.addView(layout,params);
        }
    }

    /**
     * 设置获取天数据还是周数据
     * @param day 1-天 7-周
     */
    public void getDataByDay(int day){
        mTvDay.setTextColor(getResources().getColor(R.color.text_col1));
        mTvWeek.setTextColor(getResources().getColor(R.color.text_col1));
        if (day==1){
            mTvDay.setTextColor(getResources().getColor(R.color.white));
            mCode = mDayCode;
        }else if (day==7){
            mTvWeek.setTextColor(getResources().getColor(R.color.white));
            mCode = mWeekCode;
        }
        mBandModel.getGiftData(mCode, 0, 50, new BeanCallback<BaseJson<List<GiftBean>>>(getActivity(),false,null) {
            @Override
            public void response(boolean success, BaseJson<List<GiftBean>> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        if (response.getData()!=null && response.getData().size()!=0){
                            mGiftList = response.getData();
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_list_day:
                getDataByDay(1);
                break;
            case R.id.tv_list_week:
                getDataByDay(7);
                break;
        }
    }

    class OnRankingListener implements View.OnClickListener{


        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            Log.e("@#",index+"");
        }
    }


}
