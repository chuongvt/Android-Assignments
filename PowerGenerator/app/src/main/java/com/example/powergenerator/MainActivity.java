package com.example.powergenerator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public int sum2, sum3, sum4, sum5;
    public int exp2, exp3, exp4, exp5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        final EditText editText3 = (EditText) findViewById(R.id.editText3);
        final EditText editText4 = (EditText) findViewById(R.id.editText4);
        final EditText editText5 = (EditText) findViewById(R.id.editText5);

        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);

        // Event Listener for power of 2 button
        try {
            button2.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            String str2 = editText2.getText().toString();
                            if (!str2.isEmpty()) {
                                exp2 = Integer.parseInt(str2);
                                float sum2 = (float) Math.pow(2, exp2);
                                TextView answer2 = (TextView) findViewById(R.id.answer2);
                                answer2.setText(String.format("%.2f", sum2));
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Event Listener for power of 3 button
        try {
            button3.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            String str3 = editText3.getText().toString();
                            if (!str3.isEmpty()) {
                                exp3 = Integer.parseInt(str3);
                                float sum3 = (float) Math.pow(3, exp3);
                                TextView answer3 = (TextView) findViewById(R.id.answer3);
                                answer3.setText(String.format("%.2f", sum3));
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Event Listener for power of 4 button
        try {
            button4.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            String str4 = editText4.getText().toString();
                            if (!str4.isEmpty()) {
                                exp4 = Integer.parseInt(str4);
                                float sum4 = (float) Math.pow(4, exp4);
                                TextView answer4 = (TextView) findViewById(R.id.answer4);
                                answer4.setText(String.format("%.2f", sum4));
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Event Listener for power of 5 button
        try {
            button5.setOnClickListener(
                    new Button.OnClickListener() {
                        public void onClick(View v) {
                            String str5 = editText5.getText().toString();
                            if (!str5.isEmpty()) {
                                exp5 = Integer.parseInt(str5);
                                float sum5 = (float) Math.pow(5, exp5);
                                TextView answer5 = (TextView) findViewById(R.id.answer5);
                                answer5.setText(String.format("%.2f", sum5));
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
