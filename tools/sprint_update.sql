DROP TABLE IF EXISTS updates;

CREATE TABLE usersPrivateExtensions (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	user_id INT(10) UNSIGNED NOT NULL,
	extension_key VARCHAR(32) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE `updates` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `type` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

CREATE TABLE  updateNewPassword (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	update_id INT(10) UNSIGNED NOT NULL,
	classic_app_id INT(10) UNSIGNED NOT NULL,
	new_password VARCHAR(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (update_id) REFERENCES updates(id),
	FOREIGN KEY (classic_app_id) REFERENCES classicApss(id)
);

CREATE TABLE updateNewAccount (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	update_id INT(10) UNSIGNED NOT NULL,
	website_id INT(10) UNSIGNED NOT NULL,
	type VARCHAR(25) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (website_id) REFERENCES websites(id),
	FOREIGN KEY (update_id) REFERENCES updates(id)
);

CREATE TABLE updateNewLogWithApp (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	update_new_account_id INT(10) UNSIGNED NOT NULL,
	logWith_app_id INT(10) UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (update_new_account_id) REFERENCES updateNewAccount(id),
	FOREIGN KEY (logWith_app_id) REFERENCES logWithApps(id)
);

CREATE TABLE updateNewClassicApp (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	update_new_account_id INT(10) UNSIGNED NOT NULL,
	password VARCHAR(255) NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (update_new_account_id) REFERENCES updateNewAccount(id)
);

CREATE TABLE classicUpdateInformations (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	update_new_classic_app_id INT(10) UNSIGNED NOT NULL,
	information_name VARCHAR(255),
	information_value VARCHAR(255),
	PRIMARY KEY (id),
	FOREIGN KEY (update_new_account_id) REFERENCES updateNewClassicApp(id)
);