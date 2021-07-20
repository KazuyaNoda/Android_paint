package com.example.y3033113.saishu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.Image;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.swap;

// 描画を担当するクラス
public class MyView extends View {

    static float prex;                      // 図形を描くときのleftにあたる
    static float prey;                      // 図形を描くときのtopにあたる
    static float x;                         // 図形を描くときのrightにあたる
    static float y;                         // 図形を描くときのbottomにあたる
    static Paint paint;                            // キャンバスに描くためのPaint
    static int color = Color.BLACK;
    static int height;
    static int width;
    static int thick = 5;
    static Paint paint_ac = new Paint();    // allclearをするためのPaint

    // 描画モードの定数定義
    final static byte mode_Line = 0;         // 線
    final static byte mode_fillRect = 1;     // 塗りつぶしあり四角形
    final static byte mode_Rect = 2;         // 塗りつぶしなし四角形
    final static byte mode_fillOval = 3;     // 塗りつぶしあり楕円
    final static byte mode_Oval = 4;         // 塗りつぶしなし楕円
    final static byte mode_clip = 5;
    final static byte mode_Eraser = 6;
    final static byte mode_bitmap = 7;

    static byte mode = mode_Line;            // 描画モードを格納する変数
    static Path path = new Path();

    static boolean drawing = true;
    static boolean moving = false;
    static boolean cliping = false;

    static List<Float> points = new ArrayList<>();

    static Structure newdraw;
    static List<Structure> drawlist = new ArrayList<>(64);
    static List<Structure> keptlist = new ArrayList<>(64);
    static List<Bitmap> bitmap = new ArrayList<>(10);
    static List<Canvas> canvas_bm = new ArrayList<>(10);
    static List<List<Structure>> layers = new ArrayList<>(10);
    static List<String> invisible = new ArrayList<>(10);


