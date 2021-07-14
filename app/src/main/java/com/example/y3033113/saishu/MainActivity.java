package com.example.y3033113.saishu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static java.lang.String.format;


// レイアウトなどを記述したメインクラス
public class MainActivity extends AppCompatActivity {
    final byte mode_draw = 0;
    final byte mode_mode = 1;
    final byte mode_color = 2;
    final byte mode_tool = 3;
    final byte mode_layer = 4;
    final byte mode_output = 5;
    final byte mode_expand = 6;

    byte viewmode = mode_draw;

    LinearLayout layout_mode;
    LinearLayout layout_color;
    LinearLayout layout_tool;
    LinearLayout layout_layer;
    LinearLayout layout_output;
    LinearLayout layout_expand;

    Button button_redo;
    Button button_undo;

    Button button_mode;
    Button button_color;
    Button button_tool;
    Button button_layer;
    Button button_output;

    Button button_Line;         // 線を引くモードに変更させるボタン
    Button button_fillRect;     // 塗りつぶしあり四角形を描くモードに変更させるボタン
    Button button_Rect;         // 塗りつぶしなし四角形を描くモードに変更させるボタン
    Button button_fillOval;     // 塗りつぶしあり楕円を描くモードに変更させるボタン
    Button button_Oval;         // 塗りつぶしなし楕円を描くモードに変更させるボタン
    Button button_clip;
    Button button_clipreset;
    Button button_eraser;

    Button button_red;
    Button button_green;
    Button button_blue;
    Button button_cyan;
    Button button_magenta;
    Button button_yellow;
    Button button_black;
    Button button_white;

    Button button_ac;           // allclear機能を持たせるボタン
    Button button_expand;

    Button button_expand_ok;
    Button button_expand_no;

    Button button_add_layer;
    Button button_change_layer;
    Button button_exchange_layer;
    Button button_close;


    TextView text_R;            // RGBのうちRの値を表示するTextView
    TextView text_G;            // RGBのうちGの値を表示するTextView
    TextView text_B;            // RGBのうちBの値を表示するTextView
    TextView text_alpha;        // 不透明度を表示するTextView
    TextView text_seekBar_thick;
    TextView text_currentLayer;

    EditText et_number;
    EditText et_index_a;
    EditText et_index_b;

    static SeekBar seekBar_R;       // RGBのうちRの値を入力するSeekBar
    static SeekBar seekBar_G;       // RGBのうちGの値を入力するSeekBar
    static SeekBar seekBar_B;       // RGBのうちBの値を入力するSeekBar
    static SeekBar seekBar_alpha;   // 不透明度の値を入力するSeekBar
    static SeekBar seekBar_thick;
    static SeekBar seekBar_expand;

    static int value_R = 0;         // RGBのうちRの値を格納する変数
    static int value_G = 0;         // RGBのうちGの値を格納する変数
    static int value_B = 0;         // RGBのうちBの値を格納する変数
    static int value_alpha = 255;   // 不透明度の値を格納する変数

    String s_number;
    int index;
    int index_a;
    int index_b;
    static int thick = 10;
    static int color = Color.BLACK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                 // activity_main.xmlを表示
        MyView.layers.add(new ArrayList<Structure>(64));

        // LinerLayoutの設定
        layout_mode = (LinearLayout)findViewById(R.id.mode);
        layout_color = (LinearLayout)findViewById(R.id.color);
        layout_tool = (LinearLayout)findViewById(R.id.tool);
        layout_layer = (LinearLayout)findViewById(R.id.layer);
        layout_output = (LinearLayout)findViewById(R.id.output);
        layout_expand = (LinearLayout)findViewById(R.id.layout_expand);

        // TextViewの設定
        text_R = (TextView)findViewById(R.id.textView_R);
        text_G = (TextView)findViewById(R.id.textView_G);
        text_B = (TextView)findViewById(R.id.textView_B);
        text_alpha = (TextView)findViewById(R.id.textView_alpha);
        text_seekBar_thick = (TextView)findViewById(R.id.text_seekBar_thick);
        text_currentLayer = (TextView)findViewById(R.id.text_currentLayer);

        et_number = (EditText)findViewById(R.id.editTextNumber);
        et_number.setText("1");

        et_index_a = (EditText)findViewById(R.id.et_index_a);
        et_index_b = (EditText)findViewById(R.id.et_index_b);

        // seekBarの設定
        seekBar_R = (SeekBar)findViewById(R.id.seekBar_R);
        seekBar_G = (SeekBar)findViewById(R.id.seekBar_G);
        seekBar_B = (SeekBar)findViewById(R.id.seekBar_B);
        seekBar_alpha = (SeekBar)findViewById(R.id.seekBar_alpha);
        seekBar_thick = (SeekBar)findViewById(R.id.seekBar_thick);
        //seekBar_expand = (SeekBar)findViewById(R.id.seekBar_expand);

        // seekBarの最大値を設定
        seekBar_R.setMax(255);
        seekBar_G.setMax(255);
        seekBar_B.setMax(255);
        seekBar_alpha.setMax(255);
        seekBar_thick.setMax(15);
        //seekBar_expand.setMax(1500);

