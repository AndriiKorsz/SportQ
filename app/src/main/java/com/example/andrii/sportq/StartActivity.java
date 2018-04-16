package com.example.andrii.sportq;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class StartActivity extends AppCompatActivity {
    Button aaa;
    ImageButton footballImBtn, basketballImBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        footballImBtn = (ImageButton)findViewById(R.id.football_ImBtn);
        basketballImBtn = (ImageButton)findViewById(R.id.basketball_ImBtn);

        footballImBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("ball", "Football");
                startActivity(intent);

            }
        });

        basketballImBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("ball", "Basketball");
                startActivity(intent);
            }
        });

    }
}
