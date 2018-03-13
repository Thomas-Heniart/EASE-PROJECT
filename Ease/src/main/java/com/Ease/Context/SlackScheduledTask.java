package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.ClickOnApp;
import com.Ease.NewDashboard.App;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Slack.SlackAPIWrapper;

import java.util.*;
import java.util.stream.Collectors;

public class SlackScheduledTask extends TimerTask {
    @Override
    public void run() {
        if (!Variables.ENVIRONNEMENT.equals("Prod"))
            return;
        System.out.println("Start Slack scheduled task...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            this.notificationForTeamsConnectedToday(hibernateQuery);
            this.notificationClickOnEightAppsInADay(hibernateQuery);
            this.notificationClickOnThirtyAppsInAWeek(hibernateQuery);
            this.notificationInactiveUsersOfLast5Days(hibernateQuery);
            this.notificationForTeamsActivityOfLastWeek(hibernateQuery);
            this.notificationTeamsAfterOneWeekAndLessThanEightApps(hibernateQuery);
            this.notificationsTeamsInvitationsNotSentAfterOneWeek(hibernateQuery);
            this.notificationUsersLostPasswordsThreeDaysAgo(hibernateQuery);
            hibernateQuery.commit();
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
        System.out.println("End Slack scheduled task...");
    }

    private List<Team> getTeamsConnectedToday(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        hibernateQuery.queryString("SELECT c.user_id FROM ConnectionMetric c WHERE c.year = :year AND c.day_of_year = :day_of_year");
        hibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        hibernateQuery.setParameter("day_of_year", calendar.get(Calendar.DAY_OF_YEAR));
        List<Integer> userIds = hibernateQuery.list();
        if (userIds.isEmpty())
            return new LinkedList<>();
        hibernateQuery.queryString("SELECT DISTINCT t.team FROM TeamUser t WHERE t.user IS NOT NULL AND t.team.active IS true AND t.user.db_id IN (:userIds)");
        hibernateQuery.setParameter("userIds", userIds);
        return hibernateQuery.list();
    }

    private List<Team> getTeamLessThanEightAppsOneWeekAfterSubscription(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS true AND DATE(t.subscription_date) = (:date)");
        hibernateQuery.setDate("date", calendar);
        List<Team> teams = hibernateQuery.list();
        return teams.stream().filter(team -> team.getTeamCardSet().size() < 8).collect(Collectors.toList());
    }

    private List<Team> getTeamWithoutInvitationOneWeekAfterSubscription(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS true AND DATE(t.subscription_date) = (:date)");
        hibernateQuery.setDate("date", calendar);
        List<Team> teams = hibernateQuery.list();
        return teams.stream().filter(team -> !team.isInvitations_sent()).collect(Collectors.toList());
    }

    private List<Team> getActiveTeamsOfLastWeek(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        int eYear = calendar.get(Calendar.YEAR);
        int eDay = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        int sYear = calendar.get(Calendar.YEAR);
        int sDay = calendar.get(Calendar.DAY_OF_YEAR);
        if (sYear == eYear) {
            hibernateQuery.queryString("SELECT c.user_id FROM ConnectionMetric c WHERE c.year = :year AND c.day_of_year BETWEEN :sDay AND :eDay");
            hibernateQuery.setParameter("year", eYear);
            hibernateQuery.setParameter("sDay", sDay);
            hibernateQuery.setParameter("eDay", eDay);
        } else {
            while (calendar.get(Calendar.YEAR) < eYear)
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            int jDay = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            int iDay = calendar.get(Calendar.DAY_OF_YEAR);
            hibernateQuery.queryString("SELECT c.user_id FROM ConnectionMetric c WHERE (c.year = :sYear AND c.day_of_year BETWEEN :sDay AND :iDay) OR (c.year = :eYear AND c.day_of_year BETWEEN :jDay AND :eDay)");
            hibernateQuery.setParameter("sYear", sYear);
            hibernateQuery.setParameter("sDay", sDay);
            hibernateQuery.setParameter("iDay", iDay);
            hibernateQuery.setParameter("jDay", jDay);
            hibernateQuery.setParameter("eYear", eYear);
            hibernateQuery.setParameter("eDay", eDay);
        }
        List<Integer> userIds = hibernateQuery.list();
        if (userIds.isEmpty())
            return new LinkedList<>();
        hibernateQuery.queryString("SELECT DISTINCT t.team FROM TeamUser t WHERE t.user IS NOT NULL AND t.user.db_id IN (:userIds)");
        hibernateQuery.setParameter("userIds", userIds);
        return hibernateQuery.list();
    }

    private Set<User> getUsersWhoClickOnEightAppsInADay(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        hibernateQuery.queryString("SELECT c.app_id, c.day_" + this.convertDayToString(calendar.get(Calendar.DAY_OF_WEEK)) + " FROM ClickOnApp c WHERE c.year = :year AND c.week_of_year = :week_of_year");
        hibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        hibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
        List<Object[]> appIdAndDayCount = hibernateQuery.list();
        if (appIdAndDayCount.isEmpty())
            return new HashSet<>();
        Map<Integer, Integer> appIdAndDayCountMap = new HashMap<>();
        appIdAndDayCount.forEach(integers -> appIdAndDayCountMap.put((Integer) integers[0], (Integer) integers[1]));
        hibernateQuery.queryString("SELECT DISTINCT a FROM App a WHERE a.profile IS NOT NULL AND a.profile.user IS NOT NULL AND a.profile.user.userStatus.click_on_eight_apps_in_a_day is false AND a.db_id IN (:appIds)");
        hibernateQuery.setParameter("appIds", appIdAndDayCountMap.keySet());
        List<App> apps = hibernateQuery.list();
        Map<User, Integer> userAndCountMap = new HashMap<>();
        for (App app : apps) {
            User user = app.getProfile().getUser();
            Integer count = userAndCountMap.get(user);
            if (count == null)
                count = 0;
            count += appIdAndDayCountMap.get(app.getDb_id());
            userAndCountMap.put(user, count);
        }
        Set<User> users = new HashSet<>();
        userAndCountMap.forEach((user, integer) -> {
            if (integer >= 8)
                users.add(user);
        });
        return users;
    }

    private Set<User> getUsersWhoClickOnThirtyAppsThisWeek(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        hibernateQuery.queryString("SELECT c.app_id, c FROM ClickOnApp c WHERE c.year = :year AND c.week_of_year = :week_of_year");
        hibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        hibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
        List<Object[]> appIdAndDayCount = hibernateQuery.list();
        if (appIdAndDayCount.isEmpty())
            return new HashSet<>();
        Map<Integer, Integer> appIdAndDayCountMap = new HashMap<>();
        appIdAndDayCount.forEach(integers -> appIdAndDayCountMap.put((Integer) integers[0], ((ClickOnApp) integers[1]).getTotalClicks()));
        hibernateQuery.queryString("SELECT DISTINCT a FROM App a WHERE a.profile IS NOT NULL AND a.profile.user IS NOT NULL AND a.profile.user.userStatus.click_on_thirty_apps_in_a_week is false AND a.db_id IN (:appIds)");
        hibernateQuery.setParameter("appIds", appIdAndDayCountMap.keySet());
        List<App> apps = hibernateQuery.list();
        Map<User, Integer> userAndCountMap = new HashMap<>();
        for (App app : apps) {
            User user = app.getProfile().getUser();
            Integer count = userAndCountMap.get(user);
            if (count == null)
                count = 0;
            count += appIdAndDayCountMap.get(app.getDb_id());
            userAndCountMap.put(user, count);
        }
        Set<User> users = new HashSet<>();
        userAndCountMap.forEach((user, integer) -> {
            if (integer >= 30)
                users.add(user);
        });
        return users;
    }

    private List<User> getInactiveUsersOfLast5Days(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        hibernateQuery.queryString("SELECT c.user_id FROM ConnectionMetric c WHERE c.year = :year AND c.day_of_year = :day_of_year AND c.connected IS true AND c.user_id IN " +
                "(SELECT c1.user_id FROM ConnectionMetric c1 WHERE c1.year = :other_year AND c1.day_of_year = :other_day_of_year AND c.connected IS true)");
        hibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        hibernateQuery.setParameter("day_of_year", calendar.get(Calendar.DAY_OF_YEAR));
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        hibernateQuery.setParameter("other_year", calendar.get(Calendar.YEAR));
        hibernateQuery.setParameter("other_day_of_year", calendar.get(Calendar.DAY_OF_YEAR));
        List<Integer> userIds = hibernateQuery.list();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        int sYear = calendar.get(Calendar.YEAR);
        int sDay = calendar.get(Calendar.DAY_OF_YEAR);
        calendar.add(Calendar.DAY_OF_YEAR, 5);
        int eYear = calendar.get(Calendar.YEAR);
        int eDay = calendar.get(Calendar.DAY_OF_YEAR);
        if (eDay < sDay) {
            while (calendar.get(Calendar.YEAR) > sYear)
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            hibernateQuery.queryString("SELECT c.user_id FROM ConnectionMetric c WHERE c.connected IS true AND ((c.year = :sYear AND c.day_of_year BETWEEN :sDay AND :iDay) OR (c.year = :eYear AND c.day_of_year BETWWEN :jDay AND :eDay))");
            hibernateQuery.setParameter("sYear", sYear);
            hibernateQuery.setParameter("sDay", sDay);
            hibernateQuery.setParameter("iDay", calendar.get(Calendar.DAY_OF_YEAR));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            hibernateQuery.setParameter("jDay", calendar.get(Calendar.DAY_OF_YEAR));
            hibernateQuery.setParameter("eYear", eYear);
            hibernateQuery.setParameter("eDay", eDay);
        } else {
            hibernateQuery.queryString("SELECT c.user_id FROM ConnectionMetric c WHERE c.year = :year AND c.connected IS true AND c.day_of_year BETWEEN :sDay AND :eDay");
            hibernateQuery.setParameter("year", sYear);
            hibernateQuery.setParameter("sDay", sDay);
            hibernateQuery.setParameter("eDay", eDay);
        }
        List<Integer> userIds1 = hibernateQuery.list();
        userIds.removeAll(userIds1);
        if (userIds.isEmpty())
            return new LinkedList<>();
        hibernateQuery.queryString("SELECT u FROM User u WHERE u.db_id IN (:userIds)");
        hibernateQuery.setParameter("userIds", userIds);
        return hibernateQuery.list();
    }

    private List<User> getUsersWhoLostPasswordsThreeDaysAgo(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        hibernateQuery.queryString("SELECT u FROM User u WHERE u.db_id IN (SELECT p.user_id FROM PasswordLost p WHERE DATE(p.dateOfRequest) = (:date))");
        hibernateQuery.setDate("date", calendar);
        return hibernateQuery.list();
    }

    private void notificationForTeamsConnectedToday(HibernateQuery hibernateQuery) throws Exception {
        List<Team> teams = this.getTeamsConnectedToday(hibernateQuery);
        if (teams.isEmpty())
            return;
        StringBuilder stringBuilder = new StringBuilder("*✅Active companies today (")
                .append(teams.size())
                .append(")*\n");
        teams.forEach(team -> stringBuilder.append("- ").append(team.getName()).append("\n"));
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private void notificationForTeamsActivityOfLastWeek(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY)
            return;
        List<Team> activeTeams = this.getActiveTeamsOfLastWeek(hibernateQuery);
        StringBuilder stringBuilder;
        if (!activeTeams.isEmpty()) {
             stringBuilder = new StringBuilder("*✅Active companies this week (")
                    .append(activeTeams.size())
                    .append(")*\nCompanies connected this week:\n");
            printTeams(stringBuilder, activeTeams);
            stringBuilder.append("\n=======\n=======\n=======");
            SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
        }
        if (!activeTeams.isEmpty()) {
            hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS true AND t NOT IN (:teams)");
            hibernateQuery.setParameter("teams", activeTeams);
        } else
            hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS true");
        List<Team> teams = hibernateQuery.list();
        stringBuilder = new StringBuilder("*✅Companies not using (")
                .append(teams.size())
                .append(")*\nThese companies did not use Ease.space last week:\n");
        printTeams(stringBuilder, teams);
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private void notificationTeamsAfterOneWeekAndLessThanEightApps(HibernateQuery hibernateQuery) throws Exception {
        List<Team> teams = this.getTeamLessThanEightAppsOneWeekAfterSubscription(hibernateQuery);
        if (teams.isEmpty())
            return;
        StringBuilder stringBuilder = new StringBuilder("*✅Teams not activated (")
                .append(teams.size())
                .append(")*\nThese teams has less than 8 apps since their registrations one week ago:\n");
        printTeams(stringBuilder, teams);
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private void notificationsTeamsInvitationsNotSentAfterOneWeek(HibernateQuery hibernateQuery) throws Exception {
        List<Team> teams = this.getTeamWithoutInvitationOneWeekAfterSubscription(hibernateQuery);
        if (teams.isEmpty())
            return;
        StringBuilder stringBuilder = new StringBuilder("*✅Teams not invited (")
                .append(teams.size())
                .append(")*\nThese teams have not invite their members:\n");
        printTeams(stringBuilder, teams);
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private void notificationClickOnEightAppsInADay(HibernateQuery hibernateQuery) throws Exception {
        Set<User> users = this.getUsersWhoClickOnEightAppsInADay(hibernateQuery);
        if (users.isEmpty())
            return;
        StringBuilder stringBuilder = new StringBuilder("*✅New users")
                .append(users.size())
                .append(")*\nThese people made 8 click on apps this day for the first time:\n");
        users.forEach(user -> {
            user.getUserStatus().setClick_on_eight_apps_in_a_day(true);
            hibernateQuery.saveOrUpdateObject(user.getUserStatus());
            printUser(stringBuilder, user);
        });
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private void notificationClickOnThirtyAppsInAWeek(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
            return;
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Set<User> users = this.getUsersWhoClickOnThirtyAppsThisWeek(hibernateQuery);
        if (users.isEmpty())
            return;
        StringBuilder stringBuilder = new StringBuilder("*✅New weekly users (")
                .append(users.size())
                .append(")*\nThese people made 30 click on apps this week for the first time:\n");
        users.forEach(user -> {
            user.getUserStatus().setClick_on_thirty_apps_in_a_week(true);
            hibernateQuery.saveOrUpdateObject(user.getUserStatus());
            printUser(stringBuilder, user);
        });
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private void notificationInactiveUsersOfLast5Days(HibernateQuery hibernateQuery) throws Exception {
        List<User> users = this.getInactiveUsersOfLast5Days(hibernateQuery);
        if (users.isEmpty())
            return;
        StringBuilder stringBuilder = new StringBuilder("*✅ALERT, some users are leaving us !*\nThese people don't use Ease since last 5 days\n");
        users.forEach(user -> printUser(stringBuilder, user));
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private void notificationUsersLostPasswordsThreeDaysAgo(HibernateQuery hibernateQuery) throws Exception {
        List<User> users = this.getUsersWhoLostPasswordsThreeDaysAgo(hibernateQuery);
        if (users.isEmpty())
            return;
        StringBuilder stringBuilder = new StringBuilder("*✅Passwords lost 3 days ago*\nThese people lost their passwords 3 days ago and never came back:\n");
        users.forEach(user -> printUser(stringBuilder, user));
        stringBuilder.append("\n=======\n=======\n=======");
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }

    private String convertDayToString(int day) throws HttpServletException {
        switch (day) {
            case 1:
                return "seven";
            case 2:
                return "one";
            case 3:
                return "two";
            case 4:
                return "three";
            case 5:
                return "four";
            case 6:
                return "five";
            case 7:
                return "six";
            default:
                throw new HttpServletException(HttpStatus.BadRequest, "No such day");
        }
    }

    private void printUser(StringBuilder stringBuilder, User user) {
        stringBuilder.append("- ").append(user.getEmail());
        user.getTeamUsers().stream().filter(teamUser -> teamUser.getTeam().isActive()).forEach(teamUser -> stringBuilder
                .append(" (")
                .append(teamUser.getEmail())
                .append(", ")
                .append(teamUser.getTeamUserRole().getRoleName())
                .append(", ")
                .append(teamUser.getTeam().getName())
                .append(")"));
        stringBuilder.append("\n");
    }

    private void printTeams(StringBuilder stringBuilder, List<Team> teams) {
        teams.forEach(team -> {
            TeamUser teamUser_owner = team.getTeamUserOwner();
            User owner = teamUser_owner.getUser();
            stringBuilder
                    .append(teamUser_owner.getEmail())
                    .append(", ")
                    .append(team.getName())
                    .append(", ")
                    .append(owner.getPersonalInformation().getPhone_number())
                    .append("\n");
        });
    }
}
