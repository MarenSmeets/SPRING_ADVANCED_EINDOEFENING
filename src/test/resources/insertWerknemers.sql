insert into werknemers (familienaam, voornaam, email, chefid, jobtitelid, salaris, paswoord, geboorte, rijksregisternr)
values
('chef1','noChef','test1@test.com',NULL,1,1000,'{bcrypt}$2a$10$i4MlDK9l7YM.cpwCY68j4OJP7CEin5.wFDJCptUP7CQWHnNPh6xjy','1975-05-05',75050500203),
('chef2','onderChef1','test2@test.com',1,2,1000,'{bcrypt}$2a$10$i4MlDK9l7YM.cpwCY68j4OJP7CEin5.wFDJCptUP7CQWHnNPh6xjy','1976-06-06',76060600102),
('test1','onderChef1','test3@test.com',1,3,1000,'{bcrypt}$2a$10$i4MlDK9l7YM.cpwCY68j4OJP7CEin5.wFDJCptUP7CQWHnNPh6xjy','1977-07-07',77070700296),
('test2','onderChef2','test4@test.com',2,4,1000,'{bcrypt}$2a$10$i4MlDK9l7YM.cpwCY68j4OJP7CEin5.wFDJCptUP7CQWHnNPh6xjy','1978-08-08',78080800294),
('test3','onderChef2','test5@test.com',2,5,1000,'{bcrypt}$2a$10$i4MlDK9l7YM.cpwCY68j4OJP7CEin5.wFDJCptUP7CQWHnNPh6xjy','1979-09-09',79090900193);