package com.example.y3033113.saishu;
// タイトル画面・ギャラリーを担当するアクティビティクラス
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    final Byte mode_title = 0;      // タイトル画面
    final Byte mode_gallery = 1;    // ギャラリー画面
    final Byte mode_check = 2;      // 削除を確認する画面

    Byte mode = mode_title;         // 画面のモードを格納する変数

    LinearLayout layout_gallery;
    LinearLayout layout_work1;
    LinearLayout layout_work2;
    LinearLayout layout_work3;
    LinearLayout layout_work4;
    LinearLayout layout_work5;
    LinearLayout layout_work6;
    LinearLayout layout_selected;   // 作品選択時に表示するレイアウト
    LinearLayout layout_check;      // 作品を本当に削除するかどうか確認するときのレイアウト

    LinearLayout[] layout_works = new LinearLayout[6];

    TextView text_title;
    TextView text_work1;
    TextView text_work2;
    TextView text_work3;
    TextView text_work4;
    TextView text_work5;
    TextView text_work6;

    TextView[] text_works = new TextView[6];

    ImageView image_title;
    ImageView image_work1;
    ImageView image_work2;
    ImageView image_work3;
    ImageView image_work4;
    ImageView image_work5;
    ImageView image_work6;

    ImageView[] image_works = new ImageView[6];

    // 表示する作品を変更するボタン
    Button button_gallery_up;
    Button button_gallery_down;

    Button button_back;         // タイトルに戻るボタン
    Button button_decide;       // 作品の編集を再開するボタン
    Button button_delete_work;  // 作品を削除する選択ボタン
    Button button_add_work;     // 新規作成ボタン

    Button button_check_delete; // 作品の削除ボタン
    Button button_check_cancel; // 削除確認から離脱するボタン

    List<Bitmap> bitmap_works = new ArrayList<>();
    int showingWork = 0;        // 表示している作品の番号

    final int none = -1;        // 何も作品を選択していない
    int selected = none;        // 選択している作品の番号

    int gallerySize = 0;        // 作品の数
    int newWork = 0;            // 使用する作品の番号
    static int workNum = 0;     // 選択している作品の番号
    static String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // レイアウトの設定
        layout_gallery = (LinearLayout)findViewById(R.id.layout_gallery);
        layout_work1 = (LinearLayout)findViewById(R.id.layout_work1);
        layout_work2 = (LinearLayout)findViewById(R.id.layout_work2);
        layout_work3 = (LinearLayout)findViewById(R.id.layout_work3);
        layout_work4 = (LinearLayout)findViewById(R.id.layout_work4);
        layout_work5 = (LinearLayout)findViewById(R.id.layout_work5);
        layout_work6 = (LinearLayout)findViewById(R.id.layout_work6);
        layout_selected = (LinearLayout)findViewById(R.id.layout_selected);
        layout_check = (LinearLayout)findViewById(R.id.layout_check);

        layout_works[0] = layout_work1;
        layout_works[1] = layout_work2;
        layout_works[2] = layout_work3;
        layout_works[3] = layout_work4;
        layout_works[4] = layout_work5;
        layout_works[5] = layout_work6;

        // textViewの設定
        text_title = (TextView)findViewById(R.id.text_title);
        text_work1 = (TextView)findViewById(R.id.text_work1);
        text_work2 = (TextView)findViewById(R.id.text_work2);
        text_work3 = (TextView)findViewById(R.id.text_work3);
        text_work4 = (TextView)findViewById(R.id.text_work4);
        text_work5 = (TextView)findViewById(R.id.text_work5);
        text_work6 = (TextView)findViewById(R.id.text_work6);

        text_works[0] = text_work1;
        text_works[1] = text_work2;
        text_works[2] = text_work3;
        text_works[3] = text_work4;
        text_works[4] = text_work5;
        text_works[5] = text_work6;

        // imageViewの設定
        image_title = (ImageView)findViewById(R.id.image_title);
        image_work1 = (ImageView)findViewById(R.id.image_work1);
        image_work2 = (ImageView)findViewById(R.id.image_work2);
        image_work3 = (ImageView)findViewById(R.id.image_work3);
        image_work4 = (ImageView)findViewById(R.id.image_work4);
        image_work5 = (ImageView)findViewById(R.id.image_work5);
        image_work6 = (ImageView)findViewById(R.id.image_work6);

        image_works[0] = image_work1;
        image_works[1] = image_work2;
        image_works[2] = image_work3;
        image_works[3] = image_work4;
        image_works[4] = image_work5;
        image_works[5] = image_work6;

        // リスナ登録
        image_work1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                               // ギャラリー画面ならば
                    if(selected == showingWork+1){                      // すでに押されているならば
                        layout_selected.setVisibility(View.INVISIBLE);  // 選択時レイアウトを閉じる
                        selected = none;                                // 何も選択していないことにする
                        button_add_work.setBackgroundColor(Color.rgb(100, 0, 255));
                    }
                    else{                                               // まだ押していないならば
                        layout_selected.setVisibility(View.VISIBLE);    // 選択時レイアウトを表示
                        selected = showingWork+1;                       // 押した作品を選択
                        button_add_work.setBackgroundColor(Color.rgb(150, 150, 150));
                    }
                }
            }
        });

        image_work2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                               // ギャラリー画面ならば
                    if(selected == showingWork+2){                      // すでに押されているならば
                        layout_selected.setVisibility(View.INVISIBLE);  // 選択時レイアウトを閉じる
                        selected = none;                                // 何も選択していないことにする
                        button_add_work.setBackgroundColor(Color.rgb(100, 0, 255));
                    }
                    else{                                               // まだ押していないならば
                        layout_selected.setVisibility(View.VISIBLE);    // 選択時レイアウトを表示
                        selected = showingWork+2;                       // 押した作品を選択
                        button_add_work.setBackgroundColor(Color.rgb(150, 150, 150));
                    }
                }
            }
        });

        image_work3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                               // ギャラリー画面ならば
                    if(selected == showingWork+3){                      // すでに押されているならば
                        layout_selected.setVisibility(View.INVISIBLE);  // 選択時レイアウトを閉じる
                        selected = none;                                // 何も選択していないことにする
                        button_add_work.setBackgroundColor(Color.rgb(100, 0, 255));
                    }
                    else{                                               // まだ押していないならば
                        layout_selected.setVisibility(View.VISIBLE);    // 選択時レイアウトを表示
                        selected = showingWork+3;                       // 押した作品を選択
                        button_add_work.setBackgroundColor(Color.rgb(150, 150, 150));
                    }
                }
            }
        });

        image_work4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                               // ギャラリー画面ならば
                    if(selected == showingWork+4){                      // すでに押されているならば
                        layout_selected.setVisibility(View.INVISIBLE);  // 選択時レイアウトを閉じる
                        selected = none;                                // 何も選択していないことにする
                        button_add_work.setBackgroundColor(Color.rgb(100, 0, 255));
                    }
                    else{                                               // まだ押していないならば
                        layout_selected.setVisibility(View.VISIBLE);    // 選択時レイアウトを表示
                        selected = showingWork+4;                       // 押した作品を選択
                        button_add_work.setBackgroundColor(Color.rgb(150, 150, 150));
                    }
                }
            }
        });

        image_work5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                               // ギャラリー画面ならば
                    if(selected == showingWork+5){                      // すでに押されているならば
                        layout_selected.setVisibility(View.INVISIBLE);  // 選択時レイアウトを閉じる
                        selected = none;                                // 何も選択していないことにする
                        button_add_work.setBackgroundColor(Color.rgb(100, 0, 255));
                    }
                    else{                                               // まだ押していないならば
                        layout_selected.setVisibility(View.VISIBLE);    // 選択時レイアウトを表示
                        selected = showingWork+5;                       // 押した作品を選択
                        button_add_work.setBackgroundColor(Color.rgb(150, 150, 150));
                    }
                }
            }
        });

        image_work6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                               // ギャラリー画面ならば
                    if(selected == showingWork+6){                      // すでに押されているならば
                        layout_selected.setVisibility(View.INVISIBLE);  // 選択時レイアウトを閉じる
                        selected = none;                                // 何も選択していないことにする
                        button_add_work.setBackgroundColor(Color.rgb(100, 0, 255));
                    }
                    else{                                               // まだ押していないならば
                        layout_selected.setVisibility(View.VISIBLE);    // 選択時レイアウトを表示
                        selected = showingWork+6;                       // 押した作品を選択
                        button_add_work.setBackgroundColor(Color.rgb(150, 150, 150));
                    }
                }
            }
        });

        // buttonの設定
        button_gallery_up = (Button)findViewById(R.id.button_gallery_up);
        button_gallery_down = (Button)findViewById(R.id.button_gallery_down);
        button_back = (Button)findViewById(R.id.button_back);
        button_decide = (Button)findViewById(R.id.button_decide);
        button_delete_work = (Button)findViewById(R.id.button_delete_work);
        button_add_work = (Button)findViewById(R.id.button_add_work);
        button_check_delete = (Button)findViewById(R.id.button_check_delete);
        button_check_cancel = (Button)findViewById(R.id.button_check_cancel);

        // リスナ登録
        button_gallery_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                       // ギャラリー画面ならば
                    if(showingWork > 0){                        // 表示される作品の番号が０より大きいならば
                        showingWork -= 1;
                        renew();                                // 表示を更新
                    }
                }
            }
        });

        button_gallery_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                       // ギャラリー画面ならば
                    if(bitmap_works.size() > showingWork + 5){  // 表示される作品番号がサイズを超えていなければ
                        showingWork += 1;
                        renew();                                // 表示を更新
                    }
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode==mode_gallery){                       // ギャラリー画面ならば
                    toTitle();                                // タイトル画面へ
                }
            }
        });

        button_decide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                       // ギャラリー画面ならば
                    if(selected != none){                       // 選択された作品があるならば
                        setupWork();                            // 作品を始める準備
                        startWork();                            // 作品を始める
                    }
                }
            }
        });

        button_delete_work.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                       // ギャラリー画面ならば
                    if(selected != none){                       // 選択された作品があるならば
                        layout_check.setVisibility(View.VISIBLE);// 削除かどうかを確認するレイアウトを表示
                        mode = mode_check;
                    }
                }
            }
        });

        button_add_work.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){                       // ギャラリー画面ならば
                    if(selected == none){                       // 何も選択されていないのならば
                        workNum = gallerySize;
                        filePath = "work" + Integer.toString(workNum);
                        MyView.layers = new ArrayList<>(10);
                        MyView.layers.add(new ArrayList<Structure>(64));
                        startWork();                            // 作品の開始
                    }
                }
            }
        });

        button_check_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteWork();                                   // 作品を削除
                // ギャラリー画面に戻る
                mode = mode_gallery;
                selected = none;
                layout_check.setVisibility(View.INVISIBLE);
                layout_selected.setVisibility(View.INVISIBLE);
                button_add_work.setVisibility(View.VISIBLE);
                renew();                                        // 表示の更新
            }
        });

        button_check_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // ギャラリー画面に戻る
                mode = mode_gallery;
                layout_check.setVisibility(View.INVISIBLE);
            }
        });
    }

    // タッチイベント
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:   // 触ったとき
                break;
            case MotionEvent.ACTION_UP:     // 離したとき
                if(mode == mode_title){     // タイトル画面で
                    toGallery();            // ギャラリーへ
                }
                break;
            case MotionEvent.ACTION_MOVE:   // スライドしたとき
                break;
        }
        return mode==mode_title;
    }

    // ギャラリー画面へ遷移するメソッド
    void toGallery(){
        mode = mode_gallery;
        text_title.setVisibility(View.INVISIBLE);
        image_title.setVisibility(View.INVISIBLE);
        button_add_work.setVisibility(View.VISIBLE);
        layout_gallery.setVisibility(View.VISIBLE);
        button_add_work.setBackgroundColor(Color.rgb(100, 0, 255));

        load();         // 作品の読み込み
        renew();        // 表示の更新
    }

    // タイトル画面へ遷移するメソッド
    void toTitle(){
        mode = mode_title;
        text_title.setVisibility(View.VISIBLE);
        image_title.setVisibility(View.VISIBLE);
        button_add_work.setVisibility(View.INVISIBLE);
        layout_gallery.setVisibility(View.INVISIBLE);

        bitmap_works = new ArrayList<>(10);
        selected = none;
        layout_selected.setVisibility(View.INVISIBLE);
    }

    // 作品を読み込むメソッド
    void load(){
        int i = 1;  // 作品番号
        try{        // 作品の情報を載せたテキストファイルを読み込み(作品数を数える目的)
            while(true){
                String name = "work" + Integer.toString(i);
                File workFile = new File(getApplicationContext().getFilesDir(), name + ".txt");
                if(!workFile.exists()){
                    break;
                }
                i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        gallerySize = i;
        newWork = i;
        String[] widthArray = new String[gallerySize-1];
        String[] heightArray = new String[gallerySize-1];
        try{                                                // 改めてテキストファイルを読み込み、情報を得る
            for(int j=0; j<gallerySize-1; j++){
                String name = "work" + Integer.toString(j+1);
                File file = new File(getApplicationContext().getFilesDir(), name + ".txt");
                FileInputStream input = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
                widthArray[j] = reader.readLine();
                heightArray[j] = reader.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{                                                // 作品のイメージを得る
            for(int j=0; j<gallerySize-1; j++){
                String name = "work" + Integer.toString(j+1);
                File file = new File(getApplicationContext().getFilesDir(), name + ".png");
                FileInputStream input = new FileInputStream(file);
                bitmap_works.add(bitmap_works.size(), BitmapFactory.decodeStream(input));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ギャラリーの表示を更新するメソッド
    void renew(){
        for(int i=0; i<layout_works.length; i++){
            if(bitmap_works.size() <= i){                       // 作品数が表示数より小さいならば
                layout_works[i].setVisibility(View.INVISIBLE);  // 作品の表示をやめる
            }
            else{                                               // 作品数が表示数より大きいならば
                layout_works[i].setVisibility(View.VISIBLE);    // 作品を表示
                image_works[i].setImageBitmap((bitmap_works.get(showingWork+i)));
                text_works[i].setText(Integer.toString(showingWork+i+1));
            }
        }

        if(showingWork <= 0){   // 表示している作品番号が０以上
            button_gallery_up.setBackgroundColor(Color.rgb(150, 150, 150));
        }
        else{                   // ０未満
            button_gallery_up.setBackgroundColor(Color.rgb(100, 0, 255));
        }
        if(bitmap_works.size() <= showingWork + 5){ // 表示している作品番号が作品数より小さい
            button_gallery_down.setBackgroundColor(Color.rgb(150, 150, 150));
        }
        else{                                       // 作品数より大きい
            button_gallery_down.setBackgroundColor(Color.rgb(100, 0, 255));
        }
    }

    // 作品を再開する前の準備をするメソッド
    void setupWork(){
        filePath = "work" + Integer.toString(selected);
        String tmp;
        List<String> in = new ArrayList<>();

        try{            // 選択した作品の情報をテキストファイルから得る
            File file = new File(getApplicationContext().getFilesDir(), filePath + ".txt");
            FileInputStream input = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            while( (tmp = reader.readLine()) != null ){
                in.add(in.size(), tmp);
            }
            reader.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        int width = Integer.valueOf(in.get(0));
        int height = Integer.valueOf(in.get(1));
        int layerNum = Integer.valueOf(in.get(2));
        int invisibleNum = Integer.valueOf(in.get(3));
        for(int i=0; i<invisibleNum; i++){
            MyView.invisible.add(in.get(3+1+i));
        }

        Bitmap[] images = new Bitmap[layerNum];

        try{            // 作品のレイヤーイメージを得る
            for(int i=0; i<layerNum; i++){
                File file = new File(getApplicationContext().getFilesDir(), filePath + "_" + Integer.toString(i+1) + ".png");
                FileInputStream input = new FileInputStream(file);
                images[i] = BitmapFactory.decodeStream(input);
                input.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        MyView.layers = new ArrayList<>(10);
        for(int i=0; i<layerNum; i++){
            MyView.layers.add(MyView.layers.size(), new ArrayList<Structure>(64));
            MyView.layers.get(i).add(new Structure(null, 0, MyView.mode_bitmap, 0, null, false, images[i]));
        }
    }

    // 選択した作品を削除するメソッド
    void deleteWork(){
        filePath = "work" + Integer.toString(selected);
        try{        // 作品のテキストファイル・画像を削除
            File file = new File(getApplicationContext().getFilesDir(), filePath + ".txt");
            file.delete();

            file = new File(getApplicationContext().getFilesDir(), filePath + ".png");
            file.delete();

            int j = 1;
            while(true){
                file = new File(getApplicationContext().getFilesDir(), filePath + "_" + Integer.toString(j) + ".png");
                if(!file.exists()){
                    break;
                }
                file.delete();
                j++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        bitmap_works.remove(selected-1);
    }

    // 作品を開始
    void startWork(){
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
        finish();
    }
}