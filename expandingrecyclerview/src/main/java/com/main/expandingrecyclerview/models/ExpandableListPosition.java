package com.main.expandingrecyclerview.models;

import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExpandableListPosition {
    private static  final  int MAX_POOL_SIZE = 5;
    private static ArrayList<ExpandableListPosition> sPool = new ArrayList<ExpandableListPosition>(MAX_POOL_SIZE);

    public final static int CHILD = 1;

    public final static int GROUP = 2;

    public int groupPos;

    public int childPos;

    int flatListPos;

    public int type;

    private void resetState(){
        groupPos =0;
        childPos = 0;
        flatListPos =0;
        type = 0;
    }

    public ExpandableListPosition() {
    }

    public long getPackedPosition(){
        if (type == CHILD){
            return ExpandableListView.getPackedPositionForChild(groupPos,childPos);
        }else {
            return ExpandableListView.getPackedPositionGroup(groupPos);
        }
    }
    static  ExpandableListPosition obtainGroupPosition(int groupPosition){
        return obtain(GROUP,groupPosition,0,0);
    }

    static ExpandableListPosition obtainCHildPosition(int groupPosition,int childPosition){
        return obtain(CHILD,groupPosition,childPosition,0);
    }

    static  ExpandableListPosition obtainPosition(long packedPosition){
        if (packedPosition == ExpandableListView.PACKED_POSITION_VALUE_NULL){
            return null;
        }
        ExpandableListPosition elp = getRecycledOrCreate();
        elp.groupPos = ExpandableListView.getPackedPositionGroup(packedPosition);
        if (ExpandableListView.getPackedPositionType(packedPosition)==
        ExpandableListView.PACKED_POSITION_TYPE_CHILD){
            elp.type = CHILD;
            elp.childPos = ExpandableListView.getPackedPositionChild(packedPosition);
        }else {
            elp.type = GROUP;
        }
        return elp;
    }
    public static ExpandableListPosition obtain(int type,int groupPos, int childPos,int flatListPos){
        ExpandableListPosition elp = getRecycledOrCreate();
        elp.type = type;
        elp.groupPos = groupPos;
        elp.childPos = childPos;
        elp.flatListPos = flatListPos;
        return elp;
    }

    private static ExpandableListPosition getRecycledOrCreate(){
        ExpandableListPosition elp;
        synchronized (sPool){
            if (sPool.size() >0){
                elp = sPool.remove(0);
            }else {
                return new ExpandableListPosition();
            }
        }
        elp.resetState();
        return elp;
    }

    public void recycle(){
        synchronized (sPool){
            if (sPool.size() < MAX_POOL_SIZE){
                sPool.add(this);
            }
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        ExpandableListPosition that = (ExpandableListPosition) obj;

        if (groupPos != that.groupPos)
            return false;
        if (childPos != that.childPos)
            return false;
        if (flatListPos != that.flatListPos)
            return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result  = groupPos;
        result = 31* result +childPos;
        result = 31 * result + flatListPos;
        result = 31 * result + type;
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "ExpandableListPosition{" +
                "groupPos=" + groupPos +
                ", childPos=" + childPos +
                ", flatListPos=" + flatListPos +
                ", type=" + type +
                '}';
    }
}
