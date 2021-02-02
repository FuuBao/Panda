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

public class PwEditActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private EditText currentPassword;
    private EditText newPw;
    private EditText newPwconfirm;
    private Button edit;
    private Button cancel;

    private String Id;
    private String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw_edit);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        Intent intent=getIntent();
        Id=intent.getExtras().getString("Id");
        pw=intent.getExtras().getString("pw");

        currentPassword=findViewById(R.id.currentPassword);
        newPw=findViewById(R.id.newPw);
        newPwconfirm=findViewById(R.id.newPwconfirm);
        edit=findViewById(R.id.edit);
        cancel=findViewById(R.id.cancel);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pw.equals(currentPassword.getText().toString()) && newPw.getText().toString().equals(newPwconfirm.getText().toString())) {
                    myRef.child(Id).child("pw").child("pw").setValue(newPw.getText().toString());
                    Toast.makeText(getApplicationContext(), "암호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(pw.equals(currentPassword.getText().toString())==false && newPw.getText().toString().equals(newPwconfirm.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "현재 암호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(pw.equals(currentPassword.getText().toString()) && newPw.getText().toString().equals(newPwconfirm.getText().toString())==false) {
                    Toast.makeText(getApplicationContext(), "새 암호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), "다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}