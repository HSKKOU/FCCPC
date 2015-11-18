package jp.fccpc.taskmanager.Managers.mock;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.Util.Utils;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by nakac on 15/10/23.
 */
public class GroupManagerMock implements GroupManager {

    private static final List<Group> groups;
    private static final List<Group> allGroups;
    private UserManager userManager;

    private Long nextId = 11L;

    static {
        groups = Lists.newArrayList(
                new Group(1L, "dummyGroup A", 1L,
                         Lists.<Membership>newArrayList(new Membership (1L, 1L, true, true),
                                                new Membership (1L, 2L, true, true),
                                                new Membership (1L, 3L, true, false),
                                                new Membership (1L, 4L, false, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(2L, "dummyGroup B", 1L,
                        Lists.<Membership>newArrayList(new Membership (1L, 1L, true, true),
                                new Membership (1L, 2L, true, true),
                                new Membership (1L, 4L, false, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(3L, "dummyGroup C", 3L,
                        Lists.<Membership>newArrayList(new Membership (3L, 3L, true, true),
                                new Membership (3L, 1L, true, false),
                                new Membership (3L, 4L, false, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(4L, "dummyGroup D", 2L,
                        Lists.<Membership>newArrayList(new Membership (2L, 2L, true, true),
                                new Membership (2L, 5L, true, true),
                                new Membership (2L, 3L, true, false),
                                new Membership (2L, 1L, false, true)), Calendar.getInstance().getTimeInMillis())
        );

        allGroups = Lists.newArrayList(
                new Group(1L, "dummyGroup A", 1L,
                        Lists.<Membership>newArrayList(new Membership (1L, 1L, true, true),
                                new Membership (1L, 2L, true, true),
                                new Membership (1L, 3L, true, false),
                                new Membership (1L, 4L, false, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(2L, "dummyGroup B", 1L,
                        Lists.<Membership>newArrayList(new Membership (1L, 1L, true, true),
                                new Membership (1L, 2L, true, true),
                                new Membership (1L, 4L, false, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(3L, "dummyGroup C", 3L,
                        Lists.<Membership>newArrayList(new Membership (3L, 3L, true, true),
                                new Membership (3L, 1L, true, false),
                                new Membership (3L, 4L, false, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(4L, "dummyGroup D", 2L,
                        Lists.<Membership>newArrayList(new Membership (2L, 2L, true, true),
                                new Membership (2L, 5L, true, true),
                                new Membership (2L, 3L, true, false),
                                new Membership (2L, 1L, false, true)), Calendar.getInstance().getTimeInMillis()),

                new Group(5L, "dummyGroup E", 2L,
                        Lists.<Membership>newArrayList(new Membership(2L, 2L, true, true),
                                new Membership(2L, 4L, true, false)), Calendar.getInstance().getTimeInMillis()),
                new Group(6L, "dummyGroup F", 6L,
                        Lists.<Membership>newArrayList(new Membership(6L, 6L, true, true),
                                new Membership(6L, 3L, false, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(7L, "dummyGroup G", 3L,
                        Lists.<Membership>newArrayList(new Membership(3L, 3L, true, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(8L, "dummyGroup H", 4L,
                        Lists.<Membership>newArrayList(new Membership(4L, 4L, true, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(9L, "dummyGroup I", 4L,
                        Lists.<Membership>newArrayList(new Membership(4L, 4L, true, true)), Calendar.getInstance().getTimeInMillis()),
                new Group(10L, "dummyGroup J", 5L,
                        Lists.<Membership>newArrayList(new Membership(5L, 5L, true, true)), Calendar.getInstance().getTimeInMillis())
        );
    }

    public GroupManagerMock(UserManager userManager) {
        this.userManager = userManager;
    }

    public List<Group> getList() {
        return groups;
    }

    public Group get(final Long groupId) {
        return Utils.find(groups, new Function<Group, Boolean>() {
            @Override
            public Boolean apply(Group group) {
                return group.getGroupId() == groupId;
            }
        });
    }

    private User waitGetUser(Long userId) {
        class Waiter implements UserManager.UserCallback, Future<User> {
            private User value = null;
            private boolean done;

            @Override
            public void callback(User user) {
                synchronized (this) {
                    this.value = user;
                    done = true;
                    notifyAll();
                }
            }

            @Override
            public boolean cancel(boolean b) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return done;
            }

            @Override
            public User get() throws InterruptedException, ExecutionException {
                synchronized (this) {
                    while (!done) wait();
                    return value;
                }
            }

            @Override
            public User get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                throw new UnsupportedOperationException();
            }
        }
        Waiter waiter = new Waiter();
        userManager.get(userId, waiter);
        try {
            return waiter.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // null or "" ?
    public List<Group> search(final String groupName, final String adminName) {
        if (groupName.equals("") && adminName.equals(""))
            throw new IllegalArgumentException();

        return Utils.findAll(allGroups, new Function<Group, Boolean>() {
            @Override
            public Boolean apply(Group group) {
                User admin = waitGetUser(group.getAdministratorId());
                if (!groupName.equals("") && !adminName.equals("")) {
                    //return groupName.equals(group.getName()) && adminName.equals(admin.getName());
                    return group.getName().contains(groupName) && admin.getName().contains(adminName);
                } else if (!groupName.equals("")) {
                    // return groupName.equals(group.getName());
                    return group.getName().contains(groupName);
                } else {
                    // return adminName.equals(admin.getName());
                    return admin.getName().contains(adminName);
                }
            }
        });
    }

    public List<Group> searchWithGroupName(String groupName) {
        return search(groupName, null);
    }

    public List<Group> searchWithAdminName(String adminName) {
        return search(null, adminName);
    }

    public void create(Group group)
    {
        groups.add(group);
        allGroups.add(group);
    }

    public void update(final Group group) {
        int i = 0;
        for(Group g: allGroups){
            if(g.getGroupId().equals(group.getGroupId())){
                allGroups.set(i, group);
                break;
            }

            i++;
        }

        i = 0;
        for(Group g: groups){
            if(g.getGroupId().equals(group.getGroupId())){
                groups.set(i, group);
                break;
            }

            i++;
        }
        if(i == groups.size()){
            groups.add(group);
        }
    }

    public void delete(final Long groupId) {
        groups.remove(Utils.find(groups, new Function<Group, Boolean>() {
                    @Override
                    public Boolean apply(Group group) {
                        Long id = group.getGroupId();
                        return id != null && id.equals(groupId);
                    }
                }
        ));
    }

    @Override
    public void getList(GroupListCallback callback) {
        callback.callback(getList());
    }

    @Override
    public void get(Long groupId, GroupCallback callBack) {
        callBack.callback(get(groupId));
    }

    @Override
    public void search(String groupName, String adminName, GroupListCallback callback) {
        callback.callback(search(groupName, adminName));
    }

    @Override
    public void searchWithGroupName(String groupName, GroupListCallback callback) {
        callback.callback(searchWithGroupName(groupName));
    }

    @Override
    public void searchWithAdminName(String adminName, GroupListCallback callback) {
        callback.callback(searchWithAdminName(adminName));
    }

    @Override
    public void create(Group group, Callback callback) {
        Group g = new Group(nextId, group.getName(), group.getAdministratorId(), group.getMemberships(), group.getUpdatedAt());
        nextId++;
        create(g);
        callback.callback(true);
    }

    @Override
    public void update(Group group, Callback callback) {
        update(group);
        callback.callback(true);
    }

    @Override
    public void delete(Long groupId, Callback callback) {
        delete(groupId);
        callback.callback(true);
    }
}
