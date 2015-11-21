package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Values.Task;

import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_CREATED_AT;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_DEADLINE;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_DETAIL;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_DONE_AT;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ETAG;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_GROUP_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_REMINDER_TIME;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_TITLE;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.KEY_UPDATED_AT;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TABLE_TASK;
import static jp.fccpc.taskmanager.SQLite.Controller.DBOpenHelper.TASK_COLUMNS;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class TaskDataController extends SQLiteDataController {
    private static final String TAG = TaskDataController.class.getSimpleName();

    public TaskDataController(Context context){
        super(context, TABLE_TASK);
    }

    public long createTask(Task task){
        ContentValues v = new ContentValues();
        v.put(KEY_ID, task.getTaskId());
        v.put(KEY_GROUP_ID, task.getGroupId());
        v.put(KEY_TITLE, task.getTitle());
        v.put(KEY_DEADLINE, task.getDeadline());
        v.put(KEY_DETAIL, task.getDetail());
        v.put(KEY_REMINDER_TIME, task.getReminderTime());
        v.put(KEY_CREATED_AT, task.getCreatedAt());
        v.put(KEY_UPDATED_AT, task.getUpdatedAt());
        v.put(KEY_DONE_AT, this.NLong2long(task.getDoneAt()));
        v.put(KEY_ETAG, task.getETag());
        return this.createModel(v);
    }

//    public void createDefaultTasks(int num){
//        for (int i=0; i<num; i++){
//            Task t = new Task();
//            t.setTitle("task" + i);
//            t.setContent("tasktasktask" + (i * 111));
//            t.setDeadline((new Date()).getTime() + 86400000);
//            t.setCompleteFlag(i%2);
//            this.createTask(t);
//        }
//    }

    public Task getTask(Long id) {
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_TASK,
                TASK_COLUMNS,
                " id = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        Task t = null;

        if(c != null){
            c.moveToFirst();
            t = this.cursor2task(c);
        }

        db.close();

        return t;
    }

    public void deleteTask(Task task) {
        this.deleteModel(String.valueOf(task.getTaskId()));
    }

    public long updateTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_ID, task.getGroupId());
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DEADLINE, task.getDeadline());
        values.put(KEY_DETAIL, task.getDetail());
        values.put(KEY_REMINDER_TIME, task.getReminderTime());
        values.put(KEY_CREATED_AT, task.getCreatedAt());
        values.put(KEY_UPDATED_AT, task.getUpdatedAt());
        values.put(KEY_DONE_AT, this.NLong2long(task.getDoneAt()));
        values.put(KEY_ETAG, task.getETag());

        return this.updateModel(values, String.valueOf(task.getTaskId()));
    }

    public List<Task> getAllTasks(){
        db = dbHelper.getReadableDatabase();
        List<Task> themeList = new ArrayList<Task>();
        String selectQuery = "SELECT * FROM " + this.tableName;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Task t = this.cursor2task(c);
                themeList.add(t);
            } while (c.moveToNext());
        }
        db.close();

        return themeList;
    }

    public List<Task> getAllTasks(Long groupId){
        db = dbHelper.getReadableDatabase();
        List<Task> themeList = new ArrayList<Task>();
        Cursor c = db.query(TABLE_TASK,
                TASK_COLUMNS,
                KEY_GROUP_ID + " = ?",
                new String[]{String.valueOf(groupId)},
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            do {
                Task t = this.cursor2task(c);
                themeList.add(t);
            } while (c.moveToNext());
        }
        db.close();

        return themeList;
    }

    private List<Task> getComletedTasks(boolean tf) {
        db = dbHelper.getReadableDatabase();
        List<Task> themeList = new ArrayList<Task>();

        String whereArg = "";
        if(tf) {whereArg = KEY_DONE_AT + " > 0";}
        else {whereArg = KEY_DONE_AT + " = 0";}

        Cursor c = db.query(
                this.tableName,
                null,
                whereArg,
                null,
                null,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Task t = this.cursor2task(c);
                themeList.add(t);
            } while (c.moveToNext());
        }
        db.close();

        return themeList;
    }

    public List<Task> getCompletedTasks() { return getComletedTasks(true); }
    public List<Task> getUnCompletedTasks() { return getComletedTasks(false); }

    private Task cursor2task(Cursor c) {
        if(c.getCount() == 0) { return null; }

        Task t = new Task(
                c.getLong(c.getColumnIndex(KEY_ID)),
                c.getLong(c.getColumnIndex(KEY_GROUP_ID)),
                c.getString(c.getColumnIndex(KEY_TITLE)),
                c.getLong(c.getColumnIndex(KEY_DEADLINE)),
                c.getString(c.getColumnIndex(KEY_DETAIL)),
                c.getString(c.getColumnIndex(KEY_REMINDER_TIME)),
                c.getLong(c.getColumnIndex(KEY_CREATED_AT)),
                c.getLong(c.getColumnIndex(KEY_UPDATED_AT)),
                this.long2NLong(c.getLong(c.getColumnIndex(KEY_DONE_AT))),
                c.getString(c.getColumnIndex(KEY_ETAG))
        );

        return t;
    }

    private Long NLong2long(Long in) {
        if(in == null) { return 0L; }
        return in;
    }

    private Long long2NLong(Long in) {
        if(in == 0) {return null;}
        return in;
    }
}