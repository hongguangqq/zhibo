package com.jyt.baseapp.view.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.StrangerAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.EventBean;
import com.jyt.baseapp.bean.FriendNewsBean;
import com.jyt.baseapp.bean.PushMessageBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
import com.jyt.baseapp.model.TabModel;
import com.jyt.baseapp.model.impl.TabModelImpl;
import com.jyt.baseapp.util.HawkUtil;
import com.jyt.baseapp.view.viewholder.StrangerViewHolder;
import com.jyt.baseapp.view.widget.CircleImageView;
import com.jyt.baseapp.view.widget.MyRecycleView;
import com.xiaomi.mipush.sdk.MiPushMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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


    private StrangerAdapter mAdapter;
    private List<FriendNewsBean> mDataList;
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
        mAdapter = new StrangerAdapter();
        mDataList = HawkUtil.getStrangerList();
        mTabModel = new TabModelImpl();
        mTabModel.onStart(getActivity());
        EventBus.getDefault().register(this);

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
                    int num = response.getData();
                    if (num!=0){
                        mTvVistorNum.setVisibility(View.VISIBLE);
                        mTvVistorNum.setText(String.valueOf(num));
                    }
                }
            }
        });
        List<PushMessageBean> pushMessageList = HawkUtil.getPushList();
        if (pushMessageList != null && pushMessageList.size()>0){
            mTvMark.setText(pushMessageList.get(0).getMiPushMessage().getDescription());
        }else {
            mTvMark.setText("暂无消息");
        }

        mAdapter.setsetOnOpenVideoListener(new StrangerViewHolder.OnOpenVideoListener() {
            @Override
            public void openVideo(FriendNewsBean user) {
                IntentHelper.OpenLaunchActivity(getActivity(),user.getId(),2, Const.getUserNick(),Const.getUserHeadImg());
            }

            @Override
            public void openCom(FriendNewsBean user) {
                IntentHelper.OpenCommunicationActivity(user.getId(),getActivity());
            }
        });

    }

    @OnClick(R.id.ll_tab2_n1)
    public void OpenNewActivity1() {
        gFriendNewNum = 0;
        mTvFriendNum.setVisibility(View.GONE);
        IntentHelper.OpenNewActivity(getActivity(), 1);
    }

    @OnClick(R.id.ll_tab2_n2)
    public void OpenNewActivity2() {
        mTvVistorNum.setVisibility(View.GONE);
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
            Log.e("@#","好友消息："+gFriendNewNum);
            gFriendNewNum++;
            mTvFriendNum.setText(String.valueOf(gFriendNewNum));
            mTvFriendNum.setVisibility(View.VISIBLE);
        } else if (Const.Event_SystemFirst.equals(bean.getCode())){
            MiPushMessage msg = (MiPushMessage) bean.getItem1();
            String content = msg.getDescription();
            //设置最新一条系统消息
            mTvMark.setText(content);
        }else if (Const.Event_StrangeArrive.equals(bean.getCode())){
            //陌生人消息到来
            mDataList = HawkUtil.getStrangerList();
            mAdapter.notifyData(mDataList);
        }else if (Const.Event_NewUpData.equals(bean.getCode())){
            //好友列表发生变动
            mDataList = HawkUtil.getStrangerList();
            mAdapter.notifyData(mDataList);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabModel.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
