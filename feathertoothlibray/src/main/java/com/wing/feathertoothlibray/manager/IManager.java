package com.wing.feathertoothlibray.manager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.wing.feathertoothlibray.bluetooth.ToothClient;
import com.wing.feathertoothlibray.bluetooth.ToothService;

/**
 * Created by dell on 2019/7/5.
 */

public interface IManager {
    public void setConfig(String uuid, String BlueToothName);  //设置参数
    public  void connectTooth();  //连接蓝牙
    public  BluetoothDevice isConncetToothBlue();  //判断是否连接
    public  ToothClient getBlueToothClient(BluetoothDevice device);//获取客户端
    public void registerReceiver(Context context);
    public ToothService getToothService();//获取服务端对象
}
