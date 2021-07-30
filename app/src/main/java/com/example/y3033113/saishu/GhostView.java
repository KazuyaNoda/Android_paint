package com.example.y3033113.saishu;
// プレビュー図形・ループ描画を担当するクラス
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

public class GhostView extends View {
    static boolean drawghostkey = false;        // プレビュー図形の表示を開始/終了するためのboolean型変数
    List<Float> points = new ArrayList<>();     // 描画に用いる座標
    static Paint paint;
    static Canvas canvas_sub;                   // このクラスで使用するcanvas(ダブルバッファリング)
    static Bitmap bitmap_sub;                   // このクラスで使用するbitmap
    static Bitmap bitmap_roop;                  // ループ描画するときに用いるbitmap
    static boolean rooping = false;             // ループ描画かどうか
    static boolean roopRect = false;            // ループRectかどうか
    static boolean roopRound = false;           // ループRoundかどうか

    static float prex = 0;                      // 初期座標
    static float prey = 0;
    static float ghost_x = 0;                   // 動かした先の座標
    static float ghost_y = 0;


    static int width;                           // キャンバスの横幅
    static int height;                          // キャンバスの縦幅

    public GhostView(Context context) {
        super(context);
    }
    public GhostView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(paint == null){          // pantが無ければ
            paint = new Paint();
            width = MyView.width;   // MyViewからキャンバスの横幅・縦幅を取得
            height = MyView.height;
            // bitmapの作成・canvasに設定
            bitmap_sub = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            canvas_sub = new Canvas(bitmap_sub);
        }

        if(!rooping){                                                       // ループ描画でないならば
            canvas_sub.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // 透明で塗りつぶす
            canvas.drawBitmap(bitmap_sub, 0, 0, null);
        }

        if(drawghostkey || rooping){                    // GhostViewの描画を開始、または、ループ描画のとき
            paint.setStrokeWidth(MainActivity2.thick);  // ブラシの太さの設定
            paint.setColor(MainActivity2.color);        // 色の設定


            if(!com.example.y3033113.saishu.MyView.points.isEmpty()){   // 座標が取得されていれば
                prex = MyView.points.get(0);                            // 初期座標を設定
                prey = MyView.points.get(1);
            }

            if(rooping){                                                                // ループ描画ならば
                if(MainActivity2.color == Color.TRANSPARENT){                           // 色が透明ならば
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));   // モードをクリアーにする
                }

                if(roopRect){                                                       // ループRectならば
                    // 描画範囲を青色で表示
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLUE);
                    canvas.drawRect(0, 0, width/2, height/2, paint);

