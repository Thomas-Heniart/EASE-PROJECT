ALTER TABLE status
  ADD COLUMN tip_team_user_settings_seen TINYINT(1) UNSIGNED NOT NULL DEFAULT 0;
ALTER TABLE status
  ADD COLUMN tip_team_channel_settings_seen TINYINT(1) UNSIGNED NOT NULL DEFAULT 0;