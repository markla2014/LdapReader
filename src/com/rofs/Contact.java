package com.rofs;

/**
 * Created by markla on 2017/6/7.
 */
public class Contact {
    private String realName;
    private String mail;
    private String department;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "realName='" + realName + '\'' +
                ", mail='" + mail + '\'' +
                ", department='" + department + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
