package com.example.fraccionamiento.Activities.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fraccionamiento.Adapters.AdapterNotifications;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.example.fraccionamiento.Classes.NotificationClass;
import com.example.fraccionamiento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class NotificationsActivity extends AppCompatActivity {
    private ArrayList<NotificationClass> listNotifications;
    private DatabaseReference dbRef;
    private TextView lblNoFoundNotifications;
    private AdapterNotifications adpNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
//        Toolbar toolbar = findViewById(R.id.toolbarActivity);
//        setSupportActionBar(toolbar);

        lblNoFoundNotifications = findViewById(R.id.lblNoFoundNotifications);


        // Title and Up
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(getString(R.string.notification));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView rcVwNotifications = findViewById(R.id.recyclerNotifications);

        listNotifications = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        dbRef = FirebaseDatabase.getInstance().getReference().child(FirebaseClass.NOTIFICATIONS).child(user.getUid());

        adpNotifications = new AdapterNotifications(listNotifications);

        // Recycler options
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,linearLayoutManager.getOrientation());

        rcVwNotifications.setLayoutManager(linearLayoutManager);
        rcVwNotifications.addItemDecoration(itemDecoration);


        rcVwNotifications.setAdapter(adpNotifications);


        // Get data from fireBase
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listNotifications.removeAll(listNotifications);
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    NotificationClass notification = snapshot.getValue(NotificationClass.class);
                    listNotifications.add(notification);

                }
                adpNotifications.notifyDataSetChanged();


                // Show message if don't exits data

                if (adpNotifications.getItemCount()==0){
                    lblNoFoundNotifications.setVisibility(View.VISIBLE);
                }else {
                    lblNoFoundNotifications.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemChecked:
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            NotificationClass notification = snapshot.getValue(NotificationClass.class);
                            Map<String, Object> notificationUpdate = notification.toMap();
                            notificationUpdate.put("checked", true);
                            snapshot.getRef().updateChildren(notificationUpdate);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//            case R.id.menuItemClearAll:
//                dbRef.removeValue();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
