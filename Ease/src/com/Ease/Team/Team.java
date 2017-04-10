package com.Ease.Team;

import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Created by thomas on 10/04/2017.
 */
public class Team {

    public static Team createTeam(String teamName, String company_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO teams values(?, ?, ?);");
        request.setNull();
        request.setInt(company_id);
        request.setString(teamName);
        String db_id = request.set().toString();
        int single_id = sm.getNextSingle_id();
        return new Team(db_id, single_id, teamName);
    }

    protected String db_id;
    protected int single_id;
    protected String teamName;

    public Team(String db_id, int single_id, String teamName) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.teamName = teamName;
    }
}
