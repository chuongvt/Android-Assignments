// CHUONG VIET TRUONG
// CMPS 121
// ASSIGNMENT 2
// MAY 10TH, 2018

package com.example.chuong.eventcapturer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;

public class AddEvent extends AppCompatActivity implements OnRequestPermissionsResultCallback {

    private static final String TAG = "TEST";
    public JSONObject jsonO = null;
    public JSONArray jsonA = null;
    int cDay, cMonth, cYear, cHour, cMinute, cSecond, cAMPM;
    Double longitude , latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ActivityCompat.requestPermissions(AddEvent.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                99);

        final EditText title = (EditText) findViewById(R.id.titleinput_edittext);
        final EditText description = (EditText) findViewById(R.id.eventdescriptioninput_edittext);
        Button enterButton = (Button) findViewById(R.id.enter_button);

        TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
        final Calendar theCalender = new GregorianCalendar();

        // Read file
        try {
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream o = new ObjectInputStream(fi);
            String j = null;
            try {
                j = (String) o.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                jsonO = new JSONObject(j);
                jsonA = jsonO.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            // Initialize new JSONObject
            jsonO = new JSONObject();
            jsonA = new JSONArray();
            try {
                jsonO.put("data", jsonA);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }

        enterButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss aa");
                String time = dateFormat.format(new Date()).toString();

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);
                Location l;
                // Go through the location providers starting with GPS, stop as soon
                // as we find one.
                for (int i=providers.size()-1; i>=0; i--) {
                    l = lm.getLastKnownLocation(providers.get(i));
                    longitude = l.getLongitude();
                    latitude = l.getLatitude();
                    if (l != null) break;
                }

                longitude = (double)Math.round(longitude * 100000d) / 100000d;
                latitude = (double)Math.round(latitude * 100000d) / 100000d;

                cDay = theCalender.get(Calendar.DAY_OF_MONTH);
                cMonth = theCalender.get(Calendar.MONTH) + 1;
                cYear = theCalender.get(Calendar.YEAR);
                cHour = theCalender.get(Calendar.HOUR);
                cMinute = theCalender.get(Calendar.MINUTE);
                cSecond = theCalender.get(Calendar.SECOND);
                cAMPM = theCalender.get(Calendar.AM_PM);

                final String date = (cMonth < 10 ? "0"+cMonth : cMonth) + "-" +
                        (cDay < 10 ? "0"+cDay : cDay) + "-" + cYear;
                final String gps = latitude + ", " + longitude;

                String titleText = title.getText().toString();
                String descriptionText = description.getText().toString();

                JSONObject temp = new JSONObject();
                try {
                    temp.put("title", titleText);
                    temp.put("time", time);
                    temp.put("date", date);
                    temp.put("gps", gps);
                    temp.put("description", descriptionText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonA.put(temp);

                // Write file
                try {
                    File f = new File(getFilesDir(), "file.ser");
                    FileOutputStream fo = new FileOutputStream(f);
                    ObjectOutputStream o = new ObjectOutputStream(fo);
                    String j = jsonO.toString();
                    o.writeObject(j);
                    o.close();
                    fo.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(AddEvent.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "callback");
        switch (requestCode) {
            case 99:
                // If the permissions aren't set, then return. Otherwise, proceed.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                                , 10);
                    }
                    Log.d(TAG, "returning program");
                    return;
                } else {
                    // Create Intent to reference MyService, start the Service.
                    Log.d(TAG, "starting service");
                    Intent i = new Intent(this, MyService.class);
                    if (i == null)
                        Log.d(TAG, "intent null");
                    else {
                        startService(i);
                    }

                }
                break;
            default:
                break;
        }
    }
    */
}