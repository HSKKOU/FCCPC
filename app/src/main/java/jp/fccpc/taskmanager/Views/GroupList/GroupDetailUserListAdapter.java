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

import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.Membership;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/17.
 */
public class GroupDetailUserListAdapter extends ArrayAdapter<Membership> {
    static final int RESOURCE_ID = R.layout.list_item_group_detail_user;
    private Context mContext;
    private List<Membership> mMemberships;
    private List<User> mUsers;
    private LayoutInflater mInflater;
    private Long ownerId;
    private boolean isOwner;

    public GroupDetailUserListAdapter(Context context, List<Membership> memberships, List<User> users, Long onwerid, boolean isowner) {
        super(context, RESOURCE_ID, memberships);

        mContext = context;
        mMemberships = memberships;
        mUsers = users;
        ownerId = onwerid;
        isOwner = isowner;
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
        final Button acceptButton = (Button) view.findViewById(R.id.user_accept_button);
        final TextView userName = (TextView) view.findViewById(R.id.user_name_item_text);
        final TextView isAlreadyAdded = (TextView) view.findViewById(R.id.user_already_added_text);
        final TextView waitingOwnerAccept = (TextView) view.findViewById(R.id.waiting_owner_accept_text);
        final TextView isOwnerText = (TextView) view.findViewById(R.id.group_user_is_owner_text);

        final Membership member = mMemberships.get(position);
        final User user = mUsers.get(position);

        userName.setText(user.getName());

        // グループから/メンバーからの承認状況により表示を変える
        if(member.isGroupAgreed() && member.isUserAgreed()){
            acceptButton.setVisibility(View.INVISIBLE);
            waitingOwnerAccept.setVisibility(View.INVISIBLE);
            isAlreadyAdded.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(Color.WHITE);
        } else if (member.isGroupAgreed() && !member.isUserAgreed()){
            acceptButton.setVisibility(View.INVISIBLE);
            waitingOwnerAccept.setVisibility(View.INVISIBLE);
            isAlreadyAdded.setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.rgb(230,230,230));
        } else if (!member.isGroupAgreed() && member.isUserAgreed()){
            acceptButton.setVisibility(View.VISIBLE);
            waitingOwnerAccept.setVisibility(View.VISIBLE);
            isAlreadyAdded.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(Color.rgb(230, 230, 230));
        } else {
            // Todo: handle error
        }

        // オーナーの色を変更する
        if(user.getUserId().equals(ownerId)){
            isOwnerText.setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.rgb(247, 171, 166));
        }

        // 自分がグループのオーナーの場合ユーザーの承認等を行えるようにする
        if(isOwner) {
            // 追加したユーザーを取り除く
            removeButton.setVisibility(View.VISIBLE);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GroupDetailActivity) mContext).removeUser(position);
                }
            });

            // ユーザーを承認する
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GroupDetailActivity) mContext).acceptUser(position);
                }
            });

            // 自分の名前なら取り除けないようにボタンを隠す
            if(user.getUserId().equals(ownerId)){
                removeButton.setVisibility(View.INVISIBLE);
            }
        } else {
            removeButton.setVisibility(View.INVISIBLE);
            acceptButton.setVisibility(View.INVISIBLE);
        }

        return view;
    }

}
