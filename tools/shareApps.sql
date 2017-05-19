DROP TABLE IF EXISTS pendingTeamUserVerifications, shareableApps, sharedApps, pendingJoinTeamRequests, pendingJoinChannelRequests, teamUserChannels, createTeamInvitations, pendingTeamInvitations, channelsAndTeamUsersMap, channelAndTeamUserMap, channels, teamUsers, teamUserPermissions, teamAndTeamUsersMap, teamAndTeamUserMap, teamAndWebsiteMap, teams;

CREATE TABLE teams (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  publicKey TEXT NOT NULL,
  PRIMARY KEY(id),
  UNIQUE (name)
);

CREATE TABLE teamUserPermissions (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  permissions BIT(8),
  PRIMARY KEY(id)
);

CREATE TABLE teamUsers (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT(10) UNSIGNED,
  team_id INT(10) UNSIGNED NOT NULL,
  firstName VARCHAR(100),
  lastName VARCHAR(100),
  username VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  permissions_id INT(10) UNSIGNED NOT NULL,
  departureDate DATETIME,
  arrivalDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  teamPrivateKey TEXT,
  verified TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY(id),
  FOREIGN KEY(user_id) REFERENCES users(id),
  FOREIGN KEY(team_id) REFERENCES teams(id),
  FOREIGN KEY(permissions_id) REFERENCES teamUserPermissions(id),
  UNIQUE (team_id, email, username)
);

CREATE TABLE channels (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  team_id INT(10) UNSIGNED NOT NULL,
  name VARCHAR(100),
  purpose VARCHAR(250),
  PRIMARY KEY(id),
  FOREIGN KEY(team_id) REFERENCES teams(id),
  UNIQUE (team_id, name)
);

CREATE TABLE channelAndTeamUserMap (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  channel_id INT(10) UNSIGNED NOT NULL,
  team_user_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(channel_id) REFERENCES channels(id),
  FOREIGN KEY(team_user_id) REFERENCES teamUsers(id)
);

CREATE TABLE createTeamInvitations (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(100) BINARY NOT NULL,
  code VARCHAR(255) NOT NULL,
  expiration_date DATETIME NOT NULL,
  PRIMARY KEY(id),
);

CREATE TABLE pendingTeamInvitations (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  teamUser_id INT(10) UNSIGNED NOT NULL,
  code VARCHAR(255) NOT NULL,
  team_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (teamUser_id) REFERENCES teams(id),
  FOREIGN KEY (team_id) REFERENCES teams(id),
  UNIQUE (code)
);

CREATE TABLE pendingTeamUserVerifications (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  teamUser_id INT(10) UNSIGNED NOT NULL,
  code VARCHAR(255) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (teamUser_id) REFERENCES teamUsers(id),
  UNIQUE (code)
);

CREATE TABLE pendingJoinTeamRequests (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  team_id INT(10) UNSIGNED NOT NULL,
  user_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(team_id) REFERENCES teams(id),
  FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE pendingJoinChannelRequests (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  channel_id INT(10) UNSIGNED NOT NULL,
  teamUser_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(channel_id) REFERENCES channels(id),
  FOREIGN KEY(teamUser_id) REFERENCES teamUsers(id)
);

/* App modifications */

DROP TABLE IF EXISTS profileAndAppMap, userAndProfileMap;

CREATE TABLE profileAndAppMap (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  profile_id INT(10) UNSIGNED NOT NULL,
  app_id INT(10) UNSIGNED NOT NULL,
  position TINYINT(3) UNSIGNED NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY (profile_id) REFERENCES profiles(id),
  FOREIGN KEY (app_id) REFERENCES apps(id)
);

INSERT INTO profileAndAppMap
    SELECT NULL, profile_id, id, position FROM apps;

ALTER TABLE apps DROP FOREIGN KEY apps_ibfk_1;
ALTER TABLE apps DROP COLUMN profile_id;
ALTER TABLE apps DROP COLUMN position;


/* Account modifications */

ALTER TABLE accounts ADD COLUMN lastUpdateDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE accounts ADD COLUMN reminderIntervalValue TINYINT;
ALTER TABLE accounts ADD COLUMN reminderIntervalType VARCHAR(25);

/* Apps sharing */
DROP TABLE IF EXISTS sharedApps, shareableApps, appAndSharedAppMap, channelAndAppMap;

ALTER TABLE apps
  ADD COLUMN shareable TINYINT(1) DEFAULT 1;

ALTER TABLE apps
  ADD COLUMN shared TINYINT(1) DEFAULT 0;

CREATE TABLE shareableApps (
  id INT(10) UNSIGNED NOT NULL,
  team_id INT(10) UNSIGNED NOT NULL,
  teamUser_owner_id INT(10) UNSIGNED NOT NULL,
  channel_id INT(10) UNSIGNED,
  description VARCHAR(255),
  PRIMARY KEY(id),
  FOREIGN KEY(team_id) REFERENCES teams(id),
  FOREIGN KEY(teamUser_owner_id) REFERENCES teamUsers(id),
  FOREIGN KEY(channel_id) REFERENCES channels(id),
  FOREIGN KEY(id) REFERENCES apps(id)
);

CREATE TABLE sharedApps (
  id INT(10) UNSIGNED NOT NULL,
  team_id INT(10) UNSIGNED NOT NULL,
  teamUser_tenant_id INT(10) UNSIGNED NOT NULL,
  shareable_app_id INT(10) UNSIGNED NOT NULL,
  channel_id INT(10) UNSIGNED,
  received TINYINT(1) NOT NULL,
  PRIMARY KEY(id),
  FOREIGN KEY(team_id) REFERENCES teams(id),
  FOREIGN KEY(teamUser_tenant_id) REFERENCES teamUsers(id),
  FOREIGN KEY(channel_id) REFERENCES channels(id),
  FOREIGN KEY(shareable_app_id) REFERENCES shareableApps(id),
  FOREIGN KEY(id) REFERENCES apps(id)
);

/* Teams and websites */

DROP TABLE IF EXISTS teamAndWebsiteMap;

CREATE TABLE teamAndWebsiteMap (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  team_id INT(10) UNSIGNED NOT NULL,
  website_id INT(10) UNSIGNED NOT NULL,
  FOREIGN KEY(team_id) REFERENCES teams(id),
  FOREIGN KEY(website_id) REFERENCES websites(id),
  PRIMARY KEY(id)
);

/* user public and private key */
ALTER TABLE userKeys ADD COLUMN publicKey TEXT;
ALTER TABLE userKeys ADD COLUMN privateKey TEXT;


/* account keys */
ALTER TABLE accounts ADD COLUMN publicKey TEXT;
ALTER TABLE accounts ADD COLUMN privateKey TEXT;
ALTER TABLE accounts ADD COLUMN mustBeReciphered TINYINT(1) DEFAULT 0;

/* team public key for shareableApp ciphering */
ALTER TABLE teams ADD COLUMN publicKey TEXT;
ALTER TABLE teamUsers ADD COLUMN teamPrivateKey TEXT;

/*
USE information_schema;
SELECT *
FROM
  KEY_COLUMN_USAGE
WHERE
  REFERENCED_TABLE_NAME = 'teams'
  AND REFERENCED_COLUMN_NAME = 'id';*/