package com.example.zcarey.bluetoothcar.BluetoothServices;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by zcarey on 9/10/2017.
 */

public class BluetoothService extends BroadcastReceiver {

    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter adapter = null;
    private BluetoothSocket socket = null;
    private Activity mainActivity = null;
    private BluetoothListener listener = null;
    private OutputStream socketOS = null;

    public BluetoothService(Activity mainActivity){
        this.mainActivity = mainActivity;
        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            //Bluetooth is not supported.
        }

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        mainActivity.registerReceiver(this, filter1);
        mainActivity.registerReceiver(this, filter2);
        mainActivity.registerReceiver(this, filter3);

        askEnableBluetooth();
    }

    //Returns true if permission had to be asked.
    private boolean askEnableBluetooth(){
        if(!adapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mainActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return true;
        }

        return false;
    }

    public List<BluetoothDevice> getPairedDevices(){
        List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
        for(BluetoothDevice device : adapter.getBondedDevices()){
            deviceList.add(device);
        }

        return deviceList;
    }

    public void attemptConnection(BluetoothDevice device){
        if(askEnableBluetooth()){
            return;
        }
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            adapter.cancelDiscovery();
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
            socketOS = socket.getOutputStream();
            invokeConnected();
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
    }

    public void write(byte data){
        if(isConnected() && socketOS != null){
            try {
                socketOS.write(data);
            }catch(IOException e){
                System.err.println("Could not write data!: " + e.getMessage() + "\n" + e.getStackTrace());
            }
        }
    }

    public void disconnect(){
        if(isConnected()){
            try {
                socket.close();
                if(!isConnected()){
                    invokeDisconnected();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected(){
        if(socket == null){
            return false;
        }

        return socket.isConnected();
    }

    public void selectDevice(){
        new DeviceSelector(this, mainActivity);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //Device found
        }else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
            //invokeConnected();
        }else if(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)){
            disconnect();
            //invokeDisconnected();
        }
        else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            disconnect();
            invokeDisconnected();
        }
        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            //Done searching
        }
    }

    public void setBluetoothListener(BluetoothListener listener){
        this.listener = listener;
    }

    private void invokeConnected(){
        if(listener != null){
            listener.onConnect();
        }
    }

    private void invokeDisconnected(){
        if(listener != null){
            listener.onDisconnect();
        }
    }

}
