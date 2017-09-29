package com.kghy1234gmail.numbaseball;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class InputNumDialog extends AppCompatActivity {

    MaterialEditText m100, m10, m1;
    ImageView btnInput, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_input_num_dialog);

        setFinishOnTouchOutside(false);

        m100 = (MaterialEditText)findViewById(R.id.input_num_dialog_my100);
        m10 = (MaterialEditText)findViewById(R.id.input_num_dialog_my10);
        m1 = (MaterialEditText)findViewById(R.id.input_num_dialog_my1);

        m100.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1) m10.requestFocus();
                else if(charSequence.length() == 0) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(m100.getWindowToken(), 0);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        m10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 1) m1.requestFocus();
                else if(charSequence.length() == 0) m100.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        m1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0) m10.requestFocus();
                else if(charSequence.length() == 1){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(m1.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnExit = (ImageView)findViewById(R.id.input_num_dialog_btn_exit);
        btnInput = (ImageView)findViewById(R.id.input_num_dialog_btn_input);

        btnExit.setOnClickListener(btnListener);
        btnInput.setOnClickListener(btnListener);


    }



    ImageView.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.input_num_dialog_btn_input:

                    if(m100.getText().toString().length() > 1 ||
                            m10.getText().toString().length() > 1 ||
                            m1.getText().toString().length() > 1){
                        Toast.makeText(InputNumDialog.this, "숫자는 한개씩만 넣어주세요!", Toast.LENGTH_SHORT).show();
                        m1.setText("");
                        m10.setText("");
                        m100.setText("");
                    }

                    if(m100.getText().toString().equals("") || m100.getText().toString() == null ||
                            m10.getText().toString().equals("") || m10.getText().toString() == null ||
                            m1.getText().toString().equals("") || m1.getText().toString() == null) {
                        Toast.makeText(InputNumDialog.this, "빈칸을 채워주세요!", Toast.LENGTH_SHORT).show();
                        m1.setText("");
                        m10.setText("");
                        m100.setText("");
                        return;
                    }

                    if(m1.getText().toString().equals(m10.getText().toString()) ||
                            m1.getText().toString().equals(m100.getText().toString()) ||
                            m10.getText().toString().equals(m100.getText().toString())){
                        Toast.makeText(InputNumDialog.this, "중복되지 않은 숫자를 넣어주세요!", Toast.LENGTH_SHORT).show();
                        m1.setText("");
                        m10.setText("");
                        m100.setText("");
                        return;
                    }



                    getIntent().putExtra("m100",m100.getText().toString());
                    getIntent().putExtra("m10", m10.getText().toString());
                    getIntent().putExtra("m1", m1.getText().toString());

                    setResult(RESULT_OK, getIntent());
                    finish();

                    break;

                case R.id.input_num_dialog_btn_exit:

                    setResult(RESULT_CANCELED);
                    finish();
                    break;
            }

        }
    };
}
