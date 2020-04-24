package com.inihood.facey.android;

import android.app.Application;
import android.graphics.Bitmap;

public class Global extends Application {

    Bitmap bitmap_forground;
    Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public Bitmap setImage(Bitmap image) {
        this.image = image;
        return  image;
    }

    public Bitmap getBitmap_forground() {
        return bitmap_forground;
    }

    public void setBitmap_forground(Bitmap bitmap_forground) {
        this.bitmap_forground = bitmap_forground;
    }


}
