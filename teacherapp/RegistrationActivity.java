package com.example.teacherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.example.teacherapp.databinding.ActivityRegistrationBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {


    ProgressBar progressBar;
    ImageView img_logo;
    TextView textLogIn,txt_welcome,txt_signup_to_start;
    String pass1;

    TextInputLayout reg_name,reg_mob,reg_mail,reg_password,reg_conf_pass;

    ActivityRegistrationBinding binding;

    AppCompatButton btn_signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_registration);
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        img_logo=findViewById(R.id.img_logo1);
        txt_welcome=findViewById(R.id.welcome1);
        txt_signup_to_start=findViewById(R.id.sign_up_to_start);
        textLogIn=findViewById(R.id.txt_back_to_login);
        reg_name=findViewById(R.id.fullnamecontainer);
        reg_mob=findViewById(R.id.mobilecontainer);
        reg_mail=findViewById(R.id.emailcontainer);
        reg_password=findViewById(R.id.passwordcontainer);
        reg_conf_pass=findViewById(R.id.confirmpasswordcontainer);
        btn_signup=findViewById(R.id.btn_signup);
        progressBar=findViewById(R.id.reg_progress);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        String text = "<font color=#000>Already have an account? </font> <font color=#1637EF> LogIn</font>";
        textLogIn.setText(Html.fromHtml(text));



        textLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LogIn=new Intent(RegistrationActivity.this, LoginActivity.class);
                Pair[] pairs=new Pair[7];

                pairs[0]=new Pair<View,String>(img_logo,"logo_img");
                pairs[1]=new Pair<View,String>(txt_welcome,"app_name");
                pairs[2]=new Pair<View,String>(txt_signup_to_start,"app_desc");
                pairs[3]=new Pair<View,String>(reg_mail,"mail_tran");
                pairs[4]=new Pair<View,String>(reg_password,"password_tran");
                pairs[5]=new Pair<View,String>(btn_signup,"button_tran");
                pairs[6]=new Pair<View,String>(textLogIn,"login_signup_tran");

                LogIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(RegistrationActivity.this,pairs);
                startActivity(LogIn,options.toBundle());
                finish();





            }

        });

        nameFocusListener();
        mobileFocusListener();
        emailFocusListener();
        passwordFocusListener();
        confirmpasswordFocusListener();

    }
    //fullname hypertext
    private Boolean nameFocusListener() {
        binding.editfullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.fullnamecontainer.setHelperText(!hasFocus ? validName() : " ");
            }
        });
        if(validName()==null){
            return true;
        }
        else{
            return false;
        }
    }
    private String validName() {

        String val=binding.editfullname.getText().toString();

        if(val.isEmpty())
        {
            return "Field can't be empty.";
        }
        if(!val.matches("^[A-Za-z\\s]+$")){
            return "Field must contain characters only.";
        }

        return null;
    }

    //mobile hypertext
    private Boolean mobileFocusListener() {
        binding.editmob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.mobilecontainer.setHelperText(!hasFocus ? validMobile() : " ");
            }
        });
        if(validMobile()==null){
            return true;
        }
        else{
            return false;
        }
    }
    private String validMobile() {

        String val=binding.editmob.getText().toString();

        if(val.isEmpty())
        {
            return "Field can't be empty.";
        }
        else if (val.length()!=10){
            return "Mobile No. should be 10 digits.";

        }
        else if(!Patterns.PHONE.matcher(val).matches()){
            return "Field shouldn't contain character.";
        }

        return null;
    }

    // email hypertext
    private Boolean emailFocusListener() {
        binding.editregmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.emailcontainer.setHelperText(!hasFocus ? validEmail() : " ");
            }
        });
        if(validEmail()==null){
            return true;
        }
        else{
            return false;
        }
    }
    private String validEmail() {

        String val=binding.editregmail.getText().toString();

        if(val.isEmpty())
        {
            return "Field can't be empty.";
        }
        else if(!val.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.edu$")){

            return "Invalid email address.";
        }

        return null;
    }

    // password hypertext
    private Boolean passwordFocusListener() {
        binding.editregpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.passwordcontainer.setHelperText(!hasFocus ? validPassword() : " ");
            }
        });
        if(validPassword()==null){
            return true;
        }
        else{
            return false;
        }
    }
    private String validPassword() {

        pass1=binding.editregpassword.getText().toString();
        if(pass1.isEmpty())
        {
            return "Field can't be empty.";
        }
        if(pass1.length()<6){
            return "Password must be at least 6 characters.";
        }
        if(!pass1.matches(".*[A-Z].*")){
            return "Password must contain at least one uppercase letter.";
        }
        if(!pass1.matches(".*[a-z].*")){
            return "Password must contain at least one lowercase letter.";
        }
        if(!pass1.matches(".*\\d.*")){
            return "Password must contain at least one digit.";
        }
        if(!pass1.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~].*")){
            return "Password must contain at least one special character.";
        }
        if(!pass1.matches("^\\S+$")){
            return "Password cannot contain whitespace characters.";
        }
        if(pass1.length()>25){
            return "Password must be at most 25 characters long.";
        }

        return null;
    }

    // confirm password hypertext
    private Boolean confirmpasswordFocusListener() {
        binding.editregconfirmpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.confirmpasswordcontainer.setHelperText(!hasFocus ? validConfirmPassword() : " ");
            }
        });
        if(validConfirmPassword()==null){
            return true;
        }
        else{
            return false;
        }
    }
    private String validConfirmPassword() {

        String pass2=binding.editregconfirmpassword.getText().toString();
        if(pass2.isEmpty())
        {
            return "Field can't be empty.";
        }
        if(!pass1.equals(pass2)){
            return "Password does not matches.";
        }
        binding.confirmpasswordcontainer.setHelperText("");


        return null;
    }

    public void funRegister(View view) {

        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLACK));


        if(!nameFocusListener()  || !mobileFocusListener() || !emailFocusListener() || !passwordFocusListener() || !confirmpasswordFocusListener()){

            Toast.makeText(this,"Please fill up the data properly.",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            binding.fullnamecontainer.setHelperText(validName());
            binding.mobilecontainer.setHelperText(validMobile());
            binding.emailcontainer.setHelperText(validEmail());
            binding.passwordcontainer.setHelperText(validPassword());
            binding.confirmpasswordcontainer.setHelperText(validConfirmPassword());

            return;
        }

        //fetching values in string
        String full_name,prn_no,roll,year,dept,div,mob,email,pass,cfm_pass;

        full_name=reg_name.getEditText().getText().toString();
        mob=reg_mob.getEditText().getText().toString();
        email=reg_mail.getEditText().getText().toString();
        pass=reg_password.getEditText().getText().toString();
        cfm_pass=reg_conf_pass.getEditText().getText().toString();



        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mob,
                60,
                TimeUnit.SECONDS,
                RegistrationActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);

                        Intent otpverification=new Intent(RegistrationActivity.this,OTPVerificationActivity.class);
                        otpverification.putExtra("full_name",full_name);
                        otpverification.putExtra("mobile",mob);
                        otpverification.putExtra("email",email);
                        otpverification.putExtra("password",pass);
                        otpverification.putExtra("confirm_password",cfm_pass);
                        otpverification.putExtra("verificationID",verificationID);

                        Pair[] pairs=new Pair[5];
                        pairs[0]=new Pair<View,String>(img_logo,"logo_img");
                        pairs[1]=new Pair<View,String>(txt_welcome,"app_name");
                        pairs[2]=new Pair<View,String>(txt_signup_to_start,"app_desc");
                        pairs[3]=new Pair<View,String>(btn_signup,"button_tran");
                        pairs[4]=new Pair<View,String>(textLogIn,"login_signup_tran");

                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(RegistrationActivity.this,pairs);
                        startActivity(otpverification,options.toBundle());
                    }
                });

    }
}