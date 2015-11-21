package jp.fccpc.taskmanager.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Views.GroupList.GroupAddActivity;
import jp.fccpc.taskmanager.Views.GroupList.GroupCreateActivity;
import jp.fccpc.taskmanager.Views.GroupList.GroupListFragment;
import jp.fccpc.taskmanager.Views.TaskList.TaskListFragment;
import jp.fccpc.taskmanager.Views.UserAccountInfo.UserAccountFragment;

public class MainActivity extends AppCompatActivity
        implements GroupListFragment.Callbacks {


    Toolbar mToolbar;
    Menu mMenu;

    private GroupListFragment mGroupListFragment;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_user_info:
                mGroupListFragment.clearSelection();

                Bundle arguments = new Bundle();
                UserAccountFragment fragment = new UserAccountFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_app_bar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        mGroupListFragment = ((GroupListFragment) getSupportFragmentManager().findFragmentById(R.id.item_list));
        mGroupListFragment.setActivateOnItemClick(true);

        Button mCreateGroupButton = (Button) findViewById(R.id.create_group_button);
        mCreateGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GroupCreateActivity.class);
                startActivity(intent);
            }
        });

        Button mAddGroupButton = (Button) findViewById(R.id.add_group_button);
        mAddGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GroupAddActivity.class);
                startActivity(intent);
            }
        });

        openTaskList(0);
        mGroupListFragment.getListView().setItemChecked(0, true);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    /*
     * Callback method from {@link GroupListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int position){
        // change Toolbar name and show menubar
//        final int i = position;
//        App.get().getGroupManager().getList(new GroupManager.GroupListCallback() {
//            @Override
//            public void callback(List<Group> groupList) {
//                mToolbar.setTitle(getResources().getString(R.string.app_name) + "(" +  groupList.get(i).getName() + ")");
//            }
//        });

        openTaskList(position);
    }

    private void openTaskList(int position){
        // set task list fragment
        Bundle arguments = new Bundle();
        arguments.putInt(TaskListFragment.ARG_POSITION, position);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();
    }
}