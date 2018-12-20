package com.implude.flanner_java;

import android.accessibilityservice.FingerprintGestureController;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import io.github.yavski.fabspeeddial.FabSpeedDial;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class StudyTodoFragment extends Fragment {

    public static final int REQUEST_CODE_MENU = 1001;

    FabSpeedDial fabSpeedDial;
    String subject, unit, stralarm, textbook;
    String goal;
    int chk = -1;
    int time1, time2, time3;
    int a;

    TextView date1textview, date2textview, time1textview, time2textview, time3textview, textbook1textview, textbook2textview, textbook3textview, goal1textview, goal2textview, goal3textview;
    Button delete1button, delete2button, delete3button;
    Button getbutton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference studyRef = db.collection("users").document("user_test1").collection("study");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_study_todo,container,false);

        time1 = 1812251710;
        time2 = 1812251950;
        time3 = 1812302140;

        date1textview = (TextView)view.findViewById(R.id.textview_date1);
        date2textview = (TextView)view.findViewById(R.id.textview_date2);
        time1textview = (TextView)view.findViewById(R.id.textview_time1);
        time2textview = (TextView)view.findViewById(R.id.textview_time2);
        time3textview = (TextView)view.findViewById(R.id.textview_time3);
        textbook1textview = (TextView)view.findViewById(R.id.textview_textbook1);
        textbook2textview = (TextView)view.findViewById(R.id.textview_textbook2);
        textbook3textview = (TextView)view.findViewById(R.id.textview_textbook3);
        goal1textview = (TextView)view.findViewById(R.id.textview_goal1);
        goal2textview = (TextView)view.findViewById(R.id.textview_goal2);
        goal3textview = (TextView)view.findViewById(R.id.textview_goal3);
        delete1button = (Button)view.findViewById(R.id.button_delete9);
        delete2button = (Button)view.findViewById(R.id.button_delete10);
        delete3button = (Button)view.findViewById(R.id.button_delete11);
        getbutton = (Button)view.findViewById(R.id.button_get);

        delete3button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent longintent = new Intent(getActivity(), LongPressActivity.class);
                startActivityForResult(longintent, REQUEST_CODE_MENU);
                if(a == 1) {
                    date2textview.setText(" ");
                    time3textview.setText(" ");
                    textbook3textview.setText(" ");
                    goal3textview.setText(" ");
                    delete3button.setVisibility(View.INVISIBLE);
                    chk = 3;
                    Toast.makeText(getActivity(), "삭제 완료", Toast.LENGTH_SHORT).show();
                }
                a = 0;
            }
        });

        getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chk<=0) {
                    Toast.makeText(getActivity(), "입력하시고 새로고침해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(chk == 1){
                    Get();
                    Toast.makeText(getActivity(), "더블클릭해주세요", Toast.LENGTH_SHORT).show();

                    date1textview.setText("20"+time1 / 100000000 + "/" + (time1 % 100000000) / 1000000 + "/" + (time1 % 1000000 / 10000));
                    time1textview.setText((time1 % 10000) / 100 + ":" + (time1 % 100));
                    textbook1textview.setText("" + textbook);
                    goal1textview.setText("" + goal + unit);
                    delete1button.setVisibility(View.VISIBLE);
                }
                else if(chk == 3){
                    Get();
                    Toast.makeText(getActivity(), "더블클릭해주세요", Toast.LENGTH_SHORT).show();

                    time2textview.setText((time2 % 10000) / 100 + ":" + (time2 % 100));
                    textbook2textview.setText("" + textbook);
                    goal2textview.setText("" + goal + unit);
                    delete2button.setVisibility(View.VISIBLE);
                }
                else if(chk == 5){
                    Get();
                    Toast.makeText(getActivity(), "더블클릭해주세요", Toast.LENGTH_SHORT).show();

                    date2textview.setText("20"+time3 / 100000000 + "/" + (time3 % 100000000) / 1000000 + "/" + (time3 % 1000000 / 10000));
                    time3textview.setText((time3 % 10000) / 100 + ":" + (time3 % 100));
                    textbook3textview.setText("" + textbook);
                    goal3textview.setText("" + goal + unit);
                    delete3button.setVisibility(View.VISIBLE);
                }
            }
        });


        fabSpeedDial = (FabSpeedDial)view.findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.float_study:
                        Intent selectsubjectintent = new Intent(getActivity(), SelectSubjectActivity.class);
                        startActivity(selectsubjectintent);
                        chk+=2;
                        break;
                    case R.id.float_todo:
                        Intent selecttodointent = new Intent(getActivity(), SelectTodoActivity.class);
                        startActivity(selecttodointent);
                        break;
                }
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });
        return view;
    }

    @SuppressLint("SetTextI18n")
    public void Get(){


        if(chk == 1) {
            DocumentReference docRef = db.collection("users").document("user_test1").collection("study").document(String.valueOf(time1));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            subject = document.getString("subject");
                            unit = document.getString("unit");
                            textbook = document.getString("textbook");
                            goal = document.getString("goal");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
        else if(chk == 3){
            DocumentReference docRef = db.collection("users").document("user_test1").collection("study").document(String.valueOf(time2));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            subject = document.getString("subject");
                            unit = document.getString("unit");
                            stralarm = document.getString("alarm");
                            textbook = document.getString("textbook");
                            goal = document.getString("goal");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        else if(chk == 5){
            DocumentReference docRef = db.collection("users").document("user_test1").collection("study").document(String.valueOf(time3));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            subject = document.getString("subject");
                            unit = document.getString("unit");
                            stralarm = document.getString("alarm");
                            textbook = document.getString("textbook");
                            goal = document.getString("goal");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            a = data.getExtras().getInt("num");
        }
    }
}
