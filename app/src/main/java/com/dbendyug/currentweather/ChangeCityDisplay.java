package com.dbendyug.currentweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeCityDisplay extends AppCompatActivity {
    final String CITY_EXTRA = "City";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);
        final EditText editCityTextView = findViewById(R.id.enterCityEditText);
        final ImageButton backImageButton = findViewById(R.id.backImageButton);
        final Button applyButton = findViewById(R.id.applyButton);

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeCityDisplay.this, WeatherDisplay.class);
                finish();
                startActivity(intent);
            }
        });

        editCityTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final String city = editCityTextView.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.empty_city_toast, Toast.LENGTH_SHORT).show();
                } else{
                    final Intent intent = new Intent(ChangeCityDisplay.this, WeatherDisplay.class);
                    intent.putExtra(CITY_EXTRA, city);
                    startActivity(intent);
                }
                return false;
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String city = editCityTextView.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.empty_city_toast, Toast.LENGTH_SHORT).show();
                } else {
                    final Intent intent = new Intent(ChangeCityDisplay.this, WeatherDisplay.class);
                    intent.putExtra(CITY_EXTRA, city);
                    startActivity(intent);
                }
            }
        });
    }
}
