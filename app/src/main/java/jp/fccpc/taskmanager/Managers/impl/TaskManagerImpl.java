package jp.fccpc.taskmanager.Managers.impl;

import android.content.Context;

import java.util.List;

import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.SQLite.Controller.TaskDataController;
import jp.fccpc.taskmanager.Server.EndPoint;
import jp.fccpc.taskmanager.Server.Response;
import jp.fccpc.taskmanager.Server.ServerConnector;
import jp.fccpc.taskmanager.Util.JsonParser;
import jp.fccpc.taskmanager.Values.BoardItem;
import jp.fccpc.taskmanager.Values.Task;

/**
 * Created by Shunta on 10/22/15.
 */
public class TaskManagerImpl extends ManagerImpl implements TaskManager {
    private TaskDataController taskDataController;

    public TaskManagerImpl(Context context) {
        super(context);
        this.taskDataController = new TaskDataController(context);
    }

    @Override
    public void getList(final TaskListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    List<Task> taskList = JsonParser.tasks(response.bodyJSON, response.ETag);
                    for (Task t : taskList) {taskDataController.updateTask(t);}

                    callback.callback(taskList);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.task(null);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(this.taskDataController.getAllTasks());
        }
    }

    @Override
    public void getList(Long groupId, final TaskListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    List<Task> taskList = JsonParser.tasks(response.bodyJSON, response.ETag);
                    for (Task t : taskList) {taskDataController.updateTask(t);}

                    callback.callback(taskList);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.groupTasks(groupId);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(this.taskDataController.getAllTasks(groupId));
        }
    }

    @Override
    public void get(Long taskId, final TaskCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    Task task = JsonParser.tasks(response.bodyJSON, response.ETag).get(0);
                    taskDataController.updateTask(task);
                    callback.callback(task);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.task(taskId);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(this.taskDataController.getTask(taskId));
        }
    }

    @Override
    public void getBoardItems(Long taskId, int begin, int end, final BoardItemListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    callback.callback(JsonParser.boardItems(response.bodyJSON));
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.boardItem(taskId);
            String method = ServerConnector.GET;
            String params = makeParamsString(new String[]{"range"},
                    new String[]{begin + "-" + end});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(null);
        }
    }

    @Override
    public void getBoardItems(Long taskId, int num, final BoardItemListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    callback.callback(JsonParser.boardItems(response.bodyJSON));
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.boardItem(taskId);
            String method = ServerConnector.GET;
            String params = makeParamsString(new String[]{"range"}, new String[]{num+""});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(null);
        }
    }

    @Override
    public void create(Task task, final Callback callback) {
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

            String endPoint = EndPoint.task(null);
            String method = ServerConnector.POST;
            String params = makeParamsString(
                    new String[]{"group_id", "title", "deadline", "detail"},
                    new String[]{
                            Long.toString(task.getGroupId()),
                            task.getTitle(),
                            Long.toString(task.getDeadline()),
                            task.getDetail()
                    });

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void createBoardItem(Long taskId, BoardItem boardItem, final Callback callback) {
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

            String endPoint = EndPoint.boardItem(taskId);
            String method = ServerConnector.POST;
            String params = makeParamsString(
                    new String[]{"content"},
                    new String[]{boardItem.getContent()});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void update(Task task, final Callback callback) {
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

            String endPoint = EndPoint.task(task.getTaskId());
            String method = ServerConnector.PUT;
            String params = makeParamsString(
                    new String[]{"group_id", "title", "deadline", "detail"},
                    new String[]{
                            Long.toString(task.getGroupId()),
                            task.getTitle(),
                            Long.toString(task.getDeadline()),
                            task.getDetail()
                    });

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void delete(Long taskId, final Callback callback) {
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

            String endPoint = EndPoint.task(taskId);
            String method = ServerConnector.DELETE;
            String params = "";

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void finish(List<Long> taskIds, final Callback callback) {
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

            String endPoint = EndPoint.taskFinished(null);
            String method = ServerConnector.POST;
            String params = "";
            boolean first = true;
            for (Long taskId: taskIds) {
                if (first) {
                    first = false;
                } else {
                    params += ",";
                }
                params += taskId;
            }

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void getFinishedUserList(Long taskId, final UserListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    callback.callback(JsonParser.users(response.bodyJSON));
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.taskFinished(taskId);
            String method = ServerConnector.GET;
            String params = "";

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(null);
        }
    }

}
