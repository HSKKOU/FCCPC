package jp.fccpc.taskmanager.Views.TaskList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.Managers.mock.GroupManagerMock;
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

    private ListView mUnFinishedTaskList, mFinishedTaskList;
    private TaskListAdapterUnfinished adapterUnfinished;
    private TaskListAdapterFinished adapterFinished;
    private List<TaskListItem> unfinished_tasks;
    private List<TaskListItem> finished_tasks;

    private Button mCreateTaskButton;
    private Button mCompleteTaskButton;

    private int checkedCounter = 0;

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
            int position = getArguments().getInt(ARG_POSITION);
            mGroup = ((GroupManagerMock) App.get().getGroupManager()).getList().get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        mainLayout = (LinearLayout) rootView.findViewById(R.id.task_list_main);
        waitingUserAcceptLayout = (LinearLayout) rootView.findViewById(R.id.task_list_waiting_user_accept);
        waitingGroupAcceptLayout = (LinearLayout) rootView.findViewById(R.id.task_list_waiting_group_accept);

        if (mGroup != null) {
            // 自分の ID を取得
            final Long[] myId_ = new Long[1];
            App.get().getUserManager().get(null, new UserManager.UserCallback() {
                @Override
                public void callback(User user) {
                    myId_[0] = user.getUserId();
                }
            });
            myId = myId_[0];

            Membership myMembership = null;
            // 自分が招待されているかどうか確認
            int i = 0;
            for(Membership m : mGroup.getMemberships()){
                if(m.getUserId().equals(myId)){
                    myMembership = m;
                    break;
                }
                i++;
            }
            myMembershipPosition = i;

            if(myMembership == null || (!myMembership.isUserAgreed() && !myMembership.isGroupAgreed())){
                // Todo: handle this (should not happen)
                return rootView;
            }

            // 承認状況によって使用するレイアウトを変更
            if(myMembership.isGroupAgreed() && !myMembership.isUserAgreed()){
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
        return rootView;
    }

    public void setupWaitingUserAcceptLayout(final View rootView){
        TextView noteText = (TextView) rootView.findViewById(R.id.task_list_user_accept_note);
        final String[] ownerName = new String[1];
        App.get().getUserManager().get(mGroup.getAdministratorId(), new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
                if(user != null){
                    ownerName[0] = user.getName();
                } else {
                    // Todo: handle error
                }
            }
        } );
        String s = ownerName[0] + "さんからグループ「" + mGroup.getName() +"」に招待されています";
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
                        ;
                    }
                });

                // レイアウトを変更
                waitingUserAcceptLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
                isMainLayout = true;
                setupMainLayout(rootView);
            }
        });
    }

    public void setupMainLayout(View rootView){
        // for finished task lists
        mUnFinishedTaskList = (ListView) rootView.findViewById(R.id.unfinished_task_list);
        mUnFinishedTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("taskId", unfinished_tasks.get(i).getId());
                startActivity(intent);
            }
        });
        unfinished_tasks = new ArrayList<>();
        adapterUnfinished = new TaskListAdapterUnfinished(getActivity(), unfinished_tasks);
        mUnFinishedTaskList.setAdapter(adapterUnfinished);


        mFinishedTaskList = (ListView) rootView.findViewById(R.id.finished_task_list);
        mFinishedTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra("taskId", finished_tasks.get(i).getId());
                startActivity(intent);
            }
        });
        finished_tasks = new ArrayList<>();
        adapterFinished = new TaskListAdapterFinished(getActivity(), finished_tasks);
        mFinishedTaskList.setAdapter(adapterFinished);

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

        // 自分がオーナーの時のみタスク追加ボタンを出す (デフォルト: off)
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
        }
    }

    private void updateTaskList(){
        final TaskListFragment f = this;
        App.get().getTaskManager().getList(mGroup.getGroupId(), new TaskManager.TaskListCallback() {
            @Override
            public void callback(List<Task> taskList) {
                if (taskList != null) {
                    // Log.d("setTaskListAdapter", "called");

                    finished_tasks.clear();
                    unfinished_tasks.clear();
                    for (Task t : taskList) {
                        TaskListItem item = new TaskListItem(t.getTaskId(), t.getTitle(), t.getDeadline());
                        item.setTaskListFragment(f);
                        if (t.getDoneAt() != null) {
                            // Log.d("setTaskListAdapter", "finished");
                            finished_tasks.add(item);
                        } else {
                            // Log.d("setTaskListAdapter", "unfinished");
                            unfinished_tasks.add(item);
                        }
                    }

                    adapterUnfinished.notifyDataSetChanged();
                    adapterFinished.notifyDataSetChanged();
                }
            }
        });
    }

    public void onCompleteTaskButtonClicked() {
        List<TaskListItem> items = ((TaskListAdapterUnfinished) mUnFinishedTaskList.getAdapter()).getItems();
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
        List<TaskListItem> items = ((TaskListAdapterUnfinished) mUnFinishedTaskList.getAdapter()).getItems();
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