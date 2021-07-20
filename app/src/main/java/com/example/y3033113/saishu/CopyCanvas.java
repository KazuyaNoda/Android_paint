package com.example.y3033113.saishu;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CopyCanvas {
    static Bitmap bitmap_copy;
    static Canvas canvas;


    static void copy(){
        if(bitmap_copy == null){
            bitmap_copy = Bitmap.createBitmap(MyView.width, MyView.height, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap_copy);
        }

        for(int i=0; i<MyView.bitmap.size(); i++){
            canvas.drawBitmap(MyView.bitmap.get(i), 0, 0, null);
        }
    }
}
