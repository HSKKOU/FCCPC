package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import jp.fccpc.taskmanager.Values.User;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_EMAIL;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_NAME;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_TOKEN;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_USER_ID;
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
        v.put(KEY_USER_ID, user.getUserId());
        v.put(KEY_NAME, user.getName());
        v.put(KEY_EMAIL, user.getEmailAddress());
        return super.createModel(v);
    }

    public User getUser(Long id) {
        User user = null;

        db = dbHelper.getReadableDatabase();

        String whereClause = "";
        String[] whereArgs;

        if(id == null) {
            whereClause = KEY_ID + " = 1";
            whereArgs = null;
        } else {
            whereClause = KEY_USER_ID + " = ?";
            whereArgs = new String[] { String.valueOf(id) };
        }

        Cursor c = db.query(TABLE_USER,
                USER_COLUMNS,
                whereClause,
                whereArgs,
                null,
                null,
                null,
                null);

        if(c != null){
            c.moveToFirst();
            user = this.cursor2user(c);
        }

        db.close();

        return user;
    }

    public User getUserByName(String name) {
        User user = null;

        db = dbHelper.getReadableDatabase();

        Cursor c = db.query(TABLE_USER,
                USER_COLUMNS,
                " name = ?",
                new String[] { name },
                null,
                null,
                null,
                null);

        if(c != null){
            c.moveToFirst();
            user = this.cursor2user(c);
        }

        db.close();

        return user;
    }

    public void deleteUser(User user) {
        super.deleteModel(String.valueOf(user.getUserId()));
    }

    public long updateUser(User user) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getUserId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmailAddress());

        long id =  this.updateModel(values, String.valueOf(user.getUserId()));
        db.close();

        return id;
    }

    public String getToken() {
        String token = null;

        db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                TABLE_USER,
                USER_COLUMNS,
                KEY_ID + " = 1",
                null,
                null,
                null,
                null,
                null);

        if(c != null){
            c.moveToFirst();
            token = c.getString(c.getColumnIndex(KEY_TOKEN));
        }

        db.close();

        return token;
    }

    public void updateToken(String token) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOKEN, token);

        db.update(
                TABLE_USER,
                values,
                KEY_ID + " = 1",
                null
        );
        db.close();
    }

    private User cursor2user(Cursor c) {
        if(c.getCount() == 0) { return null; }
        User u = new User(
                c.getLong(c.getColumnIndex(KEY_USER_ID)),
                c.getString(c.getColumnIndex(KEY_NAME)),
                c.getString(c.getColumnIndex(KEY_EMAIL))
        );

        return u;
    }
}
