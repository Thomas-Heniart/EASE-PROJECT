package com.Ease.Metrics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamWebsiteCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;

import java.util.TimerTask;

public class MetricsSchedulerTask extends TimerTask {

    private TeamManager teamManager;

    public MetricsSchedulerTask(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            for (Team team : teamManager.getTeams(hibernateQuery)) {
                TeamMetrics teamMetrics = TeamMetrics.getMetrics(team.getDb_id(), hibernateQuery);
                int people_invited = 0;
                int people_joined = 0;
                int people_with_cards = 0;
                StringBuilder people_invited_emails = new StringBuilder();
                StringBuilder people_joined_emails = new StringBuilder();
                StringBuilder people_with_cards_emails = new StringBuilder();
                for (TeamUser teamUser : team.getTeamUsers().values()) {
                    people_invited++;
                    people_invited_emails.append(teamUser.getEmail()).append(";");
                    if (teamUser.isVerified()) {
                        people_joined++;
                        people_joined_emails.append(teamUser.getEmail()).append(";");
                        if (!teamUser.getTeamCardReceivers().isEmpty()) {
                            people_with_cards++;
                            people_with_cards_emails.append(teamUser.getEmail()).append(";");
                        }
                    }
                }
                int cards = 0;
                int cards_with_receiver = 0;
                int cards_with_receiver_and_password_policy = 0;
                int single_cards = 0;
                int enterprise_cards = 0;
                int link_cards = 0;
                for (TeamCard teamCard : team.getTeamCardSet()) {
                    cards++;
                    if (!teamCard.getTeamCardReceiverMap().isEmpty()) {
                        cards_with_receiver++;
                        if (teamCard.isTeamWebsiteCard() && ((TeamWebsiteCard) teamCard).getPassword_reminder_interval() > 0)
                            cards_with_receiver_and_password_policy++;
                    }
                    if (teamCard.isTeamSingleCard())
                        single_cards++;
                    else if (teamCard.isTeamEnterpriseCard())
                        enterprise_cards++;
                    else
                        link_cards++;
                }
                int room_number = 0;
                StringBuilder room_names = new StringBuilder("(");
                for (Channel channel : team.getChannels().values()) {
                    room_number++;
                    room_names.append(channel.getName()).append(", ");
                }
                int people_click_on_app_once = 0;
                StringBuilder people_click_on_app_once_emails = new StringBuilder();
                int people_click_on_app_three_times = 0;
                StringBuilder people_click_on_app_three_times_emails = new StringBuilder();
                int people_click_on_app_five_times = 0;
                int people_with_personnal_apps = 0;
                StringBuilder people_with_personnal_apps_emails = new StringBuilder();
                StringBuilder people_click_on_app_five_times_emails = new StringBuilder();
                for (TeamUser teamUser : team.getTeamUsers().values()) {
                    if (!teamUser.isVerified())
                        continue;
                    int week_clicks = 0;
                    for (TeamCardReceiver teamCardReceiver : teamUser.getTeamCardReceivers()) {
                        if (teamCardReceiver.getApp().hasBeenClickedForDays(5, hibernateQuery)) {
                            week_clicks = 5;
                            break;
                        } else if (teamCardReceiver.getApp().hasBeenClickedForDays(3, hibernateQuery))
                            week_clicks = 3;
                        else if (teamCardReceiver.getApp().hasBeenClickedForDays(1, hibernateQuery))
                            week_clicks = 1;
                    }
                    switch (week_clicks) {
                        case 5:
                            people_click_on_app_once++;
                            people_click_on_app_three_times++;
                            people_click_on_app_five_times++;
                            people_click_on_app_once_emails.append(teamUser.getEmail()).append(";");
                            people_click_on_app_three_times_emails.append(teamUser.getEmail()).append(";");
                            people_click_on_app_five_times_emails.append(teamUser.getEmail()).append(";");
                            break;
                        case 3:
                            people_click_on_app_once++;
                            people_click_on_app_three_times++;
                            people_click_on_app_once_emails.append(teamUser.getEmail()).append(";");
                            people_click_on_app_three_times_emails.append(teamUser.getEmail()).append(";");
                            break;
                        case 1:
                            people_click_on_app_once++;
                            people_click_on_app_once_emails.append(teamUser.getEmail()).append(";");
                            break;
                        default:
                            break;
                    }
                    for (App app : teamUser.getUser().getApps()) {
                        if (app.getTeamCardReceiver() == null && !app.isEmpty()) {
                            people_with_personnal_apps++;
                            people_with_personnal_apps_emails.append(teamUser.getEmail()).append(";");
                            break;
                        }
                    }
                }
                room_names.replace(room_names.length() - 2, room_names.length(), ")");
                if (people_with_personnal_apps_emails.length() > 0)
                    people_with_personnal_apps_emails.deleteCharAt(people_with_personnal_apps_emails.length() - 1);
                if (people_invited_emails.length() > 0)
                    people_invited_emails.deleteCharAt(people_invited_emails.length() - 1);
                if (people_joined_emails.length() > 0)
                    people_joined_emails.deleteCharAt(people_joined_emails.length() - 1);
                if (people_with_cards_emails.length() > 0)
                    people_with_cards_emails.deleteCharAt(people_with_cards_emails.length() - 1);
                if (people_click_on_app_once_emails.length() > 0)
                    people_click_on_app_once_emails.deleteCharAt(people_click_on_app_once_emails.length() - 1);
                if (people_click_on_app_three_times_emails.length() > 0)
                    people_click_on_app_three_times_emails.deleteCharAt(people_click_on_app_three_times_emails.length() - 1);
                if (people_click_on_app_five_times_emails.length() > 0)
                    people_click_on_app_five_times_emails.deleteCharAt(people_click_on_app_five_times_emails.length() - 1);
                teamMetrics.setPeople_invited(people_invited);
                teamMetrics.setPeople_invited_emails(people_invited_emails.toString());
                teamMetrics.setPeople_joined(people_joined);
                teamMetrics.setPeople_joined_emails(people_joined_emails.toString());
                teamMetrics.setPeople_with_cards(people_with_cards);
                teamMetrics.setPeople_with_cards_emails(people_with_cards_emails.toString());
                teamMetrics.setPeople_with_personnal_apps(people_with_personnal_apps);
                teamMetrics.setPeople_with_personnal_apps_emails(people_with_personnal_apps_emails.toString());
                teamMetrics.setCards(cards);
                teamMetrics.setCards_with_receiver(cards_with_receiver);
                teamMetrics.setCards_with_receiver_and_password_policy(cards_with_receiver_and_password_policy);
                teamMetrics.setSingle_cards(single_cards);
                teamMetrics.setEnterprise_cards(enterprise_cards);
                teamMetrics.setLink_cards(link_cards);
                teamMetrics.setRoom_number(room_number);
                teamMetrics.setRoom_names(room_names.toString());
                teamMetrics.setPeople_click_on_app_once(people_click_on_app_once);
                teamMetrics.setPeople_click_on_app_three_times(people_click_on_app_three_times);
                teamMetrics.setPeople_click_on_app_five_times(people_click_on_app_five_times);
                teamMetrics.setPeople_click_on_app_once_emails(people_click_on_app_once_emails.toString());
                teamMetrics.setPeople_click_on_app_three_times_emails(people_click_on_app_three_times_emails.toString());
                teamMetrics.setPeople_click_on_app_five_times_emails(people_click_on_app_five_times_emails.toString());
                hibernateQuery.saveOrUpdateObject(teamMetrics);
            }
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}
