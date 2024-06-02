package com.example.teacherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teacherapp.databinding.ActivityProfileBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends AppCompatActivity {

    ImageView img_back_btn;

    ActivityProfileBinding binding;

    TextView txt_teacher_name;


    TextInputLayout txt_name,txt_mob,txt_email;
    TextInputEditText editname,editroll,editmob,editemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_profile);
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        img_back_btn=findViewById(R.id.img_back);
        txt_name=findViewById(R.id.fullnamecontainer);
        editname=findViewById(R.id.editfullname);
        txt_teacher_name=findViewById(R.id.txt_teacher_name);
        txt_mob=findViewById(R.id.mobilecontainer);
        editmob=findViewById(R.id.editmob);
        txt_email=findViewById(R.id.emailcontainer);
        editemail=findViewById(R.id.editemail);

        img_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.super.onBackPressed();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        //displaying user data from firebase
        showAllUserData();



    }
    private void showAllUserData() {


        TeacherInfo teacher_obj = LoginActivity.teacher_obj;

        editname.setText(teacher_obj.getTeacher_name());
        txt_teacher_name.setText("Name: "+teacher_obj.getTeacher_name());
        editmob.setText(teacher_obj.getTeacher_mob());
        editemail.setText(teacher_obj.getTeacher_email());



    }
}

