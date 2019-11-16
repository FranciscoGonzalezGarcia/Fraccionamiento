package com.example.fraccionamiento.Activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import com.example.fraccionamiento.Activities.LoginActivity;
import com.example.fraccionamiento.Classes.UserClass;
import com.example.fraccionamiento.R;

import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdminActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView lblUser;
    private TextView lblEmail;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CircleImageView imgUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View navHeader = navigationView.getHeaderView(0);
        lblUser = navHeader.findViewById(R.id.lblNavAdminUser);
        lblEmail = navHeader.findViewById(R.id.lblNavAdminEmail);
        imgUser = navHeader.findViewById(R.id.imageNavAdminUser);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user==null){
            goToLogin();
        }

        // User Authentication
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClass userDatabase = dataSnapshot.getValue(UserClass.class);
                if(userDatabase.getName()!=null){
                    lblUser.setText(userDatabase.getName());
                }
                if(userDatabase.getEmail()!=null){
                    lblEmail.setText(userDatabase.getEmail());
                }
                if(userDatabase.getUrlImg()!=null){
                    Picasso.get().load(userDatabase.getUrlImg()).into(imgUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void goToLogin() {
        finish();
        Intent goToLogin = new Intent(this, LoginActivity.class);
        startActivity(goToLogin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            toggle.getToolbarNavigationClickListener();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void iconAction(MenuItem menuItem){
        // Properties for item selected
        menuItem.setChecked(true);

        switch(menuItem.getItemId()){
            case R.id.action_logout:
                logout();
                break;

        }
    }

    private void logout() {
        firebaseAuth.signOut();
        goToLogin();
    }
}
