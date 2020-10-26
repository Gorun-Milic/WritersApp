package com.gorun.multipleactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gorun.model.Story;
import com.gorun.model.User;

import java.util.List;

public class ThirdTabActivity extends AppCompatActivity {

    DataBaseHelper db;
    User user;
    int iduser;

    ListView rememberedList;
    List<Story> rememberedStories;
    ArrayAdapter<Story> storiesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_tab);

        db = new DataBaseHelper(this);
        user = new User();

        Intent activityThatCalled = getIntent();
        String stringIduser = activityThatCalled.getExtras().getString("iduser");
        iduser = Integer.parseInt(stringIduser);

        user = db.getUser(iduser);

        rememberedList = findViewById(R.id.rememberedList);
        rememberedStories = db.rememberedStories(iduser);

        storiesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                rememberedStories
        );

        rememberedList.setAdapter(storiesAdapter);

        rememberedList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Story story = rememberedStories.get(position);
                        Intent intent = new Intent(ThirdTabActivity.this, StoryActivity.class);
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
}
