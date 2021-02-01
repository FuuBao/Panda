package com.example.panda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> implements OnMemoItemClickListener {

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
        itemViewHolder.subtext.setText(memo.getD());
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

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);

            maintext = itemView.findViewById(R.id.item_maintext);
            subtext = itemView.findViewById(R.id.item_subtext);

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