package com.implude.flanner_java;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class LongPressActivity extends AppCompatActivity {

    Button deletebutton;
    Button cancelbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_long_press);
        deletebutton = (Button)findViewById(R.id.button_delete);
        cancelbutton = (Button)findViewById(R.id.button_cancel);

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backintent = new Intent();
                backintent.putExtra("num", 1);
                setResult(RESULT_OK, backintent);

                finish();
            }
        });
    }
}
