package com.Ease.Team.TeamCardReceiver;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "teamLinkCardReceivers")
@PrimaryKeyJoinColumn(name = "id")
public class TeamLinkCardReceiver extends TeamCardReceiver {

    public TeamLinkCardReceiver() {

    }

    @Override
    public String getType() {
        return "teamLinkApp";
    }
}