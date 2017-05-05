package com.Ease.NewDashboard.User;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManagerHibernate;
import com.Ease.Website.Tag;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.servlet.http.Cookie;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 21/04/2017.
 */
@Entity
@Table(name = "users")
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected List<SessionSave> sessionSaveList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected List<UserEmail> userEmails;

    @Transient
    protected SessionSave currentSessionSave;

    @Transient
    protected ProfileManager profileManager;

    public User(String firstName, String lastName, String email, Date registrationDate, Keys keys, Options options, Status status, List<SessionSave> sessionSaveList, List<UserEmail> userEmails) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.keys = keys;
        this.options = options;
        this.status = status;
        this.sessionSaveList = sessionSaveList;
        this.userEmails = userEmails;
    }

    public User(String firstName, String lastName, String email, Date registrationDate, Keys keys, Options options, Status status, List<SessionSave> sessionSaveList) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = registrationDate;
        this.keys = keys;
        this.options = options;
        this.status = status;
        this.sessionSaveList = sessionSaveList;
    }

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

    public List<SessionSave> getSessionSaveList() {
        return sessionSaveList;
    }

    public void setSessionSaveList(List<SessionSave> sessionSaveList) {
        this.sessionSaveList = sessionSaveList;
    }

    public List<UserEmail> getUserEmails() {
        return userEmails;
    }

    public void setUserEmails(List<UserEmail> userEmails) {
        this.userEmails = userEmails;
    }

    public SessionSave getCurrentSessionSave() {
        if (this.currentSessionSave != null)
            return this.currentSessionSave;
        HibernateQuery query = new HibernateQuery();
        this.currentSessionSave = new SessionSave(this.getKeys().getDecipheredKeyUser(), this);
        query.saveOrUpdateObject(this.currentSessionSave);
        query.commit();
        return this.currentSessionSave;
    }

    public void setCurrentSessionSave(SessionSave currentSessionSave) {
        this.currentSessionSave = currentSessionSave;
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

    /* Status utils */
    public boolean appsImported() {
        return this.status.appsImported();
    }

    public boolean allTipsDone() {
        return this.status.allTipsDone();
    }

    public List<String> getUnverifiedEmails() {
        List<String> res = new LinkedList<String>();
        for (UserEmail userEmail : this.getUserEmails()) {
            if (userEmail.getVerified())
                res.add(userEmail.getEmail());
        }
        return res;
    }

    public List<String> getVerifiedEmails() {
        List<String> res = new LinkedList<String>();
        for (UserEmail userEmail : this.getUserEmails()) {
            if (!userEmail.getVerified())
                res.add(userEmail.getEmail());
        }
        return res;
    }

    public boolean canSeeTag (Tag tag) {
        return true;
    }

    public void lazyLoadUserEmails() {
        HibernateQuery query = new HibernateQuery();
        this.getUserEmails();
        query.commit();
        System.out.println("UserEmails size: " + this.userEmails.size());
    }
}
