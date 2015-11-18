package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jp.fccpc.taskmanager.Values.Membership;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ETAG;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_GROUP_AGREED;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_GROUP_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_USER_AGREED;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_USER_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TABLE_GROUP;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TABLE_MEMBERSHIP;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.USER_COLUMNS;

/**
 * Created by hskk1120551 on 15/11/03.
 */
public class MembershipDataController extends SQLiteDataController {
    private static final String TAG = GroupDataController.class.getSimpleName();

    public MembershipDataController(Context context) { super(context, TABLE_MEMBERSHIP); }

    public long createMembership(Membership membership){
        ContentValues v = new ContentValues();
        v.put(KEY_ID, membership.getMembershipId());
        v.put(KEY_GROUP_ID, membership.getGroupId());
        v.put(KEY_USER_AGREED, membership.getUserId());
        v.put(KEY_GROUP_ID, this.bool2int(membership.isGroupAgreed()));
        v.put(KEY_USER_ID, this.bool2int(membership.isUserAgreed()));
        v.put(KEY_ETAG, membership.getETag());
        return super.createModel(v);
    }

    public Membership getMembership(Long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_GROUP,
                USER_COLUMNS,
                " id = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        if(c != null) c.moveToFirst();

        return this.cursor2group(c);
    }

    public void deleteMembership(Membership membership) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_GROUP,
                KEY_ID + " = ?",
                new String[]{ String.valueOf(membership.getMembershipId())});
        db.close();
    }

    public int updateGroup(Membership membership) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, membership.getMembershipId());
        values.put(KEY_GROUP_ID, membership.getGroupId());
        values.put(KEY_USER_AGREED, membership.getUserId());
        values.put(KEY_GROUP_ID, this.bool2int(membership.isGroupAgreed()));
        values.put(KEY_USER_ID, this.bool2int(membership.isUserAgreed()));
        values.put(KEY_ETAG, membership.getETag());

        int id = db.update(TABLE_GROUP, //table
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(membership.getMembershipId())});
        db.close();

        return id;
    }

    private Membership cursor2group(Cursor c) {
        Membership m = new Membership(
                c.getLong(c.getColumnIndex(KEY_ID)),
                c.getLong(c.getColumnIndex(KEY_GROUP_ID)),
                c.getLong(c.getColumnIndex(KEY_USER_ID)),
                this.int2bool(c.getInt(c.getColumnIndex(KEY_GROUP_AGREED))),
                this.int2bool(c.getInt(c.getColumnIndex(KEY_USER_AGREED))),
                c.getString(c.getColumnIndex(KEY_ETAG))
        );

        return m;
    }

    private int bool2int(boolean b) {return b? 1: 0;}
    private boolean int2bool(int i) {return i != 0;}
}
