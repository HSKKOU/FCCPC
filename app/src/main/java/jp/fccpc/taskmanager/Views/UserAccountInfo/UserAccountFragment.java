package jp.fccpc.taskmanager.Views.UserAccountInfo;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.User;
import jp.fccpc.taskmanager.Managers.App;

/**
 * Created by tm on 2015/11/02.
 */
public class UserAccountFragment extends Fragment {
    Button mLogoutButton, mDeleteAccountButton;
    TextView mUserNameText, mUserEmailText;

    public UserAccountFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_account, container, false);

        // Todo: fix to be able to change username/email/password
        mUserEmailText = (TextView) rootView.findViewById(R.id.user_name);
        mUserNameText = (TextView) rootView.findViewById(R.id.user_email);

        // TODO: fix to get my account
        App.get().getUserManager().get(1L, new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
                if (user != null){
                    mUserNameText.setText(user.getName());
                    mUserEmailText.setText(user.getEmailAddress());
                }
            }
        });

        mLogoutButton = (Button) rootView.findViewById(R.id.logout_button_useraccount);
        mDeleteAccountButton = (Button) rootView.findViewById(R.id.delete_button_useraccount);

        final FragmentManager manager = this.getActivity().getFragmentManager();
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newDialog = new UserAccountLogoutDialog();
                newDialog.show(manager, "Logout");
            }
        });
        mDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newDialog = new UserAccountDeleteDialog();
                newDialog.show(manager, "Delete");
            }
        });

        return rootView;
    }
}
