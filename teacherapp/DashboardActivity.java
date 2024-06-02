package com.example.teacherapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.teacherapp.databinding.ActivityDashboardBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import android.content.pm.ActivityInfo;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ImageView img_menu_icon,img_scanner,img_teacher_profile;

    LinearLayout contentView;

    WebView webView;

    ActivityDashboardBinding binding;

    TextView txt_teacher_header_name;

    static  final float END_SCALE =0.7f;

    DatabaseReference databaseReference;
    ValueEventListener eventListener;



    private ActivityResultLauncher<String> requestPermissionLauncher=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted-> {
                if(isGranted){
                        showCamera();
                }
                else{
                    //show why user need this permission
                }
            });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher=registerForActivityResult(new ScanContract(),result->{
        if(result.getContents()==null){
            //Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(DashboardActivity.this, ScannerActivity.class);
            startActivity(intent);
        }
        else{
                setResult(result.getContents());
        }
    });

    private void setResult(String contents) {

        int i=StudentInfo.collectionPRN.indexOf(contents);
        StudentInfo.collection.get(i).setAttendance("P");
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initView();
        //setContentView(R.layout.activity_dashboard);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);
        img_menu_icon=findViewById(R.id.img_menu_icon);
        img_scanner=findViewById(R.id.imgscanner);
        webView=findViewById(R.id.webview);
        // Inflate the header view
        View headerView = navigationView.getHeaderView(0);

        // Access the TextView from the header view
        txt_teacher_header_name = headerView.findViewById(R.id.txt_teacher_header_name);
        img_teacher_profile=headerView.findViewById(R.id.img_profile);

        // Now you can manipulate the TextView
        txt_teacher_header_name.setText(LoginActivity.teacher_obj.getTeacher_name());

        contentView=findViewById(R.id.content);


        databaseReference= FirebaseDatabase.getInstance().getReference("StudentInfo");

        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StudentInfo.collection.clear();
                StudentInfo.collectionPRN.clear();
                for(DataSnapshot items:snapshot.getChildren()){
                    StudentInfo studentInfo=items.getValue(StudentInfo.class);
                    StudentInfo.collection.add(studentInfo);
                    StudentInfo.collectionPRN.add(studentInfo.getStud_prn_no());
                }
//                Toast.makeText(DashboardActivity.this, "Data : "+StudentInfo.collection.size(), Toast.LENGTH_SHORT).show();
                StudentAttendanceAdapter adapter=new StudentAttendanceAdapter(DashboardActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });




        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Mobile Safari/537.36");
        webView.loadUrl("https://cms.sinhgad.edu/sinhgad_engineering_institutes/vadgaon_scoe/about.aspx");

        navigationDrawer();

    }

    private void initBinding() {
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    private void initView() {
        binding.imgscanner.setOnClickListener(view -> {
            checkPermissionAndShowActivity(this);
        });
    }



    public void checkPermissionAndShowActivity(Context context) {
        if(ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        )== PackageManager.PERMISSION_GRANTED){
            showCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
            Toast.makeText(context, "camera permission required", Toast.LENGTH_SHORT).show();
        }else{
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

    }



    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        img_menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animationNavigationDrawer();
    }

    private void animationNavigationDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.splash_status_color));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //scale the view based on  current slide offset
                final float diffScaledOffset = slideOffset*(1-END_SCALE);
                final float offsetScale=1-diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //translate the view, accounting for the scaled width
                final float xOffset=drawerView.getWidth() * slideOffset;
                final float xOffsetdiff=contentView.getWidth() * diffScaledOffset/2;
                final float xTranslation=xOffset - xOffsetdiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(R.id.nav_home==item.getItemId()){
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        }
        else if(R.id.nav_myattendance==item.getItemId()){
            startActivity(new Intent(getApplicationContext(), AttendanceActivity.class));
        }
        else if(R.id.nav_rating==item.getItemId()){
            startActivity(new Intent(getApplicationContext(), RateUsActivity.class));
        }
        else if(R.id.nav_profile==item.getItemId()){
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
        else if(R.id.nav_logout==item.getItemId()){
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            return false;
        }

        return true;
    }
}