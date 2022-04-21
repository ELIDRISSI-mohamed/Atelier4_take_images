package com.tp.atelier3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.*;

public class MainActivity extends AppCompatActivity {
    //Initialisation
    private ImageView imageView;
    private Button btnTakeImg;
    private TextView textInfo;
    private final int REQUEST_IMAGE_CAPTURE = 1;

    OutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Assign
        imageView = findViewById(R.id.imageView);
        btnTakeImg = findViewById(R.id.btnTakeImg);
        //Check camera permission
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 200);
        }
        btnTakeImg.setOnClickListener(e->{
            //open camera
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 200);
        });
        //Save image in External storage
        imageView.setOnClickListener(e->{
            Bitmap bitmap = null;
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            File filepath = Environment.getExternalStorageDirectory();
            File dir = new File(filepath.getAbsolutePath()+"/pic");
            dir.mkdir();
            File file = new File(dir, System.currentTimeMillis()+".png");
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Toast.makeText(this, "Image save to external storage", Toast.LENGTH_SHORT).show();
            try {
                outputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            //get image
            Bitmap imgCapt = (Bitmap) data.getExtras().get("data");
            // set imageView
            imageView.setImageBitmap(imgCapt);
            textInfo = findViewById(R.id.textInfo);
            textInfo.setText("Tapez sur l'image pour le enregistrer !!");
        }
    }

}