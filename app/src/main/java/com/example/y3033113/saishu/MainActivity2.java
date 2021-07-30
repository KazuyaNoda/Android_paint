package com.example.y3033113.saishu;
// 描画ソフトのメイン部分を担当するアクティビティクラス
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;


// レイアウトなどを記述したメインクラス
public class MainActivity2 extends AppCompatActivity {
    final byte mode_draw = 0;       // 描くモード
    final byte mode_mode = 1;       // 描画モードを選ぶモード
    final byte mode_color = 2;      // 色を選ぶモード
    final byte mode_reshape = 3;    // 変形するモード
    final byte mode_layer = 4;      // レイヤ機能をつかうモード
    final byte mode_output = 5;     // アウトプットするモード

    byte viewmode = mode_draw;      // 何のモードかを格納する変数

    // 各レイアウト
    LinearLayout layout_mode;
    LinearLayout layout_color;
    LinearLayout layout_layer;
    LinearLayout layout_output;
    LinearLayout layout_reshape;
    LinearLayout layout_layerBitmap;    // レイヤー画面での画像表示を担当
    LinearLayout layout_layer2;         // レイヤー画面での二つ目の画像

    // 各ボタン
    Button button_redo;     // redo
    Button button_undo;     // undo

    Button button_mode;
    Button button_color;
    Button button_reshape;
    Button button_layer;
    Button button_output;

    Button button_Line;     // 線
    Button button_Line2;    // 直線
    Button button_fillRect; // 塗りつぶし四角形
    Button button_Rect;     // 四角形
    Button button_fillOval; // 塗りつぶし楕円
    Button button_Oval;     // 楕円
    Button button_fillRound;// 塗りつぶし真円
    Button button_Round;    // 真円
    Button button_clip;     // 領域指定
    Button button_clipreset;// 領域解除
    Button button_eraser;   // 消しゴム

    Button button_roop_rect;    // 四角形ループ
    Button button_roop_round;   // 円形ループ

    // 色を変更するボタン
    Button button_red;
    Button button_green;
    Button button_blue;
    Button button_cyan;
    Button button_magenta;
    Button button_yellow;
    Button button_black;
    Button button_white;

    Button button_ac;           // 全消し

    // 変形
    Button button_reshape_ok;       // 変形の「ok」
    Button button_reshape_no;       // 変形の「no」
    Button button_expand;           // 拡大縮小
    Button button_slide;            // 平行移動

    // レイヤー
    Button button_add_layer;        // レイヤー追加
    Button button_delete_layer;     // レイヤー削除
    Button button_exchange_layer;   // レイヤーの順番を入れ替え
    Button button_layer_up;         // レイヤー画面で表示するレイヤーを変更
    Button button_layer_down;
    Button button_visible_layer1;   // レイヤーの非表示切り替え
    Button button_visible_layer2;
    Button button_preview;          // スライドショーへの切り替え

    // アウトプット
    Button button_output_yes;       // 保存して終了
    Button button_output_no;        // 保存せず終了
    Button button_output_cancel;    // やっぱりやめる

    Button button_close;        // あらゆる場面で使われるクローズボタンmode = mode_drawに切り替える


    TextView text_R;            // RGBのうちRの値を表示するTextView
    TextView text_G;            // RGBのうちGの値を表示するTextView
    TextView text_B;            // RGBのうちBの値を表示するTextView
    TextView text_alpha;        // 不透明度を表示するTextView
    TextView text_seekBar_thick;    // 太さを表示する
    TextView text_currentLayer;     // 現在のレイヤー番号を表示する
    TextView text_layer1;           // 現在のレイヤー画面のレイヤー番号を表示する
    TextView text_layer2;

    ImageView image_layer1;     // レイヤー画面の現在のレイヤーイメージ
    ImageView image_layer2;

    EditText et_delete_num;     // 削除するレイヤーの番号
    EditText et_index_a;        // 交換するレイヤーの番号
    EditText et_index_b;

