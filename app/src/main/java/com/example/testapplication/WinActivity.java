package com.example.testapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WinActivity extends AppCompatActivity {

    private TextView winLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        winLabel = findViewById(R.id.winText);
        Intent intent = getIntent();
        winLabel.setText(intent.getStringExtra("winText"));
    }

    public void btnExit (View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void btnRestart (View v){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}