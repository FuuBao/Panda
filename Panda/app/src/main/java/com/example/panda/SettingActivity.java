package com.example.panda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button pwset;
    private Button pwchange;
    private Button pwdelete;
    private String Id;
    private String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        Intent intent=getIntent();
        Id=intent.getExtras().getString("Id");

        pw=null;
        myRef.child(Id).child("pw").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    pw=ds.getValue(String.class); //저장해놓은 암호 불러오기
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {  }
        });

        pwset=findViewById(R.id.pwset);
        pwchange=findViewById(R.id.pwchange);
        pwdelete=findViewById(R.id.pwdelete); //암호 설정, 변경, 삭제 버튼

        pwset.setOnClickListener(new View.OnClickListener() { //암호 없을 때, 암호 설정
            @Override
            public void onClick(View v) {
                if(pw==null) {
                    Intent intent=new Intent(SettingActivity.this, MakePwActivity.class);
                    intent.putExtra("Id", Id);
                    startActivity(intent);
                }
                else Toast.makeText(getApplicationContext(), "설정된 암호가 이미 존재합니다.", Toast.LENGTH_SHORT).show();
            }
        });

        pwchange.setOnClickListener(new View.OnClickListener() { //암호 있을 때, 암호 변경
            @Override
            public void onClick(View v) {
                if(pw!=null) {
                    Intent intent=new Intent(SettingActivity.this, PwEditActivity.class);
                    intent.putExtra("Id", Id);
                    intent.putExtra("pw", pw);
                    startActivity(intent);
                }
                else Toast.makeText(getApplicationContext(), "암호를 먼저 설정해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        pwdelete.setOnClickListener(new View.OnClickListener() { //암호 있을 때, 암호 삭제
            @Override
            public void onClick(View v) {
                if(pw!=null) {
                    Intent intent=new Intent(SettingActivity.this, PwDeleteActivity.class);
                    intent.putExtra("Id", Id);
                    intent.putExtra("pw", pw);
                    startActivity(intent);
                }
                else Toast.makeText(getApplicationContext(), "암호를 먼저 설정해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}