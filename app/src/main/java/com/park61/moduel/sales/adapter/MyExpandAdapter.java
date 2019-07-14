package com.park61.moduel.sales.adapter;

import android.database.DataSetObserver;
import android.widget.ExpandableListAdapter;

/**
 * Created by shushucn2012 on 2016/10/30.
 */

public abstract class MyExpandAdapter implements ExpandableListAdapter {
    //用抽象方法把下拉子菜单的不用的方法集中在这里,方便观看
    //抽象出接口,回调方法,用方法继承就可以定义子菜单
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
    @Override
    public void onGroupExpanded(int groupPosition) {
    }
    @Override
    public void onGroupCollapsed(int groupPosition) {

    }
    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }
    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
