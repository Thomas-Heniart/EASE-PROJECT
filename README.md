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

##LA CRYPTO 2.0 :+1:

[x] Passer à BCrypt
[x] Salage + poivrage
[] AES 256 => Trop couteux en ressources pour le gain de sécurité. AES128 est déjà très très solide.

###Hashage (BCrypt)
* Tout est en static dans la classe *com.Ease.Utils.Crypto.Hashing*
    * public static String hash(String toHash);
    ```java
    String hashed_password = Hashing.hash(password);
    ```
    > Hash directement avec BCrypt. Le salage est fait dans la méthode.
    
    * public static boolean compare(String plainText, String hashText);
    ```java
    if(!Hashing.compare(password, hashed_password)){
        throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
    }
    ```
    > Compare un text en clair et un hashé et renvoie true si le hashé est un hashé du clair.

###Chiffrement AES128
* Tout est en static dans la classe *com.Ease.Utils.Crypto.AES*
    * Attribut static de cette classe :
    ```java
    private static final int KEY_SIZE = 128;
    private final static String[] PEPPERS  = {"xxxx", "xxxx" /* etc. */};
    ```
    > Pour changer la key_size en 256, il faut importer une lib qui permet à java de dépasser sa limite naturelle de clef qui est de 128.
    > Les peppers servent au salage et sont en dur dans le code pour qu'un mec ayant accès à la BDD n'ait quand meme pas accès aux infos.
    
    * Crypter et décrypter un mot de passe (*pareil qu'avant*) :
    ```java
    String cryptedPasswd = AES.encrypt(data, keyUser);
    String passwd = AES.decrypt(cryptedPasswd, keyUser);
    ```
    
    * Crypter et décrypter la keyUser à partir du easePass :
    ```java
    String salt = AES.generateSalt();
    String crypted_keyUser = AES.encryptUserKey(keyUser, password, salt);
    String decrypted_keyUser = AES.decryptUserKey(crypted_keyUser, password, salt);
    ```
    > Le salt sert à saler le password avant de le transformer en clef pour chiffrer la keyUser. Cependant, le salt est combiné avec un pepper dans la méthode avant de saler le password.
    
    * Les méthodes oldDecryptUserKey et oldEncryptUserKey sont toujours présentes pour faire la transition de l'ancienne à la nouvelle crypto.
    
###Transition de l'ancienne crypto à la nouvelle
* Dans *com.Ease.Dashboard.User.Keys*, dans la méthode loadKeys, un bout de code est entouré de deux commentaires. C'est lui qui permet de passer de l'ancien chiffrement au nouveau. Il faudra supprimer ce bout de code quand la transition sera finie.

* Si la valeur de la colonne "saltEase" dans la table **userKeys** est nulle, c'est que le user est passé à la nouvelle crypto.

* Il faut **absolument vider la tables savedSessions** la première fois que la nouvelle crypto est mis en prod, pour que personne ne se reconnecte via les cookies, et bien via la connexion normale.
    