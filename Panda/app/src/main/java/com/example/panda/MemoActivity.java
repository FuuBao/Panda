package com.example.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
public class MemoActivity extends AppCompatActivity {
    RecyclerView rv;
    RecyclerAdapter adapter;
    ItemTouchHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        rv = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new RecyclerAdapter();
        rv.setAdapter(adapter);
        helper = new ItemTouchHelper(new ItemTouchHelperCallback(adapter));
        helper.attachToRecyclerView(rv);
        /*Person person1 = new Person(R.drawable.image1,"파이리",1);
        Person person2 = new Person(R.drawable.image2,"피카츄",2);
        Person person3 = new Person(R.drawable.image3,"꼬부기",3);
        Person person4 = new Person(R.drawable.image4,"이상해씨",4);
        adapter.addItem(person1);
        adapter.addItem(person2);
        adapter.addItem(person3);
        adapter.addItem(person4);

         */

        Button button=(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WriteActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

