package com.mahesh.contactdemo.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mahesh.contactdemo.model.Contact;

@Database(entities = {Contact.class},version = 1,exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDao Dao();

    public static ContactRoomDatabase INSTANCE;

    public static ContactRoomDatabase getDatabase(Context context){
        if(INSTANCE==null){
            synchronized (ContactRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context,
                            ContactRoomDatabase.class,"ContactList")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;

    }
}
