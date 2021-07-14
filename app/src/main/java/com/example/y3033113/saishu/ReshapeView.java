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

public class ReshapeView extends View {
    static Bitmap bitmap;
    static Bitmap bm_rv;
    static Canvas canvas_rv;
    static Paint paint;
    Matrix matrix;
    static int width;
    static int height;
    float y;
    float next_y;
    float ratio;
    static boolean reshaping = false;
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
        if(reshaping){
            if(paint == null){
                paint = new Paint();
                paint.setColor(Color.TRANSPARENT);                           // 色を透明に設定
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                width = MyView.width;
                height = MyView.height;
                bm_rv = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas_rv = new Canvas(bm_rv);
            }
            canvas_rv.drawRect(0, 0, width, height, paint);    // 画面を塗りつぶす
            canvas.drawRect(0, 0, width, height, paint);

            canvas_rv.drawBitmap(bitmap, matrix,null);
            canvas.drawBitmap(bm_rv, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                next_y = event.getY();
                ratio = (float)(((float)(height+y-next_y))/height);
                matrix = new Matrix();
                matrix.preScale(ratio, ratio, width/2, height/2);
                invalidate();
                break;
        }
        return touching_rv;
    }

    static void expand(){
        bitmap = MyView.bitmap.get(MyView.currentLayer).copy(Bitmap.Config.ARGB_8888, true);
        touching_rv = true;
        reshaping = true;
        expanding = true;
    }

    static void end(){
        canvas_rv.drawRect(0, 0, width, height, paint);
        touching_rv = false;
        reshaping = false;
        expanding = false;
    }
}
