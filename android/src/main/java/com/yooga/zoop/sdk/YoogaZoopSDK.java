package com.yooga.zoop.sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.yooga.zoop.sdk.yoogazoopsdk.R;
import com.zoop.zoopandroidsdk.ZoopAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NativePlugin()
public class YoogaZoopSDK extends Plugin {

    @PluginMethod()
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void init(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();

        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            List<String> s = new ArrayList<String>();
            for(BluetoothDevice bt : pairedDevices)
                s.add(bt.getName());

             ret.put("value", s);
             call.success(ret);
        } catch(Exception e) {
            value = e.getMessage();
        }

        ret.put("value", value);
        call.success(ret);
    }
}
