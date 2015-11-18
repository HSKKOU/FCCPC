package jp.fccpc.taskmanager.Views.GroupList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/14.
 */
public class GroupSearchResultAdapter extends ArrayAdapter<Group> {
    static final int RESOURCE_ID = R.layout.list_item_group_search_result;
    private Context mContext;
    private List<Group> mResultGroups;
    private List<Boolean> mAlreadyAdded;
    private LayoutInflater mInflater;

    private String sGroupName, sOwnerName;

    public GroupSearchResultAdapter(Context context, List<Group> resultItems, List<Boolean> alreadyadded) {
        super(context, RESOURCE_ID, resultItems);

        mContext = context;
        mResultGroups = resultItems;
        mAlreadyAdded = alreadyadded;
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

        final Group  group = mResultGroups.get(position);
        final TextView mOwnerName, mGroupNmae;
        View alreadyInText;

        mOwnerName = (TextView) view.findViewById(R.id.owner_name_group_search_reuslt_item);
        mGroupNmae = (TextView) view.findViewById(R.id.group_name_group_search_result_item);
        alreadyInText = (View) view.findViewById(R.id.group_search_already_in_text);

        // すでに入っているグループかどうか確認
        // すでに入っているグループならば背景色を変える
        if(mAlreadyAdded.get(position)) {
            view.setBackgroundColor(Color.LTGRAY);
            alreadyInText.setVisibility(View.VISIBLE);
        }

        App.get().getUserManager().get(group.getAdministratorId(), new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
            if (user != null) {
                // 一致した部分の色を変更する
                int i = user.getName().indexOf(sOwnerName);
                int j = group.getName().indexOf(sGroupName);

                String pre = "<font color=#dd0000>";
                String suf = "</font>";
                if(i == -1) {
                    mOwnerName.setText(user.getName());
                } else {
                    mOwnerName.setText(Html.fromHtml(user.getName().substring(0,i) + pre + sOwnerName + suf
                            + user.getName().substring(i+sOwnerName.length(), user.getName().length())));
                }
                if(j == -1) {
                    mGroupNmae.setText(group.getName());
                } else {
                    mGroupNmae.setText(Html.fromHtml(group.getName().substring(0, j) + pre + sGroupName + suf
                            + group.getName().substring(j + sGroupName.length(), group.getName().length())));
                }
            } else {
                // Todo: handle error
            }
            }
        });

        return view;
    }

    public void setSearchWords(String groupname, String ownername){
        sGroupName = groupname;
        sOwnerName = ownername;
    }
}
