package com.caijia.chat.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 聊天的商品的透传消息
 * Created by cai.jia on 2016/4/8 0008.
 */
public class GoodsInfoMessage extends CmdMessage implements Parcelable {

    private String platName;

    private String proName;

    private String price;

    private String logo;

    private String goodsId;

    public GoodsInfoMessage() {
    }

    public GoodsInfoMessage(String platName, String proName, String price, String logo, String goodsId) {
        this.platName = platName;
        this.proName = proName;
        this.price = price;
        this.logo = logo;
        this.goodsId = goodsId;
    }

    public GoodsInfoMessage(String logo, String price, String proName) {
        this.logo = logo;
        this.price = price;
        this.proName = proName;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.platName);
        dest.writeString(this.proName);
        dest.writeString(this.price);
        dest.writeString(this.logo);
        dest.writeString(this.goodsId);
    }

    protected GoodsInfoMessage(Parcel in) {
        super(in);
        this.platName = in.readString();
        this.proName = in.readString();
        this.price = in.readString();
        this.logo = in.readString();
        this.goodsId = in.readString();
    }

    public static final Creator<GoodsInfoMessage> CREATOR = new Creator<GoodsInfoMessage>() {
        public GoodsInfoMessage createFromParcel(Parcel source) {
            return new GoodsInfoMessage(source);
        }

        public GoodsInfoMessage[] newArray(int size) {
            return new GoodsInfoMessage[size];
        }
    };
}
