package com.example.panda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup; import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements ItemTouchHelperListener{ArrayList<MemoList> items = new ArrayList<>();
        public RecyclerAdapter(){}
        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.memoitem, parent, false);
            return new ItemViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }


    @Override
    public int getItemCount() { return items.size();}
    public void addItem(MemoList memo){items.add(memo);}
    @Override
    public boolean onItemMove(int from_position, int to_position)
    { MemoList memo = items.get(from_position);
    items.remove(from_position);
    items.add(to_position,memo);
    notifyItemMoved(from_position,to_position);
    return true;}
    @Override
    public void onItemSwipe(int position)
    {items.remove(position);notifyItemRemoved(position);}
    class ItemViewHolder extends RecyclerView.ViewHolder
    {TextView list_textview1, list_textview2;
    public ItemViewHolder(View itemView)
    { super(itemView);
    list_textview1 = itemView.findViewById(R.id.textView1);
    list_textview2 = itemView.findViewById(R.id.textView2);
    }
    public void onBind(MemoList memo)
    {list_textview1.setText(memo.getText1());
    list_textview2.setText(String.valueOf(memo.getText2()));
    }} }
