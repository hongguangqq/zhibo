package com.jyt.baseapp.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.PageBannerAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.ListBean;
import com.jyt.baseapp.bean.ThemeBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.TabModel;
import com.jyt.baseapp.model.impl.TabModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.widget.SlFlowLayout;
import com.jyt.baseapp.view.widget.SwitchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LinWei on 2018/5/4 14:41
 */
public class FragmentTab1 extends BaseFragment {

    @BindView(R.id.tv_tab1_dynamic)
    TextView mTvDynamic;
    @BindView(R.id.sv_tab1_state)
    SwitchView mSvState;
    @BindView(R.id.tv_tab1_stateOn)
    TextView mTvStateOn;
    @BindView(R.id.tv_tab1_stateOff)
    TextView mTvStateOff;
    @BindView(R.id.vp_tab1_news)
    ViewPager mvpNews;
    @BindView(R.id.ll_tab1_banner)
    LinearLayout mLlBanner;
    @BindView(R.id.flow_tab1_focus)
    SlFlowLayout mFlowFocus;
    @BindView(R.id.flow_tab1_hot)
    SlFlowLayout mFlowHot;
    @BindView(R.id.flow_tab1_user)
    SlFlowLayout mFlowUser;
    @BindView(R.id.flow_tab1_recommend)
    SlFlowLayout mFlowRecommend;
    @BindView(R.id.flow_tab1_theme)
    SlFlowLayout mFlowTheme;
    @BindView(R.id.rl_tab1_search)
    RelativeLayout mRlSearch;
    @BindView(R.id.ll_tab1_search)
    LinearLayout mLlSearch;
    @BindView(R.id.ll_tab1_focus)
    LinearLayout mLlFocus;
    @BindView(R.id.ll_tab1_hot)
    LinearLayout mLlHot;
    @BindView(R.id.ll_tab1_user)
    LinearLayout mLlUser;
    @BindView(R.id.tv_tab1_user)
    TextView mTvUser;
    @BindView(R.id.iv_tab1_ulogo)
    ImageView mIvUser;
    @BindView(R.id.ll_tab1_recommend)
    LinearLayout mLlRecommend;
    @BindView(R.id.ll_tab1_theme)
    LinearLayout mLlTheme;
    @BindView(R.id.ll_tab1_hotParent)
    LinearLayout mLlHotParent;
    @BindView(R.id.ll_tab1_themeParent)
    LinearLayout mLlThemeParent;
    private boolean mWorking = true;

