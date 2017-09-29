package com.kghy1234gmail.numbaseball;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
    }

    public void clickSingle(View v){
        Intent intent = new Intent(this, SingleGameActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public void clickMulti(View v){

        BlueToothSelectDialog dialog = new BlueToothSelectDialog(this);
        dialog.show();
    }

    public void clickExit(View v){
        finishAndRemoveTask();
        overridePendingTransition(0, android.R.anim.fade_out);
    }

}
