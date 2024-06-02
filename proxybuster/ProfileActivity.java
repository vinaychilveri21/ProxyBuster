package com.example.proxybuster;

import com.example.proxybuster.StudentInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proxybuster.databinding.ActivityProfileBinding;
import com.example.proxybuster.databinding.ActivityRegistrationBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends AppCompatActivity {

    String[] year={"First Year","Second Year","Third Year","Fourth Year"};
    String[] div={"A","B","C","D"};
    String[] dept={"Computer","IT","E&TC","Electrical","Mechanical","Civil","BioTech","Chemical","Production"};


    ImageView img_back_btn;

    ActivityProfileBinding binding;


    TextInputLayout txt_name,txt_roll,txt_year,txt_dept,txt_div,txt_mob,txt_email;
    TextInputEditText editname,editroll,editmob,editemail;
    AutoCompleteTextView autoDiv,autoDept,autoYr;

    TextView txt_prn;


    ArrayAdapter<String> adapterDiv,adapterDept,adapterYear;

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
        txt_prn=findViewById(R.id.txt_prn);
        txt_roll=findViewById(R.id.rollnocontainer);
        editroll=findViewById(R.id.editrollno);
        txt_year=findViewById(R.id.yearcontainer);
        autoYr=findViewById(R.id.autoyear);
        txt_dept=findViewById(R.id.deptcontainer);
        autoDept=findViewById(R.id.autodept);
        txt_div=findViewById(R.id.divcontainer);
        autoDiv=findViewById(R.id.autodiv);
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

        adapterYear=new ArrayAdapter<String>(this,R.layout.dropdown_list,year);
        autoYr.setAdapter(adapterYear);
        adapterDiv=new ArrayAdapter<String>(this,R.layout.dropdown_list,div);
        autoDiv.setAdapter(adapterDiv);
        adapterDept=new ArrayAdapter<String>(this,R.layout.dropdown_list,dept);
        autoDept.setAdapter(adapterDept);

        autoYr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String yr=adapterView.getItemAtPosition(i).toString();
            }
        });


        autoDiv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String div=adapterView.getItemAtPosition(i).toString();
            }
        });

        autoDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String dept=adapterView.getItemAtPosition(i).toString();
            }
        });

        //displaying user data from firebase
        showAllUserData();
    }

    private void showAllUserData() {


        StudentInfo stud_obj = LoginActivity.stud_obj;

        editname.setText(stud_obj.getStud_name());
        txt_prn.setText("PRN NO: "+stud_obj.getStud_prn_no());
        editroll.setText(stud_obj.getStud_roll());

        autoYr.setText(stud_obj.getStud_year(), false);
        autoDept.setText(stud_obj.getStud_dept(),false);
        autoDiv.setText(stud_obj.getStud_div(),false);

       editmob.setText(stud_obj.getStud_mob());
       editemail.setText(stud_obj.getStud_email());



    }
}