#Tomcat 8

##Systemctl
```bash
systemctl [action] tomcat8
```

##Deployment
1. **scp file.war user@serverIp:file.war**
2. ssh user@serverIp (once you are connected, **sudo su**)
3. **systemctl stop tomcat8**
4. **cd /opt/tomcat/webapps**
5. Rename old WAR file and move it somewhere else
6. Move new WAR file in **/opt/tomcat/webapps** and rename it to **ROOT.war** if needed
7. **systemctl start tomcat8**