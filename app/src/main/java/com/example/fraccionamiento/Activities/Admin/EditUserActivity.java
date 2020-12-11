package com.example.fraccionamiento.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fraccionamiento.Activities.Client.ReceiptsActivity;
import com.example.fraccionamiento.Activities.LoginActivity;
import com.example.fraccionamiento.Adapters.AdapterPayments;
import com.example.fraccionamiento.Adapters.AdapterPaymentsAdmin;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.example.fraccionamiento.Classes.PaymentsClass;
import com.example.fraccionamiento.Classes.UserClass;
import com.example.fraccionamiento.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {
    // Declaramos las variables
    private TextView name, lastName, email, rent, maintenance, build, dept, day;
    private DatabaseReference mRef;
//    private ValueEventListener valueEventListenerReceipts;
//    private DatabaseReference paymentsRef;
//    private ArrayList<PaymentsClass> payments;
//    private AdapterPaymentsAdmin adapterPayments;
    private TextView lblNotPaymentsFound;
    private String uid;
//    private DatabaseReference ref;
//    private ValueEventListener valueEventListenerPayments;
//    private int debtCount;
    private ArrayList<UserClass> user;
    private String nameS, lastNameS, emailS, pass;
    private Button btnSave, btnDelete;
    private AlertDialog alertDialog;
    ProgressDialog prgDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        name = findViewById(R.id.txtEditName);
        lastName = findViewById(R.id.txtEditUserLastName);
        email = findViewById(R.id.txtEditEmail);
        rent = findViewById(R.id.txtEditRent);
        maintenance = findViewById(R.id.txtEditMaintenance);
        build = findViewById(R.id.txtEditBuild);
        dept = findViewById(R.id.txtEditDept);
        day = findViewById(R.id.txtEditDay);
        btnSave = findViewById(R.id.btnSaveUser);
        btnDelete = findViewById(R.id.btnDeleteUser);
        prgDlg = new ProgressDialog(EditUserActivity.this);

        rent.setEnabled(false);
        maintenance.setEnabled(false);
        build.setEnabled(false);
        dept.setEnabled(false);
        day.setEnabled(false);
        email.setEnabled(false);

        nameS = "";
        lastNameS = "";
        emailS = "";



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authUser();
            }
        });

        lblNotPaymentsFound = findViewById(R.id.lblNotPaymentsFound);
//      final RecyclerView rcVwPayments = findViewById(R.id.rcVwPayments);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(getString(R.string.user_info));
        }else {
            Toast.makeText(EditUserActivity.this,getString(R.string.fatal_error), Toast.LENGTH_LONG).show();
            finish();
        }

        Bundle bundle = getIntent().getExtras();

        // almacenamos los datos envíados por la actividad principal

        if(bundle!=null){
            uid = bundle.getString("uid");

        }else {
            Toast.makeText(EditUserActivity.this,getString(R.string.fatal_error), Toast.LENGTH_LONG).show();
            finish();
        }

        // Creamos una instancia a la base de datos para traer los pagos del usuario solicitado
        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.child(FirebaseClass.USERS).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserClass userClass = dataSnapshot.getValue(UserClass.class);

                    nameS = userClass.getName();
                    name.setText(nameS);
                    lastNameS = userClass.getLastName();
                    lastName.setText(lastNameS);
                    emailS = userClass.getEmail();
                    email.setText(emailS);
                    rent.setText(String.valueOf(userClass.getRent()));
                    maintenance.setText(String.valueOf(userClass.getMaintenance()));
                    build.setText(userClass.getBuild());
                    dept.setText(String.valueOf(userClass.getDeptNum()));
                    day.setText(String.valueOf(userClass.getPayDay()));
                    pass = userClass.getPass();
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(FirebaseClass.USERS).child(uid);
                            userRef.child("name").setValue(name.getText().toString());
                            userRef.child("lastName").setValue(lastName.getText().toString());
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void authUser(){

        AlertDialog.Builder builderAlert = new AlertDialog.Builder(this);
        builderAlert.setTitle(getString(R.string.atention)).setMessage(getString(R.string.delete_user_message)).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog prgDlg = new ProgressDialog(EditUserActivity.this);
                // Asignamos el mensaje
                prgDlg.setMessage(getString(R.string.deleting_user));
                // Mostramos la ventana
                prgDlg.show();
                final FirebaseAuth fBAuth = FirebaseAuth.getInstance();
                // Tratamos de iniciar sesión con los datos introdcidos por el usuario
                fBAuth.signInWithEmailAndPassword(emailS, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Asignamos el mensaje
                                prgDlg.setMessage(getString(R.string.deleting_user));
                                // Mostramos la ventana
                                prgDlg.show();

                                // Iniciamos una instancia de autenticacion con firebase
                                final FirebaseAuth fBAuth = FirebaseAuth.getInstance();
                                // Tratamos de iniciar sesión con los datos introdcidos por el usuario
                                fBAuth.signInWithEmailAndPassword(emailS, pass)
                                        .addOnCompleteListener(EditUserActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Al iniciar sesión, vamos a la Activity principal
                                                    FirebaseUser user = fBAuth.getCurrentUser();
                                                    if(user != null){
                                                        deleteUserFromAuth(user);
                                                    }

                                                    prgDlg.dismiss();

                                                } else {
                                                    // Indicamos al usuario que los datos ingresados son invalidos
                                                    prgDlg.dismiss();
                                                    alertDialog = crateAlertDialog(getString(R.string.error_deleted_data));
                                                    alertDialog.show();
                                                }

                                            }
                                        }).addOnCanceledListener(EditUserActivity.this, new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        // Mostramos una ventana con un error fatal al iniciar sesión
                                        alertDialog = crateAlertDialog(getString(R.string.error_deleted_data));
                                        alertDialog.show();
                                        prgDlg.dismiss();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertDialog = crateAlertDialog(getString(R.string.error_deleted_data));
                        alertDialog.show();
                        prgDlg.dismiss();

                    }
                });
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builderAlert.create();
        builderAlert.show();




    }

    private AlertDialog crateAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditUserActivity.this);
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();

    }


    private void deleteUserFromAuth(final FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child(FirebaseClass.USERS).child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        prgDlg.dismiss();
                        Toast.makeText(EditUserActivity.this, getString(R.string.deleted_data), Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(EditUserActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertDialog = crateAlertDialog(getString(R.string.error_deleted_data));
                        alertDialog.show();
                        prgDlg.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUserActivity.this, getString(R.string.error_deleted_data), Toast.LENGTH_SHORT).show();
                prgDlg.dismiss();
            }
        });


    }


    // Creamos la alerta con el titulo, el mensaje y los botones de si y no para asignar el pago como realizado e insertamos los resultados en la base de datos

    // Asignamos opciones a los iconos superiores
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_close:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // metodo para regresar al menu principal
    private void goToMain() {
        Intent intent = new Intent(EditUserActivity.this, MainAdminActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_close, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
