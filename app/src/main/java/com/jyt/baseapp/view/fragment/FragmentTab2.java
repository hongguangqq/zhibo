package com.jyt.baseapp.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.NewAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
import com.jyt.baseapp.model.TabModel;
import com.jyt.baseapp.model.impl.TabModelImpl;
import com.jyt.baseapp.view.widget.CircleImageView;
import com.jyt.baseapp.view.widget.MyRecycleView;
import com.xiaomi.mipush.sdk.MiPushMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LinWei on 2018/5/4 14:41
 */
public class FragmentTab2 extends BaseFragment {
    @BindView(R.id.ll_tab2_n1)
    LinearLayout mLlN1;
    @BindView(R.id.ll_tab2_n2)
    LinearLayout mLlN2;
    @BindView(R.id.ll_tab2_n3)
    LinearLayout mLlN3;
    @BindView(R.id.ll_tab2_n4)
    LinearLayout mLlN4;
    @BindView(R.id.ll_tab2_n5)
    LinearLayout mLlN5;
    @BindView(R.id.iv_tab2_system)
    CircleImageView mIvSystem;
    @BindView(R.id.tv_tab2_system)
    TextView mTvSystem;
    @BindView(R.id.tv_tab2_mark)
    TextView mTvMark;
    @BindView(R.id.rl_tab2_system)
    RelativeLayout mRlSystem;
    @BindView(R.id.rv_tab2_stranger)
    MyRecycleView mRvStranger;
    @BindView(R.id.tv_tab2_friendNum)
    TextView mTvFriendNum;
    @BindView(R.id.tv_tab2_VistorNum)
    TextView mTvVistorNum;


    private NewAdapter mAdapter;
    private List<String> mDataList;
    private TabModel mTabModel;
    public static int gFriendNewNum;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab2;
    }

    @Override
    protected void firstInit() {
        init();
        initSetting();
    }

    private void init() {
        mAdapter = new NewAdapter(2);
        mDataList = new ArrayList<>();
        mTabModel = new TabModelImpl();
        mTabModel.onStart(getActivity());
        EventBus.getDefault().register(this);
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
    }

    private void initSetting() {
        mRvStranger.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRvStranger.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 1, getResources().getColor(R.color.line_color2)));
        mAdapter.setDataList(mDataList);
        mRvStranger.setAdapter(mAdapter);
        if(gFriendNewNum!=0){
            mTvFriendNum.setVisibility(View.VISIBLE);
            mTvFriendNum.setText(String.valueOf(gFriendNewNum));//设置好友消息数量
        }
        mTabModel.getVistorNum(System.currentTimeMillis(), new BeanCallback<BaseJson<Integer>>() {
            @Override
            public void response(boolean success, BaseJson<Integer> response, int id) {
                if (success && response.getCode()==200 && response.getData()!=null){
                    mTvVistorNum.setVisibility(View.VISIBLE);
                    mTvVistorNum.setText(String.valueOf(response.getData()));
                }
            }
        });

    }

    @OnClick(R.id.ll_tab2_n1)
    public void OpenNewActivity1() {
        gFriendNewNum = 0;
        IntentHelper.OpenNewActivity(getActivity(), 1);
    }

    @OnClick(R.id.ll_tab2_n2)
    public void OpenNewActivity2() {
        IntentHelper.OpenNewActivity(getActivity(), 2);
    }

    @OnClick(R.id.ll_tab2_n3)
    public void OpenNewActivity3() {
        IntentHelper.OpenNewActivity(getActivity(), 3);
    }

    @OnClick(R.id.ll_tab2_n4)
    public void OpenNewActivity4() {
        IntentHelper.OpenNewActivity(getActivity(), 4);
    }

    @OnClick(R.id.ll_tab2_n5)
    public void OpenNewActivity5() {
        IntentHelper.OpenNewActivity(getActivity(), 5);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventOver(EventBean bean) {
        if (Const.Event_NewArrive.equals(bean.getCode())){
            gFriendNewNum++;
            mTvFriendNum.setText(String.valueOf(gFriendNewNum));
        } else if (Const.Event_SystemFirst.equals(bean.getCode())){
            MiPushMessage msg = (MiPushMessage) bean.getItem1();
            String content = msg.getDescription();
            //设置最新一条系统消息
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabModel.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    //    mPager = (ViewPager) findViewById(R.id.vp_tab1_news);
    //    final LinearLayout ll_banner= (LinearLayout) findViewById(R.id.ll_tab1_banner);
    //    final List<String> list = new ArrayList<>();
    //        list.add("!");
    //        list.add("!");
    //        list.add("!");
    //        list.add("!");
    //    PageBannerAdapter adapter = new PageBannerAdapter(this,list);
    //        mPager.setAdapter(adapter);
    //        for (int i = 0; i < list.size(); i++) {
    //        ImageView point=new ImageView(this);
    //        if (i==0){
    //            point.setImageResource(R.mipmap.indicator_selected);
    //        }else {
    //            point.setImageResource(R.mipmap.indicator_normal);
    //        }
    //        LinearLayout.LayoutParams params=
    //                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    //        if (i>0){
    //            params.leftMargin=3;
    //        }
    //        ll_banner.addView(point ,params);
    //
    //    }
    //        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    //        @Override
    //        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    //
    //        }
    //
    //        @Override
    //        public void onPageSelected(int position) {
    //            position=position%list.size();
    //            ImageView view= (ImageView) ll_banner.getChildAt(position);//取得孩子节点，强转为ImageView
    //            view.setImageResource(R.mipmap.indicator_selected);
    //            //上个点改为不选中
    //            ImageView priPoint= (ImageView) ll_banner.getChildAt(mPreviousPs);//取得上一个孩子节点，强转为ImageView
    //            priPoint.setImageResource(R.mipmap.indicator_normal);
    //            mPreviousPs=position;//重新记录被选中的点
    //        }
    //
    //        @Override
    //        public void onPageScrollStateChanged(int state) {
    //
    //        }
    //    });
    //    HomeHeadTask task=new HomeHeadTask();
    //        task.start();
    //
    //}
    //
    ////自动轮播
    //class HomeHeadTask implements Runnable{
    //
    //    public void start(){
    //        //移除所有的消息,以便清除其他消息的干扰
    //        BaseUtil.getHandle().removeCallbacks(null);
    //        BaseUtil.getHandle().postDelayed(this,3000);
    //    }
    //
    //    @Override
    //    public void run() {
    //        int currentItem=mPager.getCurrentItem();
    //        currentItem++;
    //        mPager.setCurrentItem(currentItem);
    //        //延迟3秒，继续发消息,实现内循环
    //        BaseUtil.getHandle().postDelayed(this, 3000);
    //    }
    //}
}
