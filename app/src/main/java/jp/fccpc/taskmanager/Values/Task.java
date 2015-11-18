package jp.fccpc.taskmanager.Values;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.fccpc.taskmanager.Server.ServerConnector;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class Task {
    /*
- fields
    - (id: Int) (readonly)
	- group\_id: Int
    - title: String
    - deadline: Time
    - detail: String
    - (remainder\_time: String or null): deadlineまでの残り時間，またはdeadlineを過ぎていることを表す`null`
    - (created\_at: Time) (readonly)
    - (updated\_at: Time) (readonly): サーバが自動的に更新する
    - (done\_at: Time or null) (readonly): タスクの完了時刻，または完了していないことを表す`null`
    - (eTag: String) (readonly): GETした場合に自動更新する。新規作成など、View側で生成する場合はnullを代入

     */
    public static final String TAG = Task.class.getSimpleName();
    private Long taskId;
    private Long groupId;
    private String title;
    private long deadline;
    private String detail;
    private String reminderTime;
    private long createdAt;
    private long updatedAt;
    private Long doneAt;
    private String eTag;
    private String[] KEYS = {"group", "title", "deadline", "detail"};

    public Task(Long taskId,
                Long groupId,
                String title,
                long deadline,
                String detail,
                String reminderTime,
                long createdAt,
                long updatedAt,
                Long doneAt,
                String eTag) {
        this.taskId = taskId;
        this.groupId = groupId;
        this.title = title;
        this.deadline = deadline;
        this.detail = detail;
        this.reminderTime = reminderTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.doneAt = doneAt;
        this.eTag = eTag;
    }

    public Task(Long taskId,
                Long groupId,
                String title,
                long deadline,
                String detail,
                String reminderTime,
                long createdAt,
                long updatedAt,
                Long doneAt) {
        this(taskId, groupId, title, deadline, detail, reminderTime, createdAt, updatedAt, doneAt, null);
    }

    // id
    public long getTaskId(){return this.taskId;}

    // groupId
    public long getGroupId() { return this.groupId; }

    // title
    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title = title;}

    // deadline
    public long getDeadline(){return this.deadline;}
    public void setDeadline(long dealine){this.deadline = dealine;}
    // detail
    public String getDetail(){return this.detail;}
    public void setDetail(String detail){this.detail = detail;}

    // reminderTime
    public String getReminderTime() { return this.reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }

    // createdAt
    public long getCreatedAt(){return this.createdAt;}

    // updatedAt
    public long getUpdatedAt(){return this.updatedAt;}

    // doneAt
    public Long getDoneAt() { return doneAt; }

    // eTag
    public String getETag() { return eTag; }

    public String toString(){
        return this.getTaskId() + ", "
                + this.getGroupId() + ", "
                + this.getTitle() + ", "
                + this.getDeadline() + ", "
                + this.getDetail() + ", "
                + this.getReminderTime() + ", "
                + this.getCreatedAt() + ", "
                + this.getUpdatedAt() + ", "
                + this.getDoneAt() + ", ";
    }

    public String getQuery() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        // private String[] KEYS = {"group", "title", "deadline", "detail"};
        // TODO: replace dummy_group
        String[] values = {"12345", this.getTitle(), this.getDeadline()+"", this.getDetail()+""};
        for(int i = 0; i < Math.min(values.length, KEYS.length); i++) {
            if(i > 0) result.append("&");
            result.append(URLEncoder.encode(KEYS[i], ServerConnector.CHAR_SET));
            result.append("=");
            result.append(URLEncoder.encode(values[i], ServerConnector.CHAR_SET));
        }
        Log.d(TAG, "getQuery: " + result.toString());
        return result.toString();
    }
}
