package com.example.android.sunshine.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.sunshine.data.SunshineRepository;
import com.example.android.sunshine.data.database.WeatherEntry;

import java.util.Date;
import java.util.List;

/**
 * Created by vyshas on 04/03/2018.
 */

public class MainActivityViewModel extends ViewModel {

    private LiveData<WeatherEntry> mWeather;

    private SunshineRepository sunshineRepository;

    public MainActivityViewModel(SunshineRepository sunshineRepository) {
        this.mWeather = new MediatorLiveData<>();
        this.sunshineRepository = sunshineRepository;
    }

    public LiveData<List<WeatherEntry>> getForecastsAfterDate(Date date){
        return sunshineRepository.getForecastAfterDate(date);
    }


}
