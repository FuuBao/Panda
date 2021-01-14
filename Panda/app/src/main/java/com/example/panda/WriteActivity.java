package com.example.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WriteActivity extends AppCompatActivity {
    private EditText mTitleEditText;
    private EditText mContentsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        mTitleEditText = findViewById(R.id.title_edit);
        mContentsEditText = findViewById(R.id.contents_edit);
    }

    @Override
    public void onBackPressed() {
        String title = mTitleEditText.getText().toString();
        String contents = mContentsEditText.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_CONTENTS, contents);

        SQLiteDatabase db = MemoDBHelper.getInstance(this).getWritableDatabase();
        long newRowId = db.insert(MemoContract.MemoEntry.TABLE_NAME,
                null,
                contentValues);

        if(newRowId == -1){
            Toast.makeText(this, "저장에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(this, "메모가 저장되었습니다", Toast.LENGTH_SHORT).show();
        }

        super.onBackPressed();
    }

    /*private EditText mMemoEdit = null;
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
    }*/
}

