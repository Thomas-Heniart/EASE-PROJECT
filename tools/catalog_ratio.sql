DROP PROCEDURE IF EXISTS updateWebsitesPositions;
DELIMITER //

CREATE PROCEDURE updateWebsitesPositions()
BEGIN
  SET @i=2;
  UPDATE websites SET position = (@i:=@i+1) WHERE insertDate < CURDATE() - INTERVAL 3 AND locked = 0 DAY ORDER BY ratio DESC;
END //

DELIMITER ;

UPDATE
    websites AS w
    LEFT JOIN (
        SELECT
            website_id,
            COUNT(app_id) AS appsNumber
        FROM
            websiteApps
        GROUP BY
            website_id
    ) AS a ON
        w.id = a.website_id
SET
    w.ratio = a.appsNumber
WHERE a.appsNumber IS NOT NULL;