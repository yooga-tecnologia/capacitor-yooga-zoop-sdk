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

import java.util.Set;
import java.util.Vector;

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
            System.out.println("INICIALOUZ FILHO DA PUTA ============================================================");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        // ZoopTerminalPayment terminal = new ZoopTerminalPayment();
        TerminalListManager tl = new TerminalListManager(new DeviceSelectionListener() {
            @Override
            public void showDeviceListForUserSelection(Vector<JSONObject> vector) {
                System.out.println("==================== showDeviceListForUserSelection ===================");
            }

            @Override
            public void updateDeviceListForUserSelection(JSONObject jsonObject, Vector<JSONObject> vector, int i) {
                System.out.println("==================== updateDeviceListForUserSelection ===================");

            }

            @Override
            public void bluetoothIsNotEnabledNotification() {
                System.out.println("==================== bluetoothIsNotEnabledNotification ===================");
            }

            @Override
            public void deviceSelectedResult(JSONObject jsonObject, Vector<JSONObject> vector, int i) {
                System.out.println("==================== deviceSelectedResult ===================");
            }
        }, this.getContext());

        JSONObject bluetoothDevice = new JSONObject();

        try {

            bluetoothDevice.put("name", call.getString("name"));
            bluetoothDevice.put("uri", call.getString("address"));
            bluetoothDevice.put("communication", call.getString("communication"));
            bluetoothDevice.put("persistent", call.getBoolean("persistent"));
            bluetoothDevice.put("dateTimeDetected", call.getString("dateTimeDetected"));

            tl.startTerminalsDiscovery();
            tl.setSelectedTerminal(bluetoothDevice);
            tl.requestZoopDeviceSelection(bluetoothDevice);
            tl.finishTerminalDiscovery();

            System.out.println("============================= BLUETOOTH SETADO =======================");

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
            java.math.BigDecimal valor = new java.math.BigDecimal(call.getDouble("value"));
            ZoopTerminalPayment zoopTerminalPayment = new ZoopTerminalPayment();

            //=============================================================================================
            // Terminal Payment Listener
            //=============================================================================================

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
                    bridge.triggerWindowJSEvent("paymentSuccessful", "{ 'data': " + jsonObject + " }");
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

            //=============================================================================================
            // Application Display Listener
            //=============================================================================================

            ApplicationDisplayListener adl = new ApplicationDisplayListener() {
                @Override
                public void showMessage(String s, TerminalMessageType terminalMessageType) {
                    System.out.println("showMessage");
                    bridge.triggerWindowJSEvent("showMessage", "{ 'message': '" + s + "'  }");
                }

                @Override
                public void showMessage(String s, TerminalMessageType terminalMessageType, String s1) {
                    System.out.println("showMessage2");
                    bridge.triggerWindowJSEvent("showMessage", "{ 'message': '" + s + "'  }");
                }
            };

            //=============================================================================================
            // Extra Card Information Listener
            //=============================================================================================

            ExtraCardInformationListener ecil = new ExtraCardInformationListener() {
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
            };

            //=============================================================================================
            // Set Listeners
            //=============================================================================================

            zoopTerminalPayment.setTerminalPaymentListener(tp);
            zoopTerminalPayment.setApplicationDisplayListener(adl);
            zoopTerminalPayment.setExtraCardInformationListener(ecil);
            zoopTerminalPayment.charge(
                valor,
                call.getInt("payment_option"),
                call.getInt("installments"),
                call.getString("marketplace_id"),
                call.getString("seller_id"),
                call.getString("publishable_key")
            );

            ret.put("value", "true");
        } catch (Exception e) {
            System.out.println("ERRO TRANSACTION");
            System.out.println(e.getMessage());
            ret.put("value", e.getMessage());
        }

        call.success(ret);
    }
}