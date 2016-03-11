/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Ofer Nitka-Nakash
 * Created: 2-Feb-2016
 */
-- DROP DATABASE IF EXISTS csdbookstore;
-- CREATE DATABASE csdbookstore;
USE g4w16;
DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS Poll;
DROP TABLE IF EXISTS Feed;
DROP TABLE IF EXISTS Banner;
DROP TABLE IF EXISTS TaxeRates;
DROP TABLE IF EXISTS SalesDetails;
DROP TABLE IF EXISTS Sales;
DROP TABLE IF EXISTS BookContributors;
DROP TABLE IF EXISTS BookIdentifiers;
DROP TABLE IF EXISTS BookGenres;
DROP TABLE IF EXISTS BookFormats;
DROP TABLE IF EXISTS Contributor;
DROP TABLE IF EXISTS ContributionType;
DROP TABLE IF EXISTS Genre;
DROP TABLE IF EXISTS IdentifierType;
DROP TABLE IF EXISTS Format;
DROP TABLE IF EXISTS Reviews;
DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS BillingAddress;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Title;
DROP TABLE IF EXISTS Province;

CREATE TABLE Province (
  ID varchar(128) PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE Title (
  ID varchar(128) PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE Client (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Title varchar(128) NOT NULL,
  FirstName varchar(128) NOT NULL,
  LastName varchar(128) NOT NULL,
  Email varchar(128) NOT NULL UNIQUE,
  Password varchar(254) NOT NULL,
  CompanyName varchar(128) NOT NULL DEFAULT "",
  homePhoneNumber varchar(20) NOT NULL,
  cellPhoneNumber varchar(20) NOT NULL
)ENGINE=InnoDB;

CREATE TABLE BillingAddress (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name varchar(128) NOT NULL DEFAULT "",
  Client int NOT NULL,
  Province varchar(128) NOT NULL,
  City varchar(128) NOT NULL,
  FirstCivicAddress varchar(250) NOT NULL,
  SecondCivicAddress varchar(250) NOT NULL DEFAULT "",
  PostalCode varchar(128) NOT NULL,
  FOREIGN KEY(Client) REFERENCES Client(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Books (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Title varchar(128) NOT NULL,
  Publisher varchar(128) NOT NULL,
  Description MEDIUMTEXT NOT NULL,
  Pages int NOT NULL,
  WholesalePrice DECIMAL(12,2) NOT NULL DEFAULT 0,
  ListPrice DECIMAL(12,2) NOT NULL,
  SalePrice DECIMAL(12,2) NOT NULL DEFAULT 0,
  PubDate timestamp NOT NULL,
  DateEntered timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  RemovalStatus BOOLEAN NOT NULL DEFAULT 0
)ENGINE=InnoDB;


CREATE TABLE Reviews (
  Book int NOT NULL,
  CreationDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Client int NOT NULL,
  Rating int(1) NOT NULL,
  Text MEDIUMTEXT NOT NULL,
  Approval BOOLEAN NOT NULL DEFAULT 0,
  PRIMARY KEY (Book, Client),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Client) REFERENCES Client(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE ContributionType (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE Contributor (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name varchar(128) NOT NULL,
  Contribution varchar(128) NOT NULL,
  FOREIGN KEY(Contribution) REFERENCES ContributionType(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE BookContributors (
  Book int NOT NULL,
  Contributor int NOT NULL,
  PRIMARY KEY (Book, Contributor),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Contributor) REFERENCES Contributor(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE IdentifierType (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE BookIdentifiers (
  Book int NOT NULL,
  Type varchar(128) NOT NULL,
  Code  varchar(128) NOT NULL UNIQUE,
  PRIMARY KEY (Book, Type),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Type) REFERENCES IdentifierType(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Genre (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE BookGenres (
  Book int NOT NULL,
  Genre varchar(128) NOT NULL,
  PRIMARY KEY (Book, Genre),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Genre) REFERENCES Genre(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Format (
  Type varchar(128) NOT NULL PRIMARY KEY
)ENGINE=InnoDB;

CREATE TABLE BookFormats (
  Book int NOT NULL,
  Format varchar(128) NOT NULL,
  File varchar(254) NOT NULL,
  PRIMARY KEY (Book, Format),
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY(Format) REFERENCES Format(Type) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Sales (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  DateEntered timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Client int NOT NULL,  
  GrossValue DECIMAL(12,2) NOT NULL DEFAULT 0, 
  NetValue DECIMAL(12,2) NOT NULL DEFAULT 0,
  Removed BOOLEAN NOT NULL DEFAULT 0,
  FOREIGN KEY(Client) REFERENCES Client(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE SalesDetails (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Sale int NOT NULL, 
  Book int NOT NULL,
  Price DECIMAL(12,2) NOT NULL,
  PST DECIMAL(12,2) DEFAULT 0,
  HST DECIMAL(12,2) DEFAULT 0,
  QST DECIMAL(12,2) DEFAULT 0,
  Removed BOOLEAN NOT NULL DEFAULT 0,
  FOREIGN KEY(Sale) REFERENCES Sales(ID) ON DELETE CASCADE,
  FOREIGN KEY(Book) REFERENCES Books(ID) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE Poll (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Question varchar(128) NOT NULL UNIQUE,
  FirstAnswer varchar(128) NOT NULL UNIQUE,
  SecondAnswer varchar(128) NOT NULL UNIQUE,
  ThirdAnswer varchar(128) NOT NULL UNIQUE,
  FourthAnswer varchar(128) NOT NULL UNIQUE,
  FirstCount int DEFAULT 0,
  SecondCount int DEFAULT 0,
  ThirdCount int DEFAULT 0,
  FourthCount int DEFAULT 0
)ENGINE=InnoDB;

CREATE TABLE Admin (
  username varchar(128) PRIMARY KEY,
  Password varchar(254) NOT NULL
)ENGINE=InnoDB;

CREATE TABLE Feed (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Name varchar(254) NOT NULL,
  URI varchar(254) NOT NULL
)ENGINE=InnoDB;

CREATE TABLE Banner (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  URI varchar(254) NOT NULL
)ENGINE=InnoDB;

CREATE TABLE TaxeRates (
  Province varchar(128) PRIMARY KEY,
  PST DECIMAL(12,2) DEFAULT 0,
  HST DECIMAL(12,2) DEFAULT 0,
  QST DECIMAL(12,2) DEFAULT 0,
  Updated timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=InnoDB;
