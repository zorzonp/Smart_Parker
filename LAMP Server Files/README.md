# Smart_Parker
This is a project for ECE601. The goal of the project is to use image recognition to determine license plates as cars enter and exit a parking lot and charge the owner accordingly. 

# How To:

## DESCRIPTION:

Our server consists of an Amazon Web Service (AWS) Virtual Private Cloud (VPC), EC2 Instance, and a MySQL Database on an Amazon RDS instance. The RDS instance is on a private subnet that can only be accessed from the EC2 instance. The EC2 instance can be accessed by the public via the web or via SSH from approved IP addresses. 

To create the AWS VPC I used a tutorial found on Amazon (See Sources 1).


## SOURCES:
1) https://aws.amazon.com/getting-started/tutorials/create-mysql-db/ 


## ACCESS EC2 INSTANCE:
### SSH: 
	ssh -i "<key_pair_file>" ec2-user@smartparker.cf
	ssh -i ssh -i "smart-parker-key-pair.pem" ec2-user@smartparker.cf
### HTTPS:
	https://smartparker.cf

Visiting the Website via HTTPS will display our homepage. We have created a basic webpage using HTML and CSS. Here we detail the technologies we used and the process for 
signing up and using our service.


## ACCESS DB INSTANCE:
	1) To access the MySQL DB directly you must be signed into the EC2 instance.
	2) Enter the command: mysql -h smart-parker-db-instance.cdcizhetgsdg.us-east-2.rds.amazonaws.com -P 3306 -u smartparker_user -p
	3) Enter the password. You are now signed into the MySQL console and can give it MySQL commands


## SETUP:

### Install MySQL on EC2 Instance
	sudo yum install mysql56-server.x86_64

### Add IP to known list:
  1) Get your IP: https://checkip.amazonaws.com
  2) Log into the AWS console: https://us-east-2.console.aws.amazon.com/console/home?region=us-east-2
  3) Navigate to the EC2 Instance
  4) Click the Security Groups menu
  5) Select smart_parker-securitygroup
  6) Update or add a new SSH type to the inbound tab using the IP address from step 1


