package jp.fccpc.taskmanager.Views.GroupList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/15.
 */
public class GroupCreateUserListAdapter extends ArrayAdapter<User> {
    static final int RESOURCE_ID = R.layout.list_item_group_create_user;
    private Context mContext;
    private List<User> mUsers;
    private LayoutInflater mInflater;

    public GroupCreateUserListAdapter(Context context, List<User> users) {
        super(context, RESOURCE_ID, users);

        mContext = context;
        mUsers = users;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(RESOURCE_ID, null);
        }

        final Button removeButton = (Button) view.findViewById(R.id.user_remove_button);
        TextView userName = (TextView) view.findViewById(R.id.user_name_item_text);
        final TextView isOwner = (TextView) view.findViewById(R.id.group_user_is_owner_text);

        final User user = mUsers.get(position);

        userName.setText(user.getName());

        // 追加したユーザーを取り除く
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo: GroupDetailActivity などからも呼ばれるようにロジックを変更
                ((GroupCreateActivity) mContext).removeUser(position);
            }
        });

        // 自分の名前なら取り除けないようにボタンを隠す
        App.get().getUserManager().get(null, new UserManager.UserCallback() {
            @Override
            public void callback(User u) {
                if(u != null && u.getUserId() == user.getUserId()) {
                    removeButton.setVisibility(View.INVISIBLE);
                    isOwner.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(Color.rgb(247,171,166));
                }
            }
        });

        return view;
    }
}
