package com.example.y3033113.saishu;


import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

public class Structure {
    List<Float> points = new ArrayList<>(10);
    int color = 0;
    int mode = 0;
    float thick = 0;
    Path path = null;
    boolean cliping = false;

    Structure(List<Float> points, int color, int mode, float thick, Path path, boolean cliping) {
        this.points = points;
        this.color = color;
        this.mode = mode;
        this.thick = thick;
        this.path = path;
        this.cliping = cliping;
    }
}
