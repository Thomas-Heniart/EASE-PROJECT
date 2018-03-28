package com.Ease.User;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "status")
public class UserStatus {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_connection")
    private boolean firstConnection = true;

    @Column(name = "chrome_scrapping")
    private boolean chromeScrappingDone = false;

    @Column(name = "apps_manually_added")
    private boolean appsManuallyAdded = false;

    @Column(name = "tuto_done")
    private boolean tutoDone = false;

    @Column(name = "last_connection")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastConnection = new Date();

    @Column(name = "homepage_email_sent")
    private boolean homepageEmailSent = false;

    @Column(name = "terms_reviewed")
    private boolean termsReviewed = true;

    @Column(name = "team_tuto_done")
    private boolean teamTutoDone = false;

    @Column(name = "edit_email_code")
    private String editEmailCode;

    @Column(name = "email_requested")
    private String emailRequested;

    @Column(name = "new_feature_seen")
    private boolean newFeatureSeen = false;

    @Column(name = "onboarding_step")
    private Integer onboardingStep = 0;

    @Column(name = "tip_team_user_settings_seen")
    private boolean tipTeamUserSettingsSeen = false;

    @Column(name = "tip_team_channel_settings_seen")
    private boolean tipTeamChannelSettingsSeen = false;

    @Column(name = "tip_importation_seen")
    private boolean tipImportationSeen = false;

    @Column(name = "registered")
    private boolean registered = false;

    @Column(name = "click_on_eight_apps_in_a_day")
    private boolean clickOnEightAppsInADay = false;

    @Column(name = "click_on_thirty_apps_in_a_week")
    private boolean clickOnThirtyAppsInAWeek = false;

    @Column(name = "popup_choose_connection_lifetime_seen")
    private boolean popupChooseConnectionLifetimeSeen = false;

    public UserStatus() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isFirstConnection() {
        return firstConnection;
    }

    public void setFirstConnection(boolean firstConnection) {
        this.firstConnection = firstConnection;
    }

    public boolean isChromeScrappingDone() {
        return chromeScrappingDone;
    }

    public void setChromeScrappingDone(boolean chromeScrappingDone) {
        this.chromeScrappingDone = chromeScrappingDone;
    }

    public boolean isAppsManuallyAdded() {
        return appsManuallyAdded;
    }

    public void setAppsManuallyAdded(boolean appsManuallyAdded) {
        this.appsManuallyAdded = appsManuallyAdded;
    }

    public boolean isTutoDone() {
        return tutoDone;
    }

    public void setTutoDone(boolean tutoDone) {
        this.tutoDone = tutoDone;
    }

    public Date getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Date lastConnection) {
        this.lastConnection = lastConnection;
    }

    public boolean isHomepageEmailSent() {
        return homepageEmailSent;
    }

    public void setHomepageEmailSent(boolean homepageEmailSent) {
        this.homepageEmailSent = homepageEmailSent;
    }

    public boolean isTermsReviewed() {
        return termsReviewed;
    }

    public void setTermsReviewed(boolean termsReviewed) {
        this.termsReviewed = termsReviewed;
    }

    public boolean isTeamTutoDone() {
        return teamTutoDone;
    }

    public void setTeamTutoDone(boolean teamTutoDone) {
        this.teamTutoDone = teamTutoDone;
    }

    public String getEditEmailCode() {
        return editEmailCode;
    }

    public void setEditEmailCode(String editEmailCode) {
        this.editEmailCode = editEmailCode;
    }

    public String getEmailRequested() {
        return emailRequested;
    }

    public void setEmailRequested(String emailRequested) {
        this.emailRequested = emailRequested;
    }

    public boolean isNewFeatureSeen() {
        return newFeatureSeen;
    }

    public void setNewFeatureSeen(boolean newFeatureSeen) {
        this.newFeatureSeen = newFeatureSeen;
    }

    public Integer getOnboardingStep() {
        return onboardingStep;
    }

    public void setOnboardingStep(Integer onboardingStep) {
        this.onboardingStep = onboardingStep;
    }

    public boolean isTipTeamUserSettingsSeen() {
        return tipTeamUserSettingsSeen;
    }

    public void setTipTeamUserSettingsSeen(boolean tipTeamUserSettingsSeen) {
        this.tipTeamUserSettingsSeen = tipTeamUserSettingsSeen;
    }

    public boolean isTipTeamChannelSettingsSeen() {
        return tipTeamChannelSettingsSeen;
    }

    public void setTipTeamChannelSettingsSeen(boolean tipTeamChannelSettingsSeen) {
        this.tipTeamChannelSettingsSeen = tipTeamChannelSettingsSeen;
    }

    public boolean isTipImportationSeen() {
        return tipImportationSeen;
    }

    public void setTipImportationSeen(boolean tipImportationSeen) {
        this.tipImportationSeen = tipImportationSeen;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean clickOnEightAppsInADay() {
        return clickOnEightAppsInADay;
    }

    public void setClickOnEightAppsInADay(boolean clickOnEightAppsInADay) {
        this.clickOnEightAppsInADay = clickOnEightAppsInADay;
    }

    public boolean clickOnThirtyAppsInAWeek() {
        return clickOnThirtyAppsInAWeek;
    }

    public void setClickOnThirtyAppsInAWeek(boolean clickOnThirtyAppsInAWeek) {
        this.clickOnThirtyAppsInAWeek = clickOnThirtyAppsInAWeek;
    }

    public boolean isPopupChooseConnectionLifetimeSeen() {
        return popupChooseConnectionLifetimeSeen;
    }

    public void setPopupChooseConnectionLifetimeSeen(boolean popupChooseConnectionLifetimeSeen) {
        this.popupChooseConnectionLifetimeSeen = popupChooseConnectionLifetimeSeen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStatus that = (UserStatus) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("tuto_done", this.isTutoDone());
        res.put("team_tuto_done", this.isTeamTutoDone());
        res.put("terms_reviewed", this.isTermsReviewed());
        res.put("onboarding_step", this.getOnboardingStep());
        res.put("tip_team_user_settings_seen", this.isTipTeamUserSettingsSeen());
        res.put("tip_team_channel_settings_seen", this.isTipTeamChannelSettingsSeen());
        res.put("tip_importation_seen", this.isTipImportationSeen());
        res.put("popup_choose_connection_lifetime_seen", this.isPopupChooseConnectionLifetimeSeen());
        return res;
    }

    public void setTip(String name, boolean value) {
        switch (name) {
            case "tip_team_user_settings_seen":
                this.setTipTeamUserSettingsSeen(value);
                break;
            case "tip_team_channel_settings_seen":
                this.setTipTeamChannelSettingsSeen(value);
                break;
            case "tip_importation_seen":
                this.setTipImportationSeen(value);
                break;
            default:
                break;
        }
    }
}