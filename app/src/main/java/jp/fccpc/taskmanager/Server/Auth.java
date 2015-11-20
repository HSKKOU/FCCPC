package jp.fccpc.taskmanager.Server;

import android.content.Context;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import jp.fccpc.taskmanager.SQLite.Controller.UserDataController;
import jp.fccpc.taskmanager.Util.JsonParser;

/**
 * Created by hskk1120551 on 15/10/26.
 */
public class Auth {
    private static final String TAG = Auth.class.getSimpleName();


    /*
     * callback params
     *   success: サーバー通信成功フラグ
     *   data: 通信成功ならばnull, 失敗ならばエラーメッセージ
     *
     * */
    public interface AuthCallback {
        public void recieveResponse(boolean success, String data);
    }

    public static void login(final Context context, final String userName, String password, final AuthCallback callback) {
        ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
            @Override
            public void success(Response response) {
                // store token
                String token = JsonParser.loginToken(response.bodyJSON);
                Log.d(TAG, token);

                UserDataController udc = new UserDataController(context);
                udc.updateToken(token);

                callback.recieveResponse(true, "login succeed");
            }

            @Override
            public void failure(Response response) {
                callback.recieveResponse(false, response.bodyJSON);
            }
        });

        String password_hashed = getSha256(password);
        String paramStr = "user_name=" + userName + "&password=" + password_hashed;
        sc.execute(EndPoint.login(), "POST", paramStr, "");
    }

    public static void logout(final Context context, String userName, AuthCallback callback) {
        // TODO: logout function
        callback.recieveResponse(true, "");
    }

    /*
     * 文字列から SHA256 のハッシュ値を取得
     */
    private static String getSha256(String target) {
        MessageDigest md = null;
        StringBuffer buf = new StringBuffer();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(target.getBytes());
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                buf.append(String.format("%02x", digest[i]));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }
}
