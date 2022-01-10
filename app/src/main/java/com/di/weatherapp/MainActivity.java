package com.di.weatherapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static JSONObject jsonStatic;
    public String municipio = "Córdoba";
    private String[] xdata={"L","M","X","J","V"};
    private int[] yfata={0,5,10,15,20,25,30,35,40,45,50};
    private float[] linedata={0,10,6,30,5};
    private float[] linedata2={10,-4,12,40,15};
    private LinearLayout view;
    public Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.view = (LinearLayout) findViewById(R.id.view);
        this.boton = (Button) findViewById(R.id.boton);
        Context context = this;
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

    /**
     * Método que pulsando el botón, muestra la gráfica de temperaturas
     *
     * @param view
     */
    public void onButton(View view){
        LineChartView linechartview = new LineChartView(this);
        linechartview.setChartdate(xdata,yfata,linedata, linedata2);
        /**
         * Quiero que el linechartview se vea solo en el activity_main, pero no consigo meterlo en algún
         * contenedor, solo puedo machacar el content que hay.
         */
        setContentView(linechartview);
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
            daxesPaint.setColor(Color.BLACK);
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
            canvas.drawLine(widthCriterion - minCriterion, hightCriterion + minCriterion, widthCriterion + 2, hightCriterion, p);
            canvas.drawLine(widthCriterion, hightCriterion, widthCriterion + minCriterion - 2, hightCriterion + minCriterion, p);
            // Start x Draw a coordinate system
            canvas.drawLine(widthCriterion - 4, hightCriterion * (yCopies - 1), widthCriterion * (xCopies - 1), hightCriterion * (yCopies - 1), p);
            // draw x horn
            canvas.drawLine(widthCriterion * (xCopies - 1) - minCriterion, hightCriterion * (yCopies - 1) - minCriterion, widthCriterion * (xCopies - 1), hightCriterion * (yCopies - 1) + 2, p);
            canvas.drawLine(widthCriterion * (xCopies - 1) - minCriterion, hightCriterion * (yCopies - 1) + minCriterion, widthCriterion * (xCopies - 1), hightCriterion * (yCopies - 1) - 2, p);

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

    /*
        public void setTextView(String view) {
            this.textView.setText(view);
        }
        */

}