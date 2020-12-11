package com.example.fraccionamiento.Activities.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fraccionamiento.Activities.LoginActivity;
import com.example.fraccionamiento.CalendarClientActivity;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.example.fraccionamiento.Classes.NotificationClass;
import com.example.fraccionamiento.Classes.UserClass;
import com.example.fraccionamiento.ProfileActivity;
import com.example.fraccionamiento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    // Declaración de variables
    private FirebaseAuth firebaseAuth;
    private TextView txtPayDate, txtQuantityPay;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ImageView imgVwIconPayStatus;
    private final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mRefUser;
    private DatabaseReference mRefPaymentInfo;
    private ValueEventListener valueEventListenerPayInfo;
    private Boolean flagNotifications = false;

    // Creamos la vista de la aplicación y asignamos las respectivas vistas a los objetos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar toolbar = getSupportActionBar();
        if(toolbar!=null){
            toolbar.setTitle(R.string.home_page);
        }
        txtPayDate = findViewById(R.id.txtPayDate);
        txtQuantityPay = findViewById(R.id.txtQuantityPay);

        Date date = new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
        cal.setTime(date);
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);

        // Creamos las instancias a las bases de datos
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        mRef.child(FirebaseClass.USERS).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClass userClass = dataSnapshot.getValue(UserClass.class);
                int rentTotal = userClass.getRent()+userClass.getMaintenance();
                txtQuantityPay.setText(String.valueOf(rentTotal));
                int realMonth;
                if(userClass.getPayDay()>=day){
                    realMonth = month + 1;
                }else {
                    realMonth = month +2;
                }

                String limitDay;
                if(realMonth<12){
                    limitDay = userClass.getPayDay() + " / " + realMonth + " / " + year;
                }else {
                    limitDay = userClass.getPayDay() + " / " + 1 + " / " + (year+1);
                }

                txtPayDate.setText(limitDay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // Creamos el menu de iconos
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        areThereNotifications();
        return true;
    }

    private void areThereNotifications() {
       mRef.child(FirebaseClass.NOTIFICATIONS).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    NotificationClass notification = snapshot.getValue(NotificationClass.class);
                    if(notification.getChecked()){
                        flagNotifications = false;
                        invalidateOptionsMenu();
                    }else {
                        flagNotifications = true;
                        invalidateOptionsMenu();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(flagNotifications){
            menu.findItem(R.id.action_notifications).setIcon(R.drawable.ic_notifications_active);
            flagNotifications = false;
        }else{
            menu.findItem(R.id.action_notifications).setIcon(R.drawable.ic_notifications);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        areThereNotifications();
        super.onResume();
    }

    // funcion para determinar accion tras presionar iconos
    public void iconAction(MenuItem menuItem){
        // Properties for item selected
        menuItem.setChecked(true);

        switch (menuItem.getItemId()){
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_notifications:
                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_profile:
                Intent intent3 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent3);
                break;
            case R.id.action_calendar:
                Intent intent1 = new Intent(MainActivity.this, CalendarClientActivity.class);
                startActivity(intent1);
                break;
        }
    }

    // metodo de deslogeo
    private void logout() {
        firebaseAuth.signOut();
        goToLogin();


    }

    // Ir a login tras deslogearse
    private void goToLogin() {
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }


    @Override
    public void onBackPressed() {
        logout();
    }

    @Override
    public boolean onSupportNavigateUp() {
        logout();
        return false;
    }
}
