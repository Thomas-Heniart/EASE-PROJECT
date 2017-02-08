/* For new things */

ALTER TABLE status ADD last_connection DATETIME DEFAULT CURRENT_TIMESTAMP;

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