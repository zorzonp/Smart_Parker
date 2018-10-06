# Smart_Parker
This is a project for ECE601. The goal of the project is to use image recognition to determine license plates as cars enter and exit a parking lot and charge the owner accordingly. 

# How To:

## DESCRIPTION:

Our server consists of an Amazon Web Service(AWS) Virtual Private Cloud (VPC), EC2 Instance, and a MySQL Database on an Amazon RDS instance. The RDS instance is on a private subnet that can only be accessed from the EC2 intance. The EC2 instance can be accedd by the public via the web or via SSH from approved IP addresses. 

To create the AWS VPC I used a tutorial found on Amazon(See Sources 1).


## SOURCES:
1) https://aws.amazon.com/getting-started/tutorials/create-mysql-db/


## ACCESS EC2 INSTANCE:
### SSH: 
	ssh -i "<key_pair_file>" ec2-user@ec2-18-217-234-17.us-east-2.compute.amazonaws.com
	ssh -i "smart-parker-key-pair.pem" ec2-user@ec2-18-217-234-17.us-east-2.compute.amazonaws.com
### HTTP:
	ec2-18-217-234-17.us-east-2.compute.amazonaws.com

Visiting the Website via HTTP will display a tempoary test HTML page 


## ACCESS DB INSTANCE:
	Must be signed into the EC2 instance.
	mysql -h smart-parker-db-instance.cdcizhetgsdg.us-east-2.rds.amazonaws.com -P 3306 -u smartparker_user -p


## SETUP:

### Install MySQL on EC2 Instance
	sudo yum install mysql56-server.x86_64

### Add IP to known list:
   https://checkip.amazonaws.com

# Other Info: 
users table 

+---------------+-------------+------+-----+---------+-------+

| Field         | Type        | Null | Key | Default | Extra |

+---------------+-------------+------+-----+---------+-------+

| username      | varchar(20) | NO   | PRI | NULL    |       |

| first_name    | varchar(20) | YES  |     | NULL    |       |

| last_name     | varchar(20) | YES  |     | NULL    |       |

| password      | char(100)   | YES  |     | NULL    |       |

| salt          | char(10)    | YES  |     | NULL    |       |

| license_num   | varchar(7)  | YES  |     | NULL    |       |

| license_state | varchar(2)  | YES  |     | NULL    |       |

| make          | varchar(10) | YES  |     | NULL    |       |

| modle         | varchar(20) | YES  |     | NULL    |       |

| year          | int(11)     | YES  |     | NULL    |       |

| color         | varchar(25) | YES  |     | NULL    |       |

| venmo_uname   | varchar(20) | YES  |     | NULL    |       |

+---------------+-------------+------+-----+---------+-------+

owners table

+----------+--------------+------+-----+---------+-------+

| Field    | Type         | Null | Key | Default | Extra |

+----------+--------------+------+-----+---------+-------+

| username | varchar(20)  | NO   | PRI | NULL    |       |

| salt     | char(10)     | NO   |     | NULL    |       |

| password | varchar(100) | NO   |     | NULL    |       |

+----------+--------------+------+-----+---------+-------+

