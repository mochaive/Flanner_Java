package com.implude.flanner_java;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.text.format.DateFormat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class SelectSubjectActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button koreanbutton, englishbutton, mathbutton, sciencebutton, socialbutton;
    Button unit1button, unit2button, unit3button;
    Button backbutton;
    Button timebutton;
    EditText textbookedittext, goaledittext;
    CheckBox alarmcheckBox;


    String subject = NULL, goal = NULL, unit = NULL, alarm = NULL, textbook = NULL;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);

        textbookedittext = (EditText) findViewById(R.id.edittext_textbook);
        goaledittext = (EditText) findViewById(R.id.edittext_goal);
        alarmcheckBox = (CheckBox) findViewById(R.id.checkbox_alarm);
        timebutton = (Button) findViewById(R.id.button_time);

        timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectSubjectActivity.this, SelectSubjectActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });


        backbutton = (Button) findViewById(R.id.button_back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textbook = textbookedittext.getText().toString();
                goal = goaledittext.getText().toString();

                if(subject.equals(NULL)) Toast.makeText(SelectSubjectActivity.this, "과목을 선택해주세요", Toast.LENGTH_SHORT).show();
                else if(textbook.equals(NULL)) Toast.makeText(SelectSubjectActivity.this, "교재를 선택해주세요", Toast.LENGTH_SHORT).show();
                else if(goal.equals(NULL)) Toast.makeText(SelectSubjectActivity.this, "목표를 설정해주세요", Toast.LENGTH_SHORT).show();
                else if(unit.equals(NULL)) Toast.makeText(SelectSubjectActivity.this, "단위를 선택해주세요", Toast.LENGTH_SHORT).show();
                else if(time == 0) Toast.makeText(SelectSubjectActivity.this, "시간을 설정해주세요", Toast.LENGTH_SHORT).show();
                else {
                    if (alarmcheckBox.isChecked()) {
                        alarm = "true";
                    } else {
                        alarm = "false";
                    }

                    Map<String, Object> user = new HashMap<>();
                    user.put("subject", subject);
                    user.put("textbook", textbook);
                    user.put("alarm", alarm);
                    user.put("goal", goal);
                    user.put("unit", unit);

                    db.collection("users").document("user_test1").collection("study").document(String.valueOf(time))
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

                    Toast.makeText(SelectSubjectActivity.this, "저장되었습니다", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        koreanbutton = (Button)findViewById(R.id.button_korean);
        koreanbutton.setOnClickListener(selectonClickListner);
        englishbutton = (Button)findViewById(R.id.button_english);
        englishbutton.setOnClickListener(selectonClickListner);
        mathbutton = (Button)findViewById(R.id.button_math);
        mathbutton.setOnClickListener(selectonClickListner);
        sciencebutton = (Button)findViewById(R.id.button_science);
        sciencebutton.setOnClickListener(selectonClickListner);
        socialbutton = (Button)findViewById(R.id.button_social);
        socialbutton.setOnClickListener(selectonClickListner);

        unit1button = (Button)findViewById(R.id.button_unit1);
        unit1button.setOnClickListener(selectonClickListner);
        unit2button = (Button)findViewById(R.id.button_unit2);
        unit2button.setOnClickListener(selectonClickListner);
        unit3button = (Button)findViewById(R.id.button_unit3);
        unit3button.setOnClickListener(selectonClickListner);

    }

    private Button.OnClickListener selectonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View view)
        {
            switch (view.getId()){
                //////////과목 버튼 // 국어 = 1, 영어 = 2, 수학 = 3, 과학 = 4, 사회 = 5, 한국사 = 6
                case R.id.button_korean:
                    Toast.makeText(SelectSubjectActivity.this, "국어", Toast.LENGTH_SHORT).show();
                    subject = "국어";
                    break;
                case R.id.button_english:
                    Toast.makeText(SelectSubjectActivity.this, "영어", Toast.LENGTH_SHORT).show();
                    subject = "영어";
                    break;
                case R.id.button_math:
                    Toast.makeText(SelectSubjectActivity.this, "수학", Toast.LENGTH_SHORT).show();
                    subject = "수학";
                    break;
                case R.id.button_science:
                    Toast.makeText(SelectSubjectActivity.this, "과학", Toast.LENGTH_SHORT).show();
                    subject = "과학";
                    break;
                case R.id.button_social:
                    Toast.makeText(SelectSubjectActivity.this, "사회", Toast.LENGTH_SHORT).show();
                    subject = "사회";
                    break;
                case R.id.button_koreanhistory:
                    Toast.makeText(SelectSubjectActivity.this, "한국사", Toast.LENGTH_SHORT).show();
                    subject = "한국사";
                    break;

                //////////단위 버튼 // 쪽 = 1, 단원 = 2, 강 = 3
                case R.id.button_unit1:
                    Toast.makeText(SelectSubjectActivity.this, "쪽", Toast.LENGTH_SHORT).show();
                    unit = "쪽";
                    break;

                case R.id.button_unit2:
                    Toast.makeText(SelectSubjectActivity.this, "단원", Toast.LENGTH_SHORT).show();
                    unit = "단원";
                    break;

                case R.id.button_unit3:
                    Toast.makeText(SelectSubjectActivity.this, "강", Toast.LENGTH_SHORT).show();
                    unit = "강";
                    break;

            }
        }
    };


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearFinal = year;
        monthFinal = month+1;
        dayFinal = dayOfMonth;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(SelectSubjectActivity.this, SelectSubjectActivity.this, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hourFinal = hourOfDay;
        minuteFinal = minute;
        Toast.makeText(SelectSubjectActivity.this, yearFinal + "년 " + monthFinal + "월 " + dayFinal  + "일 " + hourFinal + "시 " + minuteFinal + "분 부터 시작합니다", Toast.LENGTH_SHORT).show();
        yearFinal = yearFinal%100 * 100000000;
        monthFinal = monthFinal * 1000000;
        dayFinal = dayFinal * 10000;
        hourFinal = hourFinal * 100;

        time = yearFinal + monthFinal + dayFinal + hourFinal + minuteFinal;
    }


}
