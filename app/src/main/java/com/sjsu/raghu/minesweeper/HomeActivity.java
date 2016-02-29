package com.sjsu.raghu.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View rulesBtn = findViewById(R.id.btnRules);
        rulesBtn.setOnClickListener(this);

        View exitBtn =  findViewById(R.id.btnExit);
        exitBtn.setOnClickListener(this);

        View resumeBtn = findViewById(R.id.btnResume);
        resumeBtn.setOnClickListener(this);

        View startBtn = findViewById(R.id.btnStart);
        startBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnExit:
                finish(); //end the application
                break;
            case R.id.btnResume:
                // Code come here
                break;
            case R.id.btnStart:
                // Code come here
                startNewGame();
                break;
            case R.id.btnRules:
                // Code come here
                showRules();
                break;
        }
    }

    public void showRules(){
        Intent rulesIntent = new Intent(this, RulesActivity.class);
        startActivity(rulesIntent);
    }

    public void startNewGame(){
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
    }

}
