package com.example.teacherapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.teacherapp.databinding.ActivityDashboardBinding;
import com.example.teacherapp.databinding.ActivityScannerBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScannerActivity extends AppCompatActivity {

    ListView lst_view;
    AppCompatButton btn_scan,btn_submit;

    ActivityScannerBinding binding;
    StudentAttendanceAdapter adapter;

    FirebaseDatabase firebasedatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    AttendanceRecord record_obj;


    private ActivityResultLauncher<String> requestPermissionLauncher=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted-> {
                if(isGranted){
                    showCamera();
                }
                else{
                    //show why user need this permission
                }
            });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher=registerForActivityResult(new ScanContract(), result->{
        if(result.getContents()==null){
            //Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
        else{
            setResult(result.getContents());
        }
    });

    private void setResult(String contents) {

        int i=StudentInfo.collectionPRN.indexOf(contents);
        StudentInfo.collection.get(i).setAttendance("P");
       // Toast.makeText(this, "Rollno: "+StudentInfo.collection.get(i).getStud_roll(), Toast.LENGTH_SHORT).show();
        if(i>-1){
            StudentInfo.collection.get(i).setAttendance("P");
            Toast.makeText(this, "Rollno: "+StudentInfo.collection.get(i).getStud_roll(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Invalid: "+StudentInfo.collection.get(i).getStud_roll(), Toast.LENGTH_SHORT).show();
        }
        showCamera();
    }

    public void showCamera() {
        ScanOptions options=new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan a QR code");
        options.setCameraId(0);
        options.setBeepEnabled(true);
        //options.setTimeout(10000);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(true);

        qrCodeLauncher.launch(options);

    }
    String formattedDateTime,formattedDate, formattedTimeStr ;


    LocalDate currentDate;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initView();
//        setContentView(R.layout.activity_scanner);
        getSupportActionBar().hide();

        lst_view= findViewById(R.id.lst_view);
        btn_submit= findViewById(R.id.btn_submit);

        firebasedatabase = FirebaseDatabase.getInstance();
        databaseReference=firebasedatabase.getReference("AttendanceRecord");

        currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Get the current date and time
        LocalDateTime currentDateTime=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           currentDateTime = LocalDateTime.now();
        }


        // Define the desired date format
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        }
        DateTimeFormatter formatter1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        }
        DateTimeFormatter formatterTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        }


        // Format the current date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formattedDate = currentDateTime.format(formatter1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formattedTimeStr = currentDateTime.format(formatterTime);
        }

        // Format the current date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formattedDateTime = currentDateTime.format(formatter);
        }


        //record_obj=new AttendanceRecord();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDatatoFirebase(StudentInfo.collection);
                Toast.makeText(ScannerActivity.this, "data added", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ScannerActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });



         adapter=new StudentAttendanceAdapter(ScannerActivity.this);
        lst_view.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }
    }
    private void initBinding() {
        binding= ActivityScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    private void initView() {
        binding.btnScan.setOnClickListener(view -> {
            checkPermissionAndShowActivity(this);
        });
    }



    public void checkPermissionAndShowActivity(Context context) {
        if(ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
        )== PackageManager.PERMISSION_GRANTED){
            showCamera();
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(context, "camera permission required", Toast.LENGTH_SHORT).show();
        }else{
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

    }

    private void addDatatoFirebase(ArrayList<StudentInfo> collection){
        // below 4 lines of code is used to set
        // data in our object class.

        for(int i=0; i<collection.size();i++){
            record_obj=new AttendanceRecord(collection.get(i).getStud_prn_no(),formattedDate, collection.get(i).getStud_roll(),
                                            collection.get(i).getStud_name(),collection.get(i).getAttendance(),formattedTimeStr);
            databaseReference.child(collection.get(i).getStud_prn_no()+"-"+formattedDateTime).setValue(record_obj);
        }

            /*


        // we are use add value event listener method
        // which is called with database reference.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                ///databaseReference.child(mob).setValue(record_obj);

                for(int i=0;i<collection.size();i++){
                    AttendanceRecord attendanceRecord= new AttendanceRecord();
                    attendanceRecord.setStud_prn_no(collection.get(i).getStud_prn_no());
                    attendanceRecord.setStud_name(collection.get(i).getStud_name());
                    attendanceRecord.setStud_roll(collection.get(i).getStud_roll());
                    attendanceRecord.setStud_attendance_status(collection.get(i).getAttendance());
                    Toast.makeText(ScannerActivity.this, "Attendance: "+collection.get(i).getAttendance(), Toast.LENGTH_SHORT).show();
                    attendanceRecord.setStud_attendance_date(formattedDate);
                    databaseReference.child(collection.get(i).getStud_prn_no()+"-"+currentDate).setValue(attendanceRecord);
                }
                // after adding this data we are showing toast message.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(ScannerActivity.this, "fail to add data."+error, Toast.LENGTH_SHORT).show();
            }
        });

             */

    }

}