                    paint.setColor(MainActivity2.color);                            // 色を設定
                    // 縦２個横２個にループ描画
                    for(int i=0; i<2; i++){
                        for(int j=0; j<2; j++){
                            canvas_sub.save();                                      // キャンバスの状態を保存
                            canvas_sub.translate(i*width/2, j*height/2);    // キャンバスの座標を変更する
                            if(MyView.moving){                                      // 書き込み中ならば
                                drawghost();                                        // 描画
                            }
                            canvas_sub.restore();                                   // キャンバスの状態を保存した時点に戻す
                        }
                    }
                }
                else if(roopRound){                                                 // ループRoundならば
                    // 描画できる範囲を青で表示
                    // 遷移先の範囲を赤で表示
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.BLUE);
                    canvas.drawLine(0, 0, width/2, height/2, paint);
                    canvas.drawLine(width/2, height/2, width, 0, paint);
                    paint.setColor(Color.RED);
                    canvas.drawLine(width/2, height/2, width, height, paint);
                    canvas.drawLine(width/2, height/2, width, height/2, paint);

                    paint.setColor(MainActivity2.color);                            // 色を設定
                    // 円形に６回ループ描画
                    for(int i=0; i<6; i++){
                        canvas_sub.save();                                          // キャンバスの状態を保存
                        canvas_sub.translate(width/2, height/2);            // キャンバスの座標を変更する
                        canvas_sub.rotate(60*i);                            // キャンバスを回す
                        if(MyView.moving){                                          // 書き込み中ならば
                            drawghost();                                            // 描画
                        }
                        canvas_sub.restore();                                       // キャンバスの設定を保存時点に戻す
                    }
                }
                if(MainActivity2.color == Color.TRANSPARENT){                       // 色が透明ならば
                    paint.setXfermode(null);                                        // モードを初期化
                }
                canvas.drawBitmap(bitmap_sub, 0, 0, null);          // 描画したものをキャンバスに表示
            }
            else if(drawghostkey){                                                  // GhostViewの描画を開始したとき
                paint.setColor(MainActivity2.color);                                // 色を設定
                if(MainActivity2.color == Color.TRANSPARENT){                       // 色が透明のとき
                    paint.setColor(Color.WHITE);                                    // 色を白に設定(TRANSPARENT&Mode.CLEARで試みたが、下地が表示されてしまった)
                }
                if(MyView.moving){                                                  // 描きこみ中ならば
                    drawghost();                                                    // 描画
                    canvas.drawBitmap(bitmap_sub, 0, 0, null);      // キャンバスに表示
                }
                if(!MyView.path.isEmpty()){                                         // pathが存在するならば
                    paint.setColor(Color.GREEN);                                    // 緑色、
                    paint.setStyle(Paint.Style.STROKE);                             // 線で
                    canvas.drawPath(MyView.path, paint);                            // pathを描画
                }
            }
        }
        invalidate();       // 再描画
    }

    // canvas_subに描画をするメソッド
    // 塗りつぶしなし図形の描画はsetStyle(Paint.Style.STROKE)を利用していたが、連続で描画されるようになってしまった。
    // 過去のバージョンに戻って原因を探ってみたが、それらしいものは見つからなかった。
    // Print文を挟んでもPrint文が連続で出力されることはなく、
    // setStyle()を変更するだけでバグが発生するため、作品の品質は落ちるがすべてFILLにした。
    void drawghost(){
        RectF rect;                     // 描画に使用する
        switch(MyView.mode){            // 描画モードによって処理を分ける
            case MyView.mode_Line:      // 線
                points = MyView.points;
                for(int i=0; i+3<points.size(); i += 2){
                    canvas_sub.drawLine(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3), paint);
                }
                break;
            case MyView.mode_Line2:     // 直線
                canvas_sub.drawLine(prex, prey, ghost_x, ghost_y, paint);
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
                paint.setStyle(Paint.Style.FILL);
                canvas_sub.drawOval(rect, paint);
                break;
            case MyView.mode_fillRound:      // 塗りつぶしあり真円を描く
                paint.setStyle(Paint.Style.FILL);
                canvas_sub.drawCircle(prex, prey, MyView.getDistance(prex, prey, ghost_x, ghost_y), paint);
                break;
            case MyView.mode_Round:      // 塗りつぶしなし真円を描く
                paint.setStyle(Paint.Style.FILL);
                canvas_sub.drawCircle(prex, prey, MyView.getDistance(prex, prey, ghost_x, ghost_y), paint);
                break;
            case MyView.mode_clip:          // 領域指定
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.GREEN);
                points = MyView.points;
                for(int i=0; i+3<points.size(); i += 2){    // 線で描画(領域を描き途中のとき)
                    canvas_sub.drawLine(points.get(i), points.get(i+1), points.get(i+2), points.get(i+3), paint);
                }
                canvas_sub.drawPath(MyView.path, paint);    // Pathで描画(領域を描いた後閉じた曲線を描画するため)
                break;
        }
    }

    // ループ描画をストップするメソッド
    static void stopRoop(){
        // ループ描画したbitmapをレイヤー情報に入れる
        MyView.keptlist = new ArrayList<>(64);
        MyView.layers.get(MyView.currentLayer).add(new Structure(null, 0, MyView.mode_bitmap, 0, null, false, bitmap_roop));

        canvas_sub.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // キャンバスをクリアー
        MyView.myDraw();                                                // メインキャンバスの表示を更新
    }
}
