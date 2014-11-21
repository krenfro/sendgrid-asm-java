sendgrid-asm-java
=================

SendGrid Advanced Suppression Manager (ASM) Java Client

Java client for the SendGrid v3 Web API Suppression features.

#Suppressions
```java
SuppressionManager suppressionMgr = new SuppressionManager("username","password");

//get Suppression for a particular user and group id.
Suppression suppression = suppressionMgr.retrieve(42, "email");

//if you already have a Group, you can use that instead
suppression = suppressionMgr.retrieve(group, "email");

if (suppression.isSuppressed()){
	System.out.println(suppression.getDescription());
}

//remove a suppression
suppressionMgr.remove(group, "email");

//get list of all Suppressions, one for each suppression group
List<Suppression> suppressions = suppressionMgr.retrieve("email");

//change them
for (Suppression suppression: suppressions){
	suppression.setSuppressed(false);
}

//save them
suppressionManager.save("email", suppressions);

```


#Groups
```java
GroupManager groupMgr = new GroupManager("username", "password");

//retrieve all defined groups
List<Group> groups = groupMgr.retrieve();

//add a new group
Group group = groupMgr.add("group name", "group description");

//retrieve a group by id
group = groupMgr.retrieve(42);

//remove a group
groupMgr.remove(group);

```

#Global Suppressions
```java
GlobalSuppressionManager globalMgr = new GlobalSuppressionManager("username", "password");
if (globalMgr.exists("email")){
	//do something
}

globalMgr.add("email");
globalMgr.remove("email");

//you can add and remove multiple e-mails
globalMgr.add("email", "email2", ..., "emailN");
globalMgr.remove("email", "email2", ..., "emailN");


```