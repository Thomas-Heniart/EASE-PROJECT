ALTER TABLE status ADD COLUMN click_on_eight_apps_in_a_day TINYINT(1) UNSIGNED NOT NULL DEFAULT 0;
UPDATE status SET click_on_eight_apps_in_a_day = 0;

ALTER TABLE status ADD COLUMN click_on_thirty_apps_in_a_week TINYINT(1) UNSIGNED NOT NULL DEFAULT 0;
UPDATE status SET click_on_thirty_apps_in_a_week = 0;