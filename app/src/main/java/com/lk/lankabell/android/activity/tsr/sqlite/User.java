package com.lk.lankabell.android.activity.tsr.sqlite;

import java.util.Date;


public class User {

    String userName;
    String mobileNo;
    String name;
    int id;
    String password;
    int delId;
    Date enteredDate;
    int attachedBranch;


    public User(String name, int id, String password, int delId, String mobileNo, String userName, Date enteredDate, int branch) {
        super();
        this.name = name;
        this.id = id;
        this.password = password;
        this.delId = delId;
        this.userName = userName;
        this.mobileNo = mobileNo;
        this.enteredDate = enteredDate;
        this.attachedBranch = branch;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDelId() {
        return delId;
    }

    public void setDelId(int delId) {
        this.delId = delId;
    }

    public String getmobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getEnterDate() {
        return enteredDate;
    }

    public void setEnteredDate(Date entereDate) {
        this.enteredDate = entereDate;
    }

    public String getFullName() {
        return name;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public int getAttachedBranch() {
        return attachedBranch;
    }

    public void setFullName(int attachedBranch) {
        this.attachedBranch = attachedBranch;
    }

    @Override
    public String toString() {
        return "User [userName=" + userName + ", mobileNo=" + mobileNo + ", name=" + name + ", id=" + id + ", password=" + password + ", delId=" + delId + ", enteredDate=" + enteredDate + ", attachedBranch=" + attachedBranch + "]";
    }


}
