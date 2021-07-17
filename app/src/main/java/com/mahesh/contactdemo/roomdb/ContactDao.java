package com.mahesh.contactdemo.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahesh.contactdemo.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact contact);

    @Query("SELECT count(*) FROM table_contact where deleteFlag = 0 order by name")
    Long getCount();

    @Query("SELECT count(*) FROM table_contact where favFlag = 1 order by name")
    Long getFavCount();

    @Query("SELECT count(*) FROM table_contact where deleteFlag = 1 order by name")
    Long getDeleteCount();

    @Query("SELECT * FROM table_contact where deleteFlag= :deleteId order by name")
    List<Contact> getAllContacts(int deleteId);

    @Query("SELECT * FROM table_contact where favFlag = :favId order by name")
    List<Contact> getAllFavouriteContacts(int favId);

    @Query("SELECT * FROM table_contact where deleteFlag = :deleteId order by name")
    List<Contact> getAllDeletedContacts(int deleteId);

    @Query("UPDATE table_contact SET favFlag = :fav WHERE phoneNumber = :number")
    void updateFav(String number,int fav);

    @Query("UPDATE table_contact SET deleteFlag = :del,favFlag = :fav WHERE phoneNumber = :number")
    void updateDel(String number,int del,int fav);

    @Query("UPDATE table_contact SET deleteFlag = 0 WHERE deleteFlag = 1")
    void restoreAll();

}
