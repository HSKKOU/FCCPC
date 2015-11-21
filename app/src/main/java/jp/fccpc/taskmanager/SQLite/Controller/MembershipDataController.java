package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Values.Membership;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ETAG;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_GROUP_AGREED;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_GROUP_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_USER_AGREED;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_USER_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.MEMBERSHIP_COLUMNS;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TABLE_MEMBERSHIP;

/**
 * Created by hskk1120551 on 15/11/03.
 */
public class MembershipDataController extends SQLiteDataController {
    private static final String TAG = GroupDataController.class.getSimpleName();

    public MembershipDataController(Context context) { super(context, TABLE_MEMBERSHIP); }

    public long createMembership(Membership membership){
        ContentValues v = new ContentValues();
        v.put(KEY_GROUP_ID, membership.getGroupId());
        v.put(KEY_USER_ID, membership.getUserId());
        v.put(KEY_GROUP_AGREED, this.bool2int(membership.isGroupAgreed()));
        v.put(KEY_USER_AGREED, this.bool2int(membership.isUserAgreed()));
        v.put(KEY_ETAG, membership.getETag());
        return super.createModel(v);
    }

    public void createMemberships(List<Membership> memberships) {
        if(memberships.size() == 0) { return; }
        for(Membership m : memberships) {
            this.createMembership(m);
        }
    }

    public Membership getMembership(Long groupId, Long userId) {
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_MEMBERSHIP,
                MEMBERSHIP_COLUMNS,
                KEY_GROUP_ID + " = ? AND " + KEY_USER_ID + " = ?",
                new String[] { String.valueOf(groupId), String.valueOf(userId) },
                null,
                null,
                null,
                null);

        Membership m = null;

        if(c != null){
            c.moveToFirst();
            m = this.cursor2membership(c);
        }

        db.close();

        return m;
    }

    public List<Membership> getMembershipsByGroupId(Long groupId) {
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_MEMBERSHIP,
                MEMBERSHIP_COLUMNS,
                KEY_GROUP_ID + " = ?",
                new String[] { String.valueOf(groupId) },
                null,
                null,
                null,
                null);

        List<Membership> ms = new ArrayList<Membership>();

        if (c.moveToFirst()) {
            do {
                Membership m = this.cursor2membership(c);
                ms.add(m);
            } while (c.moveToNext());
        }

        db.close();

        return ms;
    }

    public List<Membership> getMembershipsByUserId(Long userId) {
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_MEMBERSHIP,
                MEMBERSHIP_COLUMNS,
                KEY_USER_ID + " = ?",
                new String[] { String.valueOf(userId) },
                null,
                null,
                null,
                null);

        List<Membership> ms = new ArrayList<Membership>();

        if (c.moveToFirst()) {
            do {
                Membership m = this.cursor2membership(c);
                ms.add(m);
            } while (c.moveToNext());
        }

        db.close();

        return ms;
    }


    public void deleteMembership(Membership membership) {
        db = dbHelper.getWritableDatabase();
        db.delete(TABLE_MEMBERSHIP,
                KEY_GROUP_ID + " = ? AND " + KEY_USER_ID + " = ?",
                new String[]{ String.valueOf(membership.getGroupId()), String.valueOf(membership.getUserId()) });
        db.close();
    }

    public void updateMembership(Membership membership) {
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_AGREED, this.bool2int(membership.isGroupAgreed()));
        values.put(KEY_USER_AGREED, this.bool2int(membership.isUserAgreed()));
        values.put(KEY_ETAG, membership.getETag());

        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(this.tableName,
                MEMBERSHIP_COLUMNS,
                KEY_GROUP_ID + " = ? AND " + KEY_USER_ID + " = ?",
                new String[]{ String.valueOf(membership.getGroupId()), String.valueOf(membership.getUserId()) },
                null,
                null,
                null,
                null);

        if(c == null || c.getCount() == 0) {
            this.createMembership(membership);
        } else {
            db = dbHelper.getWritableDatabase();
            db.update(TABLE_MEMBERSHIP, //table
                    values,
                    KEY_GROUP_ID + " = ? AND " + KEY_USER_ID + " = ?",
                    new String[]{String.valueOf(membership.getGroupId()), String.valueOf(membership.getUserId()) });
        }

        db.close();
    }

    private Membership cursor2membership(Cursor c) {
        if(c.getCount() == 0) { return null; }
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
