package com.main.expandingrecyclerview;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.main.expandingrecyclerview.listners.ExpandCollapseListener;
import com.main.expandingrecyclerview.listners.GroupExpandCollapseListener;
import com.main.expandingrecyclerview.listners.OnGroupClickListener;
import com.main.expandingrecyclerview.models.ExpandableGroup;
import com.main.expandingrecyclerview.models.ExpandableList;
import com.main.expandingrecyclerview.models.ExpandableListPosition;
import com.main.expandingrecyclerview.viewholders.ChildViewHolder;
import com.main.expandingrecyclerview.viewholders.GroupViewHolder;

import java.util.List;

public abstract class ExpandableRecyclerViewAdapter<GVH extends GroupViewHolder,CVH extends ChildViewHolder>
extends RecyclerView.Adapter implements ExpandCollapseListener, OnGroupClickListener
{
    public static final String EXPAND_STATE_MAP = "expandable_recyclerview_adapter_expand_state_map";

    protected ExpandableList expandableList;
    private ExpandCollapseController expandCollapseController;

    private OnGroupClickListener groupClickListener;
    private GroupExpandCollapseListener expandCollapseListener;


    public ExpandableRecyclerViewAdapter(List<? extends ExpandableGroup> groups){
        this.expandableList = new ExpandableList(groups);
        this.expandCollapseController = new ExpandCollapseController(this, expandableList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case  ExpandableListPosition.GROUP:
                GVH gvh = onCreateGroupViewHolder(parent, viewType);
                gvh.setOnGroupClickListener(this);
                return gvh;
            case ExpandableListPosition.CHILD:
                CVH cvh = onCreateChildViewHolder(parent, viewType);
                return cvh;
            default:
                throw new IllegalArgumentException("viewType is not valid");

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(position);
        ExpandableGroup group = expandableList.getExpandableGroup(listPos);
        switch (listPos.type) {
            case ExpandableListPosition.GROUP:
                onBindGroupViewHolder((GVH) holder, position, group);

                if (isGroupExpanded(group)) {
                    ((GVH) holder).expand();
                } else {
                    ((GVH) holder).collapse();
                }
                break;
            case ExpandableListPosition.CHILD:
                onBindChildViewHolder((CVH) holder, position, group, listPos.childPos);
                break;
        }
    }
    @Override
    public int getItemCount() {
        return expandableList.getVisibleItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return expandableList.getUnflattenedPosition(position).type;
    }

    @Override
    public void onGroupExpanded(int positionStart, int itemCount) {
        //update header
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        // only insert if there items to insert
        if (itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
            if (expandCollapseListener != null) {
                int groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos;
                expandCollapseListener.onGroupExpanded(getGroups().get(groupIndex));
            }
        }
    }


    @Override
    public void onGroupCollapsed(int positionStart, int itemCount) {
        //update header
        int headerPosition = positionStart - 1;
        notifyItemChanged(headerPosition);

        // only remote if there items to remove
        if (itemCount > 0) {
            notifyItemRangeRemoved(positionStart, itemCount);
            if (expandCollapseListener != null) {
                //minus one to return the position of the header, not first child
                int groupIndex = expandableList.getUnflattenedPosition(positionStart - 1).groupPos;
                expandCollapseListener.onGroupCollapsed(getGroups().get(groupIndex));
            }
        }
    }

    @Override
    public boolean onGroupClick(int flatPos) {
        if (groupClickListener != null) {
            groupClickListener.onGroupClick(flatPos);
        }
        return expandCollapseController.toggleGroup(flatPos);
    }

    /**
     * @param flatPos The flat list position of the group
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    public boolean toggleGroup(int flatPos) {
        return expandCollapseController.toggleGroup(flatPos);
    }

    /**
     * @param group the {@link ExpandableGroup} being toggled
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    public boolean toggleGroup(ExpandableGroup group) {
        return expandCollapseController.toggleGroup(group);
    }

    /**
     * @param flatPos the flattened position of an item in the list
     * @return true if {@code group} is expanded, false if it is collapsed
     */
    public boolean isGroupExpanded(int flatPos) {
        return expandCollapseController.isGroupExpanded(flatPos);
    }


    public boolean isGroupExpanded(ExpandableGroup group) {
        return expandCollapseController.isGroupExpanded(group);
    }


    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBooleanArray(EXPAND_STATE_MAP, expandableList.expandedGroupIndexes);
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXPAND_STATE_MAP)) {
            return;
        }
        expandableList.expandedGroupIndexes = savedInstanceState.getBooleanArray(EXPAND_STATE_MAP);
        notifyDataSetChanged();
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        groupClickListener = listener;
    }

    public void setOnGroupExpandCollapseListener(GroupExpandCollapseListener listener) {
        expandCollapseListener = listener;
    }


    public List<? extends ExpandableGroup> getGroups() {
        return expandableList.groups;
    }

    public abstract GVH onCreateGroupViewHolder(ViewGroup parent, int viewType);


    public abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);


    public abstract void onBindChildViewHolder(CVH holder, int flatPosition, ExpandableGroup group,
                                               int childIndex);

    public abstract void onBindGroupViewHolder(GVH holder, int flatPosition, ExpandableGroup group);


}
