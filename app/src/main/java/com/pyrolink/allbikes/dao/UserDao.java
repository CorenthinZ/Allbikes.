package com.pyrolink.allbikes.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pyrolink.allbikes.model.User;

@Dao
public interface UserDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void create(User user);

    /*@Query("select exists(select * from User where id = :id)")
    boolean exist(String id);

    /*@Query("select * from user where id = :id")
    User read(String id);*/
}
