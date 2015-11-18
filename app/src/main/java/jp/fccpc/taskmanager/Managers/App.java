package jp.fccpc.taskmanager.Managers;

import android.app.Application;

import jp.fccpc.taskmanager.Managers.mock.GroupManagerMock;
import jp.fccpc.taskmanager.Managers.mock.TaskManagerMock;
import jp.fccpc.taskmanager.Managers.mock.UserManagerMock;

/**
 * Created by nakac on 15/10/23.
 */
public class App extends Application {
    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        Context context = getApplicationContext();
//        taskManager = new TaskManagerImpl(context);
//        userManager = new UserManagerImpl(context);
//        groupManager = new GroupManagerImpl(context);
//        authManager = new AuthManagerImpl(context);
        taskManager = new TaskManagerMock();
        userManager = new UserManagerMock();
        groupManager = new GroupManagerMock(userManager);
    }

    GroupManager groupManager;
    TaskManager taskManager;
    UserManager userManager;
    AuthManager authManager;

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}
