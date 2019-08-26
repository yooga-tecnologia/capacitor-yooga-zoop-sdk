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
import com.zoop.zoopandroidsdk.terminal.ApplicationDisplayListener;
import com.zoop.zoopandroidsdk.terminal.DeviceSelectionListener;
import com.zoop.zoopandroidsdk.terminal.ExtraCardInformationListener;
import com.zoop.zoopandroidsdk.terminal.TerminalMessageType;
import com.zoop.zoopandroidsdk.terminal.TerminalPaymentListener;

import org.json.JSONArray;
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
    public void getBluetoothDevices(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();

        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            JSONArray s = new JSONArray();


            for(BluetoothDevice bt : pairedDevices) {
                JSONObject obj = new JSONObject();

                obj.put("name",bt.getName());
                obj.put("type", bt.getType());
                obj.put("address", bt.getAddress());
                obj.put("class", bt.getBluetoothClass());
                s.put(obj);
            }

            ret.put("devices", s);
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

        try {
            ZoopAPI.initialize(this.getContext());
        } catch(Exception e) {

        }

        // ZoopTerminalPayment terminal = new ZoopTerminalPayment();
        TerminalListManager tl = new TerminalListManager(null, null);

        JSONObject bluetoothDevice = new JSONObject();
        try {

            bluetoothDevice.put("name", "BC:14:EF:93:E4:F0");
            bluetoothDevice.put("uri", "btspp://BC:14:EF:93:E4:F0");
            bluetoothDevice.put("communication", call.getString("communication"));
            bluetoothDevice.put("persistent", call.getString("persistent"));
            bluetoothDevice.put("dateTimeDetected", call.getString("dateTimeDetected"));

            tl.setSelectedTerminal(bluetoothDevice);
            System.out.println("============================= BLUETOOTH SETADO?? =======================");

        } catch(Exception e) {
            System.out.println("============================= enviaBluetooth =======================");
            System.out.println(e.getMessage());
            System.out.println("============================= enviaBluetooth =======================");
            ret.put("value", e);
            call.success(ret);
        }

        tl.requestZoopDeviceSelection(bluetoothDevice);

        ret.put("value", "true");
        call.success(ret);
    }

    @PluginMethod()
    public void transaction(PluginCall call) {

        JSObject ret = new JSObject();


        try {
            ZoopAPI.sMarketplaceId = "9e55e10ff08746daaee031e207935494";
            ZoopAPI.sSellerId = "2e2ce9fcc0454ec290a6c90fc66624e7";
            ZoopAPI.sPublishableKey = "zpk_test_pxfPkCBWxYWzyKWR3toFW3Fd";

            java.math.BigDecimal numero = new java.math.BigDecimal(1);
            ZoopTerminalPayment zoopTerminalPayment = new ZoopTerminalPayment();

            TerminalPaymentListener tp = new TerminalPaymentListener() {
                @Override
                public void paymentFailed(JSONObject jsonObject) {
                    System.out.println("paymentFailed");
                    System.out.println(jsonObject);
                    System.out.println("============== paymentFailed ==============");

                }

                @Override
                public void paymentDuplicated(JSONObject jsonObject) {
                    System.out.println("paymentDuplicated");
                }

                @Override
                public void paymentSuccessful(JSONObject jsonObject) {
                    System.out.println("paymentSuccessful");
                }

                @Override
                public void paymentAborted() {
                    System.out.println("paymentAborted");
                }

                @Override
                public void cardholderSignatureRequested() {
                    System.out.println("cardholderSignatureRequested");
                }

                @Override
                public void currentChargeCanBeAbortedByUser(boolean b) {
                    System.out.println("currentChargeCanBeAbortedByUser");
                }

                @Override
                public void signatureResult(int i) {
                    System.out.println("signatureResult");
                }
            };

            zoopTerminalPayment.setTerminalPaymentListener(tp);
            zoopTerminalPayment.setApplicationDisplayListener(new ApplicationDisplayListener() {
                @Override
                public void showMessage(String s, TerminalMessageType terminalMessageType) {
                    System.out.println("showMessage");
                }

                @Override
                public void showMessage(String s, TerminalMessageType terminalMessageType, String s1) {

                    System.out.println("showMessage2");
                }
            });
            zoopTerminalPayment.setExtraCardInformationListener(new ExtraCardInformationListener() {
                @Override
                public void cardLast4DigitsRequested() {
                    System.out.println("cardLast4DigitsRequested");
                }

                @Override
                public void cardExpirationDateRequested() {
                    System.out.println("cardExpirationDateRequested");

                }

                @Override
                public void cardCVCRequested() {
                    System.out.println("cardCVCRequested");

                }
            });
            zoopTerminalPayment.charge(numero,
                    1, 1,
                    "3249465a7753536b62545a6a684b0000",
                    "1e5ee2e290d040769806c79e6ef94ee1",
                    "zpk_test_EzCkzFFKibGQU6HFq7EYVuxI");

            ret.put("value", "true");
        } catch (Exception e) {
            System.out.println("ERRO TRANSACTION");
            System.out.println(e.getMessage());
            ret.put("value", e.getMessage());
        }

        call.success(ret);
    }
}
