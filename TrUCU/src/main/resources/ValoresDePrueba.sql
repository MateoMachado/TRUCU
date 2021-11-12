USE Trucu;

-- Rol
INSERT INTO Rol (name, description) VALUES 
('User', 'Normal user'),
('Admin', 'Administrator');

-- Account
INSERT INTO Account (email, name, lastName, birthDate, password, rolName) VALUES
('nicopuig@correo.ucu.edu.uy', 'Nicolas', 'Puig', '2001-02-14', 'nicopuig', 'User'),
('sebamazzey@correo.ucu.edu.uy', 'Sebastian', 'Mazzey', '2000-06-25', 'sebamazzey', 'User'),
('mateomachado@correo.ucu.edu.uy', 'Mateo', 'Machado', '2000-10-16', 'mateomacha', 'User'),
('rickyfort@ucu.edu.uy', 'Ricardo', 'Fort', '1985-01-01', 'maeame', 'Admin');

-- Publication
INSERT INTO Publication (title, description, ucuCoinValue, publicationDate, accountEmail) VALUES
('Bicicleta', 'Bicicleta GT rodado 29', 1500, '2021-10-31 00:05:00', 'nicopuig@correo.ucu.edu.uy'),
('Auriculares Logitech G533', 'Auriculares Logitech G533 Wireless', 900, '2021-10-31 03:02:01', 'nicopuig@correo.ucu.edu.uy'),
('Monitor Samsung 24 pulgadas', 'Monitor Samsung 24 pulgadas 1 anio de uso', 1900, '2021-07-31 00:00:00', 'sebamazzey@correo.ucu.edu.uy'),
('Web Cam HD 1080P', 'Web Cam Full Hd 1080P con poco uso', 450, '2021-10-01 07:12:13', 'mateomachado@correo.ucu.edu.uy');

INSERT INTO Publication (title, description, ucuCoinValue, accountEmail) VALUES
('Lapiz digital Sony', 'Para usar en dispositivos tactiles', 10, 'nicopuig@correo.ucu.edu.uy'),
('Agua Salus', '1 Litro', 1, 'nicopuig@correo.ucu.edu.uy'),
('Lampara de techo', 'Uso domestico', 150,  'mateomachado@correo.ucu.edu.uy'),
('Sofa Cama', '200x120cm', 11500,  'mateomachado@correo.ucu.edu.uy'),
('Sofa', '200x120cm', 11500,  'mateomachado@correo.ucu.edu.uy'),
('Vino Faisan en caja', '1 Litro', 10000,  'sebamazzey@correo.ucu.edu.uy');

-- Offer
INSERT INTO Offer (idPublication) VALUES
(3), (3), (2), (7), (10), (5);

-- Offered Publications
INSERT INTO OfferedPublications (idOffer, idPublication) VALUES 
(1, 1), (1,2), (2,4), (3,10), (4, 6), (4, 5), (5, 8), (5, 9);

-- Reason
INSERT INTO Reason (description) VALUES ('Publicacion ofensiva'), ('Contenido ilegal'), ('Spam');

-- Report
INSERT INTO Report (idReason, idPublication) VALUES 
(1, 4), (2, 10), (3,3);