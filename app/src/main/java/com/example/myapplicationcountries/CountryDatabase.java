package com.example.myapplicationcountries;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Country.class}, version = 1)
public abstract class CountryDatabase extends RoomDatabase {

    private static CountryDatabase instance;

    public abstract CountryDao countryDao();

    public static synchronized CountryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            CountryDatabase.class, "country_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
