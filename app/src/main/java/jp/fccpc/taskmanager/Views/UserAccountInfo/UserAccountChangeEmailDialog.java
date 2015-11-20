package jp.fccpc.taskmanager.Views.UserAccountInfo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/18.
 */
public class UserAccountChangeEmailDialog extends DialogFragment {
    public UserAccountChangeEmailDialog(){}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText emailText = new EditText(getActivity());
        builder.setTitle("新しいメールアドレスを入力")
                .setView(emailText)
                .setPositiveButton("変更", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Todo: 有効なメールアドレスかどうか確認
                        if (!emailText.getText().toString().equals("")) {
                            App.get().getUserManager().get(null, new UserManager.UserCallback() {
                                @Override
                                public void callback(User user) {
                                    if (user != null) {
                                        User u = new User(user.getUserId(), user.getName(), emailText.getText().toString());
                                        App.get().getUserManager().update(u, new UserManager.Callback() {
                                            @Override
                                            public void callback(boolean success) {
                                                if(success) {
                                                    Toast.makeText(getActivity(), "メールアドレスを更新しました", Toast.LENGTH_SHORT).show();
                                                    mListener.reloadUserInfo();
                                                } else  {
                                                    Toast.makeText(getActivity(), "メールアドレスの更新に失敗しました", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), "メールアドレスの更新に失敗しました", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    private UserAccountDialogInterface mListener;
    public void setListener(UserAccountDialogInterface listener){
        mListener = listener;
    }
}
