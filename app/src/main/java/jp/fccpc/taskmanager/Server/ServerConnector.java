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

import jp.fccpc.taskmanager.SQLite.Controller.UserDataController;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class ServerConnector extends AsyncTask<String, Integer, Response> {

    public interface ServerConnectorDelegate {
        void success(Response response);
        void failure(Response response);
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

    private String eTag = "";

    private Context context;

    private ServerConnectorDelegate delegate;

    // for Debug
    public ServerConnector(ServerConnectorDelegate delegate) {
        this.delegate = delegate;
    }

    public ServerConnector(Context context, ServerConnectorDelegate delegate) {
        this.context = context;
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


    private Response decodeResponse(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();

        InputStream is = null;

        Log.d(TAG, "response code : " + responseCode);

        if (responseCode / 100 == 4 || responseCode / 100 == 5) {
            return this.failResponse(con);
        } else {
            return this.successResponse(con);
        }
    }

    private Response successResponse(HttpURLConnection con) throws IOException {
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, CHAR_SET));

//        Map<String,List<String>> headers = con.getHeaderFields();
//        for(String key : headers.keySet()) {
//            Log.d(TAG, "key: " + key + ", value: " + headers.get(key));
//        }

        // TODO: implement ETag
        return new Response(this.buf2str(br), "ETag");
    }

    private Response failResponse(HttpURLConnection con) throws IOException {
//        InputStream errIS = con.getErrorStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(errIS, CHAR_SET));
//        return this.buf2str(br);
        // TODO: error handlers
        return new Response("err", null);
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
        String token = this.getTokenFromSQL();
        if(token != null && !"".equals(token)) { con.setRequestProperty("Authorization", "Token " + token); }
        if(this.eTag != null && !"".equals(this.eTag)) { con.setRequestProperty("If-Match", "ETag " + this.eTag); }
    }

    private HttpURLConnection connectServer(URL _url) throws IOException {
        return (HttpURLConnection) _url.openConnection();
    }

    private String getTokenFromSQL() {
        UserDataController udc = new UserDataController(this.context);
        return udc.getToken();
    }

    @Override
    protected Response doInBackground(String... params) {
        String endPointStr = params[0];
        String methodStr = params[1];
        String paramsStr = params[2];
        String eTag = params[3];

        Log.d(TAG, "endpoint: " + endPointStr + ", method: " + methodStr + ", params: " + paramsStr + ", eTag: " + eTag);

        this.eTag = eTag;

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

            return this.decodeResponse(con);
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Response r) {
        if(r != null) Log.d(TAG, r.toString());

        if("err".equals(r.bodyJSON)) {
            this.delegate.failure(r);
        } else {
            this.delegate.success(r);
        }
    }
}
