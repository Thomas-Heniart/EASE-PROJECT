DELIMITER //

CREATE PROCEDURE deleteUser(IN p_user_id INT(10) UNSIGNED)
BEGIN
  START TRANSACTION;
  DROP TEMPORARY TABLE IF EXISTS accountsIds;
  DROP TEMPORARY TABLE IF EXISTS profilesIds;
  CREATE TEMPORARY TABLE profilesIds AS
    SELECT profile_id FROM profiles WHERE user_id = p_user_id;
  CREATE TEMPORARY TABLE accountsIds AS
    SELECT account_id FROM apps JOIN profilesIds ON apps.profile_id = profilesIds.profile_id;
  DELETE FROM savedSessions where user_id = p_user_id;
  DELETE FROM usersEmails where user_id = p_user_id;
  DELETE logWithAccounts FROM logWithAccounts JOIN accountsIds ON logWithAccounts.account_id = accountsIds.account_id;
  DELETE ClassicAccountsInformations FROM ClassicAccountsInformations JOIN accountsIds ON ClassicAccountsInformations.account_id = accountsIds.account_id;
  DELETE classicAccounts FROM classicAccounts JOIN accountsIds ON classicAccounts.account_id = accountsIds.account_id;
  DELETE linkAccounts FROM linkAccounts JOIN accountsIds ON linkAccounts.account_id = accountsIds.account_id;
  DELETE apps FROM apps JOIN profilesIds ON apps.profile_id = profilesIds.profile_id;
  DELETE accounts FROM accounts JOIN accountsIds ON accounts.account_id = accountsIds.account_id;
  DELETE FROM profiles where user_id = p_user_id;
  DELETE FROM GroupAndUserMap where user_id = p_user_id;
  DELETE FROM users where user_id = p_user_id;
  COMMIT;
END //

DELIMITER ;

DELIMITER //