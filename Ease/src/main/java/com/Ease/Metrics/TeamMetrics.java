package com.Ease.Metrics;

import com.Ease.Hibernate.HibernateQuery;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "metricTeam")
public class TeamMetrics {
    public static TeamMetrics getMetrics(Integer team_id, HibernateQuery hibernateQuery) {
        Calendar calendar = Calendar.getInstance();
        return getMetrics(team_id, calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), hibernateQuery);
    }

    public static TeamMetrics getMetrics(Integer team_id, Integer year, Integer week_of_year, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT m FROM TeamMetrics m WHERE m.team_id = :team_id AND m.year = :year AND m.week_of_year = :week");
        hibernateQuery.setParameter("team_id", team_id);
        hibernateQuery.setParameter("year", year);
        hibernateQuery.setParameter("week", week_of_year);
        TeamMetrics teamMetrics = (TeamMetrics) hibernateQuery.getSingleResult();
        if (teamMetrics == null) {
            teamMetrics = new TeamMetrics(team_id, year, week_of_year);
            hibernateQuery.saveOrUpdateObject(teamMetrics);
        }
        return teamMetrics;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long db_id;

    @Column(name = "team_id")
    private Integer team_id;

    @Column(name = "year")
    private int year;

    @Column(name = "week_of_year")
    private int week_of_year;

    @Column(name = "people_invited")
    private int people_invited = 0;

    @Column(name = "people_invited_emails")
    private String people_invited_emails = "";

    @Column(name = "people_joined")
    private int people_joined = 0;

    @Column(name = "people_joined_emails")
    private String people_joined_emails = "";

    @Column(name = "people_with_cards")
    private int people_with_cards = 0;

    @Column(name = "people_with_cards_emails")
    private String people_with_cards_emails = "";

    @Column(name = "people_with_personnal_apps")
    private int people_with_personnal_apps = 0;

    @Column(name = "people_with_personnal_apps_emails")
    private String people_with_personnal_apps_emails;

    @Column(name = "people_click_on_app_once")
    private int people_click_on_app_once = 0;

    @Column(name = "people_click_on_app_once_emails")
    private String people_click_on_app_once_emails = "";

    @Column(name = "people_click_on_app_three_times")
    private int people_click_on_app_three_times = 0;

    @Column(name = "people_click_on_app_three_times_emails")
    private String people_click_on_app_three_times_emails = "";

    @Column(name = "people_click_on_app_five_times")
    private int people_click_on_app_five_times = 0;

    @Column(name = "people_click_on_app_five_times_emails")
    private String people_click_on_app_five_times_emails = "";

    @Column(name = "room_number")
    private int room_number = 0;

    @Column(name = "room_names")
    private String room_names = "";

    @Column(name = "cards")
    private int cards = 0;

    @Column(name = "cards_with_receiver")
    private int cards_with_receiver = 0;

    @Column(name = "cards_with_receiver_and_password_policy")
    private int cards_with_receiver_and_password_policy = 0;

    @Column(name = "single_cards")
    private int single_cards;

    @Column(name = "enterprise_cards")
    private int enterprise_cards;

    @Column(name = "link_cards")
    private int link_cards;

    public TeamMetrics() {

    }

    private TeamMetrics(Integer team_id, int year, int week_of_year) {
        this.team_id = team_id;
        this.year = year;
        this.week_of_year = week_of_year;
    }

    public long getDb_id() {
        return db_id;
    }

    public void setDb_id(long db_id) {
        this.db_id = db_id;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek_of_year() {
        return week_of_year;
    }

    public void setWeek_of_year(int week_of_year) {
        this.week_of_year = week_of_year;
    }

    public int getPeople_invited() {
        return people_invited;
    }

    public void setPeople_invited(int people_invited) {
        this.people_invited = people_invited;
    }

    public String getPeople_invited_emails() {
        return people_invited_emails;
    }

    public void setPeople_invited_emails(String people_invited_emails) {
        this.people_invited_emails = people_invited_emails;
    }

    public int getPeople_joined() {
        return people_joined;
    }

    public void setPeople_joined(int people_joined) {
        this.people_joined = people_joined;
    }

    public String getPeople_joined_emails() {
        return people_joined_emails;
    }

    public void setPeople_joined_emails(String people_joined_emails) {
        this.people_joined_emails = people_joined_emails;
    }

    public int getPeople_with_cards() {
        return people_with_cards;
    }

    public void setPeople_with_cards(int people_with_cards) {
        this.people_with_cards = people_with_cards;
    }

    public int getPeople_with_personnal_apps() {
        return people_with_personnal_apps;
    }

    public void setPeople_with_personnal_apps(int people_with_personnal_apps) {
        this.people_with_personnal_apps = people_with_personnal_apps;
    }

    public String getPeople_with_personnal_apps_emails() {
        return people_with_personnal_apps_emails;
    }

    public void setPeople_with_personnal_apps_emails(String people_with_personnal_apps_emails) {
        this.people_with_personnal_apps_emails = people_with_personnal_apps_emails;
    }

    public String getPeople_with_cards_emails() {
        return people_with_cards_emails;
    }

    public void setPeople_with_cards_emails(String people_with_cards_emails) {
        this.people_with_cards_emails = people_with_cards_emails;
    }

    public int getPeople_click_on_app_once() {
        return people_click_on_app_once;
    }

    public void setPeople_click_on_app_once(int people_click_on_app_once) {
        this.people_click_on_app_once = people_click_on_app_once;
    }

    public String getPeople_click_on_app_once_emails() {
        return people_click_on_app_once_emails;
    }

    public void setPeople_click_on_app_once_emails(String people_click_on_app_once_emails) {
        this.people_click_on_app_once_emails = people_click_on_app_once_emails;
    }

    public int getPeople_click_on_app_three_times() {
        return people_click_on_app_three_times;
    }

    public void setPeople_click_on_app_three_times(int people_click_on_app_three_times) {
        this.people_click_on_app_three_times = people_click_on_app_three_times;
    }

    public String getPeople_click_on_app_three_times_emails() {
        return people_click_on_app_three_times_emails;
    }

    public void setPeople_click_on_app_three_times_emails(String people_click_on_app_three_times_emails) {
        this.people_click_on_app_three_times_emails = people_click_on_app_three_times_emails;
    }

    public int getPeople_click_on_app_five_times() {
        return people_click_on_app_five_times;
    }

    public void setPeople_click_on_app_five_times(int people_click_on_app_five_times) {
        this.people_click_on_app_five_times = people_click_on_app_five_times;
    }

    public String getPeople_click_on_app_five_times_emails() {
        return people_click_on_app_five_times_emails;
    }

    public void setPeople_click_on_app_five_times_emails(String people_click_on_app_five_times_emails) {
        this.people_click_on_app_five_times_emails = people_click_on_app_five_times_emails;
    }

    public int getRoom_number() {
        return room_number;
    }

    public void setRoom_number(int room_number) {
        this.room_number = room_number;
    }

    public String getRoom_names() {
        return room_names;
    }

    public void setRoom_names(String room_names) {
        this.room_names = room_names;
    }

    public int getCards() {
        return cards;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }

    public int getCards_with_receiver() {
        return cards_with_receiver;
    }

    public void setCards_with_receiver(int cards_with_receiver) {
        this.cards_with_receiver = cards_with_receiver;
    }

    public int getCards_with_receiver_and_password_policy() {
        return cards_with_receiver_and_password_policy;
    }

    public void setCards_with_receiver_and_password_policy(int cards_with_receiver_and_password_policy) {
        this.cards_with_receiver_and_password_policy = cards_with_receiver_and_password_policy;
    }

    public int getSingle_cards() {
        return single_cards;
    }

    public void setSingle_cards(int single_cards) {
        this.single_cards = single_cards;
    }

    public int getEnterprise_cards() {
        return enterprise_cards;
    }

    public void setEnterprise_cards(int enterprise_cards) {
        this.enterprise_cards = enterprise_cards;
    }

    public int getLink_cards() {
        return link_cards;
    }

    public void setLink_cards(int link_cards) {
        this.link_cards = link_cards;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("people_invited", this.getPeople_invited());
        res.put("people_invited_emails", this.getPeople_invited_emails());
        res.put("people_joined", this.getPeople_joined());
        res.put("people_joined_emails", this.getPeople_joined_emails());
        res.put("people_with_cards", this.getPeople_with_cards());
        res.put("people_with_cards_emails", this.getPeople_with_cards_emails());
        res.put("people_with_personnal_apps", this.getPeople_with_personnal_apps());
        res.put("people_with_personnal_apps_emails", this.getPeople_with_personnal_apps_emails());
        res.put("people_click_on_app_once", this.getPeople_click_on_app_once());
        res.put("people_click_on_app_once_emails", this.getPeople_click_on_app_once_emails());
        res.put("people_click_on_app_three_times", this.getPeople_click_on_app_three_times());
        res.put("people_click_on_app_three_times_emails", this.getPeople_click_on_app_three_times_emails());
        res.put("people_click_on_app_five_times", this.getPeople_click_on_app_five_times());
        res.put("people_click_on_app_five_times_emails", this.getPeople_click_on_app_five_times_emails());
        res.put("rooms", this.getRoom_number());
        res.put("room_names", this.getRoom_names());
        res.put("cards", this.getCards());
        res.put("cards_with_receiver", this.getCards_with_receiver());
        res.put("cards_with_receiver_and_password_policy", this.getCards_with_receiver_and_password_policy());
        res.put("single_cards", this.getSingle_cards());
        res.put("enterprise_cards", this.getEnterprise_cards());
        res.put("link_cards", this.getLink_cards());
        return res;
    }
}