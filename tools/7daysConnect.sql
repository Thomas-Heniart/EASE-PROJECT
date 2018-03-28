ALTER TABLE options
  ADD COLUMN connection_lifetime TINYINT UNSIGNED NOT NULL DEFAULT 1;
ALTER TABLE status
  ADD COLUMN popup_choose_connection_lifetime_seen TINYINT UNSIGNED NOT NULL DEFAULT 0;