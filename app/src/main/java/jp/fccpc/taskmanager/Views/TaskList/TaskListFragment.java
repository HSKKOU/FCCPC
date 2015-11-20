package jp.fccpc.taskmanager.Views.TaskList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Membership;
import jp.fccpc.taskmanager.Values.Task;
import jp.fccpc.taskmanager.Values.User;

public class TaskListFragment extends Fragment {
    // selected item position
    public static final String ARG_POSITION = "position";

    // current group
    private Group mGroup;

    private ExpandableListView mTaskList;
    private TaskListAdapter mAdapter;

    private final static int UNFINISHED_TASKS = 0;
    private final static int FINISHED_TASKS = 1;
    private List<TaskListItem> unfinished_tasks;
    private List<TaskListItem> finished_tasks;

    private Button mCreateTaskButton;
    private Button mCompleteTaskButton;

    private int checkedCounter = 0;

    private int myPositionAtGroupList;
    private boolean isMainLayout;
    private int myMembershipPosition;
    private Long myId;

    private LinearLayout mainLayout, waitingUserAcceptLayout, waitingGroupAcceptLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_POSITION)){
            myPositionAtGroupList = getArguments().getInt(ARG_POSITION);
            //mGroup = ((GroupManagerMock) App.get().getGroupManager()).getList().get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        mainLayout = (LinearLayout) rootView.findViewById(R.id.task_list_main);
        waitingUserAcceptLayout = (LinearLayout) rootView.findViewById(R.id.task_list_waiting_user_accept);
        waitingGroupAcceptLayout = (LinearLayout) rootView.findViewById(R.id.task_list_waiting_group_accept);

        // 1: get Group
        // 2: get my User
        // 3: setup layout
        App.get().getGroupManager().getList(new GroupManager.GroupListCallback() {
            @Override
            public void callback(List<Group> groupList) {
                if (groupList != null) {
                    mGroup = groupList.get(myPositionAtGroupList);

                    App.get().getUserManager().get(null, new UserManager.UserCallback() {
                        @Override
                        public void callback(User user) {
                            if (user != null) {
                                myId = user.getUserId();

                                Membership myMembership = null;
                                // 自分が招待されているかどうか確認
                                int i = 0;
                                for (Membership m : mGroup.getMemberships()) {
                                    if (m.getUserId().equals(myId)) {
                                        myMembership = m;
                                        break;
                                    }
                                    i++;
                                }
                                myMembershipPosition = i;

                                chooseLayout(myMembership, rootView);

                            } else {
                                // Todo: handle error
                            }
                        }
                    });
                } else {
                    // Todo: handle error
                }
            }
        });

        return rootView;
    }

    // ユーザー/グループが参加を許可しているかどうかによって使用するレイアウトを選択
    public void chooseLayout(Membership myMembership, View rootView){
        if(myMembership == null || (!myMembership.isUserAgreed() && !myMembership.isGroupAgreed())){
            // Todo: handle this (should not happen)

        } else if (myMembership.isGroupAgreed() && !myMembership.isUserAgreed()){
            // ユーザー承認ボタンを表示
            mainLayout.setVisibility(View.GONE);
            waitingUserAcceptLayout.setVisibility(View.VISIBLE);
            waitingGroupAcceptLayout.setVisibility(View.GONE);
            isMainLayout = false;

            setupWaitingUserAcceptLayout(rootView);
        } else if (!myMembership.isGroupAgreed() && myMembership.isUserAgreed()){
            // グループ承認待ち画面を表示
            mainLayout.setVisibility(View.GONE);
            waitingUserAcceptLayout.setVisibility(View.GONE);
            waitingGroupAcceptLayout.setVisibility(View.VISIBLE);
            isMainLayout = false;
        } else {
            // 実際のタスクリストを表示
            mainLayout.setVisibility(View.VISIBLE);
            waitingUserAcceptLayout.setVisibility(View.GONE);
            waitingGroupAcceptLayout.setVisibility(View.GONE);
            isMainLayout = true;

            setupMainLayout(rootView);
        }
    }

    public void setupWaitingUserAcceptLayout(final View rootView){
        App.get().getUserManager().get(mGroup.getAdministratorId(), new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
                if (user != null) {
                    String ownerName = user.getName();
                    String s = ownerName + "さんからグループ「" + mGroup.getName() + "」に招待されています";

                    TextView noteText = (TextView) rootView.findViewById(R.id.task_list_user_accept_note);
                    noteText.setText(s);

                    Button acceptButton = (Button) rootView.findViewById(R.id.task_list_user_accept_button);
                    acceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Membership 情報を変更
                            List<Membership> m = mGroup.getMemberships();
                            m.get(myMembershipPosition).setUserAgreed(true);
                            Group g = new Group(mGroup.getGroupId(), mGroup.getName(), mGroup.getAdministratorId(), m, Calendar.getInstance().getTimeInMillis());

                            App.get().getGroupManager().update(g, new GroupManager.Callback() {
                                @Override
                                public void callback(boolean success) {
                                    if(success) {
                                        // レイアウトを変更
                                        waitingUserAcceptLayout.setVisibility(View.GONE);
                                        mainLayout.setVisibility(View.VISIBLE);
                                        isMainLayout = true;
                                        setupMainLayout(rootView);
                                    } else {
                                        // Todo : handle error
                                        Toast.makeText(getActivity(), "通信に失敗しました", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                } else {
                    // Todo: handle error
                }
            }
        });
    }

    public void setupMainLayout(View rootView){
        unfinished_tasks = new ArrayList<>();
        finished_tasks = new ArrayList<>();

        mTaskList = (ExpandableListView) rootView.findViewById(R.id.task_list_expandable_list);
        mTaskList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);

                Long taskId;
                if(i == UNFINISHED_TASKS) { taskId = unfinished_tasks.get(i1).getId();}
                else { taskId = finished_tasks.get(i1).getId();}

                intent.putExtra("taskId", taskId);
                startActivity(intent);
                return true;
            }
        });
        mAdapter = new TaskListAdapter(getActivity(), unfinished_tasks, finished_tasks);
        mTaskList.setAdapter(mAdapter);
        mTaskList.expandGroup(UNFINISHED_TASKS);


        updateTaskList();

        mCreateTaskButton = (Button) rootView.findViewById(R.id.create_task_button_tasklist);
        mCreateTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TaskCreateActivity.class);
                intent.putExtra("GroupID", mGroup.getGroupId());
                startActivity(intent);
            }
        });

        // 自分がオーナーの時のみタスク追加ボタンを出す (デフォルト: gone)
        if (myId.equals(mGroup.getAdministratorId())) {
            mCreateTaskButton.setVisibility(View.VISIBLE);
        }

        mCompleteTaskButton = (Button) rootView.findViewById(R.id.complete_task_button_tasklist);
        mCompleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCompleteTaskButtonClicked();
            }
        });
        mCompleteTaskButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isMainLayout) {
            updateTaskList();
            mCompleteTaskButton.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isDoingUpdateList = false;
    private void updateTaskList(){
        if(isDoingUpdateList) return;

        isDoingUpdateList = true;
        final TaskListFragment f = this;
        App.get().getTaskManager().getList(mGroup.getGroupId(), new TaskManager.TaskListCallback() {
            @Override
            public void callback(List<Task> taskList) {
                if (taskList != null) {
                    finished_tasks.clear();
                    unfinished_tasks.clear();
                    for (Task t : taskList) {
                        TaskListItem item = new TaskListItem(t.getTaskId(), t.getTitle(), t.getDeadline());
                        item.setTaskListFragment(f);
                        if (t.getDoneAt() != null) {
                            finished_tasks.add(item);
                        } else {
                            unfinished_tasks.add(item);
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                } else{
                    // Todo: handle error
                    Toast.makeText(getActivity(), "タスクリストの更新に失敗しました", Toast.LENGTH_SHORT).show();
                }

                isDoingUpdateList = false;
            }
        });
    }

    public void onCompleteTaskButtonClicked() {
        List<TaskListItem> items = ((TaskListAdapter) mTaskList.getExpandableListAdapter()).getChild(UNFINISHED_TASKS);
        List<Long> checkedIds = new ArrayList<Long>();
        for (TaskListItem item : items) {
            if (item.checked) {
                checkedIds.add(item.id);
            }
        }
        App.get().getTaskManager().finish(checkedIds, new TaskManager.Callback() {
            @Override
            public void callback(boolean success) {
                endCheckMode();
                updateTaskList();
            }
        });
    }

    public void onChecked(boolean checked) {
        checkedCounter = 0;
        // TODO: 呼ばれるたびにインクリメント/デクリメントを用いて高速化
        List<TaskListItem> items = ((TaskListAdapter) mTaskList.getExpandableListAdapter()).getChild(UNFINISHED_TASKS);
        for (TaskListItem item : items) {
            if (item.checked) checkedCounter++;
        }
        Log.d("onChecked", "" + checkedCounter);
        if (checked && checkedCounter == 0) {
            beginCheckMode();
        }
        if (!checked && checkedCounter == 1) {
            endCheckMode();
        }
    }

    private void beginCheckMode() {
        mCompleteTaskButton.setVisibility(View.VISIBLE);
    }

    private void endCheckMode() {
        this.checkedCounter = 0;
        mCompleteTaskButton.setVisibility(View.INVISIBLE);
    }

}