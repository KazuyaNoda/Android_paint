package com.example.y3033113.saishu;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class RotationView extends View {

    static boolean rotating = false;
    public RotationView(Context context) {
        super(context);
    }
    public RotationView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas){

    }

    public boolean onTouchEvent(MotionEvent event){
        if(rotating){
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    invalidate();
                    break;
            }
        }
        return rotating;
    }
}
