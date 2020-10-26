package com.gorun.multipleactivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gorun.model.Category;
import com.gorun.model.Comment;
import com.gorun.model.CommentDTO;
import com.gorun.model.Story;
import com.gorun.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    DataBaseHelper db;
    int iduser;
    int idstory;
    Story story;

    TextView txtTitle;
    TextView txtText;
    TextView txtAuthorName;
    TextView txtNumberOfLikes;
    EditText edtAddComment;
    ListView commentList;
    User author;
    Button btnRemember;
    Button btnLike;

    boolean isRemembered;
    boolean isLiked;

    List<CommentDTO> comments;
    ArrayAdapter<CommentDTO> commentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        db = new DataBaseHelper(this);

        Intent activityThatCalled = getIntent();
        String stringIduser = activityThatCalled.getExtras().getString("iduser");
        iduser = Integer.parseInt(stringIduser);

        String stringIdstory = activityThatCalled.getExtras().getString("idstory");
        idstory = Integer.parseInt(stringIdstory);

        story = db.getStory(idstory);
        author = db.getUser(story.getIduser());

        txtTitle = findViewById(R.id.txtStoryTitle);
        txtText = findViewById(R.id.txtStoryText);
        txtAuthorName = findViewById(R.id.txtStoryAuthorName);

        txtTitle.setText(story.getTitle());
        txtText.setText(story.getText());
        txtAuthorName.setText(author.getName() + " " + author.getSurname());

        Category category = db.getCategory(story.getIdcategory());

        TextView txtCategory = findViewById(R.id.txtStoryCategory);
        txtCategory.append(category.getName());


        btnRemember = findViewById(R.id.btnRememberStory);
        checkIfRemembered();

        btnLike = findViewById(R.id.btnLikeStory);
        checkIfLiked();

        txtNumberOfLikes = findViewById(R.id.txtNumberOfLikes);
        getNumberOfLikes();

        edtAddComment = findViewById(R.id.edtAddComment);
        commentList = findViewById(R.id.commentList);

        showComments();
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

    public void rememberOrForget(View view) {
        if (isRemembered) {
            db.forgetStory(iduser, idstory);
        }else {
            db.rememberStory(iduser, idstory);
        }
        checkIfRemembered();
    }

    public void checkIfRemembered() {
        isRemembered = db.isRemembered(idstory, iduser);
        if (isRemembered) {
            btnRemember.setText("Forget");
            btnRemember.setBackgroundColor(getResources().getColor(R.color.darkgreen));
        }else {
            btnRemember.setText("Remember");
            btnRemember.setBackgroundColor(getResources().getColor(R.color.green));
        }
    }

    public void likeOrUnlike(View view) {
        if (isLiked) {
            db.unlike(iduser, idstory);
        }else {
            db.likeStory(iduser, idstory);
        }
        checkIfLiked();
        getNumberOfLikes();
    }

    public void checkIfLiked() {
        isLiked = db.isLiked(idstory, iduser);
        if (isLiked) {
            btnLike.setText("Unlike");
            btnLike.setBackgroundColor(getResources().getColor(R.color.unlikeblue));
        }else {
            btnLike.setText("Like");
            btnLike.setBackgroundColor(getResources().getColor(R.color.likeblue));
        }
    }

    public void getNumberOfLikes() {
        int numberOfLikes = db.getNumberOfLikes(idstory);
        txtNumberOfLikes.setText(numberOfLikes + "");
    }

//    public void saveInFile(View view) {
//        String title = story.getTitle();
//        String text = story.getText();
//        FileOutputStream fos = null;
//
//        try {
//            fos = openFileOutput(title, MODE_PRIVATE);
//            fos.write(text.getBytes());
//
//            Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if (fos!=null) {
//                try {
//                    fos.close();
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public void download(View view) {
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //getExternalStoragePublicDirectory
            File root = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            //File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String storyTitle = story.getTitle().replaceAll("\\s+","");
            File file = new File(root,  "/" + story.getTitle() + ".txt");
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(story.getText().getBytes());



                fo.close();
                Toast.makeText(this, "Successfully downloaded", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(this, "Download not successful", Toast.LENGTH_SHORT).show();
        }
    }

    public void scanFile(Context ctxt, File f, String mimeType) {
        MediaScannerConnection
                .scanFile(ctxt, new String[] {f.getAbsolutePath()},
                        new String[] {mimeType}, null);
    }

    public void addComment(View view) {
        String commentText = edtAddComment.getText().toString();
        String date = (new Date()).toString();
        boolean valid = db.addComment(iduser, idstory, commentText, date);
        showComments();


    }

    public void showComments() {
        comments = db.getComments(idstory);
        commentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                comments
        );

        commentList.setAdapter(commentAdapter);
        edtAddComment.setText("");
    }

}
