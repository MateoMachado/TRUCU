
-- Rol creation
insert into Rol (name,description) VALUES ('User', 'Normal user');
insert into Rol (name,description) VALUES ('Admin', 'Administrator');

-- Account creation
insert into Account (CI, name, lastName, email, birthDate, password, rolName) 
VALUES ('51973513', 'Sebastian', 'Mazzey', 'sebastian.mazzey@correo.ucu.edu.uy','20000830', 'seba123','User');
insert into Account (CI, name, lastName, email, birthDate, password, rolName) 
VALUES ('12345678', 'Mateo', 'Machado', 'mateo.machado@correo.ucu.edu.uy','20010124', 'mateo123','User');
insert into Account (CI, name, lastName, email, birthDate, password, rolName) 
VALUES ('23456789', 'Nicolas', 'Puig', 'nicolas.puig@correo.ucu.edu.uy','20000214', 'nico123','User');
insert into Account (CI, name, lastName, email, birthDate, password, rolName) 
VALUES ('12345', 'Profesores', 'Base', 'profesores@correo.ucu.edu.uy','20211114', 'trucu123','User');

SELECT * FROM Account

-- Publication
insert into Publication (title, description, ucuCoinValue, publicationDate, status, accountCI)
VALUES ('Bicicleta', 'Bicicleta GT rodado 29', 1500, '20211031', 'OPEN', '51973513')
insert into Publication (title, description, ucuCoinValue, publicationDate, status, accountCI)
VALUES ('Auriculares Logitech G533', 'Auriculares Logitech G533 Wireless', 900, '20211031', 'OPEN', '12345678')
insert into Publication (title, description, ucuCoinValue, publicationDate, status, accountCI)
VALUES ('Monitor Samsung 24 pulgadas', 'Monitor Samsung 24 pulgadas 1 año de uso', 1900, '20211031', 'OPEN', '12345678')
insert into Publication (title, description, ucuCoinValue, publicationDate, status, accountCI)
VALUES ('Web Cam HD 1080P', 'Web Cam Full Hd 1080P con poco uso', 450, '20211031', 'OPEN', '12345678')

SELECT * FROM Publication

