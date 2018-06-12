package com.jyt.baseapp.model;

import com.jyt.baseapp.bean.SearchConditionBean;
import com.zhy.http.okhttp.callback.Callback;

/**
 * @author LinWei on 2018/5/29 16:20
 */
public interface SearchModel extends BaseModel {


    void SearchByKey(SearchConditionBean condition, Callback callback);
}
