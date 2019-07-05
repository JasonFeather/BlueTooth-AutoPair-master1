package com.wing.feathertoothlibray.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.wing.feathertoothlibray.ToothConfig;
import com.wing.feathertoothlibray.tools.ClsUtils;


import java.io.InputStream;
import java.io.OutputStream;


public class BluetoothReceiver extends BroadcastReceiver{

	String pin = "1234";  //此处为你要连接的蓝牙设备的初始密钥，一般为1234或0000
	public BluetoothReceiver() {
		
	}

	//广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行 
	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction(); //得到action
		Log.e("action1=", action);
		BluetoothDevice btDevice=null;  //创建一个蓝牙device对象
		 // 从Intent中获取设备对象
		btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		String name = btDevice.getName();
		Log.e("发现设备:", name+"");
		if(BluetoothDevice.ACTION_FOUND.equals(action)){  //发现设备
			Log.e("qqqqqq:", "["+btDevice.getName()+"]"+":"+btDevice.getAddress());

			if(btDevice.getName()!=null&&btDevice.getName().contains(ToothConfig.BlueToothName))//HC-05设备如果有多个，第一个搜到的那个会被尝试。
			{
				if (btDevice.getBondState() == BluetoothDevice.BOND_NONE) {

					Log.e("ywq", "attemp to bond:"+"["+btDevice.getName()+"]");
					try {
						//通过工具类ClsUtils,调用createBond方法
						ClsUtils.createBond(btDevice.getClass(), btDevice);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
			}
		}else if(action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) //再次得到的action，会等于PAIRING_REQUEST
		{
			Log.e("action2=", action);
			if(btDevice.getName()!=null&&btDevice.getName().contains("小米手机123"))
			{
				Log.e("here", "OKOKOK");

				try {
					//1.确认配对
					ClsUtils.setPairingConfirmation(btDevice.getClass(), btDevice, true);
					//2.终止有序广播
					Log.i("order...", "isOrderedBroadcast:"+isOrderedBroadcast()+",isInitialStickyBroadcast:"+isInitialStickyBroadcast());
					abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
					//3.调用setPin方法进行配对...
					boolean ret = ClsUtils.setPin(btDevice.getClass(), btDevice, pin);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else
				Log.e("提示信息", "这个设备不是目标蓝牙设备");

		}else if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)){
			try {
				BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				BluetoothDevice remoteDevice = bluetoothAdapter.getRemoteDevice(btDevice.getAddress());
				BluetoothSocket insecureRfcommSocketToServiceRecord = remoteDevice.createInsecureRfcommSocketToServiceRecord(ToothConfig.MY_UUID);
				insecureRfcommSocketToServiceRecord.connect();
				//获得输出流（客户端指向服务端输出文本）
				OutputStream outputStream = insecureRfcommSocketToServiceRecord.getOutputStream();
				outputStream.write("客户端发文字".getBytes("UTF-8"));
				InputStream inputStream = insecureRfcommSocketToServiceRecord.getInputStream();
				while (true) {
					byte[] buffer = new byte[1024];
					int count = inputStream.read(buffer);
					Message msg = new Message();
					String s = new String(buffer, 0, count, "utf-8");
					Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
				}
			}catch (Exception e){

			}

		}
	}
}