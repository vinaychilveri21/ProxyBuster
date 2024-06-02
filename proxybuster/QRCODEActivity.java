package com.example.proxybuster;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCODEActivity extends AppCompatActivity {

    ImageView img_back_btn,imgQR_code;
    TextView txt_stud_name;

    QRGEncoder qrgEncoder;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodeactivity);
        getSupportActionBar().hide();
        img_back_btn=findViewById(R.id.img_back);
        imgQR_code=findViewById(R.id.img_qrcode);
        txt_stud_name=findViewById(R.id.txt_stud_name);


        img_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCODEActivity.super.onBackPressed();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        funQRCODE();


    }

    private void funQRCODE() {
        WindowManager manager=(WindowManager)getSystemService(WINDOW_SERVICE);
        Display display=manager.getDefaultDisplay();
        Point point=new Point();
        display.getSize(point);

        int width=point.x;
        int height=point.y;

        int dimen=width < height? width : height;
        dimen=dimen*3/4;

        qrgEncoder=new QRGEncoder(LoginActivity.stud_obj.getStud_prn_no(),null, QRGContents.Type.TEXT,dimen);
        try {
            bitmap=qrgEncoder.encodeAsBitmap();
            imgQR_code.setImageBitmap(bitmap);
            txt_stud_name.setText(LoginActivity.stud_obj.getStud_name().toUpperCase());

        }catch (Exception e)
        {
            Log.e("Tag",e.toString());
        }
    }
}