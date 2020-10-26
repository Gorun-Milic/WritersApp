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

import com.gorun.model.Category;
import com.gorun.model.Story;
import com.gorun.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WriteStoryActivity extends AppCompatActivity {

    //main data
    int iduser;
    User user;
    Story story;
    DataBaseHelper db;
    Spinner categorySpinner;

    //input fields
    EditText edtTitle;
    EditText edtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story);

        db = new DataBaseHelper(this);
        Intent activityThatCalled = getIntent();
        String stringIduser = activityThatCalled.getExtras().getString("iduser");
        iduser = Integer.parseInt(stringIduser);

        user = db.getUser(iduser);
        story = new Story();
        story.setIduser(iduser);

        //category
        categorySpinner = findViewById(R.id.categorySpinner);
        final List<Category> categories = db.getCategories();

        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Category category = categories.get(position);
                        story.setIdcategory(category.getIdcategory());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        story.setIdcategory(1);
                    }
                }
        );

    }

    public void openFirstTab(View view) {
        Intent intent = new Intent(this, FirstTabActivity.class);
        intent.putExtra("iduser", iduser + "");
        startActivity(intent);
    }

    public void openSecondTab(View view) {
        Intent intent = new Intent(this, SecondTabActivity.class);
        intent.putExtra("iduser", iduser + "");
        startActivity(intent);
    }

    public void openThirdTab(View view) {
        Intent intent = new Intent(this, ThirdTabActivity.class);
        intent.putExtra("iduser", iduser + "");
        startActivity(intent);
    }

    public void addStory(View view) {
        edtTitle = findViewById(R.id.edtTitle);
        edtText = findViewById(R.id.edtText);

        String title = edtTitle.getText().toString();
        String text = edtText.getText().toString();

        Date date = new Date();
        String stringDate = date.toString();

        story.setTitle(title);
        story.setText(text);
        story.setDate(stringDate);

        boolean storyAdded = db.addStory(story);

        if (storyAdded) {
            Toast.makeText(this, "New Story added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, FirstTabActivity.class);
            intent.putExtra("iduser", iduser + "");
            startActivity(intent);
        }else {
            Toast.makeText(this, "Story not added, try again", Toast.LENGTH_SHORT).show();
        }

    }

}
