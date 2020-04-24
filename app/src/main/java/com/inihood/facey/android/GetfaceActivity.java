package com.inihood.facey.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;


public class GetfaceActivity extends AppCompatActivity {


    private Bitmap myBitmap;
    private FaceDetector.Face[] detectedFaces;
    int value = 50;
    int valueAlpha = 255;
    Global mGlobal;

    int val = 0;
    SeekBar seek;
    SeekBar seekalpha;
    ImageView image;


    Bitmap save = null;
    CheckBox cb;

    Boolean isRev = false;
    ScrollView scrollview;

    Integer[] imgid = {
            R.drawable.a0,
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7

    };
//    private InterstitialAd interstitial;
//    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_getface);
        getSupportActionBar().hide();
        mGlobal = ((Global) getApplication());
        //Locate the Banner Ad in activity_main.xml
//        adView = (AdView) this.findViewById(R.id.ads);
//        interstitial = new InterstitialAd(GetfaceActivity.this);
//        interstitial.setAdUnitId(getResources().getString(R.string.ads_intersitials));
//        // Request for Ads
//        AdRequest adRequest = new AdRequest.Builder()
//
//                .build();
//
//        // Load ads into Banner Ads
//        interstitial.loadAd(adRequest);
//        interstitial.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                // Call displayInterstitial() function
//               interstitial.show();
//            }
//        });
//        try {
//            adView.loadAd(adRequest);
//        } catch (Exception e) {
//        }
        scrollview = (ScrollView) findViewById(R.id.scrollview);

        myBitmap = mGlobal.getImage();
        seek = (SeekBar) findViewById(R.id.seek);
        seekalpha = (SeekBar) findViewById(R.id.seekalpha);
        cb = (CheckBox) findViewById(R.id.cb);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRev = b;
                ProcessingBitmap();
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                value = i;


                ProcessingBitmap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekalpha.setProgress(valueAlpha - 100);

        seekalpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                valueAlpha = 100 + i;

                ProcessingBitmap();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Intent i = getIntent();
        image = (ImageView) findViewById(R.id.image);
        image.setImageBitmap(myBitmap);
        image.setBackgroundDrawable(new BitmapDrawable(getResources(), myBitmap));
        ProcessingBitmap();
    }


    public void getimg(View v) {

        ImageView i = (ImageView) v;
        val = Integer.parseInt(v.getTag().toString()) - 1;

        ProcessingBitmap();

    }

    public void changeanimal(View v) {


        scrollview.setVisibility(scrollview.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

    }


    private Bitmap ProcessingBitmap() {
        Bitmap bm1 = null;
        Bitmap bm2 = null;
        Bitmap newBitmap = null;
        try {
            bm1 = myBitmap;
            bm2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), imgid[val]), bm1.getWidth(), bm1.getHeight(), true);
            int w;
            if (bm1.getWidth() >= bm2.getWidth()) {
                w = bm1.getWidth();
            } else {
                w = bm2.getWidth();
            }
            int h;
            if (bm1.getHeight() >= bm2.getHeight()) {
                h = bm1.getHeight();
            } else {
                h = bm2.getHeight();
            }
            Bitmap.Config config = bm1.getConfig();
            if (config == null) {
                config = Bitmap.Config.ARGB_8888;
            }
            newBitmap = Bitmap.createBitmap(w, h, config);
            Canvas newCanvas = new Canvas(newBitmap);


            if (isRev) {
                Rect rect1Half = new Rect(0, 0, ((bm2.getWidth() * 100) / 100),
                        bm2.getHeight());
                Rect rect2Half = new Rect(((bm1.getWidth() * value) / 100) + 1,
                        0, bm2.getWidth(), bm1.getHeight());

                Paint paint = new Paint();

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                paint.setAlpha(valueAlpha);
                paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));

                newCanvas.drawBitmap(bm2, rect1Half, rect1Half, null);
                newCanvas.drawBitmap(bm1, rect2Half, rect2Half, paint);

            } else {
                //define half/half area
                Rect rect1Half = new Rect(0, 0, ((bm1.getWidth() * 100) / 100),
                        bm1.getHeight());
                Rect rect2Half = new Rect(((bm2.getWidth() * value) / 100) + 1,
                        0, bm2.getWidth(), bm2.getHeight());

                Paint paint = new Paint();

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                paint.setAlpha(valueAlpha);
                paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));

                newCanvas.drawBitmap(bm1, rect1Half, rect1Half, null);
                newCanvas.drawBitmap(bm2, rect2Half, rect2Half, paint);

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        image.setImageBitmap(newBitmap);
        save = newBitmap;
        return newBitmap;
    }


    File file;

    public void captureImage(View v) {
        // TODO Auto-generated method stub
        OutputStream output;

        Calendar cal = Calendar.getInstance();

        // Find the SD Card path
        File filepath = Environment.getExternalStorageDirectory();

        // Create a new folder in SD Card
        File dir = new File(filepath.getAbsolutePath() + "/aniface/");
        dir.mkdirs();

        String mImagename = "imagetemp" + cal.getTimeInMillis() + ".png";
        save = overlay(myBitmap, save);
        // Create a name for the saved image
        file = new File(dir, mImagename);
        MediaScannerConnection.scanFile(this, new String[]{file.getPath()},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // now visible in gallery
                    }
                });

        // Show a toast message on successful save
        Toast.makeText(GetfaceActivity.this, "Image Saved to SD Card",
                Toast.LENGTH_SHORT).show();

        try {

            output = new FileOutputStream(file);
            // Compress into png format image from 0% - 100%
            save.compress(Bitmap.CompressFormat.PNG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ShareDaialog();
        //    return file.getPath();

    }

    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }


    public void ShareDaialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        // TODO Auto-generated method stub
                        Intent intent = null;
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        Uri bmpUri = Uri.parse(file.getPath());
                        intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        intent.putExtra(
                                Intent.EXTRA_TEXT,
                                "Animal Face Morphing : ");


                        startActivity(Intent.createChooser(intent, "compatible apps:"));


                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(GetfaceActivity.this);
        builder.setMessage("Did you want to share this image?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

}