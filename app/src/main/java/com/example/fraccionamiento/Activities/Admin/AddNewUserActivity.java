package com.example.fraccionamiento.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fraccionamiento.Activities.LoginActivity;
import com.example.fraccionamiento.Classes.UserClass;
import com.example.fraccionamiento.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddNewUserActivity extends AppCompatActivity {
    private TextView name, lastname, email, pass, confirmPass;
    private Button btnAddUser;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    private String nameS, lastnameS, emailS, passS, confirmPassS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        ActionBar mActionBar = getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setTitle(getString(R.string.add_new_user));
        }

        name = findViewById(R.id.txtAddName);
        lastname = findViewById(R.id.txtAddLastName);
        email = findViewById(R.id.txtAddEmail);
        pass = findViewById(R.id.txtAddPass);
        confirmPass = findViewById(R.id.txtAddConfirmPass);

        btnAddUser = findViewById(R.id.btnAddUser);

        mAuth = FirebaseAuth.getInstance();

        mref = FirebaseDatabase.getInstance().getReference().child("users");

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameS = name.getText().toString();
                emailS = email.getText().toString();
                passS = pass.getText().toString();
                lastnameS = lastname.getText().toString();
                confirmPassS = confirmPass.getText().toString();

                if(areCorrectFields()){
                    if(passMatch()){
                        mAuth.createUserWithEmailAndPassword(emailS, passS).addOnCompleteListener(AddNewUserActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    user = mAuth.getCurrentUser();
                                    Map<String, UserClass> users = new HashMap<>();
                                    users.put(user.getUid(), new UserClass(nameS, emailS, lastnameS, "client", "url"));
                                    mref.setValue(users);
                                    Toast.makeText(AddNewUserActivity.this, getString(R.string.successfull_user_add), Toast.LENGTH_LONG).show();
                                    mAuth.signOut();
                                    finish();
                                    Intent intent = new Intent(AddNewUserActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                }else {
                                    Toast.makeText(AddNewUserActivity.this, getString(R.string.already_user), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }else {
                        Toast.makeText(AddNewUserActivity.this, getString(R.string.pass_not_match), Toast.LENGTH_LONG).show();
                    }

                }
            }
        });





    }

    private boolean passMatch() {
        if(passS.equals(confirmPassS)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean areCorrectFields(){
        if(nameS.isEmpty()){
            // Si el campo de correo esta vacío
            // Arrojamos el error de campo correo vacío
            name.setError(getString(R.string.error_empty_field));
            // retornamos el valor de falla en la condición
            return false;

        }
        else if(lastnameS.isEmpty()){
            // Si el campo de contrasña esta vacío
            // Arrojamos el error de campo contraseña vacío
            lastname.setError(getString(R.string.error_empty_field));
            // retornamos el valor de falla en la condición
            return false;

        }else if(passS.isEmpty()){
            // Si el campo de contrasña esta vacío
            // Arrojamos el error de campo contraseña vacío
            pass.setError(getString(R.string.error_empty_field));
            // retornamos el valor de falla en la condición


            return false;

        }else if(passS.length()<7){
            pass.setError(getString(R.string.minor_7));
            return false;

        }else if(confirmPassS.isEmpty()){
            // Si el campo de contrasña esta vacío
            // Arrojamos el error de campo contraseña vacío
            confirmPass.setError(getString(R.string.error_empty_field));
            // retornamos el valor de falla en la condición
            return false;

        }
        else {
            // retornamos el valor de cumplimiento en la condición
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_close:
                finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_close, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
