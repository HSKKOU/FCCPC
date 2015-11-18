package jp.fccpc.taskmanager.Views.UserAccountInfo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/19.
 */
public class UserAccountChangePasswordDialog extends DialogFragment {
    public UserAccountChangePasswordDialog(){}
    private UserAccountDialogInterface mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_password, null);

        final TextView currentPass, newPass, newPass2;

        currentPass = (TextView) view.findViewById(R.id.change_password_current_password);
        newPass = (TextView) view.findViewById(R.id.change_password_new_password);
        newPass2 = (TextView) view.findViewById(R.id.change_password_new_password_second);


        builder.setView(view);
        builder.setTitle("パスワードの変更")
                .setPositiveButton("変更", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Todo: 失敗した時にダイアログが閉じないようにレイアウト等を修正
                        if (newPass.getText().toString().equals(newPass2.getText().toString())) {
                            App.get().getUserManager().get(null, new UserManager.UserCallback() {
                                @Override
                                public void callback(User user) {
                                    if (user != null) {
                                        App.get().getUserManager().updatePassword(currentPass.getText().toString(),
                                                newPass.getText().toString(), new UserManager.Callback() {
                                                    @Override
                                                    public void callback(boolean success) {
                                                        if (success) {
                                                            Toast.makeText(getActivity(), "パスワードを変更しました", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getActivity(), "パスワードの変更に失敗しました", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        // Todo: handle error
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

    public void setListener(UserAccountDialogInterface listener){
        mListener = listener;
    }
}
