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

ALTER TABLE teamSingleCards
  ADD COLUMN lastPasswordScoreAlertDate DATETIME;
ALTER TABLE teamSingleSoftwareCards
  ADD COLUMN lastPasswordScoreAlertDate DATETIME;
ALTER TABLE teamEnterpriseCardReceivers
  ADD COLUMN lastPasswordScoreAlertDate DATETIME;
ALTER TABLE teams
  ADD COLUMN lastPasswordScoreAlertDate DATETIME;
ALTER TABLE teamEnterpriseCards
  ADD COLUMN lastPasswordScoreAlertDate DATETIME;
ALTER TABLE teamEnterpriseSoftwareCards
  ADD COLUMN lastPasswordScoreAlertDate DATETIME;

ALTER TABLE accounts
  ADD COLUMN strongerPasswordAsked TINYINT(1) NOT NULL DEFAULT 0;