package com.main.expandingrecyclerview.listners;

public interface ExpandCollapseListener {

    void onGroupExpanded(int positionStart, int itemCount) ;

    void onGroupCollapsed(int positionStart, int itemCount);
}
