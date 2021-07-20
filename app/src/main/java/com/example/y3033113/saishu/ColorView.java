package com.example.y3033113.saishu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

// 色のプレビューを表示するクラス
public class ColorView extends View {
    public ColorView(Context context) {
        super(context);
    }
    public ColorView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        // 入力されたARGBの値から色を定義し、塗りつぶしあり四角形で表示
        paint.setColor(Color.argb(com.example.y3033113.saishu.MainActivity2.value_alpha, com.example.y3033113.saishu.MainActivity2.value_R, com.example.y3033113.saishu.MainActivity2.value_G, com.example.y3033113.saishu.MainActivity2.value_B));
        canvas.drawRect(0, 0, 250, 250, paint);

        invalidate();  // 再描画
    }
}
