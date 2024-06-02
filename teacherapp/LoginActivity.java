package com.example.teacherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teacherapp.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    TextView textSignUp,welcome,login_to_continue;

    TextInputLayout txt_mob,txt_password;

    ImageView img_logo;

    AppCompatButton btn_login;

    ProgressBar login_progressbar;

    //FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    static TeacherInfo teacher_obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_login);
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        txt_mob=findViewById(R.id.loginmobilecontainer);
        txt_password=findViewById(R.id.loginpasswordcontainer);
        btn_login=findViewById(R.id.btn_login);
        textSignUp=findViewById(R.id.txt_signUp);
        welcome=findViewById(R.id.welcome);
        login_to_continue=findViewById(R.id.login_to_continue);
        img_logo=findViewById(R.id.img_logo);
        login_progressbar=findViewById(R.id.login_progress);

        //teacher_obj=new TeacherInfo();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }

        SpannableString spannableString = new SpannableString("Don't have an account? Sign Up");

        int signupcolor = Color.parseColor("#1637EF");
        spannableString.setSpan(new ForegroundColorSpan(signupcolor), 23, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textSignUp.setText(spannableString);

        /*
        String text = "<font color=#000>Don't have an account? </font> <font color=#1637EF> Sign Up</font>";
        textSignUp.setText(Html.fromHtml(text));

         */

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUp=new Intent(LoginActivity.this,RegistrationActivity.class);
                Pair[] pairs=new Pair[7];

                pairs[0]=new Pair<View,String>(img_logo,"logo_img");
                pairs[1]=new Pair<View,String>(welcome,"app_name");
                pairs[2]=new Pair<View,String>(login_to_continue,"app_desc");
                pairs[3]=new Pair<View,String>(txt_mob,"mob_tran");
                pairs[4]=new Pair<View,String>(txt_password,"password_tran");
                pairs[5]=new Pair<View,String>(btn_login,"button_tran");
                pairs[6]=new Pair<View,String>(textSignUp,"login_signup_tran");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(SignUp,options.toBundle());

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mob,password;
                mob=txt_mob.getEditText().getText().toString();
                password=txt_password.getEditText().getText().toString();
                login_progressbar.setVisibility(View.VISIBLE);

                if(!loginmobFocusListener() || !loginpasswordFocusListener()){
                    Toast.makeText(LoginActivity.this, "Please fill up the data properly", Toast.LENGTH_SHORT).show();
                    login_progressbar.setVisibility(View.GONE);

                    binding.loginmobilecontainer.setHelperText(validMobile());
                    binding.loginpasswordcontainer.setHelperText(validPassword());
                    return;
                }
                else{
                    isUser();
                }


                /*

                firebaseAuth.signInWithEmailAndPassword(email,password)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        login_progressbar.setVisibility(View.GONE);
                                        //binding.loginpasswordcontainer.setHelperText("");

//                                        Toast.makeText(LoginActivity.this, "value="+authResult, Toast.LENGTH_SHORT).show();

                                        isUser();
                                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        //function for getting data from firebase


                                        /*

                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = database.getReference("https://student-registration-dat-89f40-default-rtdb.firebaseio.com/StudentInfo");

                                        // Attach a listener to read the data at our posts reference
                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                StudentInfo info = dataSnapshot.getValue(StudentInfo.class);
                                                System.out.println(info);
                                                Intent home=new Intent(LoginActivity.this,DashboardActivity.class);
                                                startActivity(home);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                System.out.println("The read failed: " + databaseError.getCode());
                                            }
                                        });






                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                login_progressbar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Please enter valid email and password.", Toast.LENGTH_SHORT).show();
                            }
                        });

                 */


            }
        });


        loginmobFocusListener();
        loginpasswordFocusListener();
    }
    private void isUser(){
        String userEnteredemob,userEnteredPassword;
        userEnteredemob=txt_mob.getEditText().getText().toString();
        userEnteredPassword=txt_password.getEditText().getText().toString();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("TeacherInfo");
        Query checkUser=reference.orderByChild("teacher_mob").equalTo(userEnteredemob);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String passwordfromDB=snapshot.child(userEnteredemob).child("teacher_pass").getValue(String.class);
                    binding.loginmobilecontainer.setHelperText("");

                    if(passwordfromDB.equals(userEnteredPassword)){

                        binding.loginpasswordcontainer.setHelperText("");
                        login_progressbar.setVisibility(View.GONE);

                        String teachernamefromDB,teachermobfromDB,teacheremailfromDB;

                        teachernamefromDB=snapshot.child(userEnteredemob).child("teacher_name").getValue(String.class);
                        teachermobfromDB=snapshot.child(userEnteredemob).child("teacher_mob").getValue(String.class);
                        teacheremailfromDB=snapshot.child(userEnteredemob).child("teacher_email").getValue(String.class);

                        teacher_obj=new TeacherInfo(teachernamefromDB,teachermobfromDB,teacheremailfromDB,passwordfromDB);

//                        teacher_obj.setTeacher_name(teachernamefromDB);
//                        teacher_obj.setTeacher_mob(teachermobfromDB);
//                        teacher_obj.setTeacher_email(teacheremailfromDB);
//                        teacher_obj.setTeacher_pass(passwordfromDB);
                        /*
                        home.putExtra("name",studnamefromDB);
                        home.putExtra("prn",studprnfromDB);
                        home.putExtra("roll",studrollfromDB);
                        home.putExtra("year",studyearfromDB);
                        home.putExtra("dept",studdeptfromDB);
                        home.putExtra("div",studdivfromDB);
                        home.putExtra("mob",studmobfromDB);
                        home.putExtra("email",studemailfromDB);
                        home.putExtra("pass",passwordfromDB);

                         */
                        Intent home=new Intent(LoginActivity.this,DashboardActivity.class);
                        startActivity(home);

                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        login_progressbar.setVisibility(View.GONE);
                        binding.loginpasswordcontainer.setHelperText("Invalid Password");
                    }
                }
                else{
                    login_progressbar.setVisibility(View.GONE);
                    binding.loginmobilecontainer.setHelperText("No such Mobile exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //mobile hypertext
    private Boolean loginmobFocusListener() {
        binding.loginmob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.loginmobilecontainer.setHelperText(!hasFocus ? validMobile() : " ");
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

        String val=binding.loginmob.getText().toString();

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

    private Boolean loginpasswordFocusListener() {
        binding.loginpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.loginpasswordcontainer.setHelperText(!hasFocus ? validPassword() : " ");
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

        String password=binding.loginpassword.getText().toString();

        if(password.isEmpty())
        {
            return "Field can't be empty.";
        }
        if(password.length()<6){
            return "Password must be at least 6 characters.";
        }
        if(!password.matches(".*[A-Z].*")){
            return "Password must contain at least one uppercase letter.";
        }
        if(!password.matches(".*[a-z].*")){
            return "Password must contain at least one lowercase letter.";
        }
        if(!password.matches(".*\\d.*")){
            return "Password must contain at least one digit.";
        }
        if(!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~].*")){
            return "Password must contain at least one special character.";
        }
        if(!password.matches("^\\S+$")){
            return "Password cannot contain whitespace characters.";
        }
        if(password.length()>25){
            return "Password must be at most 25 characters long.";
        }

        binding.loginpasswordcontainer.setHelperText("");
        return null;
    }
}