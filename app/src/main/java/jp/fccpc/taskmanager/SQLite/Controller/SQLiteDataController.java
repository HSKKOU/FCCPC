package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class SQLiteDataController {
    private static final String TAG = SQLiteDataController.class.getSimpleName();

    protected DBOpenHelper dbHelper;
    protected SQLiteDatabase db;
    protected String tableName = "";

    public SQLiteDataController(Context _context, String _tableName){
        dbHelper = new DBOpenHelper(_context);
        this.tableName = _tableName;
    }

    protected long createModel(ContentValues _v){
        db = dbHelper.getWritableDatabase();
        long id = db.insert(this.tableName, null, _v);
        db.close();
        return id;
    }

    protected long updateModel(ContentValues _v, String idStr){
        long id = 0;
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(this.tableName,
                new String[]{KEY_ID},
                KEY_ID + " = ?",
                new String[]{idStr},
                null,
                null,
                null,
                null);

        if(c == null || c.getCount() == 0) {
            _v.put(KEY_ID, idStr);
            id = this.createModel(_v);
        } else {
            db = dbHelper.getWritableDatabase();
            id = db.update(this.tableName, _v, KEY_ID + " = ?", new String[]{idStr});
        }
        db.close();
        return id;
    }

    protected long deleteModel(String idStr) {
        db = dbHelper.getWritableDatabase();
        long id = db.delete(this.tableName,
                KEY_ID + " = ?",
                new String[]{ idStr });
        db.close();
        return id;
    }


    public void deleteAll() {
        db = dbHelper.getWritableDatabase();
        int num = db.delete(this.tableName, null, null);
        db.close();
    }
}
