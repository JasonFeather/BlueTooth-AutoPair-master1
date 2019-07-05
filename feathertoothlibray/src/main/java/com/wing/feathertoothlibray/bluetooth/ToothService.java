package com.wing.feathertoothlibray.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.util.Log;

import com.wing.feathertoothlibray.SocketLister;
import com.wing.feathertoothlibray.ToothConfig;

import java.io.OutputStream;

/*
* @author zhangjinqi
* create at 2019/7/5
* description: 蓝牙服务端对象
*/
public class ToothService extends AbBlueTool {
    BluetoothServerSocket mBluetoothServerSocket;
    OutputStream outputStream;
    public ToothService(BluetoothAdapter bluetoothAdapter){
        try {
            mBluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(ToothConfig.BlueToothName, ToothConfig.MY_UUID);
        }catch (Exception e){

        }
    }
    @Override
    public void sentMessge(String text) {
        try {
             outputStream = mSocket.getOutputStream();
            outputStream.write(text.getBytes("UTF-8"));
        }catch (Exception e){

        }
    }

    @Override
    public void getMessage(SocketLister socketLister) {
        mSocketLister=socketLister;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket =mBluetoothServerSocket.accept();
                    imputStream(mSocket);
                }catch (Exception e){
                    Log.e("Exception",e.toString());
                }
            }
        }).start();
    }

    @Override
    public void cancel() {
        try {
            mBluetoothServerSocket.close();
            mSocket.close();
            outputStream.close();
            super.cancel();
        }catch (Exception e){
        }
    }
}
