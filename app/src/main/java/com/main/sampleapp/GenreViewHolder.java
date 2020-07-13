package com.main.sampleapp;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.main.expandingrecyclerview.models.ExpandableGroup;
import com.main.expandingrecyclerview.viewholders.GroupViewHolder;

import org.w3c.dom.Text;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class GenreViewHolder extends GroupViewHolder {

    private TextView genreName;
    private ImageView arrow;
    private ImageView icon;

    public GenreViewHolder(@NonNull View itemView) {
        super(itemView);
        genreName = (TextView) itemView.findViewById(R.id.list_item_genre_name);
        arrow = (ImageView) itemView.findViewById(R.id.list_item_genre_arrow);
        icon = (ImageView) itemView.findViewById(R.id.list_item_genre_icon);
    }
    public void setGenreTitle(ExpandableGroup genre) {
        if (genre instanceof Genre) {
            genreName.setText(genre.getTitle());
            icon.setBackgroundResource(((Genre) genre).getIconResId());
        }

    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
