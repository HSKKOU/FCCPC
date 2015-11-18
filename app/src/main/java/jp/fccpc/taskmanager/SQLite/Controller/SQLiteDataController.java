package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    public int deleteAll() {
        db = dbHelper.getWritableDatabase();
        int num = db.delete(this.tableName, null, null);
        db.close();
        return num;
    }
}
