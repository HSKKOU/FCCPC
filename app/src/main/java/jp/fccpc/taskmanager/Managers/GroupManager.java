package jp.fccpc.taskmanager.Managers;

import java.util.List;

import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;

/**
 * Created by nakac on 15/10/22.
 */
public interface GroupManager {
    interface Callback { void callback(boolean success); }
    interface GroupCallback { void callback(Group group); }
    interface GroupListCallback { void callback(List<Group> groupList); }
    interface MembershipCallback { void callback(Membership membership); }

    void getList(GroupListCallback callback);

    void get(Long groupId, GroupCallback callback);

    void search(String groupName, String adminName, GroupListCallback callback);

    void searchWithGroupName(String groupName, GroupListCallback callback);

    void searchWithAdminName(String adminName, GroupListCallback callback);

    void create(Group group, Callback callback);

    void update(Group group, Callback callback);

    void delete(Long groupId, Callback callback);

    void createMembership(Membership membership, Callback callback);

    void createMemberships(List<Membership> memberships, Callback callback);

    void getMembership(Long groupId, Long userId, MembershipCallback callback);

    void updateMembership(Membership membership, Callback callback);

    void deleteMembership(Membership membership, Callback callback);
}
