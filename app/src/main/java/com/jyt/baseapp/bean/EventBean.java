package com.jyt.baseapp.bean;

/**
 * @author LinWei on 2018/7/9 16:43
 */
public class EventBean<A,B,C,D,E,F> {
    private String code;
    A item1;
    B item2;
    C item3;
    D itme4;
    E item5;
    F item6;



    public EventBean(String code){
        this.code = code;
    }

    public EventBean(String code , A item1){
        this.code = code;
        this.item1 = item1;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public A getItem1() {
        return item1;
    }

    public void setItem1(A item1) {
        this.item1 = item1;
    }

    public B getItem2() {
        return item2;
    }

    public void setItem2(B item2) {
        this.item2 = item2;
    }

    public C getItem3() {
        return item3;
    }

    public void setItem3(C item3) {
        this.item3 = item3;
    }

    public D getItme4() {
        return itme4;
    }

    public void setItme4(D itme4) {
        this.itme4 = itme4;
    }

    public E getItem5() {
        return item5;
    }

    public void setItem5(E item5) {
        this.item5 = item5;
    }

    public F getItem6() {
        return item6;
    }

    public void setItem6(F item6) {
        this.item6 = item6;
    }
}
