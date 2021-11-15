CREATE DATABASE Trucu;
GO
USE Trucu;

CREATE TABLE Rol (
	name VARCHAR(32),
	description VARCHAR(255) NOT NULL,

	CONSTRAINT PK_rol PRIMARY KEY (name)
);

CREATE TABLE Account (
	email VARCHAR(50),
	name VARCHAR(32) NOT NULL,
	lastName VARCHAR(32) NOT NULL,
	birthDate DATE NOT NULL,
	password VARCHAR(32) NOT NULL,
	rolName VARCHAR(32) NOT NULL,

	CONSTRAINT PK_account PRIMARY KEY (email),
	CONSTRAINT CHK_account CHECK (
		(email like '%@%ucu.edu.uy' and email not like '%..%' and email not like '%@%@%' and email not like '%@.%' and email not like '%.@%')),
	CONSTRAINT FK_account_rol FOREIGN KEY (rolName) REFERENCES Rol(name)
);

CREATE TABLE Publication (
	idPublication INT IDENTITY(1, 1),
	title VARCHAR(64) NOT NULL,
	description VARCHAR(256),
	ucuCoinValue INT NOT NULL,
	publicationDate DateTime DEFAULT GetDate(),
	status VARCHAR(16) DEFAULT 'OPEN',
	accountEmail VARCHAR(50) NOT NULL,

	CONSTRAINT PK_publication PRIMARY KEY (idPublication),
	CONSTRAINT CHK_publication CHECK (
		ucuCoinValue > 0 and 
		status in ('OPEN', 'CLOSED', 'HIDDEN', 'SETTLING', 'CANCELED', 'REPORTED')),
	CONSTRAINT FK_publication_account FOREIGN KEY (accountEmail) REFERENCES Account(email)
);

CREATE TABLE Offer(
	idOffer INT IDENTITY(1, 1),
	status VARCHAR(16) DEFAULT 'OPEN',
	offerDate DateTime DEFAULT GetDate(),
	idPublication INT NOT NULL,

	CONSTRAINT PK_offer PRIMARY KEY (idOffer),
	CONSTRAINT CHK_offer CHECK (status in ('OPEN', 'CLOSED', 'REJECTED', 'SETTLING', 'CANCELED', 'CHANGED')),
	CONSTRAINT FK_offer_publication FOREIGN KEY (idPublication) REFERENCES Publication(idPublication)
);

CREATE TABLE OfferedPublications(
	idOffer INT,
	idPublication INT,

	CONSTRAINT PK_offeredPublications PRIMARY KEY (idOffer, idPublication),
	CONSTRAINT FK_offeredPublications_publication FOREIGN KEY (idPublication) REFERENCES Publication(idPublication),
	CONSTRAINT FK_offeredPublications_offer FOREIGN KEY (idOffer) REFERENCES Offer(idOffer)
);

CREATE TABLE Image(
	idImage INT IDENTITY(1, 1),
	imageBytes VARCHAR(MAX),
	idPublication INT NOT NULL,

	CONSTRAINT PK_image PRIMARY KEY (idImage),
	CONSTRAINT FK_image_publication FOREIGN KEY (idPublication) REFERENCES Publication(idPublication)
);

CREATE TABLE Reason(
	idReason INT IDENTITY(1, 1),
	description VARCHAR(255),

	CONSTRAINT PK_reason PRIMARY KEY (idReason)
);

CREATE TABLE Report (
	idReport INT IDENTITY(1, 1),
	status VARCHAR(16) DEFAULT 'OPEN',
	idReason INT NOT NULL,
        idPublication INT NOT NULL,

	CONSTRAINT PK_report PRIMARY KEY (idReport),
	CONSTRAINT CHK_report CHECK (status in ('OPEN', 'REJECTED', 'ACCEPTED')),
	CONSTRAINT FK_report_reason FOREIGN KEY (idReason) REFERENCES Reason(idReason),
        CONSTRAINT FK_report_publication FOREIGN KEY (idPublication) REFERENCES Publication(idPublication)
);

-- User creation
CREATE LOGIN trucu WITH PASSWORD = 'Bdtrucu123'

IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'trucu')
BEGIN
	CREATE USER [trucu] FOR LOGIN [trucu]
	EXEC sp_addrolemember N'db_owner', N'trucu'
END
