package com.jyt.baseapp.view.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.Tuple;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.LiveModel;
import com.jyt.baseapp.model.impl.LiveModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.FastBlurUtil;
import com.jyt.baseapp.view.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MateActivity extends BaseMCVActivity {

    @BindView(R.id.iv_answer_bg)
    ImageView mIvBg;
    @BindView(R.id.iv_mate_s1)
    CircleImageView mIvS1;
    @BindView(R.id.iv_mate_s2)
    CircleImageView mIvS2;
    @BindView(R.id.iv_mate_s3)
    CircleImageView mIvS3;
    @BindView(R.id.iv_mate_s4)
    CircleImageView mIvS4;
    @BindView(R.id.iv_mate_s5)
    CircleImageView mIvS5;
    @BindView(R.id.iv_mate_s6)
    CircleImageView mIvS6;
    @BindView(R.id.iv_mate_s7)
    CircleImageView mIvS7;
    @BindView(R.id.iv_mate_s8)
    CircleImageView mIvS8;
    @BindView(R.id.iv_mate_s9)
    CircleImageView mIvS9;
    @BindView(R.id.btn_mate)
    Button mBtnMate;

    private int progressCode;
    private final int STATE_CODE_REFRESH = 1;
    private final int STATE_CODE_TIMEOUT = 2;
    private List<CircleImageView> mImgList;
    private List<Tuple> mNumberList;
    private List<UserBean> mUserList;
    private boolean isFirst=true;
    private int index;
    private LiveModel mLiveModel;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case STATE_CODE_REFRESH:
                    Thread thread = new Thread(new MateThread());
                    thread.start();
                    break;
                case STATE_CODE_TIMEOUT:
                    break;
            }
            return false;
        }
    });


    @Override
    protected int getLayoutId() {
        return R.layout.activity_mate;
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
        setvMainBackground(R.mipmap.bg_entrance);
        mLiveModel = new LiveModelImpl();
        mLiveModel.onStart(this);
        mNumberList = new ArrayList<>();
        mUserList = new ArrayList<>();
        mImgList = new ArrayList<>();
        mImgList.add(mIvS1);
        mImgList.add(mIvS2);
        mImgList.add(mIvS3);
        mImgList.add(mIvS4);
        mImgList.add(mIvS5);
        mImgList.add(mIvS6);
        mImgList.add(mIvS7);
        mImgList.add(mIvS8);
        mImgList.add(mIvS9);
    }

    private void initSetting() {
        Resources rs = getResources();
        Bitmap scaledBitmap = BitmapFactory.decodeResource(rs, R.mipmap.timg);
        Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 5);
        mIvBg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mIvBg.setImageBitmap(blurBitmap);
        mLiveModel.RandomDialing(new BeanCallback<BaseJson<List<UserBean>>>() {
            @Override
            public void response(boolean success, BaseJson<List<UserBean>> response, int id) {
                if (success && response.getCode()==200){
                    mUserList = response.getData();

                }
            }
        });

        mIvBg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < mImgList.size(); i++) {
                    CircleImageView iv = mImgList.get(i);
                    RelativeLayout.LayoutParams par= (RelativeLayout.LayoutParams) iv.getLayoutParams();
                    Log.e("@#","left:"+par.leftMargin+" top:"+par.topMargin);
                    Tuple tuple = new Tuple(par.leftMargin,par.topMargin,par.rightMargin,par.bottomMargin);
                    mNumberList.add(tuple);
                    iv.setTag(tuple);
                }
                Message msg = new Message();
                msg.what= STATE_CODE_REFRESH;
                mHandler.sendMessage(msg);
                mIvBg.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });





    }

    @OnClick(R.id.btn_mate)
    public void Mate(){
        mBtnMate.setVisibility(View.GONE);
        index = 0;
        ShowOrHideIv();
        ShowOrHideIvRandom();
        Thread thread = new Thread(new MateThread());
        thread.start();
    }

    /**
     * 改动位置出现
     */
    private void ShowOrHideIv(){
        for (int i = 0; i < mImgList.size(); i++) {
            CircleImageView v = mImgList.get(i);
            int num1 = (int) ((BaseUtil.dip2px(15)*Math.random())*(Math.random()>0.5?1:-1));
            int num2 = (int) ((BaseUtil.dip2px(20)*Math.random())*(Math.random()>0.5?1:-1));
            Tuple tuple = (Tuple) v.getTag();
            int l = (Integer)tuple.getItem1()+num2;
            int t = (Integer)tuple.getItem2()+num1;
            int r = (Integer)tuple.getItem3()-num2;
            int b = (Integer)tuple.getItme4()-num1;
            Log.e("@#","l2:"+l+" t2:"+t+"----");
            RelativeLayout.LayoutParams par= (RelativeLayout.LayoutParams) v.getLayoutParams();
            par.setMargins(l,t,r,b);
            v.setLayoutParams(par);
            final int finalI = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IntentHelper.OpenLaunchActivity(MateActivity.this,mUserList.get(0).getId(),2,mUserList.get(0).getNickname(),mUserList.get(0).getHeadImg());
                    finish();
                }
            });
        }
    }

    /**
     * 随机选择三个不显示
     */
    private void ShowOrHideIvRandom(){
        int n=0;
        List<Integer> list = new ArrayList<>();
        while (n<2){
            int t = (int) (Math.random()*9);
            if (!list.contains(t)){
                list.add(t);
                n++;
            }
        }
        int r1 = list.get(0);
        int r2 = list.get(1);
        for (int i = 0; i < mImgList.size(); i++) {
            if (i==r1 || i==r2){
                mImgList.get(i).setVisibility(View.INVISIBLE);
            }else {
                mImgList.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    class MateThread implements Runnable{

        @Override
        public void run() {
            while (progressCode<7){
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ShowOrHideIv();
                            ShowOrHideIvRandom();
                            index++;
                            progressCode++;
                        }
                    });
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //重置
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressCode=0;
                    mBtnMate.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private View.OnClickListener OnImgClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLiveModel.onDestroy();
    }
}
