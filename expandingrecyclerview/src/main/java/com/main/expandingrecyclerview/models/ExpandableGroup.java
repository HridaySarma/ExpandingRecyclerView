package com.main.expandingrecyclerview.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ExpandableGroup<T extends Parcelable> implements Parcelable {

    private String title;
    private List<T> items;

    public ExpandableGroup(String title, List<T> items) {
        this.title = title;
        this.items = items;
    }

    public String getTitle() {
        return title;
    }

    public List<T> getItems() {
        return items;
    }

    public int getItemCount(){
        return items == null ? 0:items.size();
    }

    @NonNull
    @Override
    public String toString() {
        return "ExpandableGroup{" +
                "title='" + title +'\''+
                ",items=" + items +'}';
    }

    protected ExpandableGroup(Parcel in) {
        title = in.readString();
        byte hasItems = in.readByte();
        int size = in.readInt();
        if (hasItems == 0x01){
            items = new ArrayList<T>(size);
            Class<?> type = (Class<?>) in.readSerializable();
            in.readList(items,type.getClassLoader());
        }else {
            items = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        if (items == null){
            parcel.writeByte((byte)(0x01));
            parcel.writeInt(items.size());
            final Class<?> objectsType = items.get(0).getClass();
            parcel.writeSerializable(objectsType);
            parcel.writeList(items);
        }
    }

    public static final Creator<ExpandableGroup> CREATOR = new Creator<ExpandableGroup>() {
        @Override
        public ExpandableGroup createFromParcel(Parcel in) {
            return new ExpandableGroup(in);
        }

        @Override
        public ExpandableGroup[] newArray(int size) {
            return new ExpandableGroup[size];
        }
    };

}
