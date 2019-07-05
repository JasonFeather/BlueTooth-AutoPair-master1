package com.wing.feathertoothlibray.bluetooth;

import android.widget.TextView;

import com.wing.feathertoothlibray.SocketLister;

/*
* @author zhangjinqi
* create at 2019/7/5
* description:蓝牙实体类接口
*/
public interface IBlueTooth {
    public void sentMessge(String text);
    public void getMessage(SocketLister socketLister);
    public void cancel() throws Exception;
}
