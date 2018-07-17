package com.jyt.baseapp.adapter;

import android.text.TextUtils;
import android.view.ViewGroup;

import com.jyt.baseapp.bean.BarrageBean;
import com.jyt.baseapp.view.viewholder.BarrageImgViewHolder;
import com.jyt.baseapp.view.viewholder.BarrageTextViewHolder;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;

/**
 * @author LinWei on 2018/7/17 19:33
 */
public class BarrageAdapter extends BaseRcvAdapter {
    private final int Barrage_Text = 1;
    private final int Barrage_Img = 2;

    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Barrage_Text){
            return new BarrageTextViewHolder(parent);
        }else if(viewType == Barrage_Img){
            return new BarrageImgViewHolder(parent);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        BarrageBean bean = (BarrageBean) dataList.get(position);
        if (TextUtils.isEmpty(bean.getImg())){
            //文字弹幕
            return Barrage_Text;
        }else {
            //图片弹幕
            return Barrage_Img;
        }

    }
}
