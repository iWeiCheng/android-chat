package com.caijia.chat.morefunc.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 选择图片的分组
 * Created by cai.jia on 2015/8/8.
 */
public class PictureGroup implements Parcelable {

    /**
     * 包含图片的文件夹名称
     */
    private String groupName;

    /**
     * 文件夹下面的所有文件的路径
     */
    private List<String> picturePaths;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getPicturePaths() {
        return picturePaths;
    }

    public void setPicturePaths(List<String> picturePaths) {
        this.picturePaths = picturePaths;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.groupName);
        dest.writeStringList(this.picturePaths);
    }

    public PictureGroup() {
    }

    protected PictureGroup(Parcel in) {
        this.groupName = in.readString();
        this.picturePaths = in.createStringArrayList();
    }

    public static final Parcelable.Creator<PictureGroup> CREATOR = new Parcelable.Creator<PictureGroup>() {
        public PictureGroup createFromParcel(Parcel source) {
            return new PictureGroup(source);
        }

        public PictureGroup[] newArray(int size) {
            return new PictureGroup[size];
        }
    };
}
