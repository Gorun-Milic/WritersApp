package com.gorun.multipleactivities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.gorun.model.Category;
import com.gorun.model.Comment;
import com.gorun.model.CommentDTO;
import com.gorun.model.Story;
import com.gorun.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, "stories.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE IF NOT EXISTS `user` (" +
                "  iduser integer primary key autoincrement," +
                "  `name` TEXT," +
                "  `surname` TEXT," +
                "  `email` TEXT," +
                "  `password` TEXT," +
                "  `gender` TEXT," +
                "  `img` TEXT" +
                "  )";

        String createCategoryTable = "CREATE TABLE IF NOT EXISTS `category` (" +
                "  `idcategory` integer primary key autoincrement," +
                "  `name` TEXT" +
                "  )";

        String createStoryTable = "CREATE TABLE IF NOT EXISTS `story` (" +
                "  `idstory` integer primary key autoincrement," +
                "  `title` TEXT," +
                "  `text` TEXT," +
                "  `date` TEXT," +
                "  `iduser` INT," +
                "  `idcategory` INT," +
                "    FOREIGN KEY (`iduser`)" +
                "    REFERENCES `user` (`iduser`)," +
                "    FOREIGN KEY (`idcategory`)" +
                "    REFERENCES `category` (`idcategory`))";

        String createCommentTable = "CREATE TABLE IF NOT EXISTS `comment` (" +
                "  `idcomment` integer primary key autoincrement," +
                "  `iduser` INT," +
                "  `idstory` INT," +
                "  `text` TEXT," +
                "  `date` TEXT," +
                "    FOREIGN KEY (`iduser`)" +
                "    REFERENCES `user` (`iduser`)," +
                "    FOREIGN KEY (`idstory`)" +
                "    REFERENCES `story` (`idstory`))";


        String createLikeTable = "CREATE TABLE IF NOT EXISTS `like` (" +
                "  `idlike` integer primary key autoincrement," +
                "  `iduser` INT," +
                "  `idstory` INT," +
                "    FOREIGN KEY (`iduser`)" +
                "    REFERENCES `user` (`iduser`)," +
                "    FOREIGN KEY (`idstory`)" +
                "    REFERENCES `story` (`idstory`))";

        String createRememberTable = "CREATE TABLE IF NOT EXISTS `remember` (" +
                "  `idremember` integer primary key autoincrement," +
                "  `iduser` INT," +
                "  `idstory` INT," +
                "    FOREIGN KEY (`iduser`)" +
                "    REFERENCES `user` (`iduser`)," +
                "    FOREIGN KEY (`idstory`)" +
                "    REFERENCES `story` (`idstory`))";

        db.execSQL(createUserTable);
        db.execSQL(createCategoryTable);
        db.execSQL(createStoryTable);
        db.execSQL(createCommentTable);
        db.execSQL(createLikeTable);
        db.execSQL(createRememberTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //za sada nista
    }


    // Functions for table USER

    public boolean validateBeforeInsert(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? or password = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        //user vec postoji
        if (cursor.moveToFirst()) {
            return false;
        }
        //user ne postoji
        return true;
    }

    public int addUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", user.getName());
        cv.put("surname", user.getSurname());
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());
        cv.put("gender", user.getGender());
        cv.put("img", user.getImg());

        long insertValid = db.insert("user", null, cv);

        int id = -1;
        if (insertValid!=-1) {
            id = getIDUser(user.getEmail());
        }
        return id;
    }

    public int getIDUser(String email) {
        ArrayList<User> listUser = new ArrayList<>();
        String query = "SELECT * FROM user WHERE email = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            int customerId = cursor.getInt(0);
            return customerId;
        }

        return -1;
    }

    public User getUser(int iduser) {
        User user = new User();

        ArrayList<User> listUser = new ArrayList<>();
        String query = "SELECT * FROM user WHERE iduser = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{iduser + ""});

        if (cursor.moveToFirst()) {
            user.setIduser(iduser);
            user.setName(cursor.getString(1));
            user.setSurname(cursor.getString(2));
            user.setEmail(cursor.getString(3));
            user.setGender(cursor.getString(5));
            user.setImg(cursor.getString(6));
        }

        return user;
    }

    public int loginUser(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ? and password = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        //user postoji
        if (cursor.moveToFirst()) {
            int iduser = cursor.getInt(0);
            return iduser;
        }
        //user ne postoji
        return -1;
    }

    // Functions for table STORY

    public boolean addStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", story.getTitle());
        cv.put("text", story.getText());
        cv.put("date", story.getDate());
        cv.put("iduser", story.getIduser());
        cv.put("idcategory", story.getIdcategory());

        long insertValid = db.insert("story", null, cv);

        if (insertValid==-1) {
            return false;
        }
        return true;
    }

    public List<Story> getStoriesForUser(int iduser) {
        List<Story> stories = new ArrayList<>();
        String query = "SELECT * FROM story WHERE iduser = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{iduser + ""});

        if (cursor.moveToFirst()) {
            do {
                int idstory = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);
                String date = cursor.getString(3);
                int userid = cursor.getInt(4);
                int categoryid = cursor.getInt(5);
                Story story = new Story(idstory, title, text, date, userid, categoryid);
                stories.add(story);
            }while (cursor.moveToNext());
        }
        return stories;
    }

    public List<Story> getAllStories() {
        List<Story> stories = new ArrayList<>();
        String query = "SELECT * FROM story";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idstory = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);
                String date = cursor.getString(3);
                int userid = cursor.getInt(4);
                int categoryid = cursor.getInt(5);
                Story story = new Story(idstory, title, text, date, userid, categoryid);
                stories.add(story);
            }while (cursor.moveToNext());
        }
        return stories;
    }


    public Story getStory(int idstory) {
        Story story = new Story();

        ArrayList<Story> listStory = new ArrayList<>();
        String query = "SELECT * FROM story WHERE idstory = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{idstory + ""});

        if (cursor.moveToFirst()) {
            story.setIdstory(idstory);
            story.setTitle(cursor.getString(1));
            story.setText(cursor.getString(2));
            story.setDate(cursor.getString(3));
            story.setIduser(cursor.getInt(4));
            story.setIdcategory(cursor.getInt(5));
        }

        return story;
    }

    public boolean updateStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", story.getTitle());
        cv.put("text", story.getText());
        cv.put("date", story.getDate());
        cv.put("iduser", story.getIduser());
        cv.put("idcategory", story.getIdcategory());

        int numberRowsAffected = db.update("story", cv, "idstory=?", new String[]{story.getIdstory() + ""});
        if (numberRowsAffected == 0) {
            return false;
        }else {
            return true;
        }
    }

    public void deleteStory(Story story) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String queryString = "DELETE FROM story WHERE idstory = ?";
