package jp.fccpc.taskmanager.Views.GroupList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.fccpc.taskmanager.Managers.App;
import jp.fccpc.taskmanager.Managers.GroupManager;
import jp.fccpc.taskmanager.Values.Group;
import jp.fccpc.taskmanager.Views.TaskList.TaskListFragment;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link TaskListFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class GroupListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */

    //private ArrayAdapter<String> adapterGroup;
    // private List<String> GroupNames;
    private GroupListAdapter adapterGroup;
    private List<Group> groups;

    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        //public void onItemSelected(String id);
        public void onItemSelected(int position);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int position) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        GroupNames = new ArrayList<>();
//        adapterGroup = new ArrayAdapter<String>(
//                getActivity(),
//                //android.R.layout.simple_expandable_list_item_1,
//                //android.R.layout.simple_list_item_activated_1,
//                R.layout.list_item_group,
//                R.id.group_list_item,
//                GroupNames);
        groups = new ArrayList<>();
        adapterGroup = new GroupListAdapter(getActivity(), groups);
        setListAdapter(adapterGroup);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    private int prevPosition = ListView.INVALID_POSITION;
    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        listView.setItemChecked(position, true);
        listView.setSelected(true);
        listView.setSelection(position);

        prevPosition = position;
        mCallbacks.onItemSelected(position);
    }

    public void clearSelection(){
        if(prevPosition != ListView.INVALID_POSITION){
            getListView().setItemChecked(prevPosition, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GroupListFragment", "onResume");

        updateGroupList();
    }

    public void updateGroupList(){
        App.get().getGroupManager().getList(new GroupManager.GroupListCallback() {
            @Override
            public void callback(List<Group> groupList) {
                if (groupList != null) {
//                    GroupNames.clear();
//                    for (Group g : groupList) {
//                        GroupNames.add(g.getName());
//                    }

                    groups.clear();
                    groups.addAll(groupList);

                    adapterGroup.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

}
