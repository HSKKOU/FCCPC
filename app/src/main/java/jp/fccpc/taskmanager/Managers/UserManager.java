package jp.fccpc.taskmanager.Managers;

import java.util.List;

import jp.fccpc.taskmanager.Values.User;

/**
 * Created by nakac on 15/10/22.
 */
public interface UserManager {
    interface Callback { void callback(boolean success); }
    interface UserCallback { void callback(User user); }
    interface UserListCallback { void callback(List<User> userList); }

    void get(Long userId, UserCallback callback);

    void searchUser(String userName, UserListCallback callback);

    void create(User user, String password, Callback callback);

    void update(User user, Callback callback);

    void updatePassword(String oldPassword, String newPassword, Callback callback);
}
