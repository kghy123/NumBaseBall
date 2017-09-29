package com.kghy1234gmail.numbaseball;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class BlueToothSelectDialog extends Dialog {

    ImageView btnCreateRoom, btnConnect, btnExit;
    Context context;


    public BlueToothSelectDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public BlueToothSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected BlueToothSelectDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bluetooth_dialog);

        btnConnect = (ImageView)findViewById(R.id.bluetooth_dialog_connect);
        btnCreateRoom = (ImageView)findViewById(R.id.bluetooth_dialog_create_room);
        btnExit = (ImageView)findViewById(R.id.bluetooth_dialog_exit);

        btnConnect.setOnClickListener(listener);
        btnCreateRoom.setOnClickListener(listener);
        btnExit.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.bluetooth_dialog_create_room:

                    Intent intent = new Intent(context, ServerActivity.class);
                    context.startActivity(intent);
                    dismiss();

                    break;

                case R.id.bluetooth_dialog_connect:

                    Intent intent2 = new Intent(context, ClientActivity.class);
                    context.startActivity(intent2);
                    dismiss();

                    break;

                case R.id.bluetooth_dialog_exit:
                    dismiss();
                    break;
            }

        }
    };
}