        // seekBarの初期値を設定
        seekBar_R.setProgress(0);
        seekBar_G.setProgress(0);
        seekBar_B.setProgress(0);
        seekBar_alpha.setProgress(255);
        seekBar_thick.setProgress(5);
        //seekBar_expand.setProgress(500);

        // seekBarのリスナ登録,イベント処理
        seekBar_R.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_R = progress;                                                 // 各valueに各seekBarの値を格納
                        String str = format("R: %d", progress);                       // TextViewに表示する文字列を編集
                        text_R.setText(str);                                               // Textviewに表示
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

        // Buttonの設定
        button_close = (Button)findViewById(R.id.button_close);
        button_redo = (Button)findViewById(R.id.button_redo);
        button_undo = (Button)findViewById(R.id.button_undo);

        button_mode = (Button)findViewById(R.id.button_mode);
        button_color = (Button)findViewById(R.id.button_color);
        button_tool = (Button)findViewById(R.id.button_tool);
        button_layer = (Button)findViewById(R.id.button_layer);
        button_output = (Button)findViewById(R.id.button_output);

        button_Line = (Button)findViewById(R.id.button_Line);
        button_fillRect = (Button)findViewById(R.id.button_fillRect);
        button_Rect = (Button)findViewById(R.id.button_Rect);
        button_fillOval = (Button)findViewById(R.id.button_fillOval);
        button_Oval = (Button)findViewById(R.id.button_Oval);
        button_clip = (Button)findViewById(R.id.button_clip);
        button_clipreset = (Button)findViewById(R.id.button_clipreset);
        button_eraser = (Button)findViewById(R.id.button_eraser);

        button_red = (Button)findViewById(R.id.button_red);
        button_green = (Button)findViewById(R.id.button_green);
        button_blue = (Button)findViewById(R.id.button_blue);
        button_cyan = (Button)findViewById(R.id.button_cyan);
        button_magenta = (Button)findViewById(R.id.button_magenta);
        button_yellow = (Button)findViewById(R.id.button_yellow);
        button_black = (Button)findViewById(R.id.button_black);
        button_white = (Button)findViewById(R.id.button_white);

        button_ac = (Button)findViewById(R.id.button_ac);
        button_expand = (Button)findViewById(R.id.button_expand);

        button_expand_ok = (Button)findViewById(R.id.button_expand_ok);
        button_expand_no = (Button)findViewById(R.id.button_expand_no);

        button_add_layer = (Button)findViewById(R.id.button_add_layer);
        button_change_layer = (Button)findViewById(R.id.button_change_layer);
        button_exchange_layer = (Button)findViewById(R.id.button_exchange_layer);

