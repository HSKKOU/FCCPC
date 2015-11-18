package jp.fccpc.taskmanager.Managers;

import java.util.List;

import jp.fccpc.taskmanager.Values.BoardItem;
import jp.fccpc.taskmanager.Values.Task;
import jp.fccpc.taskmanager.Values.User;


/**
 * Created by nakac on 15/10/22.
 */
public interface TaskManager {

    interface Callback { void callback(boolean success); }
    interface TaskCallback { void callback(Task task); }
    interface TaskListCallback { void callback(List<Task> taskList); }
    interface BoardItemListCallback { void callback(List<BoardItem> boardItemList); }
    interface UserListCallback { void callback(List<User> userList); }

    void getList(TaskListCallback callback);

    void getList(Long groupId, TaskListCallback callback);

    void get(Long taskId, TaskCallback callback);

    void getBoardItems(Long taskId, int begin, int end, BoardItemListCallback callback);
    void getBoardItems(Long taskId, int num, BoardItemListCallback callback);

    void create(Task task, Callback callback);

    void createBoardItem(Long taskId, BoardItem boardItem, Callback callback);

    void update(Task task, Callback callback);

    void delete(Long taskId, Callback callback);

    void finish(List<Long> taskId, Callback callback);

    void getFinishedUserList(Long taskId, UserListCallback callback);
}
