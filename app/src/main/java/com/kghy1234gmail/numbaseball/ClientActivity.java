package com.kghy1234gmail.numbaseball;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.victor.loading.rotate.RotateLoading;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ClientActivity extends AppCompatActivity {

    public static final int REQ_ENABLE = 10;
    public static final int REQ_DISCOVERABLE = 20;
    public static final int DEVICE_SLECTED = 30;

    static final UUID BT_UUID= UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    BluetoothAdapter btAdapter;
    BluetoothServerSocket serverSocket;
    BluetoothSocket socket;
    DataInputStream dis;
    DataOutputStream dos;

    AnimationDrawable framePitcher;

    RotateLoading loading;
    TextView tvMyNum;

    boolean isConn = false;
    boolean isRdy = false;
    boolean isPlaying = false;

    MaterialEditText p100, p10, p1;

    RecyclerView mRecycler , eRecycler;
    SingleRecyclerAdapter mAdapter, eAdapter;


    ArrayList<HitTheRound> mHitTheRounds = new ArrayList<>();
    ArrayList<HitTheRound> eHitTheRounds = new ArrayList<>();

    ImageView serverPitcherImage;

    int[] mNum = new int[3];
    int[] eNum = new int[3];

    SocketThread socketThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_client);

        mRecycler = (RecyclerView)findViewById(R.id.client_recycler_view);
        eRecycler = (RecyclerView)findViewById(R.id.client_recycler_view_enemy);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new SingleRecyclerAdapter(this, mHitTheRounds);
        eAdapter = new SingleRecyclerAdapter(this, eHitTheRounds);
        mRecycler.setAdapter(mAdapter);
        eRecycler.setAdapter(eAdapter);

        tvMyNum = (TextView)findViewById(R.id.client_mynum);
        tvMyNum.setText("LOADING...");

        serverPitcherImage = (ImageView)findViewById(R.id.client_pitcher_img_view);
        framePitcher = (AnimationDrawable)serverPitcherImage.getDrawable();

        p100 = (MaterialEditText)findViewById(R.id.client_player100);
        p10 = (MaterialEditText)findViewById(R.id.client_player10);
        p1 = (MaterialEditText)findViewById(R.id.client_player1);

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

        loading = (RotateLoading)findViewById(R.id.client_loading);
        loading.start();

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null) finish();

        if(btAdapter.isEnabled()){

            discoveryBTDevice();

        }else{
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQ_ENABLE);
        }

    }

    public void clickHit(View v) {

        if (isRdy) {
            if (p100.getText().toString().length() > 1 ||
                    p10.getText().toString().length() > 1 ||
                    p1.getText().toString().length() > 1) {
                Toast.makeText(this, "숫자는 한개씩만 넣어주세요!", Toast.LENGTH_SHORT).show();
                p1.setText("");
                p10.setText("");
                p100.setText("");
            }

            if (p100.getText().toString().equals("") || p100.getText().toString() == null ||
                    p10.getText().toString().equals("") || p10.getText().toString() == null ||
                    p1.getText().toString().equals("") || p1.getText().toString() == null) {
                Toast.makeText(this, "빈칸을 채워주세요!", Toast.LENGTH_SHORT).show();
                p1.setText("");
                p10.setText("");
                p100.setText("");
                return;
            }

            if (p1.getText().toString().equals(p10.getText().toString()) ||
                    p1.getText().toString().equals(p100.getText().toString()) ||
                    p10.getText().toString().equals(p100.getText().toString())) {
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

            for (int i = 0; i < eNum.length; i++) {
                if (eNum[i] == check[i]) s++;

                switch (i) {
                    case 0:
                        if (eNum[i] == check[1]) b++;
                        if (eNum[i] == check[2]) b++;
                        break;
                    case 1:
                        if (eNum[i] == check[0]) b++;
                        if (eNum[i] == check[2]) b++;

                        break;
                    case 2:
                        if (eNum[i] == check[0]) b++;
                        if (eNum[i] == check[1]) b++;
                        break;
                }
            }

            o = 3 - (s + b);

            Log.d("count", s + " : " + b + " : " + o);

            final HitTheRound hit = new HitTheRound(s, b, o);
            hit.num = (check[0] * 100) + (check[1] * 10) + (check[2]);
            mHitTheRounds.add(hit);

            mAdapter.notifyItemInserted(mHitTheRounds.size() - 1);
            mRecycler.scrollToPosition(mHitTheRounds.size() - 1);

            if (framePitcher.isRunning()) framePitcher.stop();
            framePitcher.start();

            new Thread() {
                @Override
                public void run() {
                    final String msg = "HitTheRound:" + hit.num + "," + hit.strikes + "," + hit.balls + "," + hit.outs;
                    isRdy = false;
                    try {
                        dos.writeUTF(msg);
                        dos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            if(s==3){
                new Thread(){
                    @Override
                    public void run() {
                        String msg = "gameover";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvMyNum.setText("승리");
                            }
                        });
                        isPlaying = false;
                        try {
                            dos.writeUTF(msg);
                            dos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

        }else{
            Toast.makeText(ClientActivity.this, "순서를 기다려 주세요!", Toast.LENGTH_SHORT).show();
        }


    }

    void showInputNumDialog(){
        Intent intent = new Intent(this, InputNumDialog.class);
        startActivityForResult(intent, 25);
    }


    void discoveryBTDevice(){
        //블루투스 장치를 탐색하여 그 결과를 리스트로 보여주는 Activity실행
        Intent intent = new Intent(this, BTDeviceListActivity.class);
        startActivityForResult(intent, DEVICE_SLECTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_ENABLE:

                if(resultCode != RESULT_CANCELED){
                    discoveryBTDevice();
                }else{
                    finish();
                }
                break;

            case DEVICE_SLECTED:
                if(resultCode == RESULT_OK){
                    String addr = data.getStringExtra("addr");
                    Log.e("addr", addr);

                    socketThread = new SocketThread(addr);
                    showInputNumDialog();
                }

                break;

            case 25:
                if(resultCode == RESULT_CANCELED) finish();

                mNum[0] = Integer.parseInt(data.getStringExtra("m100"));
                mNum[1] = Integer.parseInt(data.getStringExtra("m10"));
                mNum[2] = Integer.parseInt(data.getStringExtra("m1"));

                isRdy = true;

                loading.stop();
                loading.setVisibility(View.GONE);

                tvMyNum.setText("내 숫자 : " + ((mNum[0]* 100) + (mNum[1] * 10) + mNum[2]));

                socketThread.start();

                break;
        }
    }

    class SocketThread extends Thread{

        String addr;
        BluetoothDevice device;

        SocketThread(String addr){
            this.addr = addr;

            device = btAdapter.getRemoteDevice(addr);
        }

        @Override
        public void run() {

            try {
                socket = device.createInsecureRfcommSocketToServiceRecord(BT_UUID);
                socket.connect();

                isPlaying = true;

                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());

                dos.writeUTF("num:" + mNum[0] +","+ mNum[1] + ","+mNum[2]);
                dos.flush();


                while (isPlaying){
                    String msg = dis.readUTF();
                    msgSet(msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    void msgSet(String msg){

        if(msg.contains("num:")){
            msg = msg.replace("num:","");
            String[] splits = msg.split(",");
            eNum[0] = Integer.parseInt(splits[0]);
            eNum[1] = Integer.parseInt(splits[1]);
            eNum[2] = Integer.parseInt(splits[2]);
        }else if(msg.contains("HitTheRound:")){
//            final String msg = "HitTheRound:" + hit.num +"," + hit.strikes + "," + hit.balls + "," + hit.outs;
            msg = msg.replace("HitTheRound:", "");
            String[] splits = msg.split(",");
            HitTheRound hit = new HitTheRound(Integer.parseInt(splits[1]), Integer.parseInt(splits[2]),Integer.parseInt(splits[3]));
            hit.num = Integer.parseInt(splits[0]);

            isRdy = true;

            eHitTheRounds.add(hit);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    eAdapter.notifyItemInserted(eHitTheRounds.size()-1);
                    eRecycler.scrollToPosition(eHitTheRounds.size()-1);
                }
            });
        }else if(msg.equals("gameover")){
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   tvMyNum.setText("패배");
               }
           });
        }

    }
}
