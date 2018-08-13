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

public class ChangeCityDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_city_layout);

        final EditText editCityTextView = findViewById(R.id.enterCityEditText);
        ImageButton backImageButton = findViewById(R.id.backImageButton);
        Button applyButton = findViewById(R.id.applyButton);

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
                String city = editCityTextView.getText().toString();
                Intent intent = new Intent(ChangeCityDisplay.this, WeatherDisplay.class);
                intent.putExtra("City", city);
                startActivity(intent);
                return false;
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editCityTextView.getText().toString();
                Intent intent = new Intent(ChangeCityDisplay.this, WeatherDisplay.class);
                intent.putExtra("City", city);
                startActivity(intent);
            }
        });


    }

}
