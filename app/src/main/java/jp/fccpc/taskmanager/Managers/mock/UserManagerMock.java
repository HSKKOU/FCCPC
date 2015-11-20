package jp.fccpc.taskmanager.Managers.mock;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.Util.Utils;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by nakac on 15/10/23.
 */
public class UserManagerMock implements UserManager {

    private static final List<User> users;

    static {
        users = Lists.newArrayList(
                new User(1L, "John Doe", "john@example.com"),
                new User(2L, "Alice Cartelet", "alice@example.com"),
                new User(3L, "Shinobu Ōmiya", "shinobu@example.com"),
                new User(4L, "Aya Komichi", "aya@example.com"),
                new User(5L, "Yōko Inokuma", "yoko@example.com"),
                new User(6L, "Karen Kujō", "karen@example.com")
        );
    }

    public User get(final Long userId) {
        if (userId == null) // Own
            return users.get(0);
        return Utils.find(users, new Function<User, Boolean>() {
            @Override
            public Boolean apply(User user) {
                return user.getUserId() == userId;
            }
        });
    }

    public List<User> searchUser(final String userName) {
        if (userName == null)
            throw new IllegalArgumentException();
        return Utils.findAll(users, new Function<User, Boolean>() {
            @Override
            public Boolean apply(User user) {
                // return userName.equals(user.getName());
                return user.getName().contains(userName);
            }
        });
    }

    @Override
    public void get(Long userId, UserCallback callback) {
        callback.callback(get(userId));
    }

    @Override
    public void searchUser(String userName, UserListCallback callback) {
        callback.callback(searchUser(userName));
    }

    @Override
    public void create(User user, String password, Callback callback) {
        users.add(user);
        callback.callback(true);
    }

    @Override
    public void update(User user, Callback callback) {
        int i = 0;
        for(User u : users){
            if(user.getUserId().equals(u.getUserId())){
                users.set(i, user);
                break;
            }
            i++;
        }

        callback.callback( (i < users.size()) );
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword, Callback callback) {
        callback.callback(true);
    }
}