    static int currentLayer = 0;


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
            height = canvas.getHeight();
            width = canvas.getWidth();
        }
        if(canvas_bm.size() == 0){
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.add(0, bmp);
            canvas_bm.add(0, new Canvas(bitmap.get(0)));
        }
        if(canvas_bm.size() != layers.size()){
            while(canvas_bm.size() != layers.size()){
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.add(canvas_bm.size(), bmp);
                canvas_bm.add(canvas_bm.size(), new Canvas(bitmap.get(canvas_bm.size()-1)));
            }
        }
        System.out.println(String.format("myDrawの前 : %d", layers.size()));

        myDraw();

        canvas.drawColor(Color.WHITE);
        for(int i=0; i<layers.size(); i++){
            if(!invisible.contains(String.format("%d", i))){
                canvas.drawBitmap(bitmap.get(i), 0, 0, null);  // canvasにbitmapのイメージを描く
            }
        }
        CopyCanvas.copy();

    }

    // タッチイベントの処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        keptlist.clear();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:           // 触ったとき
                if(mode == mode_clip){
                    path = new Path();
                }
                prex = event.getX();                // 座標を図形の始点に設定
                prey = event.getY();
                points.add(prex);
                points.add(prey);
                GhostView.drawghostkey = true;
                break;
            case MotionEvent.ACTION_UP:             // 離したとき
                x = event.getX();                   // 座標を図形の終点に設定
                y = event.getY();
                points.add(x);
                points.add(y);
                moving = false;

                if(mode == mode_clip){
                    path = createPath(points);
                    cliping = true;
                    points = new ArrayList<>();
                }
                else{
                    color = MainActivity2.color;
                    thick = MainActivity2.thick;
                    newdraw = new Structure(points, color, mode, thick, path, cliping, null);
                    layers.get(currentLayer).add(newdraw);

                    points = new ArrayList<>();

                    if(cliping){
                        GhostView.drawghostkey = false;     // プレビュー図形の表示を終了
                    }
                    invalidate();                       // 再描画
                }

                break;
            case MotionEvent.ACTION_MOVE:           // スライドするとき
                moving = true;
                if(MyView.mode == MyView.mode_Line || MyView.mode == MyView.mode_clip){              // 描画モードが線のとき
                    MyView.x = event.getX();               // 座標を終点に設定
                    MyView.y = event.getY();
                    MyView.points.add(MyView.x);
                    MyView.points.add(MyView.y);
                }
                GhostView.ghost_x = event.getX();         // 座標をプレビュー図形の終点に設定
                GhostView.ghost_y = event.getY();
                break;
        }

        return drawing;
    }


    static void myDraw(){
        RectF rect;             // 楕円を描くときに用いる
        Structure drawnow;
        List<Float> drawpoints;

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        System.out.println(String.format("errorの前: %d", layers.size()));
        for(int i=0; i<layers.size(); i++){
            canvas_bm.get(i).drawRect(0, 0, width, height, paint);
        }
        paint.setXfermode(null);

        for(int i=0; i<layers.size(); i++){
            for(int j=0; j<layers.get(i).size(); j++){
                drawnow = layers.get(i).get(j);
                canvas_bm.get(i).save();

                drawpoints = drawnow.points;
                paint.setColor(drawnow.color);
                if(drawnow.color == Color.TRANSPARENT){
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                }
                paint.setStrokeWidth(drawnow.thick);
                if(drawnow.cliping){
                    canvas_bm.get(i).clipPath(drawnow.path);
                }

                switch(drawnow.mode){       // 描画モードで処理を分ける
                    case mode_Line:     // 線を描く
                        for(int k=0; k+3<drawpoints.size(); k += 2){
                            canvas_bm.get(i).drawLine(drawpoints.get(k), drawpoints.get(k+1), drawpoints.get(k+2), drawpoints.get(k+3), paint);
                        }
                        break;
                    case mode_fillRect: // 塗りつぶしあり四角形を描く
                        paint.setStyle(Paint.Style.FILL);
                        canvas_bm.get(i).drawRect(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3), paint);
                        break;
                    case mode_Rect:     // 塗りつぶしなし四角形を描く
                        paint.setStyle(Paint.Style.STROKE);
                        canvas_bm.get(i).drawRect(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3), paint);
                        break;
                    case mode_fillOval: // 塗りつぶしあり楕円を描く
                        paint.setStyle(Paint.Style.FILL);
                        rect = new RectF(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3));
                        canvas_bm.get(i).drawOval(rect, paint);
                        break;
                    case mode_Oval:     // 塗りつぶしなし楕円を描く
                        paint.setStyle(Paint.Style.STROKE);
                        rect = new RectF(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3));
                        canvas_bm.get(i).drawOval(rect, paint);
                        break;
                    case mode_Eraser:
                        break;
                    case mode_bitmap:
                        canvas_bm.get(i).drawBitmap(drawnow.bitmap, 0,0,null);
                        break;
                }
                if(drawnow.cliping){
                    canvas_bm.get(i).restore();
                }
                if(drawnow.color == Color.TRANSPARENT){
                    paint.setXfermode(null);
                }
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
        if(canvas_bm.get(currentLayer).getSaveCount() > 1){
            canvas_bm.get(currentLayer).restore();
        }
        paint_ac.setColor(Color.TRANSPARENT);                           // 色を白に設定
        paint_ac.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas_bm.get(currentLayer).drawRect(0, 0, width, height, paint_ac);    // 画面を塗りつぶす
        paint_ac.setXfermode(null);
        GhostView.drawghostkey = false;
        layers.get(currentLayer).clear();
        path.reset();
        cliping = false;
    }

    static void clipReset(){
        path = new Path();
        cliping = false;
    }

    static void Undo(){
        drawlist = layers.get(currentLayer);
        int index = drawlist.size() - 1;
        if(index >= 0){
            Structure tmp = drawlist.get(index);
            keptlist.add(tmp);
            drawlist.remove(index);
            layers.set(currentLayer, drawlist);
        }
        paint_ac.setColor(Color.TRANSPARENT);                           // 色を白に設定
        paint_ac.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));                                                          // 色を白に設定
        canvas_bm.get(currentLayer).drawRect(0, 0, width, height, paint_ac);    // 画面を塗りつぶす
        paint_ac.setXfermode(null);
        myDraw();
    }

    static void Redo(){
        int index = keptlist.size() - 1;
        if(index >= 0){
            Structure tmp = keptlist.get(index);
            layers.get(currentLayer).add(tmp);
            keptlist.remove(index);
        }
        paint_ac.setColor(Color.TRANSPARENT);                           // 色を白に設定
        paint_ac.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas_bm.get(currentLayer).drawRect(0, 0, width, height, paint_ac);    // 画面を塗りつぶす
        paint_ac.setXfermode(null);
        myDraw();
    }

    static void exchangeLayers(int index_a, int index_b){
        Collections.swap(layers, index_a, index_b);
        myDraw();
    }

}
