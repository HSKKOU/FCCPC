package jp.fccpc.taskmanager.Server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class ConnectivityCheck {
    private static final String TAG = ConnectivityCheck.class.getSimpleName();
    private static final String DISACTIVE_MESSAGE = "connection disactive";
    private static final String NOT_AVAILABLE_MESSAGE = "not available";
    private static final String NOT_CONNECTED_MESSAGE = "not connected";
    private static final String CONNECTED_MESSAGE = "connected: ";

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }

    public static boolean isAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network == null) {
            showMessage(context, DISACTIVE_MESSAGE);
        } else {
            if(!network.isAvailable()) {
                showMessage(context, NOT_AVAILABLE_MESSAGE);
            } else if(!network.isConnectedOrConnecting()) {
                showMessage(context, NOT_CONNECTED_MESSAGE);
            } else {
                showMessage(context, CONNECTED_MESSAGE + network.getTypeName());
                return true;
            }
        }
        return false;
    }
}
