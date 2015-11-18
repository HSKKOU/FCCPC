package jp.fccpc.taskmanager.Managers.impl;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by Shunta on 10/27/15.
 */
public class ManagerImpl {
    protected Context context;

    ManagerImpl(Context context) {
        this.context = context;
    }

    protected boolean isOnline () {
        // TODO: implement
        return true;
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String makeParamsString(String[] keys, String[] values) {
        String paramsString = "";
        for (int i = 0; i < Math.min(keys.length, values.length); i++) {
            paramsString += encode(keys[i]) + "=" + encode(values[i]);
        }

        return paramsString;
    }
}