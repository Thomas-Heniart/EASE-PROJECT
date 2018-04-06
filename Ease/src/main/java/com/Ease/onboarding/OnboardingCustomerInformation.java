package com.Ease.onboarding;

import com.Ease.Context.Variables;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ONBOARDING_CUSTOMER_INFORMATION")
public class OnboardingCustomerInformation {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "teamName")
    private String teamName;

    @Column(name = "teamSize")
    private Integer teamSize;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "planId")
    private Integer planId;

    @Column(name = "teamCreationLink")
    private String teamCreationLink;

    @Column(name = "transferOwnershipLink")
    private String transferOwnershipLink;

    @Column(name = "creationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @Column(name = "lastUpdateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDate = new Date();

    public OnboardingCustomerInformation() {
    }

    public OnboardingCustomerInformation(String email, String firstName, String lastName, String teamName, Integer teamSize, String phoneNumber, Integer planId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamName = teamName;
        this.teamSize = teamSize;
        this.phoneNumber = phoneNumber;
        this.planId = planId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getTeamCreationLink() {
        return teamCreationLink;
    }

    public void setTeamCreationLink(String teamCreationLink) {
        this.teamCreationLink = teamCreationLink;
    }

    public String getTransferOwnershipLink() {
        return transferOwnershipLink;
    }

    public void setTransferOwnershipLink(String transferOwnershipLink) {
        this.transferOwnershipLink = transferOwnershipLink;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OnboardingCustomerInformation that = (OnboardingCustomerInformation) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void generateTeamCreationLink() {
        this.setTeamCreationLink(Variables.URL_PATH + "api/v1/admin/onboarding/create-team?id=" + this.getId());
    }
}
