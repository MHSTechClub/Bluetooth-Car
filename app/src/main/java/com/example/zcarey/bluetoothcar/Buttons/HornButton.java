package com.example.zcarey.bluetoothcar.Buttons;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.example.zcarey.bluetoothcar.DataState;
import com.example.zcarey.bluetoothcar.R;

/**
 * Created by zcarey on 9/11/2017.
 */

public class HornButton implements View.OnTouchListener{

    private DataState state;

    public HornButton(DataState state){
        this.state = state;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            state.HornOn = true;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            state.HornOn = false;
        }

        ImageButton v = (ImageButton) view;
        if(state.HornOn){
            v.setBackgroundResource(R.drawable.hornon);
        }else{
            v.setBackgroundResource(R.drawable.hornoff);
        }
        return true;
    }

}
