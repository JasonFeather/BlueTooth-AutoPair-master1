package com.example.mybuletooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wing.feathertoothlibray.SocketLister;
import com.wing.feathertoothlibray.bluetooth.ToothClient;
import com.wing.feathertoothlibray.manager.ToothManager;

/**
 * Created by dell on 2019/6/28.
 */

public class Test1Activity extends Activity {

    private TextView textView;
    ToothClient blueToothClient;
    int num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        textView = (TextView)findViewById(R.id.textView1);

        ToothManager.getInstance().setConfig("abcd1234-ab12-ab12-ab12-abcdef123456","啊啊啊");
        ToothManager instance = ToothManager.getInstance();
        BluetoothDevice conncetToothBlue = instance.isConncetToothBlue();
        if(conncetToothBlue==null){
            instance.connectTooth();
        }else {
             blueToothClient = instance.getBlueToothClient(conncetToothBlue);
            blueToothClient.getMessage(new SocketLister() {
                @Override
                public void getText(String text) {
                    textView.setText(text);
                }
            });
        }


        findViewById(R.id.sent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                blueToothClient.sentMessge("客户端发送"+num);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(blueToothClient!=null){
            blueToothClient.cancel();
        }
    }
}
