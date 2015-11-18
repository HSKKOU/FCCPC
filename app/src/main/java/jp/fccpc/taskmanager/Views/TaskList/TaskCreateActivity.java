package jp.fccpc.taskmanager.Views.TaskList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.Task;
import jp.fccpc.taskmanager.Views.DateDialog.SelectDateFragment;

/**
 * Created by tm on 2015/11/04.
 */
// TODO: fix to manage deadline
public class TaskCreateActivity extends AppCompatActivity
        implements SelectDateFragment.OnSelectDateListener {

    EditText mTitle, mContent, mDeadLine;
    Button mCreateButton, mCancelButton;

    Long mGroupId;
    Long deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_create);

        mGroupId = getIntent().getLongExtra("GroupID", 0L);

        mTitle = (EditText) findViewById(R.id.task_create_input_title);

        mDeadLine = (EditText) findViewById(R.id.task_create_input_deadline);
        mDeadLine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    clickDateTextField();
                }
            }
        });
        mDeadLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDateTextField();
            }
        });
        mContent = (EditText) findViewById(R.id.task_create_input_content);


        mCreateButton = (Button) findViewById(R.id.create_task_button_taskcreate);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTitle.getText().toString().equals("") || mContent.getText().toString().equals("")){
                    Toast.makeText(TaskCreateActivity.this, "空のフィールドがあります", Toast.LENGTH_SHORT).show();;
                } else {
                    addTask();
                    finish();
                }
            }
        });

        mCancelButton = (Button) findViewById(R.id.cancel_button_taskcreate);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickDateTextField(){
        SelectDateFragment sdf = new SelectDateFragment();
        sdf.setSelectDateListener(this);
        sdf.setDateTextField(mDeadLine);
        // 日付をダイアログで選択した後に次のフィールドにフォーカスを移動するためのコールバック
        sdf.setCallback(new SelectDateFragment.setDateCallback() {
            @Override
            public void callback(Date d, String dateText) {
                mDeadLine.setText(dateText);
                deadline = d.getTime();
                mContent.requestFocus();
            }
        });
        sdf.show(getSupportFragmentManager(), "DatePicker");
    }
    // SelectDateDialog Listener
    @Override
    public void onSelectDate(Date date) {

    }

    private void addTask(){
        long now = Calendar.getInstance().getTimeInMillis();

        Task task = new Task(null, mGroupId, mTitle.getText().toString(), deadline, mContent.getText().toString(),
                "reminderTime", now, now, null);
        App.get().getTaskManager().create(task, new TaskManager.Callback() {
            @Override
            public void callback(boolean success) {
                ;
            }
        });
    }
}