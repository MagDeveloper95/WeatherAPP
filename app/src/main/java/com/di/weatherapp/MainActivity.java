package com.di.weatherapp;

//import org.jetbrains.annotations.Nullable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static JSONObject jsonStatic;
    public String municipio = "Córdoba";
    private String[] xdata = {"L", "M", "X", "J", "V", "S", "D", "L"};
    private String[] days = new String[8];
    private int[] yfata = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
    public float[] linedata = {0, 10, 6, 30, 5, 10, 12, 13};
    public float[] linedata2 = {10, 14, 12, 40, 15, 18, 20, 23};
    private LinearLayout root;
    public Button boton;       //botón de ciudad
    public EditText editText;
    public TextView textView;
    public Button button;      //botón de temperatura

    public String lat = "37.88";
    public String lon = "-4.77";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.root = (LinearLayout) findViewById(R.id.view);
        this.boton = findViewById(R.id.boton);
        this.button = findViewById(R.id.button);
        this.editText = findViewById(R.id.edittext);
        this.textView = findViewById(R.id.textview);

        this.boton.setEnabled(false);


        /**
         * Método que pulsando el botón de "Obtener Datos", comprueba la ciudad introducida si es
         * válida, comprobando el JSON de las ciudades, si es válida, setea la lat y lon, después
         * hace una petición a la API y si obtiene los datos correctamente los setea y habilita el
         * botón de ver temperatura.
         */
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enableButton = false;
                try {
                    enableButton = getCity(editText.getText().toString());
                    //Realizamos la petición a la API
                    JSONObject json = new RetrieveFeedTask().execute("https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon=" + lon + "&units=metric&lang=es&exclude=minutely,hourly&appid=df120d9fec587d76e8732e07c21a99cf").get();
                    JSONObject current = json.getJSONObject("current");
                    JSONArray daily = json.getJSONArray("daily");
                    showToast("Datos cargados correctamente");
                    boton.setEnabled(enableButton);

                    for (int i = 0; i < daily.length(); i++) {
                        //Parseamos el dt: UNIX a Date, para tratarlo en JAVA
                        java.util.Date time = new java.util.Date((long) (Integer.parseInt(daily.getJSONObject(i).get("dt").toString())) * 1000);
                        Calendar c = Calendar.getInstance();
                        c.setTime(time);
                        String dayWeekText = new SimpleDateFormat("EEEE").format(time);
                        days[i] = dayWeekText; //Seteamos los días de la semana.
                        //Obtenemos los valores de las temperaturas y las seteamos en las variables globales.
                        float min = Float.parseFloat(daily.getJSONObject(i).getJSONObject("temp").get("min").toString());
                        float max = Float.parseFloat(daily.getJSONObject(i).getJSONObject("temp").get("max").toString());
                        linedata[i] = (min<0)?0:min;
                        linedata2[i] = (max<0)?0:max;
                    }

                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Función que muestra un toast con un texto pasado por parámetro
     * @param text texto a mostrar
     */
    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    //Introduce una ciudad y si la encuentra devuelve lat y lon del JSON 
    public boolean getCity(String city) {
        boolean getCity = false;
        if (city.equals("")) {
            showToast("No se ha introducido ninguna ciudad");
        } else {
            if (city.length() < 0) {
                showToast("No se ha introducido ninguna ciudad");
            } else {
                if (city.matches("[0-9]+")) {
                    showToast("No se ha introducido una ciudad válida");
                } else {
                    try {
                        JSONArray json = new JSONArray(readJSONFromAsset());
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject jsonObject = json.getJSONObject(i);
                            if (jsonObject.getString("city").equals(city)) {
                                lat = jsonObject.getString("lat");
                                lon = jsonObject.getString("lng");
                                showToast("Ciudad cargada correctamente");
                                getCity = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return getCity;
    }

    /**
     * Método que lee el JSON con los municipios.
     *
     * @return devuelve el JSON como String
     */
    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("es.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Método que pulsando el botón, muestra la gráfica de temperaturas
     */
    public void drawChart(View view) {
        this.boton.setEnabled(false);
        setXdata(days);
        LineChartView linechartview = new LineChartView(this);
        linechartview.setChartdate(xdata, yfata, linedata, linedata2);

        this.root.removeAllViews();
        this.root.addView(linechartview);

    }

    /**
     * Setea los datos de los Días en el ejeX
     * @param days días de la semana
     */
    public void setXdata(String[] days) {
        for (int i = 0; i < this.xdata.length; i++) {
            this.xdata[i] = setDay(days[i]);
        }
    }

    /**
     * Método que recogiendo el Día, devuelve su abreviatura.
     *
     * @param day Día de la semana
     * @return abreviatura del día.
     */
    public String setDay(String day) {
        String setDay = "";
        switch (day) {
            case "lunes":
                setDay = "L";
                break;
            case "martes":
                setDay = "M";
                break;
            case "miércoles":
                setDay = "X";
                break;
            case "jueves":
                setDay = "J";
                break;
            case "viernes":
                setDay = "V";
                break;
            case "sábado":
                setDay = "S";
                break;
            case "domingo":
                setDay = "D";
                break;
        }
        return setDay;
    }


    public void setTextView(String view) {
        System.out.println(view);
        this.textView.setText(view);
    }


    /**
     * Clase que pinta la gráfica de lineas.
     * Utilizando canvas.
     */
    public class LineChartView extends View {

        private int minCriterion;
        private int hightCriterion;
        private int widthCriterion;
        private int canvasHeight;
        private int canvasWidth;
        private int textFont;

        private String[] xdate;
        private int[] ydate;
        private float[] linedate;
        private float[] linedate2;
        private int xCopies;
        private float yCopies;

        public void setChartdate(String[] xdate, int[] ydate, float[] linedate, float[] linedate2) {
            this.xdate = xdate;
            this.ydate = ydate;
            this.linedate = linedate;
            this.linedate2 = linedate2;
        }

        public LineChartView(Context context) {
            super(context);
        }

        public LineChartView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);

        }

        /**
         * Control de errores y pinta
         * @param canvas
         */
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (xdate.length != 0 && ydate.length != 0 && linedate.length != 0 && linedate2.length != 0 && xdate.length >= linedate.length && xdate.length >= linedate2.length) {
                if (yMaxdata() >= lineMaxdata()) {
                    drawAxis(canvas);
                }
            }
        }

        // draw
        private void drawAxis(Canvas canvas) {
            xCopies = xdate.length + 2;
            yCopies = ydate.length + 2;
            Paint daxesPaint, axispointPaint, brokenLinePaint, brokenLinePaint2;
            // Canvas width
            canvasWidth = canvas.getWidth();
            // Canvas height
            canvasHeight = canvas.getHeight();
            widthCriterion = canvasWidth / xCopies;
            hightCriterion = (int) (canvasHeight / yCopies);
            minCriterion = widthCriterion > hightCriterion ? hightCriterion / 2 : widthCriterion / 2;
            // Start painting the underlying background
            daxesPaint = new Paint();
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    daxesPaint.setColor(Color.WHITE);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    daxesPaint.setColor(Color.BLACK);
                    break;
            }
            daxesPaint.setAntiAlias(true);  // Remove the jagged effect
            daxesPaint.setStrokeWidth(7.0f);
            drawDaxes(canvas, daxesPaint);

            // Start drawing coordinate points
            axispointPaint = daxesPaint;
            drawAxispoint(canvas, axispointPaint);

            // Start drawing polylines and points on the line
            brokenLinePaint = axispointPaint;
            brokenLinePaint.setStrokeWidth(5.0f);

            brokenLinePaint2 = axispointPaint;
            brokenLinePaint2.setStrokeWidth(5.0f);

            drawbrokenLine(canvas, brokenLinePaint);
            drawbrokenLine2(canvas, brokenLinePaint2);
        }


        private void drawDaxes(Canvas canvas, Paint p) {
            // Start y Draw a coordinate system
            canvas.drawLine(widthCriterion, hightCriterion, widthCriterion, hightCriterion * (yCopies - 1), p);
            // draw y horn
            //canvas.drawLine(widthCriterion - minCriterion, hightCriterion + minCriterion, widthCriterion + 2, hightCriterion, p);
            //canvas.drawLine(widthCriterion, hightCriterion, widthCriterion + minCriterion - 2, hightCriterion + minCriterion, p);
            // Start x Draw a coordinate system
            canvas.drawLine(widthCriterion - 4, hightCriterion * (yCopies - 1), widthCriterion * (xCopies - 1), hightCriterion * (yCopies - 1), p);
            // draw x horn
            //canvas.drawLine(widthCriterion * (xCopies - 1) - minCriterion, hightCriterion * (yCopies - 1) - minCriterion, widthCriterion * (xCopies - 1), hightCriterion * (yCopies - 1) + 2, p);
            //canvas.drawLine(widthCriterion * (xCopies - 1) - minCriterion, hightCriterion * (yCopies - 1) + minCriterion, widthCriterion * (xCopies - 1), hightCriterion * (yCopies - 1) - 2, p);

        }

        private void drawAxispoint(Canvas canvas, Paint p) {
            textFont = widthCriterion / 5 * 2;
            Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
            p.setTypeface(font);
            p.setTextSize(textFont);
            // draw x Axis data
            for (int i = 0; i < xdate.length; i++) {
                String text = xdate[i];
                int stringWidth = (int) p.measureText(text);     // Text length
                canvas.drawText(text, (i + 1) * widthCriterion - stringWidth / 2, hightCriterion * (yCopies - 1) + textFont, p);//  Draw text
            }
            for (int i = 0; i < ydate.length; i++) {
                String text = String.valueOf(ydate[i]);

                int stringWidth = (int) p.measureText(text);
                // Text length
                if (i == 0) {
                } else {
                    canvas.drawText(text, widthCriterion - textFont - stringWidth, hightCriterion * (yCopies - 1) - i * hightCriterion + stringWidth / 2, p);//  Draw text

                }
            }
        }

        private void drawbrokenLine(Canvas canvas, Paint p) {
            float line = (hightCriterion * (yCopies - 1) - hightCriterion * 2) / ydate[ydate.length - 1];
            p.setColor(Color.BLUE);

            for (int i = 0; i < linedate.length; i++) {
                float height = hightCriterion * (yCopies - 1) - line * linedate[i];
                if (i != linedate.length - 1) {
                    float elseheight = hightCriterion * (yCopies - 1) - line * linedate[i + 1];
                    canvas.drawLine(widthCriterion * (i + 1), height, widthCriterion * (i + 2), elseheight, p);
                    canvas.drawCircle(widthCriterion * (i + 1), height, 10, p);
                } else {
                    float endheight = hightCriterion * (yCopies - 1) - line * linedate[linedate.length - 1];
                    canvas.drawCircle(widthCriterion * (i + 1), endheight, 10, p);
                }

            }
        }

        private void drawbrokenLine2(Canvas canvas, Paint p) {
            float line = (hightCriterion * (yCopies - 1) - hightCriterion * 2) / ydate[ydate.length - 1];
            p.setColor(Color.RED);

            for (int i = 0; i < linedate2.length; i++) {
                float height = hightCriterion * (yCopies - 1) - line * linedate2[i];
                if (i != linedate2.length - 1) {
                    float elseheight = hightCriterion * (yCopies - 1) - line * linedate2[i + 1];
                    canvas.drawLine(widthCriterion * (i + 1), height, widthCriterion * (i + 2), elseheight, p);
                    canvas.drawCircle(widthCriterion * (i + 1), height, 10, p);
                } else {
                    float endheight = hightCriterion * (yCopies - 1) - line * linedate2[linedate2.length - 1];
                    canvas.drawCircle(widthCriterion * (i + 1), endheight, 10, p);
                }

            }
        }

        private float yMaxdata() {
            float max = 0;
            for (int i = 0; i < ydate.length; i++) {

                if (ydate[i] > max) {
                    max = ydate[i];

                }
            }
            return max;
        }

        private float lineMaxdata() {
            float max = 0;
            for (int i = 0; i < linedate.length; i++) {

                if (linedate[i] > max) {
                    max = linedate[i];

                }
            }
            return max;
        }
    }
}