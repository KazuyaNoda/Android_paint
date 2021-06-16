package com.example.y3033113.saishu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// プレビュー図形を表示するクラス
public class GhostView extends View {
    static boolean drawghostkey = false;        // プレビュー図形の表示を開始/終了するためのboolean型変数
    List<Float> points = new ArrayList<>();

    public GhostView(Context context) {
        super(context);
    }
    public GhostView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect;                         // 楕円を描くときに用いる
        Paint paint = new Paint();
        paint.setStrokeWidth(5);            // 線の太さを５に設定(将来的に変更できるようにする)

        // 入力されたARGBの値から色を定義
        paint.setColor(Color.argb(com.example.y3033113.saishu.MainActivity.value_alpha, com.example.y3033113.saishu.MainActivity.value_R, com.example.y3033113.saishu.MainActivity.value_G, com.example.y3033113.saishu.MainActivity.value_B));

        if(drawghostkey) {                  // プレビュー図形を描画できるとき
            float prex = com.example.y3033113.saishu.MyView.prex;
            float prey = com.example.y3033113.saishu.MyView.prey;
            float ghost_x = com.example.y3033113.saishu.MyView.ghost_x;
            float ghost_y = com.example.y3033113.saishu.MyView.ghost_y;
            switch(com.example.y3033113.saishu.MyView.mode){            // 描画モードによって処理を分ける
                case com.example.y3033113.saishu.MyView.mode_Line:
                    points = com.example.y3033113.saishu.MyView.points;
                    for(int i=0; i+3<points.size(); i += 2){
                        canvas.drawLine(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3), paint);
                    }
                    break;
                case com.example.y3033113.saishu.MyView.mode_fillRect:  // 塗りつぶしあり四角形を描く
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRect(prex, prey, ghost_x, ghost_y, paint);
                    break;
                case com.example.y3033113.saishu.MyView.mode_Rect:      // 塗りつぶしなし四角形を描く
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(prex, prey, ghost_x, ghost_y, paint);
                    break;
                case com.example.y3033113.saishu.MyView.mode_fillOval:  // 塗りつぶしあり楕円を描く
                    rect = new RectF(prex, prey, ghost_x, ghost_y);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawOval(rect, paint);
                    break;
                case com.example.y3033113.saishu.MyView.mode_Oval:      // 塗りつぶしなし楕円を描く
                    rect = new RectF(prex, prey, ghost_x, ghost_y);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawOval(rect, paint);
                    break;
                case com.example.y3033113.saishu.MyView.mode_clip:
                    points = com.example.y3033113.saishu.MyView.points;
                    for(int i=0; i+3<points.size(); i += 2){
                        canvas.drawLine(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3), paint);
                    }
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawPath(MyView.path, paint);
                    break;
            }
        }

        invalidate();   // 再描画
    }
}
