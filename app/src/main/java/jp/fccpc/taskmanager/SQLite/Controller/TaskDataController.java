package jp.fccpc.taskmanager.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
        // TODO: updated_atの実装を待つ。
        ContentValues v = new ContentValues();
        v.put(KEY_ID, task.getTaskId());
        v.put(KEY_GROUP_ID, task.getGroupId());
        v.put(KEY_TITLE, task.getTitle());
        v.put(KEY_DEADLINE, task.getDeadline());
        v.put(KEY_DETAIL, task.getDetail());
        v.put(KEY_REMINDER_TIME, task.getReminderTime());
        v.put(KEY_CREATED_AT, task.getCreatedAt());
        v.put(KEY_UPDATED_AT, task.getUpdatedAt());
        v.put(KEY_DONE_AT, task.getDoneAt());
        v.put(KEY_ETAG, task.getETag());
        return super.createModel(v);
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(TABLE_TASK,
                TASK_COLUMNS,
                " id = ?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        if(c != null) c.moveToFirst();

        return this.cursor2task(c);
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_TASK,
                KEY_ID + " = ?",
                new String[]{ String.valueOf(task.getTaskId())});
        db.close();
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_ID, task.getGroupId());
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DEADLINE, task.getTitle());
        values.put(KEY_DETAIL, task.getDetail());
        values.put(KEY_REMINDER_TIME, task.getReminderTime());
        values.put(KEY_UPDATED_AT, task.getUpdatedAt());
        values.put(KEY_DONE_AT, task.getDoneAt());
        values.put(KEY_ETAG, task.getETag());

        int id = db.update(TABLE_TASK, //table
                values,
                KEY_ID + " = ?",
                new String[]{String.valueOf(task.getTaskId())});
        db.close();

        return id;
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

        return themeList;
    }

    private List<Task> getComletedTasks(boolean tf) {
        db = dbHelper.getReadableDatabase();
        List<Task> themeList = new ArrayList<Task>();
        String str_tf = (tf ? "1" : "0");
        Cursor c = db.query(
                this.tableName,
                null,
                KEY_DONE_AT + " = ?",
                new String[]{str_tf},
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

        return themeList;
    }

    public List<Task> getCompletedTasks() { return getComletedTasks(true); }
    public List<Task> getUnCompletedTasks() { return getComletedTasks(false); }

    private Task cursor2task(Cursor c) {
        Task t = new Task(
                c.getLong(c.getColumnIndex(KEY_ID)),
                c.getLong(c.getColumnIndex(KEY_GROUP_ID)),
                c.getString(c.getColumnIndex(KEY_TITLE)),
                c.getLong(c.getColumnIndex(KEY_DEADLINE)),
                c.getString(c.getColumnIndex(KEY_DETAIL)),
                c.getString(c.getColumnIndex(KEY_REMINDER_TIME)),
                c.getLong(c.getColumnIndex(KEY_CREATED_AT)),
                c.getLong(c.getColumnIndex(KEY_UPDATED_AT)),
                c.getLong(c.getColumnIndex(KEY_DONE_AT)),
                c.getString(c.getColumnIndex(KEY_ETAG))
        );

        return t;
    }
}