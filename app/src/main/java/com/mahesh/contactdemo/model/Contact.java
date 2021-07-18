package com.mahesh.contactdemo.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "table_contact", indices = @Index(value = {"phoneNumber"}, unique = true))
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;
    @NonNull
    @ColumnInfo(name = "phoneNumber")
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @NonNull
    @ColumnInfo(name = "photo")
    @SerializedName("photo")
    private String photo;
    @NonNull
    @ColumnInfo(name = "favFlag")
    @SerializedName("favFlag")
    private int favFlag = 0;
    @NonNull
    @ColumnInfo(name = "deleteFlag")
    @SerializedName("deleteFlag")
    private int deleteFlag = 0;

    @ColumnInfo(name = "colorCode")
    @SerializedName("colorCode")
    private int colorCode = 0;


    public Contact() {
    }

    public Contact(@NonNull String name, @NonNull String phoneNumber, @NonNull String photo, int favFlag, int deleteFlag,int colorCode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
        this.favFlag = favFlag;
        this.deleteFlag = deleteFlag;
        this.colorCode = colorCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(@NonNull String photo) {
        this.photo = photo;
    }

    public int getFavFlag() {
        return favFlag;
    }

    public void setFavFlag(int favFlag) {
        this.favFlag = favFlag;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return getPhoneNumber().equals(contact.getPhoneNumber());
    }

}

