package com.yooga.zoop.sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.ArrayAdapter;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

import com.zoop.zoopandroidsdk.TerminalListManager;
import com.zoop.zoopandroidsdk.ZoopAPI;
import com.zoop.zoopandroidsdk.ZoopTerminalPayment;
import com.zoop.zoopandroidsdk.ZoopTerminalTransaction;
import com.zoop.zoopandroidsdk.terminal.TerminalPaymentListener;

import org.json.JSONObject;

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

            for(BluetoothDevice bt : pairedDevices) {
                s.add(bt.getName());
            }

             ret.put("value", s);
             call.success(ret);
        } catch(Exception e) {
            value = e.getMessage();
        }

        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void setBluetoothDevice(PluginCall call) {
        JSObject ret = new JSObject();

        // ZoopTerminalPayment terminal = new ZoopTerminalPayment();
        TerminalListManager tl = new TerminalListManager();

        JSONObject bluetoothDevice = new JSONObject();
        bluetoothDevice.put("name", call.getString("name"));
        bluetoothDevice.put("uri", call.getString("uri"));
        bluetoothDevice.put("communication", call.getString("communication"));
        bluetoothDevice.put("persistent", call.getString("persistent"));
        bluetoothDevice.put("dateTimeDetected", call.getString("dateTimeDetected"));


        tl.requestZoopDeviceSelection(bluetoothDevice);

        ret.put("value", "true");
        call.success(ret);
    }

    @PluginMethod()
    public void transaction(PluginCall call) {
        JSObject ret = new JSObject();


        try {
            java.math.BigDecimal numero = new java.math.BigDecimal(2);
            ZoopTerminalPayment zoopTerminalPayment = new ZoopTerminalPayment();
            zoopTerminalPayment.setTerminalPaymentListener(MainActivity.this);
            zoopTerminalPayment.setApplicationDisplayListener(MainActivity.this);
            zoopTerminalPayment.setExtraCardInformationListener(MainActivity.this);
            zoopTerminalPayment.charge(numero,
            1, 1,
            "asdasfasd",
            "sadasdsad",
            "aopfkopkdposk");

            ret.put("value", "true");
        } catch (Exception e) {
            ret.put("value", e.getMessage());
        }

        call.success(ret);
    }
}
