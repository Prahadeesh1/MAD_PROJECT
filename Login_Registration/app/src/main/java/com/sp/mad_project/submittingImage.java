package com.sp.mad_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class submittingImage extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1000;
    Button btnUpload;
    ImageView btnCamera;
    Button btnSubmit;
    ImageView imgGallery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitting_image);

        imgGallery = findViewById(R.id.imgGallery);
        btnUpload = findViewById(R.id.btnUpload);
        btnCamera = findViewById(R.id.btnCamera);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCamera.setOnClickListener(onCamera);
        btnSubmit.setOnClickListener(onSubmit);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_OK){

            if(requestCode==GALLERY_REQ_CODE){
                //for gallery

                imgGallery.setImageURI(data.getData());

            }
        }
    }

    private View.OnClickListener onCamera = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(submittingImage.this, Camera.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener onSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(submittingImage.this, questionaire.class);
            startActivity(intent);
        }
    };

}