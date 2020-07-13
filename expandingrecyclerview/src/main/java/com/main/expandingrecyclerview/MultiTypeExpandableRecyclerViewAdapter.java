package com.main.expandingrecyclerview;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.main.expandingrecyclerview.models.ExpandableGroup;
import com.main.expandingrecyclerview.models.ExpandableListPosition;
import com.main.expandingrecyclerview.viewholders.ChildViewHolder;
import com.main.expandingrecyclerview.viewholders.GroupViewHolder;

import java.util.List;

public abstract class MultiTypeExpandableRecyclerViewAdapter<GVH extends GroupViewHolder, CVH extends ChildViewHolder>
        extends ExpandableRecyclerViewAdapter<GVH, CVH> {

    public MultiTypeExpandableRecyclerViewAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isGroup(viewType)) {
            GVH gvh = onCreateGroupViewHolder(parent, viewType);
            gvh.setOnGroupClickListener(this);
            return gvh;
        } else if (isChild(viewType)) {
            CVH cvh = onCreateChildViewHolder(parent, viewType);
            return cvh;
        }
        throw new IllegalArgumentException("viewType is not valid");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(position);
        ExpandableGroup group = expandableList.getExpandableGroup(listPos);
        if (isGroup(getItemViewType(position))) {
            onBindGroupViewHolder((GVH) holder, position, group);

            if (isGroupExpanded(group)) {
                ((GVH) holder).expand();
            } else {
                ((GVH) holder).collapse();
            }
        } else if (isChild(getItemViewType(position))) {
            onBindChildViewHolder((CVH) holder, position, group, listPos.childPos);
        }
    }


    @Override
    public int getItemViewType(int position) {
        ExpandableListPosition listPosition = expandableList.getUnflattenedPosition(position);
        ExpandableGroup group = expandableList.getExpandableGroup(listPosition);

        int viewType = listPosition.type;
        switch (viewType) {
            case ExpandableListPosition.GROUP:
                return getGroupViewType(position, group);
            case ExpandableListPosition.CHILD:
                return getChildViewType(position, group, listPosition.childPos);
            default:
                return viewType;
        }
    }


    public int getChildViewType(int position, ExpandableGroup group, int childIndex) {
        return super.getItemViewType(position);
    }


    public int getGroupViewType(int position, ExpandableGroup group) {
        return super.getItemViewType(position);
    }


    public boolean isGroup(int viewType) {
        return viewType == ExpandableListPosition.GROUP;
    }


    public boolean isChild(int viewType) {
        return viewType == ExpandableListPosition.CHILD;
    }
}