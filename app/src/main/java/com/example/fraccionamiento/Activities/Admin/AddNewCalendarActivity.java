package com.example.fraccionamiento.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.fraccionamiento.Classes.CalendarClass;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.example.fraccionamiento.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class AddNewCalendarActivity extends AppCompatActivity {

    private EditText txtTitle, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_calendar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null){
            actionBar.setTitle(R.string.add_calendar);
        }

        final Spinner spnHour1 = findViewById(R.id.spnHour1);
        final Spinner spnHourSystem = findViewById(R.id.spnHourSystem);
        final Spinner spnHour2 = findViewById(R.id.spnHour2);
        final Spinner spnMinute = findViewById(R.id.spnMin1);
        final Spinner spnDay = findViewById(R.id.spnDay);
        final Spinner spnMonth = findViewById(R.id.spnMonth);
        final Spinner spnYear = findViewById(R.id.spnYear);
        txtTitle = findViewById(R.id.txtAddCalendarTitle);
        txtDescription = findViewById(R.id.txtAddCalendarDescription);


        final ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.hours_1, android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.hours_2, android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.hours_3, android.R.layout.simple_spinner_dropdown_item);
        Button btnCalendar = findViewById(R.id.btnAddCalendar);

        spnHour1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    spnHour2.setAdapter(adapter2);
                }else {
                    spnHour2.setAdapter(adapter3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(areCorrectFields()){
                    FirebaseDatabase fDB = FirebaseDatabase.getInstance();
                    DatabaseReference mRef = fDB.getReference().child(FirebaseClass.CALENDAR);
                    final DatabaseReference pushRef = mRef.push();

                    CalendarClass calendar = new CalendarClass(txtTitle.getText().toString(), txtDescription.getText().toString(), spnHour1.getSelectedItem().toString(), spnHour2.getSelectedItem().toString(), spnMinute.getSelectedItem().toString()+"0", spnDay.getSelectedItem().toString(), spnMonth.getSelectedItem().toString(), spnYear.getSelectedItem().toString(), spnHourSystem.getSelectedItem().toString());
                    Map<String, Object> calendarMap = calendar.toMap();

                    pushRef.setValue(calendarMap);
                    fDB.getReference().child(FirebaseClass.USERS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                String uid = snapshot.getKey();
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = new Date(System.currentTimeMillis());
                                Map<String, Object> notification = new HashMap<>();
                                String notify = getString(R.string.new_notification) + "\n\n\t" + txtTitle.getText().toString();
                                notification.put("notification",notify);
                                notification.put("date", formatter.format(date));
                                notification.put("date", formatter.format(date));
                                notification.put("checked", false);
                                DatabaseReference nRef = FirebaseDatabase.getInstance().getReference().child(FirebaseClass.NOTIFICATIONS).child(uid).child(pushRef.getKey());
                                nRef.setValue(notification);

                            }
                            onBackPressed();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });
    }

    private boolean areCorrectFields() {
        if(txtTitle.getText().toString().isEmpty()){
            txtTitle.setError(getString(R.string.error_empty_field));
            return false;
        }else if(txtDescription.getText().toString().isEmpty()){
            txtDescription.setError(getString(R.string.error_empty_field));
            return false;
        }else {
            return true;
        }
    }
}
