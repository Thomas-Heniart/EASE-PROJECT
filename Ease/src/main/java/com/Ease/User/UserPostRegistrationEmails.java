package com.Ease.User;

import javax.persistence.*;

@Entity
@Table(name = "USER_POST_REGISTRATION_EMAILS")
public class UserPostRegistrationEmails {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "email_j2_sent")
    private boolean email_j2_sent = false;

    @Column(name = "email_j4_sent")
    private boolean email_j4_sent = false;

    @Column(name = "email_j6_sent")
    private boolean email_j6_sent = false;

    @Column(name = "email_j13_sent")
    private boolean email_j13_sent = false;

    @Column(name = "email_team_creation_sent")
    private boolean email_team_creation_sent = false;

    @Column(name = "email_use_seven_on_fourteen_days_sent")
    private boolean email_use_seven_on_fourteen_days_sent = false;

    public UserPostRegistrationEmails() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isEmail_j2_sent() {
        return email_j2_sent;
    }

    public void setEmail_j2_sent(boolean email_j2_sent) {
        this.email_j2_sent = email_j2_sent;
    }

    public boolean isEmail_j4_sent() {
        return email_j4_sent;
    }

    public void setEmail_j4_sent(boolean email_j4_sent) {
        this.email_j4_sent = email_j4_sent;
    }

    public boolean isEmail_j6_sent() {
        return email_j6_sent;
    }

    public void setEmail_j6_sent(boolean email_j6_sent) {
        this.email_j6_sent = email_j6_sent;
    }

    public boolean isEmail_j13_sent() {
        return email_j13_sent;
    }

    public void setEmail_j13_sent(boolean email_j13_sent) {
        this.email_j13_sent = email_j13_sent;
    }

    public boolean isEmail_team_creation_sent() {
        return email_team_creation_sent;
    }

    public void setEmail_team_creation_sent(boolean email_team_creation_sent) {
        this.email_team_creation_sent = email_team_creation_sent;
    }

    public boolean isEmail_use_seven_on_fourteen_days_sent() {
        return email_use_seven_on_fourteen_days_sent;
    }

    public void setEmail_use_seven_on_fourteen_days_sent(boolean email_use_seven_on_fourteen_days_sent) {
        this.email_use_seven_on_fourteen_days_sent = email_use_seven_on_fourteen_days_sent;
    }
}