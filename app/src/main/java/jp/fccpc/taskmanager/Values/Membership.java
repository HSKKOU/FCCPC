package jp.fccpc.taskmanager.Values;

/**
 * Created by Shunta on 10/22/15.
 */
public class Membership {
    /*
- fields (group\_agreedとuser\_agreedの少なくとも一方は`true`でなければならない)
    - (group\_id: Int) (readonly)
    - (user\_id: Int) (readonly)
    - group\_agreed: Bool
    - user\_agreed: Bool
    - (eTag: String) (readonly): GETした場合に自動更新する。新規作成など、View側で生成する場合はnullを代入
    */

    private Long membershipId;
    private Long groupId;
    private Long userId;
    private boolean groupAgreed;
    private boolean userAgreed;
    private String eTag;

    public Membership(Long membershipId, Long groupId, Long userId, boolean groupAgreed, boolean userAgreed, String eTag) {
        this.membershipId = membershipId;
        this.groupId = groupId;
        this.userId = userId;

        if (!groupAgreed && !userAgreed)
            throw new IllegalArgumentException("userAgreedかgroupAgreeのどちらか一方は真である必要があります");
        this.groupAgreed = groupAgreed;
        this.userAgreed = userAgreed;
        this.eTag = eTag;
    }

    public Membership(Long groupId, Long userId, boolean groupAgreed, boolean userAgreed, String eTag) {
        this(null, groupId, userId, groupAgreed, userAgreed, eTag);
    }

    public Membership(Long groupId, Long userId, boolean groupAgreed, boolean userAgreed) {
        this(groupId, userId, groupAgreed, userAgreed, null);
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isGroupAgreed() {
        return groupAgreed;
    }

    public void setGroupAgreed(boolean groupAgreed) {
        this.groupAgreed = groupAgreed;
    }

    public boolean isUserAgreed() {
        return userAgreed;
    }

    public void setUserAgreed(boolean userAgreed) {
        this.userAgreed = userAgreed;
    }

    public String getETag() {
        return eTag;
    }
}