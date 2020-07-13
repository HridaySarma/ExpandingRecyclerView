package com.main.expandingrecyclerview;

import com.main.expandingrecyclerview.listners.ExpandCollapseListener;
import com.main.expandingrecyclerview.models.ExpandableGroup;
import com.main.expandingrecyclerview.models.ExpandableList;
import com.main.expandingrecyclerview.models.ExpandableListPosition;

public class ExpandCollapseController {
    private ExpandCollapseListener listener;
    private ExpandableList expandableList;

    public ExpandCollapseController(ExpandCollapseListener listener, ExpandableList expandableList) {
        this.listener = listener;
        this.expandableList = expandableList;
    }

    private void collapseGroup(ExpandableListPosition listPosition){
        expandableList.expandedGroupIndexes[listPosition.groupPos] = false;
        if (listener != null){
            listener.onGroupCollapsed(expandableList.getFlattenedGroupIndex(listPosition) +1,
                    expandableList.groups.get(listPosition.groupPos).getItemCount());
        }
    }

    private void expandGrouup(ExpandableListPosition listPosition){
        expandableList.expandedGroupIndexes[listPosition.groupPos] = true;
        if (listener != null){
            listener.onGroupExpanded(expandableList.getFlattenedGroupIndex(listPosition) +1
            ,expandableList.groups.get(listPosition.groupPos).getItemCount());
        }
    }

    public boolean isGroupExpanded(ExpandableGroup group){
        int groupIndex = expandableList.groups.indexOf(group);
        return expandableList.expandedGroupIndexes[groupIndex];
    }

    public boolean isGroupExpanded(int flatPos){
        ExpandableListPosition listPosition = expandableList.getUnflattenedPosition(flatPos);
        return expandableList.expandedGroupIndexes[listPosition.groupPos];
    }

    public boolean toggleGroup(int flatPos){
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(flatPos);
        boolean expanded = expandableList.expandedGroupIndexes[listPos.groupPos];
        if (expanded){
            collapseGroup(listPos);
        }else {
            expandGrouup(listPos);
        }
        return expanded;
    }

    public boolean toggleGroup(ExpandableGroup group){
        ExpandableListPosition listPos = expandableList.getUnflattenedPosition(expandableList.getFlattenedGroupIndex(group));
        boolean expanded = expandableList.expandedGroupIndexes[listPos.groupPos];
        if (expanded){
            collapseGroup(listPos);
        }else {
            collapseGroup(listPos);
        }
        return expanded;
    }

}

