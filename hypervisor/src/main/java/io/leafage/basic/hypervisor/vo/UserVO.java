package io.leafage.basic.hypervisor.vo;

/**
 * vo class for user.
 *
 * @author wq li 2019/8/31 15:50
 **/
public class UserVO {

    /**
     * user
     */
    private String username;
    /**
     * 姓
     */
    private String firstname;
    /**
     * 名
     */
    private String lastname;

    /**
     * 描述
     */
    private String description;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
