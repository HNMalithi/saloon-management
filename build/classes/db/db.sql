/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  User
 * Created: Aug 13, 2019
 */

DROP DATABASE saloonDB;

CREATE DATABASE saloonDB;

USE saloonDB;

CREATE TABLE systemUser(
	userId VARCHAR(255) NOT NULL,
	firstName VARCHAR(255),
        lastName VARCHAR(255),
	contactNo VARCHAR(255),
	username VARCHAR(255),
	password VARCHAR(255),
	userPost VARCHAR(255),
	userRegDate VARCHAR(255),
	CONSTRAINT PRIMARY KEY(userId)		
);

CREATE TABLE customer(
	custId VARCHAR(255) NOT NULL,
	custName VARCHAR(255),
	contactNo VARCHAR(255),
	custRegDate VARCHAR(255),
	CONSTRAINT PRIMARY KEY(custId)		
);

CREATE TABLE sellItem(
	itemCode VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	unitPrice DECIMAL(10,2),
	qty INT,
	CONSTRAINT PRIMARY KEY(itemCode)
);

CREATE TABLE rentItem(
	itemCode VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	rentPrice DECIMAL(10,2),
	qty INT,
	CONSTRAINT PRIMARY KEY(itemCode)
);

CREATE TABLE service(
	serviceNo VARCHAR(255) NOT NULL,
	customerId VARCHAR(255),
	description VARCHAR(255),
	serviceCharge DECIMAL(10,2),
        serviceDate VARCHAR(255),
	CONSTRAINT PRIMARY KEY(serviceNo),
	CONSTRAINT FOREIGN KEY(customerId) REFERENCES customer(custId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE sellItemOrder(
	orderId VARCHAR(255) NOT NULL,
	customerId VARCHAR(255),
	orderDate VARCHAR(255),
	CONSTRAINT PRIMARY KEY(orderId),
	CONSTRAINT FOREIGN KEY(customerId) REFERENCES customer(custId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE sellItemDetail(
	orderId VARCHAR(255) NOT NULL,
	itemCode VARCHAR(255) NOT NULL,
	qty INT,
	unitPrice DECIMAL(10,2),
	CONSTRAINT PRIMARY KEY(orderId,itemCode),
	CONSTRAINT FOREIGN KEY(orderId) REFERENCES sellItemOrder(orderId) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT FOREIGN KEY(itemCode) REFERENCES sellItem(itemCode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE rentItemOrder(
	orderId VARCHAR(255) NOT NULL,
	customerId VARCHAR(255),
        orderDate VARCHAR(255),
	advancePayment DECIMAL(10,2),
	totalAmount DECIMAL(10,2),
	borrowDate VARCHAR(255),
	dueDate VARCHAR(255),
	rentStatus VARCHAR(255),
	CONSTRAINT PRIMARY KEY(orderId),
	CONSTRAINT FOREIGN KEY(customerId) REFERENCES customer(custId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE rentItemOrderDetail(
        orderId VARCHAR(255) NOT NULL,
	itemCode VARCHAR(255),
	qty INT,
        unitPrice DECIMAL(10,2),
	CONSTRAINT PRIMARY KEY(orderId,itemCode),
	CONSTRAINT FOREIGN KEY(orderId) REFERENCES rentItemOrder(orderId) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT FOREIGN KEY(itemCode) REFERENCES rentItem(itemCode) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE appoinment(
	appoinmentNo VARCHAR(255) NOT NULL,
	customerId VARCHAR(255),
	description VARCHAR(255),
	appoinmentDate VARCHAR(255),
	appoinmentTime VARCHAR(255),
	appoinmentStatus VARCHAR(255),
        amount DECIMAL(10,2),
	regDate VARCHAR(255),
	CONSTRAINT PRIMARY KEY(appoinmentNo),
	CONSTRAINT FOREIGN KEY(customerId) REFERENCES customer(custId) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE packages(
	packageId VARCHAR(255) NOT NULL,
	packageName VARCHAR(255),
	packagePrice DECIMAL(10,2),
	packageDescription TEXT,
	addedDate VARCHAR(255),
	modifiedDate VARCHAR(255),
	CONSTRAINT PRIMARY KEY(packageId)
);

CREATE TABLE soldPackages(
	soldPackageNo VARCHAR(255) NOT NULL,
	packageId VARCHAR(255),
        customerId VARCHAR(255),
	packagePrice DECIMAL(10,2),
	soldDate VARCHAR(255),
	CONSTRAINT PRIMARY KEY(soldPackageNo),
        CONSTRAINT FOREIGN KEY(customerId) REFERENCES customer(custId) ON UPDATE CASCADE ON DELETE CASCADE,
        CONSTRAINT FOREIGN KEY(packageId) REFERENCES packages(packageId) ON UPDATE CASCADE ON DELETE CASCADE
);
