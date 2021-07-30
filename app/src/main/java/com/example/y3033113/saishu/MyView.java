package com.example.y3033113.saishu;
// 描画を担当するクラス
// 各描画情報を格納したオブジェクトをレイヤー単位で管理し、
// キャンバスを毎回クリアーしてレイヤー情報をもとに描画している。
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

public class MyView extends View {

    static float prex;                      // 図形を描くときのleftにあたる
    static float prey;                      // 図形を描くときのtopにあたる
    static float x;                         // 図形を描くときのrightにあたる
    static float y;                         // 図形を描くときのbottomにあたる
    static Paint paint;
    static int color = Color.BLACK;         // 初期色は黒
    static int height;                      // canvasの縦幅
    static int width;                       // canvasの横幅
    static int thick = 5;                   // ブラシの太さ
    static Paint paint_ac = new Paint();    // allclearをするためのPaint

    // 描画モードの定数定義
    final static byte mode_Line = 0;         // 線
    final static byte mode_Line2 = 1;        // 直線
    final static byte mode_fillRect = 2;     // 塗りつぶしあり四角形
    final static byte mode_Rect = 3;         // 塗りつぶしなし四角形
    final static byte mode_fillOval = 4;     // 塗りつぶしあり楕円
    final static byte mode_Oval = 5;         // 塗りつぶしなし楕円
    final static byte mode_fillRound = 6;    // 塗りつぶしあり真円
    final static byte mode_Round = 7;        // 塗りつぶしなし真円
    final static byte mode_clip = 8;         // 領域指定
    final static byte mode_Eraser = 9;       // 消しゴム
    final static byte mode_bitmap = 10;      // bitmapを描画するとき

    static byte mode = mode_Line;            // 描画モードを格納する変数
    static Path path = new Path();           // 領域を指定するときに用いるPath

    static boolean drawing = true;           // タッチイベントの操作に用いる
    static boolean moving = false;           // GhostViewの処理に用いる
    static boolean cliping = false;          // 領域が指定されていることを表す

    static List<Float> points = new ArrayList<>();  // タッチイベントで取得した座標を格納する

    static Structure newdraw;                       // 新しく描くもの
    static List<Structure> drawlist = new ArrayList<>(64);      // 現在のレイヤー情報をコピーする先
    static List<Structure> keptlist = new ArrayList<>(64);      // undoで取り除いたレイヤー情報を格納するリスト
    static List<Bitmap> bitmap = new ArrayList<>(10);           // 各レイヤーのイメージ
    static List<Canvas> canvas_bm = new ArrayList<>(10);        // 各レイヤーのcanvas
    static List<List<Structure>> layers = new ArrayList<>(10);  // レイヤー (描くものリストのリスト)
    static List<String> invisible = new ArrayList<>(10);        // 非表示にするレイヤー番号


    static int currentLayer = 0;            // 現在操作しているレイヤー番号


    public MyView(Context context){
        super(context);
    }
    public MyView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if(paint == null){                          // paintが定義されていなかったら
            paint = new Paint();                    // paintを宣言
            height = canvas.getHeight();
            width = canvas.getWidth();
        }
        if(canvas_bm.size() == 0){                  // canvas_bmに要素が存在しないならcanvasを追加
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.add(0, bmp);
            canvas_bm.add(0, new Canvas(bitmap.get(0)));
        }
        if(canvas_bm.size() != layers.size()){      // レイヤーサイズとcanvas数が一致していないならばcanvasを追加(作品を再開したときの処理)
            while(canvas_bm.size() != layers.size()){
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                bitmap.add(canvas_bm.size(), bmp);
                canvas_bm.add(canvas_bm.size(), new Canvas(bitmap.get(canvas_bm.size()-1)));
            }
        }

        if(!moving){    // 動いていないならば(GhostViewで描画していないならば)
            myDraw();   // 各レイヤーの描画を各キャンバスで行う
        }

