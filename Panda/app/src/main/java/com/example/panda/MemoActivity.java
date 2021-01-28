package com.example.panda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private int count; //item의 수

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild; //로그 체크용

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private String Id;

    private ImageButton setting;

    private List<Memo> memoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        setting = findViewById(R.id.setting);
        int color = ContextCompat.getColor(this, R.color.green);
        setting.setColorFilter(color);

        initDatabase();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Intent intent=getIntent();
        Id=intent.getExtras().getString("Id"); //로그인한 id 받아오기

        recyclerView = findViewById(R.id.recyclerview);

        memoList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MemoActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new RecyclerAdapter(memoList);
        recyclerView.setAdapter(recyclerAdapter);
        //btnAdd = findViewById(R.id.btnAdd);

        myRef = database.getReference().child(Id); // 로그인 시 표시되는 recyclerview - db에서 불러오기
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recyclerAdapter.clear();

                for (DataSnapshot memoData : dataSnapshot.getChildren()) {
                    //String t=memoData.
                    Memo getMemo = memoData.getValue(Memo.class);
                    Memo pushMemo = new Memo(getMemo.getTitle(), getMemo.getD());
                    recyclerAdapter.addItem(pushMemo);
                    // child 내에 있는 데이터만큼 반복합니다.
                }
                recyclerAdapter.notifyDataSetChanged();
                count=recyclerAdapter.getItemCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new FABClickListener());

        //item 클릭 이벤트
        recyclerAdapter.setOnItemClicklistener(new OnMemoItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapter.ItemViewHolder holder, View view, int position) {
                //암호설정여부확인
                //kjkjkjk

                Memo memo=recyclerAdapter.getItem(position);
                Intent intent=new Intent(getApplicationContext(), ItemClickActivity.class);
                intent.putExtra("Id", Id);
                intent.putExtra("pos", Integer.toString(position));
                intent.putExtra("oldTitle", memo.getTitle());
                intent.putExtra("oldContents", memo.getContents());
                intent.putExtra("ischecked", memo.getPw());

                startActivity(intent);
            }
        });

        /*
        recyclerAdapter.setOnItemLongClicklistener(new OnMemoItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerAdapter.ItemViewHolder holder, View view, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);

                builder.setTitle("비밀번호를 입력하세요.");
                builder.setView(R.layout.pwdialog);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Dialog f = (Dialog) dialog;
                        EditText input = (EditText) f.findViewById(R.id.pwdialog);
                        String value = input.getText().toString(); //value.toString(); helper_category.insert(value);
                        // Do something with value!


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        }
                });
                AlertDialog dialog = builder.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                builder.show();
            }
        });
        */

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MemoActivity.this, SettingActivity.class);
                intent.putExtra("Id", Id);
                startActivity(intent);
            }
        });
    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

    //writeactivity에서 저장버튼 클릭이벤트
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if(data.getStringExtra("title") == null || data.getStringExtra("contents") ==null)
                Toast.makeText(getApplicationContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();

            String title = data.getStringExtra("title");
            String contents=data.getStringExtra("contents");
            Boolean ischecked=data.getBooleanExtra("ischecked", false); //기본값은 false
            String strSub = data.getStringExtra("sub");

            myRef = database.getReference();
            Memo memo = new Memo(title, contents, ischecked, strSub); //db엔 내용까지 저장
            myRef.child(Id).child(Integer.toString(count)).setValue(memo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    myRef.child(Id).child("count").setValue(count);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            Memo memo1 = new Memo(title, strSub);
            recyclerAdapter.addItem(memo1);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements OnMemoItemClickListener {

        private List<Memo> listdata;
        OnMemoItemClickListener listener;

        public RecyclerAdapter(List<Memo> listdata) {
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
            return new ItemViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public void setOnItemClicklistener(OnMemoItemClickListener listener){ this.listener = listener; }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
            Memo memo = listdata.get(i);

            itemViewHolder.maintext.setText(memo.getTitle());
            //itemViewHolder.subtext.setText(memo.getSubtext());

            /*if(memo.getIsdone()==0){
                itemViewHolder.img.setBackgroundColor(Color.LTGRAY);
            }else{
                itemViewHolder.img.setBackgroundColor(Color.GREEN);
            }*/

        }

        void clear() { listdata.clear(); }

        void addItem(Memo memo){
            listdata.add(memo);
        }

        public Memo getItem(int position){ return listdata.get(position); }

        void removeItem(int position){
            listdata.remove(position);
        }

        @Override
        public void onItemClick(ItemViewHolder holder, View view, int position) {
            if(listener != null){ listener.onItemClick(holder,view,position); }
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{

            private TextView maintext;
            private TextView subtext;
            private ImageView img;

            public ItemViewHolder(@NonNull View itemView){
                super(itemView);

                maintext = itemView.findViewById(R.id.item_maintext);
                subtext = itemView.findViewById(R.id.item_subtext);
                img = itemView.findViewById(R.id.item_image);

                //아이템 클릭 이벤트
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=getAdapterPosition();
                        if(listener!=null) listener.onItemClick(ItemViewHolder.this, v, position);
                    }
                });
            }
        }
    }

    private class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // FAB Click 이벤트 처리 구간
            Intent intent = new Intent(MemoActivity.this, WriteActivity.class);
            startActivityForResult(intent, 0);
        }
    }

}