package com.kghy1234gmail.numbaseball;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BTDeviceListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter adapter;

    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> devices;

    DiscoveryReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btdevice_list);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            int permission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if(permission == PackageManager.PERMISSION_DENIED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            }
        }

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        devices = btAdapter.getBondedDevices();

        for(BluetoothDevice d : devices){
            String name = d.getName();
            String addr = d.getAddress();   //이것이 소켓 통신에서 중요!!
            items.add(name +"\n" + addr);
        }

        listView = (ListView)findViewById(R.id.listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setSelection(items.size()-1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = items.get(i);
                String[] strs = str.split("\n");
                String addr = strs[1];

                getIntent().putExtra("addr", addr);

                setResult(RESULT_OK, getIntent());
                finish();
            }
        });

        //탐색하기 전에 탐색결과 방송을 듣는 리스너를 등록
        receiver = new DiscoveryReceiver();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);

        btAdapter.startDiscovery();

        setFinishOnTouchOutside(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 10:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this,"주변 기기를 검색할수 없기에 사용을 종료합니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null){
            if(btAdapter.isDiscovering()) btAdapter.cancelDiscovery();
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    class DiscoveryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();



            if(action.equals(BluetoothDevice.ACTION_FOUND)){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String addr = device.getAddress();


                items.add(name + "\n" + addr);
                adapter.notifyDataSetChanged();


            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                Toast.makeText(context, "탐색이 종료되었습니다.", Toast.LENGTH_SHORT).show();
            }

//            switch (action){
//                case BluetoothDevice.ACTION_FOUND:
//
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    String name = device.getName();
//                    String addr = device.getAddress();
//
//                    items.add(name + "\n" + addr);
//                    adapter.notifyDataSetChanged();
//
//                    break;
//                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
//                    break;
//            }

        }
    }
}
