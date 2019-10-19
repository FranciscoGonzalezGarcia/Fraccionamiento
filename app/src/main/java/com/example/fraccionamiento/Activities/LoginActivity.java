package com.example.fraccionamiento.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fraccionamiento.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.fraccionamiento.Activities.Admin.MainAdminActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText lblEmail;
    EditText lblUserPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lblEmail = findViewById(R.id.inputEmail);
        lblUserPass = findViewById(R.id.inputUserPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });
        final FirebaseAuth fBAuth = FirebaseAuth.getInstance();
        if(fBAuth.getCurrentUser()!=null){
            goToMainActivity();
        }
    }

    private void tryLogin() {
        String email = lblEmail.getText().toString().trim();
        String password = lblUserPass.getText().toString().trim();
        final ProgressDialog prgDlg = new ProgressDialog(this);
        prgDlg.setMessage(getString(R.string.staring_session));
        prgDlg.show();
        final FirebaseAuth fBAuth = FirebaseAuth.getInstance();
        fBAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fBAuth.getCurrentUser();
                            if(user != null){
                                goToMainActivity();
                            }

                        } else {

                            Toast.makeText(LoginActivity.this, getString(R.string.not_authenticaed), Toast.LENGTH_SHORT).show();

                        }
                        prgDlg.dismiss();
                    }
                });
    }

    private void errorStartingSession(boolean notUser) {
        if(notUser){

        }
    }

    private void goToMainActivity(){
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+uid);
//        String CLIENT_ROLE = "client";
//        String ADMIN_ROLE = "admin";
//       if(ref.getKey("role").equals(ADMIN_ROLE)){
            finish();
            Intent goToMainActivityAdmin = new Intent(this, MainAdminActivity.class);
            startActivity(goToMainActivityAdmin);
//        }else if(fCC.getRol().equals(CLIENT_ROLE)){
//            finish();
//            Intent goToMainActivity = new Intent(this, MainActivity.class);
//            startActivity(goToMainActivity);
//        }

}


}
