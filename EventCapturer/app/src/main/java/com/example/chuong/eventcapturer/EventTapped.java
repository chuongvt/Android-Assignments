package com.example.chuong.eventcapturer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EventTapped extends AppCompatActivity {

    JSONObject jsonO = null;
    JSONArray jsonA = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_tapped);

        Intent i = getIntent();
        final String title = i.getStringExtra("title");
        final String time = i.getStringExtra("time");
        final String date = i.getStringExtra("date");
        final String gps = i.getStringExtra("gps");
        final String description = i.getStringExtra("description");

        TextView theTitle = (TextView) findViewById(R.id.title_edittext);
        TextView theTime = (TextView) findViewById(R.id.time_edittext);
        TextView theDate = (TextView) findViewById(R.id.date_edittext);
        TextView theGPS = (TextView) findViewById(R.id.gps_edittext);
        TextView theDescription = (TextView) findViewById(R.id.event_edittext);
        Button deleteButton = (Button) findViewById(R.id.delete_button);

        theTitle.setText(title);
        theTime.setText(time);
        theDate.setText(date);
        theGPS.setText(gps);
        theDescription.setText(description);

        deleteButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    File f = new File(getFilesDir(), "file.ser");
                    FileInputStream fi = new FileInputStream(f);
                    ObjectInputStream o = new ObjectInputStream(fi);
                    String j = null;
                    try {
                        j = (String) o.readObject();
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsonO = new JSONObject(j);
                        jsonA = jsonO.getJSONArray("data");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject temp = new JSONObject();
                try {
                    temp.put("title", title);
                    temp.put("time", time);
                    temp.put("date", date);
                    temp.put("gps", gps);
                    temp.put("description", description);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Delete entry in JSON array
                for (int i = 0; i < jsonA.length(); i++){
                    try {
                        if (jsonA.getJSONObject(i).getString("title").equals(temp.getString("title").toString())) {
                            jsonA.remove(i);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

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

                Intent intent = new Intent(EventTapped.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}
