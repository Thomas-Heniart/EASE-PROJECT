ALTER TABLE accounts
  ADD COLUMN lastPasswordReminderDate DATETIME;

ALTER TABLE teamSingleCards
  ADD COLUMN password_score TINYINT;
ALTER TABLE teamSingleSoftwareCards
  ADD COLUMN password_score TINYINT;
ALTER TABLE teamEnterpriseCardReceivers
  ADD COLUMN password_score TINYINT;

ALTER TABLE teams
  ADD COLUMN passwordScoreInitialize TINYINT(1) NOT NULL DEFAULT 1;
UPDATE teams
SET passwordScoreInitialize = 1
WHERE active = 0;
UPDATE teams
SET passwordScoreInitialize = 0
WHERE active = 1;