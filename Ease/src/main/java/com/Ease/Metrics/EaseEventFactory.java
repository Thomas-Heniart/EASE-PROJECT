package com.Ease.Metrics;

import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import org.json.JSONObject;

public class EaseEventFactory {
    private static EaseEventFactory ourInstance = new EaseEventFactory();

    public static EaseEventFactory getInstance() {
        return ourInstance;
    }

    private EaseEventFactory() {
    }

    public EaseEvent createPasswordUsedEvent(Integer user_id, String from, TeamCard teamCard) {
        JSONObject data = new JSONObject();
        data.put("from", from);
        data.put("id", teamCard.getDb_id());
        data.put("type", teamCard.getType());
        EaseEvent easeEvent = new EaseEvent();
        easeEvent.setName("PasswordUsed");
        easeEvent.setData(data.toString());
        easeEvent.setUser_id(user_id);
        return easeEvent;
    }

    public EaseEvent createPasswordUsedEvent(Integer user_id, String from, App app) {
        JSONObject data = new JSONObject();
        data.put("from", from);
        data.put("id", app.getDb_id());
        data.put("type", app.getType());
        if (app.getTeamCardReceiver() != null) {
            data.put("type", app.getTeamCardReceiver().getType());
            data.put("type", app.getTeamCardReceiver().getTeamCard().getType());
        }
        EaseEvent easeEvent = new EaseEvent();
        easeEvent.setName("PasswordUsed");
        easeEvent.setData(data.toString());
        easeEvent.setUser_id(user_id);
        return easeEvent;
    }
}
