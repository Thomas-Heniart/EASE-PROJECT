CREATE TABLE blacklistedWebsites (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  url VARCHAR(255) NOT NULL,
  count INT(10) UNSIGNED NOT NULL,
  PRIMARY KEY(id)
);

DELETE FROM websiteAttributes WHERE id IN (68, 314, 316, 319, 342);

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 5;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 49;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 50;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 52;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 53;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 55;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 58;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 59;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 60;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 71;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 97;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 98;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 132;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 154;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 258;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 259;

INSERT INTO websiteAttributes values(null, default, null, '2017-01-01 00:00:00', 0, 1);
UPDATE websites SET website_attributes_id = LAST_INSERT_ID() WHERE id = 260;
