package jp.fccpc.taskmanager.Views.UserSearchDialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.base.Function;

import java.util.List;

import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Util.Utils;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/15.
 */
public class UserSearchResultAdapter extends ArrayAdapter<User> {
    static final int RESOURCE_ID = R.layout.list_item_user_search_result;
    private Context mContext;
    private List<User> mResultUsers;
    private List<User> mCurrentUsers;
    private LayoutInflater mInflater;
    private String mSearchWord;

    public UserSearchResultAdapter(Context context, List<User> currentUsers, List<User> resultUsers) {
        super(context, RESOURCE_ID, resultUsers);

        mContext = context;
        mCurrentUsers = currentUsers;
        mResultUsers = resultUsers;
        mSearchWord = "";
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

        TextView userName = (TextView) view.findViewById(R.id.user_search_name_item_text);
        final TextView isAdded = (TextView) view.findViewById(R.id.user_search_already_in_text);

        final User user = mResultUsers.get(position);

        // 検索に一致した部分の文字色を変更
        int i = user.getName().indexOf(mSearchWord);
        String pre = "<font color=#dd0000>";
        String suf = "</font>";
        if(i == -1) {
            userName.setText(user.getName());
        } else {
            userName.setText(Html.fromHtml(user.getName().substring(0, i) + pre + mSearchWord + suf
                    + user.getName().substring(i + mSearchWord.length(), user.getName().length())));
        }

        // should be improved implementation
        if( Utils.find(mCurrentUsers, new Function<User, Boolean>() {
            @Override
            public Boolean apply(User input) {
                if(user.getUserId() == input.getUserId()) return true;
                else return false;
            }
          }) != null){
            isAdded.setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.LTGRAY);
        }

        return view;
    }

    public void setSearchWord (String searchword){
        mSearchWord = searchword;
    }
}
