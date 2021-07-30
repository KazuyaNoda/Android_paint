package com.example.y3033113.saishu;
// メインキャンバスのイメージをコピーするクラス
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

public class CopyCanvas {
    static Bitmap bitmap_copy;  // コピー先bitmap
    static Canvas canvas;       // コピー先bitmapを操作するcanvas


    // コピーするメソッド
    static void copy(){
        if(bitmap_copy == null){    // コピー先bitmapが存在しないならば
            // bitmapの作成
            bitmap_copy = Bitmap.createBitmap(MyView.width, MyView.height, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap_copy);
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);     // キャンバスのクリアー

        for(int i=0; i<MyView.bitmap.size(); i++){                      // 各レイヤーのイメージをキャンバスに描画
            canvas.drawBitmap(MyView.bitmap.get(i), 0, 0, null);
        }
    }
}
