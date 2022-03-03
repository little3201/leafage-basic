package io.leafage.basic.hypervisor.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * VO class for user.
 *
 * @author liwenqiang 2019/8/31 15:50
 **/
public class UserVO implements Serializable {

    private static final long serialVersionUID = -43247624019806041L;

    /**
     * 账号
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
     * 性别
     */
    private Character gender;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 出生日期
     */
    private LocalDate birthday;
    /**
     * 学历
     */
    private String degree;
    /**
     * 兴趣爱好
     */
    private String hobbies;
    /**
     * 民族
     */
    private String ethnicity;
    /**
     * 公司
     */
    private String company;
    /**
     * 职位
     */
    private String position;
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;


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

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}
