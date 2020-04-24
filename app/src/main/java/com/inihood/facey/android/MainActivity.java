package com.inihood.facey.android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    protected static final int REQUEST_CAMERA = 111;
    protected static final int SELECT_FILE = 222;
    private static final int REQUEST_WRITE_STORAGE_REQUEST_CODE = 0;
    private Bitmap m_bitmap1;

    Global mGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //Locate the Banner Ad in activity_main.xml


        mGlobal = ((Global) getApplication());

        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment
                        .getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(f));
                startActivityForResult(intent, REQUEST_CAMERA);

            }
        });

        findViewById(R.id.galery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        SELECT_FILE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);

        // /==========================

        if (resultCode == RESULT_OK) {

            Intent i = new Intent(MainActivity.this, SelectcropActivity.class);


            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    btmapOptions.inSampleSize = 2;

                    m_bitmap1 = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                    mGlobal.setImage(m_bitmap1);

                    startActivity(i);

                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String tempPath = getPath(selectedImageUri,
                        MainActivity.this);

                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                Bitmap bm1 = BitmapFactory.decodeFile(tempPath, btmapOptions);

                if (bm1 != null){
                    Matrix mat = new Matrix();
                    Bitmap bMapRotate = Bitmap.createBitmap(bm1, 0, 0,
                            bm1.getWidth(), bm1.getHeight(), mat, true);
                    mGlobal.setImage(bMapRotate);
                    startActivity(i);
                }else {
                    Toast.makeText(this, "was null", Toast.LENGTH_SHORT).show();
                }



            }

        }
    }


    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestAppPermissions();
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_WRITE_STORAGE_REQUEST_CODE); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
}
