package jp.fccpc.taskmanager.Managers.impl;

import android.content.Context;

import java.util.List;

import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.SQLite.Controller.GroupDataController;
import jp.fccpc.taskmanager.Server.EndPoint;
import jp.fccpc.taskmanager.Server.ServerConnector;
import jp.fccpc.taskmanager.Util.JsonParser;
import jp.fccpc.taskmanager.Values.Group;

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
                public void recieveResponse(String responseStr) {
                    callback.callback(JsonParser.groups(responseStr));
                }
            });

            String endPoint = EndPoint.group(null);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            // TODO: sql
        }
    }

    @Override
    public void get(Long groupId, final GroupCallback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void recieveResponse(String responseStr) {
                    callback.callback(JsonParser.groups(responseStr).get(0));
                }
            });

            String endPoint = EndPoint.group(groupId);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            // TODO: sql
        }
    }

    @Override
    public void search(String groupName, String adminName, final GroupListCallback callback) {
        if (isOnline()) {
            /*
            ServerConnector sc = new ServerConnector(new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void recieveResponse(String responseStr) {
                    callback.callback(JsonParser.groups(responseStr));
                }
            });

            String endPoint = EndPoint.group(null);
            String method = ServerConnector.GET;
            String params = null;

            sc.execute(endPoint, method, params, null);
            */
        } else {
            // TODO: sql
        }
    }

    @Override
    public void searchWithGroupName(String groupName, final GroupListCallback callback) {

    }

    @Override
    public void searchWithAdminName(String adminName, final GroupListCallback callback) {

    }

    @Override
    public void create(Group group, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void recieveResponse(String responseStr) {
                    callback.callback(true);
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
                public void recieveResponse(String responseStr) {
                    callback.callback(true);
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
                public void recieveResponse(String responseStr) {
                    callback.callback(true);
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
