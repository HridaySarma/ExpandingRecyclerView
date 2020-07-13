package com.main.sampleapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.main.expandingrecyclerview.viewholders.ChildViewHolder;

public class ArtistViewHolder extends ChildViewHolder {

    private TextView childTextView;

    public ArtistViewHolder(@NonNull View itemView) {
        super(itemView);
        childTextView = itemView.findViewById(R.id.list_item_artist_name);
    }
    public void setArtistName(String name) {
        childTextView.setText(name);
    }
}
