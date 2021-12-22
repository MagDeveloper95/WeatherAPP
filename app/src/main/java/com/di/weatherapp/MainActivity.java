package com.di.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public Button button;
    public TextView textView;
    public static JSONObject jsonStatic;
    public String municipio = "Córdoba";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        this.button = findViewById(R.id.button);
        this.textView = findViewById(R.id.textView);

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //municipio = getText();
                    JSONObject json = new RetrieveFeedTask().execute("https://api.openweathermap.org/data/2.5/weather?q="+municipio+",,ES&lang=es&units=metric&appid=df120d9fec587d76e8732e07c21a99cf").get();
                    JSONObject main = json.getJSONObject("main");
                    JSONArray weather = json.getJSONArray("weather");
                    setTextView("Municipio: "+json.get("name").toString()+"\n"+
                            "Temperatura Actual= "+main.get("temp").toString()+"ªC"+"\n"+
                            "Temperatura Máxima = "+main.get("temp_max").toString()+"ªC"+"\n"+
                            "Temperatura Mínima = "+main.get("temp_min").toString()+"ªC"+"\n"+
                            "Humedad = "+main.get("humidity").toString()+"%"+"\n"+
                            "Descripción: "+weather.getJSONObject(0).get("description").toString().substring(0, 1).toUpperCase() + weather.getJSONObject(0).get("description").toString().substring(1));
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         */
    }
    /*
    public void setTextView(String view) {
        this.textView.setText(view);
    }
    */

}