package com.jyt.baseapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jyt.baseapp.api.Const;
import com.jyt.baseapp.view.viewholder.BaseViewHolder;
import com.jyt.baseapp.view.viewholder.ComMeViewHolder;
import com.jyt.baseapp.view.viewholder.ComOtherViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Message;

/**
 * @author LinWei on 2018/6/11 16:40
 */
public class ComAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List<Message> dataList;
    BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener;
    BaseViewHolder.OnViewHolderClickListener onViewHolderLongClickListener;
    private int Type_Me;
    private int Type_Other;

    public List getDataList() {
        return dataList;
    }

    public void setDataList(List<Message> dataList) {
        this.dataList = dataList;
    }

    public void setOnViewHolderClickListener(BaseViewHolder.OnViewHolderClickListener onViewHolderClickListener) {
        this.onViewHolderClickListener = onViewHolderClickListener;
    }

    public void setOnViewHolderLongClickListener(BaseViewHolder.OnViewHolderClickListener onViewHolderLongClickListener) {
        this.onViewHolderLongClickListener = onViewHolderLongClickListener;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        if (viewType==Type_Me){
            holder = new ComMeViewHolder(parent);
        }else if (viewType==Type_Other){
            holder = new ComOtherViewHolder(parent);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataList!=null){
            return dataList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = dataList.get(position);
        if (Const.getUserID().equals(message.getSenderUserId())){
            return Type_Me;
        }else {
            return Type_Other;
        }
    }

    /**
     * 刷新数据
     * @param dataList
     */
    public void notifyData(List dataList){
        if (dataList==null){
            dataList= new ArrayList();
        }
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    /**
     * 加载更多数据
     * @param dataList
     */
    public void LoadMoreData(List dataList){
        if (dataList!=null){
            dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }


}
