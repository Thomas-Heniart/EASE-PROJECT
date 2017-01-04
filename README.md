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

### NOUVELLE CRYPTO :+1:

##BCrypt
* *com.Ease.Utils.Crypto.Hashing*
    * public static String hash(String toHash);
    > Cette méthode hash un text en utilisant des méthodes de la classe *com.Ease.Utils.Crypto.BCrypt*. Elle utilise un salt mais il n'y a pas besoin de l'enregistrer (Magic bcrypt). Du coup ça rend useless les colonnes "saltEase" de la table userKeys et "saltToken" de savedSessions.
    * public static boolean compare(String plainText, String hashText);
    > Cette méthode renvoie true ssi le hashText est bien un hashé du plainText. _Si j'ai bien compris, ya pas besoin du salt parce que BCrypt(plain, salt) = BCrypt(plain, hash(plain, salt)). En gros si on réutilise le hashé comme salt retombe sur le meme hashé._

##Nouveau salage pour AES
* *com.Ease.Utils.Crypto.AES*
    * private final static String[] PEPPERS;
    > Des salts supplémentaires, en dur dans le code pour qu'un mec ayant accès à la BDD n'ait quand meme pas accès aux infos
    * public static String encryptUserKey(String plainKey, String easePass, String salt);
    * public static String decryptUserKey(String encryptedKey, String easePass, String salt);
    > Le nouveau salage est utilisé dans les méthodes encryptUserKey et decryptUserKey et consiste à additionner le sel de la base de donnée avec un pepper.
    * private static String getPepper(String pass);
    > Permet de déterminer quel pepper utiliser suivant le easePass utilisé pour chiffrer la clef user.
    * private static byte[] pepperedSalt(byte[] saltBytes, String pass);
    > Fait la somme du salt et du pepper.
    * public static oldDecryptUserKey(String encryptedKey, String easePass, String salt);
    > Ancienne méthode, utile pour switcher à la nouvelle crypto
    
##Transition de l'ancienne crypte à la nouvelle
* *com.Ease.Dashboard.User.Keys*
    * public static Keys loadKeys(String id, String password, ServletManager sm);
    ```java
    if(saltEase != null){
	   System.out.println("reset keys");
       String hashedPass = Hashing.SHA(password, saltEase);
       if (hashedPass.equals(hashed_password) == false) {
            throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
        }
		keyUser = AES.oldDecryptUserKey(crypted_keyUser, password, saltPerso);
		String newSalt = AES.generateSalt();
		crypted_keyUser = AES.encryptUserKey(keyUser, password, newSalt);
		hashed_password = Hashing.hash(password);
		saltEase = null;
		saltPerso = newSalt;
        db.set("UPDATE userKeys SET password='"+hashed_password+"', saltEase=null, saltPerso='"+newSalt+"', keyUser='"+crypted_keyUser+"' WHERE id="+id+";");
	   System.out.println("ok");
    }
    ```
    > C'est cette partie de code, au moment où on load les keys, qui transforma la BDD. Cette opération n'est pas faite dans l'autre méthode "loadKeysWithoutPassword". Il faut donc **absolument vider la tables savedSessions** la première fois que ceci est mis en prod, pour que personne ne se reconnecte via les cookies.
* Base de données
    * userKeys
    > Au fure et à mesure de la transition vers le nouveau hashage, la colonne "saltEase" va prendre des valeur null. A terme, il faudra la supprimer.
    * savedSessions
    > On a supprimé la colonne saltToken.
    