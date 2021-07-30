package com.example.y3033113.saishu;
// 描画するものの情報を格納する構造体のようなクラス
import android.graphics.Bitmap;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    List<Float> points = new ArrayList<>(10);   // 座標
    int color = 0;                                          // 色
    int mode = 0;                                           // 描画するもの
    float thick = 0;                                        // 太さ
    Path path = null;                                       // 領域を指定するためのPath
    boolean cliping = false;                                // 領域を指定するかどうか
    Bitmap bitmap = null;                                   // bitmapを描画するときのbitmap

    Structure(List<Float> points, int color, int mode, float thick, Path path, boolean cliping, Bitmap bitmap) {
        this.points = points;
        this.color = color;
        this.mode = mode;
        this.thick = thick;
        this.path = path;
        this.cliping = cliping;
        this.bitmap = bitmap;
    }
}