        // Buttonのリスナ登録, イベント処理
        button_ac.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.AllClear();  // 画面を白に塗りつぶす(MyView.javaのメソッド)
                viewmode = mode_draw;
                layout_tool.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_redo.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){
                    MyView.Redo();
                }
            }
        });

        button_undo.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                if(viewmode == mode_draw){
                    MyView.Undo();
                }
            }
        });

        button_Line.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Line;     // 線を引くモードにする
                viewmode = mode_draw;
                button_mode.setText("~");
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_fillRect.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_fillRect; // 塗りつぶしあり四角形を描くモードにする
                viewmode = mode_draw;
                button_mode.setText("■");
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_Rect.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Rect;     // 塗りつぶしなし四角形を描くモードにする
                viewmode = mode_draw;
                button_mode.setText("□");
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_fillOval.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_fillOval; // 塗りつぶしあり楕円を描くモードにする
                viewmode = mode_draw;
                button_mode.setText("●");
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_Oval.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Oval;     // 塗りつぶしなし楕円を描くモードにする
                viewmode = mode_draw;
                button_mode.setText("◯");
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_clip.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MyView.mode = MyView.mode_clip;
                viewmode = mode_draw;
                button_mode.setText("c");
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_clipreset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MyView.clipReset();
                viewmode = mode_draw;
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_eraser.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                color = Color.TRANSPARENT;

                viewmode = mode_draw;
                button_color.setText("era");
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_mode.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                close_button();
                if(viewmode == mode_mode){
                    viewmode = mode_draw;
                    layout_mode.setVisibility(View.INVISIBLE);
                    button_close.setVisibility(View.INVISIBLE);
                }
                else{
                    viewmode = mode_mode;
                    layout_mode.setVisibility(View.VISIBLE);
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_color.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                close_button();
                if(viewmode == mode_color){
                    viewmode = mode_draw;
                    layout_color.setVisibility(View.INVISIBLE);
                    button_close.setVisibility(View.INVISIBLE);
                }
                else{
                    viewmode = mode_color;
                    layout_color.setVisibility(View.VISIBLE);
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_tool.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                close_button();
                if(viewmode == mode_tool){
                    viewmode = mode_draw;
                    layout_tool.setVisibility(View.INVISIBLE);
                    button_close.setVisibility(View.INVISIBLE);
                }
                else{
                    viewmode = mode_tool;
                    layout_tool.setVisibility(View.VISIBLE);
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_layer.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                close_button();
                if(viewmode == mode_layer){
                    viewmode = mode_draw;
                    layout_layer.setVisibility(View.INVISIBLE);
                    button_close.setVisibility(View.INVISIBLE);
                }
                else{
                    viewmode = mode_layer;
                    layout_layer.setVisibility(View.VISIBLE);
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_output.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                close_button();
                if(viewmode == mode_output){
                    viewmode = mode_draw;
                    layout_output.setVisibility(View.INVISIBLE);
                    button_close.setVisibility(View.INVISIBLE);
                }
                else{
                    viewmode = mode_output;
                    layout_output.setVisibility(View.VISIBLE);
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_add_layer.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.layers.add(MyView.currentLayer + 1, new ArrayList<>(64));
                MyView.bitmap.add(MyView.currentLayer + 1, Bitmap.createBitmap(MyView.width, MyView.height, Bitmap.Config.ARGB_8888));
                MyView.canvas_bm.add(MyView.currentLayer + 1, new Canvas(MyView.bitmap.get(MyView.currentLayer + 1)));
                et_number.setText(String.format("%d", MyView.currentLayer + 2));
                Toast.makeText(MainActivity.this, "add new layer", Toast.LENGTH_SHORT).show();
            }
        });

        button_change_layer.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                s_number = et_number.getText().toString();
                index = Integer.valueOf(s_number) - 1;

                if(MyView.layers.size() > index){
                    MyView.currentLayer = index;
                    text_currentLayer.setText(String.format("currentLayer: %d/%d", MyView.currentLayer+1, MyView.layers.size()));

                    viewmode = mode_draw;
                    layout_layer.setVisibility(View.INVISIBLE);
                    button_close.setVisibility(View.INVISIBLE);
                }
                else{
                    et_number.setText(String.format("%d以下の数字を入力", MyView.layers.size()-1));
                }
            }
        });

        button_exchange_layer.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                s_number = et_index_a.getText().toString();
                index_a = Integer.valueOf(s_number) - 1;

                s_number = et_index_b.getText().toString();
                index_b = Integer.valueOf(s_number) - 1;

                if(MyView.layers.size() > index_a){
                    if(MyView.layers.size() > index_b){
                        MyView.exchangeLayers(index_a, index_b);
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        et_index_b.setText(String.format("%d以下の数字を入力", MyView.layers.size()-1));
                    }
                }
                else{
                    et_index_a.setText(String.format("%d以下の数字を入力", MyView.layers.size()-1));
                }
            }
        });

        button_expand.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                viewmode = mode_expand;
                ReshapeView.expand();
                layout_expand.setVisibility(View.VISIBLE);
                button_close.setVisibility(View.INVISIBLE);
                layout_tool.setVisibility(View.INVISIBLE);
            }
        });

        button_expand_ok.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.keptlist = MyView.layers.get(MyView.currentLayer);
                MyView.layers.set(MyView.currentLayer, new ArrayList<>());
                MyView.layers.get(MyView.currentLayer).add(new Structure(null, 0, MyView.mode_bitmap, 0, null, false, ReshapeView.bm_rv));
                MyView.myDraw();

                ReshapeView.end();

                viewmode = mode_draw;
                layout_expand.setVisibility(View.INVISIBLE);
            }
        });

        button_expand_no.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                viewmode = mode_draw;
                layout_expand.setVisibility(View.INVISIBLE);
                ReshapeView.end();
            }
        });

        button_close.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                close_button();
                viewmode = mode_draw;
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_red.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.RED;
                setColor(255, 0, 0);
            }
        });

        button_green.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.GREEN;
                setColor(0, 255, 0);
            }
        });

        button_blue.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.BLUE;
                setColor(0, 0, 255);
            }
        });

        button_cyan.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.CYAN;
                setColor(0, 255, 255);
            }
        });

        button_magenta.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.MAGENTA;
                setColor(255, 0, 255);
            }
        });

        button_yellow.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.YELLOW;
                setColor(255, 255, 0);
            }
        });

        button_black.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.BLACK;
                setColor(0, 0, 0);
            }
        });

        button_white.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                color = Color.WHITE;
                setColor(255, 255, 255);
            }
        });
    }

    void setColor(int r, int g, int b){
        seekBar_R.setProgress(r);
        seekBar_G.setProgress(g);
        seekBar_B.setProgress(b);
        String str_R = format("R: %d", r);
        String str_G = format("R: %d", g);
        String str_B = format("R: %d", b);
        text_R.setText(str_R);
        text_G.setText(str_G);
        text_B.setText(str_B);
    }

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
            case mode_tool:
                layout_tool.setVisibility(View.INVISIBLE);
                break;
            case mode_layer:
                layout_layer.setVisibility(View.INVISIBLE);
                break;
            case mode_output:
                layout_output.setVisibility(View.INVISIBLE);
                break;
        }
    }
}