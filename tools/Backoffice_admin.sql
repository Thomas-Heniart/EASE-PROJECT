USE ease;

CREATE TABLE metricClickOnApp (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  app_id INT(10) UNSIGNED NOT NULL,
  year INT(10) UNSIGNED NOT NULL,
  week_of_year TINYINT UNSIGNED NOT NULL,
  day_0 SMALLINT UNSIGNED NOT NULL,
  day_1 SMALLINT UNSIGNED NOT NULL,
  day_2 SMALLINT UNSIGNED NOT NULL,
  day_3 SMALLINT UNSIGNED NOT NULL,
  day_4 SMALLINT UNSIGNED NOT NULL,
  day_5 SMALLINT UNSIGNED NOT NULL,
  day_6 SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE metricTeam (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  team_id INT(10) UNSIGNED NOT NULL,
  year INT(10) UNSIGNED NOT NULL,
  week_of_year TINYINT UNSIGNED NOT NULL,
  people_invited MEDIUMINT UNSIGNED NOT NULL,
  people_invited_emails TEXT,
  people_joined MEDIUMINT UNSIGNED NOT NULL,
  people_joined_emails TEXT,
  people_with_cards MEDIUMINT UNSIGNED NOT NULL,
  people_with_cards_emails TEXT,
  people_click_on_app_once MEDIUMINT UNSIGNED NOT NULL,
  people_click_on_app_once_emails TEXT,
  people_click_on_app_three_times MEDIUMINT UNSIGNED NOT NULL,
  people_click_on_app_three_times_emails TEXT,
  people_click_on_app_five_times MEDIUMINT UNSIGNED NOT NULL,
  people_click_on_app_five_times_emails TEXT,
  room_number TINYINT NOT NULL,
  room_names TEXT,
  cards MEDIUMINT UNSIGNED NOT NULL,
  cards_with_receiver MEDIUMINT UNSIGNED NOT NULL,
  cards_with_receiver_and_password_policy MEDIUMINT UNSIGNED NOT NULL,
  single_cards MEDIUMINT UNSIGNED NOT NULL,
  enterprise_cards MEDIUMINT UNSIGNED NOT NULL,
  link_cards MEDIUMINT UNSIGNED NOT NULL,
  PRIMARY KEY (id)
);