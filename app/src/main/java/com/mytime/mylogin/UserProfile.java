package com.mytime.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private TextView textViewWelcome,textViewFullName,textViewEmail,textViewDoB,textViewGender,textViewPhone;
    private String fullName,email,doB,gender,mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        //use toolbar as action bar
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
       navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);


        ///values
        textViewWelcome=findViewById(R.id.textViewWelcome);
        textViewFullName=findViewById(R.id.textview_show_full_Name);
        textViewEmail=findViewById(R.id.textview_show_email);
        textViewDoB=findViewById(R.id.textview_show_dob);
        textViewGender=findViewById(R.id.textview_show_gender);
        textViewPhone=findViewById(R.id.textview_show_phone);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        if(firebaseUser==null)
        {
            Toast.makeText(UserProfile.this,"Something went wrong! User details are not available at the moment", Toast.LENGTH_LONG).show();
        }
        else{
            checkIfEmailVerified(firebaseUser);
            showUserProfile(firebaseUser);

        }
    }
//user coming to userprofileActivity after registration
    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified())
        {
            showAlertDialog();

        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(UserProfile.this);
        builder.setTitle("Email not Verified");
        builder.setMessage("Please verify your email now.You cannot login without email verification next time.");
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

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId=firebaseUser.getUid();
        //extracting user reference from database
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if(readWriteUserDetails!=null)
                {
                    fullName= readWriteUserDetails.fullName;
                    email=firebaseUser.getEmail();
                    doB=readWriteUserDetails.doB;
                    gender=readWriteUserDetails.gender;
                    mobile=readWriteUserDetails.mobile;

                    textViewWelcome.setText("Welcome "+fullName+" !");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewPhone.setText(mobile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfile.this,"Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_home:
                Intent intent=new Intent(UserProfile.this,UserHomePage.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:

                break;
            case R.id.nav_refresh:
                startActivity(getIntent());
                overridePendingTransition(0,0);
                break;
           /* case R.id.nav_update_profile:
                Intent intent1=new Intent(UserProfile.this,UpdateProfileActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_delete_profile:
                break;
            case R.id.nav_update_email:
                Intent intent2=new Intent(UserProfile.this,UpdateEmailActivity.class);
                startActivity(intent2);
                break;
            case R.id.nav_settings:
                Toast.makeText(UserProfile.this,"menu_settings",Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_change_password:
                break;*/
            case R.id.nav_logout:
                authProfile.signOut();
                Toast.makeText(UserProfile.this,"You have logged out",Toast.LENGTH_LONG).show();
                Intent intent3=new Intent(UserProfile.this,LoginActivity.class);
                //clear stack to prevent user coming back to user procfile
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                finish();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}