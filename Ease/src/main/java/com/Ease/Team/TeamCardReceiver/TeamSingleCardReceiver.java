package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamUser;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamSingleCardReceivers")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamSingleCardReceiver extends TeamCardReceiver {

    @Column(name = "allowed_to_see_password")
    private boolean allowed_to_see_password;

    public TeamSingleCardReceiver() {

    }

    public TeamSingleCardReceiver(App app, TeamCard teamCard, TeamUser teamUser, boolean allowed_to_see_password) {
        super(app, teamCard, teamUser);
        /* Everyone can see password for the moment */
        //this.allowed_to_see_password = allowed_to_see_password;
        this.allowed_to_see_password = true;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public boolean isAllowed_to_see_password() {
        return allowed_to_see_password;
    }

    public void setAllowed_to_see_password(boolean allowed_to_see_password) {
        /* Everyone can see password for the moment */
        //this.allowed_to_see_password = allowed_to_see_password;
        this.allowed_to_see_password = true;
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("allowed_to_see_password", this.isAllowed_to_see_password());
        return res;
    }

    @Override
    public JSONObject getCardJson() {
        JSONObject res = super.getCardJson();
        res.put("allowed_to_see_password", this.isAllowed_to_see_password());
        return res;
    }

    @Override
    public String getType() {
        return "teamSingleApp";
    }

    @Override
    public boolean isTeamSingleCardReceiver() {
        return true;
    }
}