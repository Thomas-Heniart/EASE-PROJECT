# EASE-PROJECT

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
