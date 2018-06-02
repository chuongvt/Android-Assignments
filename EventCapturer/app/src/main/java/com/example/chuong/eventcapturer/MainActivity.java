package com.example.chuong.eventcapturer;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public JSONArray jsonA = null;
    public JSONObject jsonO = null;
    String json = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();

        ListView list = (ListView) findViewById(R.id.eventlist_listview);
        TextView text = (TextView) findViewById(R.id.noevents_textview);
        text.setVisibility(View.INVISIBLE);

        // Read file that exists
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

            // Show List
            final ArrayList<ListData> eventList = new ArrayList<ListData>();
            for (int i = 0; i < jsonA.length(); i++) {
                ListData ld = new ListData();
                try {
                    ld.title = jsonA.getJSONObject(i).getString("title");
                    ld.time = jsonA.getJSONObject(i).getString("time");
                    ld.date = jsonA.getJSONObject(i).getString("date");
                    ld.gps = jsonA.getJSONObject(i).getString("gps");
                    ld.description = jsonA.getJSONObject(i).getString("description");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                eventList.add(ld);
            }

            // Array of ListData object titles
            String[] listItems = new String[eventList.size()];

            for (int i = 0; i < eventList.size(); i++) {
                ListData listD = eventList.get(i);
                listItems[i] = listD.title;
            }

            // Show ListView if items exist
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
            list.setAdapter(adapter);

            // Set listener when item clicked on list
            final Context context = this;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListData selected = eventList.get(position);

                    Intent eventTapped = new Intent(context, EventTapped.class);


                    eventTapped.putExtra("title", selected.title);
                    eventTapped.putExtra("time", selected.time);
                    eventTapped.putExtra("date", selected.date);
                    eventTapped.putExtra("gps", selected.gps);
                    eventTapped.putExtra("description", selected.description);


                    startActivity(eventTapped);
                }
            });
        }
        catch (IOException e) {
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);

            text.setVisibility(View.VISIBLE);
        }
    }

    // Create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addeventbutton) {
            Intent i = new Intent(this, AddEvent.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



