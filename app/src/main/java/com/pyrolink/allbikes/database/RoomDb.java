package com.pyrolink.allbikes.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pyrolink.allbikes.dao.UserDao;
import com.pyrolink.allbikes.model.User;

@Database(entities = { User.class }, version = RoomDb.DATABASE_VERSION, exportSchema = false)
@TypeConverters(RoomConverters.class)
public abstract class RoomDb extends RoomDatabase
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Allbikes.LocalDb";

    public abstract UserDao userDao();

    private static volatile RoomDb INSTANCE;

    public static RoomDb getDatabase(Context context)
    {
        if (INSTANCE != null)
            return INSTANCE;


        synchronized (RoomDb.class)
        {
            if (INSTANCE == null)
                INSTANCE = Room.databaseBuilder(context, RoomDb.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return INSTANCE;
    }
}