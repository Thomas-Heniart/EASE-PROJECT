DROP PROCEDURE IF EXISTS addEmail;

DELIMITER //

CREATE PROCEDURE addEmail(IN p_user_id INT(10) UNSIGNED, IN p_email VARCHAR(100))
BEGIN
  SELECT count(id) INTO @var FROM usersEmails WHERE user_id = p_user_id AND email = p_email;
  IF @var = 0 THEN
    INSERT INTO usersEmails values (NULL, p_user_id, p_email, 0);
  END IF;
END //

DELIMITER ;
