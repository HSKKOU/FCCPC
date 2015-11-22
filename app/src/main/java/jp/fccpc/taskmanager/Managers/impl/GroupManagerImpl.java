package jp.fccpc.taskmanager.Managers.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.SQLite.Controller.GroupDataController;
import jp.fccpc.taskmanager.SQLite.Controller.MembershipDataController;
import jp.fccpc.taskmanager.SQLite.Controller.UserDataController;
import jp.fccpc.taskmanager.Server.EndPoint;
import jp.fccpc.taskmanager.Server.Response;
import jp.fccpc.taskmanager.Server.ServerConnector;
import jp.fccpc.taskmanager.Util.JsonParser;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by Shunta on 10/22/15.
 */
public class GroupManagerImpl extends ManagerImpl implements GroupManager {
    private GroupDataController groupDataController;
    private MembershipDataController membershipDataController;

    public GroupManagerImpl(Context context) {
        super(context);
        this.groupDataController = new GroupDataController(context);
        this.membershipDataController = new MembershipDataController(context);
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
    public void search(final String groupName, String adminName, final GroupListCallback callback) {
        if (isOnline()) {
            this.searchWithAdminName(adminName, new GroupListCallback() {
                @Override
                public void callback(List<Group> groupList) {
                    List<Group> gList = new ArrayList<Group>();
                    for(Group g : groupList) {
                        if(groupName.equals(g.getName())) { gList.add(g); }
                    }

                    callback.callback(gList);
                }
            });
        } else {
            callback.callback(null);
        }
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
                    UserDataController udc = new UserDataController(context);
                    udc.updateUser(u);

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
                    groupDataController.createGroup(group);
                    createMemberships(group.getMemberships(), new Callback() {
                        @Override
                        public void callback(boolean success) {
                            callback.callback(success);
                        }
                    });
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
    public void update(final Group group, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    groupDataController.updateGroup(group);
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
    public void delete(final Long groupId, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    groupDataController.deleteGroup(new Group(groupId, null, null, null, null));
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

    @Override
    public void createMembership(final Membership membership, final Callback callback) {
        if(membership.isGroupAgreed() && membership.isUserAgreed()) {
            callback.callback(false);
            return;
        }

        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    membershipDataController.createMembership(membership);
                    callback.callback(true);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(false);
                }
            });

            // TODO: check endpoint
            String endPoint = EndPoint.membership(membership.getGroupId(), membership.getUserId());
            String method = ServerConnector.POST;
            String params = makeParamsString(
                    new String[]{"group_agreed", "user_agreed"},
                    new String[]{String.valueOf(membership.isGroupAgreed()), String.valueOf(membership.isUserAgreed())});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }


    // TODO: should fix multi thread
    private int finished = 0;
    @Override
    public void createMemberships(List<Membership> memberships, final Callback callback) {
        if(memberships.size() == 0) {
            callback.callback(true);
            return;
        }

        finished = 0;

        final int membershipLength = memberships.size();
        for(Membership m : memberships) {
            createMembership(m, new Callback() {
                @Override
                public void callback(boolean success) {
                    finished++;
                    if(finished == membershipLength) {
                        callback.callback(true);
                    }
                }
            });
        }
    }

    @Override
    public void updateMembership(final Membership membership, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    membershipDataController.updateMembership(membership);
                    callback.callback(true);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(false);
                }
            });

            String endPoint = EndPoint.membership(membership.getGroupId(), membership.getUserId());
            String method = ServerConnector.PUT;
            String params = makeParamsString(
                    new String[]{"group_agreed", "user_agreed"},
                    new String[]{String.valueOf(membership.isGroupAgreed()), String.valueOf(membership.isUserAgreed())});

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }

    @Override
    public void deleteMembership(final Membership membership, final Callback callback) {
        if (isOnline()) {
            ServerConnector sc = new ServerConnector(context, new ServerConnector.ServerConnectorDelegate() {
                @Override
                public void success(Response response) {
                    membershipDataController.deleteMembership(membership);
                    callback.callback(true);
                }

                @Override
                public void failure(Response response) {
                    callback.callback(false);
                }
            });

            String endPoint = EndPoint.membership(membership.getGroupId(), membership.getUserId());
            String method = ServerConnector.DELETE;
            String params = null;

            sc.execute(endPoint, method, params, null);
        } else {
            callback.callback(false);
        }
    }
}
