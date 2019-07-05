package com.wing.feathertoothlibray.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.wing.feathertoothlibray.SocketLister;

import java.io.OutputStream;

/*
* @author zhangjinqi
* create at 2019/7/5
* description: 蓝牙客户端对象
*/
public class ToothClient  extends AbBlueTool {
    BluetoothSocket mClicentSocket;
    OutputStream outputStream;
    public ToothClient(BluetoothSocket clicentSocket){
       this.mClicentSocket=clicentSocket;
       try {
           outputStream = mClicentSocket.getOutputStream();
       }catch (Exception e){

       }

    }

    @Override
    public void sentMessge(String text) {
        //获得输出流（客户端指向服务端输出文本）
        try {
            outputStream.write(text.getBytes("UTF-8"));
        } catch (Exception e) {

        }
    }

    @Override
    public void getMessage(SocketLister socketLister) {
        mSocketLister=socketLister;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    imputStream(mClicentSocket);
                } catch (Exception e) {
                    Log.e("Exception",e.toString());
                }
            }
        }).start();
    }

    @Override
    public void cancel()  {
        super.cancel();
        try {
            mClicentSocket.close();
            outputStream.close();
        }catch (Exception e){

        }

    }
}
