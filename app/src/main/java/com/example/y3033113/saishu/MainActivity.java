package com.example.y3033113.saishu;

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

    final Byte mode_title = 0;
    final Byte mode_gallery = 1;
    final Byte mode_check = 2;

    Byte mode = mode_title;

    LinearLayout layout_gallery;
    LinearLayout layout_work1;
    LinearLayout layout_work2;
    LinearLayout layout_work3;
    LinearLayout layout_work4;
    LinearLayout layout_work5;
    LinearLayout layout_work6;
    LinearLayout layout_check;

    LinearLayout[] layout_works = new LinearLayout[6];

    TextView text_title;
    TextView text_work1;
    TextView text_work2;
    TextView text_work3;
    TextView text_work4;
    TextView text_work5;
    TextView text_work6;

    TextView[] text_works = new TextView[6];

    ImageView image_work1;
    ImageView image_work2;
    ImageView image_work3;
    ImageView image_work4;
    ImageView image_work5;
    ImageView image_work6;

    ImageView[] image_works = new ImageView[6];

    Button button_gallery_up;
    Button button_gallery_down;
    Button button_back;
    Button button_decide;
    Button button_delete_work;
    Button button_add_work;

    Button button_check_delete;
    Button button_check_cancel;

    List<Bitmap> bitmap_works = new ArrayList<>();
    int showingWork = 0;

    final int none = -1;
    int selected = none;

    int gallerySize = 0;
    int newWork = 0;
    static int workNum = 0;
    static String filePath;// filePath = Integer.toString(workNum);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        layout_gallery = (LinearLayout)findViewById(R.id.layout_gallery);
        layout_work1 = (LinearLayout)findViewById(R.id.layout_work1);
        layout_work2 = (LinearLayout)findViewById(R.id.layout_work2);
        layout_work3 = (LinearLayout)findViewById(R.id.layout_work3);
        layout_work4 = (LinearLayout)findViewById(R.id.layout_work4);
        layout_work5 = (LinearLayout)findViewById(R.id.layout_work5);
        layout_work6 = (LinearLayout)findViewById(R.id.layout_work6);
        layout_check = (LinearLayout)findViewById(R.id.layout_check);

        layout_works[0] = layout_work1;
        layout_works[1] = layout_work2;
        layout_works[2] = layout_work3;
        layout_works[3] = layout_work4;
        layout_works[4] = layout_work5;
        layout_works[5] = layout_work6;

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

        image_work1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected == showingWork){
                        turnOffButtons();
                        selected = none;
                    }
                    else{
                        turnOnButtons();
                        selected = showingWork;
                    }
                }
            }
        });

        image_work2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected == showingWork+1){
                        turnOffButtons();
                        selected = none;
                    }
                    else{
                        turnOnButtons();
                        selected = showingWork+1;
                    }
                }
            }
        });

        image_work3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected == showingWork+2){
                        turnOffButtons();
                        selected = none;
                    }
                    else{
                        turnOnButtons();
                        selected = showingWork+2;
                    }
                }
            }
        });

        image_work4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected == showingWork+3){
                        turnOffButtons();
                        selected = none;
                    }
                    else{
                        turnOnButtons();
                        selected = showingWork+3;
                    }
                }
            }
        });

        image_work5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected == showingWork+4){
                        turnOffButtons();
                        selected = none;
                    }
                    else{
                        turnOnButtons();
                        selected = showingWork+4;
                    }
                }
            }
        });

        image_work6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected == showingWork+5){
                        turnOffButtons();
                        selected = none;
                    }
                    else{
                        turnOnButtons();
                        selected = showingWork+5;
                    }
                }
            }
        });

        button_gallery_up = (Button)findViewById(R.id.button_gallery_up);
        button_gallery_down = (Button)findViewById(R.id.button_gallery_down);
        button_back = (Button)findViewById(R.id.button_back);
        button_decide = (Button)findViewById(R.id.button_decide);
        button_delete_work = (Button)findViewById(R.id.button_delete_work);
        button_add_work = (Button)findViewById(R.id.button_add_work);
        button_check_delete = (Button)findViewById(R.id.button_check_delete);
        button_check_cancel = (Button)findViewById(R.id.button_check_cancel);

        button_gallery_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(showingWork > 0){
                        showingWork -= 1;
                        renew();
                    }
                }
            }
        });

        button_gallery_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(bitmap_works.size() > showingWork + 5){
                        showingWork += 1;
                        renew();
                    }
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode==mode_gallery){
                    toTitle();
                }
            }
        });

        button_decide.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected != none){
                        setupWork();
                        startWork();
                    }
                }
            }
        });

        button_delete_work.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    if(selected != none){
                        layout_check.setVisibility(View.VISIBLE);
                        mode = mode_check;
                    }
                }
            }
        });

        button_add_work.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mode == mode_gallery){
                    workNum = gallerySize;
                    filePath = "work" + Integer.toString(workNum);
                    MyView.layers = new ArrayList<>(10);
                    MyView.layers.add(new ArrayList<Structure>(64));
                    startWork();
                }
            }
        });

        button_check_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteWork();
                mode = mode_gallery;
                layout_check.setVisibility(View.INVISIBLE);
            }
        });

        button_check_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mode = mode_gallery;
                layout_check.setVisibility(View.INVISIBLE);
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if(mode == mode_title){
                    toGallery();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return mode==mode_title;
    }

    void toGallery(){
        mode = mode_gallery;
        text_title.setVisibility(View.INVISIBLE);
        //image_title.setVisibility(View.INVISIBLE);
        layout_gallery.setVisibility(View.VISIBLE);

        road();
        renew();
    }

    void toTitle(){
        mode = mode_title;
        text_title.setVisibility(View.VISIBLE);
        //image_title.setVisibility(View.VISIBLE);
        layout_gallery.setVisibility(View.INVISIBLE);

        bitmap_works = new ArrayList<>(10);
        selected = none;
        turnOffButtons();
    }

    void road(){
        int i = 1;
        try{
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
        System.out.println(i);
        String[] widthArray = new String[gallerySize-1];
        String[] heightArray = new String[gallerySize-1];
        try{
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

        try{
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

    void renew(){
        for(int i=0; i<layout_works.length; i++){
            if(bitmap_works.size() <= i){
                layout_works[i].setVisibility(View.INVISIBLE);
            }
            else{
                layout_works[i].setVisibility(View.VISIBLE);
                image_works[i].setImageBitmap((bitmap_works.get(showingWork+i)));
                text_works[i].setText(Integer.toString(showingWork+i+1));
            }
        }

        if(showingWork <= 0){
            button_gallery_up.setBackgroundColor(Color.rgb(150, 150, 150));
        }
        else{
            button_gallery_up.setBackgroundColor(Color.rgb(100, 0, 255));
        }
        if(bitmap_works.size() <= showingWork + 5){
            button_gallery_down.setBackgroundColor(Color.rgb(150, 150, 150));
        }
        else{
            button_gallery_down.setBackgroundColor(Color.rgb(100, 0, 255));
        }
    }

    void setupWork(){
        filePath = "work" + Integer.toString(selected + 1);
        String tmp;
        List<String> in = new ArrayList<>();

        try{
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

        try{
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

    void deleteWork(){
        filePath = "work" + Integer.toString(workNum);
        try{
            File file = new File(getApplicationContext().getFilesDir(), filePath + ".txt");
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void startWork(){
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
        finish();
    }

    void turnOnButtons(){
        button_decide.setBackgroundColor(Color.rgb(100, 0, 255));
        button_delete_work.setBackgroundColor(Color.rgb(255, 50, 50));
    }

    void turnOffButtons(){
        button_decide.setBackgroundColor(Color.rgb(150, 150, 150));
        button_delete_work.setBackgroundColor(Color.rgb(150, 150, 150));
    }
}