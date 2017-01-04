# EASE-PROJECT

##Tests before production
###Create user
* Check with wrong inputs
* Without group
  * Check in db (users, keys, profiles, usersEmails, not in groups)

* With a group
  * * Check in db (users, keys, profiles, usersEmails, groups)

##Java documentation
###Generate random code
To generate a new random code
```java
String newCode = CodeGenerator.generateNewCode();
```

##Server documentation
###Add a new timer
Goto
```bash
cd /etc/systemd/system
```

New file **task.service**
```bash
[Unit]
Description=Description of you task

[Service]
Type=oneshot
ExecStart=/usr/bin/sh -c '~/bin/yourTask.sh'
```

New file **task.timer**
```bash
[Unit]
Description=Run backup.service every 10 minutes

[Timer]
OnCalendar=*-*-* 05:00:00

[Install]
WantedBy=multi-user.target
```
Enable, start end check task
```bash
systemctl enable task.timer
systemctl start task.timer
systemctl is-enabled task.timer
systemctl is-active task.timer
```
If you change a file, execute :
```bash
systemctl daemon-reload
```

###Add sudo user
```bash
useradd -m -G wheel -s /bin/bash newUser
passwd newUser
```

###Edit ssh paremeters
```bash
emacs /etc/ssh/sshd_config.pacnew

PermitRootLogin no
AllowUsers user1 user2 user3
```
**Then**
```bash
systemctl restart sshd
```

###Logs
* SSH logs
```bash
journalctl -u sshd |tail -10
```