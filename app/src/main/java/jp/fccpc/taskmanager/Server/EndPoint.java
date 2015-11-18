package jp.fccpc.taskmanager.Server;

/**
 * Created by hskk1120551 on 15/10/25.
 */
public class EndPoint {
    private static final String REST_PATH           = "rest/";
    private static final String GROUP_PATH          = "groups";
    private static final String TASK_PATH           = "tasks";
    private static final String TASK_FINISHED_PATH  = "finished";
    private static final String BOARD_ITEM_PATH     = "board_items";
    private static final String MEMBERSHIPS_PATH    = "memberships";
    private static final String USER_PATH           = "user";
    private static final String USER_SEARCH_PATH    = USER_PATH + "/search";

    private static final String AUTH_PATH   = "auth/";
    private static final String LOGIN_PATH  = "login";
    private static final String LOGOUT_PATH = "logout";

    // Task's end point
    public static String task(Long taskId) {
        if(taskId == null) {return REST_PATH + TASK_PATH;}
        return REST_PATH + TASK_PATH + "/" + taskId;
    }


    public static String taskFinished(Long taskId) {
        if(taskId == null) {return REST_PATH + TASK_PATH + "/" + TASK_FINISHED_PATH;}
        return REST_PATH + TASK_PATH + "/" + TASK_FINISHED_PATH + "/" + taskId;
    }

    // BoardItem's end point
    public static String boardItem(Long taskId) {
        if(taskId == null) {return null;}
        return REST_PATH + TASK_PATH + "/" +  + taskId + "/" + BOARD_ITEM_PATH;
    }

    // Group's end point
    public static String group(Long groupId) {
        if(groupId == null) {return REST_PATH + GROUP_PATH;}
        return REST_PATH + GROUP_PATH + "/" + groupId;
    }
    public static String groupTasks(Long groupId) {
        if(groupId == null) {return null;}
        return REST_PATH + GROUP_PATH + "/" +  + groupId + "/" + TASK_PATH;
    }

    // Membership's end point
    public static String membership(Long groupId, Long userId) {
        if(groupId == null || userId == null) {return REST_PATH + MEMBERSHIPS_PATH;}
        return REST_PATH + MEMBERSHIPS_PATH + "/" + groupId + "," + userId;
    }


    // User's end point
    public static String user(Long userId) {
        if (userId == null) { return USER_PATH; }
        return USER_PATH + "/" + userId;
    }
    public static String userSearch() {return USER_SEARCH_PATH;}


    // Auth end point
    public static String login() {return AUTH_PATH + LOGIN_PATH;}
    // TODO: fix after API completion
    public static String logout() {return AUTH_PATH + LOGOUT_PATH;}
}
