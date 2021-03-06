CREATE DATABASE IF NOT EXISTS movietracker;

USE movietracker ;

CREATE TABLE IF NOT EXISTS User (
 email VARCHAR(255) NOT NULL,
 name VARCHAR(255) NOT NULL,
 password VARCHAR(255) NOT NULL,
 PRIMARY KEY (email)) IF NOT EXISTS;

CREATE TABLE IF NOT EXISTS Movie (
 movieID VARCHAR(255) NOT NULL,
 movieName VARCHAR(255) NOT NULL,
 description VARCHAR(255),
 posterImage VARCHAR(255),
 PRIMARY KEY (MovieID)) IF NOT EXISTS;

CREATE TABLE IF NOT EXISTS TVShow (
 seriesID VARCHAR(255) NOT NULL,
 seriesName VARCHAR(255) NOT NULL,
 description VARCHAR(255),
 posterImage VARCHAR(255),
 PRIMARY KEY (seriesID));

create table WatchList( 
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    mediaId VARCHAR(255) NOT NULL , 
    email VARCHAR(255) NOT NULL ,
    FOREIGN KEY(mediaId) REFERENCES Media(MediaId), 
    FOREIGN KEY(email) REFERENCES User(email) 
    );

create table Media( 
    mediaId VARCHAR(255) NOT NULL PRIMARY KEY, 
    mediaName VARCHAR(255) NOT NULL);

create table Review( 
    reviewID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    mediaId VARCHAR(255) NOT NULL FOREIGN KEY, 
    email VARCHAR(255) NOT NULL FOREIGN KEY,
    title VARCHAR(255) NOT NULL, 
    Description VARCHAR(255) NOT NULL,
    rating DOUBLE(2,2) NOT NULL  );

create table Review( 
    reviewID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
    mediaId VARCHAR(255) NOT NULL , 
    email VARCHAR(255) NOT NULL ,
    title VARCHAR(255) NOT NULL, 
    Description VARCHAR(255) NOT NULL,
    rating DOUBLE(2,2) NOT NULL,
    FOREIGN KEY(mediaId) REFERENCES Media(MediaId), 
    FOREIGN KEY(email) REFERENCES User(email) );