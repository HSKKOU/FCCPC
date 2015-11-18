package jp.fccpc.taskmanager.Views.TaskList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.util.List;

import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.BoardItem;

/**
 * Created by tm on 2015/11/13.
 */
public class BoardListItemAdapter extends ArrayAdapter<BoardItem> {

    static final int RESOURCE_ID = R.layout.list_item_board;
    private Context mContext;
    private List<BoardItem> mItems;
    private LayoutInflater mInflater;

    BoardListItemAdapter(Context context, List<BoardItem> boardItems){
        super(context, RESOURCE_ID, boardItems);

        mContext = context;
        mItems = boardItems;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(RESOURCE_ID, null);
        }

        BoardItem item = mItems.get(position);
        TextView mNumber, mName, mDate, mContent;

        mNumber = (TextView) view.findViewById(R.id.number_board_item);
        mName = (TextView) view.findViewById(R.id.username_board_item);
        mDate = (TextView) view.findViewById(R.id.date_board_item);
        mContent = (TextView) view.findViewById(R.id.content_board_item);

        mNumber.setText(Long.toString(item.getNumber()));
        mName.setText(item.getUserName());
        mDate.setText(new Date(item.getCreatedAt()).toString()); // Todo : 日付表示を整える
        mContent.setText(item.getContent());

        return view;
    }
}
