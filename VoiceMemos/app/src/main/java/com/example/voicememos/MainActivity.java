// Chuong Viet Truong
// CMPS 121
// ASSIGNMENT 3
// MAY 30TH, 2018

package com.example.voicememos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    int numVoiceMemos;
    SharedPreferences shared;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ListView list = (ListView) findViewById(R.id.voicememos_listview);
        TextView text = (TextView) findViewById(R.id.nomemos_textview);

        // DEFAULT TEXTVIEW IS INVISIBLE
        text.setVisibility(View.INVISIBLE);

        // GET VALUE FROM SHARED PREFERENCES
        shared = getSharedPreferences("files", Context.MODE_PRIVATE);
        numVoiceMemos = shared.getInt("filesAmount", 0);

        if(numVoiceMemos != 0) {
            // ARRAY OF AUDIO RECORDINGS
            String[] audioRecordings = new String[numVoiceMemos];
            for(int i = 0; i < numVoiceMemos; i++) {
                audioRecordings[i] = "Audio Recording " + (i+1);
            }

            // SHOW LISTVIEW ITEMS IF ITEMS EXIST
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                    android.R.layout.simple_list_item_1, audioRecordings);
            list.setAdapter(adapter);

            // SET LISTENER WHEN ITEM CLICKED ON LIST
            final Context context = this;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                "Audio Recording "+(position+1)+".3gp");
                        mediaPlayer.prepare();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                }
            });
        }
        else{
            // IF NO FILES, SET LIST TO INVISIBLE AND TEXT TO VISIBLE
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
        }
    }

    // CREATE AN ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // HANDLE BUTTON ACTIVITIES
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addbutton) {
            Intent i = new Intent(MainActivity.this, AddMemo.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
