package com.example.panda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ItemClickActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private EditText mTitleEditText;
    private EditText mContentsEditText;
    private ImageButton button2;
    private ImageButton button3;
    private ImageButton button4;
    private ImageButton button5;
    private CheckBox ispw;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private String oldTitle;
    private String oldContents;
    private Boolean oldischecked;

    private String titletext;
    private String title;
    private String contents;
    private Boolean ischecked;
    private String Id;
    private String position;
    private int pos;
    private int newPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Intent intent=getIntent();
        Id=intent.getExtras().getString("Id"); //로그인한 id 받아오기
        position=intent.getExtras().getString("pos"); //선택한 위치 받아오기
        pos=Integer.parseInt(position);
        oldTitle=intent.getExtras().getString("oldTitle"); //원래 제목
        oldContents=intent.getExtras().getString("oldContents"); //원래 내용
        oldischecked=intent.getExtras().getBoolean("ischecked"); //원래 암호여부 체

        mTitleEditText = (EditText) findViewById(R.id.title_edit);
        mContentsEditText = (EditText) findViewById(R.id.contents_edit);
        ispw=(CheckBox)findViewById(R.id.ispw);

        mTitleEditText.setText(oldTitle);
        mContentsEditText.setText(oldContents);
        ispw.setChecked(oldischecked);//원래 메모 보이게

        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        int color = ContextCompat.getColor(this, R.color.green);
        button2.setColorFilter(color);
        button3.setColorFilter(color);
        button4.setColorFilter(color);
        button5.setColorFilter(color);
        //PNG 색상 지정

        button2.setOnClickListener(new View.OnClickListener() { //알림
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DateActivity.class);
                titletext = mTitleEditText.getText().toString();
                intent.putExtra("titletext", titletext);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() { //음성인식
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() { //저장(업데이트)
            @Override
            public void onClick(View v) {
                title=mTitleEditText.getText().toString();
                contents=mContentsEditText.getText().toString();
                ischecked=ispw.isChecked();
                if(title.length()>0 && contents.length()>0){
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String substr = sdf.format(date);

                    Memo memo=new Memo(title, contents, ischecked, substr);
                    myRef.child(Id).child("memo").child(position).setValue(memo);
                }
            finish();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {//삭제(db에서 지우기)
            @Override
            public void onClick(View v) {
                myRef.child(Id).child("memo").child(position).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myRef.child(Id).child("memo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot memoData : snapshot.getChildren()) {
                                    newPos=Integer.parseInt(memoData.getKey());
                                    Memo getMemo = memoData.getValue(Memo.class);
                                    // child 내에 있는 데이터만큼 반복합니다.
                                    if(newPos>pos) {
                                        myRef.child(Id).child("memo").child(Integer.toString(newPos-1)).setValue(getMemo);
                                    }
                                    newPos=Integer.parseInt(memoData.getKey());
                                }
                                if(newPos!=(pos-1))
                                    myRef.child(Id).child("memo").child(Integer.toString(newPos)).removeValue();
                            } //0, 1, 2, 3에 저장됐을 때 3을 삭제. 그럼 pos=3이고 newPos는 2까지감
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                        }
                });
                Toast.makeText(getApplicationContext(),
                        "삭제되었습니다.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "메모할 말을 말씀하세요");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "죄송합니다, 당신의 기기는 음성녹음을 지원하지 않습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index;

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    View view=getCurrentFocus();
                    if(view instanceof EditText && view==mTitleEditText) {
                        index = mTitleEditText.getSelectionStart();
                        mTitleEditText.setText(mTitleEditText.getText().subSequence(0, index) + result.get(0) + mTitleEditText.getText().subSequence(index, mTitleEditText.length()));
                    }
                    else if(view instanceof EditText && view==mContentsEditText){
                        index = mContentsEditText.getSelectionStart();
                        mContentsEditText.setText(mContentsEditText.getText().subSequence(0, index) + result.get(0) + mContentsEditText.getText().subSequence(index, mContentsEditText.length()));
                    }

                    //추가
                }
                break;
            }

        }
    }
}

