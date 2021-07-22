package com.example.y3033113.saishu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class ReshapeView extends View {
    static Bitmap bitmap;
    static Bitmap bm_rv;
    static Bitmap reshaped;
    static Canvas canvas_rv;
    Matrix matrix;
    static int width;
    static int height;

    float x;
    float y;
    float next_x;
    float next_y;
    float ratio = 1.0f;
    static float pre_ratio = 1.0f;
    float dx = 0;
    float dy = 0;
    static float pre_dx = 0;
    static float pre_dy = 0;

    static boolean drawReshape = false;
    static boolean expanding = false;
    static boolean sliding = false;
    static boolean touching_rv = false;

    public ReshapeView(Context context) {
        super(context);
    }
    public ReshapeView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas){
        if(drawReshape){
            if(canvas_rv == null){
                width = MyView.width;
                height = MyView.height;
                bm_rv = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas_rv = new Canvas(bm_rv);
            }

            canvas.drawColor(Color.WHITE);
            canvas_rv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);    // 画面を塗りつぶす

            for(int i=0; i<MyView.bitmap.size(); i++){
                if(i != MyView.currentLayer){
                    canvas_rv.drawBitmap(MyView.bitmap.get(i), 0, 0, null);
                }
            }
            if(matrix == null){
                canvas_rv.drawBitmap(bitmap, 0, 0,null);
            }
            else{
                canvas_rv.drawBitmap(bitmap, matrix,null);
            }
            canvas_rv.drawBitmap(bm_rv, 0, 0, null);
            canvas.drawBitmap(bm_rv, 0, 0, null);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(touching_rv){
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    pre_ratio = ratio;
                    pre_dx = dx;
                    pre_dy = dy;
                    reshaped = bm_rv.copy(Bitmap.Config.ARGB_8888, true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    next_x = event.getX();
                    next_y = event.getY();

                    matrix = new Matrix();
                    if(expanding){
                        ratio = (float)(((float)(height+y-next_y))/height * pre_ratio);
                    }
                    else if(sliding){
                        dx = next_x - x + pre_dx;
                        dy = next_y - y + pre_dy;
                    }
                    matrix.setTranslate(dx, dy);
                    matrix.preScale(ratio, ratio, width/2, height/2);
                    break;
            }
        }
        return touching_rv;
    }

    static void expand(){
        bitmap = MyView.bitmap.get(MyView.currentLayer).copy(Bitmap.Config.ARGB_8888, true);
        drawReshape = true;
        touching_rv = true;

        sliding = false;
        expanding = true;
    }

    static void slide(){
        bitmap = MyView.bitmap.get(MyView.currentLayer).copy(Bitmap.Config.ARGB_8888, true);
        drawReshape = true;
        touching_rv = true;

        expanding = false;
        sliding = true;
    }

    static void decide(){
        MyView.keptlist = new ArrayList<>(64);
        MyView.layers.set(MyView.currentLayer, new ArrayList<>());
        MyView.layers.get(MyView.currentLayer).add(new Structure(null, 0, MyView.mode_bitmap, 0, null, false, reshaped));
    }

    static void end(){
        if(canvas_rv != null){
            canvas_rv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        pre_ratio = 1.0f;
        pre_dx = 0;
        pre_dy = 0;
        touching_rv = false;
        drawReshape = false;
        expanding = false;
        sliding = false;
    }
}
