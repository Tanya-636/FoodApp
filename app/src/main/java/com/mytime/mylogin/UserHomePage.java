package com.mytime.mylogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();



        //use toolbar as action bar
        setSupportActionBar(toolbar);
navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);






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
                break;
            case R.id.nav_profile:
                Intent intent=new Intent(UserHomePage.this,UserProfile.class);
                startActivity(intent);
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
                Toast.makeText(UserHomePage.this,"You have logged out",Toast.LENGTH_LONG).show();
                Intent intent3=new Intent(UserHomePage.this,LoginActivity.class);
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