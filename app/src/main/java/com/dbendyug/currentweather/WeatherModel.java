package com.dbendyug.currentweather;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherModel {
    private String mTemperature;
    private String mIconName;
    private String mCityName;
    private String mCondition;
    private int mWeatherCondition;
    private int mWind;

    public static WeatherModel fromJson (JSONObject jsonObject){
        try{
            WeatherModel weatherModel = new WeatherModel();
            weatherModel.mCityName = jsonObject.getString("name");
            weatherModel.mWind = jsonObject.getJSONObject("wind").getInt("speed");
            Log.d("Current Weather", Integer.toString(weatherModel.mWind));
            weatherModel.mWeatherCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherModel.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            weatherModel.mCondition = weatherModel.mCondition.substring(0,1).toUpperCase() + weatherModel.mCondition.substring(1);
            Log.d("Current Weather", "condition is: " + weatherModel.mCondition);
            weatherModel.mIconName = findWeatherIcon(weatherModel.mWeatherCondition);

            int roundedTemperature = (int) Math.rint(jsonObject.getJSONObject("main").getDouble("temp") - 273.15);
            weatherModel.mTemperature = Integer.toString(roundedTemperature);
            return weatherModel;

        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private static String findWeatherIcon(int weatherCondition) {

        if (weatherCondition >= 0 && weatherCondition < 300) {
            return "thunderstorm";
        } else if (weatherCondition >= 300 && weatherCondition < 500) {
            return "showerrain";
        } else if (weatherCondition >= 500 && weatherCondition < 520) {
            return "rain";
        } else if(weatherCondition >= 520 && weatherCondition < 600) {
            return "showerrain";
        } else if (weatherCondition >= 600 && weatherCondition <= 700) {
            return "snow";
        } else if (weatherCondition >= 701 && weatherCondition < 800) {
            return "mist";
        } else if (weatherCondition == 800) {
            return "clearsky";
        } else if (weatherCondition == 801) {
            return "fewclouds";
        } else if (weatherCondition == 803){
            return "scatteredclouds";
        } else if (weatherCondition == 804 || weatherCondition == 805){
            return "brokenclouds";
        }

        return "unknown";
    }

    public String getmTemperature() {
        return mTemperature + "°C";
    }

    public String getmCity() {
        return mCityName;
    }

    public String getmIconName() {
        return mIconName;
    }

    public int getmWind() {
        return mWind;
    }

    public String getmCondition() {
        return mCondition;
    }
}