        canvas.drawColor(Color.WHITE);          // キャンバスの一番下はホワイト
        for(int i=0; i<layers.size(); i++){                                     // canvasに各レイヤーのイメージを描く
            if(!invisible.contains(String.format("%d", i))){
                canvas.drawBitmap(bitmap.get(i), 0, 0, null);
            }
        }
        CopyCanvas.copy();                  // キャンバスのイメージをCopyCanvasにコピーする
        invalidate();                       // 再描画
    }

    // タッチイベント
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        keptlist.clear();                           // redoできなくする
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:           // 触ったとき
                if(mode == mode_clip){              // 領域指定中ならば
                    path = new Path();              // 領域指定に用いるPathを初期化
                }
                prex = event.getX();                // 座標を図形の始点に設定
                prey = event.getY();
                points.add(prex);                   // 取得した座標をpointsに追加
                points.add(prey);
                GhostView.drawghostkey = true;      // GhostViewの描画を開始
                break;
            case MotionEvent.ACTION_UP:             // 離したとき
                x = event.getX();                   // 座標を図形の終点に設定
                y = event.getY();
                points.add(x);                      // 取得した座標をpointsに追加
                points.add(y);
                moving = false;

                if(mode == mode_clip){              // 領域指定中ならば
                    path = createPath(points);      // 取得した座標の集まりからPathを作成
                    cliping = true;                 // 領域が指定されているものと表す
                    points = new ArrayList<>();     // pointsの初期化
                }
                else if(GhostView.rooping){         // ループ描画中であれば
                    points = new ArrayList<>();     // pointsの初期化
                    // GhostViewの現在のイメージを一時保存
                    GhostView.bitmap_roop = GhostView.bitmap_sub.copy(Bitmap.Config.ARGB_8888, true);
                }
                else{
                    color = MainActivity2.color;            // MainActivity2で指定された色
                    thick = MainActivity2.thick;            // MainActivity2で指定された太さ
                    newdraw = new Structure(points, color, mode, thick, path, cliping, null);   // 各パラメーターで描くオブジェクトを作成
                    layers.get(currentLayer).add(newdraw);  // 現在操作中のレイヤーに作ったオブジェクトを格納

                    points = new ArrayList<>();             // pointsの初期化

                    if(!cliping){                           // 領域指定中でないならば
                        GhostView.drawghostkey = false;     // プレビュー図形の表示を終了(領域指定中ならば、指定している領域のPathを描画し続ける必要があるため)
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:                   // スライドするとき
                moving = true;
                if(MyView.mode == MyView.mode_Line || MyView.mode == MyView.mode_clip){     // 描画モードが線のとき
                    MyView.x = event.getX();                // 座標を終点に設定
                    MyView.y = event.getY();
                    MyView.points.add(MyView.x);            // 取得した座標をpointsに格納
                    MyView.points.add(MyView.y);
                }
                GhostView.ghost_x = event.getX();         // 座標をプレビュー図形の終点に設定
                GhostView.ghost_y = event.getY();
                break;
        }

        return drawing;
    }


    // 各レイヤーの描画を行うメソッド
    static void myDraw(){
        RectF rect;                 // 楕円を描くときに用いる
        Structure drawnow;          // 描画するオブジェクト
        List<Float> drawpoints;     // 描画に用いる座標をコピーする

        paint.setColor(Color.TRANSPARENT);                                      // 色を透明に設定
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));       // モードをクリアーに
        for(int i=0; i<layers.size(); i++){                                     // 全てのレイヤーのイメージをクリアー
            canvas_bm.get(i).drawRect(0, 0, width, height, paint);
        }
        paint.setXfermode(null);                                                // モードを初期化

        for(int i=0; i<layers.size(); i++){                 // レイヤー間のループ
            for(int j=0; j<layers.get(i).size(); j++){      // レイヤー内のループ
                drawnow = layers.get(i).get(j);         // 描画するオブジェクトを得る
                canvas_bm.get(i).save();                // キャンバスの状態を保存

                drawpoints = drawnow.points;            // 描画に使用する座標の情報を取得
                paint.setColor(drawnow.color);          // 色を取得
                if(drawnow.color == Color.TRANSPARENT){                                 // 取得した色が透明であるとき
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));   // モードをクリアーに変更
                }
                paint.setStrokeWidth(drawnow.thick);            // 太さを取得
                if(drawnow.cliping){                            // オブジェクトが領域指定されているものであったら
                    canvas_bm.get(i).clipPath(drawnow.path);    // 領域内だけ描画
                }

                switch(drawnow.mode){                           // 描画モードで処理を分ける
                    case mode_Line:     // 線を描く
                        for(int k=0; k+3<drawpoints.size(); k += 2){
                            canvas_bm.get(i).drawLine(drawpoints.get(k), drawpoints.get(k+1), drawpoints.get(k+2), drawpoints.get(k+3), paint);
                        }
                        break;
                    case mode_Line2:    // 直線を描く
                        canvas_bm.get(i).drawLine(drawpoints.get(0), drawpoints.get(1), drawpoints.get(2), drawpoints.get(3), paint);
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
                    case mode_fillRound:// 塗りつぶしあり真円を描く
                        paint.setStyle(Paint.Style.FILL);
                        canvas_bm.get(i).drawCircle(drawpoints.get(0), drawpoints.get(1), getDistance(drawpoints.get(0),drawpoints.get(1),drawpoints.get(2),drawpoints.get(3)), paint);
                        break;
                    case mode_Round:    // 塗りつぶしなし真円を描く
                        paint.setStyle(Paint.Style.STROKE);
                        canvas_bm.get(i).drawCircle(drawpoints.get(0), drawpoints.get(1), getDistance(drawpoints.get(0),drawpoints.get(1),drawpoints.get(2),drawpoints.get(3)), paint);
                        break;
                    case mode_bitmap:   // bitmapを描く
                        canvas_bm.get(i).drawBitmap(drawnow.bitmap, 0,0,null);
                        break;
                    case mode_Eraser:   // 消しゴム
                        break;
                }
                if(drawnow.cliping){                        // 領域指定がされているオブジェクトであったなら
                    canvas_bm.get(i).restore();             // 指定した領域を解除する(保存した時点のキャンバス設定に戻る)
                }
                if(drawnow.color == Color.TRANSPARENT){     // 取得した色が透明なら
                    paint.setXfermode(null);                // モードを初期化
                }
            }
        }
    }

    // pointsからPathの作成
    public Path createPath(List<Float> points){
        path = new Path();
        if(points.size() >= 4){                                 // pointsに開始点以外の点が格納されているならば
            path.moveTo(points.get(0), points.get(1));          // pathの初期点
            for(int i=2; i<points.size(); i+=2){                // pathの作成
                path.lineTo(points.get(i), points.get(i+1));
            }
            path.close();                                       // pathを閉じた曲線にする
        }

        return path;
    }

    // 画面のイメージを消すメソッド
    static void AllClear(){
        if(canvas_bm.get(currentLayer).getSaveCount() > 1){             // キャンバスの設定が保存されているならば
            canvas_bm.get(currentLayer).restore();                      // 保存地点に戻る
        }
        paint_ac.setColor(Color.TRANSPARENT);                                           // 色を透明に設定
        paint_ac.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));            // モードをクリアーに設定
        canvas_bm.get(currentLayer).drawRect(0, 0, width, height, paint_ac);   // 画面を塗りつぶす
        paint_ac.setXfermode(null);                                                     // モードを初期化
        GhostView.drawghostkey = false;                                                 // GhostViewの描画をやめる
        layers.get(currentLayer).clear();                                               // 操作しているレイヤー情報をクリアー
        path.reset();                                                                   // 指定している領域を解除
        cliping = false;
    }

    // 指定している領域を解除するメソッド
    static void clipReset(){
        path = new Path();  // Pathの初期化
        cliping = false;    // 領域指定を解除
    }

    // undo機能
    static void Undo(){
        drawlist = layers.get(currentLayer);        // 現在操作中のレイヤー情報をコピー
        int index = drawlist.size() - 1;            // 最新のオブジェクトを指定する
        if(index >= 0){                             // 指定したオブジェクトが存在するならば
            Structure tmp = drawlist.get(index);    // オブジェクトをコピー
            keptlist.add(tmp);                      // オブジェクトを保持
            drawlist.remove(index);                 // オブジェクトをレイヤー情報をコピーしたものから除外
            layers.set(currentLayer, drawlist);     // レイヤー情報をコピーしたものをレイヤー情報にコピーする
        }
        paint_ac.setColor(Color.TRANSPARENT);                                           // 色を透明に設定
        paint_ac.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));            // モードをクリアーに設定                                              // 色を白に設定
        canvas_bm.get(currentLayer).drawRect(0, 0, width, height, paint_ac);   // 画面を塗りつぶす
        paint_ac.setXfermode(null);                                                     // モードを初期化
        myDraw();                                                                       // イメージの更新
    }

    // redo機能
    static void Redo(){
        int index = keptlist.size() - 1;            // オブジェクトを保持しているレイヤーの最新のオブジェクトを指定
        if(index >= 0){                             // 指定したオブジェクトが存在するのならば
            Structure tmp = keptlist.get(index);    // オブジェクトをコピー
            layers.get(currentLayer).add(tmp);      // コピーしたオブジェクトをレイヤー情報に追加
            keptlist.remove(index);                 // 保持リストからオブジェクトを除外
        }
        paint_ac.setColor(Color.TRANSPARENT);                                           // 色を透明に設定
        paint_ac.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));            // モードをクリアーに設定
        canvas_bm.get(currentLayer).drawRect(0, 0, width, height, paint_ac);   // 画面を塗りつぶす
        paint_ac.setXfermode(null);                                                     // モードを初期化
        myDraw();                                                                       // イメージの更新
    }

    // レイヤーの交換をするメソッド
    static void exchangeLayers(int index_a, int index_b){
        Collections.swap(layers, index_a, index_b);     //レイヤーの交換
        myDraw();                                       // イメージの更新
    }

    // 二点間の距離を取得するメソッド
    static float getDistance(float prex, float prey, float x, float y){
        double distance = Math.sqrt((double)((x-prex)*(x-prex) + (y-prey)*(y-prey)));   // 三平方の定理
        return (float)(distance);
    }

}
