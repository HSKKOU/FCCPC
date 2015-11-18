package jp.fccpc.taskmanager.Views.GroupList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.User;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;

/**
 * Created by tm on 2015/11/04.
 */
public class GroupAddActivity extends Activity {
    private EditText mGroupName, mOwnerName;
    private TextView mSearchResultNumText;
    private ListView mSearchReusltView;
    private GroupSearchResultAdapter mAdapter;
    private Button mSearchButton, mAddButton, mCancelbutton;

    private List<Group> resultGroups, currentGroups;
    private Group mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_add);

        mGroupName = (EditText) findViewById(R.id.group_add_input_name);
        mOwnerName = (EditText) findViewById(R.id.group_add_input_owner_name);

        App.get().getGroupManager().getList(new GroupManager.GroupListCallback() {
            @Override
            public void callback(List<Group> groupList) {
                currentGroups = groupList;
            }
        });
        mSearchReusltView = (ListView) findViewById(R.id.group_search_result_list_view);
        resultGroups = new ArrayList<>();
        mAdapter = new GroupSearchResultAdapter(GroupAddActivity.this, resultGroups, currentGroups);
        mSearchReusltView.setAdapter(mAdapter);

        mSearchResultNumText = (TextView) findViewById(R.id.group_search_result_num_text);

        mSearchButton = (Button) findViewById(R.id.search_group_button_groupadd);
        mAddButton = (Button) findViewById(R.id.add_group_button_groupadd);
        mCancelbutton = (Button) findViewById(R.id.cancel_button_group_add);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String groupNmae = mGroupName.getText().toString();
                final String ownerName = mOwnerName.getText().toString();
                if(groupNmae.equals("") && ownerName.equals("")){
                    return;
                }

                hideKeyboard();

                // search group
                App.get().getGroupManager().search(groupNmae, ownerName, new GroupManager.GroupListCallback() {
                    @Override
                    public void callback(List<Group> groupList) {
                        // if result was found, load them to list and show Add button
                        if (groupList != null) {
                            if(groupList.isEmpty()) {
                                mSearchResultNumText.setText("条件に一致するグループがありませんでした");

                                resultGroups.clear();
                                mAdapter.notifyDataSetChanged();

                                mAddButton.setVisibility(View.INVISIBLE);
                            } else {
                                mSearchResultNumText.setText("" + (groupList.size()) + " 件見つかりました");

                                resultGroups.clear();
                                resultGroups.addAll(groupList);

                                mAdapter.setSearchWords(groupNmae, ownerName);
                                mAdapter.notifyDataSetChanged();

                                mAddButton.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // Todo: handle error
                        }
                    }
                });
            }
        });

        mGroup = null;
        mSearchReusltView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                mGroup = (Group) ((ListView) adapterView).getItemAtPosition(i);
                Log.d("onitemclick", mGroup.getName());
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mGroup != null) {
                    // Todo: すでに入っているグループは追加しない
                    App.get().getUserManager().get(null, new UserManager.UserCallback() {
                        @Override
                        public void callback(User user) {
                            if (user != null) {
                                Log.d("groupaddactivity", "aaaaa");
                                // グループを更新
                                // 自分を新しい membership として登録して update
                                Membership me = new Membership(mGroup.getGroupId(), user.getUserId(), false, true);
                                List<Membership> memberships = mGroup.getMemberships();
                                memberships.add(me);
                                long now = Calendar.getInstance().getTimeInMillis();
                                Group g = new Group(mGroup.getGroupId(), mGroup.getName(), mGroup.getAdministratorId(), memberships, now);

                                App.get().getGroupManager().update(g, new GroupManager.Callback() {
                                    @Override
                                    public void callback(boolean success) {
                                        ;
                                    }
                                });

                            } else {
                                // Todo: handle error
                            }

                            finish();
                        }
                    });
                }
            }
        });
        mAddButton.setVisibility(View.GONE);

        mCancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return false;
    }

    // 画面タッチでキーボードを隠す
    public void hideKeyboard(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchResultNumText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mSearchResultNumText.requestFocus();
    }
}