package jp.fccpc.taskmanager.Views.UserSearchDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/15.
 */



public class UserSearchDialog extends DialogFragment{
    public interface userSearchDialogInterface {
        public void onReturnFromUserSearch(User user);
    }

    List<User> currentUsers;

    public UserSearchDialog(){
        if(currentUsers == null ) currentUsers = new ArrayList<>();
    }

    // Todo: fix this: use setArgument
    public UserSearchDialog(List<User> currentUsers){
        this.currentUsers = currentUsers;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_user_search, null);

        ListView userList = (ListView) view.findViewById(R.id.user_serach_dialog_list);
        final List<User> searchUsers = new ArrayList<>();
        final String searchWord = "";
        final UserSearchResultAdapter mAdapter = new UserSearchResultAdapter(getActivity(), currentUsers, searchUsers);
        userList.setAdapter(mAdapter);

        final EditText searchName = (EditText) view.findViewById(R.id.user_search_dialog_input_text);
        final TextView resultNum = (TextView) view.findViewById(R.id.user_search_dialog_result_num_text);

        Button  searchButton = (Button) view.findViewById(R.id.user_search_dialog_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("usersearchdialog", searchName.getText().toString());
                if(!searchName.getText().toString().equals("")){
                    App.get().getUserManager().searchUser(searchName.getText().toString(), new UserManager.UserListCallback() {
                        @Override
                        public void callback(List<User> userList) {
                            if(userList != null){
                                if(userList.isEmpty()){
                                    resultNum.setText("一致するユーザーはいませんでした");
                                } else {
                                    resultNum.setText( "" + userList.size() + " 人のユーザーが見つかりました");
                                }
                                searchUsers.clear();
                                searchUsers.addAll(userList);
                                mAdapter.setSearchWord(searchName.getText().toString());
                                mAdapter.notifyDataSetChanged();
                            } else {
                                // Todo: handle error
                            }
                        }
                    });
                }
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userSearchDialogInterface callingActivity = (userSearchDialogInterface) getActivity();
                callingActivity.onReturnFromUserSearch((User) ((ListView) adapterView).getItemAtPosition(i));
                dismiss();
            }
        });

        builder.setView(view);

        builder.setMessage("追加するユーザーを選択");

        return builder.create();
    }
}
