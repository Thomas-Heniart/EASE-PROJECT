package com.Ease.Metrics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;

import java.util.Calendar;
import java.util.TimerTask;

public class MetricsSchedulerTask extends TimerTask {

    private TeamManager teamManager;

    public MetricsSchedulerTask(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        HibernateQuery trackingHibernateQuery = new HibernateQuery("tracking");
        Calendar calendar = Calendar.getInstance();
        try {
            System.out.println("Metric team start");
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
                    if (teamUser.isRegistered()) {
                        people_joined++;
                        people_joined_emails.append(teamUser.getEmail()).append(";");
                        if (!teamUser.getTeamCardReceivers().isEmpty()) {
                            people_with_cards++;
                            people_with_cards_emails.append(teamUser.getEmail()).append(";");
                        }
                    }
                }
                int cards = 0;
                StringBuilder cards_names = new StringBuilder();
                int cards_with_receiver = 0;
                StringBuilder cards_with_receiver_names = new StringBuilder();
                int cards_with_receiver_and_password_policy = 0;
                StringBuilder cards_with_receiver_and_password_policy_names = new StringBuilder();
                int single_cards = 0;
                StringBuilder single_cards_names = new StringBuilder();
                int enterprise_cards = 0;
                StringBuilder enterprise_cards_names = new StringBuilder();
                int link_cards = 0;
                StringBuilder link_cards_names = new StringBuilder();
                for (TeamCard teamCard : team.getTeamCardSet()) {
                    cards++;
                    cards_names.append(teamCard.getMetricName()).append(";");
                    if (!teamCard.getTeamCardReceiverMap().isEmpty()) {
                        cards_with_receiver++;
                        cards_with_receiver_names.append(teamCard.getMetricName()).append(";");
                        if (teamCard.getPassword_reminder_interval() > 0) {
                            cards_with_receiver_and_password_policy++;
                            cards_with_receiver_and_password_policy_names.append(teamCard.getMetricName()).append(";");
                        }
                    }
                    if (teamCard.isTeamSingleCard()) {
                        single_cards++;
                        single_cards_names.append(teamCard.getMetricName()).append(";");
                    } else if (teamCard.isTeamEnterpriseCard()) {
                        enterprise_cards++;
                        enterprise_cards_names.append(teamCard.getMetricName()).append(";");
                    } else {
                        link_cards++;
                        link_cards_names.append(teamCard.getMetricName()).append(";");
                    }
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
                int password_killed = 0;
                for (TeamUser teamUser : team.getTeamUsers().values()) {
                    if (!teamUser.isRegistered())
                        continue;
                    User user = teamUser.getUser();
                    trackingHibernateQuery.querySQLString("SELECT\n" +
                            "        year,\n" +
                            "        day_of_year,\n" +
                            "        COUNT(*) AS clicks\n" +
                            "      FROM (\n" +
                            "             SELECT\n" +
                            "               year,\n" +
                            "               day_of_year,\n" +
                            "               week_of_year,\n" +
                            "               id\n" +
                            "             FROM ease_tracking.EASE_EVENT\n" +
                            "             WHERE (name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser') AND user_id = :userId) AS t\n" +
                            "      WHERE year = :year AND week_of_year = :weekOfYear\n" +
                            "      GROUP BY year, day_of_year, week_of_year\n" +
                            "      ORDER BY year, day_of_year;");
                    trackingHibernateQuery.setParameter("userId", user.getDb_id());
                    trackingHibernateQuery.setParameter("weekOfYear", calendar.get(Calendar.WEEK_OF_YEAR));
                    trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
                    int weekClicks = trackingHibernateQuery.list().size();
                    if (weekClicks >= 1) {
                        people_click_on_app_once++;
                        people_click_on_app_once_emails.append(teamUser.getEmail()).append(";");
                    }
                    if (weekClicks >= 3) {
                        people_click_on_app_three_times++;
                        people_click_on_app_three_times_emails.append(teamUser.getEmail()).append(";");
                    }
                    if (weekClicks >= 5) {
                        people_click_on_app_five_times++;
                        people_click_on_app_five_times_emails.append(teamUser.getEmail()).append(";");
                    }
                    for (App app : teamUser.getUser().getApps()) {
                        if (app.getTeamCardReceiver() == null && app.getAccount() != null) {
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
                if (cards_names.length() > 0)
                    cards_names.deleteCharAt(cards_names.length() - 1);
                if (cards_with_receiver_names.length() > 0)
                    cards_with_receiver_names.deleteCharAt(cards_with_receiver_names.length() - 1);
                if (cards_with_receiver_and_password_policy_names.length() > 0)
                    cards_with_receiver_and_password_policy_names.deleteCharAt(cards_with_receiver_and_password_policy_names.length() - 1);
                if (single_cards_names.length() > 0)
                    single_cards_names.deleteCharAt(single_cards_names.length() - 1);
                if (enterprise_cards_names.length() > 0)
                    enterprise_cards_names.deleteCharAt(enterprise_cards_names.length() - 1);
                if (link_cards_names.length() > 0)
                    link_cards_names.deleteCharAt(link_cards_names.length() - 1);
                teamMetrics.setPeople_invited(people_invited);
                teamMetrics.setPeople_invited_emails(people_invited_emails.toString());
                teamMetrics.setPeople_joined(people_joined);
                teamMetrics.setPeople_joined_emails(people_joined_emails.toString());
                teamMetrics.setPeople_with_cards(people_with_cards);
                teamMetrics.setPeople_with_cards_emails(people_with_cards_emails.toString());
                teamMetrics.setPeople_with_personnal_apps(people_with_personnal_apps);
                teamMetrics.setPeople_with_personnal_apps_emails(people_with_personnal_apps_emails.toString());
                teamMetrics.setCards(cards);
                teamMetrics.setCards_names(cards_names.toString());
                teamMetrics.setCards_with_receiver(cards_with_receiver);
                teamMetrics.setCards_with_receiver_names(cards_with_receiver_names.toString());
                teamMetrics.setCards_with_receiver_and_password_policy(cards_with_receiver_and_password_policy);
                teamMetrics.setCards_with_receiver_and_password_policy_names(cards_with_receiver_and_password_policy_names.toString());
                teamMetrics.setSingle_cards(single_cards);
                teamMetrics.setSingle_cards_names(single_cards_names.toString());
                teamMetrics.setEnterprise_cards(enterprise_cards);
                teamMetrics.setEnterprise_cards_names(enterprise_cards_names.toString());
                teamMetrics.setLink_cards(link_cards);
                teamMetrics.setLink_cards_names(link_cards_names.toString());
                teamMetrics.setRoom_number(room_number);
                teamMetrics.setRoom_names(room_names.toString());
                teamMetrics.setPeople_click_on_app_once(people_click_on_app_once);
                teamMetrics.setPeople_click_on_app_three_times(people_click_on_app_three_times);
                teamMetrics.setPeople_click_on_app_five_times(people_click_on_app_five_times);
                teamMetrics.setPeople_click_on_app_once_emails(people_click_on_app_once_emails.toString());
                teamMetrics.setPeople_click_on_app_three_times_emails(people_click_on_app_three_times_emails.toString());
                teamMetrics.setPeople_click_on_app_five_times_emails(people_click_on_app_five_times_emails.toString());
                teamMetrics.setPasswordKilled(password_killed);
                hibernateQuery.saveOrUpdateObject(teamMetrics);
            }
            System.out.println("Metric team stop");
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}
