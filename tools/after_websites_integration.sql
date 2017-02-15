INSERT INTO appPermissions values (null, 11, default); /* 34 */
INSERT INTO appPermissions values (null, 12, default); /* 35 */

INSERT INTO appsInformations values (null, 'Chamilo');
INSERT INTO groupApps values (null, 4, 11, 34, 'groupWebsiteApp', LAST_INSERT_ID(), 0); /* 34 */
INSERT INTO appsInformations values (null, 'ownCloud');
INSERT INTO groupApps values (null, 4, 11, 34, 'groupWebsiteApp', LAST_INSERT_ID(), 0); /* 35 */
INSERT INTO appsInformations values (null, 'Chamilo');
INSERT INTO groupApps values (null, 5, 12, 35, 'groupWebsiteApp', LAST_INSERT_ID(), 0); /* 36 */
INSERT INTO appsInformations values (null, 'ownCloud');
INSERT INTO groupApps values (null, 5, 12, 35, 'groupWebsiteApp', LAST_INSERT_ID(), 0); /* 37 */
INSERT INTO appsInformations values (null, 'JobTeaser');
INSERT INTO groupApps values (null, 5, 12, 35, 'groupWebsiteApp', LAST_INSERT_ID(), 0); /* 38 */
INSERT INTO appsInformations values (null, 'BU Vauban');
INSERT INTO groupApps values (null, 5, 12, 35, 'groupWebsiteApp', LAST_INSERT_ID(), 0); /* 39 */


INSERT INTO groupWebsiteApps values (null, 34, 257, 'groupEmptyApp'); /* Chamilo */
INSERT INTO groupWebsiteApps values (null, 35, 258, 'groupEmptyApp'); /* ownCloud */
INSERT INTO groupWebsiteApps values (null, 36, 257, 'groupEmptyApp'); /* Chamilo */
INSERT INTO groupWebsiteApps values (null, 37, 258, 'groupEmptyApp'); /* ownCloud */
INSERT INTO groupWebsiteApps values (null, 38, 259, 'groupEmptyApp'); /* JobTeaser */
INSERT INTO groupWebsiteApps values (null, 39, 97, 'groupEmptyApp'); /*BU Vauban*/

INSERT INTO tagsAndSitesMap values (22, 257);
INSERT INTO tagsAndSitesMap values (22, 258);
INSERT INTO tagsAndSitesMap values (22, 259);
INSERT INTO tagsAndSitesMap values (22, 97);

INSERT INTO websitesAndGroupsMap values (null, 257, 10);
INSERT INTO websitesAndGroupsMap values (null, 258, 10);
INSERT INTO websitesAndGroupsMap values (null, 258, 12);
INSERT INTO websitesAndGroupsMap values (null, 97, 12);