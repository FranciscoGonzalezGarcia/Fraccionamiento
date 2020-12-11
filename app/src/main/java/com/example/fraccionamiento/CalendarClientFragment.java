package com.example.fraccionamiento;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fraccionamiento.Activities.Admin.AddNewCalendarActivity;
import com.example.fraccionamiento.Adapters.AdapterCalendarClient;
import com.example.fraccionamiento.Classes.CalendarClass;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CalendarClientFragment extends Fragment {
    private ArrayList<CalendarClass> calendarList;
    private final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private TextView lblNoFoundCalendar;
    private RecyclerView recyclerView;
    private AdapterCalendarClient adapterCalendarClient;

    public CalendarClientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_client, container, false);
        // Inflate the layout for this fragment
        lblNoFoundCalendar = view.findViewById(R.id.lblNotCalendarFound);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.rcVwCalendar);
        recyclerView.setLayoutManager(linearLayoutManager);
        calendarList = new ArrayList<>();
        adapterCalendarClient = new AdapterCalendarClient(calendarList);
        recyclerView.setAdapter(adapterCalendarClient);
        dbRef.child(FirebaseClass.CALENDAR).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CalendarClass calendarClass = snapshot.getValue(CalendarClass.class);
                    calendarList.add(calendarClass);

                    adapterCalendarClient.notifyDataSetChanged();

                    if (adapterCalendarClient.getItemCount()==0){
                        lblNoFoundCalendar.setVisibility(View.VISIBLE);
                    }
                    else {
                        lblNoFoundCalendar.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}
