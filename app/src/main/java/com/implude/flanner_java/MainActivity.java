package com.implude.flanner_java;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout myDrawer;
    private ActionBarDrawerToggle myToggle;
    NavigationView navigationView;

    private long Back_Key_before_Time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDrawer = (DrawerLayout)findViewById(R.id.myDrawer);
        myToggle = new ActionBarDrawerToggle(this, myDrawer, R.string.open, R.string.close);

        myDrawer.addDrawerListener(myToggle);
        myToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyTodoFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_study_todo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_study_todo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudyTodoFragment()).commit();
                break;
            case R.id.nav_analysis:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AnalysisFragment()).commit();
                break;
            case R.id.nav_option:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OptionFragment()).commit();
                break;
        }

        myDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        long now = System.currentTimeMillis();

        long result = now - Back_Key_before_Time;

        if(result < 2000) {
            this.finish();
        } else {
            Toast.makeText(this,"한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
            Back_Key_before_Time = System.currentTimeMillis();
        }
    } //뒤로가기
}
