STD - Backend
=========

## Dependency Stack

The following table is a list of the major dependencies we use along with their purpose and links to supplemental documentation.

| **Dependency**         | **Purpose**                                                                 | **Docs**          |
| :--------------------- | :-------------------------------------------------------------------------- | :---------------- |
| Java - JDK 8           | Project programing language | [Official Docs](https://docs.oracle.com/javase/8/docs/) |
| Maven 3.3.9            | Management tool to build the project | [Official Docs](https://maven.apache.org/docs/3.3.9/release-notes.html)|
| Spring Framework 4     | Framework to manage the mvc architecture | [Official Docs](https://docs.spring.io/spring/docs/4.0.x/spring-framework-reference/html/)|
| Hibernate Framework 4  | ORM to manage database connection mapping | [Official Docs](https://hibernate.org/orm/documentation/4.3/)|

## Files Structure
    .                   
    ├── src  
    │   └── main
    │       └── java
    │           └── pe.gob.congreso
    │               └── configuration/
    │               └── controllers/
    │               └── crypto/
    │               └── dao
    │                   └── impl/
    │               └── model
    │                   └── util/
    │               └── pkiep.invoker
    │                   └── controller.servlet
    │                   └── util
    │               └── service
    │                   └── impl/
    │               └── util/
    │               └── vo/
    │       └── resources
    │           └── application.properties
    │           └── default.properties
    │           └── ehcache.xml
    │           └── log4j.properties
    │       └── webapp              
    │           └── assets/
    │           └── WEB-INF
    │               └── views/
    │               └── jboss-web.xml                    
    ├── pom.xml             
    └── README.md

## Getting Started

### Prerequisites
##### JDK 8
You can get the complete guide about installation on [java.com](https://java.com/en/download/help/windows_manual_download.xml) page.

##### Maven 3.3.9
You can get the complete guide about installation on [Apache Maven](https://maven.apache.org/install.html) page.

##### WildFly 10
You can get the complete guide about installation on [JBoss](https://docs.jboss.org/author/display/WFLY10/Getting+Started+Guide#GettingStartedGuide-Installation) page.

###  Installation

##### Cloning the project

```bash
git clone https://svr-gitlab.congreso.net/GIT/std.git

cd std/std/
```

##### Installing dependencies

```bash
mvn install
```

We are using this command for install dependencies with the version specified on `pom.xml`.

##### Installing JDC driver 4.2

- Download the driver for the [Microsoft Official](https://www.microsoft.com/en-us/download/details.aspx?id=54671) page.
- Copy the .jar file to some path that you prefer. e.g. C:\sqljdbc4-4.2.jar
- Then execute:  

```bash
mvn install:install-file -Dfile=C:\sqljdbc4-4.2.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar -DgeneratePom=true
```

##### Installing LDAPConnection library

- Download the driver for the [GitLab Repository](https://svr-gitlab/) page.
- Copy the .jar file to some path that you prefer. e.g. C:\LDAPConnection.jar
- Then execute:  

```bash
mvn install:install-file -Dfile=C:\LDAPConnection.jar -DgroupId=pe.gob.congreso -DartifactId=LDAPConnection -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
```

##### Generate .war

```bash
mvn clean package
```
