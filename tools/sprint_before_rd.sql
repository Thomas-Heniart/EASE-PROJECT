/* For new things */

ALTER TABLE infrastructures DROP group_key;

UPDATE websites SET sso = 1 WHERE login_url LIKE "%accounts.google%";

ALTER TABLE sso ADD img_path VARCHAR(255) NOT NULL;

INSERT INTO groupsAndUsersMap (SELECT null, group_id, user_id, 1 FROM test.GroupAndUserMap WHERE user_id IN (SELECT id FROM users));

ALTER TABLE status ADD last_connection DATETIME DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE infrastructures ADD img_path VARCHAR(255) NOT NULL;

ALTER TABLE status ADD invite_sended TINYINT(1) NOT NULL;

DROP TABLE IF EXISTS integrateWebsitesAndUsersMap;

CREATE TABLE integrateWebsitesAndUsersMap (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	website_id INT(10) UNSIGNED NOT NULL,
	user_id INT(10) UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (website_id) REFERENCES websites(id),
	FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE passwordLost ADD dateOfRequest DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE options ADD homepage_state TINYINT(1) NOT NULL;

UPDATE options SET homepage_state = 1;

ALTER TABLE status ADD homepage_email_sent TINYINT(1) NOT NULL;

UPDATE status SET homepage_email_sent = 0;

DROP TABLE IF EXISTS invitationsAndGroupsMap;
DROP TABLE IF EXISTS invitations;

CREATE TABLE invitations (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL,
	linkCode VARCHAR(255) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE invitationsAndGroupsMap (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	invitation_id INT(10) UNSIGNED NOT NULL,
	group_id INT(10) UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (invitation_id) REFERENCES invitations(id),
	FOREIGN KEY (group_id) REFERENCES groups(id)
);

/* To fix websites */

UPDATE websites SET website_name = 'L\'équipe' WHERE id = 237;
UPDATE websites SET website_name = 'Auchan Direct' WHERE id = 236;
UPDATE websites SET website_name = 'Atelier des Chefs' WHERE id = 232;

UPDATE websites SET website_name = 'AlloCiné' WHERE id = 228;
UPDATE websites SET website_name = 'Bazar Chic' WHERE id = 223;
UPDATE websites SET website_name = 'Vente Du Diable' WHERE id = 222;
UPDATE websites SET website_name = 'Office365 Mails' WHERE id = 69;
UPDATE websites SET website_name = 'LinkedIn' WHERE id = 28;
UPDATE websites SET website_name = 'Zone bourse' WHERE id = 86;
UPDATE websites SET website_name = 'La Fourchette' WHERE id = 192;
UPDATE websites SET noLogin = 1 WHERE id = 99;
UPDATE websites SET website_name = 'Leboncoin' WHERE id = 61;
UPDATE websites SET website_name = 'Crème de la crème' WHERE id = 117;
UPDATE websites SET website_name = 'Vente-privée' WHERE id = 66;
UPDATE websites SET website_name = 'BU Vauban' WHERE id = 97;
UPDATE websites SET website_name = 'Showroomprivé' WHERE id = 131;
UPDATE websites SET website_name = 'Yahoo Mail' WHERE id = 140;
UPDATE websites SET website_name = 'SMENO' WHERE id = 102;
UPDATE websites SET website_name = 'Le Monde' WHERE id = 100;
UPDATE websites SET website_name = 'Le Figaro' WHERE id = 122;
UPDATE websites SET website_name = 'TED' WHERE id = 155;
UPDATE websites SET website_name = 'PayPal' WHERE id = 203;
UPDATE websites SET website_name = 'Unify Iéseg' WHERE id = 71;
UPDATE websites SET website_name = 'MYTF1' WHERE id = 103;
UPDATE websites SET website_name = 'MyIéseg' WHERE id = 98;
UPDATE websites SET website_name = 'Google Analytics' WHERE id = 129;
UPDATE websites SET website_name = 'GitHub' WHERE id = 92;
UPDATE websites SET website_name = 'Sushi Boutik' WHERE id = 91;
UPDATE websites SET website_name = 'ALLO RESTO' WHERE id = 228;
UPDATE websites SET website_name = 'Thesis Manager' WHERE id = 154;
UPDATE websites SET website_name = 'The Noun Project' WHERE id = 201;
UPDATE websites SET website_name = 'Projet Voltaire' WHERE id = 60;
UPDATE websites SET website_name = 'GMAT Prep' WHERE id = 73;
UPDATE websites SET website_name = 'Iéseg Media Plus' WHERE id = 59;
UPDATE websites SET website_name = 'Speaking Agency' WHERE id = 139;
UPDATE websites SET website_name = 'Product Hunt' WHERE id = 206;
UPDATE websites SET folder = 'BazarChic' WHERE id = 223;
UPDATE websites SET folder = 'LinkedIn' WHERE id = 28;
UPDATE websites SET folder = 'TED' WHERE id = 155;
UPDATE websites SET folder = 'TheEconomist' WHERE id = 77;
INSERT INTO websitesLogWithMap values(null, 223, 1);
DELETE FROM loginWithWebsites WHERE id >= 4;
UPDATE websites SET website_homepage = 'http://www.adecco.fr' WHERE id = 104;
UPDATE websites SET website_homepage = 'http://www.koudetatondemand.co', login_url = 'http://www.koudetatondemand.co' WHERE id = 99;
UPDATE websites SET website_homepage = 'https://www.edx.org/' WHERE id = 146;
INSERT INTO websitesLogWithMap values(null, 130, 1);
INSERT INTO websitesLogWithMap values(null, 92, 1);
INSERT INTO websitesLogWithMap values(null, 124, 1);
INSERT INTO websitesLogWithMap values(null, 145, 1);
INSERT INTO websitesLogWithMap values(null, 228, 1);


/* profilePermissions and appPermissions all permissions */
UPDATE profilePermissions SET permission = b'11111111111111111111';
UPDATE appPermissions SET permission = b'11111111111111111111';

/* Remove féfé from admins */
DELETE FROM admins WHERE user_id = 33;

/* Changer folder name de sites qui bug */
UPDATE websites set folder='PremierLeague' where website_name='PremierLeague';
UPDATE websites set folder='Trainline' where website_name='Trainline';

/* Mettre img google sso  */
UPDATE sso set img_path='google.png' where name='Google';

/* IESEG websites */
UPDATE websites SET website_attributes_id = NULL WHERE id = 5;
DELETE FROM websiteAttributes WHERE id = 1;
UPDATE websites SET website_attributes_id = NULL WHERE id = 49;
DELETE FROM websiteAttributes WHERE id = 7;
UPDATE websites SET website_attributes_id = NULL WHERE id = 50;
DELETE FROM websiteAttributes WHERE id = 8;
UPDATE websites SET website_attributes_id = NULL WHERE id = 52;
DELETE FROM websiteAttributes WHERE id = 10;
UPDATE websites SET website_attributes_id = NULL WHERE id = 53;
DELETE FROM websiteAttributes WHERE id = 11;
UPDATE websites SET website_attributes_id = NULL WHERE id = 55;
DELETE FROM websiteAttributes WHERE id = 13;
UPDATE websites SET website_attributes_id = NULL WHERE id = 58;
DELETE FROM websiteAttributes WHERE id = 16;
UPDATE websites SET website_attributes_id = NULL WHERE id = 59;
DELETE FROM websiteAttributes WHERE id = 17;
UPDATE websites SET website_attributes_id = NULL WHERE id = 60;
DELETE FROM websiteAttributes WHERE id = 18;
UPDATE websites SET website_attributes_id = NULL WHERE id = 71;
DELETE FROM websiteAttributes WHERE id = 29;
UPDATE websites SET website_attributes_id = NULL WHERE id = 97;
DELETE FROM websiteAttributes WHERE id = 44;
UPDATE websites SET website_attributes_id = NULL WHERE id = 98;
DELETE FROM websiteAttributes WHERE id = 45;
UPDATE websites SET website_attributes_id = NULL WHERE id = 132;
DELETE FROM websiteAttributes WHERE id = 67;
UPDATE websites SET website_attributes_id = NULL WHERE id = 154;
DELETE FROM websiteAttributes WHERE id = 83;

/* Estice */

INSERT INTO infrastructures values (null, "Estice-Espas-ICM", "estice.png");

INSERT INTO groups values (null, "Estice", null, 4);
INSERT INTO groups values (null, "Administratif", 10, 4);
INSERT INTO groups values (null, "Etudiant", 10, 4);

INSERT INTO profilePermissions values (null, 11, default); /* 4 */
INSERT INTO profilePermissions values (null, 12, default); /* 5 */

INSERT INTO profileInfo values (null, 'MySchool', '#373B60');
INSERT INTO groupProfiles values (null, 11, 4, LAST_INSERT_ID(), 0); /* 4 */

INSERT INTO profileInfo values (null, 'MySchool', '#373B60');
INSERT INTO groupProfiles values (null, 12, 5, LAST_INSERT_ID(), 0); /* 5 */


INSERT INTO tags values (null, 'MySchool', '1');
INSERT INTO tagsAndGroupsMap VALUES (null, LAST_INSERT_ID(), 10);