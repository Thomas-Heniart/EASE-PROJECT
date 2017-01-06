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

On ne passe pas à AES 256 => On augmente de 40% le coût en ressources pour le gain de sécurité. AES128 est déjà très très solide.

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


##Déploiement du projet

###Le script bash **deploy**
* Lancer ce script pour déployer le projet    
    * Ce script doit être dans le dossier webapps
    * Le script demande le path du .war importé sur le serveur et le login/pwd de déploiement
```bash
bash deploy
```
> Le script écrit les credentials de déploiement dans le fichier "serverLogin". Quand le projet se lance, il va lire les credentials dans le fichier, puis les efface.

* Pour ajouter des credentials de déploiement, voir dans le back-office admin.

###La serverKey
* Il y a une unique keyServer, chiffrée par les différents crédentials de déploiement, stockés dans la tables *serverKeys*.
> Cette keyServer permet notamment de chiffrer toutes les keyUser. Les keyUser chiffrées par la keyServer sont stockées dans la table "userKeys" dans la colonne "backUpKey".

* Une instance de la classe *com.Ease.Server.Context.ServerKey* est créée dans le "OnStart" à partir des crédentials de déploiement. Dans cette classe :
    
    ```java
    public static ServerKey loadServerKey(DataBaseConnection db) throws GeneralException, SQLException ;
    ```
    > Initialise une ServerKey au déploiement. La méthode lit les crédentials de déploiement dans le fichier "serverLogin", puis en les compare avec les credentials stockés et hashés dans la table *serverKeys*. Si les crédentials sont corrects, la serverKey est déchiffrée. 
    Si la table *serverKeys* est vide, la méthode crée les crédentials *root/root*.

    ---
    ```java
    public static ServerKey createServerKey(String login, String password, String keyServer, DataBaseConnection db) throws GeneralException ;
    ```
    > Créer une ServerKey dans la base de donnée en chiffrant la keyServer.

    ---
    ```java
    public static void eraseServerKey(String login, String password, DataBaseConnection db) throws GeneralException ;
    ```
    > Efface des credentials de déploiement dans la base de donnée, uniquement si le password est correct. Throw une exception si c'est le dernier credential stocké dans la table.
    /!\ Attention, si tous les crédentials sont supprimés, la serverKey est perdue et il faut réinitialiser toutes les backUpKeys.

    ---
    ```java
    private static String[] getLoginFileContent() throws GeneralException ;
    ```
    > Renvoie le contenu du fichier **serverLogin** sous la forme {login, password}.

    ---
    ```java
    private static void cleanLoginFileContent() throws GeneralException ;
    ```
    > Vide le contenu du fichier **serverLogin**

    ---
    ```java
    private String 	user, hashed_password, salt, keyServer;
    ```
    > Attributs d'une instance de *ServerKey*. (La classe contient aussi un constructeur et un getter).
    
##Variables
La classe *com.Ease.Context.Variables* contient des variables spécifiques à la machine sur laquelle le projet tourne.
Pour l'instant il faut juste décommenter la partie qui nous concerne quand on pull. C'est pas ouf mais c'est déjà mieux qu'avant.
> Il y a une variables "LOCAL" qui dit si c'est en local. Si c'est le cas, le serveur se lance avec les credentials root/root donc il ne faut pas les supprimer dans sa base en local.
