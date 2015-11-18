package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = DBOpenHelper.class.getSimpleName();

    // about DB
    public static final String DB_NAME = "FCCPC";
    public static final int DB_VERSION = 1;

    // TABLE name
    public static final String TABLE_GROUP = "groups";
    public static final String TABLE_TASK = "tasks";
    public static final String TABLE_MEMBERSHIP = "memberships";
    public static final String TABLE_USER = "users";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_GROUP_ID = "group_id";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_UPDATED_AT = "updated_at";
    public static final String KEY_ETAG = "eTag";

    // GROUP TABLE - column names
    public static final String KEY_NAME = "name";
    public static final String KEY_ADMIN_ID = "admin_id";
    public static final String KEY_MEMBERSHIPS = "memberships";
    public static final String[] GROUP_COLUMNS = {KEY_ID, KEY_NAME, KEY_ADMIN_ID, KEY_MEMBERSHIPS, KEY_CREATED_AT, KEY_UPDATED_AT, KEY_ETAG};

    // MEMBERSHIP TABLE - column names
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_GROUP_AGREED = "group_agreed";
    public static final String KEY_USER_AGREED = "user_agreed";
    public static final String[] MEMBERSHIP_COLUMNS = {KEY_ID, KEY_GROUP_ID, KEY_USER_ID, KEY_GROUP_AGREED, KEY_USER_AGREED, KEY_ETAG};

    // TASK TABLE - column names
    public static final String KEY_TITLE = "title";
    public static final String KEY_DEADLINE = "deadline";
    public static final String KEY_DETAIL = "detail";
    public static final String KEY_REMINDER_TIME = "reminder_time";
    public static final String KEY_DONE_AT = "done_at";
    public static final String[] TASK_COLUMNS = {KEY_ID, KEY_GROUP_ID, KEY_TITLE, KEY_DEADLINE, KEY_DETAIL, KEY_REMINDER_TIME, KEY_DONE_AT, KEY_CREATED_AT, KEY_UPDATED_AT, KEY_ETAG};

    // USER TABLE - column names
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN = "token";
    public static final String[] USER_COLUMNS = {KEY_ID, KEY_NAME, KEY_EMAIL};

    // Statements
    // GROUP TABLE create statement
    private static final String CREATE_TABLE_GROUP
            = "CREATE TABLE " + TABLE_GROUP + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_GROUP_ID + " INTEGER NOT NULL, "
            + KEY_NAME + " TEXT NOT NULL, "
            + KEY_ADMIN_ID + " INTEGER NOT NULL, "
            + KEY_MEMBERSHIPS + " TEXT, "
            + KEY_UPDATED_AT + " INTEGER NOT NULL, "
            + KEY_ETAG + " TEXT"
            + ")";

    // MEMBERSHIP TABLE create statement
    private static final String CREATE_TABLE_MEMBERSHIP
            = "CREATE TABLE " + TABLE_MEMBERSHIP + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_GROUP_ID + " INTEGER NOT NULL, "
            + KEY_USER_ID + " INTEGER NOT NULL, "
            + KEY_GROUP_AGREED + " INTEGER NOT NULL, "
            + KEY_USER_AGREED + " INTEGER NOT NULL, "
            + KEY_ETAG + " TEXT"
            + ")";

    // TASK TABLE create statement
    private static final String CREATE_TABLE_TASK
            = "CREATE TABLE " + TABLE_TASK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_GROUP_ID + " INTEGER NOT NULL, "
            + KEY_TITLE + " TEXT NOT NULL, "
            + KEY_DEADLINE + " INTEGER, "
            + KEY_DETAIL + " TEXT NOT NULL, "
            + KEY_REMINDER_TIME + " INTEGER NOT NULL, "
            + KEY_CREATED_AT + " INTEGER NOT NULL, "
            + KEY_UPDATED_AT + " INTEGER NOT NULL, "
            + KEY_DONE_AT + " INTEGER NOT NULL, "
            + KEY_ETAG + " TEXT"
            + ")";

    // USER TABLE create statement
    private static final String CREATE_TABLE_USER
            = "CREATE TABLE " + TABLE_USER + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + KEY_NAME + " TEXT NOT NULL, "
            + KEY_EMAIL + " TEXT NOT NULL, "
            + KEY_TOKEN + " TEXT"
            + ")";

    public DBOpenHelper(Context context){
        super(context, DB_NAME + ".db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_MEMBERSHIP);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERSHIP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // recreate
        onCreate(db);
    }
}
