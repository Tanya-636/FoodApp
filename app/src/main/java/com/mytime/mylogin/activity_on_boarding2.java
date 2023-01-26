package com.mytime.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_on_boarding2 extends AppCompatActivity {

    Button backbtn2, nextbtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding2);

        backbtn2 = findViewById(R.id.backbtn2);
        nextbtn2 = findViewById(R.id.nextbtn2);
        nextbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity3();
            }
        });

        backbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity1();
            }
        });
    }

    public void openActivity3(){
        Intent intent = new Intent(this, activity_on_boarding3.class);
        startActivity(intent);

    }

    public void openActivity1(){
        Intent intent = new Intent(this, activity_on_boading1.class);
        startActivity(intent);

    }


}