package jp.fccpc.taskmanager.Values;

/**
 * Created by Shunta on 10/22/15.
 */
public class User {
    /*
     userId
     name
     email_address
     */
    private Long userId;
    private String name;
    private String emailAddress;

    public User(Long userId, String name, String emailAddress) {
        this.userId = userId;
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
