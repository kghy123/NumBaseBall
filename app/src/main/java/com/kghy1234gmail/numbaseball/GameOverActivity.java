package com.kghy1234gmail.numbaseball;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class GameOverActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SingleRecyclerAdapter adapter;
    ArrayList<HitTheRound> hitTheRounds;
    boolean isWin;
    TextView tvWin;
    KonfettiView konfettiView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game_over);

        konfettiView = (KonfettiView) findViewById(R.id.gameover_konfetti_view);

        hitTheRounds = getIntent().getParcelableArrayListExtra("HitTheRounds");
        isWin = getIntent().getBooleanExtra("Win", false);

        recyclerView = (RecyclerView)findViewById(R.id.gameover_recycler);
        adapter = new SingleRecyclerAdapter(this, hitTheRounds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        tvWin = (TextView)findViewById(R.id.gameover_tv_win);

        if(isWin){
            tvWin.setTextColor(Color.GREEN);
            tvWin.setText("승  리");

            konfettiView.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .stream(300, 5000L);

        }else {
            tvWin.setTextColor(Color.RED);
            tvWin.setText("패  배");
        }
    }

    public void clickRestart(View v){

        Intent intent = new Intent(this, SingleGameActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void clickExit(View v){
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
