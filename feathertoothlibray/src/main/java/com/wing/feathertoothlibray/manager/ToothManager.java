package com.wing.feathertoothlibray.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;

import com.wing.feathertoothlibray.bluetooth.ToothClient;
import com.wing.feathertoothlibray.ToothConfig;
import com.wing.feathertoothlibray.bluetooth.ToothService;
import com.wing.feathertoothlibray.broadcast.BluetoothReceiver;

import java.util.Set;
import java.util.UUID;

/**
 * 蓝牙控制管理类
 */

public class ToothManager implements IManager {
    @Override
    public void setConfig(String uuid, String BlueToothName) {
                ToothConfig.MY_UUID = UUID
                .fromString(uuid);
        ToothConfig.BlueToothName = BlueToothName;
    }

    @Override
    public void connectTooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();//异步的，不会等待结果，直接返回。
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.startDiscovery();
                }
            }, 500);
        } else {
            bluetoothAdapter.startDiscovery();
        }
    }

    @Override
    public BluetoothDevice isConncetToothBlue() {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (int i = 0; i < devices.size(); i++) {
            BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
            if (device.getName().contains(ToothConfig.BlueToothName)) {
                return device;
            }
        }
        return null;
    }

        private BluetoothSocket GetServiceSocket(BluetoothDevice bluetoothDevice) {
        try {
            BluetoothDevice remoteDevice = bluetoothAdapter.getRemoteDevice(bluetoothDevice.getAddress());
            BluetoothSocket insecureRfcommSocketToServiceRecord = remoteDevice.createInsecureRfcommSocketToServiceRecord(ToothConfig.MY_UUID);
            insecureRfcommSocketToServiceRecord.connect();
            return insecureRfcommSocketToServiceRecord;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public ToothClient getBlueToothClient(BluetoothDevice device) {
                return  new ToothClient(GetServiceSocket(device));
    }

    @Override
    public void registerReceiver(Context context) {
        IntentFilter intent = new IntentFilter();
        BluetoothReceiver bluetoothReceiver = new BluetoothReceiver();
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothDevice.ACTION_FOUND);//找到設備
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        context.registerReceiver(bluetoothReceiver,intent);
    }

    @Override
    public ToothService getToothService() {
            return new ToothService(bluetoothAdapter);
    }
    private static ToothManager toothManager;
    private BluetoothAdapter bluetoothAdapter;
    private ToothManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    public static ToothManager getInstance() {
        if (toothManager == null) {
            synchronized (ToothManager.class) {
                if (toothManager == null) {
                    toothManager = new ToothManager();
                }
            }
        }
        return toothManager;
    }

}
