package jp.fccpc.taskmanager.Values;

/**
 * Created by Shunta on 10/22/15.
 */
public class BoardItem {
    /*
- fields (すべてのフィールドがreadonly)
    - number: Int レス番号(1スタート)
    - user\_id: Int ユーザID
    - user\_name: String ユーザ名
    - created\_at: Time 投稿時刻
    - content: String 投稿内容

     */

    private Long number;
    private Long userId;
    private String userName;
    private Long createdAt;
    private String content;

    public BoardItem(Long number, Long userId, String userName, Long createdAt, String content) {
        this.number = number;
        this.userId = userId;
        this.userName = userName;
        this.createdAt = createdAt;
        this.content = content;
    }

    public Long getNumber() {
        return number;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public String getContent() {
        return content;
    }
}