    boolean closing = false;    // 正常に保存できたかどうかを表す

    List<Bitmap> layerBitmap = new ArrayList<>(10); // レイヤー画面に表示されるbitmapのリスト


    static SeekBar seekBar_R;       // RGBのうちRの値を入力するSeekBar
    static SeekBar seekBar_G;       // RGBのうちGの値を入力するSeekBar
    static SeekBar seekBar_B;       // RGBのうちBの値を入力するSeekBar
    static SeekBar seekBar_alpha;   // 不透明度の値を入力するSeekBar
    static SeekBar seekBar_thick;

    static int value_R = 0;         // RGBのうちRの値を格納する変数
    static int value_G = 0;         // RGBのうちGの値を格納する変数
    static int value_B = 0;         // RGBのうちBの値を格納する変数
    static int value_alpha = 255;   // 不透明度の値を格納する変数

    int showingLayer = 0;           // 現在表示しているレイヤー
    static int thick = 10;          // 太さ
    static int color = Color.BLACK; // 色

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                 // activity_main.xmlを表示

        // LinerLayoutの設定
        layout_mode = (LinearLayout)findViewById(R.id.mode);
        layout_color = (LinearLayout)findViewById(R.id.color);
        layout_layer = (LinearLayout)findViewById(R.id.layer);
        layout_output = (LinearLayout)findViewById(R.id.output);
        layout_reshape = (LinearLayout)findViewById(R.id.layout_reshape);
        layout_layerBitmap = (LinearLayout)findViewById(R.id.layout_layerBitmap);
        layout_layer2 = (LinearLayout)findViewById(R.id.layout_layer2);

        // TextViewの設定
        text_R = (TextView)findViewById(R.id.textView_R);
        text_G = (TextView)findViewById(R.id.textView_G);
        text_B = (TextView)findViewById(R.id.textView_B);
        text_alpha = (TextView)findViewById(R.id.textView_alpha);
        text_seekBar_thick = (TextView)findViewById(R.id.text_seekBar_thick);
        text_currentLayer = (TextView)findViewById(R.id.text_currentLayer);
        text_layer1 = (TextView)findViewById(R.id.text_layer1);
        text_layer2 = (TextView)findViewById(R.id.text_layer2);

        et_delete_num = (EditText)findViewById(R.id.et_delete_num);
        et_index_a = (EditText)findViewById(R.id.et_index_a);
        et_index_b = (EditText)findViewById(R.id.et_index_b);

        // seekBarの設定
        seekBar_R = (SeekBar)findViewById(R.id.seekBar_R);
        seekBar_G = (SeekBar)findViewById(R.id.seekBar_G);
        seekBar_B = (SeekBar)findViewById(R.id.seekBar_B);
        seekBar_alpha = (SeekBar)findViewById(R.id.seekBar_alpha);
        seekBar_thick = (SeekBar)findViewById(R.id.seekBar_thick);

        // seekBarの最大値を設定
        seekBar_R.setMax(255);
        seekBar_G.setMax(255);
        seekBar_B.setMax(255);
        seekBar_alpha.setMax(255);
        seekBar_thick.setMax(15);

        // seekBarの初期値を設定
        seekBar_R.setProgress(0);
        seekBar_G.setProgress(0);
        seekBar_B.setProgress(0);
        seekBar_alpha.setProgress(255);
        seekBar_thick.setProgress(5);

