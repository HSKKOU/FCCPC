package jp.fccpc.taskmanager.Managers.impl;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.SQLite.Controller.UserDataController;
import jp.fccpc.taskmanager.Server.EndPoint;
import jp.fccpc.taskmanager.Server.Response;
import jp.fccpc.taskmanager.Server.ServerConnector;
import jp.fccpc.taskmanager.Util.JsonParser;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by Shunta on 10/22/15.
 */
public class UserManagerImpl extends ManagerImpl implements UserManager {
    static private final String NAME_KEY = "name";
    static private final String EMAIL_KEY = "email_address";
    static private final String OLD_PASSWORD_KEY = "old_password";
    static private final String PASSWORD_KEY = "password";
    static private final String QUERY_KEY = "q";

    private UserDataController userDataController;

    public UserManagerImpl(Context context) {
        super(context);
        this.userDataController = new UserDataController(context);
    }


    private String hashed(String str) {
        MessageDigest md;
        StringBuffer buf = new StringBuffer();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                buf.append(String.format("%02x", digest[i]));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    @Override
    public void get(Long userId, final UserCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    User u = JsonParser.users(response.bodyJSON).get(0);
                    userDataController.updateUser(u);
                    callback.callback(u);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.user(userId);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(this.userDataController.getUser(userId));
        }
    }

    @Override
    public void searchUser(String userName, final UserListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context ,new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    List<User> uList = JsonParser.users(response.bodyJSON);
                    if(uList != null && uList.size() != 0) {
                        for(User u : uList) {
                            userDataController.updateUser(u);
                        }
                    }
                    callback.callback(uList);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.userSearch();
            String method = ServerConnector.GET;
            String params = makeParamsString(new String[]{QUERY_KEY}, new String[]{userName});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(null);
        }
    }

    @Override
    public void create(User user, String password, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    callback.callback(true);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(false);
                }
            });

            String endPoint = EndPoint.user(null);
            String method = ServerConnector.POST;
            String params = makeParamsString(new String[]{NAME_KEY, EMAIL_KEY, PASSWORD_KEY},
                    new String[]{user.getName(), user.getEmailAddress(), hashed(password)});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void update(final User user, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    userDataController.updateUser(user);
                    callback.callback(true);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(false);
                }
            });

            String endPoint = EndPoint.user(user.getUserId());
            String method = ServerConnector.PUT;
            String params = makeParamsString(new String[]{NAME_KEY, EMAIL_KEY},
                    new String[]{user.getName(), user.getEmailAddress()});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    callback.callback(true);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(false);
                }
            });

            String endPoint = EndPoint.user(null);
            String method = ServerConnector.PUT;
            String params = makeParamsString(new String[]{OLD_PASSWORD_KEY, PASSWORD_KEY},
                    new String[]{hashed(oldPassword), hashed(newPassword)});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }
}
