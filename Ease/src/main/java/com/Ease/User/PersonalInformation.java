package com.Ease.User;

import javax.persistence.*;

@Entity
@Table(name = "USER_PERSONAL_INFORMATION")
public class PersonalInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String first_name = "";

    @Column(name = "last_name")
    private String last_name = "";

    @Column(name = "phone_number")
    private String phone_number = "";

    public PersonalInformation() {

    }

    public PersonalInformation(String first_name, String last_name, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}