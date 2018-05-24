# Changelog

Key: [+] added, [-] removed, [~] modified.

## 1.8.1

+ [~] Refactored UserInterface to Monitor since I only use it to monitor...
+ [~] Changed field names in class M to match those of class C.
+ [-] Removed obsolete end of constructor warnings for modules.

## 1.8.0

+ [+] Added search command for music. [search]
+ [+] Added about command. [about]
+ [+] Added a check to the [linestatus] command that allows for a minified version 
+ [~] Changed the [linestatus] command to add more details for services that are not 'good'.
+ [~] Another large refactor for modules similar to that of the commands refactor.
+ [~] Rewrite of the controller; module selection down from 120 lines to 9. (Yes, 9)

## 1.7.0

+ [+] Added a notice a the bottom of the README.md concerning a verified voice bug.
+ [+] Added developer only command to change presence, finished refactor.
+ [~] Moved all hardcoded configurations to program arguments. [args[]]
+ [~] Changed [linestatus] embed format to inline fields, also added support for multiple statuses.
+ [~] Heavily modified the TFL classes, removing roughly 90% since they were not being used.
+ [~] Resolved the rest of the compiler warning I felt needed attention.

## 1.6.0

+ [+] TFL London Tube service status command. [linestatus]
+ [+] Last track command to retrieve the last played track. [lasttrack]
+ [+] Repeat command to repeat the watermelon song over and over. [togglerepeat]
+ [~] Included user's display picture in the [user] command embed thumbnail.
+ [~] Resolved a ton of compiler warnings, including all performance warnings.

## 1.5.0

+ [+] Added Changelog to actually keep track of what has happened throughout versions. 

 