    private static final int HandLer_Net_Bunner=100;
    private static final int HandLer_Net_List=101;
    private static final int HandLer_Net_Theme=102;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandLer_Net_Bunner:
                    mVpList = (List<String>) msg.obj;
                    mBannerAdapter = new PageBannerAdapter(getActivity(), mVpList);
                    mvpNews.setAdapter(mBannerAdapter);
                    mvpNews.setCurrentItem(mVpList.size() * 10000);
                    for (int i = 0; i < mVpList.size(); i++) {
                        ImageView point = new ImageView(getActivity());
                        if (i == 0) {
                            point.setImageResource(R.mipmap.indicator_selected);
                        } else {
                            point.setImageResource(R.mipmap.indicator_normal);
                        }
                        LinearLayout.LayoutParams params =
                                new LinearLayout.LayoutParams(BaseUtil.dip2px(10), BaseUtil.dip2px(10));
                        if (i > 0) {
                            params.leftMargin = 5;
                        }
                        mLlBanner.addView(point, params);
                    }
                    mvpNews.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            position = position % mVpList.size();
                            ImageView view = (ImageView) mLlBanner.getChildAt(position);//取得孩子节点，强转为ImageView
                            view.setImageResource(R.mipmap.indicator_selected);
                            //上个点改为不选中
                            ImageView priPoint = (ImageView) mLlBanner.getChildAt(mPreviousPs);//取得上一个孩子节点，强转为ImageView
                            priPoint.setImageResource(R.mipmap.indicator_normal);
                            mPreviousPs = position;//重新记录被选中的点
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    HomeHeadTask task = new HomeHeadTask();
                    task.start();
                    break;
                case HandLer_Net_List:
                    List<ListBean> data = (List<ListBean>) msg.obj;
                    for(ListBean bean:data){
                        switch (bean.getType()){
                            case 1:
                                mFlowHot.removeAllViews();
                                setListData(bean,mFlowHot);
                                break;
                            case 2:
                                if (bean.getList()==null || bean.getList().size()==0){
                                    //隐藏关注列表，移动主题列表到第三位，保持其在可见第二位 注意先移除热门列表的下位规则，再添加活动列表的下位规则
                                    mLlFocus.setVisibility(View.GONE);
                                    RelativeLayout.LayoutParams paramsHot = (RelativeLayout.LayoutParams) mLlHotParent.getLayoutParams();
                                    RelativeLayout.LayoutParams paramsTheme = (RelativeLayout.LayoutParams) mLlThemeParent.getLayoutParams();
                                    paramsHot.removeRule(RelativeLayout.BELOW);
                                    paramsTheme.addRule(RelativeLayout.BELOW,mLlHotParent.getId());
                                    mLlHotParent.setLayoutParams(paramsHot);
                                    mLlThemeParent.setLayoutParams(paramsTheme);
                                }else {
                                    mLlFocus.setVisibility(View.VISIBLE);
                                }
                                mFlowFocus.removeAllViews();
                                setListData(bean,mFlowFocus);
                                break;
                            case 3:
                                mFlowRecommend.removeAllViews();
                                setListData(bean, mFlowRecommend);
                                break;
                        }
                        if (Const.getGender()==1){
                            if (bean.getType()==5){
                                mFlowUser.removeAllViews();
                                setListData(bean, mFlowUser);
                            }
                        }else if (Const.getGender()==2){
                            if (bean.getType()==4){
                                mFlowUser.removeAllViews();
                                setListData(bean, mFlowUser);
                            }
                        }

                    }
                    break;
                case HandLer_Net_Theme:
                    ThemeBean themeBean = (ThemeBean) msg.obj;
                    for (int i = 0; i < themeBean.getContent().size(); i++) {
                        ImageView iv = new ImageView(getActivity());
                        String imgs = themeBean.getContent().get(i).getImgs();
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (imgs==null){
                            Glide.with(getActivity()).load(R.mipmap.timg).into(iv);
                        }else {
                            Glide.with(getActivity()).load(imgs).error(R.mipmap.timg).into(iv);
                        }
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width_iv, BaseUtil.dip2px(140));
                        mFlowTheme.addView(iv, params);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private int width_iv;//列表Item的宽度
    private List<String> mVpList;
    private PageBannerAdapter mBannerAdapter;
    private int mPreviousPs;//上一个被选中的点
    private TabModel mTab1Model;
    private ImageClickListener mlistener;
    private boolean isFirst = true;
    private UpdataPicReceiver mReceiver;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tabcopy1;
    }

    @Override
    protected void firstInit() {
        init();
        initSetting();
    }


    private void init() {
        mVpList = new ArrayList<>();
        mlistener = new ImageClickListener();
        //---------------------------
        width_iv = (BaseUtil.getScannerWidth() - BaseUtil.dip2px(5)) / 2;
        mTab1Model = new TabModelImpl();
        mTab1Model.onStart(getActivity());
        if (Const.getOnLineState()==2){
            mSvState.open();
            mTvStateOff.setVisibility(View.VISIBLE);
            mTvStateOn.setVisibility(View.GONE);
        }else {
            mSvState.close();
            mTvStateOn.setVisibility(View.VISIBLE);
            mTvStateOff.setVisibility(View.GONE);
        }
        if (Const.getGender()==1){
            //男用户隐藏在线状态
            mRlSearch.setVisibility(View.GONE);
            mTvUser.setText("女用户列表");
            mIvUser.setImageResource(R.mipmap.icon_liebiao_suren);
        }else {
            mTvUser.setText("男用户列表");
            mIvUser.setImageResource(R.mipmap.icon_liebiao_nanyonghu);
        }
        mReceiver = new UpdataPicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.Reciver_Tab1);
        filter.setPriority(Integer.MAX_VALUE);
        getActivity().registerReceiver(mReceiver,filter);
    }

    private void initSetting() {
        mLlFocus.setTag(1);
        mLlHot.setTag(2);
        mLlUser.setTag(3);
        mLlRecommend.setTag(4);
        //在线状态切换
        mSvState.onChangeListener(new SwitchView.OnChangeListener() {
            @Override
            public void onChange(SwitchView switchView, boolean isOpened) {
                if (!isOpened) {
                    mTvStateOn.setVisibility(View.VISIBLE);
                    mTvStateOff.setVisibility(View.GONE);
                    Const.setUserOnLineState(1);
                    mTab1Model.editOnlineState(1, new BeanCallback() {
                        @Override
                        public void response(boolean success, Object response, int id) {

                        }
                    });
                } else {
                    mTvStateOff.setVisibility(View.VISIBLE);
                    mTvStateOn.setVisibility(View.GONE);
                    Const.setUserOnLineState(2);
                    mTab1Model.editOnlineState(2, new BeanCallback() {
                        @Override
                        public void response(boolean success, Object response, int id) {

                        }
                    });
                }
            }
        });
        mFlowFocus.setPadding(0, 0, 0, 0);
        mFlowFocus.setChildSpacing(BaseUtil.dip2px(5));
        mFlowHot.setPadding(0, 0, 0, 0);
        mFlowHot.setChildSpacing(BaseUtil.dip2px(5));
        mFlowUser.setPadding(0, 0, 0, 0);
        mFlowUser.setChildSpacing(BaseUtil.dip2px(5));
        mFlowRecommend.setPadding(0, 0, 0, 0);
        mFlowRecommend.setChildSpacing(BaseUtil.dip2px(5));
        mFlowTheme.setPadding(0, 0, 0, 0);
        mFlowTheme.setChildSpacing(BaseUtil.dip2px(5));

        /*基础设置完毕*/
        //----------轮播图设置-----------
        mTab1Model.getBunner(new BeanCallback<BaseJson<List<String>>>(getActivity(),false,null) {
            @Override
            public void response(boolean success, BaseJson<List<String>> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        List<String> BannerData = response.getData();
                        Message msg = new Message();
                        msg.what=HandLer_Net_Bunner;
                        msg.obj=BannerData;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        });
        //----------关注、热门、用户和推荐列表设置-----------
        mTab1Model.getAllList(new BeanCallback<BaseJson<List<ListBean>>>() {
            @Override
            public void response(boolean success, BaseJson<List<ListBean>> response, int id) {
                if(success){
                    if (response.getCode()==200){

                        List<ListBean> data = response.getData();
                        Message msg = new Message();
                        msg.what=HandLer_Net_List;
                        msg.obj=data;
                        mHandler.sendMessage(msg);
                    }
                }

            }
        });
        //----------主题设置-----------
        mTab1Model.getThemeList(0, 3, new BeanCallback<BaseJson<ThemeBean>>() {
            @Override
            public void response(boolean success, BaseJson<ThemeBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        ThemeBean data = response.getData();
                        Message message = new Message();
                        message.what = HandLer_Net_Theme;
                        message.obj = data;
                        mHandler.sendMessage(message);
                    }
                }
            }
        });

    }


    /**
     * 设置列表数据
     *
     * @param index
     * @param flow
     */
    public void setListData(int index, SlFlowLayout flow) {

        for (int i = 0; i < index; i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setBackground(getResources().getDrawable(R.mipmap.timg));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width_iv, BaseUtil.dip2px(140));
            flow.addView(iv, params);
        }
    }

    public void setListData(ListBean data, SlFlowLayout flow){
        for (int i = 0; i < data.getList().size(); i++) {
            ImageView iv = new ImageView(getActivity());
            List<String> imgs = data.getList().get(i).getImgsArray();
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setOnClickListener(mlistener);
            if (imgs==null || imgs.size()==0){
                Glide.with(getActivity()).load(R.mipmap.timg).into(iv);
            }else {
                Glide.with(getActivity()).load(imgs.get(0)).error(R.mipmap.timg).into(iv);
            }
            iv.setTag(data.getList().get(i).getId());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width_iv, BaseUtil.dip2px(140));
            flow.addView(iv, params);
        }
    }

    @OnClick(R.id.tv_tab1_dynamic)
    public void OpenDynamicActivity(){
        IntentHelper.OpenDynamicActivity(getActivity());

    }

    @OnClick(R.id.ll_tab1_search)
    public void OpenSearchActivity(View v) {
        IntentHelper.OpenSearchActivity(getActivity());
    }

    @OnClick({R.id.ll_tab1_focus,R.id.ll_tab1_hot,R.id.ll_tab1_user,R.id.ll_tab1_recommend})
    public void OpenListActivity(View v){
        int code = (int) v.getTag();
        IntentHelper.OpenListActivity(getActivity(),code);
//        IntentHelper.OpenAudienceActivity(getActivity());
    }

    @OnClick(R.id.ll_tab1_theme)
    public void OpenThemeActivity(){
        IntentHelper.OpenThemeActivity(getActivity());
//        IntentHelper.OpenLivePlayActivity(getActivity());
    }


    class ImageClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            int id = (Integer) v.getTag();
            IntentHelper.OpenPersonActivity(getActivity(),id);
        }
    }


    //自动轮播
    class HomeHeadTask implements Runnable {

        public void start() {
            //移除所有的消息,以便清除其他消息的干扰
            BaseUtil.getHandle().removeCallbacks(null);
            BaseUtil.getHandle().postDelayed(this, 3000);
        }

        @Override
        public void run() {
            if (mWorking){
                int currentItem = mvpNews.getCurrentItem();
                currentItem++;
                mvpNews.setCurrentItem(currentItem);
                //延迟3秒，继续发消息,实现内循环
                BaseUtil.getHandle().postDelayed(this, 3000);
            }
        }
    }




    class UpdataPicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Const.Reciver_Tab1.equals(intent.getAction())){
                mTab1Model.getAllList(new BeanCallback<BaseJson<List<ListBean>>>() {
                    @Override
                    public void response(boolean success, BaseJson<List<ListBean>> response, int id) {
                        if(success){
                            if (response.getCode()==200){

                                List<ListBean> data = response.getData();
                                Message msg = new Message();
                                msg.what=HandLer_Net_List;
                                msg.obj=data;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTab1Model.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
        if (mWorking){
            mWorking=false;
        }
    }
}
