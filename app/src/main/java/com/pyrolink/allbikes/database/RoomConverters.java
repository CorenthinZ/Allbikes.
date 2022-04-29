package com.pyrolink.allbikes.database;

import android.annotation.SuppressLint;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RoomConverters
{
    @SuppressLint("SimpleDateFormat")
    private final static SimpleDateFormat _SDF = new SimpleDateFormat("dd/MM/yyyy");

    @TypeConverter
    public static String fromDate(Date date)
    {
        return _SDF.format(date);
    }

    @TypeConverter
    public static Date toDate(String date) throws ParseException
    {
        return _SDF.parse(date);
    }

    @TypeConverter
    public static String fromList(ArrayList<String> list)
    {
        StringBuilder s = new StringBuilder(list.get(0));

        for (int i = 1; i < list.size(); i++)
            s.append("|").append(list.get(i));

        return s.toString();
    }

    @TypeConverter
    public static ArrayList<String> toList(String list)
    {
        return new ArrayList<>(Arrays.asList(list.split("\\|")));
    }
}
