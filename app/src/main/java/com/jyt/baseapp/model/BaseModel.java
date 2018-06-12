package com.jyt.baseapp.model;

import android.content.Context;

/**
 * Created by chenweiqi on 2017/11/13.
 */

public interface BaseModel {
    void onStart(Context context);
    void onDestroy();
}
