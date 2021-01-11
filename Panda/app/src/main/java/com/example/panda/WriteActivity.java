package com.example.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WriteActivity extends AppCompatActivity {


    private EditText mMemoEdit = null;
    write mTextFileManager = new write(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mMemoEdit = (EditText) findViewById(R.id.textView);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            // 1. 에디트텍스트에 입력된 메모를 텍스트 파일로 저장하기
            case R.id.button2: {
                String memoData = mMemoEdit.getText().toString();
                mTextFileManager.save(memoData);
                mMemoEdit.setText("");

                Toast.makeText(this, "저장 완료", Toast.LENGTH_LONG).show();
                break;
            }

            // 2. 저장된 메모 파일 삭제하기
            case R.id.button3: {
                mTextFileManager.delete();
                mMemoEdit.setText("");

                Toast.makeText(this, "삭제 완료", Toast.LENGTH_LONG).show();
            }
        }
    }
}

