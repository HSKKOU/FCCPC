package jp.fccpc.taskmanager.Views.TaskList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/19.
 */
public class TaskListAdapter extends BaseExpandableListAdapter {

    List<String> groups;
    List<List<TaskListItem>> children;
    private Context mContext;
    private LayoutInflater mInflater;

    private final static int UNFINISHED_TASKS = 0;
    private final static int FINISHED_TASKS = 1;

    public TaskListAdapter(Context context, List<TaskListItem> unfinishedTasks, List<TaskListItem> finishedTasks){
        mContext = context;

        groups = new ArrayList<>();
        groups.add("未完了のタスク");
        groups.add("完了したタスク");

        children = new ArrayList<>();
        children.add(unfinishedTasks);
        children.add(finishedTasks);

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<TaskListItem> getChild(int i){
        return children.get(i);
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int i) {
        return children.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return children.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i*2+i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(mContext);
        textView.setLayoutParams(param);

        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(64, 0, 0, 0);

        int childNum = children.get(i).size();
        String s = groups.get(i) + " (" + childNum + ")";

        textView.setText(s);


        return textView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        View childView = mInflater.inflate(R.layout.list_item_task, null);

        final TaskListItem item = children.get(i).get(i1);

        // テキストビュー
        TextView mTitle = (TextView) childView.findViewById(R.id.title);
        mTitle.setText(item.title);

        TextView mRemainderDate = (TextView) childView.findViewById(R.id.date);
        mRemainderDate.setText(item.getDateString());

        final TextView mFinishedUserNum = (TextView) childView.findViewById(R.id.finished_user_num);
        // get finished user list
        App.get().getTaskManager().getFinishedUserList(item.getId(), new TaskManager.UserListCallback() {
            @Override
            public void callback(List<User> userList) {
                if (userList != null) {
                    String s = "" + userList.size() + "人終了";
                    mFinishedUserNum.setText(s);
                } else {
                    // Todo: handle error
                }
            }
        });

        // チェックボックス
        CheckBox checkBox = (CheckBox) childView.findViewById(R.id.check_box);

        if(i == UNFINISHED_TASKS) {
            boolean beforeChecked = item.checked;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setChecked(isChecked);
                }
            });
            checkBox.setChecked(item.checked);
            if(item.isNearDeadline()){
                mRemainderDate.setTextColor(Color.RED);
            }
        } else {
            mRemainderDate.setTextColor(Color.GRAY);
            mTitle.setTextColor(Color.GRAY);
            // Todo: (とりあえず表示しないでおく)
            // Todo: 間違えて完了したタスクを元に戻せるようにする？
            checkBox.setVisibility(View.INVISIBLE);
        }

        return childView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
