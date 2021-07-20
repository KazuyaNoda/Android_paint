package com.example.y3033113.saishu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// プレビュー図形を表示するクラス
public class GhostView extends View {
    static boolean drawghostkey = false;        // プレビュー図形の表示を開始/終了するためのboolean型変数
    List<Float> points = new ArrayList<>();
    static Paint paint;
    static Canvas canvas_sub;
    static Bitmap bitmap_sub;
    static boolean expanding = false;

    static float prex = 0;
    static float prey = 0;
    static float ghost_x = 0;
    static float ghost_y = 0;


    static int width;
    static int height;

    public GhostView(Context context) {
        super(context);
    }
    public GhostView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint == null){
            paint = new Paint();
            width = MyView.width;
            height = MyView.height;
            bitmap_sub = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvas_sub = new Canvas(bitmap_sub);
        }

        clear();

        if(!MyView.path.isEmpty()){
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            canvas_sub.drawPath(MyView.path, paint);
        }

        paint.setStrokeWidth(MainActivity2.thick);            // 線の太さを５に設定(将来的に変更できるようにする)
        paint.setColor(MainActivity2.color);
        if(MainActivity2.color == Color.TRANSPARENT){
            paint.setColor(Color.WHITE);
        }
        if(drawghostkey) {                  // プレビュー図形を描画できるとき
            if(!com.example.y3033113.saishu.MyView.points.isEmpty()){
                prex = MyView.points.get(0);
                prey = MyView.points.get(1);
            }
            if(MyView.moving){
                drawghost();
                canvas.drawBitmap(bitmap_sub, 0, 0, null);
            }
        }
        invalidate();
    }


    void drawghost(){
        RectF rect;
        switch(MyView.mode){            // 描画モードによって処理を分ける
            case MyView.mode_Line:
                points = MyView.points;
                for(int i=0; i+3<points.size(); i += 2){
                    canvas_sub.drawLine(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3), paint);
                }
                break;
            case MyView.mode_fillRect:  // 塗りつぶしあり四角形を描く
                paint.setStyle(Paint.Style.FILL);
                canvas_sub.drawRect(prex, prey, ghost_x, ghost_y, paint);
                break;
            case MyView.mode_Rect:      // 塗りつぶしなし四角形を描く
                paint.setStyle(Paint.Style.STROKE);
                canvas_sub.drawRect(prex, prey, ghost_x, ghost_y, paint);
                break;
            case MyView.mode_fillOval:  // 塗りつぶしあり楕円を描く
                rect = new RectF(prex, prey, ghost_x, ghost_y);
                paint.setStyle(Paint.Style.FILL);
                canvas_sub.drawOval(rect, paint);
                break;
            case MyView.mode_Oval:      // 塗りつぶしなし楕円を描く
                rect = new RectF(prex, prey, ghost_x, ghost_y);
                paint.setStyle(Paint.Style.STROKE);
                canvas_sub.drawOval(rect, paint);
                break;
            case MyView.mode_clip:
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.GREEN);
                points = MyView.points;
                for(int i=0; i+3<points.size(); i += 2){
                    canvas_sub.drawLine(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3), paint);
                }
                canvas_sub.drawPath(MyView.path, paint);
                break;
        }
    }

    static void clear(){
        paint.setColor(Color.TRANSPARENT);                           // 色を透明に設定
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas_sub.drawRect(0, 0, width, height, paint);    // 画面を塗りつぶす
        paint.setXfermode(null);
    }
}
