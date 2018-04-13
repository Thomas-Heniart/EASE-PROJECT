ALTER TABLE accounts
  ADD COLUMN lastPasswordReminderDate DATETIME;

ALTER TABLE teamSingleCards
  ADD COLUMN password_score TINYINT;
ALTER TABLE teamSingleSoftwareCards
  ADD COLUMN password_score TINYINT;
ALTER TABLE teamEnterpriseCardReceivers
  ADD COLUMN password_score TINYINT;