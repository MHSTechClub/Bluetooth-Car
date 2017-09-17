package com.example.zcarey.bluetoothcar.Buttons;

import com.example.zcarey.bluetoothcar.BluetoothServices.BluetoothService;
import com.example.zcarey.bluetoothcar.DataState;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zcarey on 9/11/2017.
 */

public class DataSender extends TimerTask{

    private static final byte HEARTBEAT = 0;
    private static final byte COMMAND_LIGHTS = 1;
    private static final byte COMMAND_HORN = 2;
    private static final byte COMMAND_SPEED = 3;
    private static final byte COMMAND_CONTROL = 4;

    private static final byte LIGHTS_ON = 1;
    private static final byte LIGHTS_OFF = 2;
    private static final byte HORN_ON = 1;
    private static final byte HORN_OFF = 2;
    private static final byte STEER_LEFT_BIT = 2;
    private static final byte STEER_RIGHT_BIT = 1;
    private static final byte MOVE_FORWARD_BIT = 8;
    private static final byte MOVE_BACKWARD_BIT = 4;

    private DataState state;

    private boolean lightsOnDB = false;
    private boolean hornOnDB = false;
    private int speedDB = 0;
    private boolean steerLeftDB = false;
    private boolean steerRightDB = false;
    private boolean moveForwardDB = false;
    private boolean moveBackwardDB = false;

    private BluetoothService bluetooth;
    private boolean sendAll = true;
    private Timer timer = null;

    private static final byte BYTE1 = 1;

    public DataSender(BluetoothService bluetooth, int DATA_SPEED_MS, DataState state){
        this.state = state;
        this.bluetooth = bluetooth;
        timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, DATA_SPEED_MS);
    }

    public void stop(){
        timer.cancel();
    }

    @Override
    public void run(){
        bluetooth.write(HEARTBEAT);
        if((state.LightsOn != lightsOnDB) || sendAll) sendLights();
        if((state.HornOn != hornOnDB) || sendAll) sendHorn();
        if((state.speed != speedDB) || sendAll) sendSpeed();
        if(((state.left != steerLeftDB) || (state.right != steerRightDB) || (state.forwards != moveForwardDB) || (state.backwards != moveBackwardDB)) || sendAll) sendControl();
        sendAll = false;
    }

    private void sendLights(){
        bluetooth.write(COMMAND_LIGHTS);
        lightsOnDB = state.LightsOn;
        if(lightsOnDB){
            bluetooth.write(LIGHTS_ON);
        }else {
            bluetooth.write(LIGHTS_OFF);
        }
    }

    private void sendHorn(){
        bluetooth.write(COMMAND_HORN);
        hornOnDB = state.HornOn;
        if(hornOnDB){
            bluetooth.write(HORN_ON);
        }else{
            bluetooth.write(HORN_OFF);
        }
    }

    private void sendSpeed(){
        bluetooth.write(COMMAND_SPEED);
        speedDB = state.speed;
        byte speedByte =(byte) speedDB;
        if(speedByte == 0){
            speedByte = (byte) 1;
        }

        bluetooth.write(speedByte);
    }

    private void sendControl(){
        bluetooth.write(COMMAND_CONTROL);
        steerLeftDB = state.left;
        steerRightDB = state.right;
        moveForwardDB = state.forwards;
        moveBackwardDB = state.backwards;
        byte bl = steerLeftDB ? STEER_LEFT_BIT : 0;
        byte br = steerRightDB ? STEER_RIGHT_BIT : 0;
        byte bf = moveForwardDB ? MOVE_FORWARD_BIT : 0;
        byte bb = moveBackwardDB ? MOVE_BACKWARD_BIT : 0;

        byte b = (byte) (bl | br | bf | bb);
        bluetooth.write(b);
    }

}
