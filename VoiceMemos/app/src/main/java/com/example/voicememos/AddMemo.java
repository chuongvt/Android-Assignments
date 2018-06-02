package com.example.voicememos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddMemo extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    public static final int RequestPermissionCode = 1;
    static final public String TAG = "TAG";
    String currentFileName = null, AudioSavePathInDevice = null;
    int numVoiceMemos;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memo);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());

        // ENABLE JAVASCRIPT FOR WEBVIEW
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // BINDS JAVASCRIPT INTERFACE
        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        myWebView.loadUrl("https://users.soe.ucsc.edu/~dustinadams/CMPS121/assignment3/www/index.html");
    }

    // INSTANTIATE THE INTERFACE AND SET THE CONTEXT
    public class JavaScriptInterface {

        // CONTEXT IS USEFUL FOR THINGS LIKE ACCESSING PREFERENCES
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void record() {
            Log.i(TAG, "I am in the JavaScript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    // CODE
                    // GET VALUE FROM SHARED PREFERENCES AND ADD ONE
                    shared = getSharedPreferences("files", Context.MODE_PRIVATE);
                    numVoiceMemos = shared.getInt("filesAmount", 0);
                    numVoiceMemos++;

                    currentFileName = "Audio Recording "+numVoiceMemos+".3gp";

                    if(checkPermission()) {
                        // THE PATH WE'LL BE SAVING THE FILE TO. NOTICE IT IS FROM THE EXTERNAL
                        // STORAGE DIRECTORY
                        AudioSavePathInDevice =
                                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                        currentFileName;

                        MediaRecorderReady();

                        try {
                            // RECORDING STARTS
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                        }
                        catch(IllegalStateException e) {
                            e.printStackTrace();
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        requestPermission();
                        record();
                    }

                    Toast.makeText(AddMemo.this, "Recording", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void stop() {
            Log.i(TAG, "I am in the JavaScript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    // CODE
                    mediaRecorder.stop();

                    final SharedPreferences.Editor editor = shared.edit();

                    // GET VALUE FROM SHARED PREFERENCES, ADD ONE, AND PUT IT BACK IN
                    shared = getSharedPreferences("files", Context.MODE_PRIVATE);
                    numVoiceMemos = shared.getInt("filesAmount", 0);
                    numVoiceMemos++;

                    editor.putInt("filesAmount", numVoiceMemos);
                    editor.commit();


                    Toast.makeText(AddMemo.this, "Stopping", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void play() {
            Log.i(TAG, "I am in the JavaScript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    // CODE
                    // OBJECT TO PLAY THE AUDIO
                    mediaPlayer = new MediaPlayer();

                    try {
                        mediaPlayer.setDataSource(AudioSavePathInDevice);
                        mediaPlayer.prepare();
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();

                    Toast.makeText(AddMemo.this, "Playing", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void stoprec() {
            Log.i(TAG, "I am in the JavaScript call.");
            runOnUiThread(new Runnable() {
                public void run() {
                    // CODE
                    if(mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        MediaRecorderReady();
                    }

                    Toast.makeText(AddMemo.this, "Stop recording", Toast.LENGTH_LONG).show();
                }
            });
        }

        @JavascriptInterface
        public void exit() {
            runOnUiThread(new Runnable() {
                public void run(){
                    Intent i = new Intent(AddMemo.this, MainActivity.class);
                    i.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }
    }

    // INITIALIZE RECORDER OBJECT
    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    // PERMISSIONS FROM USER
    private void requestPermission() {
        ActivityCompat.requestPermissions(AddMemo.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    // CALLBACK METHOD
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case RequestPermissionCode:
                if(grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if(StoragePermission && RecordPermission) {
                        Toast.makeText(AddMemo.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(AddMemo.this, "Permission Denied",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}
