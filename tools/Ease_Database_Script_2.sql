ALTER TABLE shareableApps DROP FOREIGN KEY `shareableApps_ibfk_2`;
ALTER TABLE shareableApps DROP COLUMN `teamUser_owner_id`;

ALTER TABLE sharedApps DROP FOREIGN KEY `sharedApps_ibfk_3`;
ALTER TABLE sharedApps DROP COLUMN channel_id;
ALTER TABLE shareableApps DROP FOREIGN KEY `shareableApps_ibfk_3`;
ALTER TABLE shareableApps MODIFY COLUMN channel_id INT(10) UNSIGNED NOT NULL;
ALTER TABLE shareableApps ADD CONSTRAINT `shareableApps_ibfk_3` FOREIGN KEY (channel_id) REFERENCES channels(id);
ALTER TABLE accounts DROP COLUMN canSeeInformation;

CREATE TABLE enterpriseAppRequests (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  request_id INT(10) UNSIGNED NOT NULL,
  information_name VARCHAR(255) NOT NULL,
  information_value VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (request_id) REFERENCES pendingJoinAppRequests(id)
);

CREATE TABLE enterpriseAppAttributes (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  enterprise_app_id INT(10) UNSIGNED NOT NULL,
  fill_in_switch TINYINT(1) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (enterprise_app_id) REFERENCES shareableApps(id)
);

INSERT INTO enterpriseAppAttributes SELECT null, app_id, 1 FROM websiteApps JOIN shareableApps ON websiteApps.app_id = shareableApps.id WHERE websiteApps.type LIKE "websiteApp";