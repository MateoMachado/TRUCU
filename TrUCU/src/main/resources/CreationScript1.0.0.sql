CREATE DATABASE Trucu;

USE Trucu;

CREATE TABLE Rol (
	name VARCHAR,
	description VARCHAR,

	CONSTRAINT PK_rol PRIMARY KEY (name)
);

CREATE TABLE Account (
	CI VARCHAR(10),
	name VARCHAR(32) NOT NULL,
	lastName VARCHAR(32) NOT NULL,
	email VARCHAR(50) NOT NULL,
	birthDate DATE NOT NULL,
	password VARCHAR(32) NOT NULL,
	rolName VARCHAR NOT NULL,

	CONSTRAINT PK_account PRIMARY KEY (CI),
	CONSTRAINT FK_account_rol FOREIGN KEY (rolName) REFERENCES Rol(name)
);

CREATE TABLE Publication (
	idPublication INT IDENTITY(1, 1),
	title VARCHAR(64),
	description VARCHAR(256),
	ucuCoinValue INT,
	publicationDate DateTime DEFAULT GetDate(),
	status VARCHAR(16),
	accountCI VARCHAR(10),

	CONSTRAINT PK_publication PRIMARY KEY (idPublication),
	CONSTRAINT FK_publication_account FOREIGN KEY (accountCI) REFERENCES Account(CI)
);

CREATE TABLE Offer(
	idOffer INT IDENTITY(1, 1),
	status VARCHAR(16),
	offerDate DateTime,
	idPublication INT,

	CONSTRAINT PK_offer PRIMARY KEY (idOffer),
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
	path VARCHAR,
	idPublication INT,

	CONSTRAINT PK_image PRIMARY KEY (idImage),
	CONSTRAINT FK_image_publication FOREIGN KEY (idPublication) REFERENCES Publication(idPublication)
);

CREATE TABLE Reason(
	idReason INT IDENTITY(1, 1),
	description VARCHAR,

	CONSTRAINT PK_reason PRIMARY KEY (idReason)
);

CREATE TABLE Report (
	idReport INT IDENTITY(1, 1),
	status VARCHAR(16),
	idReason INT,

	CONSTRAINT PK_report PRIMARY KEY (idReport),
	CONSTRAINT FK_report_reason FOREIGN KEY (idReason) REFERENCES Reason(idReason)
);

CREATE TABLE UserAuthentication(
	CI VARCHAR(10),

	CONSTRAINT PK_userAuthentication PRIMARY KEY (CI),
	CONSTRAINT FK_userAuthentication_account FOREIGN KEY (CI) REFERENCES Account(CI)
);