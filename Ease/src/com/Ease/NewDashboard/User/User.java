package com.Ease.NewDashboard.User;

import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManagerHibernate;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.servlet.http.Cookie;
import java.util.Date;

/**
 * Created by thomas on 21/04/2017.
 */
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "firstName")
    protected String firstName;

    @Column(name = "lastName")
    protected String lastName;

    @Column(name = "email")
    protected String email;

    @Column(name = "registration_date")
    protected Date registrationDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "key_id")
    protected Keys keys;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "option_id")
    protected Options options;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id")
    protected Status status;

    @Transient
    protected ProfileManager profileManager;

    public User(String firstName, String lastName, String email, Date registrationDate, Keys keys, Options options, Status status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.keys = keys;
        this.options = options;
        this.status = status;
    }

    public User() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Keys getKeys() {

        return keys;
    }

    public void setKeys(Keys keys) {
        this.keys = keys;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void populateProfileManager() {
        this.profileManager = new ProfileManager();
        this.profileManager.populate(this.db_id);
    }

    public void createFirstProfiles() {
        this.profileManager = new ProfileManager();
        this.profileManager.createFirstProfilesForUser(this);
    }

    public void logout(ServletManagerHibernate sm) throws GeneralException {
        Cookie cookie = null;
        Cookie 	cookies[] = sm.getRequest().getCookies();
        if (cookies != null){
            for (int i = 0;i < cookies.length ; i++) {
                cookie = cookies[i];
                if((cookie.getName()).compareTo("sId") == 0){
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    sm.getResponse().addCookie(cookie);
                } else if((cookie.getName()).compareTo("sTk") == 0){
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    sm.getResponse().addCookie(cookie);
                }
            }
        }
    }
}
