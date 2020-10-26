package com.gorun.multipleactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gorun.model.Category;
import com.gorun.model.Story;
import com.gorun.model.User;

import java.util.List;

public class SecondTabActivity extends AppCompatActivity {

    DataBaseHelper db;
    User user;
    int iduser;

    Spinner categorySpinner;
    ListView storiesList;
    List<Story> stories;
    ArrayAdapter<Story> storiesAdapter;
    EditText edtSearch;
    String searchTerm = "";
    int idcategorySearch = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_tab);

        db = new DataBaseHelper(this);
        user = new User();

        Intent activityThatCalled = getIntent();
        String stringIduser = activityThatCalled.getExtras().getString("iduser");
        iduser = Integer.parseInt(stringIduser);

        user = db.getUser(iduser);

        categorySpinner = findViewById(R.id.categorySpinnerSearch);
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
                        idcategorySearch = category.getIdcategory();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        storiesList = findViewById(R.id.allStoriesList);
        stories = db.getAllStories();

        storiesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                stories
        );

        storiesList.setAdapter(storiesAdapter);

        storiesList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Story story = stories.get(position);
                        Intent intent = new Intent(SecondTabActivity.this, StoryActivity.class);
                        intent.putExtra("idstory", story.getIdstory() + "");
                        intent.putExtra("iduser", iduser + "");
                        startActivity(intent);
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

    public void search(View view) {
        edtSearch = findViewById(R.id.edtSearch);
        searchTerm = edtSearch.getText().toString();

        if (!searchTerm.equals("") && idcategorySearch ==0) {
            stories = db.getSearchedStories(searchTerm);
        }else if (searchTerm.equals("") && idcategorySearch !=0) {
            stories = db.getSearchedStories(idcategorySearch);
        }else if (!searchTerm.equals("") && idcategorySearch !=0) {
            stories = db.getSearchedStories(searchTerm, idcategorySearch);
        }

        storiesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                stories
        );

        storiesList.setAdapter(storiesAdapter);
    }
}
