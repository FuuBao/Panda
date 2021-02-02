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

public class MakePwActivity extends AppCompatActivity {
    private EditText newPw;
    private EditText newPwconfirm;
    private Button make_button;
    private Button cancel_button;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private String Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_pw);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        newPw=(findViewById(R.id.newPw));
        newPwconfirm=(findViewById(R.id.newPwconfirm));
        make_button=(findViewById(R.id.make_button));
        cancel_button=(findViewById(R.id.cancel_button));

        Intent intent=getIntent();
        Id=intent.getExtras().getString("Id");

        make_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPw.getText().toString().equals(newPwconfirm.getText().toString())) {
                    myRef.child(Id).child("pw").child("pw").setValue(newPw.getText().toString());
                    Toast.makeText(getApplicationContext(), "암호를 설정했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "암호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}