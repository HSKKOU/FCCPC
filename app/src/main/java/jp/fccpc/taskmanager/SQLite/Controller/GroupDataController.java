package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Util.JsonParser;
import jp.fccpc.taskmanager.Values.Group;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.GROUP_COLUMNS;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ADMIN_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ETAG;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_MEMBERSHIPS;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_NAME;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_UPDATED_AT;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TABLE_GROUP;

/**
 * Created by hskk1120551 on 15/11/03.
 */
public class GroupDataController extends SQLiteDataController {
    private static final String TAG = GroupDataController.class.getSimpleName();

    public GroupDataController(Context context) { super(context, TABLE_GROUP); }

    public long createGroup(Group group){
        ContentValues v = new ContentValues();
        v.put(KEY_ID, group.getGroupId());
        v.put(KEY_NAME, group.getName());
        v.put(KEY_ADMIN_ID, group.getAdministratorId());
        v.put(KEY_MEMBERSHIPS, JsonParser.membership2str(group.getMemberships()));
        v.put(KEY_UPDATED_AT, group.getUpdatedAt());
        v.put(KEY_ETAG, group.getETag());
        return this.createModel(v);
    }

    public Group getGroup(Long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_GROUP,
                GROUP_COLUMNS,
                KEY_ID + " = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        if(c != null){
            c.moveToFirst();
            return this.cursor2group(c);
        }

        return null;
    }

    public List<Group> getAllGroups() {
        db = dbHelper.getReadableDatabase();
        List<Group> groupList = new ArrayList<Group>();
        String selectQuery = "SELECT * FROM " + this.tableName;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Group g = this.cursor2group(c);
                groupList.add(g);
            } while (c.moveToNext());
        }

        return groupList;
    }

    public void deleteGroup(Group group) {
        this.deleteModel(String.valueOf(group.getGroupId()));
    }

    public long updateGroup(Group group) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getGroupId());
        values.put(KEY_NAME, group.getName());
        values.put(KEY_ADMIN_ID, group.getAdministratorId());
        values.put(KEY_MEMBERSHIPS, JsonParser.membership2str(group.getMemberships()));
        values.put(KEY_UPDATED_AT, group.getUpdatedAt());
        values.put(KEY_ETAG, group.getETag());

        return this.updateModel(values, String.valueOf(group.getGroupId()));
    }

    private Group cursor2group(Cursor c) {
        if(c.getCount() == 0) {return null;}
        Group g = new Group(
                c.getLong(c.getColumnIndex(KEY_ID)),
                c.getString(c.getColumnIndex(KEY_NAME)),
                c.getLong(c.getColumnIndex(KEY_ADMIN_ID)),
                JsonParser.memberships(c.getString(c.getColumnIndex(KEY_MEMBERSHIPS))),
                c.getLong(c.getColumnIndex(KEY_UPDATED_AT)),
                c.getString(c.getColumnIndex(KEY_ETAG))
        );

        return g;
    }
}
