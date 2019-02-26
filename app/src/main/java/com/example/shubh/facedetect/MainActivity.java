package com.example.shubh.facedetect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    Button b;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
   //final Bitmap myBitmap;


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
               myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                img.setImageBitmap(myBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=findViewById(R.id.imageView);
        b=findViewById(R.id.button);

      /*  img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });*/
        final Bitmap myBitmap= BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.se);
        img.setImageBitmap(myBitmap);

      final  Paint  rec=new Paint();
        rec.setStrokeWidth(5);
        rec.setColor(Color.RED);
        rec.setStyle(Paint.Style.STROKE);



        //assert myBitmap != null;
        final Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        final Canvas tempCanvas = new Canvas(tempBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FaceDetector fd=new FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).setMode(FaceDetector.FAST_MODE).build();

                if(!fd.isOperational()){
                    new AlertDialog.Builder(v.getContext()).setMessage("Could not set up the face detector!").show();
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> faces = fd.detect(frame);

                for(int i=0; i<faces.size(); i++) {
                    Face thisFace = faces.valueAt(i);
                    float x1 = thisFace.getPosition().x;
                    float y1 = thisFace.getPosition().y;
                    float x2 = x1 + thisFace.getWidth();
                    float y2 = y1 + thisFace.getHeight();

                    RectF rectF=new RectF(x1,y1,x2,y2);


                    tempCanvas.drawRoundRect(rectF, 2, 2, rec);
                }
               img.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));

            }
        });
    }
}
