package com.example.y3033113.saishu;
// スライドショーのようなものを担当するアクティビティクラス
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity3 extends AppCompatActivity {
    ImageView image_layer;
    int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // imageViewの設定
        image_layer = (ImageView)findViewById(R.id.imageView);
        image_layer.setImageBitmap(MyView.bitmap.get(index));

        // リスナ登録
        // クリックするたびに表示するレイヤーイメージを変更する
        image_layer.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                index += 1;
                if(index >= MyView.bitmap.size()){      // レイヤーが最後まで表示されたらアクティビティを終了
                    index= 0;
                    finish();
                }
                image_layer.setImageBitmap(MyView.bitmap.get(index));
            }
        });
    }
}