package com.example.zcarey.bluetoothcar.BluetoothServices;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zcarey.bluetoothcar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcarey on 9/10/2017.
 */

public class DeviceSelector implements View.OnClickListener, AdapterView.OnItemClickListener {

    private AlertDialog dialog;
    private TextView text;
    private Context context;
    private BluetoothService bluetooth;

    private List<BluetoothDevice> devices;
    private int selection = -1;

    DeviceSelector(BluetoothService bluetooth, Activity main){
        this.bluetooth = bluetooth;
        List<String> items = new ArrayList<String>();
        devices = bluetooth.getPairedDevices();
        for(BluetoothDevice device : devices){
            items.add(device.getName());
        }

        context = main.getApplicationContext();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(main);
        View mView = main.getLayoutInflater().inflate(R.layout.bluetooth_devices, null);
        text = (TextView) mView.findViewById(R.id.pairedDevices);
        ListView mList = (ListView) mView.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item, items);
        mList.setAdapter(adapter);

        mList.setOnItemClickListener(this);
        Button mButton = (Button) mView.findViewById(R.id.button);
        mButton.setOnClickListener(this);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selection = i;
        String selection = adapterView.getItemAtPosition(i).toString();
        text.setText(selection);
        //Toast.makeText(context, "Selected item: " + selection + " - " + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view){
        if(selection > -1) {
            bluetooth.attemptConnection(devices.get(selection));
        }
        dialog.cancel();
    }

}
