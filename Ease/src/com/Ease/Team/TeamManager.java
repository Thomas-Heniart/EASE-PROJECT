package com.Ease.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.*;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by thomas on 28/04/2017.
 */
public class TeamManager {

    protected List<Team> teams;
    protected HashMap<Integer, Team> teamIdMap;

    public TeamManager(ServletContext context, DataBaseConnection db) throws HttpServletException {
        this.teams = Team.loadTeams(context, db);
        this.teamIdMap = new HashMap<>();
        for (Team team : this.teams)
            this.teamIdMap.put(team.getDb_id(), team);

    }

    public List<Team> getTeams() {
        return teams;
    }

    public Team getTeamWithId(Integer team_id) throws HttpServletException {
        Team team = this.teamIdMap.get(team_id);
        if (team == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such team");
        for (Channel channel : team.getChannels()) {
            if (!channel.getTeamUsers().isEmpty()) {

            }
        }
        return team;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
        this.teamIdMap.put(team.getDb_id(), team);
    }

    public void removeTeam(Team team) {
        this.teams.remove(team);
        this.teamIdMap.remove(team.getDb_id());
    }

    public void removeTeamWithId(Integer team_id) throws HttpServletException {
        Team team = this.getTeamWithId(team_id);
        this.removeTeam(team);
    }

    public Team getTeamWithName(String team_name) {
        for (Team team : this.getTeams()) {
            if (team.getName().equals(team_name))
                return team;
        }
        return null;
    }

    public void updateTeamsSubscriptions() {
        Date now = new Date();
        HibernateQuery hibernateQuery = new HibernateQuery();
        for (Team team : this.getTeams()) {
            team.updateSubscription(now);
            hibernateQuery.saveOrUpdateObject(team);
        }
        try {
            hibernateQuery.commit();
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }
}
