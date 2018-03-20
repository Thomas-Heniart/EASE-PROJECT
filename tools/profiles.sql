ALTER TABLE teamUsers
  DROP COLUMN profile_id;
ALTER TABLE profiles
  ADD COLUMN teamUser_id INT(10) UNSIGNED;
ALTER TABLE profiles
  ADD COLUMN channel_id INT(10) UNSIGNED;
ALTER TABLE profiles
  ADD CONSTRAINT `profiles_ibfk_4` FOREIGN KEY (teamUser_id) REFERENCES teamUsers (id);
ALTER TABLE profiles
  ADD CONSTRAINT `profiles_ibfk_5` FOREIGN KEY (channel_id) REFERENCES channels (id);