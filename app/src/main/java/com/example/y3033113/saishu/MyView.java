package com.example.y3033113.saishu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.Image;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// 描画を担当するクラス
public class MyView extends View {

    static float prex;                      // 図形を描くときのleftにあたる
    static float prey;                      // 図形を描くときのtopにあたる
    static float x;                         // 図形を描くときのrightにあたる
    static float y;                         // 図形を描くときのbottomにあたる
    static float ghost_x;                   // プレビュー図形のrightにあたる
    static float ghost_y;                   // プレビュー図形のbottomにあたる
    static Bitmap bitmap;                   // 今のキャンバスのイメージを格納する
    static Paint paint;                            // キャンバスに描くためのPaint
    int color;
    int height;
    int width;
    static Paint paint_ac = new Paint();    // allclearをするためのPaint
    static Canvas canvas_bm;                // bitmapを編集するためのCanvas

    // 描画モードの定数定義
    final static int mode_Line = 0;         // 線
    final static int mode_fillRect = 1;     // 塗りつぶしあり四角形
    final static int mode_Rect = 2;         // 塗りつぶしなし四角形
    final static int mode_fillOval = 3;     // 塗りつぶしあり楕円
    final static int mode_Oval = 4;         // 塗りつぶしなし楕円
    final static int mode_clip = 5;
    final static int mode_Eraser = 6;

    static int mode = mode_clip;            // 描画モードを格納する変数
    static Path path = new Path();

    static List<Structure> drawlist = new ArrayList<>(64);
    static Structure newdraw;
    static List<Float> points = new ArrayList<>();

    static List<Structure> keptlist = new ArrayList<>(10);

    public MyView(Context context){
        super(context);
    }
    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(paint == null){      // paintが定義されていなかったら
            paint = new Paint();                    // paintを宣言
            paint.setStrokeWidth(5);                // 線の太さを5にする(将来的には変更できるようにする)
            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);   // bitmapをcanvasにあわせてつくる
            canvas_bm = new Canvas(bitmap);
            height = canvas_bm.getHeight();
            width = canvas_bm.getWidth();
        }
        paint.setColor(Color.WHITE);
        canvas_bm.drawRect(0, 0, width, height, paint);
        // 入力されたARGBの値で色を定義
        color = Color.argb(com.example.y3033113.saishu.MainActivity.value_alpha, com.example.y3033113.saishu.MainActivity.value_R, com.example.y3033113.saishu.MainActivity.value_G, com.example.y3033113.saishu.MainActivity.value_B);

        myDraw();


        canvas.drawBitmap(bitmap, 0, 0, null);  // canvasにbitmapのイメージを描く

    }

    // タッチイベントの処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        keptlist.clear();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:           // 触ったとき
                if(mode == mode_clip){
                    path.reset();
                }
                prex = event.getX();                // 座標を図形の始点に設定
                prey = event.getY();
                points.add(prex);
                points.add(prey);
                break;
            case MotionEvent.ACTION_UP:             // 離したとき
                x = event.getX();                   // 座標を図形の終点に設定
                y = event.getY();
                points.add(x);
                points.add(y);

                if(mode == mode_clip){
                    path = createPath(points);
                }
                else{
                    newdraw = new Structure(points, color, mode, 5, path);
                    drawlist.add(newdraw);

                    points = new ArrayList<>();
                    path.reset();

                    GhostView.drawghostkey = false;     // プレビュー図形の表示を終了
                    invalidate();                       // 再描画
                }



                break;
            case MotionEvent.ACTION_MOVE:           // スライドするとき
                if(mode == mode_Line || mode == mode_clip){              // 描画モードが線のとき
                    x = event.getX();               // 座標を終点に設定
                    y = event.getY();
                    points.add(x);
                    points.add(y);

                    invalidate();                   // 再描画
                }
                ghost_x = event.getX();         // 座標をプレビュー図形の終点に設定
                ghost_y = event.getY();
                GhostView.drawghostkey = true;  // プレビュー図形の表示を開始
                break;
        }

        return true;
    }

    static void myDraw(){
        RectF rect;             // 楕円を描くときに用いる
        Structure drawnow;
        List<Float> drawpoints;

        for(int i=0; i<drawlist.size(); i++){
            drawnow = drawlist.get(i);

            drawpoints = drawnow.points;
            paint.setColor(drawnow.color);
            paint.setStrokeWidth(drawnow.thick);

            switch(drawnow.mode){       // 描画モードで処理を分ける
                case mode_Line:     // 線を描く
                    for(int j=0; j+3<drawpoints.size(); j += 2){
                        canvas_bm.drawLine(drawpoints.get(j), drawpoints.get(j+1), drawpoints.get(j+2), drawpoints.get(j+3), paint);
                    }
                    break;
                case mode_fillRect: // 塗りつぶしあり四角形を描く
                    paint.setStyle(Paint.Style.FILL);
                    canvas_bm.drawRect(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3), paint);
                    break;
                case mode_Rect:     // 塗りつぶしなし四角形を描く
                    paint.setStyle(Paint.Style.STROKE);
                    canvas_bm.drawRect(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3), paint);
                    break;
                case mode_fillOval: // 塗りつぶしあり楕円を描く
                    paint.setStyle(Paint.Style.FILL);
                    rect = new RectF(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3));
                    canvas_bm.drawOval(rect, paint);
                    break;
                case mode_Oval:     // 塗りつぶしなし楕円を描く
                    paint.setStyle(Paint.Style.STROKE);
                    rect = new RectF(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3));
                    canvas_bm.drawOval(rect, paint);
                    break;
                case mode_Eraser:
                    break;

            }
        }
    }

    public Path createPath(List<Float> points){
        path = new Path();
        if(points.size() >= 4){
            path.moveTo(points.get(0), points.get(1));
            for(int i=2; i<points.size(); i+=2){
                path.lineTo(points.get(i), points.get(i+1));
            }
            path.close();
        }

        return path;
    }

    // 画面のイメージを消すメソッド
    static void AllClear(){
        paint_ac.setColor(Color.WHITE);                                                              // 色を白に設定
        canvas_bm.drawRect(0, 0, canvas_bm.getWidth(), canvas_bm.getHeight(), paint_ac);    // 画面を塗りつぶす
        drawlist.clear();
    }

    static void Undo(){
        int index = drawlist.size() - 1;
        if(index >= 0){
            Structure tmp = drawlist.get(index);
            keptlist.add(tmp);
            drawlist.remove(index);
        }
        paint_ac.setColor(Color.WHITE);                                                              // 色を白に設定
        canvas_bm.drawRect(0, 0, canvas_bm.getWidth(), canvas_bm.getHeight(), paint_ac);    // 画面を塗りつぶす
        myDraw();
    }

    static void Redo(){
        int index = keptlist.size() - 1;
        if(index >= 0){
            Structure tmp = keptlist.get(index);
            drawlist.add(tmp);
            keptlist.remove(index);
        }
        paint_ac.setColor(Color.WHITE);                                                              // 色を白に設定
        canvas_bm.drawRect(0, 0, canvas_bm.getWidth(), canvas_bm.getHeight(), paint_ac);    // 画面を塗りつぶす
        myDraw();
    }

}
