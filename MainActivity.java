package com.example.openweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText e1;
    String jsoninfo, jsoninfo2, strings, str;
    Button b1;
    TextView t1;
    ArrayList<JSONObject> forecast; //= new ArrayList<>();
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        forecast = new ArrayList<>();
        str = new String();
        jsoninfo = new String();
        jsoninfo2 = new String();
        strings = new String();
        e1 = findViewById(R.id.editText);
        b1 = findViewById(R.id.button);
        t1 = findViewById(R.id.dayOnetext);
        img = findViewById(R.id.imageView);
        b1.setOnClickListener(new Listener());
    }

    public class Listener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            AsyncThread task = new AsyncThread();
            String zip = e1.getText().toString();
            Log.d("ZIP" , zip);
            task.execute(zip);
        }
    }

    public class AsyncThread extends AsyncTask<String, Void, Void> {
        String varlat;
        String varlong;
        String varLocation;

        String vardate;
        @Override
        protected Void doInBackground(String... strings) {
            URL url, url2;
            URLConnection connection, connection2;
            BufferedReader reader, reader2;
            InputStream IS, IS2;
            StringBuilder string = new StringBuilder();
            StringBuilder string2 = new StringBuilder();
            String geocodingapikey = "https://api.openweathermap.org/geo/1.0/zip?zip=" + strings[0] + ",US&appid=af68941da6d4659acea6c1c12c401342";

            try {
                url = new URL(geocodingapikey);
                connection = url.openConnection();
                IS = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(IS));
                String st;
                while (((st = reader.readLine()) != null)) {
                    string.append(st);
                }
                jsoninfo = string.toString();
                JSONObject jsonobj = new JSONObject(jsoninfo);
                reader.close();
                IS.close();

                varlat = jsonobj.getString("lat");
                varlong = jsonobj.getString("lon");
                varLocation = jsonobj.getString("name");
                //vardate = jsonobj.getString("dt_text");
                String weatherapikey = "https://api.openweathermap.org/data/2.5/forecast?lat=" + varlat + "&lon=" + varlong + "&appid=af68941da6d4659acea6c1c12c401342&units=imperial";
                url2 = new URL(weatherapikey);
                connection2 = url2.openConnection();
                IS2 = connection2.getInputStream();
                reader2 = new BufferedReader(new InputStreamReader(IS2));
                String st2;
                while ((st2 = reader2.readLine()) != null) {
                    string2.append(st2);
                }
                jsoninfo2 = string2.toString();
                JSONObject jsonobj2 = new JSONObject(jsoninfo2);
                JSONArray list = jsonobj2.getJSONArray("list");
                reader2.close();
                IS2.close();
                //Log.d("TAG", jsonobj.toString());
                //Log.d("TAG2", jsonobj2.toString());
                //Log.d("TAG3","doInBackground: string passed: "+strings[0]);
                //Log.d("TAG4","lat: "+varlat+ "\n" + "lon: "+varlong);
                if(forecast.size()> 1)
                    forecast.clear();
                for (int i = 0; i < 40; i += 8) {
                    forecast.add(list.getJSONObject(i));
                }
                //Log.d("TAGDATA", forecast.get(0).toString());
                //Log.d("TAGDATA2", forecast.get(1).toString());
                ////Log.d("TAGDATA3", forecast.get(2).toString());
                //Log.d("TAGDATA4", forecast.get(3).toString());
                //Log.d("TAGDATA5", forecast.get(4).toString());
                Log.d("LOGLIST", forecast.toString());
                Log.d("TAGTEMPUTURE", forecast.get(0).getJSONObject("main").getString("temp"));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            t1.setText("CHANGE");
            img.setImageResource(R.drawable.wreckitimg);

            try {
                String dec1 = forecast.get(0).getJSONArray("weather").getJSONObject(0).getString("description");
                String dec2 = forecast.get(1).getJSONArray("weather").getJSONObject(0).getString("description");
                String dec3 = forecast.get(2).getJSONArray("weather").getJSONObject(0).getString("description");
                String dec4 = forecast.get(3).getJSONArray("weather").getJSONObject(0).getString("description");
                String dec5 = forecast.get(4).getJSONArray("weather").getJSONObject(0).getString("description");
                String temp1 = forecast.get(0).getJSONObject("main").getString("temp");
                String temp2 = forecast.get(1).getJSONObject("main").getString("temp");
                String temp3 = forecast.get(2).getJSONObject("main").getString("temp");
                String temp4 = forecast.get(3).getJSONObject("main").getString("temp");
                String temp5 = forecast.get(4).getJSONObject("main").getString("temp");
                if (varlat != null && varlong != null && varLocation != null && dec1 != null) {
                    Log.d("ONPOST", varlat);
                    Log.d("OnPOSTLONG", varlong);
                    t1.setText("Latitude: " + varlat + "\n" +"Longitude: "+varlong + "\n" + "Location: "+varLocation +"\n" + "Weather: "+dec1 + "\n" + "current temp: " +temp1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
//don't forget to adjust the margin tops for day2,3,4, and 5 texts after adding each image view and each description