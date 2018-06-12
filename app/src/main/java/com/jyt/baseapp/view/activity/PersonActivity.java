package com.jyt.baseapp.view.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.ItemAdapter;
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
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.IPhoneDialog;
import com.jyt.baseapp.view.dialog.PersonDialog;

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

    private PersonDialog mDialog;
    private IPhoneDialog mReportDialog;
    private VisitorAdapter mVisitorAdapter;
    private ItemAdapter mItemAdapter;
    private PagePropagandaAdapter mPropagandaAdapter;
    private List<UserBean> mVisitorList;
    private List<PersonBean.GiftData> mItemList;
    private LinkedList<PropagationBean> mPropagandaList;
    private MediaPlayer mediaPlayer;
    private boolean isPlayAudio;
    private PersonModel mPersonModel;
    private PersonBean mPersonData;
    private UserBean mUser;
    private int id;
    private int mPreviousPs;

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
        mDialog = new PersonDialog(this);
        mReportDialog = new IPhoneDialog(this);
        mReportDialog.setTitle("举报原因");
        mReportDialog.setInputShow(true);
        mReportDialog.setInputLine(4);
        mVisitorAdapter = new VisitorAdapter();
        mItemAdapter = new ItemAdapter();
        mPropagandaList = new LinkedList<>();


//        mPropagandaList.add(new PropagationBean(false,"http://asset.nos-eastchina1.126.net/test/31/469b73aca39e212d30dfcea4a3e6d96b.jpg"));
//        mPropagandaList.add(new PropagationBean(true,"http://asset.nos-eastchina1.126.net/test/31/4782.mp4"));
//        mPropagandaList.add(new PropagationBean(false,"http://asset.nos-eastchina1.126.net/test/31/469b73aca39e212d30dfcea4a3e6d96b.jpg"));
//        mPropagandaList.add(new PropagationBean(false,"http://asset.nos-eastchina1.126.net/test/31/469b73aca39e212d30dfcea4a3e6d96b.jpg"));
        mPropagandaAdapter = new PagePropagandaAdapter(this, mPropagandaList);
        mVisitorList = new ArrayList<>();
        mItemList= new ArrayList<>();

    }

    private void initSetting() {
        mRvVisitor.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvVisitor.addItemDecoration(new SpacesItemDecoration(BaseUtil.dip2px(10),4));
        mRvItem.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mRvItem.addItemDecoration(new SpacesItemDecoration(BaseUtil.dip2px(10),4));
        mRvVisitor.setAdapter(mVisitorAdapter);
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

        getUserData();

    }

    private void getUserData(){
        mPersonModel.getOtherData(id, new BeanCallback<BaseJson<PersonBean>>(this,false,null) {
            @Override
            public void response(boolean success, BaseJson<PersonBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        mPersonData = response.getData();
                        mUser = mPersonData.getUser();
                        //异性显示
                        if (Const.getGender()!=mUser.getGender()){
                            mLlBottom.setVisibility(View.VISIBLE);
                        }
                        if (mUser.getGender()==1){
                            mIvSex.setImageResource(R.mipmap.icon_nan2);
                        }else {
                            mIvSex.setImageResource(R.mipmap.icon_nv2);
                        }
                        mTvMoney.setText(mUser.getPrice()+"币/分钟");
                        mTvName.setText(mUser.getNickname());
                        if (!TextUtils.isEmpty(mUser.getProfession())){
                            mTvWork.setVisibility(View.VISIBLE);
                            mTvWork.setText(mUser.getProfession());
                        }
                        if (!TextUtils.isEmpty(mUser.getMark())){
                            mTvSign.setText(mUser.getMark());
                        }
                        mTvAl.setText(mUser.getAge()+" "+mUser.getCityName());
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
                        mItemAdapter.notifyData(mItemList);
                    }
                }
            }
        });
    }


    @OnClick(R.id.tv_person_focus)
    public void TofollowUser(){
        if (mPersonData.isFllow()){
            //当前处于关注状态，取消关注
            mPersonModel.CancelFollow(mUser.getId(), new BeanCallback<BaseJson>(PersonActivity.this,false,null) {
                @Override
                public void response(boolean success, BaseJson response, int id) {
                    if (success && response.getCode()==200){
                        mTvFocus.setText("未关注");
                        mPersonData.setFllow(false);
                        sendBroadcast( new Intent().setAction(Const.Reciver_CODE1));
                    }
                }
            });
        }else {
            //当前处于未关注状态，关注
            mPersonModel.ToFollow(mUser.getId(), new BeanCallback<BaseJson>(PersonActivity.this,false,null) {
                @Override
                public void response(boolean success, BaseJson response, int id) {
                    if (success && response.getCode()==200){
                        mTvFocus.setText("已关注");
                        mPersonData.setFllow(true);
                        sendBroadcast( new Intent().setAction(Const.Reciver_CODE1));
                    }
                }
            });
        }

    }

    @OnClick(R.id.ll_person1)
    public void OpenCommunicationActivity(){
        IntentHelper.OpenCommunicationActivity(mUser.getId(),this);
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
        MediaPlayer mediaPlayer = mPropagandaAdapter.getMediaPlayer();
        if (mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayer mediaPlayer = mPropagandaAdapter.getMediaPlayer();
        if (mediaPlayer!=null && !TextUtils.isEmpty(mUser.getVideo())){
            mPropagandaAdapter.showPlayLogo();
            mPropagandaAdapter.setVideoData(mPropagandaList.get(1).getPath());
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
        mPropagandaAdapter.MediaPlayerDestory();
    }


}
