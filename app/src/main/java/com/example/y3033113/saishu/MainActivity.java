package com.example.y3033113.saishu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

import static com.example.y3033113.saishu.R.id.textView;


// レイアウトなどを記述したメインクラス
public class MainActivity extends AppCompatActivity {

    Button button_ac;           // allclear機能を持たせるボタン
    Button button_Line;         // 線を引くモードに変更させるボタン
    Button button_fillRect;     // 塗りつぶしあり四角形を描くモードに変更させるボタン
    Button button_Rect;         // 塗りつぶしなし四角形を描くモードに変更させるボタン
    Button button_fillOval;     // 塗りつぶしあり楕円を描くモードに変更させるボタン
    Button button_Oval;         // 塗りつぶしなし楕円を描くモードに変更させるボタン
    Button button_redo;
    Button button_undo;

    TextView text_R;            // RGBのうちRの値を表示するTextView
    TextView text_G;            // RGBのうちGの値を表示するTextView
    TextView text_B;            // RGBのうちBの値を表示するTextView
    TextView text_alpha;        // 不透明度を表示するTextView

    static SeekBar seekBar_R;       // RGBのうちRの値を入力するSeekBar
    static SeekBar seekBar_G;       // RGBのうちGの値を入力するSeekBar
    static SeekBar seekBar_B;       // RGBのうちBの値を入力するSeekBar
    static SeekBar seekBar_alpha;   // 不透明度の値を入力するSeekBar

    static int value_R = 0;         // RGBのうちRの値を格納する変数
    static int value_G = 0;         // RGBのうちGの値を格納する変数
    static int value_B = 0;         // RGBのうちBの値を格納する変数
    static int value_alpha = 255;   // 不透明度の値を格納する変数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                 // activity_main.xmlを表示

        // TextViewの設定
        text_R = (TextView)findViewById(R.id.textView);
        text_G = (TextView)findViewById(R.id.textView2);
        text_B = (TextView)findViewById(R.id.textView3);
        text_alpha = (TextView)findViewById(R.id.textView4);

        // seekBarの設定
        seekBar_R = (SeekBar)findViewById(R.id.seekBar);
        seekBar_G = (SeekBar)findViewById(R.id.seekBar2);
        seekBar_B = (SeekBar)findViewById(R.id.seekBar3);
        seekBar_alpha = (SeekBar)findViewById(R.id.seekBar4);

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
                        String str = String.format(Locale.US, "R:%d", progress);    // TextViewに表示する文字列を編集
                        text_R.setText(str);                                               // Textviewに表示
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

        seekBar_G.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_G = progress;
                        String str = String.format(Locale.US, "G:%d", progress);
                        text_G.setText(str);
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

        seekBar_B.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_B = progress;
                        String str = String.format(Locale.US, "B:%d", progress);
                        text_B.setText(str);
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

        seekBar_alpha.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener(){

                    // ツマミがドラッグされたとき
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        value_alpha = progress;
                        String str = String.format(Locale.US, "alpha:%d", progress);
                        text_alpha.setText(str);
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
        button_ac = (Button)findViewById(R.id.button);
        button_Line = (Button)findViewById(R.id.button2);
        button_fillRect = (Button)findViewById(R.id.button3);
        button_Rect = (Button)findViewById(R.id.button4);
        button_fillOval = (Button)findViewById(R.id.button5);
        button_Oval = (Button)findViewById(R.id.button6);
        button_redo = (Button)findViewById(R.id.button7);
        button_undo = (Button)findViewById(R.id.button8);

        // Buttonのリスナ登録, イベント処理
        button_ac.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.AllClear();  // 画面を白に塗りつぶす(MyView.javaのメソッド)
            }
        });

        button_Line.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Line;     // 線を引くモードにする
            }
        });

        button_fillRect.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_fillRect; // 塗りつぶしあり四角形を描くモードにする
            }
        });

        button_Rect.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Rect;     // 塗りつぶしなし四角形を描くモードにする
            }
        });

        button_fillOval.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_fillOval; // 塗りつぶしあり楕円を描くモードにする
            }
        });

        button_Oval.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.mode = MyView.mode_Oval;     // 塗りつぶしなし楕円を描くモードにする
            }
        });

        button_redo.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.Redo();
            }
        });

        button_undo.setOnClickListener(new View.OnClickListener(){
            // 押されたとき
            @Override
            public void onClick(View v) {
                MyView.Undo();
            }
        });
    }
}