package com.caijia.chat.emoticon.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cai.jia on 2016/3/30 0030.
 */
public class Emoticon implements Parcelable {

    private String unicode;

    public Emoticon(String unicode) {
        this.unicode = unicode;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.unicode);
    }

    public Emoticon() {
    }

    protected Emoticon(Parcel in) {
        this.unicode = in.readString();
    }

    public static final Creator<Emoticon> CREATOR = new Creator<Emoticon>() {
        @Override
        public Emoticon createFromParcel(Parcel source) {
            return new Emoticon(source);
        }

        @Override
        public Emoticon[] newArray(int size) {
            return new Emoticon[size];
        }
    };
}
