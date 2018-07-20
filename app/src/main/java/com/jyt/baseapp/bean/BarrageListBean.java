package com.jyt.baseapp.bean;

import java.util.List;

/**
 * @author LinWei on 2018/7/19 22:34
 */
public class BarrageListBean  {
    private List<String> tiaoxiao;
    private List<String> guli;
    private List<String> tixing;

    public List<String> getTiaoxiao() {
        return tiaoxiao;
    }

    public void setTiaoxiao(List<String> tiaoxiao) {
        this.tiaoxiao = tiaoxiao;
    }

    public List<String> getGuli() {
        return guli;
    }

    public void setGuli(List<String> guli) {
        this.guli = guli;
    }

    public List<String> getTixing() {
        return tixing;
    }

    public void setTixing(List<String> tixing) {
        this.tixing = tixing;
    }
}
