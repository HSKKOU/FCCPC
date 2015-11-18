package jp.fccpc.taskmanager.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class ServerConnector extends AsyncTask<String, Integer, String> {
    public interface ServerConnectorDelegate {
        void recieveResponse(String responseStr);
    }

    public static final String GET = "GET";         // 取得
    public static final String PUT = "PUT";         // 更新
    public static final String POST = "POST";       // 追加
    public static final String DELETE = "DELETE";   // 削除

    //private static final String POST_CHECK_URL = "http://www.muryou-tools.com/test/aaaa.php";
    private static final String POST_CHECK_URL = "https://www.kojikoji.net/";

    private static final String HOST_PORT = "fccpc.fruit-sandwich.com:3000/";
    private static final String API_PATH = "api/";

    private static final String API_BASE_URL = "https://" + HOST_PORT + API_PATH;

    private static String TAG = "ServerConnector";
    public static final String CHAR_SET = "UTF-8";

    private String token = "";
    public void setToken(String token) {this.token = token;}

    private ServerConnectorDelegate delegate;

    public ServerConnector(Context context, ServerConnectorDelegate delegate) {
        this.delegate = delegate;
    }


    private HttpURLConnection getConnection(String urlStr, String method) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = this.connectServer(url);
        con.setRequestMethod(method);
        con.setInstanceFollowRedirects(false);
        this.setDefaultHeader(con);
        con.connect();
        return con;
    }

    private HttpURLConnection getConnection(String urlStr, String method, String queryStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = this.connectServer(url);
        con.setRequestMethod(method);
        con.setDoInput(true);
        con.setDoOutput(true);
        this.setDefaultHeader(con);

        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, CHAR_SET));
        writer.write(queryStr);
        writer.flush();
        writer.close();
        os.close();

        con.connect();
        return con;
    }


    private String response2str(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();

        InputStream is = null;

        if (responseCode / 100 == 4 || responseCode / 100 == 5) {
            return this.failResponse(con);
        } else {
            return this.successResponse(con);
        }
    }

    private String successResponse(HttpURLConnection con) throws IOException {
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, CHAR_SET));
        return this.buf2str(br);
    }

    private String failResponse(HttpURLConnection con) throws IOException {
//        InputStream errIS = con.getErrorStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(errIS, CHAR_SET));
//        return this.buf2str(br);
        // TODO: error handlers
        return "err";
    }

    private String buf2str(BufferedReader br) throws IOException {
        String line = null;
        String message = "";
        while ((line = br.readLine()) != null) {
            message += line;
        }
        return message;
    }

    private void setDefaultHeader(HttpURLConnection con) {
        // set header's property
        con.setRequestProperty("Accept-Language", CHAR_SET);
        if(!"".equals(this.token)) {
            con.setRequestProperty("Authorization", "Token " + this.token);
        }
    }

    private HttpURLConnection connectServer(URL _url) throws IOException {
        return (HttpURLConnection) _url.openConnection();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground");

        String endPointStr = params[0];
        String methodStr = params[1];
        String paramsStr = params[2];
        String eTag = params[3];

        Log.d(TAG, "endpoint: " + endPointStr + ", method: " + methodStr + ", params: " + paramsStr + ", eTag: " + eTag);

        try {
            String urlStr = API_BASE_URL + endPointStr;
            HttpURLConnection con = null;

            if (methodStr.equals(GET) || methodStr.equals(DELETE)) {
                con = this.getConnection(urlStr, methodStr);
            } else if (methodStr.equals(PUT) || methodStr.equals(POST)) {
                con = this.getConnection(urlStr, methodStr, paramsStr);
            } else {
                Log.d(TAG, "method is wrong");
            }

            if (con == null) {
                return null;
            }

            return this.response2str(con);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) Log.d(TAG, s);
        this.delegate.recieveResponse(s);
    }
}
