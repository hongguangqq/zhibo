package com.jyt.baseapp.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/5/10.
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    T data;
    View itemView;


    OnViewHolderClickListener onViewHolderClickListener;


    OnViewHolderClickListener onViewHolderLongClickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onViewHolderClickListener!=null){
                    onViewHolderClickListener.onClick(BaseViewHolder.this);
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onViewHolderLongClickListener!=null){
                    onViewHolderLongClickListener.onClick(BaseViewHolder.this);
                    return true;
                }
                return false;
            }
        });
    }

    public void setData(T data){
        this.data = data;
    }

    public T getData(){
        return data;
    }

    public BaseViewHolder getHolder(){
        return this;
    }

    public void setOnViewHolderLongClickListener(OnViewHolderClickListener onViewHolderLongClickListener) {
        this.onViewHolderLongClickListener = onViewHolderLongClickListener;
    }

    public void setOnViewHolderClickListener(OnViewHolderClickListener onViewHolderClickListener) {
        this.onViewHolderClickListener = onViewHolderClickListener;
    }

    public interface OnViewHolderClickListener{
        void onClick(BaseViewHolder holder);
    }

    public View getItemView(){
        return itemView;
    }


}
