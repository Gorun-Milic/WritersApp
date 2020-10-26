package com.gorun.multipleactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gorun.model.User;

import java.util.ArrayList;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    DataBaseHelper db;

    EditText edtName;
    EditText edtSurname;
    EditText edtEmail;
    EditText edtPassword;
    Spinner genderSpinner;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DataBaseHelper(this);

        //data
        edtName = findViewById(R.id.edtName);
        edtSurname = findViewById(R.id.edtSurname);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        user = new User();

        //gender
        genderSpinner = findViewById(R.id.genderSpinner);
        final ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                genders
        );
        genderSpinner.setAdapter(genderAdapter);

        genderSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        user.setGender(genders.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        user.setGender("None");
                    }
                }
        );


    }

    public void register(View view) {
        String name = edtName.getText().toString();
        String surname = edtSurname.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(password);
        setRandomImage();

        String valid = validateUser();

        if (valid.equals("Valid input")) {
            //add to database
            //send id throw intent

            boolean notExists = db.validateBeforeInsert(email, password);
            if (notExists) {
                int id = db.addUser(user);

                if (id!=-1) {
                    Intent intent = new Intent(this, FirstTabActivity.class);
                    intent.putExtra("iduser", id + "");
                    startActivity(intent);
                }
            }
        }

        Toast.makeText(this, valid, Toast.LENGTH_SHORT).show();


    }

    public void goToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public String validateUser() {
        if (user.getName().equals("") || user.getSurname().equals("") || user.getEmail().equals("") || user.getPassword().equals("")) {
            return "Every field must be filled";
        }
        else if (user.getName().length()<5) {
            return "Name must be at least 5 characters long";
        }
        else if (!user.getEmail().contains("@")) {
            return "Invalid email address";
        }
        else if (user.getPassword().length()<5) {
            return "Password must have at least 5 characters";
        }
        return "Valid input";
    }

    public void setRandomImage() {
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add("@mipmap/m1");
        imageList.add("@mipmap/m2");
        imageList.add("@mipmap/m3");
        imageList.add("@mipmap/m4");
        imageList.add("@mipmap/m6");
        imageList.add("@mipmap/m7");
        imageList.add("@mipmap/m8");
        imageList.add("@mipmap/m9");
        imageList.add("@mipmap/m11");
        imageList.add("@mipmap/m12");
        imageList.add("@mipmap/m13");
        imageList.add("@mipmap/m14");
        imageList.add("@mipmap/m16");
        imageList.add("@mipmap/m17");
        imageList.add("@mipmap/m18");
        imageList.add("@mipmap/m19");

        Random random = new Random();
        int randomNumber = random.nextInt(16);
        String image = imageList.get(randomNumber);

        user.setImg(image);
    }

}
