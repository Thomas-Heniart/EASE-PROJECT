ALTER TABLE websites
ADD COLUMN ratio INT(10) UNSIGNED DEFAULT 0;
ALTER TABLE websites
ADD COLUMN position INT(10) UNSIGNED DEFAULT 1;
ALTER TABLE websites
ADD COLUMN insertDate DATETIME DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE websites
ADD COLUMN locked TINYINT(1) UNSIGNED DEFAULT 0;
ALTER TABLE websites
ADD COLUMN lockedExpiration DATETIME;

DROP PROCEDURE IF EXISTS increaseRatio;
DROP PROCEDURE IF EXISTS decreaseRatio;
DROP PROCEDURE IF EXISTS setRatio;
DROP PROCEDURE IF EXISTS lockWebsite;
DROP PROCEDURE IF EXISTS unlockWebsites;
DROP PROCEDURE IF EXISTS updateWebsitesPositions;
DELIMITER //

CREATE PROCEDURE increaseRatio(IN p_website_id INT(10) UNSIGNED)
BEGIN
  UPDATE websites SET ratio = ratio + 1 WHERE website_id = p_website_id;
END //

CREATE PROCEDURE decreaseRatio(IN p_website_id INT(10) UNSIGNED)
BEGIN
  UPDATE websites SET ratio = ratio - 1 WHERE website_id = p_website_id;
END //

CREATE PROCEDURE setRatio(IN p_website_id INT(10) UNSIGNED, IN new_ratio INT(10) UNSIGNED)
BEGIN
  UPDATE websites SET ratio = new_ratio WHERE website_id = p_website_id;
END //

CREATE PROCEDURE lockWebsite(IN p_website_id INT(10) UNSIGNED)
BEGIN
  START TRANSACTION;
  SELECT position INTO @var FROM websites WHERE website_id = p_website_id;
  SET @i=2;
  UPDATE websites SET position = (@i:=@i+1) WHERE position < @var ORDER BY position;
  UPDATE websites SET locked = 1, position = 2, lockedExpiration = CURDATE() + INTERVAL 3 DAY WHERE website_id = p_website_id;
  COMMIT;
END //

CREATE PROCEDURE unlockWebsites()
BEGIN
  UPDATE websites SET locked = 0 WHERE lockedExpiration >= CURDATE();
END //

CREATE PROCEDURE updateWebsitesPositions()
BEGIN
  START TRANSACTION;
  CALL unlockWebsites();
  UPDATE websites SET position = 1 WHERE insertDate >= CURDATE() - INTERVAL 3 DAY ORDER BY ratio DESC;
  UPDATE websites SET position = 2 WHERE locked = 1;
  SET @i=2;
  UPDATE websites SET position = (@i:=@i+1) WHERE insertDate < CURDATE() - INTERVAL 3 AND locked = 0 DAY ORDER BY ratio DESC;
  COMMIT;
END //

DELIMITER ;

UPDATE
    websites AS w
    LEFT JOIN (
        SELECT
            website_id,
            COUNT(app_id) AS appsNumber
        FROM
            apps
        GROUP BY
            website_id
    ) AS a ON
        w.website_id = a.website_id
SET
    w.ratio = a.appsNumber
WHERE a.appsNumber IS NOT NULL;
