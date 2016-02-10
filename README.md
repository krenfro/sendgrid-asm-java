sendgrid-asm-java
=================

[SendGrid](https://sendgrid.com/) Advanced Suppression Manager (ASM) Java Client

Manages global and group based e-mail suppressions using the [SendGrid Advanced Suppression Manager](https://sendgrid.com/docs/API_Reference/Web_API_v3/Advanced_Suppression_Manager/index.html).  This library does *not* send e-mail.  This library is a stop-gap until ASM features are added to the [SendGrid Java Client](https://github.com/sendgrid/sendgrid-java).



## Dependencies

Jackson for JSON parsing

Apache HttpClient

Apache Fluent HttpClient

## Maven
```
<dependency>
    <groupId>com.github.krenfro</groupId>
    <artifactId>sendgrid-asm-java</artifactId>
    <version>0.0.1</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.3.6</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>fluent-hc</artifactId>
    <version>4.3.6</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.4.0</version>
</dependency>
```

## Gradle
```
dependencies {
    compile "com.github.krenfro:sendgrid-asm-java:0.0.1"
    compile "org.apache.httpcomponents:httpclient:4.3.6"
    compile "org.apache.httpcomponents:fluent-hc:4.3.6"
    compile "com.fasterxml.jackson.core:jackson-databind:2.4.0"
}
```


#Usage

Classes can be loaded using either an API Key, or your username and password.

##Suppressions
```java
SuppressionManager suppressionMgr = new SuppressionManager("apiKey"); // or:
//SuppressionManager suppressionMgr = new SuppressionManager("username", "password");

//get Suppression for a particular group and user
Suppression suppression = suppressionMgr.retrieve(42, "email");

//if you already have a Group, you can use that instead
suppression = suppressionMgr.retrieve(group, "email");

if (suppression.isSuppressed()){
	System.out.println(suppression.getDescription());
}

//add suppressions
suppressionMgr.add(group, "email");
suppressionMgr.add(group, "email", "email2", "email3");

//remove a suppression
suppressionMgr.remove(group, "email");
suppressionMgr.remove(group, "email", "email2", "email3");

//get list of all Suppressions, one for each suppression group
List<Suppression> suppressions = suppressionMgr.retrieve("email");

//selectively save all of them
suppressionManager.save("email", suppressions);

//get list of all e-mails suppressed in a group
List<String> emails = suppressionMgr.retrieve(group);

```


##Groups
```java
GroupManager groupMgr = new GroupManager("apiKey"); // or:
//GroupManager groupMgr = new GroupManager("username", "password");

//retrieve all groups
List<Group> groups = groupMgr.retrieve();

//add a new group
Group group = groupMgr.add("group name", "group description");

//retrieve by id
group = groupMgr.retrieve(42);

//remove groups
groupMgr.remove(group);

```

##Global Suppressions
```java
GlobalSuppressionManager globalMgr = new GlobalSuppressionManager("apiKey"); // or:
//GlobalSuppressionManager globalMgr = new GlobalSuppressionManager("username", "password");

if (globalMgr.has("email")){
	//do something
}

globalMgr.add("email");
globalMgr.remove("email");

//you can add and remove multiple
globalMgr.add("email", "email2", "email3");
globalMgr.remove("email", "email2", "email3");


```

# Tests
Integration tests run against an actual SendGrid account with either form of credentials:
```
mvn -Dsendgrid-username=ABC -Dsendgrid-password=XXXXX clean test
mvn -Dsendgrid-apikey=XXXXX clean test
```
