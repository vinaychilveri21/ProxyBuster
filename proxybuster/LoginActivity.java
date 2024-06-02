package com.example.proxybuster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Pair;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proxybuster.databinding.ActivityLoginBinding;
import com.example.proxybuster.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
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

    TextInputLayout txt_prn,txt_password;

    ImageView img_logo;

    AppCompatButton btn_login;

    ProgressBar login_progressbar;

    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    static StudentInfo stud_obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_login);
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        txt_prn=findViewById(R.id.loginprncontainer);
        txt_password=findViewById(R.id.loginpasswordcontainer);
        btn_login=findViewById(R.id.btn_login);
        textSignUp=findViewById(R.id.txt_signUp);
        welcome=findViewById(R.id.welcome);
        login_to_continue=findViewById(R.id.login_to_continue);
        img_logo=findViewById(R.id.img_logo);
        login_progressbar=findViewById(R.id.login_progress);

        //stud_obj=new StudentInfo();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        }
        String text = "<font color=#000>Don't have an account? </font> <font color=#1637EF> Sign Up</font>";
        textSignUp.setText(Html.fromHtml(text));

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUp=new Intent(LoginActivity.this,RegistrationActivity.class);
                Pair[] pairs=new Pair[7];

                pairs[0]=new Pair<View,String>(img_logo,"logo_img");
                pairs[1]=new Pair<View,String>(welcome,"app_name");
                pairs[2]=new Pair<View,String>(login_to_continue,"app_desc");
                pairs[3]=new Pair<View,String>(txt_prn,"prn_tran");
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
                String prn,password;
                prn=txt_prn.getEditText().getText().toString();
                password=txt_password.getEditText().getText().toString();
                login_progressbar.setVisibility(View.VISIBLE);

                if(!loginprnFocusListener() || !loginpasswordFocusListener()){
                    Toast.makeText(LoginActivity.this, "Please fill up the data properly", Toast.LENGTH_SHORT).show();
                    login_progressbar.setVisibility(View.GONE);

                    binding.loginprncontainer.setHelperText(validPRN());
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


        loginprnFocusListener();
        loginpasswordFocusListener();

    }

    //getting data from firebase

    private void isUser(){
        String userEnteredprn,userEnteredPassword;
        userEnteredprn=txt_prn.getEditText().getText().toString();
        userEnteredPassword=txt_password.getEditText().getText().toString();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("StudentInfo");
        Query checkUser=reference.orderByChild("stud_prn_no").equalTo(userEnteredprn);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String passwordfromDB=snapshot.child(userEnteredprn).child("stud_pass").getValue(String.class);
                    binding.loginprncontainer.setHelperText("");

                    if(passwordfromDB.equals(userEnteredPassword)){

                        binding.loginpasswordcontainer.setHelperText("");
                        login_progressbar.setVisibility(View.GONE);

                        String studnamefromDB,studprnfromDB,studrollfromDB,studyearfromDB,studdeptfromDB,studdivfromDB,
                                studmobfromDB,studemailfromDB;

                        studnamefromDB=snapshot.child(userEnteredprn).child("stud_name").getValue(String.class);
                        studprnfromDB=snapshot.child(userEnteredprn).child("stud_prn_no").getValue(String.class);
                        studrollfromDB=snapshot.child(userEnteredprn).child("stud_roll").getValue(String.class);
                        studyearfromDB=snapshot.child(userEnteredprn).child("stud_year").getValue(String.class);
                        studdeptfromDB=snapshot.child(userEnteredprn).child("stud_dept").getValue(String.class);
                        studdivfromDB=snapshot.child(userEnteredprn).child("stud_div").getValue(String.class);
                        studmobfromDB=snapshot.child(userEnteredprn).child("stud_mob").getValue(String.class);
                        studemailfromDB=snapshot.child(userEnteredprn).child("stud_email").getValue(String.class);


                        stud_obj=new StudentInfo(studnamefromDB,studprnfromDB,studrollfromDB,studyearfromDB,
                                       studdeptfromDB,studdivfromDB,studmobfromDB,studemailfromDB,passwordfromDB);
//                        stud_obj.setStud_name(studnamefromDB);
//                        stud_obj.setStud_prn_no(studprnfromDB);
//                        stud_obj.setStud_roll(studrollfromDB);
//                        stud_obj.setStud_year(studyearfromDB);
//                        stud_obj.setStud_dept(studdeptfromDB);
//                        stud_obj.setStud_div(studdivfromDB);
//                        stud_obj.setStud_mob(studmobfromDB);
//                        stud_obj.setStud_email(studemailfromDB);
//                        stud_obj.setStud_pass(passwordfromDB);
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
                    binding.loginprncontainer.setHelperText("No such PRN no. exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /*
    private String trim(String string) {
        if(string.length() == 0) {
            return string;
        }

        int ind = string.length() - 1;

        while(string.charAt(ind) == ' ') {
            ind--;
        }

        return string.substring(0, ind + 1);
    }

     */

    //directly goto home or dashboard activity if user already login

    /*
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        }

    }

     */



    // email hypertext

    /*
   private Boolean loginemailFocusListener() {
        binding.loginemail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.loginemailcontainer.setHelperText(!hasFocus ? validEmail() : " ");
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
        String val= trim(binding.loginemail.getText().toString());

        if(val.isEmpty())
        {
            return "Field can't be empty.";
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()){

            return "Invalid email address.";
        }

        return null;
    }

     */

    //prn no hypertext
    private Boolean loginprnFocusListener() {
        binding.editprn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                binding.loginprncontainer.setHelperText(!hasFocus ? validPRN() : " ");
            }
        });
        if(validPRN()==null){
            return true;
        }
        else{
            return false;
        }
    }
    private String validPRN() {

        String val=binding.editprn.getText().toString();

        if(val.isEmpty())
        {
            return "Field can't be empty.";
        }
        if(!val.matches("^(?=.*[0-9])(?=.*[A-Z])[A-Z0-9]+$")){
            return "Field must contain numbers along with upper character.";
        }

        return null;
    }


    // password hypertext

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