package com.example.y3033113.saishu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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

    byte viewmode = mode_draw;

    LinearLayout layout_mode;
    LinearLayout layout_color;
    LinearLayout layout_tool;
    LinearLayout layout_layer;
    LinearLayout layout_output;

    Button button_ac;           // allclear機能を持たせるボタン
    Button button_Line;         // 線を引くモードに変更させるボタン
    Button button_fillRect;     // 塗りつぶしあり四角形を描くモードに変更させるボタン
    Button button_Rect;         // 塗りつぶしなし四角形を描くモードに変更させるボタン
    Button button_fillOval;     // 塗りつぶしあり楕円を描くモードに変更させるボタン
    Button button_Oval;         // 塗りつぶしなし楕円を描くモードに変更させるボタン
    Button button_redo;
    Button button_undo;
    Button button_clip;
    Button button_clipreset;
    Button button_mode;
    Button button_color;
    Button button_tool;
    Button button_layer;
    Button button_add_layer;
    Button button_change_layer;
    Button button_close;
    Button button_output;
    Button button_red;
    Button button_green;
    Button button_blue;
    Button button_cyan;
    Button button_magenta;
    Button button_yellow;
    Button button_black;
    Button button_white;

    TextView text_R;            // RGBのうちRの値を表示するTextView
    TextView text_G;            // RGBのうちGの値を表示するTextView
    TextView text_B;            // RGBのうちBの値を表示するTextView
    TextView text_alpha;        // 不透明度を表示するTextView
    EditText et_number;

    static SeekBar seekBar_R;       // RGBのうちRの値を入力するSeekBar
    static SeekBar seekBar_G;       // RGBのうちGの値を入力するSeekBar
    static SeekBar seekBar_B;       // RGBのうちBの値を入力するSeekBar
    static SeekBar seekBar_alpha;   // 不透明度の値を入力するSeekBar

    static int value_R = 0;         // RGBのうちRの値を格納する変数
    static int value_G = 0;         // RGBのうちGの値を格納する変数
    static int value_B = 0;         // RGBのうちBの値を格納する変数
    static int value_alpha = 255;   // 不透明度の値を格納する変数

    String s_number;
    int indent;


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

        // TextViewの設定
        text_R = (TextView)findViewById(R.id.textView_R);
        text_G = (TextView)findViewById(R.id.textView_G);
        text_B = (TextView)findViewById(R.id.textView_B);
        text_alpha = (TextView)findViewById(R.id.textView_alpha);

        et_number = (EditText)findViewById(R.id.editTextNumber);
        et_number.setText("1");

        // seekBarの設定
        seekBar_R = (SeekBar)findViewById(R.id.seekBar_R);
        seekBar_G = (SeekBar)findViewById(R.id.seekBar_G);
        seekBar_B = (SeekBar)findViewById(R.id.seekBar_B);
        seekBar_alpha = (SeekBar)findViewById(R.id.seekBar_alpha);

        // seekBarの最大値(ARGBだから0~255)を設定
        seekBar_R.setMax(255);
        seekBar_G.setMax(255);
        seekBar_B.setMax(255);
        seekBar_alpha.setMax(255);

        // seekBarの初期値を設定
        seekBar_R.setProgress(0);
        seekBar_G.setProgress(0);
        seekBar_B.setProgress(0);
        seekBar_alpha.setProgress(255);

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
                        MyView.color = Color.argb(value_alpha, value_R, value_G, value_B);
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
                        MyView.color = Color.argb(value_alpha, value_R, value_G, value_B);
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
                        MyView.color = Color.argb(value_alpha, value_R, value_G, value_B);
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
                        MyView.color = Color.argb(value_alpha, value_R, value_G, value_B);
                    }
                });

        // Buttonの設定
        button_ac = (Button)findViewById(R.id.button_ac);
        button_Line = (Button)findViewById(R.id.button_Line);
        button_fillRect = (Button)findViewById(R.id.button_fillRect);
        button_Rect = (Button)findViewById(R.id.button_Rect);
        button_fillOval = (Button)findViewById(R.id.button_fillOval);
        button_Oval = (Button)findViewById(R.id.button_Oval);
        button_redo = (Button)findViewById(R.id.button_redo);
        button_undo = (Button)findViewById(R.id.button_undo);
        button_clip = (Button)findViewById(R.id.button_clip);
        button_clipreset = (Button)findViewById(R.id.button_clipreset);
        button_mode = (Button)findViewById(R.id.button_mode);
        button_color = (Button)findViewById(R.id.button_color);
        button_tool = (Button)findViewById(R.id.button_tool);
        button_layer = (Button)findViewById(R.id.button_layer);
        button_add_layer = (Button)findViewById(R.id.button_add_layer);
        button_change_layer = (Button)findViewById(R.id.button_change_layer);
        button_output = (Button)findViewById(R.id.button_output);
        button_close = (Button)findViewById(R.id.button_close);
        button_red = (Button)findViewById(R.id.button_red);
        button_green = (Button)findViewById(R.id.button_green);
        button_blue = (Button)findViewById(R.id.button_blue);
        button_cyan = (Button)findViewById(R.id.button_cyan);
        button_magenta = (Button)findViewById(R.id.button_magenta);
        button_yellow = (Button)findViewById(R.id.button_yellow);
        button_black = (Button)findViewById(R.id.button_black);
        button_white = (Button)findViewById(R.id.button_white);

        // Buttonのリスナ登録, イベント処理
        button_ac.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.AllClear();  // 画面を白に塗りつぶす(MyView.javaのメソッド)
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
                layout_mode.setVisibility(View.INVISIBLE);
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_clip.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                MyView.mode = MyView.mode_clip;
                viewmode = mode_draw;
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

        button_mode.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                if(viewmode == 0){
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
                if(viewmode == 0){
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
                if(viewmode == 0){
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
                if(viewmode == 0){
                    viewmode = mode_layer;
                    layout_layer.setVisibility(View.VISIBLE);
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_add_layer.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.layers.add(new ArrayList<>(10));
                Toast.makeText(MainActivity.this, "add new layer", Toast.LENGTH_SHORT).show();
            }
        });

        button_change_layer.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                s_number = et_number.getText().toString();
                indent = Integer.valueOf(s_number) - 1;

                if(MyView.layers.size() > indent){
                    MyView.currentLayer = indent;

                    viewmode = mode_draw;
                    layout_layer.setVisibility(View.INVISIBLE);
                    button_close.setVisibility(View.INVISIBLE);
                }
                else{
                    et_number.setText(String.format("%d以下の数字を入力", MyView.layers.size()-1));
                }
            }
        });

        button_output.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                if(viewmode == 0){
                    viewmode = mode_output;
                    layout_output.setVisibility(View.VISIBLE);
                    button_close.setVisibility(View.VISIBLE);
                }
            }
        });

        button_close.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                switch(viewmode){
                    case mode_mode:
                        layout_mode.setVisibility(View.INVISIBLE);
                        break;
                    case mode_color:
                        layout_color.setVisibility(View.INVISIBLE);
                        button_color.setBackgroundColor(MyView.color);
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
                viewmode = mode_draw;
                button_close.setVisibility(View.INVISIBLE);
            }
        });

        button_red.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.RED;
                setColor(255, 0, 0);
            }
        });

        button_green.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.GREEN;
                setColor(0, 255, 0);
            }
        });

        button_blue.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.BLUE;
                setColor(0, 0, 255);
            }
        });

        button_cyan.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.CYAN;
                setColor(0, 255, 255);
            }
        });

        button_magenta.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.MAGENTA;
                setColor(255, 0, 255);
            }
        });

        button_yellow.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.YELLOW;
                setColor(255, 255, 0);
            }
        });

        button_black.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.BLACK;
                setColor(0, 0, 0);
            }
        });

        button_white.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.color = Color.WHITE;
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
}