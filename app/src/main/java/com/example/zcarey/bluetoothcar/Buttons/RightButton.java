package com.example.zcarey.bluetoothcar.Buttons;

import android.view.MotionEvent;
import android.view.View;

import com.example.zcarey.bluetoothcar.DataState;

/**
 * Created by zcarey on 9/11/2017.
 */

public class RightButton implements View.OnTouchListener{

    private DataState state;

    public RightButton(DataState state){
        this.state = state;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            state.right = true;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            state.right = false;
        }

        return true;
    }

}
