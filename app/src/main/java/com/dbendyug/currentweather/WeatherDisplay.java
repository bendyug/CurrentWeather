package com.dbendyug.currentweather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WeatherDisplay extends AppCompatActivity {
    final int REQUEST_CODE = 12345;
    final String OPENWEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String OPENWEATHER_APP_ID = "247a12fa1a27cf32aeea9f6a2d6bd134";
    // Location updates (ms)
    final long MIN_TIME_UPDATES = 10000;
    // Location updates (1km)
    final float MIN_DISTANCE_UPDATES = 1000;
    final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;
    final String CITY_EXTRA = "City";
    final String Q_REQUEST_PARAM = "q";
    final String APPID_REQUEST_PARAM = "appid";
    final String LAT_REQUEST_PARAM = "lat";
    final String LON_REQUEST_PARAM = "lon";

    TextView mCityName;
    TextView mTemperature;
    TextView mWind;
    TextView mCondition;
    ImageView mWeatherIcon;
    ImageButton mChangeCity;
    Button mMyLocationButton;
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_display_layout);
        mCityName = findViewById(R.id.cityNameTextView);
        mWeatherIcon = findViewById(R.id.weatherIconImageView);
        mWind = findViewById(R.id.windTextView);
        mTemperature = findViewById(R.id.temperatureTextView);
        mChangeCity = findViewById(R.id.changeCityButton);
        mCondition = findViewById(R.id.conditionTextView);
        mMyLocationButton = findViewById(R.id.getCurrentWeatherButton);
        mMyLocationButton.setVisibility(View.INVISIBLE);

        mMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherForMyLocation();
                Toast.makeText(getApplicationContext(), R.string.default_location, Toast.LENGTH_SHORT).show();
                mMyLocationButton.setVisibility(View.INVISIBLE);
            }
        });

        mChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherDisplay.this, ChangeCityDisplay.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        String city = intent.getStringExtra(CITY_EXTRA);
        if (city != null && !city.isEmpty()){
            getWeatherForACertainCity(city);
            mMyLocationButton.setVisibility(View.VISIBLE);
        } else {
            getWeatherForMyLocation();
        }
    }

    private void getWeatherForACertainCity (String city){
        RequestParams requestParams = new RequestParams();
        requestParams.put(Q_REQUEST_PARAM, city);
        requestParams.put(APPID_REQUEST_PARAM, OPENWEATHER_APP_ID);
        getWeatherModel(requestParams);
    }

    private void getWeatherForMyLocation(){
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (!String.valueOf(location.getLongitude()).isEmpty() && !String.valueOf(location.getLatitude()).isEmpty()) {
                    String longitude = String.valueOf(location.getLongitude());
                    String latitude = String.valueOf(location.getLatitude());
                    RequestParams requestParams = new RequestParams();
                    requestParams.put(LAT_REQUEST_PARAM, latitude);
                    requestParams.put(LON_REQUEST_PARAM, longitude);
                    requestParams.put(APPID_REQUEST_PARAM, OPENWEATHER_APP_ID);
                    getWeatherModel(requestParams);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_UPDATES, mLocationListener);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherForMyLocation();
            } 
        }
    }

    private void getWeatherModel(RequestParams requestParams){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(OPENWEATHER_URL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responce){
                WeatherModel weatherModel = WeatherModel.fromJson(responce);
                updateInterface(weatherModel);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject responce){
                Toast.makeText(WeatherDisplay.this, R.string.toast_failed_request, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateInterface(WeatherModel weatherModel){
        mTemperature.setText(weatherModel.getmTemperature());
        mCityName.setText(weatherModel.getmCity());
        final String wind = String.valueOf(weatherModel.getmWind()) + " m/s";
        mWind.setText(wind);
        final int resourceID = getResources().getIdentifier(weatherModel.getmIconName(), "drawable", getPackageName());
        mWeatherIcon.setImageResource(resourceID);
        mCondition.setText(weatherModel.getmCondition());
    }

    protected void onPause(){
        super.onPause();
        if (mLocationManager != null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
