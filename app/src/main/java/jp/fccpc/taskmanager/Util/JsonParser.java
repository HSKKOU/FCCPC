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

        if(ja == null) { return null; }

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Task t = new Task(
                        getLongJSON(jo, "id"),
                        getLongJSON(jo, "group_id"),
                        getStringJSON(jo, "title"),
                        getLongJSON(jo, "deadline"),
                        getStringJSON(jo, "detail"),
                        getStringJSON(jo, "remainder_time"),
                        getLongJSON(jo, "created_at"),
                        getLongJSON(jo, "updated_at"),
                        getLongJSON(jo, "done_at"),
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

        if(ja == null) { return null; }

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Group g = new Group(
                        getLongJSON(jo, "id"),
                        getStringJSON(jo, "name"),
                        getLongJSON(jo, "administrator"),
                        JsonParser.memberships(getStringJSON(jo, "memberships")),
                        getLongJSON(jo, "updated_at"),
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

        if(ja == null) { return null; }

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                User u = new User(
                        getLongJSON(jo, "id"),
                        getStringJSON(jo, "name"),
                        getStringJSON(jo, "email_address")
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

        if(ja == null) { return null; }

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Membership m = new Membership(
                        null,
                        getLongJSON(jo, "group_id"),
                        getLongJSON(jo, "user_id"),
                        getBoolJSON(jo, "group_agreed"),
                        getBoolJSON(jo, "user_agreed"),
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

        if(ja == null) { return null; }

        try {
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                BoardItem b = new BoardItem(
                        getLongJSON(jo, "number"),
                        getLongJSON(jo, "user_id"),
                        getStringJSON(jo, "user_name"),
                        getLongJSON(jo, "created_at"),
                        getStringJSON(jo, "content")
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

        if(jo == null) { return null; }

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
        if(str == null) { return null; }
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
        if(str == null) { return null; }
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


    private static Long getLongJSON(JSONObject jo, String cName) {
        Long retL = null;
        try{
            retL = jo.getLong(cName);
        } catch(JSONException e) {
            retL = null;
        }

        return retL;
    }

    private static String getStringJSON(JSONObject jo, String cName) {
        String retS = null;
        try{
            retS = jo.getString(cName);
        } catch(JSONException e) {
            retS = null;
        }

        return retS;
    }

    private static boolean getBoolJSON(JSONObject jo, String cName) {
        boolean retB = false;
        try{
            retB = jo.getBoolean(cName);
        } catch(JSONException e) {
            retB = false;
        }

        return retB;
    }
}
