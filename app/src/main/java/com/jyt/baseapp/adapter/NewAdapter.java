package com.jyt.baseapp.adapter;

import android.view.ViewGroup;

import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.NewViewHolder1;
import com.jyt.baseapp.view.viewholder.NewViewHolder2;
import com.jyt.baseapp.view.viewholder.NewViewHolder3;
import com.jyt.baseapp.view.viewholder.NewViewHolder4;
import com.jyt.baseapp.view.viewholder.NewViewHolder5;

/**
 * @author LinWei on 2018/5/21 17:28
 */
public class NewAdapter  extends BaseRcvAdapter{

    private int code;
    public NewAdapter(int code){
        this.code = code;
    }

    @Override
    BaseViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {

        switch (code){
            case 1:
                NewViewHolder1 holder1 = new NewViewHolder1(parent);
                return holder1;
            case 2:
                NewViewHolder2 holder2 = new NewViewHolder2(parent);
                holder2.setOnOpenVideoListener(mOpenVideoListener);
                return holder2;
            case 3:
                NewViewHolder3 holder3 = new NewViewHolder3(parent);
                return holder3;
            case 4:
                NewViewHolder4 holder4 = new NewViewHolder4(parent);
                return holder4;
            case 5:
                NewViewHolder5 holder5 = new NewViewHolder5(parent);
                return holder5;
        }
        return null;
    }

    private NewViewHolder2.OnOpenVideoListener mOpenVideoListener;
    public void setsetOnOpenVideoListener(NewViewHolder2.OnOpenVideoListener listener){
        this.mOpenVideoListener = listener;
    }
}
