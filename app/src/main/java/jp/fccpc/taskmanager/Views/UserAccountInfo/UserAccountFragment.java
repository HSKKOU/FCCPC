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

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/02.
 */
public class UserAccountFragment extends Fragment implements UserAccountDialogInterface{
    Button mLogoutButton, mDeleteAccountButton;
    TextView mUserNameText, mUserEmailText;
    TextView mUserNameTextTitle, mUserEmailTextTitle, mUserPasswordTitle;

    public UserAccountFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_account, container, false);

        mUserNameText = (TextView) rootView.findViewById(R.id.user_name);
        mUserNameTextTitle = (TextView) rootView.findViewById(R.id.change_name_title);

        mUserEmailText = (TextView) rootView.findViewById(R.id.user_email);
        mUserEmailTextTitle = (TextView) rootView.findViewById(R.id.change_email_title);

        mUserPasswordTitle = (TextView) rootView.findViewById(R.id.change_password_title);

        final FragmentManager manager = this.getActivity().getFragmentManager();
        final Fragment f = this;

        mUserNameTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccountChangeNameDialog newDialog = new UserAccountChangeNameDialog();
                newDialog.setListener((UserAccountDialogInterface) f);
                newDialog.show(manager, "ChangeName");
            }
        });

        mUserEmailTextTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccountChangeEmailDialog newDialog = new UserAccountChangeEmailDialog();
                newDialog.setListener((UserAccountDialogInterface) f);
                newDialog.show(manager, "ChangeEmail");
            }
        });

        mUserPasswordTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAccountChangePasswordDialog newDialog = new UserAccountChangePasswordDialog();
                newDialog.setListener((UserAccountDialogInterface) f);
                newDialog.show(manager, "ChangePassowrd");
            }
        });

        App.get().getUserManager().get(null, new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
                if (user != null) {
                    mUserNameText.setText(user.getName());
                    mUserEmailText.setText(user.getEmailAddress());
                } else {
                    // Todo: handle error
                }
            }
        });

        mLogoutButton = (Button) rootView.findViewById(R.id.logout_button_useraccount);
        mDeleteAccountButton = (Button) rootView.findViewById(R.id.delete_button_useraccount);

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

    @Override
    public void reloadUserInfo(){
        App.get().getUserManager().get(null, new UserManager.UserCallback() {
            @Override
            public void callback(User user) {
                if(user != null){
                    mUserNameText.setText(user.getName());
                    mUserEmailText.setText(user.getEmailAddress());
                }
            }
        });
    }
}
