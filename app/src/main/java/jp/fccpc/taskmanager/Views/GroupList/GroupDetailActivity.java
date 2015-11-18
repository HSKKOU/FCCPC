package jp.fccpc.taskmanager.Views.GroupList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;
import jp.fccpc.taskmanager.Values.User;
import jp.fccpc.taskmanager.Views.UserSearchDialog.UserSearchDialog;

/**
 * Created by tm on 2015/11/15.
 */
public class GroupDetailActivity extends Activity implements UserSearchDialog.userSearchDialogInterface {
    private TextView mGroupName;

    private ListView mGroupUserList;
    private GroupDetailUserListAdapter mAdapter;
    private Button mAddUserButton, mUpdateButton, mCancelButton;

    private Long myId;
    private Long GroupId;
    private Group mGroup;
    private List<Membership> mMemberships;
    private List<User> mUsers;
    private boolean isOwner;
    private boolean isChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        App.get().getUserManager().get(null, new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
                if (user != null) {
                    myId = user.getUserId();
                } else {
                    // Todo: handle error
                }
            }
        });

        GroupId = getIntent().getLongExtra("GroupId", 0L);
        App.get().getGroupManager().get(GroupId, new GroupManager.GroupCallback() {
            @Override
            public void callback(Group group) {
                if (group != null) {
                    mGroup = group;
                } else {
                    // Todo: handle error
                }
            }
        });

        isOwner = (myId.equals(mGroup.getAdministratorId()));


        mGroupName = (TextView) findViewById(R.id.group_detail_input_name);
        mGroupName.setText(mGroup.getName());

        mGroupUserList = (ListView) findViewById(R.id.group_detail_user_listview);

        // Membership から List<User> を構成
        mMemberships = mGroup.getMemberships();
        mUsers = new ArrayList<>();
        for(Membership m : mMemberships){
            App.get().getUserManager().get(m.getUserId(), new UserManager.UserCallback() {
                @Override
                public void callback(User user) {
                    if (user != null) {
                        mUsers.add(user);
                    } else {
                        // Todo: handle error
                    }
                }
            });
        }
        mAdapter = new GroupDetailUserListAdapter(GroupDetailActivity.this, mMemberships, mUsers, mGroup.getAdministratorId(), isOwner);
        mGroupUserList.setAdapter(mAdapter);

        mAddUserButton = (Button) findViewById(R.id.group_detail_add_user_item_button);
        if(isOwner) {
            final FragmentManager manager = getFragmentManager();
            mAddUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // call user "search dialog
                    DialogFragment dialog = new UserSearchDialog(mUsers);
                    dialog.show(manager, "usersearch");
                }
            });
        } else {
            mAddUserButton.setText("");
        }

        mUpdateButton = (Button) findViewById(R.id.update_group_button_groupdetail);
        if(isOwner) {
            mUpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateGroup();
                    finish();
                }
            });
            mUpdateButton.setVisibility(View.VISIBLE);
        } else {
            mUpdateButton.setVisibility(View.GONE);
        }

        mCancelButton = (Button) findViewById(R.id.cancel_button_groupdetail);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        isChanged = false;
    }

    public void acceptUser(int position){
        mMemberships.get(position).setGroupAgreed(true);
        isChanged = true;
        mAdapter.notifyDataSetChanged();
    }

    public void updateGroup(){
        // Todo: isChanged の判定精度を上げる
        if(isChanged) {
            Group g = new Group(mGroup.getGroupId(), mGroup.getName(), mGroup.getAdministratorId(), mMemberships, Calendar.getInstance().getTimeInMillis());
            App.get().getGroupManager().update(g, new GroupManager.Callback() {
                @Override
                public void callback(boolean success) {
                    ;
                }
            });
        }
    }

    public void removeUser(int position){
        mUsers.remove(position);
        mMemberships.remove(position);
        isChanged = true;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReturnFromUserSearch(User user) {
        // 追加済みならスキップ
        if(mUsers.contains(user)) return;

        mUsers.add(user);
        Membership g = new Membership(mGroup.getGroupId(), user.getUserId(), true, false);
        mMemberships.add(g);
        isChanged = true;
        mAdapter.notifyDataSetChanged();
    }
}
