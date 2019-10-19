package com.example.fraccionamiento.Controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConnectionController {
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fDB;
    private DatabaseReference dbRef;
    private boolean auth = false;
    private boolean notEmptyUser = false ;
    private boolean notEmptyDatabase = false;
    private String uid;
    private String rol;

    public FirebaseConnectionController() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth!=null){
            auth = true;
            fUser = mAuth.getCurrentUser();
            if(fUser!=null){
                notEmptyUser = true;
                fDB = FirebaseDatabase.getInstance();
                if(fDB!=null) {
                    notEmptyDatabase = true;
                    uid = fUser.getUid();
                    dbRef = fDB.getReference("users/"+uid);
                    rol = dbRef.child("rol").toString();

                }
            }
        }

    }

    public boolean isAuth() {
        return auth;
    }

    public boolean isNotEmptyUser() {
        return notEmptyUser;
    }

    public boolean isNotEmptyDatabase() {
        return notEmptyDatabase;
    }

    public DatabaseReference getDbRef() {
        return dbRef;
    }

    public String getUid() {
        return uid;
    }

    public String getRol() {
        return rol;
    }
}
