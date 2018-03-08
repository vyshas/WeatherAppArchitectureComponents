package com.example.android.sunshine.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

/**
 * Created by vyshas on 28/02/2018.
 */

@Dao
@TypeConverters(DateConverter.class)
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(WeatherEntry... weatherEntry);


    @Query("Select * from weather where date =:date ")
    LiveData<WeatherEntry> getWeatherByDate(Date date);


    @Query("Select count(id) from weather where date >= :date")
    int countAllFutureWeather(Date date);

    @Query("Delete from weather where date < :date")
    void deleteOldData(Date date);

    @Query("Select * from weather where date >= :date")
    LiveData<List<WeatherEntry>> getWeatherForecastAfterDate(Date date);


}
