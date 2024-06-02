package com.example.proxybuster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OTPVerificationActivity extends AppCompatActivity {

    TextView txt_mob,txt_resend_otp,txt_otp_desc,txt_otp_mob;
    EditText edit_otp1,edit_otp2,edit_otp3,edit_otp4,edit_otp5,edit_otp6;
    LinearLayout layout_otp_input,layout_resend_otp;
    ImageView otp_img;
    ProgressBar otp_progress;
    AppCompatButton btn_verify;
    //true after every 60 second
    boolean resendEnabled=false;
    int resendTime=60;
    String verificationID;
    int selected_edit_text_position=0;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebasedatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    // creating a variable for
    // our object class
    StudentInfo studentInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        otp_img=findViewById(R.id.otp_img);
        txt_otp_desc=findViewById(R.id.txt_otp_desc);
        txt_otp_mob=findViewById(R.id.txt_otp_mob);
        txt_mob=findViewById(R.id.txt_mobile);
        layout_otp_input=findViewById(R.id.layout_otp_input);
        layout_resend_otp=findViewById(R.id.layout_resend_otp);
        btn_verify=findViewById(R.id.verify_button);
        txt_resend_otp=findViewById(R.id.txt_resend_otp);
        edit_otp1=findViewById(R.id.edit_otp1);
        edit_otp2=findViewById(R.id.edit_otp2);
        edit_otp3=findViewById(R.id.edit_otp3);
        edit_otp4=findViewById(R.id.edit_otp4);
        edit_otp5=findViewById(R.id.edit_otp5);
        edit_otp6=findViewById(R.id.edit_otp6);
        otp_progress=findViewById(R.id.otp_progress);

        // below line is used to get the
        // instance of our FIrebase database.
        firebasedatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.

        databaseReference=firebasedatabase.getReference("StudentInfo");

        // initializing our object
        // class variable.

//        studentInfo=new StudentInfo();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        txt_mob.setText(String.format("+91-%s",getIntent().getStringExtra("mobile")));
        verificationID=getIntent().getStringExtra("verificationID");

        //getting text from our edittext fields of registrationActivity

        String full_name,prn_no,roll,year,dept,div,mob,email,pass,cfm_pass;
        full_name=getIntent().getStringExtra("full_name");
        prn_no=getIntent().getStringExtra("prn_no");
        roll=getIntent().getStringExtra("roll");
        year=getIntent().getStringExtra("year");
        dept=getIntent().getStringExtra("dept");
        div=getIntent().getStringExtra("div");
        mob=getIntent().getStringExtra("mobile");
        email=getIntent().getStringExtra("email");
        pass=getIntent().getStringExtra("password");
        cfm_pass=getIntent().getStringExtra("confirm_password");



        edit_otp1.addTextChangedListener(textWatcher);
        edit_otp2.addTextChangedListener(textWatcher);
        edit_otp3.addTextChangedListener(textWatcher);
        edit_otp4.addTextChangedListener(textWatcher);
        edit_otp5.addTextChangedListener(textWatcher);
        edit_otp6.addTextChangedListener(textWatcher);



        //by default open keyboar at edittext otp1
        showKeyboard(edit_otp1);

        //calling startCountDown function
        startCountDownTimer();

        txt_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resendEnabled){

                    //handling resend otp
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+getIntent().getStringExtra("mobile"),
                            60,
                            TimeUnit.SECONDS,
                            OTPVerificationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                    Toast.makeText(OTPVerificationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                                @Override
                                public void onCodeSent(@NonNull String newverificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                    verificationID=newverificationID;
                                    Toast.makeText(OTPVerificationActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                                }
                            });


                    //start new resend count down timer
                    startCountDownTimer();
                }
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit_otp1.getText().toString().trim().isEmpty()
                        || edit_otp2.getText().toString().trim().isEmpty()
                        || edit_otp3.getText().toString().trim().isEmpty()
                        || edit_otp4.getText().toString().trim().isEmpty()
                        || edit_otp5.getText().toString().trim().isEmpty()
                        || edit_otp1.getText().toString().trim().isEmpty()){
                    Toast.makeText(OTPVerificationActivity.this,"Please enter valid OTP.",Toast.LENGTH_SHORT).show();
                    return;
                }
                String getOTP=edit_otp1.getText().toString()+
                        edit_otp2.getText().toString()+
                        edit_otp3.getText().toString()+
                        edit_otp4.getText().toString()+
                        edit_otp5.getText().toString()+
                        edit_otp6.getText().toString();

                if(verificationID!=null){
                    otp_progress.setVisibility(View.VISIBLE);
                    PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(verificationID,getOTP);
                    FirebaseAuth.getInstance().
                            signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    otp_progress.setVisibility(View.GONE);
                                    if(task.isSuccessful()){

                                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,cfm_pass)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){
                                                            //add database function
                                                            addDatatoFirebase(full_name, prn_no, roll, year, dept, div, mob, email, pass);
                                                            Intent intent=new Intent(OTPVerificationActivity.this,LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            Toast.makeText(OTPVerificationActivity.this,"Verification done successfully.",Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(OTPVerificationActivity.this,"Data Registered Successfully.",Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(OTPVerificationActivity.this, "Please enter valid details", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                        /*

                                        UserInfo helperClass=new UserInfo(full_name,roll,year,dept,div,mob,email,pass,cfm_pass);
                                        reference.child(full_name).setValue(helperClass);
                                        Intent intent=new Intent(OTPVerificationActivity.this,DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(OTPVerificationActivity.this,"Verification done successfully.",Toast.LENGTH_SHORT).show();


                                         */
                                    }
                                    else{
                                        Toast.makeText(OTPVerificationActivity.this,"please enter valid gmail",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else{
                    Toast.makeText(OTPVerificationActivity.this,"The OTP you entered was wrong.",Toast.LENGTH_SHORT).show();
                }

            }


        });

    }


    private void addDatatoFirebase(String full_name,String prn_no,String roll,String year,String dept,String div,
                                   String mob,String email,String pass){
        // below 9 lines of code is used to set
        // data in our object class.
//        studentInfo.setStud_name(full_name);
//        studentInfo.setStud_prn_no(prn_no);
//        studentInfo.setStud_roll(roll);
//        studentInfo.setStud_year(year);
//        studentInfo.setStud_dept(dept);
//        studentInfo.setStud_div(div);
//        studentInfo.setStud_mob(mob);
//        studentInfo.setStud_email(email);
//        studentInfo.setStud_pass(pass);

        studentInfo=new StudentInfo(full_name,prn_no,roll,year,dept,div,mob,email,pass);
        databaseReference.child(prn_no).setValue(studentInfo);

        // we are use add value event listener method
        // which is called with database reference.
        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.child(prn_no).setValue(studentInfo);
                // after adding this data we are showing toast message.
                Toast.makeText(OTPVerificationActivity.this, "data added", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(OTPVerificationActivity.this, "fail to add data."+error, Toast.LENGTH_SHORT).show();
            }
        });

         */

    }




    void showKeyboard(EditText edit_otp){
        edit_otp.requestFocus();
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(edit_otp,InputMethodManager.SHOW_IMPLICIT);
    }


    void startCountDownTimer(){
        resendEnabled=false;
        txt_resend_otp.setTextColor(Color.parseColor("#EC0956"));

        new CountDownTimer(resendTime*1000,1000){

            @Override
            public void onTick(long l) {
                txt_resend_otp.setText("Resend OTP ("+(l/1000)+")");
            }

            @Override
            public void onFinish() {
                resendEnabled=true;
                txt_resend_otp.setText("Resend OTP.");
                txt_resend_otp.setTextColor(getResources().getColor(R.color.black));
            }
        }.start();
    }


    TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if(editable.length()>0){

                if(selected_edit_text_position==0){

                    selected_edit_text_position=1;
                    showKeyboard(edit_otp2);

                }
                else if(selected_edit_text_position==1){

                    selected_edit_text_position=2;
                    showKeyboard(edit_otp3);

                }
                else if(selected_edit_text_position==2){

                    selected_edit_text_position=3;
                    showKeyboard(edit_otp4);

                }
                else if(selected_edit_text_position==3){

                    selected_edit_text_position=4;
                    showKeyboard(edit_otp5);

                }
                else if(selected_edit_text_position==4){

                    selected_edit_text_position=5;
                    showKeyboard(edit_otp6);

                }
            }

        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_DEL){

            if(selected_edit_text_position==5){
                selected_edit_text_position=4;
                showKeyboard(edit_otp5);

            }
            else if(selected_edit_text_position==4){
                selected_edit_text_position=3;
                showKeyboard(edit_otp4);
            }
            else if(selected_edit_text_position==3){
                selected_edit_text_position=2;
                showKeyboard(edit_otp3);
            }
            else if(selected_edit_text_position==2){
                selected_edit_text_position=1;
                showKeyboard(edit_otp2);
            }
            else if(selected_edit_text_position==1){
                selected_edit_text_position=0;
                showKeyboard(edit_otp1);
            }
            return  true;
        }
        else{
            return super.onKeyUp(keyCode, event);
        }
    }


}