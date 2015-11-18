package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jp.fccpc.taskmanager.Values.User;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_EMAIL;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_NAME;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_TOKEN;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TABLE_USER;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.USER_COLUMNS;

/**
 * Created by hskk1120551 on 15/11/03.
 */
public class UserDataController extends SQLiteDataController {
    private static final String TAG = UserDataController.class.getSimpleName();

    public UserDataController(Context context) { super(context, TABLE_USER); }

    public long createUser(User user){
        ContentValues v = new ContentValues();
        v.put(KEY_ID, user.getUserId());
        v.put(KEY_NAME, user.getName());
        v.put(KEY_EMAIL, user.getEmailAddress());
        return super.createModel(v);
    }

    public User getUser(Long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        if(id == null) {id = 0L;}

        Cursor c = db.query(TABLE_USER,
                USER_COLUMNS,
                " id = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        if(c != null) c.moveToFirst();

        return this.cursor2user(c);
    }

    public User getUser(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(TABLE_USER,
                USER_COLUMNS,
                " name = ?",
                new String[] { name },
                null,
                null,
                null,
                null);

        if(c != null) c.moveToFirst();

        return this.cursor2user(c);
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_USER,
                KEY_ID + " = ?",
                new String[]{ String.valueOf(user.getUserId())});
        db.close();
    }

    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getUserId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmailAddress());

        int id = db.update(TABLE_USER, //table
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();

        return id;
    }

    public String getToken(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                TABLE_USER,
                new String[] { KEY_TOKEN },
                " name = ?",
                new String[] { name },
                null,
                null,
                null,
                null);

        if(c != null) c.moveToFirst();

        return c.getString(c.getColumnIndex(KEY_TOKEN));
    }

    public void updateToken(String name, String token) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, token);

        db.update(
                TABLE_USER,
                values,
                KEY_NAME + " = ?",
                new String[]{ name }
        );
        db.close();
    }

    private User cursor2user(Cursor c) {
        User u = new User(
                c.getLong(c.getColumnIndex(KEY_ID)),
                c.getString(c.getColumnIndex(KEY_NAME)),
                c.getString(c.getColumnIndex(KEY_EMAIL))
        );

        return u;
    }
}
