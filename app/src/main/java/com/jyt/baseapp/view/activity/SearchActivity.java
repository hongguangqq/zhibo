package com.jyt.baseapp.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jyt.baseapp.R;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.bean.BaseJson;
import com.jyt.baseapp.bean.SearchBean;
import com.jyt.baseapp.bean.SearchConditionBean;
import com.jyt.baseapp.helper.IntentHelper;
import com.jyt.baseapp.model.SearchModel;
import com.jyt.baseapp.model.impl.SearchModelImpl;
import com.jyt.baseapp.util.BaseUtil;
import com.jyt.baseapp.util.HawkUtil;
import com.jyt.baseapp.view.widget.CityPickerView;
import com.jyt.baseapp.view.widget.SearchView;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseMCVActivity {


    @BindView(R.id.sv_search)
    SearchView mSearch;
    @BindView(R.id.flow_search_history)
    FlowLayout mFlowHistory;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.tv_search_clear)
    TextView mTvClear;
    @BindView(R.id.tv_search_male)
    TextView mTvMale;
    @BindView(R.id.tv_search_female)
    TextView mTvFemale;
    @BindView(R.id.tv_search_sex)
    TextView mTvSex;
    @BindView(R.id.tv_search_age)
    TextView mTvAge;
    @BindView(R.id.ll_search_age)
    LinearLayout mLlAge;
    @BindView(R.id.tv_search_city)
    TextView mTvCity;
    @BindView(R.id.ll_search_city)
    LinearLayout mLlCity;
    @BindView(R.id.tv_search_money)
    TextView mTvMoney;
    @BindView(R.id.ll_search_money)
    LinearLayout mLlMoney;
    @BindView(R.id.tv_search_comm)
    TextView mTvComm;
    @BindView(R.id.ll_search_comm)
    LinearLayout mLlComm;
    @BindView(R.id.tv_search_time)
    TextView mTvTime;
    @BindView(R.id.ll_search_time)
    LinearLayout mLlTime;
    @BindView(R.id.btn_search)
    Button mBtnSearch;

    private List<String> mHistoryList;
    private ViewGroup.LayoutParams mTvParams;
    private OptionsPickerView.Builder mCityBuild;
    private OptionsPickerView mAgeOptions;
    private CityPickerView mCityOptions;
    private OptionsPickerView mMoneyOptions;
    private OptionsPickerView mTypeOptions;
    private OptionsPickerView mTimeOptions;
    private List<String> mAgeList;
    private List<String> mMoneyList;
    private List<String> mTypeList;
    private List<String> mTimeList;
    private String mStrAge;
    private String mStrCity;
    private String mStrMoney;
    private String mStrType;
    private String mStrTime;
    private String mStrSex;//关键词 1/男 2/女
    private SearchModel mSearchModel;
    private OnSearchKeyListener mSearchKeyListener;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
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
        mSearchModel = new SearchModelImpl();
        mSearchKeyListener = new OnSearchKeyListener();
        mSearchModel.onStart(this);
        mTvParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, BaseUtil.dip2px(34));
        mHistoryList = HawkUtil.getHistory();
        mAgeList = new ArrayList<>();
        mMoneyList = new ArrayList<>();
        mTypeList = new ArrayList<>();
        mTimeList = new ArrayList<>();
        mAgeList.add("不限");
        mTvMale.setTag(0);
        mTvFemale.setTag(1);
        mTvSex.setTag(2);
        updateHistory();
        for (int i = 23; i < 50; i++) {
            mAgeList.add(i+"");
        }
        mMoneyList = Arrays.asList(BaseUtil.getStringArray(R.array.money));
        mTypeList = Arrays.asList(BaseUtil.getStringArray(R.array.type));
        mTimeList = Arrays.asList(BaseUtil.getStringArray(R.array.time));
    }

    private void initSetting() {
        initOptions();
        //回车触发搜索按钮
//        mSearch.setOnSearchKeyDownListener(new SearchView.OnSearchKeyDownListener() {
//            @Override
//            public void over() {
//                mTvSearch.performClick();
//            }
//        });
    }

    private void initOptions(){
        //年龄
        mAgeOptions = new  OptionsPickerView.Builder(SearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                mTvAge.setText(mAgeList.get(options1));
                if (options1==0){
                    mStrAge=null;
                }else {
                    mStrAge = mAgeList.get(options1)+"";
                }

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("年龄")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        mAgeOptions.setPicker(mAgeList);
        //城市
        mCityOptions = new CityPickerView(this,new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvCity.setText(mCityOptions.getCity(options1,options2));

                if (options1==0){
                    mStrCity=null;
                }else {
                    mStrCity = mCityOptions.getCity(options1,options2);
                }
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true
                .setOutSideCancelable(true)//点击外部dismiss default true
        );
        //金币
        mMoneyOptions = new  OptionsPickerView.Builder(SearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                mTvMoney.setText(mMoneyList.get(options1));
                if (options1==0){
                    mStrMoney = null;
                }else {
                    mStrMoney = mMoneyList.get(options1);
                }

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("金币")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        mMoneyOptions.setPicker(mMoneyList);
        //聊天方式
        mTypeOptions = new  OptionsPickerView.Builder(SearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                mTvComm.setText(mTypeList.get(options1));
                if (options1==0){
                    mStrType = null;
                }else {
                    mStrType = mTypeList.get(options1);
                }
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("聊天方式")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        mTypeOptions.setPicker(mTypeList);
        //聊天时间
        mTimeOptions = new  OptionsPickerView.Builder(SearchActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                mTvTime.setText(mTimeList.get(options1));
                if (options1==0){
                    mStrTime = null;
                }else {
                    mStrTime = mTimeList.get(options1);
                }

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("聊天时间")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.picker_city))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(false)//设置是否联动，默认true
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .build();
        mTimeOptions.setPicker(mTimeList);
    }

    public void updateHistory(){
        mFlowHistory.removeAllViews();
        for (String hs : mHistoryList) {
            TextView tv_history = new TextView(this);
            tv_history.setTag(hs);
            tv_history.setText(hs);
            tv_history.setTextSize(10);
            tv_history.setOnClickListener(mSearchKeyListener);
            tv_history.setTextColor(getResources().getColor(R.color.text_col2));
            tv_history.setGravity(Gravity.CENTER);
            tv_history.setPadding(BaseUtil.dip2px(18), 0, BaseUtil.dip2px(18), 0);
            tv_history.setBackground(getResources().getDrawable(R.drawable.corner_g5));
            tv_history.setLayoutParams(mTvParams);
            tv_history.invalidate();
            mFlowHistory.addView(tv_history);
        }
    }

    class OnSearchKeyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            String searchText = (String) v.getTag();
            if (mHistoryList.contains(searchText)){
                mHistoryList.remove(searchText);
            }
            mHistoryList.add(0,searchText);
            updateHistory();
            SearchByKey(searchText);
        }
    }


    @OnClick(R.id.tv_search)
    public void Search(){
        String searchText = mSearch.getText();
        if (!TextUtils.isEmpty(searchText)){
            if (mHistoryList.contains(searchText)){
                mHistoryList.remove(searchText);
            }
            mHistoryList.add(0,searchText);
            updateHistory();
            SearchByKey(searchText);

        }
    }

    private void SearchByKey(String nick){
        final SearchConditionBean condition = new SearchConditionBean(nick);
        mSearchModel.SearchByKey(condition, new BeanCallback<BaseJson<SearchBean>>(this,false,null) {
            @Override
            public void response(boolean success, BaseJson<SearchBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        if (response.getData().getContent()==null || response.getData().getContent().size()==0){
                            BaseUtil.makeText("查无此人");
                        }else {
                            IntentHelper.OpenSearchResultActivity(SearchActivity.this,response.getData(),condition);
                        }

                    }
                }
            }
        });
    }

    @OnClick(R.id.tv_search_clear)
    public void ClaerHistory(){
        mHistoryList.clear();
        mFlowHistory.removeAllViews();
        HawkUtil.clearHistory();
    }

    @OnClick({R.id.tv_search_male,R.id.tv_search_female,R.id.tv_search_sex})
    public void ChoiceKey(View v){
        mTvMale.setBackground(getResources().getDrawable(R.drawable.corners_g5));
        mTvMale.setTextColor(getResources().getColor(R.color.white));
        mTvFemale.setTextColor(getResources().getColor(R.color.white));
        mTvFemale.setBackground(getResources().getDrawable(R.drawable.corners_g5));
        mTvSex.setBackground(getResources().getDrawable(R.drawable.corners_g5));
        mTvSex.setTextColor(getResources().getColor(R.color.white));
        int key = (int) v.getTag();
        switch (key){
            case 0:
                mStrSex = "1";
                mTvMale.setBackground(getResources().getDrawable(R.drawable.corner_w5));
                mTvMale.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                mStrSex = "2";
                mTvFemale.setBackground(getResources().getDrawable(R.drawable.corner_w5));
                mTvFemale.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                mStrSex = null;
                mTvSex.setBackground(getResources().getDrawable(R.drawable.corner_w5));
                mTvSex.setTextColor(getResources().getColor(R.color.black));
                break;
        }

    }

    @OnClick(R.id.ll_search_age)
    public void ChoiceAge(){
        mAgeOptions.show();
    }

    @OnClick(R.id.ll_search_city)
    public void ChoiceCity(){
        mCityOptions.show();
    }

    @OnClick(R.id.ll_search_money)
    public void ChoiceMoney(){
        mMoneyOptions.show();
    }

    @OnClick(R.id.ll_search_comm)
    public void ChoiceType(){
        mTypeOptions.show();
    }

    @OnClick(R.id.ll_search_time)
    public void ChoiceTime(){
        mTimeOptions.show();
    }

    @OnClick(R.id.btn_search)
    public void SearchByKey(){
        final SearchConditionBean condition = new SearchConditionBean(mStrSex,mStrAge,mStrCity,mStrMoney,mStrType,mStrTime);
        mSearchModel.SearchByKey(condition, new BeanCallback<BaseJson<SearchBean>>(this,false,null) {
            @Override
            public void response(boolean success, BaseJson<SearchBean> response, int id) {
                if (success){
                    if (response.getCode()==200){
                        if (response.getData().getContent()==null || response.getData().getContent().size()==0){
                            BaseUtil.makeText("查无此人");
                        }else {

                            IntentHelper.OpenSearchResultActivity(SearchActivity.this,response.getData(),condition);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchModel.onDestroy();
        HawkUtil.updateHistory(mHistoryList);

    }
}
