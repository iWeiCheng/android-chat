package com.caijia.chat.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cai.jia on 2016/4/8 0008.
 */
public class CmdMessage implements Parcelable {

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
    }

    public CmdMessage() {
    }

    protected CmdMessage(Parcel in) {
        this.type = in.readInt();
    }

}
