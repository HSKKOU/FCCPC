package jp.fccpc.taskmanager.Values;

import java.util.List;

/**
 * Created by Shunta on 10/22/15.
 */
public class Group {
    /*
      - fields
    - (id: Int) (readonly)
    - name: String
    - (administrator: Int) (readonly)
    - (memberships: \[Membership\]) (readonly)
    - (updated\_at: Time) (readonly): サーバが自動的に更新する
    - (eTag: String) (readonly): GETした場合に自動更新する。新規作成など、View側で生成する場合はnullを代入
     */
    private Long groupId;
    private String name;
    private Long administratorId;
    private List<Membership> memberships;
    private Long updatedAt;
    private String eTag;

    public Group(Long groupId, String name, Long administratorId, List<Membership> memberships, Long updatedAt, String eTag) {
        this.groupId = groupId;
        this.name = name;
        this.administratorId = administratorId;
        this.memberships = memberships;
        this.updatedAt = updatedAt;
        this.eTag = eTag;
    }

    public Group(Long groupId, String name, Long administratorId, List<Membership> memberships, Long updatedAt) {
        this(groupId, name, administratorId, memberships, updatedAt, null);
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getAdministratorId() {
        return administratorId;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getETag() {
        return eTag;
    }
}
