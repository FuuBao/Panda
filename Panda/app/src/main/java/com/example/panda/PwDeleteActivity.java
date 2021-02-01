package com.example.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PwDeleteActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private EditText currentPw;
    private EditText currentPwconfirm;
    private Button but_delete;
    private Button but_cancel;
    private String Id;
    private String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_delete);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        Intent intent=getIntent();
        Id=intent.getExtras().getString("Id");
        pw=intent.getExtras().getString("pw");

        currentPw=findViewById(R.id.currentPw);
        currentPwconfirm=findViewById(R.id.currentPwconfirm);
        but_delete=findViewById(R.id.but_delete);
        but_cancel=findViewById(R.id.but_cancel);

        but_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPw.getText().toString().equals(currentPwconfirm.getText().toString()) && currentPw.getText().toString().equals(pw)) {
                    myRef.child(Id).child("pw").removeValue();
                    Toast.makeText(getApplicationContext(), "암호가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else Toast.makeText(getApplicationContext(), "암호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}