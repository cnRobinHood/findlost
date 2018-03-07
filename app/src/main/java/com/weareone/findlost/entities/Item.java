package com.weareone.findlost.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    private Long itemid;
    private String createbyid;
    private String username;
    private String remark;
    private String iamgeId;
    private String date;
    private Integer status;
    private Integer type;

    public Item() {
    }

    protected Item(Parcel in) {
        this.itemid = (Long) in.readValue(Long.class.getClassLoader());
        this.createbyid = in.readString();
        this.username = in.readString();
        this.remark = in.readString();
        this.iamgeId = in.readString();
        this.date = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public Long getItemid() {
        return itemid;
    }

    public void setItemid(Long itemid) {
        this.itemid = itemid;
    }

    public String getCreatebyid() {
        return createbyid;
    }

    public void setCreatebyid(String createbyid) {
        this.createbyid = createbyid == null ? null : createbyid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getIamgeId() {
        return iamgeId;
    }

    public void setIamgeId(String iamgeId) {
        this.iamgeId = iamgeId == null ? null : iamgeId.trim();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemid=" + itemid +
                ", createbyid='" + createbyid + '\'' +
                ", username='" + username + '\'' +
                ", remark='" + remark + '\'' +
                ", iamgeId='" + iamgeId + '\'' +
                ", date='" + date + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.itemid);
        dest.writeString(this.createbyid);
        dest.writeString(this.username);
        dest.writeString(this.remark);
        dest.writeString(this.iamgeId);
        dest.writeString(this.date);
        dest.writeValue(this.status);
        dest.writeValue(this.type);
    }
}