package com.example.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        editText = findViewById(R.id.edtMemo);
        editText = findViewById(R.id.edtContent);


        findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();

                if(str.length()>0){
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    String substr = sdf.format(date);

                    Intent intent = new Intent();
                    intent.putExtra("main", str);
                    intent.putExtra("sub",substr);
                    setResult(RESULT_OK, intent);

                    finish();

                    Toast.makeText(WriteActivity.this, str+","+substr, Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

