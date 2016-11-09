#!/bin/sh

mysql -u client -pP6au23q7 test -e "DELETE FROM savedSessions WHERE datetime < SUBTIME(CURRENT_TIMESTAMP, '2 0:0:0.0');"