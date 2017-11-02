package com.Ease.Team.TeamCard;

import javax.persistence.*;

@Entity
@Table(name = "teamEnterpriseCards")
@PrimaryKeyJoinColumn(name = "id")
public class TeamEnterpriseCard extends TeamWebsiteCard {

    public TeamEnterpriseCard() {

    }

    @Override
    public String getType() {
        return "teamEnterpriseCard";
    }
}