package com.bellantoni.chetta.lieme.generalclasses;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import zephyr.android.HxMBT.*;

/**
 * Created by alessandro on 9/6/15.
 */
public class BluetoothManager {
    BluetoothAdapter adapter = null;
    BTClient _bt;
    ZephyrProtocol _protocol;
    NewConnectedListener _NConnListener;
    private final int HEART_RATE = 0x100;
    private final int INSTANT_SPEED = 0x101;
    private String TAG = "BluetoothManager";
    private ArrayList<Integer> rateHistory;

    public BluetoothManager(Context context) {
        /*Sending a message to android that we are going to initiate a pairing request*/
        IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
        /*Registering a new BTBroadcast receiver from the Main Activity context with pairing request event*/
        context.registerReceiver(new BTBroadcastReceiver(), filter);
        // Registering the BTBondReceiver in the application that the status of the receiver has changed to Paired
        IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
        context.registerReceiver(new BTBondReceiver(), filter2);
        rateHistory = new ArrayList<>();
    }

    public boolean connect() {
        if(_bt!=null){
            if(_bt.IsConnected())
            {
                Log.i(TAG, "BLUETOOTH ALREADY CONNECTED");
                return true;
                //Reset all the values to 0s

            }
            else
            {
                Log.i(TAG, "BLUETOOTH NOT CONNECTED YET");
            }
        }


        String BhMacID = "00:07:80:9D:8A:E8";
        //String BhMacID = "00:07:80:88:F6:BF";
        adapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().startsWith("HXM")) {
                    BluetoothDevice btDevice = device;
                    BhMacID = btDevice.getAddress();
                    // DEVICE FOUND
                    break;

                }
            }

            BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
            String DeviceName = Device.getName();
            _bt = new BTClient(adapter, BhMacID);
            _NConnListener = new NewConnectedListener(Newhandler, Newhandler);
            _bt.addConnectedEventListener(_NConnListener);

            if(_bt.IsConnected())
            {
                _bt.start();
                Log.i(TAG, "BLUETOOTH CONNECTED");
                return true;
                //Reset all the values to 0s

            }
            else
            {
                Log.i(TAG, "BLUETOOTH NOT CONNECTED");
                return false;
            }

        }

        return false;

    }

    public void clearRateHistory(){
        rateHistory.clear();
    }

    public float getRateAverage(){

        float result = 0;
        for(Integer f: rateHistory){
            result += f;
        }

        if(rateHistory.size()==0)
            return 0;
        return result/rateHistory.size();
    }

    public void disconnect(){
        /*Reset the global variables*/

					/*This disconnects listener from acting on received messages*/
        _bt.removeConnectedEventListener(_NConnListener);
					/*Close the communication with the device & throw an exception if failure*/
        _bt.Close();
    }

    private class BTBondReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.i("Bond state", "BOND_STATED = " + device.getBondState());
        }
    }

    private class BTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("BTIntent", intent.getAction());
            Bundle b = intent.getExtras();
            Log.i("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.i("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
            try {
                BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
                Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[]{String.class});
                byte[] pin = (byte[]) m.invoke(device, "1234");
                m = device.getClass().getMethod("setPin", new Class[]{pin.getClass()});
                Object result = m.invoke(device, pin);
                Log.i("BTTest", result.toString());
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    final Handler Newhandler = new Handler() {
        public void handleMessage(Message msg) {
            TextView tv;
            switch (msg.what) {
                case HEART_RATE:
                    String HeartRatetext = msg.getData().getString("HeartRate");
                    if(rateHistory.size()>100)
                        rateHistory.remove(0);
                    rateHistory.add(Integer.valueOf(HeartRatetext));
                    Log.i(TAG, "HEART_RATE: " + HeartRatetext);
                    break;

                case INSTANT_SPEED:
                    String InstantSpeedtext = msg.getData().getString("InstantSpeed");
                    Log.i(TAG, "SPEED_RATE: " + InstantSpeedtext);
                    break;

            }
        }

    };
}