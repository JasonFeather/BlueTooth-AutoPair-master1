package com.wing.feathertoothlibray.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.wing.feathertoothlibray.SocketLister;

import java.io.InputStream;

/*
* @author zhangjinqi
* create at 2019/7/5
* description:蓝牙实体类抽象实现类
*/

public abstract class AbBlueTool implements IBlueTooth{
    public SocketLister mSocketLister;
    public BluetoothSocket mSocket;
    InputStream inputStream;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    String text=(String)msg.obj;
                    mSocketLister.getText(text);
                    break;
            }
        }
    };

    public void imputStream(BluetoothSocket socket) throws Exception{
         inputStream = socket.getInputStream();
        final byte[] buffer = new byte[1024];
        while (inputStream.read(buffer, 0, 1024) != -1) {
            final String s = new String(buffer, 0, buffer.length, "utf-8");
            Message message = new Message();
            message.what=0;
            message.obj=s;
            handler.sendMessage(message);
        }
    }

    @Override
    public void cancel()  {
        try {
            inputStream.close();
        }catch (Exception e){

        }
    }
}
