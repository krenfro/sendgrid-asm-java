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

```


#Groups
```java
GroupManager groupMgr = new GroupManager("username", "password");

List<Group> groups = groupMgr.retrieve();

Group group = groupMgr.add("group name", "group description");

//retrieve by id
group = groupMgr.retrieve(42);

groupMgr.remove(group);

//or by id
groupMgr.remove(42);

```

#Global Suppressions
```java
GlobalSuppressionManager globalMgr = new GlobalSuppressionManager("username", "password");
if (globalMgr.exists("email")){
	//do something
}

globalMgr.add("email");
globalMgr.remove("email");

//you can add and remove multiple
globalMgr.add("email", "email2", "email3");
globalMgr.remove("email", "email2", "email3");


```