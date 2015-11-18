package jp.fccpc.taskmanager.Util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Values.BoardItem;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;
import jp.fccpc.taskmanager.Values.Task;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by Shunta on 10/22/15.
 */
public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();

    static public List<Task> tasks(String json) {
        List<Task> taskList = new ArrayList<Task>();

        JSONArray ja = str2JsonArray(json);

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Task t = new Task(
                        jo.getLong("id"),
                        jo.getLong("group_id"),
                        jo.getString("title"),
                        jo.getLong("deadline"),
                        jo.getString("detail"),
                        jo.getString("reminder_time"),
                        jo.getLong("created_at"),
                        jo.getLong("updated_at"),
                        jo.getLong("done_at"),
                        ""
                );
                taskList.add(t);
            }
        } catch(JSONException e) {
            return null;
        }

        return taskList;
    }
    static public List<Group> groups(String json) {
        List<Group> groupList = new ArrayList<Group>();

        JSONArray ja = str2JsonArray(json);

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Group g = new Group(
                        jo.getLong("id"),
                        jo.getString("name"),
                        jo.getLong("administrator"),
                        JsonParser.memberships(jo.getString("memberships")),
                        jo.getLong("updated_at"),
                        ""
                );
                groupList.add(g);
            }
        } catch(JSONException e) {
            return null;
        }

        return groupList;
    }
    static public List<User> users(String json) {
        List<User> userList = new ArrayList<User>();

        JSONArray ja = str2JsonArray(json);

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                User u = new User(
                        jo.getLong("id"),
                        jo.getString("name"),
                        jo.getString("email_address")
                );
                userList.add(u);
            }
        } catch(JSONException e) {
            return null;
        }

        return userList;
    }
    static public List<Membership> memberships(String json) {
        List<Membership> memberList = new ArrayList<Membership>();

        JSONArray ja = str2JsonArray(json);

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Membership m = new Membership(
                        null,
                        jo.getLong("group_id"),
                        jo.getLong("user_id"),
                        jo.getBoolean("group_agreed"),
                        jo.getBoolean("user_agreed"),
                        ""
                );
                memberList.add(m);
            }
        } catch(JSONException e) {
            return null;
        }

        return memberList;
    }
    static public List<BoardItem> boardItems(String json) {
        List<BoardItem> boardItemList = new ArrayList<BoardItem>();

        JSONArray ja = str2JsonArray(json);

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                BoardItem b = new BoardItem(
                        jo.getLong("number"),
                        jo.getLong("user_id"),
                        jo.getString("user_name"),
                        jo.getLong("created_at"),
                        jo.getString("content")
                );
                boardItemList.add(b);
            }
        } catch(JSONException e) {
            return null;
        }

        return boardItemList;
    }


    static public String loginToken(String json) {
        JSONObject jo = str2JsonObject(json);

        String token = null;

        try{
            token = jo.getString("token");
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON GetString Error");
        }

        return token;
    }

    static public JSONObject str2JsonObject(String str) {
        Log.d(TAG, str);
        JSONObject jo = null;
        try {
            jo = new JSONObject(str);
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON Parse Error");
        }

        return jo;
    }

    static public JSONArray str2JsonArray(String str) {
        Log.d(TAG, str);
        JSONArray ja = null;
        try {
            ja = new JSONArray(str);
        } catch(JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "JSON Parse Error");
        }

        return ja;
    }


    // TODO: implement translate
    static public String membership2str(List<Membership> l) {
        JSONArray ret = new JSONArray();
        try{
            for(int i=0; i<l.size(); i++) {
                Membership m = l.get(i);
                JSONObject jo = new JSONObject();
                jo.put("group_id", m.getGroupId());
                jo.put("user_id", m.getUserId());
                jo.put("group_agreed", m.isGroupAgreed());
                jo.put("user_agreed", m.isUserAgreed());
                ret.put(jo);
            }
        } catch(JSONException e) {
            Log.e(TAG, "this Membership Object cannot translate to JSON");
            return null;
        }

        return ret.toString();
    }
}
