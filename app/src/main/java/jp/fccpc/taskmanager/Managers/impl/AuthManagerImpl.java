package jp.fccpc.taskmanager.Managers.impl;

import android.content.Context;
import android.util.Log;

import jp.fccpc.taskmanager.Managers.AuthManager;
import jp.fccpc.taskmanager.Server.Auth;

/**
 * Created by hskk1120551 on 15/11/12.
 */
public class AuthManagerImpl extends ManagerImpl implements AuthManager {
    private static final String TAG = AuthManagerImpl.class.getSimpleName();

    public AuthManagerImpl(Context context) {
        super(context);
    }

    @Override
    public void login(Context context, String userName, String password, final Callback callback) {
        if(isOnline()) {
            Auth.login(context, userName, password, new Auth.AuthCallback() {
                @Override
                public void recieveResponse(boolean success, String data) {
                    callback.recieveResponse(success, data);
                }
            });
        } else {
            Log.d(TAG, "not online");
        }
    }

    @Override
    public void logout(Context context, String userName, final Callback callback) {
        if(isOnline()) {
            Auth.logout(context, userName, new Auth.AuthCallback() {
                @Override
                public void recieveResponse(boolean success, String data) {
                    callback.recieveResponse(success, data);
                }
            });
        } else {
            Log.d(TAG, "not online");
        }
    }
}
