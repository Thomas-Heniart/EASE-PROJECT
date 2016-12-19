/* Websites */

INSERT INTO ease.sso
SELECT * FROM test.sso;

INSERT INTO ease.websiteAttributes
SELECT null, locked, null, 1, hidden FROM test.websites;

SET @var = 0;

INSERT INTO ease.websites
SELECT website_id, website_url, website_name, folder, sso, noLogin, website_homepage, ratio, position, (@var := @var + 1) FROM test.websites;

INSERT INTO ease.websitesInformations
SELECT * FROM test.websitesInformations;

INSERT INTO ease.loginWithWebsites
SELECT null, website_id FROM test.websites WHERE haveLoginButton = 1;

INSERT INTO ease.websitesLogWithMap
SELECT null, website_id, 1 FROM test.websites WHERE FIND_IN_SET('7', haveLoginWith) > 0;

INSERT INTO ease.websitesLogWithMap
SELECT null, website_id, 2 FROM test.websites WHERE FIND_IN_SET('28', haveLoginWith) > 0;

/* Tags */

INSERT INTO ease.tags
SELECT * FROM test.tags;

/* date_dimension */

INSERT INTO ease.date_dimension
SELECT * FROM test.date_dimension;

/* Users */

INSERT INTO ease.userKeys
SELECT null, password, saltEase, saltPerso, keyUser FROM test.users;

INSERT INTO ease.options
SELECT null, bckgrndPic, 0 FROM test.users;

INSERT INTO ease.status
SELECT null, 1, 0, 0, 0, 0, 0, 0, tuto FROM test.users;

UPDATE ease.status SET first_connection=1, CGU=1, chrome_scrapping=1, click_on_app=1, move_apps=1, open_catalog=1, drag_and_drop=1 WHERE tuto_done = 1;

SET @var = 0;

INSERT INTO ease.users
SELECT user_id, firstName, lastName, email, (@var := @var + 1), @var, CURRENT_TIMESTAMP, @var FROM test.users;

/* savedSessions */

INSERT INTO ease.savedSessions
SELECT * FROM test.savedSessions;

/* profiles */

DELETE FROM test.apps WHERE profile_id IN (SELECT profile_id FROM test.profiles WHERE position IS NULL);
DELETE FROM test.profiles WHERE position IS NULL;
DELETE FROM test.GroupAndUserMap WHERE user_id NOT IN (SELECT user_id FROM test.profiles);
DELETE FROM test.usersEmails WHERE user_id NOT IN (SELECT user_id FROM test.profiles);
DELETE FROM test.users WHERE user_id NOT IN (SELECT user_id FROM test.profiles);

UPDATE test.profiles
SET columnIdx = 1, profileIdx = 0 WHERE columnIdx IS NULL;

SET @p_info_id = 0;

INSERT INTO ease.profileInfo
SELECT (@p_info_id := @p_info_id + 1), name, color FROM test.profiles;

SET @var = 0;


INSERT INTO ease.profiles
SELECT profile_id, user_id, columnIdx, profileIdx, NULL, (@var := @var + 1) FROM test.profiles;


/* groups */

INSERT INTO ease.infrastructures
SELECT null, name, 'aaa' FROM test.groups WHERE parent IS NULL;

INSERT INTO ease.groups
SELECT id, name, parent, 1 FROM test.groups;

UPDATE ease.groups
SET infrastructure_id = 2 WHERE id >= 4;


/* group profiles */

INSERT INTO ease.profilePermissions
SELECT null, group_id, perm FROM test.customProfiles;

INSERT INTO ease.profileInfo
SELECT null, name, color FROM test.customProfiles;

SET @var = 0;

INSERT INTO ease.groupProfiles
SELECT null, group_id, (@var := @var + 1), (@p_info_id := @p_info_id + 1), 0 FROM test.customProfiles;

UPDATE ease.profiles
SET group_profile_id = 1 WHERE id IN (SELECT profile_id FROM test.profiles WHERE custom = 1);

UPDATE ease.profiles
SET group_profile_id = 2 WHERE id IN (SELECT profile_id FROM test.profiles WHERE custom = 2);

UPDATE ease.profiles
SET group_profile_id = 3 WHERE id IN (SELECT profile_id FROM test.profiles WHERE custom = 3);

/* apps */




