package jp.fccpc.taskmanager.SQLite.Model;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import jp.fccpc.taskmanager.Server.ServerConnector;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class Task {
    public static final String TAG = Task.class.getSimpleName();
    private long id;
    private String title;
    private String content;
    private long deadline;
    private boolean completeFlag;
    private long created_at;
    private long updated_at;
    private String[] KEYS = {"group", "title", "deadline", "detail"};

    public Task(){}

    public Task(String _title, String _content, long _deadline, boolean _completeFlag, long _created_at){
        super();
        this.title = _title;
        this.content = _content;
        this.deadline = _deadline;
        this.completeFlag = _completeFlag;
        this.created_at = _created_at;
    }

    public Task(long _id, String _title, String _content, long _deadline, boolean _completeFlag, long _created_at){
        this(_title, _content, _deadline, _completeFlag, _created_at);
        this.id = _id;
    }

    public Task(long _id, String _title, String _content, long _deadline, int _completeFlag, long _created_at){
        this(_title, _content, _deadline, (_completeFlag != 0), _created_at);
        this.id = _id;
    }

    // id
    public long getId(){return this.id;}

    // title
    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title = title;}

    // content
    public String getContent(){return this.content;}
    public void setContent(String content){this.content = content;}

    // deadline
    public long getDeadline(){return this.deadline;}
    public void setDeadline(long dealine){this.deadline = dealine;}

    // completFlag
    public boolean getCompleteFlag(){return this.completeFlag;}
    public void setCompleteFlag(int completeFlag){this.completeFlag = (completeFlag != 0);}
    public void setCompleteFlag(boolean completeFlag){this.completeFlag = completeFlag;}

    // created_at
    public long getCreated_at(){return this.created_at;}
    public void setCreated_at(long created_at){this.created_at = created_at;}

    // updated_at
    public long getUpdated_at(){return this.updated_at;}
    public void setUpdated_at(long updated_at){this.updated_at = updated_at;}

    public String toString(){
        return this.getId() + ", "
                + this.getTitle() + ", "
                + this.getContent() + ", "
                + this.getDeadline() + ", "
                + this.getCompleteFlag() + ", "
                + this.getCreated_at() + "";

    }

    public String getQuery() throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        // private String[] KEYS = {"group", "title", "deadline", "detail"};
        // TODO: replace dummy_group
        String[] values = {"12345", this.getTitle(), this.getDeadline()+"", this.getContent()+""};
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