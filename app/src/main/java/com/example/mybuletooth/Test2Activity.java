package com.example.mybuletooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.wing.feathertoothlibray.SocketLister;
import com.wing.feathertoothlibray.bluetooth.ToothService;
import com.wing.feathertoothlibray.manager.ToothManager;
import com.ywq.broadcast.BluetoothReceiver;

import java.util.ArrayList;

/**
 * Created by dell on 2019/6/28.
 */

public class Test2Activity extends Activity implements View.OnClickListener {

    private TextView tex;
    ToothService toothService;
    private int num;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ArrayList<String> strings = new ArrayList<>();
        strings.add(Manifest.permission.BLUETOOTH);
        strings.add(Manifest.permission.BLUETOOTH_ADMIN);
        strings.add(Manifest.permission.CHANGE_NETWORK_STATE);
        strings.add(Manifest.permission.CHANGE_WIFI_STATE);
        strings.add(Manifest.permission.ACCESS_NETWORK_STATE);
        strings.add(Manifest.permission.ACCESS_WIFI_STATE);
        strings.add(Manifest.permission.INTERNET);

        ActivityCompat.requestPermissions(this, strings.toArray(new String[strings.size()]), 1);
        IntentFilter intent = new IntentFilter();
        BluetoothReceiver bluetoothReceiver = new BluetoothReceiver();
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothDevice.ACTION_FOUND);//找到設備
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        registerReceiver(bluetoothReceiver,intent);
        tex = (TextView) findViewById(R.id.textView1);
        ToothManager.getInstance().setConfig("abcd1234-ab12-ab12-ab12-abcdef123456","aa");
        ToothManager instance = ToothManager.getInstance();
        if(instance.isConncetToothBlue()!=null){
             toothService = instance.getToothService();
            toothService.getMessage(new SocketLister() {
                @Override
                public void getText(String text) {
                    tex.setText(text);
                }
            });
        }

    findViewById(R.id.buton).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buton:
                num++;
                toothService.sentMessge("服务端发送"+num);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(toothService!=null){
            toothService.cancel();
        }
    }
}
