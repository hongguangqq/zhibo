package com.jyt.baseapp.view.fragment;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.UserBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.PersonModel;
import com.jyt.baseapp.model.WalletModel;
import com.jyt.baseapp.model.impl.PersonModelImpl;
import com.jyt.baseapp.model.impl.WalletModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.view.dialog.PriceDialog;
import com.jyt.baseapp.view.widget.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author LinWei on 2018/5/4 14:41
 */
public class FragmentTab4 extends BaseFragment {
    @BindView(R.id.iv_tab4_hpic)
    CircleImageView mIvHpic;
    @BindView(R.id.tv_tab4_au)
    TextView mTvAu;
    @BindView(R.id.tv_tab4_name)
    TextView mTvName;
    @BindView(R.id.tv_tab4_setting)
    TextView mTvSetting;
    @BindView(R.id.tv_tab4_modify)
    TextView mTvModify;
    @BindView(R.id.tv_tab4_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_tab4_follow)
    TextView mTvFollow;
    @BindView(R.id.tv_tab4_fans)
    TextView mTvFans;
    @BindView(R.id.ll_tab4_follow)
    LinearLayout mLlFollow;
    @BindView(R.id.ll_tab4_fans)
    LinearLayout mLlFans;
    @BindView(R.id.ll_tab4_l1)
    LinearLayout mLL1;
    @BindView(R.id.ll_tab4_l2)
    LinearLayout mLL2;
    @BindView(R.id.ll_tab4_l3)
    LinearLayout mLL3;
    @BindView(R.id.ll_tab4_l4)
    LinearLayout mLL4;
    @BindView(R.id.ll_tab4_l5)
    LinearLayout mLL5;
    @BindView(R.id.ll_tab4_l6)
    LinearLayout mLL6;

    private PersonModel mPersonModel;
    private WalletModel mWalletModel;
    private PriceDialog mPriceDialog;
    private boolean isFirst = true;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab4;
    }


    @Override
    protected void firstInit() {
        init();
        initSetting();
    }

    private void init(){
        mPersonModel = new PersonModelImpl();
        mPersonModel.onStart(getActivity());
        mWalletModel = new WalletModelImpl();
        mWalletModel.onStart(getActivity());
        mPriceDialog = new PriceDialog(getActivity());

    }

    private void initSetting(){
        mPriceDialog.setOnProgressListener(new PriceDialog.OnPriceClickListener() {
            @Override
            public void Submit(float price) {
                mPersonModel.setPrice(price, new BeanCallback<BaseJson>() {
                    @Override
                    public void response(boolean success, BaseJson response, int id) {
                        if (success && response.getCode()==200){
                            BaseUtil.makeText("设置成功");
                        }else {
                            BaseUtil.makeText("设置失败");
                        }
                        mPriceDialog.dismiss();

                    }
                });
            }
        });
        setConstData();

    }

    public void setConstData(){
        mPersonModel.getMyUserData(new BeanCallback<BaseJson<UserBean>>(getActivity(),false,null) {
            @Override
            public void response(boolean success, BaseJson<UserBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        UserBean user = response.getData();
                        Glide.with(BaseUtil.getContext()).load(user.getHeadImg()).error(R.mipmap.timg).into(mIvHpic);
                        mTvName.setText(user.getNickname());
                        if (user.getAnchorState()==0){
                            mTvAu.setText("未认证");
                        }else if (Const.getAnchorState()==1){
                            mTvAu.setText("认证中");
                        }else if (Const.getAnchorState()==2){
                            mTvAu.setText("已认证");
                        }else if (Const.getAnchorState()==3){
                            mTvAu.setText("认证失败");
                        }
                        mPriceDialog.setCurrent((int) user.getPrice());
                    }
                }
            }
        });

    }

    @OnClick(R.id.ll_tab4_follow)
    public void OpenFfActivityByFollow(){
        IntentHelper.OpenFFActivit(getActivity(),0);
    }

    @OnClick(R.id.ll_tab4_fans)
    public void OpenFfActivityByFans(){
        IntentHelper.OpenFFActivit(getActivity(),1);
    }


    @OnClick(R.id.tv_tab4_modify)
    public void OpenModifyActivity(){
        IntentHelper.OpenModifyActivity(getActivity());
    }

    @OnClick(R.id.tv_tab4_setting)
    public void OpenSettingActivity(){
        IntentHelper.OpenSettingActivity(getActivity());
    }

    @OnClick(R.id.ll_tab4_l2)
    public void OpenWalletActivity(){
        IntentHelper.OpenWalletActivity(getActivity());
    }

    @OnClick(R.id.ll_tab4_l3)
    public void OpenThemeActivity(){
        IntentHelper.OpenThemeActivity(getActivity());
    }

    @OnClick(R.id.ll_tab4_l4)
    public void OpenDynamicActivity(){
        IntentHelper.OpenReleaseActivityForResult(getActivity());
    }
    @OnClick(R.id.ll_tab4_l5)
    public void setPrice(){
        mPriceDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst){
            mWalletModel.getBalance(new BeanCallback<BaseJson<Double>>() {
                @Override
                public void response(boolean success, BaseJson<Double> response, int id) {
                    mTvBalance.setText(String.valueOf(response.getData()));
                }
            });

            mPersonModel.getFansCount(new BeanCallback<BaseJson<Integer>>() {
                @Override
                public void response(boolean success, BaseJson<Integer> response, int id) {
                    mTvFans.setText(String.valueOf(response.getData()));
                }
            });

            mPersonModel.getFollowCount(new BeanCallback<BaseJson<Integer>>() {
                @Override
                public void response(boolean success, BaseJson<Integer> response, int id) {
                    mTvFollow.setText(String.valueOf(response.getData()));
                }
            });
        }else {
            isFirst=false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPersonModel.onDestroy();
        mWalletModel.onDestroy();
    }
}
