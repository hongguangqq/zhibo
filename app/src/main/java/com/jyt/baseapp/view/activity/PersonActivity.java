package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.MyGiftAdapter;
import com.jyt.baseapp.adapter.PagePropagandaAdapter;
import com.jyt.baseapp.adapter.VisitorAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.PersonBean;
import com.jyt.baseapp.bean.PropagationBean;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.itemDecoration.SpacesItemDecoration;
import com.jyt.baseapp.model.AppointModel;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.AppointModelImpl;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HawkUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.dialog.PersonDialog;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonActivity extends BaseMCVActivity {

    @BindView(R.id.vp_person_propaganda)
    ViewPager mVpPropaganda;
    @BindView(R.id.iv_person_more)
    ImageView mIvMore;
    @BindView(R.id.tv_person_name)
    TextView mTvName;
    @BindView(R.id.tv_person_money)
    TextView mTvMoney;
    @BindView(R.id.tv_person_focus)
    TextView mTvFocus;
    @BindView(R.id.tv_person_online)
    TextView mTvOnline;
    @BindView(R.id.ll_person_banner)
    LinearLayout mLlBanner;
    @BindView(R.id.iv_person_sex)
    ImageView mIvSex;
    @BindView(R.id.tv_person_work)
    TextView mTvWork;
    @BindView(R.id.tv_person_al)
    TextView mTvAl;
    @BindView(R.id.tv_person_ctime)
    TextView mTvCtime;
    @BindView(R.id.tv_person_sign)
    TextView mTvSign;
    @BindView(R.id.tv_person_gift)
    TextView mTvGift;
    @BindView(R.id.iv_person_yuyin)
    ImageView mIvYuyin;
    @BindView(R.id.rv_person_item)
    RecyclerView mRvItem;
    @BindView(R.id.rv_person_visitor)
    RecyclerView mRvVisitor;
    @BindView(R.id.ll_person_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.ll_person1)
    LinearLayout mLlPerson1;
    @BindView(R.id.ll_person2)
    LinearLayout mLlPerson2;
    @BindView(R.id.ll_person3)
    LinearLayout mLlPerson3;
    @BindView(R.id.ll_person4)
    LinearLayout mLlPerson4;
    @BindView(R.id.ll_person5)
    LinearLayout mLlPerson5;

    private PersonDialog mDialog;
    private IPhoneDialog mReportDialog;
    private VisitorAdapter mVisitorAdapter;
    private MyGiftAdapter mMyGiftAdapter;
    private PagePropagandaAdapter mPropagandaAdapter;
    private List<UserBean> mVisitorList;
    private List<PersonBean.GiftData> mItemList;
    private LinkedList<PropagationBean> mPropagandaList;
    private MediaPlayer mediaPlayer;
    private boolean isPlayAudio;
    private PersonModel mPersonModel;
    private AppointModel mAppointModel;
    private PersonBean mPersonData;
    private UserBean mUser;
    private int id;
    private int mPreviousPs;
    private List<String> mHourList;
    private List<ArrayList<String>> mMinuteList;
    private OptionsPickerView mTimePickerView;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Const.PLAY_COMPLETION:
                    break;
                case Const.PLAY_ERROR:
                    break;
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initSetting();
    }

    private void init() {
        HideActionBar();
        Tuple tuple = IntentHelper.PersonActivityGetPara(getIntent());
        id = (int) tuple.getItem1();
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(this);
        mAppointModel = new AppointModelImpl();
        mAppointModel.onStart(this);
        mDialog = new PersonDialog(this);
        mReportDialog = new IPhoneDialog(this);
        mReportDialog.setTitle("举报原因");
        mReportDialog.setInputShow(true);
        mReportDialog.setInputLine(4);
        mVisitorAdapter = new VisitorAdapter();
        mMyGiftAdapter = new MyGiftAdapter();
        mPropagandaList = new LinkedList<>();
        mHourList = new ArrayList<>();
        mMinuteList = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            mHourList.add(String.valueOf(i));
            ArrayList<String> list = new ArrayList<>();
            if (i!=24){
                for (int j = 0; j < 60; j++) {
                    list.add(String.valueOf(j));
                }
            }else {
                list.add(String.valueOf(0));
            }
            mMinuteList.add(list);
        }
        mVisitorList = new ArrayList<>();
        mItemList= new ArrayList<>();
        mPropagandaAdapter = new PagePropagandaAdapter(this, mPropagandaList);
        mTimePickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int hour = Integer.valueOf(mHourList.get(options1));
                int minute = Integer.valueOf(mMinuteList.get(options1).get(options2));
                int timelength = hour*60+minute;
                if (timelength>0){
                    mAppointModel.MakeAppointment(mUser.getId(), timelength, new BeanCallback<BaseJson>() {
                        @Override
                        public void response(boolean success, BaseJson response, int id) {
                            if (success && response.getCode()==200){
                                BaseUtil.makeText("预约成功");
                            }else {
                                BaseUtil.makeText("预约失败"+response.getMessage());
                            }
                        }
                    });
                }

            }}).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("预约")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLabels("小时","分钟","")
                .setLinkage(true)//设置是否联动，默认true
                .build();
        mTimePickerView.setPicker(mHourList,mMinuteList);

    }

    private void initSetting() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mRvVisitor.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvVisitor.addItemDecoration(new SpacesItemDecoration(BaseUtil.dip2px(10),4));
        mRvVisitor.setAdapter(mVisitorAdapter);
        mRvItem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvItem.addItemDecoration(new SpacesItemDecoration(BaseUtil.dip2px(10),4));
        mRvItem.setAdapter(mMyGiftAdapter);
        mDialog.setOnMenuClickListener(new PersonDialog.OnMenuClickListener() {
            @Override
            public void ClickMenu0() {


            }

            @Override
            public void ClickMenu1() {
                mPersonModel.PullBlack(mUser.getId(), new BeanCallback<BaseJson>() {
                    @Override
                    public void response(boolean success, BaseJson response, int id) {
                        if (success && response.getCode()==200){
                            BaseUtil.makeText("拉黑成功");
                            mDialog.dismiss();
                        }else {
                            BaseUtil.makeText("拉黑失败");
                        }
                    }
                });
            }

            @Override
            public void ClickMenu2() {
                //举报
                mReportDialog.setInputText("");
                mReportDialog.show();
            }

            @Override
            public void ClickMenu3() {
                //分享
            }
        });
        mReportDialog.setOnIPhoneClickListener(new IPhoneDialog.OnIPhoneClickListener() {
            @Override
            public void ClickSubmit(boolean isShow, String input) {
                if (TextUtils.isEmpty(input)){
                    BaseUtil.makeText("请填写内容");
                    return;
                }
                mPersonModel.ReportUser(mUser.getId(), input,new BeanCallback<BaseJson>() {
                    @Override
                    public void response(boolean success, BaseJson response, int id) {
                        if (success && response.getCode()==200){
                            BaseUtil.makeText("举报成功");
                            mReportDialog.dismiss();
                            mDialog.dismiss();
                        }else {
                            BaseUtil.makeText("举报失败");
                        }

                    }
                });
            }

            @Override
            public void ClickCancel() {
                mReportDialog.dismiss();
            }
        });
        mVisitorAdapter.setOnViewHolderClickListener(new BaseViewHolder.OnViewHolderClickListener() {
            @Override
            public void onClick(BaseViewHolder holder) {
                UserBean user = (UserBean) holder.getData();
                IntentHelper.OpenPersonActivity(PersonActivity.this,user.getId());
            }
        });

        getUserData();

    }


    private void getUserData(){
        mPersonModel.getOtherData(id, new BeanCallback<BaseJson<PersonBean>>() {
            @Override
            public void response(boolean success, BaseJson<PersonBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        mPersonData = response.getData();
                        mUser = mPersonData.getUser();
                        //异性显示
                        if (Const.getGender()!=mUser.getGender()){

                            if (Const.getGender()==1){
                                mLlPerson5.setVisibility(View.GONE);
                                mLlBottom.setVisibility(View.VISIBLE);
                            }else {
                                mLlPerson5.setVisibility(View.VISIBLE);
                                mLlBottom.setVisibility(View.GONE);
                            }
                        }


                        if (mUser.getGender()==1){
                            mIvSex.setImageResource(R.mipmap.icon_nan2);
                            mTvGift.setVisibility(View.GONE);

                        }else {
                            mIvSex.setImageResource(R.mipmap.icon_nv2);
                            mTvGift.setVisibility(View.VISIBLE);

                        }
                        Log.e("@#","time"+mPersonData.getTotal()/60);
                        mTvCtime.setText("本周聊天时长"+mPersonData.getWeek()/60+"小时 | 总时长 "+mPersonData.getTotal()/60+"小时");
                        mTvMoney.setText(mUser.getPrice()+"币/分钟");
                        mTvName.setText(mUser.getNickname());
                        if (!TextUtils.isEmpty(mUser.getProfession())){
                            mTvWork.setVisibility(View.VISIBLE);
                            mTvWork.setText(mUser.getProfession());
                        }
                        if (!TextUtils.isEmpty(mUser.getMark())){
                            mTvSign.setText(mUser.getMark());
                        }
                        mTvAl.setText(mUser.getAge()+"岁 "+mUser.getCityName());
                        if (mUser.getOnlineState()==1){
                            mTvOnline.setText("在线");
                        }else {
                            mTvOnline.setText("离线");
                        }
                        if (mPersonData.isFllow()){
                            mTvFocus.setText("已关注");
                        }else {
                            mTvFocus.setText("未关注");
                        }
                        mPropagandaList.clear();
                        for (String path:mUser.getImgsArray()){
                            mPropagandaList.add(new PropagationBean(false,path));
                        }
                        if (!TextUtils.isEmpty(mUser.getVideo())){
                            mPropagandaList.add(1,new PropagationBean(true,mUser.getVideo()));
                        }
                        mPropagandaAdapter.notifyData(mPropagandaList);
                        mVpPropaganda.setAdapter(mPropagandaAdapter);
                        mLlBanner.removeAllViews();
                        for (int i = 0; i < mPropagandaList.size(); i++) {
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

                        mVpPropaganda.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                if (!TextUtils.isEmpty(mUser.getVideo())){
                                    if (position!=1){
                                        mPropagandaAdapter.VideoStop();
                                    }else {
                                        mPropagandaAdapter.setVideoData(mPropagandaList.get(position).getPath());
                                    }
                                }
//                                position = position % mPropagandaList.size();
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
                        mVisitorList = mPersonData.getRecent();
                        mVisitorAdapter.notifyData(mVisitorList);
                        mItemList = mPersonData.getGiftCounts();
                        mMyGiftAdapter.notifyData(mItemList);
                    }
                }
            }
        });
    }


    @OnClick(R.id.tv_person_focus)
    public void TofollowUser(){
        if (mPersonData.isFllow()){
            //当前处于关注状态
            mPersonModel.CancelFollow(mUser.getId(), new BeanCallback<BaseJson>(PersonActivity.this,false,null) {
                @Override
                public void response(boolean success, BaseJson response, int id) {
                    if (success && response.getCode()==200){
                        mTvFocus.setText("未关注");
                        mPersonData.setFllow(false);
                        sendBroadcast( new Intent().setAction(Const.Reciver_Tab1));
                        HawkUtil.upFocusData(mUser.getId(),false);
                        //获取好友ID列表
//                        mPersonModel.getFocusIdList(new BeanCallback<BaseJson<List<UserBean>>>() {
//                            @Override
//                            public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
//                                if (success && response.getData()!=null){
//                                    HawkUtil.saveFocusList(response.getData());
//
//                                }
//                            }
//                        });
                    }
                }
            });
        }else {
            //当前处于未关注状态
            mPersonModel.ToFollow(mUser.getId(), new BeanCallback<BaseJson>(PersonActivity.this,false,null) {
                @Override
                public void response(boolean success, BaseJson response, int id) {
                    if (success && response.getCode()==200){
                        mTvFocus.setText("已关注");
                        mPersonData.setFllow(true);
                        sendBroadcast( new Intent().setAction(Const.Reciver_Tab1));
                        HawkUtil.upFocusData(mUser.getId(),true);
                        //获取好友ID列表
//                        mPersonModel.getFocusIdList(new BeanCallback<BaseJson<List<UserBean>>>() {
//                            @Override
//                            public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
//
//
//                                Log.e("UserBean","su="+success);
//                                if (response.getData()!=null){
//                                    Log.e("UserBean","response"+response.getData() .size());
//
//                                }else {
//                                    Log.e("UserBean"," null");
//
//                                }
//                               ;
//                                if (success && response.getData()!=null){
//                                    HawkUtil.saveFocusList(response.getData());
//
//                                }
//                            }
//                        });
                    }
                }
            });
        }

    }

    @OnClick({R.id.ll_person1,R.id.ll_person5})
    public void OpenCommunicationActivity(){
        IntentHelper.OpenCommunicationActivity(mUser.getId(),this);
    }

    @OnClick(R.id.ll_person2)
    public void setOrderTime(){
        mTimePickerView.show();
    }

    @OnClick(R.id.ll_person3)
    public void OpenLaunchActivityByVoice(){
        IntentHelper.OpenLaunchActivity(this,mUser.getId(),3,mUser.getNickname(),mUser.getHeadImg());
    }

    @OnClick(R.id.ll_person4)
    public void OpenLaunchActivityByVideo(){
        if (mUser.getAnchorState()!=2){
            BaseUtil.makeText("当前用户未认证");
            return;
        }
        IntentHelper.OpenLaunchActivity(this,mUser.getId(),2,mUser.getNickname(),mUser.getHeadImg());
    }

    @OnClick(R.id.iv_person_more)
    public void Duang(){
        mDialog.show();
    }

    @OnClick(R.id.iv_person_yuyin)
    public void PlayAudio(){
        if (!TextUtils.isEmpty(mUser.getVoice())){
            if (!isPlayAudio){
                startPlay(mUser.getVoice());
            }
        }else {
            BaseUtil.makeText("该用户没有语音留言");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer!=null){
            MediaPlayer mediaPlayer = mPropagandaAdapter.getMediaPlayer();
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPropagandaAdapter!=null){
            MediaPlayer mediaPlayer = mPropagandaAdapter.getMediaPlayer();
            if (mediaPlayer!=null && !TextUtils.isEmpty(mUser.getVideo())){
                mPropagandaAdapter.showPlayLogo();
                mPropagandaAdapter.setVideoData(mPropagandaList.get(1).getPath());
            }
        }

    }

    /**
     * @description 开始播放音频文件
     * @author ldm
     * @time 2017/2/9 16:56
     */
    private void startPlay(String path) {
        isPlayAudio=true;
        try {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
            //设置播放音频数据文件
            mediaPlayer.setDataSource(path);
            //设置播放监听事件
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    //播放完成
                    playEndOrFail(true);
                }
            });
            //播放发生错误监听事件
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    playEndOrFail(false);
                    return true;
                }
            });
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            //准备及播放
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            //播放失败正理
            playEndOrFail(false);
        }

    }

    /**
     * @description 停止播放或播放失败处理
     * @author ldm
     * @time 2017/2/9 16:58
     */
    private void playEndOrFail(boolean isEnd) {
        isPlayAudio = false;
        if (isEnd) {
            mHandler.sendEmptyMessage(Const.PLAY_COMPLETION);//停止播放
        } else {
            mHandler.sendEmptyMessage(Const.PLAY_ERROR);//失败
        }
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(null);
            mediaPlayer.setOnErrorListener(null);
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonModel.onDestroy();
        mAppointModel.onDestroy();
        mPropagandaAdapter.MediaPlayerDestory();
    }


}
