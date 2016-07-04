package com.china.view;

/**
 * Created by dhq on 2015/11/17.
 */
public class DialogCallBack<T> {
    /**
     * 点击确定键，不返回参数
     */
    public void ok(){};

    /**
     * 点击确定键，返回参数
     */
    public void ok(T result){};

    public void cancle(){};

}
