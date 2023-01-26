package com.mytime.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_on_boarding3 extends AppCompatActivity {


    Button nextbtn3, backbtn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding3);

        backbtn3 = findViewById(R.id.backbtn3);
        nextbtn3 = findViewById(R.id.nextbtn3);
        nextbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });

        backbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity5();
            }
        });
    }

    public void openActivity4(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    public void openActivity5(){
        Intent intent = new Intent(this, activity_on_boarding2.class);
        startActivity(intent);

    }
}