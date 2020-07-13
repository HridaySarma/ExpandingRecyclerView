package com.main.expandingrecyclerview.models;

import java.util.List;

public class ExpandableList {
    public List<? extends ExpandableGroup> groups;
    public boolean[] expandedGroupIndexes;

    public ExpandableList(List<? extends ExpandableGroup> groups) {
        this.groups = groups;

        expandedGroupIndexes = new boolean[groups.size()];
        for (int i=0;i<groups.size();i++){
            expandedGroupIndexes[i] = false;
        }
    }

    private int numberOfVisibleItemsInGroup(int group){
        if (expandedGroupIndexes[group]){
            return groups.get(group).getItemCount() +1;
        }else {
            return 1;
        }
    }

    public int getVisibleItemCount(){
        int count = 0;
        for (int i=0; i<groups.size(); i++){
            count += numberOfVisibleItemsInGroup(i);
        }
        return count;
    }

    public ExpandableListPosition getUnflattenedPosition(int flPos){
        int groupItemCount;
        int adapted = flPos;
        for (int i = 0; i < groups.size() ; i++){
            groupItemCount=  numberOfVisibleItemsInGroup(i);
            if (adapted == 0){
                return ExpandableListPosition.obtain(ExpandableListPosition.GROUP,i,-1,flPos);
            }else if (adapted < groupItemCount){
                return ExpandableListPosition.obtain(ExpandableListPosition.CHILD,i,adapted-1,flPos);
            }
            adapted -= groupItemCount;
        }
        throw new RuntimeException("Unknown state");
    }

    public  int getFlattenedGroupIndex(ExpandableListPosition listPosition){
        int groupIndex = listPosition.groupPos;
        int runningTotal = 0;

        for (int i=0; i < groupIndex; i++){
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal;
    }

    public int getFlattenedGroupIndex(int groupIndex){
        int runningTotal = 0;

        for (int i = 0; i< groupIndex;i++){
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal;
    }

    public  int getFlattenedGroupIndex(ExpandableGroup group){
        int groupIndex = groups.indexOf(group);
        int runningTotal = 0;

        for (int i = 0; i < groupIndex; i++){
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal;
    }

    public  int getFlattenedChildIndex(long packedPosition){
        ExpandableListPosition listPosition = ExpandableListPosition.obtainPosition(packedPosition);
        return getFlattenedChildIndex(listPosition);
    }

    public int getFlattenedChildIndex(ExpandableListPosition listPosition){
        int groupIndex = listPosition.groupPos;
        int childIndex = listPosition.childPos;
        int runningTotal = 0;

        for (int i = 0; i< groupIndex; i++){
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal + childIndex + 1;
    }

    public int getFlattenedChildIndex(int groupIndex, int childIndex) {
        int runningTotal = 0;

        for (int i = 0; i < groupIndex; i++) {
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal + childIndex + 1;
    }


    public int getFlattenedFirstChildIndex(int groupIndex) {
        return getFlattenedGroupIndex(groupIndex) + 1;
    }


    public int getFlattenedFirstChildIndex(ExpandableListPosition listPosition) {
        return getFlattenedGroupIndex(listPosition) + 1;
    }


    public int getExpandableGroupItemCount(ExpandableListPosition listPosition) {
        return groups.get(listPosition.groupPos).getItemCount();
    }


    public ExpandableGroup getExpandableGroup(ExpandableListPosition listPosition) {
        return groups.get(listPosition.groupPos);
    }

}
