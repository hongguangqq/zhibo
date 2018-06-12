package com.jyt.baseapp.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.baseapp.model.BaseModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenweiqi on 2017/5/10.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    Unbinder unbinder;

    List<BaseModel> models;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView==null){
            rootView = inflater.from(getContext()).inflate(getLayoutId(),container,false);
            ButterKnife.bind(this,rootView);
            createModels(models);
            allModelsStart(getContext());
            firstInit();
        }
//        unbinder = ButterKnife.bind(this,rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (unbinder!=null){
//            unbinder.unbind();
//        }
        allModelsDestroy();

    }

    public void createModels(List<BaseModel> models){

    }

    public void allModelsStart(Context context){
        if (models !=null)
            for (BaseModel model:
                    models
                    ) {
                model.onStart(context);
            }
    }

    public void allModelsDestroy(){
        if (models !=null)
            for (BaseModel model:
                    models
                    ) {
                model.onDestroy();
            }
    }

    public void refresh(){

    }

    protected abstract int getLayoutId();

    protected abstract void firstInit();


}
