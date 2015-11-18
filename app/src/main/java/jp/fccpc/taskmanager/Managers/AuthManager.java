package jp.fccpc.taskmanager.Managers;

import android.content.Context;

/**
 * Created by hskk1120551 on 15/11/12.
 */
public interface AuthManager {
    /*
     * callback params
     *   success: サーバー通信成功フラグ
     *   data: 通信成功ならばnull, 失敗ならばエラーメッセージ
     *
     * */
    interface Callback { public void recieveResponse(boolean success, String data); }

    public void login(final Context context, final String userName, String password, Callback callback);

    public void logout(final Context context, String userName, Callback callback);
}