package com.implude.flanner_java;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class SelectTodoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText todoedittext;
    Button timebutton;
    Button backbutton;
    CheckBox alarmcheckBox;

    String todo = NULL, alarm = NULL, time = NULL;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    int time_todo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_todo);

        todoedittext = (EditText) findViewById(R.id.edittext_todo);
        timebutton = (Button) findViewById(R.id.button_time);
        backbutton = (Button) findViewById(R.id.button_back);
        alarmcheckBox = (CheckBox) findViewById(R.id.checkbox_alarm);

        timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectTodoActivity.this, SelectTodoActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });


        backbutton = (Button) findViewById(R.id.button_back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo = todoedittext.getText().toString();

                if(todo.equals(NULL)) Toast.makeText(SelectTodoActivity.this, "할일을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if(time_todo == 0) Toast.makeText(SelectTodoActivity.this, "시간을 설정해주세요", Toast.LENGTH_SHORT).show();
                else {
                    if (alarmcheckBox.isChecked()) {
                        alarm = "true";
                    } else {
                        alarm = "false";
                    }

                    Map<String, Object> user = new HashMap<>();
                    user.put("todo", todo);
                    user.put("alarm", alarm);

                    db.collection("users").document("user_test1").collection("todo").document(String.valueOf(time_todo))
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                    Toast.makeText(SelectTodoActivity.this, "저장되었습니다", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month+1;
        dayFinal = dayOfMonth;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(SelectTodoActivity.this, SelectTodoActivity.this, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        Toast.makeText(SelectTodoActivity.this, yearFinal + "년 " + monthFinal + "월 " + dayFinal  + "일 " + hourFinal + "시 " + minuteFinal + "분 부터 시작합니다", Toast.LENGTH_SHORT).show();
        yearFinal = yearFinal%100 * 100000000;
        monthFinal = monthFinal * 1000000;
        dayFinal = dayFinal * 10000;
        hourFinal = hourFinal * 100;

        time_todo = yearFinal + monthFinal + dayFinal + hourFinal + minuteFinal;
    }

}
