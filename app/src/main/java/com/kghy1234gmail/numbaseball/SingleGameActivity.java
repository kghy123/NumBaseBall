package com.kghy1234gmail.numbaseball;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Random;

public class SingleGameActivity extends AppCompatActivity {

    int[] comNum = new int[3];
    Random rnd = new Random();

    AnimationDrawable framePitcher;
    ImageView pitcherImageView;

    RecyclerView recyclerView;
    ArrayList<HitTheRound> hitTheRounds = new ArrayList<>();
    SingleRecyclerAdapter adapter;

    MaterialEditText p100, p10, p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_single_game);

        pitcherImageView = (ImageView)findViewById(R.id.single_pitcher_img_view);

        framePitcher = (AnimationDrawable)pitcherImageView.getDrawable();

        recyclerView = (RecyclerView)findViewById(R.id.single_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SingleRecyclerAdapter(this, hitTheRounds);
        recyclerView.setAdapter(adapter);

        p100 = (MaterialEditText)findViewById(R.id.single_player100);
        p10 = (MaterialEditText)findViewById(R.id.single_player10);
        p1 = (MaterialEditText)findViewById(R.id.single_player1);

        p100.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1) p10.requestFocus();
                else if(charSequence.length() == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(p100.getWindowToken(), 0);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        p10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1) p1.requestFocus();
                else if(charSequence.length() == 0) p100.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        p1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0) p10.requestFocus();
                else if(charSequence.length() == 1){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(p1.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        for(int i=0; i<comNum.length; i++){
            comNum[i] = rnd.nextInt(10);
            for(int j=0; j<i; j++){
                if(comNum[i] == comNum[j]) i--;
            }
        }

        Log.d("comNum", comNum[0] + " , " + comNum[1] + " , " + comNum[2]);
    }



    public void clickHit(View v){

        if(p100.getText().toString().length() > 1 ||
                p10.getText().toString().length() > 1 ||
                p1.getText().toString().length() > 1){
            Toast.makeText(this, "숫자는 한개씩만 넣어주세요!", Toast.LENGTH_SHORT).show();
            p1.setText("");
            p10.setText("");
            p100.setText("");
        }

        if(p100.getText().toString().equals("") || p100.getText().toString() == null ||
                p10.getText().toString().equals("") || p10.getText().toString() == null ||
                p1.getText().toString().equals("") || p1.getText().toString() == null) {
            Toast.makeText(this, "빈칸을 채워주세요!", Toast.LENGTH_SHORT).show();
            p1.setText("");
            p10.setText("");
            p100.setText("");
            return;
        }

        if(p1.getText().toString().equals(p10.getText().toString()) ||
                p1.getText().toString().equals(p100.getText().toString()) ||
                p10.getText().toString().equals(p100.getText().toString())){
            Toast.makeText(this, "중복되지 않은 숫자를 넣어주세요!", Toast.LENGTH_SHORT).show();
            p1.setText("");
            p10.setText("");
            p100.setText("");
            return;
        }

        int[] check = new int[3];

        check[0] = Integer.parseInt(p100.getText().toString());
        check[1] = Integer.parseInt(p10.getText().toString());
        check[2] = Integer.parseInt(p1.getText().toString());

        p1.setText("");
        p10.setText("");
        p100.setText("");

        int s = 0;
        int b = 0;
        int o = 0;

        for(int i=0; i<comNum.length; i++){
            if(comNum[i] == check[i]) s++;

            switch (i){
                case 0:
                    if(comNum[i] == check[1]) b++;
                    if(comNum[i] == check[2]) b++;
                    break;
                case 1:
                    if(comNum[i] == check[0]) b++;
                    if(comNum[i] == check[2]) b++;

                    break;
                case 2:
                    if(comNum[i] == check[0]) b++;
                    if(comNum[i] == check[1]) b++;
                    break;
            }
        }

        o = 3 - (s+b);

        Log.d("count", s + " : " + b + " : " + o);

        HitTheRound hit = new HitTheRound(s, b, o);
        hit.num = (check[0] * 100) + (check[1] * 10) + (check[2]);
        hitTheRounds.add(hit);
        adapter.notifyItemInserted(hitTheRounds.size()-1);
        recyclerView.scrollToPosition(hitTheRounds.size()-1);


        if(framePitcher.isRunning()) framePitcher.stop();
        framePitcher.start();

        if(s == 3){
            Intent intent = new Intent(this, GameOverActivity.class);
            intent.putExtra("HitTheRounds", hitTheRounds);
            intent.putExtra("Win", true);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else if(hitTheRounds.size()>9){
            Intent intent = new Intent(this, GameOverActivity.class);
            intent.putExtra("HitTheRounds", hitTheRounds);
            intent.putExtra("Win", false);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

    }
}
