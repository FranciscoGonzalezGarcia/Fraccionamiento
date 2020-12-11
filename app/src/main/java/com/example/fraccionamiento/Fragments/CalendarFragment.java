package com.example.fraccionamiento.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fraccionamiento.Activities.Admin.AddNewCalendarActivity;
import com.example.fraccionamiento.Activities.Admin.AddNewUserActivity;
import com.example.fraccionamiento.Adapters.AdapterCalendar;
import com.example.fraccionamiento.AddUserAdminActivity;
import com.example.fraccionamiento.Classes.CalendarClass;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.example.fraccionamiento.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalendarFragment extends Fragment {
    private ArrayList<CalendarClass> calendarList;
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference dbCalendarRef;
    private TextView lblNoFoundCalendar;
    private RecyclerView recyclerView;
    private AdapterCalendar adapterCalendar;
    private ValueEventListener valueEventListenerCalendar;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        lblNoFoundCalendar = view.findViewById(R.id.lblNotCalendarFound);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.rcVwCalendar);
        recyclerView.setLayoutManager(linearLayoutManager);
        calendarList = new ArrayList<>();
        adapterCalendar = new AdapterCalendar(calendarList);
        recyclerView.setAdapter(adapterCalendar);

        FloatingActionButton fabAddCalendar = view.findViewById(R.id.fabAddNewCalendar);

        fabAddCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddNewCalendarActivity.class);
                startActivity(intent);
            }
        });

        valueEventListenerCalendar = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                calendarList.removeAll(calendarList);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CalendarClass calendarClass = snapshot.getValue(CalendarClass.class);
                    calendarClass.setKey(snapshot.getKey());
                    calendarList.add(calendarClass);
                }

                adapterCalendar.notifyDataSetChanged();

                if (adapterCalendar.getItemCount()==0){
                    lblNoFoundCalendar.setVisibility(View.VISIBLE);
                }
                else {
                    lblNoFoundCalendar.setVisibility(View.INVISIBLE);
                }

                adapterCalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = recyclerView.getChildLayoutPosition(v);
                        String calendarKey = calendarList.get(position).getKey();
                        AlertDialog alertDialog = crateAlertDialog(getString(R.string.delete_calendar_mensaje),calendarKey );
                        alertDialog.show();

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        dbCalendarRef = dbRef.child(FirebaseClass.CALENDAR);
        dbCalendarRef.addValueEventListener(valueEventListenerCalendar);
        // Inflate the layout for this fragment
        return view;
    }

    private void deleteEvent(final String calenKey) {
        DatabaseReference calendarRef = dbRef.child(FirebaseClass.CALENDAR).child(calenKey);
        final DatabaseReference notifyRef = dbRef.child(FirebaseClass.NOTIFICATIONS);
        calendarRef.removeValue();
        notifyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String refUser = snapshot.getKey();
                    notifyRef.child(refUser).child(calenKey).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private AlertDialog crateAlertDialog(String message, final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteEvent(key);

            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();

    }

    @Override
    public void onPause() {
        super.onPause();
        dbCalendarRef.removeEventListener(valueEventListenerCalendar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbCalendarRef.removeEventListener(valueEventListenerCalendar);
    }

    @Override
    public void onResume() {
        super.onResume();
        dbCalendarRef.addValueEventListener(valueEventListenerCalendar);
    }
}
