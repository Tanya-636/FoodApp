package com.mytime.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextLoginEmail,editTextLoginPwd;
    private FirebaseAuth authProfile;
    private static final String TAG="LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//     getSupportActionBar().setTitle("Login");
     editTextLoginEmail=findViewById(R.id.inputEmail);
     editTextLoginPwd=findViewById(R.id.inputPassword);
     authProfile=FirebaseAuth.getInstance();
        TextView goRegister=findViewById(R.id.gotoRegister);
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openSignUp();
            }
        });

        ImageView imageViewPassword=findViewById(R.id.imageview_show_hide_pwd);
        imageViewPassword.setImageResource(R.drawable.hidden);
        imageViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
                {
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imageViewPassword.setImageResource(R.drawable.hidden);
                }
                else
                {
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewPassword.setImageResource(R.drawable.show);

                }
            }
        });

     //login User
        Button buttonLogin=findViewById(R.id.btnLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail=editTextLoginEmail.getText().toString();
                String textPwd=editTextLoginPwd.getText().toString();
                if(TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email is required");
                    editTextLoginEmail.requestFocus();

                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches())
                {
                    Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Valid email is required");
                    editTextLoginEmail.requestFocus();
                }
                else if(TextUtils.isEmpty(textPwd))
                {
                    Toast.makeText(LoginActivity.this,"Please enter your password",Toast.LENGTH_LONG).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();

                }
                else{
                    loginUser(textEmail,textPwd);
                }
            }
        });
    }

    private void openSignUp() {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void loginUser(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {


                    //get instance of current user
                    FirebaseUser firebaseUser= authProfile.getCurrentUser();
                    ///check email verification
                   /* if(firebaseUser.isEmailVerified())
                    {
                        Toast.makeText(LoginActivity.this,"You are logged in", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this,UserHomePage.class));
                        finish();
                    }
                    else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }*/
                    startActivity(new Intent(LoginActivity.this,UserHomePage.class));
                    finish();

                }
                else{
                    try{
                        throw task.getException();

                    }
                    catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        editTextLoginEmail.setError("User does not exists or is no longer valid.Please register again");
                        editTextLoginEmail.requestFocus();
                    }
                    catch(FirebaseAuthInvalidUserException e)
                    {
                        editTextLoginEmail.setError("Invalid Credentials. Kindly, check and try again.");
                        editTextLoginEmail.requestFocus();

                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(LoginActivity.this,"Something is wrong!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not Verified");
        builder.setMessage("Please verify your email now.You cannot login without email verification.");
        //open email app
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser()!=null) {
            Toast.makeText(LoginActivity.this, "Already Logged in!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this,UserHomePage.class));
            finish();

        }
        else{
            Toast.makeText(LoginActivity.this,"You can login now",Toast.LENGTH_LONG).show();
        }



    }
}