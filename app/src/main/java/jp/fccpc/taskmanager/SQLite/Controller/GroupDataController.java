package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.GROUP_COLUMNS;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ADMIN_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ETAG;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_NAME;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_UPDATED_AT;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TABLE_GROUP;

/**
 * Created by hskk1120551 on 15/11/03.
 */
public class GroupDataController extends SQLiteDataController {
    private static final String TAG = GroupDataController.class.getSimpleName();

    private MembershipDataController mdc;

    public GroupDataController(Context context) {
        super(context, TABLE_GROUP);
        this.mdc = new MembershipDataController(context);
    }

    public long createGroup(Group group){
        ContentValues v = new ContentValues();
        v.put(KEY_ID, group.getGroupId());
        v.put(KEY_NAME, group.getName());
        v.put(KEY_ADMIN_ID, group.getAdministratorId());
        v.put(KEY_UPDATED_AT, group.getUpdatedAt());
        v.put(KEY_ETAG, group.getETag());
        long id = this.createModel(v);

        this.mdc.createMemberships(group.getMemberships());

        return id;
    }

    public Group getGroup(Long id) {
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_GROUP,
                GROUP_COLUMNS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);

        if(c == null){
            db.close();
            return null;
        }

        c.moveToFirst();
        Group g = this.cursor2group(c);

        db.close();

        return g;
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

        db.close();

        return groupList;
    }

    public void deleteGroup(Group group) {
        this.deleteModel(String.valueOf(group.getGroupId()));
        List<Membership> mList = this.mdc.getMembershipsByGroupId(group.getGroupId());
        for(Membership m : mList) {
            this.mdc.deleteMembership(m);
        }
    }

    public long updateGroup(Group group) {
        ContentValues values = new ContentValues();
        values.put(KEY_ID, group.getGroupId());
        values.put(KEY_NAME, group.getName());
        values.put(KEY_ADMIN_ID, group.getAdministratorId());
        values.put(KEY_UPDATED_AT, group.getUpdatedAt());
        values.put(KEY_ETAG, group.getETag());

        return this.updateModel(values, String.valueOf(group.getGroupId()));
    }

    private Group cursor2group(Cursor gc) {
        if(gc.getCount() == 0) {return null;}
        Long id = gc.getLong(gc.getColumnIndex(KEY_ID));
        Group g = new Group(
                id,
                gc.getString(gc.getColumnIndex(KEY_NAME)),
                gc.getLong(gc.getColumnIndex(KEY_ADMIN_ID)),
                this.mdc.getMembershipsByGroupId(id),
                gc.getLong(gc.getColumnIndex(KEY_UPDATED_AT)),
                gc.getString(gc.getColumnIndex(KEY_ETAG))
        );

        return g;
    }
}
