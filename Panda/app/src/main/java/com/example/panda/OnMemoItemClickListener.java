package com.example.panda;

import android.view.View;

public interface OnMemoItemClickListener {
    public void onItemClick(MemoActivity.RecyclerAdapter.ItemViewHolder holder, View view, int position);
}
