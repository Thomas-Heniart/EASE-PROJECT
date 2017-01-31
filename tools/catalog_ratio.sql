DROP PROCEDURE IF EXISTS updateWebsitesPositions;
DELIMITER //

CREATE PROCEDURE updateWebsitesPositions()
BEGIN
	UPDATE websites AS w
		JOIN websiteAttributes AS wA
		ON (w.website_attributes_id = wA.id)
	SET wA.new = 0
	WHERE wA.addedDate <= CURRENT_TIMESTAMP - INTERVAL 4 DAY AND wA.new = 1;

	SET @i := 2;

	UPDATE websites
	SET position = (@i := @i + 1)
	ORDER BY ratio DESC;
  
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
        WHERE type <> "websiteApp"
        GROUP BY
            website_id
    ) AS a ON
        w.id = a.website_id
SET
    w.ratio = a.appsNumber
WHERE a.appsNumber IS NOT NULL;