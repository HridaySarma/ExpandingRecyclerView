package com.main.expandingrecyclerview.listners;

import com.main.expandingrecyclerview.models.ExpandableGroup;

public interface GroupExpandCollapseListener {

    void onGroupExpanded(ExpandableGroup group);

    void onGroupCollapsed(ExpandableGroup group);

}
