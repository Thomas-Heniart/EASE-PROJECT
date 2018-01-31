DROP TABLE IF EXISTS tagsAndSitesMap, tagsAndGroupsMap, tags;

CREATE TABLE categories (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  position TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE websites ADD COLUMN `category_id` INT(10) UNSIGNED;
ALTER TABLE websites ADD FOREIGN KEY (category_id) REFERENCES categories(id);

INSERT INTO categories values (NULL, 'Analytics', 10);
INSERT INTO categories values (NULL, 'Administrative', 20);
INSERT INTO categories values (NULL, 'Communication', 30);
INSERT INTO categories values (NULL, 'Developer Tools', 40);
INSERT INTO categories VALUES (NULL, 'Design', 50);
INSERT INTO categories VALUES (NULL, 'E-Learning', 60);
INSERT INTO categories values (NULL, 'File Management', 70);
INSERT INTO categories values (NULL, 'Food', 80);
INSERT INTO categories values (NULL, 'Health & Medical', 90);
INSERT INTO categories values (NULL, 'HR', 100);
INSERT INTO categories values (NULL, 'News', 110);
INSERT INTO categories values (NULL, 'Payment & Accounting', 120);
INSERT INTO categories values (NULL, 'Productivity', 130);
INSERT INTO categories values (NULL, 'Project Management', 140);
INSERT INTO categories values (NULL, 'Sales & Marketing', 150);
INSERT INTO categories values (NULL, 'Shopping', 160);
INSERT INTO categories values (NULL, 'Social & Fun', 170);
INSERT INTO categories values (NULL, 'Travel', 180);

CREATE TABLE websiteAndSignInWebsiteMap (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  website_id INT(10) UNSIGNED NOT NULL,
  signIn_website_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (website_id) REFERENCES websites(id),
  FOREIGN KEY (signIn_website_id) REFERENCES websites(id)
);

INSERT INTO websiteAndSignInWebsiteMap
    SELECT NULL, websitesLogWithMap.website_id, loginWithWebsites.website_id FROM websitesLogWithMap JOIN loginWithWebsites ON website_logwith_id = loginWithWebsites.id;

DROP TABLE websitesLogWithMap, loginWithWebsites;


ALTER TABLE apps DROP FOREIGN KEY apps_ibfk_3;
ALTER TABLE groupApps DROP FOREIGN KEY groupapps_ibfk_1;
ALTER TABLE profiles DROP FOREIGN KEY profiles_ibfk_2;
ALTER TABLE groupLinkApps DROP FOREIGN KEY grouplinkapps_ibfk_2;
ALTER TABLE groupWebsiteApps DROP FOREIGN KEY groupwebsiteapps_ibfk_1;
ALTER TABLE groupClassicApps DROP FOREIGN KEY groupclassicapps_ibfk_1;
ALTER TABLE groupLogWithApps DROP FOREIGN KEY grouplogwithapps_ibfk_1;
ALTER TABLE groupLogWithApps DROP FOREIGN KEY grouplogwithapps_ibfk_2;
ALTER TABLE websiteApps DROP FOREIGN KEY websiteapps_ibfk_3;
ALTER TABLE classicApps DROP FOREIGN KEY classicapps_ibfk_3;
ALTER TABLE logWithApps DROP FOREIGN KEY logwithapps_ibfk_3;
ALTER TABLE linkApps DROP FOREIGN KEY linkapps_ibfk_3;
ALTER TABLE classicApps DROP COLUMN group_classic_app_id;
ALTER TABLE logWithApps DROP COLUMN group_logWith_app_id;
ALTER TABLE websiteApps DROP COLUMN group_website_app_id;
ALTER TABLE linkApps DROP COLUMN group_link_app_id;
ALTER TABLE apps DROP COLUMN group_app_id;
ALTER TABLE profiles DROP COLUMN group_profile_id;

ALTER TABLE appPermissions DROP FOREIGN KEY apppermissions_ibfk_1;
ALTER TABLE customProfiles DROP FOREIGN KEY customprofiles_ibfk_1;
ALTER TABLE groups DROP FOREIGN KEY groups_ibfk_1;
ALTER TABLE profilePermissions DROP FOREIGN KEY profilepermissions_ibfk_1;
ALTER TABLE websitesAndGroupsMap DROP FOREIGN KEY websitesandgroupsmap_ibfk_2;
DROP TABLE IF EXISTS groupClassicApps, groupLogWithApps, groupWebsiteApps, groupLinkApps, groupApps, groupProfiles, groupAdminsMap, groupsAndUsersMap, invitationsAndGroupsMap, customProfiles, customApps, appPermissions, profilePermissions, websitesAndGroupsMap, groups, infrastructuresAdminsMap, infrastructures;

DROP TABLE integrateWebsitesAndUsersMap, updateNewClassicApp, updateNewLogWithApp, updateNewAccount, updateNewPassword, updatesRemoved, updates;

DROP TABLE IF EXISTS websiteRequests, websiteCredentials;
CREATE TABLE websiteRequests (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  url VARCHAR(2000) NOT NULL,
  website_id INT(10) UNSIGNED NOT NULL,
  email VARCHAR(255) NOT NULL,
  date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  FOREIGN KEY (website_id) REFERENCES websites(id)
);

CREATE TABLE websiteCredentials (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  login TEXT NOT NULL,
  password TEXT NOT NULL,
  website_id INT(10) UNSIGNED NOT NULL,
  serverPublicKey_id INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (website_id) REFERENCES websites(id),
  FOREIGN KEY (serverPublicKey_id) REFERENCES serverPublicKeys(id)
);