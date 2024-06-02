package com.example.teacherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    ImageView img_back_btn;
    RecyclerView recyclerView;
    List<StudentInfo> studentInfoList;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    String selectedDate;

    AttendanceHistoryAdapter adapter;

    ListView lst_view;

    ImageView img_date;
    TextView txt_date;
    DatePickerDialog datePickerDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        getSupportActionBar().hide();
        img_back_btn=findViewById(R.id.img_back);
//        recyclerView=findViewById(R.id.recycleview);
//        GridLayoutManager gridLayoutManager=new GridLayoutManager(AttendanceActivity.this,1);
//        recyclerView.setLayoutManager(gridLayoutManager);
        lst_view= findViewById(R.id.lst_view);
        img_date=findViewById(R.id.img_date);
        txt_date=findViewById(R.id.txt_date);
        adapter=new AttendanceHistoryAdapter(getApplicationContext());

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String monthstr=""+(month + 1);
        if(month + 1<10){
            monthstr="0"+(month+1);
        }
        selectedDate = day + "-" + monthstr + "-" + year;
        txt_date.setText(selectedDate);

        fetchFromDB();






//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("StudentInfo");
//        Query checkUser=reference.orderByChild("stud_prn_no");
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//
//                    Toast.makeText(AttendanceActivity.this, "Data: "+snapshot, Toast.LENGTH_SHORT).show();
//
//                }
//                else{
//
//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        StudentAdapter adapter=new StudentAdapter(AttendanceActivity.this,StudentInfo.collection);
//        recyclerView.setAdapter(adapter);



        img_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttendanceActivity.super.onBackPressed();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // Create a new instance of DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String month=""+(monthOfYear + 1);
                                if(monthOfYear + 1<10){
                                    month="0"+(monthOfYear+1);
                                }
                                selectedDate = dayOfMonth + "-" + month + "-" + year;
                                txt_date.setText(selectedDate);
                                fetchFromDB();
                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });
    }

    private void fetchFromDB() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("AttendanceRecord");
        Query checkUser=reference.orderByChild("stud_attendance_date").equalTo(selectedDate);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    AttendanceRecord.collection.clear();

                    for(DataSnapshot items:snapshot.getChildren()){
                        AttendanceRecord attendanceRecord=items.getValue(AttendanceRecord.class);
                        AttendanceRecord.collection.add(attendanceRecord);


                    }
                    adapter.notifyDataSetChanged();
                    lst_view.setAdapter(adapter);
//                    Toast.makeText(AttendanceActivity.this, "val="+snapshot, Toast.LENGTH_SHORT).show();

                }
                else{
                    AttendanceRecord.collection.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(AttendanceActivity.this, "No data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}