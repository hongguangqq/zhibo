package com.jyt.baseapp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jyt.baseapp.R;
import com.jyt.baseapp.adapter.AccountAdapter;
import com.jyt.baseapp.api.BeanCallback;
import com.jyt.baseapp.itemDecoration.RecycleViewDivider;
import com.jyt.baseapp.model.WalletModel;
import com.jyt.baseapp.model.impl.WalletModelImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class AccountActivity extends BaseMCVActivity {

    @BindView(R.id.rv_account)
    RecyclerView mRvContent;

    private AccountAdapter mAccountAdapter;
    private List<String> mDataList;
    private WalletModel mWalletModel;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
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
        setTextTitle("明细");
        setvMainBackgroundColor(R.color.bg_content);
        mWalletModel = new WalletModelImpl();
        mWalletModel.onStart(this);
        mAccountAdapter = new AccountAdapter();
        mDataList = new ArrayList<>();
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
        mDataList.add("");
    }

    private void initSetting() {
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvContent.addItemDecoration(new RecycleViewDivider(getActivity(),LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.line_color2)));
        mAccountAdapter.setDataList(mDataList);
        mRvContent.setAdapter(mAccountAdapter);
        mWalletModel.getWalletAccount(0, 6, new BeanCallback() {
            @Override
            public void response(boolean success, Object response, int id) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWalletModel.onDestroy();
    }
}
