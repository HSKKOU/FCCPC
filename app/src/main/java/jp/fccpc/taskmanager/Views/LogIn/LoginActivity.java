package jp.fccpc.taskmanager.Views.LogIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jp.fccpc.taskmanager.Managers.AuthManager;
import jp.fccpc.taskmanager.Managers.impl.AuthManagerImpl;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.SQLite.Controller.UserDataController;
import jp.fccpc.taskmanager.Values.User;
import jp.fccpc.taskmanager.Views.MainActivity;

/**
 * Created by tm on 2015/11/11.
 */
public class LoginActivity extends Activity {
    private Button mLoginButton;
    private EditText mUserIdText,mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // とりあえず
        //UserDataController udc = new UserDataController(getApplicationContext());
        //udc.createUser(new User(10L, "john", "email"));

        //Todo: create Login menu
        mUserIdText = (EditText) findViewById(R.id.user_id);
        mPasswordText = (EditText) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = mUserIdText.getText().toString();
                String password = mPasswordText.getText().toString();

                if (userId == null) {
                    userId = "";
                }
                if (password == null) {
                    password = "";
                }
                login(userId,password);
            }
        });
    }

    private void login(String userName,String password){
        AuthManager am = new AuthManagerImpl(this);
        am.login(this, userName, password, new AuthManager.Callback() {
            @Override
            public void recieveResponse(boolean success, String data) {
                if(success){
                    move2Main();
                }else{
                    Toast.makeText(getBaseContext(),data,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void move2Main(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}