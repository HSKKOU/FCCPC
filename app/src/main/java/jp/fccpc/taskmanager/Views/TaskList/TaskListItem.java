package jp.fccpc.taskmanager.Views.TaskList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by hskk1120551 on 2015/10/16.
 */

public class TaskListItem implements Parcelable {
    public Long id;
    public String title;
    public Long date;  // todo : name should be changed to 'deadline' ?
    public boolean checked;

    private TaskListFragment mTaskListFragment;

    private final long MIL_SECS_OF_MINUTE = 1000 * 60;
    private final long MIL_SECS_OF_HOUR = MIL_SECS_OF_MINUTE * 60;
    private final long MIL_SECS_OF_DAY  = MIL_SECS_OF_HOUR * 24;



    public TaskListItem(Long id, String title, Long date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public void setTaskListFragment(TaskListFragment taskListFragment) {
        this.mTaskListFragment = taskListFragment;
    }

    public void setChecked(boolean checked) {
        if(this.checked != checked)
            mTaskListFragment.onChecked(checked);
        this.checked = checked;
    }

    private String toRemainderString(Long date) {
        long now = new Date().getTime();
        long df = date - now;

        String pre = (df < 0) ? "" : "あと";
        String d   = Math.abs(df / MIL_SECS_OF_DAY) > 0 ? "" + Math.abs(df / MIL_SECS_OF_DAY) + "日" :
                     Math.abs(df / MIL_SECS_OF_HOUR) > 0 ? "" + Math.abs(df / MIL_SECS_OF_HOUR) + "時間" :
                                                   "" + Math.abs(df / MIL_SECS_OF_MINUTE) + "分";
        String suf = (df < 0) ? "経過" : "";

        return pre + d + suf;
    }

    public String getDateString() {
        return toRemainderString(date);
    }

    public boolean isNearDeadline(){
        return (date - new Date().getTime()) < MIL_SECS_OF_HOUR * 24; // 締め切りが 1 日以内になったら true
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(title);
        dest.writeString(getDateString());
    }

    public Long getId(){
        return id;
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<TaskListItem> CREATOR = new Creator<TaskListItem>() {
        @Override
        public TaskListItem createFromParcel(Parcel source) {
            return null;
        }

        public TaskListItem[] newArray(int size) {
            return new TaskListItem[size];
        }
    };
}
