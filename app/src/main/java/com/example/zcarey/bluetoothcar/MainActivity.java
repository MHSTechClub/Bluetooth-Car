package com.example.zcarey.bluetoothcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zcarey.bluetoothcar.BluetoothServices.BluetoothListener;
import com.example.zcarey.bluetoothcar.BluetoothServices.BluetoothService;
import com.example.zcarey.bluetoothcar.Buttons.DataSender;
import com.example.zcarey.bluetoothcar.Buttons.DownButton;
import com.example.zcarey.bluetoothcar.Buttons.HornButton;
import com.example.zcarey.bluetoothcar.Buttons.LeftButton;
import com.example.zcarey.bluetoothcar.Buttons.LightButton;
import com.example.zcarey.bluetoothcar.Buttons.RightButton;
import com.example.zcarey.bluetoothcar.Buttons.SpeedSlider;
import com.example.zcarey.bluetoothcar.Buttons.UpButton;

import java.util.Timer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BluetoothListener {

    private ImageButton connection;
    private static final int DATA_SPEED_MS =  25;

    private BluetoothService bluetooth;
    private DataSender dataSender;
    private DataState state;

    //private BluetoothAdapter bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetooth = new BluetoothService(this);
        bluetooth.setBluetoothListener(this);
        state = new DataState();
        connection = (ImageButton)findViewById(R.id.connection);
        connection.setOnClickListener(this);

        ((ImageButton)findViewById(R.id.lights)).setOnClickListener(new LightButton(state));
        ((ImageButton)findViewById(R.id.horn)).setOnTouchListener(new HornButton(state));
        ((SeekBar)findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SpeedSlider(state));
        ((ImageButton)findViewById(R.id.left)).setOnTouchListener(new LeftButton(state));
        ((ImageButton)findViewById(R.id.right)).setOnTouchListener(new RightButton(state));
        ((ImageButton)findViewById(R.id.up)).setOnTouchListener(new UpButton(state));
        ((ImageButton)findViewById(R.id.down)).setOnTouchListener(new DownButton(state));

    }

    //When connection click is detected
    @Override
    public void onClick(View view){
        if(bluetooth.isConnected()){
            bluetooth.disconnect();
        }else{
            bluetooth.selectDevice();
        }
    }

    //Bluetooth Connected
    @Override
    public void onConnect(){
        connection.setBackgroundResource(R.drawable.connected);
        if(dataSender == null) {
            dataSender = new DataSender(bluetooth, DATA_SPEED_MS, state);
        }
    }

    //Bluetooth disconnected
    @Override
    public void onDisconnect(){
        connection.setBackgroundResource(R.drawable.notconnected);
        if(dataSender != null) {
            dataSender.stop();
            dataSender = null;
        }
    }

}
