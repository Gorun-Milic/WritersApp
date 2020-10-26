package com.gorun.multipleactivities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gorun.model.Story;
import com.gorun.model.User;

import org.w3c.dom.Text;

import java.util.FormatFlagsConversionMismatchException;
import java.util.List;

public class FirstTabActivity extends AppCompatActivity {

    int iduser;
    User user;
    DataBaseHelper db;
    ListView storiesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_tab);
        db = new DataBaseHelper(this);
        user = new User();

        Intent activityThatCalled = getIntent();
        String stringIduser = activityThatCalled.getExtras().getString("iduser");
        iduser = Integer.parseInt(stringIduser);

        user = db.getUser(iduser);

        TextView txtName = findViewById(R.id.txtName);
        TextView txtSurname = findViewById(R.id.txtSurname);
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtGender = findViewById(R.id.txtGender);

        String name = txtName.getText().toString();
        String surname = txtSurname.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getText().toString();

        txtName.setText(name + "" + user.getName());
        txtSurname.setText(surname + user.getSurname());
        txtEmail.setText(email + user.getEmail());
        txtGender.setText(gender + user.getGender());

        ImageView imageView = findViewById(R.id.imgUser);

        switch (user.getImg()) {
            case "@mipmap/m1":
                imageView.setImageResource(R.mipmap.m1);
                break;
            case "@mipmap/m2":
                imageView.setImageResource(R.mipmap.m2);
                break;
            case "@mipmap/m3":
                imageView.setImageResource(R.mipmap.m3);
                break;
            case "@mipmap/m4":
                imageView.setImageResource(R.mipmap.m4);
                break;
            case "@mipmap/m6":
                imageView.setImageResource(R.mipmap.m6);
                break;
            case "@mipmap/m7":
                imageView.setImageResource(R.mipmap.m7);
                break;
            case "@mipmap/m8":
                imageView.setImageResource(R.mipmap.m8);
                break;
            case "@mipmap/m9":
                imageView.setImageResource(R.mipmap.m9);
                break;
            case "@mipmap/m11":
                imageView.setImageResource(R.mipmap.m11);
                break;
            case "@mipmap/m12":
                imageView.setImageResource(R.mipmap.m12);
                break;
            case "@mipmap/m13":
                imageView.setImageResource(R.mipmap.m13);
                break;
            case "@mipmap/m14":
                imageView.setImageResource(R.mipmap.m14);
                break;
            case "@mipmap/m16":
                imageView.setImageResource(R.mipmap.m16);
                break;
            case "@mipmap/m17":
                imageView.setImageResource(R.mipmap.m17);
                break;
            case "@mipmap/m18":
                imageView.setImageResource(R.mipmap.m18);
                break;
            default:
                imageView.setImageResource(R.mipmap.m19);

        }

        storiesList = findViewById(R.id.storiesList);
        final List<Story> stories = db.getStoriesForUser(iduser);

        ArrayAdapter<Story> storiesAddapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                stories
        );

        storiesList.setAdapter(storiesAddapter);

        storiesList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Story story = stories.get(position);
                        Intent intent = new Intent(FirstTabActivity.this, MyStoryActivity.class);
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

    public void writeStory(View view) {
        Intent intent = new Intent(this, WriteStoryActivity.class);
        intent.putExtra("iduser", iduser + "");
        startActivity(intent);
    }
}
