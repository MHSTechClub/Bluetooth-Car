package com.example.zcarey.bluetoothcar.Buttons;

import android.widget.SeekBar;

import com.example.zcarey.bluetoothcar.DataState;

/**
 * Created by zcarey on 9/11/2017.
 */

public class SpeedSlider implements SeekBar.OnSeekBarChangeListener{

    private DataState state;

    public SpeedSlider(DataState state){
        this.state = state;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
        state.speed = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar){

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar){

    }
}
