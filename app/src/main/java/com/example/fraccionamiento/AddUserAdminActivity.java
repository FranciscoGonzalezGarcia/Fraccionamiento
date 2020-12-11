package com.example.fraccionamiento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fraccionamiento.Activities.Admin.AddNewUserActivity;
import com.example.fraccionamiento.Activities.LoginActivity;
import com.example.fraccionamiento.Classes.FirebaseClass;
import com.example.fraccionamiento.Classes.UserClass;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUserAdminActivity extends AppCompatActivity {
    private TextView name, lastName, email, pass, confirmPass;
    private String nameS, lastNameS, emailS, passS, confirmPassS;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_admin);

        ActionBar mActionBar = getSupportActionBar();
        if(mActionBar!=null){
            mActionBar.setTitle(getString(R.string.add_new_user));
        }

        name = findViewById(R.id.txtAddName);
        lastName = findViewById(R.id.txtAddLastName);
        email = findViewById(R.id.txtAddEmail);
        pass = findViewById(R.id.txtAddPass);
        confirmPass = findViewById(R.id.txtAddConfirmPass);
        Button btnAddUser = findViewById(R.id.btnAddUser);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");

        final ProgressDialog prgDlg = new ProgressDialog(this);
        prgDlg.setMessage(getString(R.string.creating_user));
        prgDlg.setCancelable(false);
        // Agregamos a función al botón
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Asignamos los valores brindados por el usuario a cada variable
                prgDlg.show();
                nameS = name.getText().toString();
                emailS = email.getText().toString();
                passS = pass.getText().toString();
                lastNameS = lastName.getText().toString();
                confirmPassS = confirmPass.getText().toString();


                // Validamos los campos
                if(areCorrectFields()){

                    //Nos aseguramos que las contraseñas cincidan
                    if(passMatch()){
                        // Intentamos crear un usuario con los datos brindados
                        mAuth.createUserWithEmailAndPassword(emailS, passS).addOnCompleteListener(AddUserAdminActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    // Si es exitoso, almacenamos los datos del usuario en un nodo de firebase
                                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                    final UserClass userClass = new UserClass();
                                    userClass.setName(nameS);
                                    userClass.setLastName(lastNameS);
                                    userClass.setEmail(emailS);
                                    userClass.setRole(FirebaseClass.ADMIN);
                                    userClass.setUrlImg("url");
                                    // Cerramos el proceso
                                    prgDlg.dismiss();


                                    // Subimos la información adicional
                                    prgDlg.setMessage(getString(R.string.uploading_data));
                                    prgDlg.show();

                                    // Insertamos los datos del usuario
                                    mRef.child(fUser.getUid()).setValue(userClass);

                                    // Creamos una instacia con la base de datos donde guardaremos la información de alojamiento del huesped


                                    Toast.makeText(AddUserAdminActivity.this, getString(R.string.successfull_user_add), Toast.LENGTH_LONG).show();

                                    // Cerramos el proceso - cerramos la sesión del usuario que se creo
                                    prgDlg.dismiss();
                                    FirebaseAuth.getInstance().signOut();

                                    // Iniciamos la actividad de login para iniciar sesión
                                    Intent intent = new Intent(AddUserAdminActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();


                                }else {
                                    // Si falla enviamos el error
                                    prgDlg.dismiss();
                                    Toast.makeText(AddUserAdminActivity.this, getString(R.string.already_user), Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {
                                // Si falla enviamos el error

                                prgDlg.dismiss();
                                Toast.makeText(AddUserAdminActivity.this, getString(R.string.fatal_error),Toast.LENGTH_LONG).show();
                            }
                        });

                    }else {
                        // Si falla enviamos el error

                        prgDlg.dismiss();
                        Toast.makeText(AddUserAdminActivity.this, getString(R.string.pass_not_match), Toast.LENGTH_LONG).show();
                    }

                }else {
                    // Verificamos que el proceso de carga de cierre
                    prgDlg.dismiss();
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

        else if(lastNameS.isEmpty()){
            // Si el campo de contrasña esta vacío
            // Arrojamos el error de campo contraseña vacío
            lastName.setError(getString(R.string.error_empty_field));
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


}
