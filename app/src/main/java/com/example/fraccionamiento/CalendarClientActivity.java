package com.example.fraccionamiento;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.fraccionamiento.Activities.Admin.EditUserActivity;
import com.example.fraccionamiento.R;

public class CalendarClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_client);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        CalendarClientFragment fragment = new CalendarClientFragment();
        fragmentTransaction.add(R.id.calendar_list, fragment);
        fragmentTransaction.commit();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(getString(R.string.menu_calendar));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else {
            Toast.makeText(CalendarClientActivity.this,getString(R.string.fatal_error), Toast.LENGTH_LONG).show();
            finish();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
