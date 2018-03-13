package com.Ease.Mail;

import com.Ease.Context.Variables;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Slack.SlackAPIWrapper;
import com.github.seratch.jslack.api.methods.SlackApiException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NewTeamThread extends Thread {
    private Team team;

    public NewTeamThread(Team team) {
        super();
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public void run() {
        if (!Variables.ENVIRONNEMENT.equals("Prod"))
            return;
        MailJetBuilder mailJetBuilder = new MailJetBuilder();
        mailJetBuilder.setFrom("contact@ease.space", "Agathe The Power");
        mailJetBuilder.addTo("thomas@ease.space");
        mailJetBuilder.addTo("victor@ease.space");
        mailJetBuilder.addTo("benjamin@ease.space");
        mailJetBuilder.setTemplateId(316829);
        mailJetBuilder.addVariable("team_name", this.getTeam().getName());
        TeamUser owner = this.getTeam().getTeamUserOwner();
        mailJetBuilder.addVariable("first_name", owner.getUser().getPersonalInformation().getFirst_name());
        mailJetBuilder.addVariable("last_name", owner.getUser().getPersonalInformation().getLast_name());
        mailJetBuilder.addVariable("phone_number", owner.getUser().getPersonalInformation().getPhone_number());
        mailJetBuilder.addVariable("email", owner.getEmail());
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        mailJetBuilder.addVariable("date", dateFormat.format(team.getSubscription_date()));
        mailJetBuilder.sendEmail();
        StringBuilder slackMessage = new StringBuilder("*✅New company*\nNew company information: ")
                .append(team.getName())
                .append(", ")
                .append(owner.getUser().getPersonalInformation().getFirst_name())
                .append(" ")
                .append(owner.getUser().getPersonalInformation().getLast_name())
                .append(", ")
                .append(owner.getUser().getPersonalInformation().getPhone_number())
                .append("\n=======\n=======\n=======");
        try {
            SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", slackMessage.toString());
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
    }
}
