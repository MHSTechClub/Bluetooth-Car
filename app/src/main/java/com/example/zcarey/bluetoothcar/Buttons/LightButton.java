package com.example.zcarey.bluetoothcar.Buttons;

import android.view.View;
import android.widget.ImageButton;

import com.example.zcarey.bluetoothcar.DataState;
import com.example.zcarey.bluetoothcar.R;

/**
 * Created by zcarey on 9/11/2017.
 */

public class LightButton implements View.OnClickListener{

    private DataState dataState;

    public LightButton(DataState state){
        this.dataState = state;
    }

    @Override
    public void onClick(View view){
        dataState.LightsOn = (!dataState.LightsOn);

        ImageButton v = (ImageButton) view;
        if(dataState.LightsOn){
            v.setBackgroundResource(R.drawable.lightson);
        }else{
            v.setBackgroundResource(R.drawable.lightsoff);
        }
    }

}
