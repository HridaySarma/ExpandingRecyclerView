package com.main.expandingrecyclerview.viewholders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.expandingrecyclerview.listners.OnGroupClickListener;

public class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnGroupClickListener listener;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onGroupClick(getAdapterPosition());
        }
    }

    public void setOnGroupClickListener(OnGroupClickListener listener){
        this.listener = listener;
    }

    public void expand(){}

    public void collapse(){}
}
