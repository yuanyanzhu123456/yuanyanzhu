# Prequired

- JDK 1.8
- Redis
- MySQL
- Maven


# How To Setup Develop environment


## Install MySQL

 in CentOS 7

      yum install -y mysql
      #or
      yum install -y mariadb*

in macos

     brew install mysql


## Install some libs

    mvn install 


## Create Database on MySQL

       CREATE DATABASE `rcs2` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

       # create user
       CREATE USER 'rcs'@'localhost' IDENTIFIED BY 'GEOrcs,,123';
       GRANT ALL ON rcs2.* TO 'rcs'@'localhost';

       CREATE USER 'rcs'@'%' IDENTIFIED BY 'GEOrcs,,123';
       GRANT ALL ON rcs2.* TO 'rcs'@'%';

## Config mysql settings

       1、Set  GROUP BY clause（Cause Rcs Event api error.）
       
       [mysqld]
       sql-mode="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION"
       
## Config redis

        password: GEOrcs,,c5d82c4e

# Program Dir

# todo