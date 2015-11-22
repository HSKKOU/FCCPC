package jp.fccpc.taskmanager.Views.GroupList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.Task;
import jp.fccpc.taskmanager.Views.TaskList.TaskListItem;

/**
 * Created by tm on 2015/11/22.
 */
public class GroupListAdapter extends ArrayAdapter<Group> {
    static final int RESOURCE_ID = R.layout.list_item_group;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Group> mGroups;


    public GroupListAdapter (Context context, List<Group> groups) {
        super(context, RESOURCE_ID, groups);

        mContext = context;
        mGroups = groups;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(RESOURCE_ID, null);
        }

        Group g = mGroups.get(position);

        TextView groupNmae = (TextView) view.findViewById(R.id.group_list_item);
        final TextView unfinishedTaskNum = (TextView) view.findViewById(R.id.group_list_unfinished_task_num);

        groupNmae.setText(g.getName());

        App.get().getTaskManager().getList(g.getGroupId(), new TaskManager.TaskListCallback() {
            @Override
            public void callback(List<Task> taskList) {
                if (taskList != null) {
                    int i = 0;
                    for (Task t : taskList) {
                        TaskListItem item = new TaskListItem(t.getTaskId(), t.getTitle(), t.getDeadline());
                        if (t.getDoneAt() == null) {
                            i++;
                        }
                    }

                    if (i != 0) {
                        unfinishedTaskNum.setText(Integer.toString(i));
                    }

                } else {
                    // Todo: handle error
                }
            }
        });

        return view;
    }
}
