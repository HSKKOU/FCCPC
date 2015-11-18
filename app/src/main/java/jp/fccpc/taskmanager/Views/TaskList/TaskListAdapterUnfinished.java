package jp.fccpc.taskmanager.Views.TaskList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import jp.fccpc.taskmanager.R;

/**
 * Created by hskk1120551 on 2015/10/16.
 */
public class TaskListAdapterUnfinished extends ArrayAdapter<TaskListItem> {
    static final int RESOURCE_ID = R.layout.list_item_task;
    private Context mContext;
    private List<TaskListItem> mItems;
    private LayoutInflater mInflater;

    private TextView mTitle, mRemainderDate;

    public TaskListAdapterUnfinished(Context context, List<TaskListItem> items) {
        super(context, RESOURCE_ID, items);

        mContext = context;
        mItems = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<TaskListItem> getItems() { return mItems; }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(RESOURCE_ID, null);
        }

        final TaskListItem item = mItems.get(position);

        // テキストビュー
        mTitle = (TextView) view.findViewById(R.id.title);
        mTitle.setText(item.title);

        mRemainderDate = (TextView) view.findViewById(R.id.date);
        mRemainderDate.setText(item.getDateString());
        // 締め切りに近くなったら文字色を赤くする
        if(item.isNearDeadline()) {
            mRemainderDate.setTextColor(Color.RED);
        } else {
            mRemainderDate.setTextColor(Color.BLACK);
        }

        // チェックボックス
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.check_box);
        boolean beforeChecked = item.checked;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecked(isChecked);
            }
        });

        checkBox.setChecked(item.checked);

        return view;
    }

}
