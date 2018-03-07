package com.weareone.findlost.entities;


import android.os.Parcel;
import android.os.Parcelable;

public class Userinfo implements Parcelable {
    public static final Parcelable.Creator<Userinfo> CREATOR = new Parcelable.Creator<Userinfo>() {
        @Override
        public Userinfo createFromParcel(Parcel source) {
            return new Userinfo(source);
        }

        @Override
        public Userinfo[] newArray(int size) {
            return new Userinfo[size];
        }
    };
    private String id;
    private String userid;
    private String username;
    private String userrealname;
    private Integer sex;
    private String qq;
    private String phonenum;
    private String headimag;
    private Integer state;

    public Userinfo() {
    }

    protected Userinfo(Parcel in) {
        this.id = in.readString();
        this.userid = in.readString();
        this.username = in.readString();
        this.userrealname = in.readString();
        this.sex = (Integer) in.readValue(Integer.class.getClassLoader());
        this.qq = in.readString();
        this.phonenum = in.readString();
        this.headimag = in.readString();
        this.state = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getUserrealname() {
        return userrealname;
    }

    public void setUserrealname(String userrealname) {
        this.userrealname = userrealname == null ? null : userrealname.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum == null ? null : phonenum.trim();
    }

    public String getHeadimag() {
        return headimag;
    }

    public void setHeadimag(String headimag) {
        this.headimag = headimag == null ? null : headimag.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userid);
        dest.writeString(this.username);
        dest.writeString(this.userrealname);
        dest.writeValue(this.sex);
        dest.writeString(this.qq);
        dest.writeString(this.phonenum);
        dest.writeString(this.headimag);
        dest.writeValue(this.state);
    }
}