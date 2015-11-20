package jp.fccpc.taskmanager.Views.TaskList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.Managers.UserManager;
import jp.fccpc.taskmanager.R;
import jp.fccpc.taskmanager.Values.BoardItem;
import jp.fccpc.taskmanager.Values.Task;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/11/11.
 */
public class TaskDetailActivity extends AppCompatActivity {
    private TextView mTitle, mDeadline, mReminderTime, mContent, mFinishedUsers;
    private EditText mAddBoardItemText;

    private ListView mBoardItemList;
    private View mFooter, mHeader;
    private BoardListItemAdapter mBoardListItemAdapter;

    private Button mAddBoardItemButton, mOkButton;

    private Long taskId;
    private List<BoardItem> boardItems;

    private final int BOARD_ITEM_NUM_SKIP = 5; // 1 度にロードする掲示板のコメント数
    private int loaded_num = 0;
    private boolean isLoadingBoardItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_detail);

        taskId = getIntent().getLongExtra("taskId", 0L);

        isLoadingBoardItems = false;

        mTitle = (TextView) findViewById(R.id.task_detail_input_title);
        mDeadline = (TextView) findViewById(R.id.task_detail_input_deadline);
        mReminderTime = (TextView) findViewById(R.id.task_detail_input_remindertime);
        mContent = (TextView) findViewById(R.id.task_detail_input_content);
        mFinishedUsers = (TextView) findViewById(R.id.task_detail_input_finished_user_list);

        mAddBoardItemText = (EditText) findViewById(R.id.task_detail_add_board_item_text);

        // 掲示板のアイテムの adapter
        mBoardItemList = (ListView) findViewById(R.id.task_detail_board_listview);
        boardItems = new ArrayList<>();
        mBoardListItemAdapter = new BoardListItemAdapter(TaskDetailActivity.this, boardItems);
        mBoardItemList.setAdapter(mBoardListItemAdapter);
        mBoardItemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean to_load_older = false;
            boolean to_load_newer = false;

            @Override
            // スクロールが一番上または一番下で止まったらリロード
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (to_load_older) {
                        loadOlderBoardItems();
                        to_load_older = false;
                    } else if (to_load_newer) {
                        loadLatestBoardItems();
                        to_load_newer = false;
                    }
                }
            }

            @Override
            // スクロールが一番上または一番下に到着したかを判定
            // i...firstVisibleItem, i1...lastVisibleItem, i2...totalCount
            // http://u64178.blogspot.jp/2014/06/android-listview.html
            public void onScroll(AbsListView v, int i, int i1, int i2) {
                Log.d("onScroll", "" + i + "," + i1 + "," + i2);
                if (v.getChildCount() == 0){
                    to_load_newer = false;
                    to_load_older = false;
                    return;
                }

                if (v.getFirstVisiblePosition() == 0 && v.getChildAt(0).getTop() == v.getPaddingTop()) {
                    to_load_newer = true;
                } else {
                    to_load_newer = false;
                }

                if (v.getLastVisiblePosition() == i2 - 1 && v.getChildAt(v.getChildCount() - 1).getBottom() <= v.getBottom() - v.getPaddingBottom()) {
                    to_load_older = true;
                } else {
                    to_load_older = false;
                }

                // これだと十分ではない:
                // to_load_older = (i+i1 ==i2);
                // to_load_newer = (i == 0);
            }
        });
        mFooter = getLayoutInflater().inflate(R.layout.list_view_footer, null);
        mHeader = getLayoutInflater().inflate(R.layout.list_view_footer, null);
        loadLatestBoardItems();

        mOkButton = (Button) findViewById(R.id.ok_button_taskdetail);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAddBoardItemButton = (Button) findViewById(R.id.task_detail_add_board_item_button);
        mAddBoardItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mAddBoardItemText.getText().toString().equals("")){
                    mAddBoardItemButton.setEnabled(false);
                    // get user info
                    App.get().getUserManager().get(null, new UserManager.UserCallback() {
                        @Override
                        public void callback(User user) {
                            if (user != null) {
                                Long userId = user.getUserId();
                                String userName = user.getName();

                                // add boarditem
                                BoardItem item = new BoardItem(null, userId, userName, new Date().getTime(), mAddBoardItemText.getText().toString());
                                App.get().getTaskManager().createBoardItem(taskId, item, new TaskManager.Callback() {
                                    @Override
                                    public void callback(boolean success) {
                                        if (success) {
                                        } else {
                                            // Todo: handle error
                                        }
                                        mAddBoardItemButton.setEnabled(true);
                                    }
                                });

                            } else {
                                mAddBoardItemButton.setEnabled(true);
                                // Todo: handle error
                            }
                        }
                    });

                }

                mAddBoardItemText.getEditableText().clear();
                // キーボードを隠す
                hideKeyboard();

                // reload list
                loadLatestBoardItems();
            }
        });

        // get task
        App.get().getTaskManager().get(taskId, new TaskManager.TaskCallback() {
            @Override
            public void callback(Task task) {
                if (task != null) {
                    // set task info
                    if(task != null) {
                        mTitle.setText(task.getTitle());

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);
                        String s = sdf.format(task.getDeadline());
                        String r = "(" + toRemainderString(task.getDeadline()) + ")";
                        mDeadline.setText(s);
                        mReminderTime.setText(r);
                        if(isNearDeadline(task.getDeadline())){
                            mReminderTime.setTextColor(Color.RED);
                        } else {
                            mReminderTime.setTextColor(Color.BLACK);
                        }

                        mContent.setText(task.getDetail());
                    }
                } else {
                    // Todo: handle error
                }
            }
        });

        // get finished user list
        App.get().getTaskManager().getFinishedUserList(taskId, new TaskManager.UserListCallback() {
            @Override
            public void callback(List<User> userList) {
                if(userList != null){
                    String users = "";
                    for(User u : userList){
                        if(!users.equals("")) {users += ", ";}
                        users += u.getName();
                    }

                    users += " (" + userList.size() + " 人)";
                    mFinishedUsers.setText(users);
                } else {
                    // Todo: handle error
                }
            }
        });

    }

    // 入力時に画面をタッチしたらキーボードを隠す
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        return false;
    }

    private void hideKeyboard(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mTitle.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mTitle.requestFocus();
    }

    private void loadLatestBoardItems(){
        if(isLoadingBoardItems) return;

        isLoadingBoardItems = true;

        // progressbar を表示
        mBoardItemList.addHeaderView(mHeader);
        App.get().getTaskManager().getBoardItems(taskId, BOARD_ITEM_NUM_SKIP, new TaskManager.BoardItemListCallback() {
            @Override
            public void callback(List<BoardItem> b) {
                if (b != null && !b.isEmpty()) {
                    Long currentLatestNum = boardItems.isEmpty() ? -1 : boardItems.get(0).getNumber();
                    Long LatestNum = b.get(b.size() - 1).getNumber();

                    Log.d("loadLatestBoardItems", "" + currentLatestNum + "," + LatestNum);

                    // 現在表示している一番大きなレス番号よりも新しく読み込んだ全てのレス番号が大きならばリストをすべて置き換える
                    // また最新のレス番号が上に来るように逆順に入れる
                    if( (currentLatestNum == -1) || (LatestNum > currentLatestNum + BOARD_ITEM_NUM_SKIP)){
                        boardItems.clear();
                        for (ListIterator it = b.listIterator(b.size()); it.hasPrevious(); ) {
                            boardItems.add((BoardItem) it.previous());
                        }
                    } else {
                        for (ListIterator it = b.listIterator(b.size()); it.hasPrevious(); ) {
                            BoardItem item = (BoardItem) it.previous();
                            if (item.getNumber() > currentLatestNum) {
                                boardItems.add(0, item);
                            }
                        }
                    }

                } else {
                    // Todo: handle error
                }

                mBoardListItemAdapter.notifyDataSetChanged();
                mBoardItemList.removeHeaderView(mHeader);
                // 先頭へ移動
                mBoardItemList.setSelection(0);

                isLoadingBoardItems = false;
            }
        });

    }

    // Todo : fix int/long
    private void loadOlderBoardItems(){
        if(isLoadingBoardItems) return;

        isLoadingBoardItems = true;

        Long end = boardItems.get(boardItems.size()-1).getNumber() -1;
        Log.d("loadOlderBoardItems", "" + end);
        if(end == 0) {return;}

        // progressbar を表示
        mBoardItemList.addFooterView(mFooter);
        Long begin = (end - BOARD_ITEM_NUM_SKIP) < 0 ? 0 : (end - BOARD_ITEM_NUM_SKIP);
        App.get().getTaskManager().getBoardItems(taskId, new Integer(begin.toString()), new Integer(end.toString()), new TaskManager.BoardItemListCallback() {
            @Override
            public void callback(List<BoardItem> b) {
                if (b != null && !b.isEmpty()) {
                    for (ListIterator it = b.listIterator(b.size()); it.hasPrevious(); ) {
                        BoardItem item = (BoardItem) it.previous();
                        boardItems.add(item);
                    }
                    mBoardListItemAdapter.notifyDataSetChanged();
                } else {
                    // Todo: handle error
                }

                mBoardItemList.removeFooterView(mFooter);
                // 末尾へ移動
                mBoardItemList.setSelection(boardItems.size()-1);

                isLoadingBoardItems = false;
            }
        });
    }


    // Todo: same function in TaskListitem, this implementation should be improved
    private final long MIL_SECS_OF_MINUTE = 1000 * 60;
    private final long MIL_SECS_OF_HOUR = MIL_SECS_OF_MINUTE * 60;
    private final long MIL_SECS_OF_DAY  = MIL_SECS_OF_HOUR * 24;

    private String toRemainderString(Long date) {
        long now = new Date().getTime();
        long df = date - now;

        String pre = (df < 0) ? "" : "あと";
        String d   = Math.abs(df / MIL_SECS_OF_DAY) > 0 ? "" + Math.abs(df / MIL_SECS_OF_DAY) + "日" :
                Math.abs(df / MIL_SECS_OF_HOUR) > 0 ? "" + Math.abs(df / MIL_SECS_OF_HOUR) + "時間" :
                        "" + Math.abs(df / MIL_SECS_OF_MINUTE) + "分";
        String suf = (df < 0) ? "経過" : "";

        return pre + d + suf;
    }
    public boolean isNearDeadline(Long date){
        return (date - new Date().getTime()) < MIL_SECS_OF_HOUR * 24;
    }
}
