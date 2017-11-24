package com.Ease.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "passwordLost")
public class PasswordLost {

    @Column(name = "linkCode")
    private String code;

    @Id
    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "dateOfRequest")
    @Temporal(TemporalType.TIMESTAMP)
    private Date request_date = new Date();

    public PasswordLost() {

    }

    public PasswordLost(String code, Integer user_id) {
        this.code = code;
        this.user_id = user_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getRequest_date() {
        return request_date;
    }

    public void setRequest_date(Date request_date) {
        this.request_date = request_date;
    }
}