//        Cursor cursor = db.rawQuery(queryString, new String[]{story.getIdstory() + ""});
        db.delete("story", "idstory=?", new String[] {Integer.toString(story.getIdstory())});
    }

    public List<Story> getSearchedStories(String term) {
        List<Story> stories = new ArrayList<>();
        String query = "SELECT * FROM story WHERE title LIKE '%" + term + "%' OR text LIKE '%" + term + "%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idstory = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);
                String date = cursor.getString(3);
                int userid = cursor.getInt(4);
                int categoryid = cursor.getInt(5);
                Story story = new Story(idstory, title, text, date, userid, categoryid);
                stories.add(story);
            }while (cursor.moveToNext());
        }
        return stories;
    }

    public List<Story> getSearchedStories(int idcategory) {
        List<Story> stories = new ArrayList<>();
        String query = "SELECT * FROM story WHERE idcategory = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{idcategory + ""});

        if (cursor.moveToFirst()) {
            do {
                int idstory = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);
                String date = cursor.getString(3);
                int userid = cursor.getInt(4);
                int categoryid = cursor.getInt(5);
                Story story = new Story(idstory, title, text, date, userid, categoryid);
                stories.add(story);
            }while (cursor.moveToNext());
        }
        return stories;
    }

    public List<Story> getSearchedStories(String term, int idcategory) {
        List<Story> stories = new ArrayList<>();
        String query = "SELECT * FROM story WHERE title LIKE '%" + term + "%' OR text LIKE '%" + term + "%' AND idcategory = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{idcategory + ""});

        if (cursor.moveToFirst()) {
            do {
                int idstory = cursor.getInt(0);
                String title = cursor.getString(1);
                String text = cursor.getString(2);
                String date = cursor.getString(3);
                int userid = cursor.getInt(4);
                int categoryid = cursor.getInt(5);
                Story story = new Story(idstory, title, text, date, userid, categoryid);
                stories.add(story);
            }while (cursor.moveToNext());
        }
        return stories;
    }

    // Functions for table Category
    public List<Category> getCategories() {

        ArrayList<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int idcategory = cursor.getInt(0);
                String name = cursor.getString(1);
                Category category = new Category(idcategory, name);
                categories.add(category);
            }while (cursor.moveToNext());
        }
        return categories;
    }

    public Category getCategory(int idcategory) {
        String query = "SELECT * FROM category WHERE idcategory = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[]{idcategory + ""});

        if (cursor.moveToFirst()) {
            int idcat = cursor.getInt(0);
            String name = cursor.getString(1);
            Category category = new Category(idcat, name);
            return category;
        }
        return null;
    }

    // Functions for table REMEMBER

    public boolean isRemembered(int idstory, int iduser) {
        String query = "SELECT * FROM remember WHERE idstory = ? and iduser = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[]{idstory + "", iduser + ""});

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public boolean rememberStory(int iduser, int idstory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("iduser", iduser);
        cv.put("idstory", idstory);

        long insertValid = db.insert("remember", null, cv);

        if (insertValid==-1) {
            return false;
        }
        return true;
    }

    public void forgetStory(int iduser, int idstory) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String queryString = "DELETE FROM remember WHERE iduser = ? and idstory = ?";
//        Cursor cursor = db.rawQuery(queryString, new String[]{iduser + "", idstory + ""});

        db.delete("remember", "iduser=? and idstory=?", new String[] {Integer.toString(iduser), Integer.toString(idstory)});
    }

    public List<Story> rememberedStories(int iduser) {
        ArrayList<Story> stories = new ArrayList<>();
        String query = "SELECT idstory FROM remember WHERE iduser = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{iduser + ""});

        if (cursor.moveToFirst()) {
            do {
                int idstory = cursor.getInt(0);
                Story story = this.getStory(idstory);
                stories.add(story);
            }while (cursor.moveToNext());
        }
        return stories;
    }

    // Functions for table LIKE

    public boolean isLiked(int idstory, int iduser) {
        String query = "SELECT * FROM `like` WHERE idstory = ? and iduser = ?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[]{idstory + "", iduser + ""});

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }

    public boolean likeStory(int iduser, int idstory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("iduser", iduser);
        cv.put("idstory", idstory);

        long insertValid = db.insert("like", null, cv);

        if (insertValid==-1) {
            return false;
        }
        return true;
    }

    public void unlike(int iduser, int idstory) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("like", "iduser=? and idstory=?", new String[] {Integer.toString(iduser), Integer.toString(idstory)});
    }

    public int getNumberOfLikes(int idstory) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = (int)DatabaseUtils.queryNumEntries(db, "like", "idstory=?", new String[]{idstory + ""});
        db.close();
        return count;
    }

    // Functions for table COMMENT

    public boolean addComment(int iduser, int idstory, String text, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("iduser", iduser);
        cv.put("idstory", idstory);
        cv.put("text", text);
        cv.put("date", date);

        long insertValid = db.insert("comment", null, cv);

        if (insertValid==-1) {
            return false;
        }
        return true;
    }

    public List<CommentDTO> getComments(int idstory) {

        ArrayList<CommentDTO> comments = new ArrayList<>();
        String query = "SELECT * FROM comment WHERE idstory = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{idstory + ""});

        if (cursor.moveToFirst()) {
            do {
                int idcomment = cursor.getInt(0);
                int iduser = cursor.getInt(1);
                User user = getUser(iduser);
                int idstr = cursor.getInt(2);
                String text = cursor.getString(3);
                String date = cursor.getString(4);

                CommentDTO comment = new CommentDTO(idcomment, iduser, idstr, user, text, date);
                comments.add(comment);
            }while (cursor.moveToNext());
        }
        return comments;
    }


}
