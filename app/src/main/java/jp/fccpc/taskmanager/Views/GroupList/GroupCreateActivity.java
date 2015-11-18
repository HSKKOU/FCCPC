package jp.fccpc.taskmanager.Views.GroupList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by tm on 2015/11/04.
 */
public class GroupCreateActivity extends Activity implements UserSearchDialog.userSearchDialogInterface {
    private TextView mGroupNameText;
    private EditText mGroupName;
    private ListView mGroupUserList;
    private GroupCreateUserListAdapter mAdapter;
    private Button mAddUserButton, mCreateButton, mCancelButton;

    private Long OwnerId;
    private List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_create);

        mGroupNameText = (TextView) findViewById(R.id.group_name_text_group_create);
        mGroupName = (EditText) findViewById(R.id.group_create_input_name);
        mGroupName.requestFocus();

        mGroupUserList = (ListView) findViewById(R.id.group_create_user_listview);
        // 自分を登録
        mUsers = new ArrayList<>();
        App.get().getUserManager().get(null, new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
                if(user != null){
                    mUsers.add(user);
                    OwnerId = user.getUserId();
                } else {
                    // Todo: handle error;
                }
            }
        });
        mAdapter = new GroupCreateUserListAdapter(GroupCreateActivity.this, mUsers);
        mGroupUserList.setAdapter(mAdapter);

        mAddUserButton = (Button) findViewById(R.id.group_create_add_user_item_button);
        final FragmentManager manager = getFragmentManager();
        mAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call user search dialog
                DialogFragment dialog = new UserSearchDialog(mUsers);
                dialog.show(manager, "usersearch");
            }
        });

        mCreateButton = (Button) findViewById(R.id.create_group_button_groupcreate);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mGroupName.getText().toString().equals("")){
                    Toast.makeText(GroupCreateActivity.this, "空のフィールドが有ります", Toast.LENGTH_SHORT).show();
                } else {
                    createGroup();
                    finish();
                }
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_button_groupcreate);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private List<Membership> createMembers(){
        List<Membership> members = new ArrayList<>();

        // Todo: gropuID が Null でいいかどうか確認
        // オーナーのみ group/user からの承認がどちらも true
        Membership owner = new Membership(null, OwnerId, true, true);
        members.add(owner);

        for(User u: mUsers){
            if(u.getUserId() == OwnerId) continue;

            Membership m = new Membership(null, u.getUserId(), true, false);
            members.add(m);
        }

        return members;
    }

    private void createGroup(){
        long now = Calendar.getInstance().getTimeInMillis();
        List<Membership> members = createMembers();

        Group group = new Group(null, mGroupName.getText().toString(), OwnerId, members, now);

        App.get().getGroupManager().create(group, new GroupManager.Callback() {
            @Override
            public void callback(boolean success) {
                ;
            }
        });
    }

    // add new user
    @Override
    public void onReturnFromUserSearch(User user){
        // 追加済みならスキップ
        if(mUsers.contains(user)) return;

        mUsers.add(user);
        mAdapter.notifyDataSetChanged();
    }

    public void removeUser(int position){
        mUsers.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return false;
    }

    // 画面タッチでキーボードを隠す
    public void hideKeyboard(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mGroupNameText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mGroupNameText.requestFocus();
    }
}