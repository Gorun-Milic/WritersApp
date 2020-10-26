package com.gorun.multipleactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gorun.model.Category;
import com.gorun.model.Story;
import com.gorun.model.User;

import java.util.List;

public class MyStoryActivity extends AppCompatActivity {

    DataBaseHelper db;

    int iduser;
    int idstory;
    User user;
    Story story;

    EditText edtTitle;
    EditText editText;
    Spinner categorySpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);

        db = new DataBaseHelper(this);

        Intent activityThatCalled = getIntent();
        String stringIduser = activityThatCalled.getExtras().getString("iduser");
        iduser = Integer.parseInt(stringIduser);

        String stringIdstory = activityThatCalled.getExtras().getString("idstory");
        idstory = Integer.parseInt(stringIdstory);

        user = db.getUser(iduser);
        story = db.getStory(idstory);

        edtTitle = findViewById(R.id.edtTitle);
        editText = findViewById(R.id.edtText);

        edtTitle.setText(story.getTitle());
        editText.setText(story.getText());

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
                        //ostane ista koja je i bila
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


    public void saveStory(View view) {
        String title = edtTitle.getText().toString();
        String text = editText.getText().toString();
        story.setTitle(title);
        story.setText(text);
        boolean updateValid = db.updateStory(story);
        if (updateValid) {
            Intent intent = new Intent(this, FirstTabActivity.class);
            intent.putExtra("iduser", iduser + "");
            startActivity(intent);
        }else {
            Toast.makeText(this, "Not updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteStory(View view) {
        db.deleteStory(story);
        Intent intent = new Intent(this, FirstTabActivity.class);
        intent.putExtra("iduser", iduser + "");
        startActivity(intent);
    }


}