        // seekBarのリスナ登録,イベント処理
        seekBar_R.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_R = progress;                                                 // 各valueに各seekBarの値を格納
                        String str = format("R: %d", progress);
                        text_R.setText(str);
                    }

                    // ツマミがタッチされたとき
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    // ツマミが離されたとき
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        color = Color.argb(value_alpha, value_R, value_G, value_B);
                    }
                });

        seekBar_G.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_G = progress;
                        String str = format("G: %d", progress);
                        text_G.setText(str);
                    }

                    // ツマミがタッチされたとき
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    // ツマミが離されたとき
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        color = Color.argb(value_alpha, value_R, value_G, value_B);
                    }
                });

        seekBar_B.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_B = progress;
                        String str = format("B: %d", progress);
                        text_B.setText(str);
                    }

                    // ツマミがタッチされたとき
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    // ツマミが離されたとき
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        color = Color.argb(value_alpha, value_R, value_G, value_B);
                    }
                });

        seekBar_alpha.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_alpha = progress;
                        String str = format("alpha: %d", progress);
                        text_alpha.setText(str);
                    }

                    // ツマミがタッチされたとき
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    // ツマミが離されたとき
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        color = Color.argb(value_alpha, value_R, value_G, value_B);
                    }
                });

        seekBar_thick.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        thick = progress;
                        String str = Integer.toString(thick);
                        text_seekBar_thick.setText(str);
                    }

                    // ツマミがタッチされたとき
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    // ツマミが離されたとき
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

        // ImageViewの設定
        image_layer1 = (ImageView)findViewById(R.id.image_layer1);
        image_layer2 = (ImageView)findViewById(R.id.image_layer2);

        // クリックリスナー
        image_layer1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int changenum = showingLayer;       // レイヤーを変更する
                changeLayer(changenum);
                close_button();
            }
        });

        image_layer2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int changenum = showingLayer + 1;    // レイヤーを変更する
                changeLayer(changenum);
                close_button();
            }
        });

        // Buttonの設定
        button_close = (Button)findViewById(R.id.button_close);
        button_redo = (Button)findViewById(R.id.button_redo);
        button_undo = (Button)findViewById(R.id.button_undo);

        button_mode = (Button)findViewById(R.id.button_mode);
        button_color = (Button)findViewById(R.id.button_color);
        button_reshape = (Button)findViewById(R.id.button_reshape);
        button_layer = (Button)findViewById(R.id.button_layer);
        button_output = (Button)findViewById(R.id.button_output);

        button_Line = (Button)findViewById(R.id.button_Line);
        button_Line2 = (Button)findViewById(R.id.button_Line2);
        button_fillRect = (Button)findViewById(R.id.button_fillRect);
        button_Rect = (Button)findViewById(R.id.button_Rect);
        button_fillOval = (Button)findViewById(R.id.button_fillOval);
        button_Oval = (Button)findViewById(R.id.button_Oval);
        button_fillRound = (Button)findViewById(R.id.button_fillRound);
        button_Round = (Button)findViewById(R.id.button_Round);
        button_clip = (Button)findViewById(R.id.button_clip);
        button_clipreset = (Button)findViewById(R.id.button_clipreset);
        button_eraser = (Button)findViewById(R.id.button_eraser);

        button_roop_rect = (Button)findViewById(R.id.button_roop_rect);
        button_roop_round = (Button)findViewById(R.id.button_roop_round);

        button_ac = (Button)findViewById(R.id.button_ac);

        button_red = (Button)findViewById(R.id.button_red);
        button_green = (Button)findViewById(R.id.button_green);
        button_blue = (Button)findViewById(R.id.button_blue);
        button_cyan = (Button)findViewById(R.id.button_cyan);
        button_magenta = (Button)findViewById(R.id.button_magenta);
        button_yellow = (Button)findViewById(R.id.button_yellow);
        button_black = (Button)findViewById(R.id.button_black);
        button_white = (Button)findViewById(R.id.button_white);

        button_reshape_ok = (Button)findViewById(R.id.button_reshape_ok);
        button_reshape_no = (Button)findViewById(R.id.button_reshape_no);
        button_expand = (Button)findViewById(R.id.button_expand);
        button_slide = (Button)findViewById(R.id.button_slide);

        button_add_layer = (Button)findViewById(R.id.button_add_layer);
        button_exchange_layer = (Button)findViewById(R.id.button_exchange_layer);
        button_delete_layer = (Button)findViewById(R.id.button_delete_layer);
        button_layer_up = (Button)findViewById(R.id.button_layer_up);
        button_layer_down = (Button)findViewById(R.id.button_layer_down);
        button_visible_layer1 = (Button)findViewById(R.id.button_visible_layer1);
        button_visible_layer2 = (Button)findViewById(R.id.button_visible_layer2);
        button_preview = (Button)findViewById(R.id.button_preview);

        button_output_yes = (Button)findViewById(R.id.button_output_yes);
        button_output_no = (Button)findViewById(R.id.button_output_no);
        button_output_cancel = (Button)findViewById(R.id.button_output_cancel);

        // Buttonのリスナ登録, イベント処理

        button_ac.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.AllClear();  // 画面をクリア
                close_button();
            }
        });

        button_redo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){
                    MyView.Redo();
                }
            }
        });

        button_undo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){
                    MyView.Undo();
                }
            }
        });

        button_Line.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Line;     // 線を引くモードにする
                button_mode.setText("~");
                close_button();
            }
        });

        button_Line2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Line2;     // 直線を引くモードにする
                button_mode.setText("-");
                close_button();
            }
        });

        button_fillRect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_fillRect; // 塗りつぶしあり四角形を描くモードにする
                button_mode.setText("■");
                close_button();
            }
        });

        button_Rect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Rect;     // 塗りつぶしなし四角形を描くモードにする
                button_mode.setText("□");
                close_button();
            }
        });

        button_fillOval.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_fillOval; // 塗りつぶしあり楕円を描くモードにする
                button_mode.setText("●da");
                close_button();
            }
        });

        button_Oval.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Oval;     // 塗りつぶしなし楕円を描くモードにする
                button_mode.setText("◯da");
                close_button();
            }
        });

        button_fillRound.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_fillRound;    // 塗りつぶし真円を描くモードにする
                button_mode.setText("●sin");
                close_button();
            }
        });

        button_Round.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Round;        // 塗りつぶしなし真円を描くモードにする
                button_mode.setText("◯sin");
                close_button();
            }
        });

        button_clip.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MyView.mode = MyView.mode_clip;     // 領域指定をするモードにする
                button_mode.setText("c");
                close_button();
            }
        });

        button_clipreset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MyView.clipReset();                 // 領域解除する
                close_button();
            }
        });

        button_eraser.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                color = Color.TRANSPARENT;          // 色を透明にする
                button_color.setText("era");        // 消しゴム表示
            }
        });

        button_roop_rect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(!GhostView.roopRect){            // ループボタンが押されていないとき
                    GhostView.rooping = true;
                    GhostView.roopRect = true;
                    GhostView.roopRound = false;
                }
                else{                               // ループボタンが押されているとき
                    GhostView.stopRoop();           // ループモードを終了
                    GhostView.rooping = false;
                    GhostView.roopRect = false;
                }
                close_button();                     // レイアウトを閉じる
            }
        });

        button_roop_round.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(!GhostView.roopRound){           // ループボタンが押されていないとき
                    GhostView.rooping = true;
                    GhostView.roopRound = true;
                    GhostView.roopRect = false;

                }
                else{                               // ループボタンが押されているとき
                    GhostView.stopRoop();           // ループモードを終了
                    GhostView.rooping = false;
                    GhostView.roopRound = false;
                }
                close_button();                     // レイアウトを閉じる
            }
        });

        button_mode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){                      // 描画モードのとき
                    viewmode = mode_mode;
                    MyView.drawing = false;
                    layout_mode.setVisibility(View.VISIBLE);    // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
                else if(viewmode == mode_mode){                 // すでにボタンが押されているとき
                    close_button();                             // レイアウトを閉じる
                }
                else{                                           // そのほかのモードのとき
                    close_button();                             // レイアウトを閉じる
                    viewmode = mode_mode;
                    MyView.drawing = false;
                    layout_mode.setVisibility(View.VISIBLE);    // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_color.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){                      // 描画モードのとき
                    viewmode = mode_color;
                    MyView.drawing = false;
                    layout_color.setVisibility(View.VISIBLE);   // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
                else if(viewmode == mode_color){                // すでにボタンが押されているとき
                    close_button();                             // レイアウトを閉じる
                }
                else{                                           // そのほかのモードのとき
                    close_button();                             // レイアウトを閉じる
                    viewmode = mode_color;
                    MyView.drawing = false;
                    layout_color.setVisibility(View.VISIBLE);   // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_reshape.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){                      // 描画モードのとき
                    viewmode = mode_reshape;
                    MyView.drawing = false;
                    layout_reshape.setVisibility(View.VISIBLE); // レイアウトを表示
                }
                else if(viewmode == mode_reshape){              // すでにボタンが押されているとき
                    close_button();                             // レイアウトを閉じる
                }
                else{                                           // そのほかのモードのとき
                    close_button();                             // レイアウトを閉じる
                    viewmode = mode_reshape;
                    MyView.drawing = false;
                    layout_reshape.setVisibility(View.VISIBLE);  // レイアウトを表示
                }
            }
        });

        button_layer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){                      // 描画モードのとき
                    viewmode = mode_layer;
                    MyView.drawing = false;
                    showBitmap();                               // レイヤーのイメージを表示
                    layout_layer.setVisibility(View.VISIBLE);   // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
                else if(viewmode == mode_layer){                // すでにボタンが押されているとき
                    close_button();                             // レイアウトを閉じる
                }
                else{                                           // そのほかのモードのとき
                    close_button();                             // レイアウトを閉じる
                    viewmode = mode_layer;
                    MyView.drawing = false;
                    showBitmap();                               // レイヤーのイメージを表示
                    layout_layer.setVisibility(View.VISIBLE);   // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_output.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){                      // 描画モードのとき
                    viewmode = mode_output;
                    MyView.drawing = false;
                    layout_output.setVisibility(View.VISIBLE);  // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
                else if(viewmode == mode_output){               // すでにボタンが押されているとき
                    close_button();                             // レイアウトを閉じる
                }
                else{                                           // そのほかのモードのとき
                    close_button();                             // レイアウトを閉じる
                    viewmode = mode_output;
                    MyView.drawing = false;
                    layout_output.setVisibility(View.VISIBLE);  // レイアウトを表示
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_add_layer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // レイヤー情報を初期化し、生成したビットマップを格納する
                MyView.layers.add(MyView.currentLayer + 1, new ArrayList<>(64));
                MyView.bitmap.add(MyView.currentLayer + 1, Bitmap.createBitmap(MyView.width, MyView.height, Bitmap.Config.ARGB_8888));
                MyView.canvas_bm.add(MyView.currentLayer + 1, new Canvas(MyView.bitmap.get(MyView.currentLayer + 1)));
                text_currentLayer.setText(String.format("current: %d/%d", MyView.currentLayer+1, MyView.layers.size()));

                showBitmap();                  // レイヤーのイメージを表示
                renewBitmap(0);     // レイヤーの表示番号を変更

                Toast.makeText(MainActivity2.this, "add new layer", Toast.LENGTH_SHORT).show();
            }
        });

        button_exchange_layer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(et_index_a.getText() != null && et_index_b.getText() != null){   // テキストが存在するとき
                    String s_number;                                // テキストに書かれた数字(String)
                    int index_a;                                    // 交換元のインデックス
                    int index_b;                                    // 交換先のインデックス

                    s_number = et_index_a.getText().toString();     // テキストの文字を取得し、Stringに変換
                    index_a = Integer.valueOf(s_number) - 1;

                    s_number = et_index_b.getText().toString();     // テキストの文字を取得し、Stringに変換
                    index_b = Integer.valueOf(s_number) - 1;

                    if(MyView.layers.size() > index_a){                 // レイヤーサイズを超えていなければ
                        if(MyView.layers.size() > index_b){
                            MyView.exchangeLayers(index_a, index_b);    // レイヤー情報を交換
                            Toast.makeText(MainActivity2.this, "success", Toast.LENGTH_SHORT).show();
                        }
                        else{                                           // index_bがレイヤーサイズを超えているとき
                            Toast.makeText(MainActivity2.this, "too large", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{                                               // index_aがレイヤーサイズを超えているとき
                        Toast.makeText(MainActivity2.this, "too large", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        button_delete_layer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String s_delete_num = et_delete_num.getText().toString();       // テキストを取得し、Stringに変換
                int delete_num = Integer.valueOf(s_delete_num) - 1;
                if(layerBitmap.size() > delete_num && layerBitmap.size() != 1){ // delete_numがレイヤーサイズを超えておらず、レイヤーサイズが２以上のとき
                    deleteLayer(delete_num);                                    // レイヤーの削除
                    showBitmap();                                               // レイヤーイメージを表示
                    MyView.myDraw();                                            // キャンバスの更新
                }
                else{                                                           // delete_numがレイヤーサイズを超えているとき
                    Toast.makeText(MainActivity2.this, "too large", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_layer_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(layerBitmap.size() > showingLayer + 2){  // レイヤーサイズを超えないならば
                    renewBitmap(1);              // レイヤーの表示番号を更新
                    showBitmap();                           // レイヤーイメージを表示
                }
            }
        });

        button_layer_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(showingLayer > 0){               // レイヤー番号が０未満にならないならば
                    renewBitmap(-1);     // レイヤーの表示番号を更新
                    showBitmap();                   // レイヤーイメージの表示
                }
            }
        });

        button_visible_layer1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(MyView.invisible.contains(String.format("%d",showingLayer))){        // すでに非表示リストに入っているならば
                MyView.invisible.remove(String.format("%d",showingLayer));              // 非表示リストから除外
                }
                else{                                                                   // 非表示リストに入っていないならば
                    MyView.invisible.add(0, String.format("%d",showingLayer));    // 非表示リストに追加
                }
                showBitmap();                                                           // レイヤーイメージの表示
            }
        });

        button_visible_layer2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(MyView.invisible.contains(String.format("%d",showingLayer + 1))){        // すでに非表示リストに入っているならば
                    MyView.invisible.remove(String.format("%d",showingLayer + 1));          // 非表示リストから除外
                }
                else{                                                                       // 非表示リストに入っていないならば
                    MyView.invisible.add(0, String.format("%d",showingLayer + 1));    // 非表示リストに追加
                }
                showBitmap();                                                               // レイヤーイメージの表示
            }
        });

        button_preview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Activity3を起動 スライドショーのようなもの
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                Toast.makeText(MainActivity2.this, "click!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        button_reshape_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ReshapeView.decide();   // bitmapを生成
                ReshapeView.end();      // 変形を終了
                MyView.myDraw();        // canvasの更新
                close_button();         // レイアウトを閉じる
            }
        });

        button_reshape_no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ReshapeView.end();      // 変形を終了
                MyView.myDraw();        // canvasの更新
                close_button();         // レイアウトを閉じる
            }
        });

        button_expand.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                button_expand.setBackgroundColor(Color.rgb(33, 150, 243));
                button_slide.setBackgroundColor(Color.rgb(73, 190, 255));
                Toast.makeText(MainActivity2.this, "expand selected", Toast.LENGTH_SHORT).show();
                ReshapeView.expand();   // 拡大縮小機能
            }
        });

        button_slide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                button_slide.setBackgroundColor(Color.rgb(33, 150, 243));
                button_expand.setBackgroundColor(Color.rgb(73, 190, 255));
                Toast.makeText(MainActivity2.this, "slide selected", Toast.LENGTH_SHORT).show();
                ReshapeView.slide();    // 平行移動機能
            }
        });

        button_output_yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                closing = true;
                save();         // 保存 (を試みる)

                if(closing){    // 正常に保存できるならば
                    quit();     // 終了
                    closing = false;
                }
                else{           // 正常に保存できないならば
                    Toast.makeText(MainActivity2.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_output_no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                quit(); // Activityの終了
            }
        });

        button_output_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                close_button(); // レイアウトを閉じる
            }
        });

        button_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                close_button(); // レイアウトを閉じる
            }
        });

        button_red.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.RED;
                setColor(255, 0, 0);
            }
        });

        button_green.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.GREEN;
                setColor(0, 255, 0);
            }
        });

        button_blue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.BLUE;
                setColor(0, 0, 255);
            }
        });

        button_cyan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.CYAN;
                setColor(0, 255, 255);
            }
        });

        button_magenta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.MAGENTA;
                setColor(255, 0, 255);
            }
        });

        button_yellow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.YELLOW;
                setColor(255, 255, 0);
            }
        });

        button_black.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.BLACK;
                setColor(0, 0, 0);
            }
        });

        button_white.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                color = Color.WHITE;
                setColor(255, 255, 255);
            }
        });
    }

    // 色を指定するメソッド
    void setColor(int r, int g, int b){
        // シークバーの位置更新
        seekBar_R.setProgress(r);
        seekBar_G.setProgress(g);
        seekBar_B.setProgress(b);
        // RGBの値の表示
        String str_R = format("R: %d", r);
        String str_G = format("G: %d", g);
        String str_B = format("B: %d", b);
        text_R.setText(str_R);
        text_G.setText(str_G);
        text_B.setText(str_B);
        layout_color.setVisibility(View.INVISIBLE); // レイアウトを閉じる
        button_close.setVisibility(View.INVISIBLE);
        button_color.setBackgroundColor(color);     // ボタンの色を指定した色に変更する
        button_color.setText("");
        viewmode = mode_draw;
        MyView.drawing = true;
    }

    // 現在開いているレイアウトを閉じるメソッド
    void close_button(){
        switch(viewmode){
            case mode_mode:
                layout_mode.setVisibility(View.INVISIBLE);
                break;
            case mode_color:
                layout_color.setVisibility(View.INVISIBLE);
                button_color.setBackgroundColor(color);
                button_color.setText("");
                break;
            case mode_reshape:
                layout_reshape.setVisibility(View.INVISIBLE);
                break;
            case mode_layer:
                layout_layer.setVisibility(View.INVISIBLE);
                break;
            case mode_output:
                layout_output.setVisibility(View.INVISIBLE);
                break;
        }
        viewmode = mode_draw;
        MyView.drawing = true;
        if(button_close.getVisibility() == View.VISIBLE){   // クローズボタンが表示されているとき
            button_close.setVisibility(View.INVISIBLE);     // 閉じる
        }
    }

    // 表示しているレイヤーを更新するメソッド
    void renewBitmap(int up_or_down){
        image_layer1.setImageBitmap(layerBitmap.get(showingLayer + up_or_down));
        image_layer2.setImageBitmap(layerBitmap.get(showingLayer + 1 + up_or_down));
        text_layer1.setText(String.format("%d", showingLayer + 1 + up_or_down));
        text_layer2.setText(String.format("%d", showingLayer + 2 + up_or_down));

        showingLayer += up_or_down;
    }

    // レイヤーイメージを表示するメソッド
    void showBitmap(){
        layerBitmap = new ArrayList<Bitmap>(MyView.bitmap);

        image_layer1.setImageBitmap(layerBitmap.get(showingLayer));
        text_layer1.setText(String.format("%d", showingLayer + 1));

        if(layerBitmap.size() == showingLayer + 1){                         // レイヤーサイズを超えなければ
            layout_layer2.setVisibility(View.INVISIBLE);
        }
        else{                                                               // レイヤーサイズを超えていれば
            layout_layer2.setVisibility(View.VISIBLE);
            image_layer2.setImageBitmap(layerBitmap.get(showingLayer + 1));
            text_layer2.setText(String.format("%d", showingLayer + 2));
        }

        if(layerBitmap.size() > showingLayer + 2){                          // レイヤーサイズが超えなければ
            button_layer_up.setBackgroundColor(Color.rgb(100, 0, 255));
        }
        else{                                                               // レイヤーサイズを超えていれば
            button_layer_up.setBackgroundColor(Color.rgb(150, 150, 150));
        }
        if(showingLayer > 0){                                               // レイヤーサイズが１以上であれば
            button_layer_down.setBackgroundColor(Color.rgb(100, 0, 255));
        }
        else{                                                               // レイヤーサイズが１未満であれば
            button_layer_down.setBackgroundColor(Color.rgb(150, 150, 150));
        }

        if(MyView.invisible.contains(String.format("%d", showingLayer))){   // 非表示リストに存在するなら
            button_visible_layer1.setBackgroundColor(Color.rgb(100, 100, 100));
        }
        else{                                                               // 非表示リストに存在しないのならば
            button_visible_layer1.setBackgroundColor(Color.rgb(100, 200, 100));
        }
        if(MyView.invisible.contains(String.format("%d", showingLayer+1))){ // 非表示リストに存在するなら
            button_visible_layer2.setBackgroundColor(Color.rgb(100, 100, 100));
        }
        else{                                                               // 非表示リストに存在しないのならば
            button_visible_layer2.setBackgroundColor(Color.rgb(100, 200, 100));
        }
    }

    // 現在選択しているレイヤーを変更するメソッド
    void changeLayer(int changenum){
        MyView.currentLayer = changenum;
        text_currentLayer.setText(String.format("current: %d/%d", MyView.currentLayer+1, MyView.layers.size()));
    }

    // レイヤーを削除するメソッド
    void deleteLayer(int layernum){
        if(layerBitmap.size()-1 - showingLayer+1 <= 2){         // レイアウトに収まる範囲であるならば

            if(showingLayer != 0){                              // レイヤー番号が0にならないならば
                showingLayer -= 1;
            }
            if(layerBitmap.size()-1 == MyView.currentLayer){    // 現在表示しているレイヤーならば
                if(MyView.currentLayer != 0){
                    MyView.currentLayer -= 1;
                }
            }
        }

        // 削除
        MyView.layers.remove(layernum);
        MyView.canvas_bm.remove(layernum);
        MyView.bitmap.remove(layernum);
        text_currentLayer.setText(String.format("current: %d/%d", MyView.currentLayer+1, MyView.layers.size()));
    }

    // レイヤー情報を保存(出力)するメソッド
    void save(){
        String workNum = Integer.toString(MainActivity.workNum);
        String filePath = MainActivity.filePath;                        // 出力先
        String output = "";                                             // 出力する文章
        output += Integer.toString(MyView.width) + "\n";                // canvasの横幅
        output += Integer.toString(MyView.height) + "\n";               // canvasの縦幅
        output += Integer.toString(MyView.layers.size()) + "\n";        // レイヤーのサイズ
        output += Integer.toString(MyView.invisible.size()) + "\n";     // 非表示リストのサイズ
        for(int i=0; i<MyView.invisible.size(); i++) {
            output += MyView.invisible.get(i) + "\n";                   // 非表示レイヤー番号を記述
        }

        try{
            File file = new File(getApplicationContext().getFilesDir(), filePath + ".txt");
            FileWriter writer = new FileWriter(file);
            writer.write(output);                                       // 記述
            writer.flush();
            writer.close();

            // canvasのイメージをpng出力
            file = new File(getApplicationContext().getFilesDir(), filePath + ".png");
            FileOutputStream outStream = new FileOutputStream(file);
            CopyCanvas.bitmap_copy.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

            // レイヤーのイメージをpng出力
            for(int i=0; i<MyView.bitmap.size(); i++){
                file = new File(getApplicationContext().getFilesDir(), filePath + "_" + Integer.toString(i+1) + ".png");
                outStream = new FileOutputStream(file);
                MyView.bitmap.get(i).compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            }

        }catch(ExceptionInInitializerError | IOException e){
            e.printStackTrace();
            closing = false;                                    // 出力できないのならば、そのことをboolean変数で表す
        }
    }

    // Activityの終了
    void quit(){
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}