package com.inihood.facey.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.myandroid.views.MultiTouchListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;


public class SelectcropActivity extends AppCompatActivity {
    protected static final int SELECT_FILE = 222;


    FrameLayout frame;
    RelativeLayout Rl;
    ImageView img;


    CropImageView mCropImageView;

    Global mGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main_crop);
        mGlobal = ((Global) getApplication());

        //Locate the Banner Ad in activity_main.xml
//        adView = (AdView) this.findViewById(R.id.ads);
//
//        // Request for Ads
//        AdRequest adRequest = new AdRequest.Builder()
//
//                .build();
//
//        // Load ads into Banner Ads
//        try {
//            adView.loadAd(adRequest);
//        } catch (Exception e) {
//        }
        img = (ImageView) findViewById(R.id.img);
        frame = (FrameLayout) findViewById(R.id.frame);
        Rl = (RelativeLayout) findViewById(R.id.sqRl);

        img.setImageBitmap(mGlobal.getImage());
        img.setOnTouchListener(new MultiTouchListener());

        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);


        findViewById(R.id.crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String filename = captureImage();

                Intent i = new Intent(SelectcropActivity.this,
                        GetfaceActivity.class);
                i.putExtra("path", "" + filename);
                startActivity(i);


            }
        });

    }

    File file;

    private String captureImage() {
        // TODO Auto-generated method stub
        OutputStream output;

        Calendar cal = Calendar.getInstance();

        Bitmap bitmap = Bitmap.createBitmap(Rl.getWidth(), Rl.getHeight(),
                Bitmap.Config.ARGB_8888);

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, Rl.getWidth(),
                Rl.getHeight());

        Canvas b = new Canvas(bitmap);
        Rl.draw(b);

        // Find the SD Card path
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/aniface/");
        dir.mkdirs();

        String mImagename = "imagetemp.png";

        // Create a name for the saved image
        file = new File(dir, mImagename);
        MediaScannerConnection.scanFile(this, new String[]{file.getPath()},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // now visible in gallery
                    }
                });

        // Show a toast message on successful save
        Toast.makeText(SelectcropActivity.this, "Image Saved to SD Card",
                Toast.LENGTH_SHORT).show();
        mGlobal.setImage(bitmap);
        try {
            output = new FileOutputStream(file);
            // Compress into png format image from 0% - 100%
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return file.getPath();

    }
}

