package com.example.y3033113.saishu;
// レイヤーイメージの拡大縮小, 平行移動をするクラス
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

import java.util.ArrayList;

public class ReshapeView extends View {
    static Bitmap bitmap;               // レイヤーのイメージ
    static Bitmap bm_rv;                // キャンバスのイメージ(ダブルバッファリング)
    static Bitmap reshaped;             // 変形後のイメージ
    static Canvas canvas_rv;            // ダブルバッファリングに用いるキャンバス
    Matrix matrix;                      // 変形に用いるmatrix
    static int width;                   // キャンバスの横幅
    static int height;                  // キャンバスの縦幅

    float x;                            // 元の座標
    float y;
    float next_x;                       // 移動先の座標
    float next_y;
    float ratio = 1.0f;                 // 拡大倍率
    static float pre_ratio = 1.0f;      // 過去の拡大倍率
    float dx = 0;                       // 遷移距離(正負あり)
    float dy = 0;
    static float pre_dx = 0;            // 過去の遷移距離(正負あり)
    static float pre_dy = 0;

    static boolean drawReshape = false; // 変形中かどうか
    static boolean expanding = false;   // 拡大縮小中かどうか
    static boolean sliding = false;     // 平行移動中かどうか
    static boolean touching_rv = false; // タッチイベントのオンオフ

    public ReshapeView(Context context) {
        super(context);
    }
    public ReshapeView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas){
        if(drawReshape){                // 変形中ならば
            if(canvas_rv == null){      // キャンバスが存在しないのならば
                width = MyView.width;   // メインキャンバスの横幅を取得
                height = MyView.height; // メインキャンバスの縦幅を取得
                // bitmap, canvasの作成・設定
                bm_rv = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                canvas_rv = new Canvas(bm_rv);
            }

            // 画面を白で塗りつぶす(TRANSPARENT&Mode.CLEARを試したが、Mode.CLEARにかかわらずキャンバスの裏地が出てきてしまったため白にした)
            canvas.drawColor(Color.WHITE);
            canvas_rv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            // レイヤーイメージを描画(TRANSPARENT&Mode.CLEARでクリアーできなかったため追加)
            for(int i=0; i<MyView.bitmap.size(); i++){
                if(i != MyView.currentLayer){
                    canvas_rv.drawBitmap(MyView.bitmap.get(i), 0, 0, null);
                }
            }
            if(matrix == null){                                         // matrixが存在しないのならば
                canvas_rv.drawBitmap(bitmap, 0, 0,null); // そのまま描画
            }
            else{                                                       // matrixが存在するならば
                canvas_rv.drawBitmap(bitmap, matrix,null);        // matrixを利用して描画
            }
            canvas_rv.drawBitmap(bm_rv, 0, 0, null);    // 自分自身のイメージを描画(画面更新用)
            canvas.drawBitmap(bm_rv, 0, 0, null);       // キャンバスにイメージを描画
        }
        invalidate();   // 再描画
    }

    // タッチイベント
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(touching_rv){                        // 変形が可能ならば
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:   // タッチしたとき
                    x = event.getX();           // 座標を取得
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:     // 離したとき
                    pre_ratio = ratio;          // 現在の拡大率を過去の拡大率にする
                    pre_dx = dx;                // 現在の遷移距離を過去の遷移距離にする
                    pre_dy = dy;
                    reshaped = bm_rv.copy(Bitmap.Config.ARGB_8888, true);   // 現在のイメージを変形後のイメージとする
                    break;
                case MotionEvent.ACTION_MOVE:   // スライドしているとき
                    next_x = event.getX();      // 座標を取得
                    next_y = event.getY();

                    matrix = new Matrix();      // matrixを初期化
                    if(expanding){              // 拡大縮小中ならば
                        ratio = (float)(((float)(height+y-next_y))/height * pre_ratio); // 取得した座標の距離から拡大率を計算
                    }
                    else if(sliding){           // 平行移動中ならば
                        dx = next_x - x + pre_dx;   // 取得した座標の距離から遷移距離を計算
                        dy = next_y - y + pre_dy;
                    }
                    matrix.setTranslate(dx, dy);                                // 平行移動
                    matrix.preScale(ratio, ratio, width/2, height/2);   // 拡大縮小
                    break;
            }
        }
        return touching_rv;
    }

    // 拡大縮小機能をオンにするメソッド
    // レイヤーイメージを取得し、各フラグを設定
    static void expand(){
        bitmap = MyView.bitmap.get(MyView.currentLayer).copy(Bitmap.Config.ARGB_8888, true);
        drawReshape = true;
        touching_rv = true;

        sliding = false;
        expanding = true;
    }

    // 平行移動機能をオンにするメソッド
    // レイヤーイメージを取得し、各フラグを設定
    static void slide(){
        bitmap = MyView.bitmap.get(MyView.currentLayer).copy(Bitmap.Config.ARGB_8888, true);
        drawReshape = true;
        touching_rv = true;

        expanding = false;
        sliding = true;
    }

    // 変形後のイメージを確定するメソッド
    static void decide(){
        // レイヤーイメージの中にレイヤー情報がすべて含まれているため、
        // レイヤー情報を初期化してから変形後のイメージをレイヤー情報に追加
        MyView.keptlist = new ArrayList<>(64);
        MyView.layers.set(MyView.currentLayer, new ArrayList<>());
        MyView.layers.get(MyView.currentLayer).add(new Structure(null, 0, MyView.mode_bitmap, 0, null, false, reshaped));
    }

    // 変形機能を終了するメソッド
    static void end(){
        if(canvas_rv != null){                                              // キャンバスが存在するのならば
            canvas_rv.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);  // キャンバスを透明でクリアー
        }
        // 各値・フラグを初期化
        pre_ratio = 1.0f;
        pre_dx = 0;
        pre_dy = 0;
        touching_rv = false;
        drawReshape = false;
        expanding = false;
        sliding = false;
    }
}
