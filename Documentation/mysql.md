#MySQL

##Logging
Edit /etc/my.cnf.d/server.cnf
````bash
[mysqld]
log_warnings                    = 1
log-warnings                    = 1
log-error                       = /var/log/mariadb/mysql-error.log
general_log                     = 1
general_log_file                = /var/log/mariadb/mysql-general.log
log-output                      = FILE
```

Check MySQL variables
````bash
MariaDB [(none)]> show variables like '%log%';
```

##AddUsers
**Root user**
````mysql
CREATE USER 'newuser'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON * . * TO 'newuser'@'localhost';
GRANT GRANT OPTION ON *.* TO 'newuser'@'localhost';
FLUSH PRIVILEGES;
```
**Client user**
```mysql
CREATE USER 'newuser'@'%' IDENTIFIED BY 'password';
GRANT INSERT,DELETE,UPDATE,SELECT ON ease.* TO 'newuser'@'%';
FLUSH PRIVILEGES
```

**Remove root**
```mysql
`DROP USER root@'localhost';
```

**Revoke privilege**
```mysql
REVOKE [type of permission] ON [database name].[table name] FROM ‘[username]’@‘localhost’;
```

**show users**
```mysql
SELECT host, user, FROM mysql.user;
```

**Show privileges of a user**
```mysql
SHOW GRANTS FOR user@'somewhere';
```

###Reset root password
```bash
systemctl stop mysqld
mysqld_safe --skip-grant-tables
mysql --user=root mysql
```
```mysql
UPDATE user SET password=PASSWORD('new-password') WHERE user='root';
FLUSH PRIVILEGES
EXIT;
```