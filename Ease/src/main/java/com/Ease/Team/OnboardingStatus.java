package com.Ease.Team;

import com.Ease.Team.Onboarding.OnboardingSteps;

import javax.persistence.*;

@Entity
@Table(name = "TEAM_ONBOARDING_STATUS")
public class OnboardingStatus {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "step")
    private int step = OnboardingSteps.TEAM_CREATED.ordinal();

    public OnboardingStatus() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}