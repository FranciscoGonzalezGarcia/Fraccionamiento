package com.example.fraccionamiento.Activities.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fraccionamiento.R;

public class EditUserActivity extends AppCompatActivity {
    private EditText name, lastName, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(getString(R.string.edit_user));
        }else {
            Toast.makeText(EditUserActivity.this,getString(R.string.fatal_error), Toast.LENGTH_LONG).show();
            finish();
        }

        name = findViewById(R.id.txtEditName);
        lastName = findViewById(R.id.txtEditLastName);
        email = findViewById(R.id.txtEditEmail);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            name.setText(bundle.getString("name"));
            lastName.setText(bundle.getString("lastName"));
            email.setText(bundle.getString("email"));
        }else {
            Toast.makeText(EditUserActivity.this,getString(R.string.fatal_error), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
