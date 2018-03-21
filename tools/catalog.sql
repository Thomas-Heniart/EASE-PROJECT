UPDATE websites
SET ratio = 0;
UPDATE websites w
SET w.ratio = (SELECT COUNT(websiteApps.id) AS count
               FROM websiteApps
                 JOIN teamCardReceivers ON teamCardReceivers.id = websiteApps.id
               WHERE websiteApps.website_id = w.id
);