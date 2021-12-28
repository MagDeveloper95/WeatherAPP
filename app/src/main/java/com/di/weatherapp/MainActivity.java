package com.di.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    public static JSONObject jsonStatic;
    public String municipio = "Córdoba";
    public LineChartView graphView;
    int[] axisData = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
    int[] yAxisData = { -10,-5,0,5,10,15,20,25,30,35,40,45,50,55,60};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphView = findViewById(R.id.TempGraph);
        List axisValues = new ArrayList();
        List yAxisValues = new ArrayList();
        graphView.setInteractive(true);


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