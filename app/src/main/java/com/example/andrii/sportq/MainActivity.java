package com.example.andrii.sportq;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SportQ";

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    public String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d(TAG, "MainActivity OnCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "MainActivity OnStart");

        Intent intent = getIntent();

        item = intent.getStringExtra("ball");

        setTitle(item);
        MainActivityFragment quizFragment = (MainActivityFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.fragment);
        Log.d(TAG, "MainActivity OnStart item " + item);
        quizFragment.resetQuiz(item);

    }
}
