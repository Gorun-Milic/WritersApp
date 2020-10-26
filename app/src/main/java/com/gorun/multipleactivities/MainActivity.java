package com.gorun.multipleactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper db;
    EditText edtEmail;
    EditText edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DataBaseHelper(this);

    }

    public void login(View view) {
        edtEmail = findViewById(R.id.edtLoginEmail);
        edtPassword = findViewById(R.id.edtLoginPassword);

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        int iduser = db.loginUser(email, password);

        if (iduser!=-1) {
            Intent intent = new Intent(this, FirstTabActivity.class);
            intent.putExtra("iduser", iduser + "");
            startActivity(intent);
        }else {
            Toast.makeText(this, "Invalid email or password, try again", Toast.LENGTH_SHORT).show();
        }

    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
