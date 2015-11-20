package jp.fccpc.taskmanager.Managers.impl;

import android.content.Context;

import java.util.List;

import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.SQLite.Controller.GroupDataController;
import jp.fccpc.taskmanager.Server.EndPoint;
import jp.fccpc.taskmanager.Server.Response;
import jp.fccpc.taskmanager.Server.ServerConnector;
import jp.fccpc.taskmanager.Util.JsonParser;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by Shunta on 10/22/15.
 */
public class GroupManagerImpl extends ManagerImpl implements GroupManager {
    private GroupDataController groupDataController;

    public GroupManagerImpl(Context context) {
        super(context);
        this.groupDataController = new GroupDataController(context);
    }

    @Override
    public void getList(final GroupListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    List<Group> groupList = JsonParser.groups(response.bodyJSON, response.ETag);
                    for (Group g : groupList) {
                        groupDataController.updateGroup(g);
                    }

                    callback.callback(groupList);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.group(null);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(this.groupDataController.getAllGroups());
        }
    }

    @Override
    public void get(Long groupId, final GroupCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    Group g = JsonParser.groups(response.bodyJSON, response.ETag).get(0);
                    groupDataController.updateGroup(g);

                    callback.callback(g);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.group(groupId);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(this.groupDataController.getGroup(groupId));
        }
    }

    @Override
    public void search(String groupName, String adminName, final GroupListCallback callback) {
        callback.callback(null);
        return;

//        if (isOnline()) {
//            ServerConnector sc = new ServerConnector(new ServerConnector.ServerConnectorDelegate() {
//                @Override
//                public void recieveResponse(String responseStr) {
//                    List<Group> groupList = JsonParser.groups(responseStr);
//                    for(Group g : groupList) { groupDataController.updateGroup(g); }
//
//                    callback.callback(groupList);
//                }
//            });
//
//            String endPoint = EndPoint.group(null);
//            String method = ServerConnector.GET;
//            String params = null;
//
//            sc.execute(endPoint, method, params, null);
//        } else {
//            callback.callback(null);
//        }
    }

    @Override
    public void searchWithGroupName(String groupName, final GroupListCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    List<Group> groupList = JsonParser.groups(response.bodyJSON, response.ETag);
                    for (Group g : groupList) {groupDataController.updateGroup(g);}

                    callback.callback(groupList);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(null);
                }
            });

            String endPoint = EndPoint.group(null);
            String method = ServerConnector.GET;
            String params = "name=" + groupName;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(null);
        }
    }

    @Override
    public void searchWithAdminName(String adminName, final GroupListCallback callback) {
        if (isOnline()) {

            UserManager um = new UserManagerImpl(context);

            um.searchUser(adminName, new UserManager.UserListCallback() {
                @Override
                public void callback(List<User> userList) {
                    if(userList == null || userList.size() == 0) {
                        callback.callback(null);
                        return;
                    }

                    User u = userList.get(0);

                    ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                        @Override
                        public void success(Response response) {
                            List<Group> groupList = JsonParser.groups(response.bodyJSON, response.ETag);
                            for (Group g : groupList) {groupDataController.updateGroup(g);}

                            callback.callback(groupList);
                        }

                        @Override
                        public void failure(Response response) {
                            callback.callback(null);
                        }
                    });

                    String endPoint = EndPoint.group(null);
                    String method = ServerConnector.GET;
                    String params = "administrator=" + u.getUserId();

                    sc.execute(endPoint, method, params, null);
                }
            });

        } else {
            callback.callback(null);
        }
    }

    @Override
    public void create(final Group group, final Callback callback) {
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

            String endPoint = EndPoint.group(null);
            String method = ServerConnector.POST;
            String params = makeParamsString(new String []{"name"},
                    new String[] {group.getName()});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void update(Group group, final Callback callback) {
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

            String endPoint = EndPoint.group(group.getGroupId());
            String method = ServerConnector.PUT;
            String params = makeParamsString(new String []{"name"},
                    new String[] {group.getName()});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void delete(Long groupId, final Callback callback) {
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

            String endPoint = EndPoint.group(groupId);
            String method = ServerConnector.DELETE;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }
}
