# Changelog

Key: [+] added, [-] removed, [~] modified.

## 1.10.1

+ [~] Added missing permissions that weren't being checked, also added checks for overrides.

## 1.10.0

+ [+] Added [__mute__] command (voice and text) to mute users with an optional reason.
+ [+] Added [__unmute__] command to reverse a mute previous mute.
+ [+] Added a cool looking banner to the console. \:\^\)
+ [+] Added a usage argument for every command, accessed via the [__help__] command.
+ [+] Added a custom Utils module allowing for storage and shorted version of verbose methods.
+ [~] Changed [__bind__], [__exclude__], [__unbind__], [__unexclude__], [__delchannel__] to allow reduced arguments, using the channel the command is executed in as the parameter.
+ [~] Changed [__ban__] and [__kick__] to allow mentions as well as idLong to make it easier to moderate.
+ [~] Changed the [__user__] command to use mentions instead of trying to find them by username.
+ [~] Fixed oversight which broke YouTube/SoundCloud links.
+ [~] Changed the 'no track found' message from just the URL to an actual message.
+ [~] Rewrote README to use tables instead of just listing commands.

## 1.9.4

+ [~] Major /minor/ change to the way commands are handled.
+ [~] General clean up of unused code from previous versions.
+ [~] Replaced some exception throws with feedback instead to make it more useful.
+ [~] Added some overlooked functions from the execution timer.

## 1.9.3

+ [+] Added a cleanup function for when the bot is removed from a server.
+ [~] Remedied a problem that would only auto-skip a background track once.
+ [~] Changed some of the emotes used as some of them weren't working correctly.
+ [~] Updated dependencies to their latest versions at time of writing.
+ [~] Remedied the [__modules__] command having a couple of minor issues.
+ [~] More code shuffling to get more qualitative data in the console.
+ [~] Modified server join function to deal with if permission isn't given for messages in general.
+ [~] Finished the join SQL function. (Apparently I didn't finish it!)

## 1.9.2

+ [+] Prints into console when new server is joined (name, server id).
+ [-] Removed the 'fun' module, it was outdated and I can do better.
+ [~] Edited the README file to clarify potential misunderstandings with the default prefix.
+ [~] Remedied an unnecessary prefix for the roll command and updated the documentation accordingly.

## 1.9.1

+ [+] Added command execution time in milliseconds but with original resolution of nanoseconds.
+ [~] Changed the message delete procedure to only delete executed commands.
+ [~] Fixed a bug involving binding multiple channels and then using a command in one.
+ [~] Fixed a bug with the audio module that stopped playlists from working.
+ [~] Fixed an issue which only allowed YouTube track to be played and not from SoundCloud, etc.
+ [~] Altered the voice channel leave feature to work for all events instead of just leave.

## 1.9.0

+ [+] Added a welcome message to give users the commands and some other information.
+ [+] Added [__setprefix__] to allow admins to set their server command prefix.
+ [+] Added a bind/exclude system for all of the modules, allowing users to decide where the bot works.
+ [+] Added a function that makes the bot leave a voice channel if it is empty. (Nobody likes to be lonely! c:)
+ [~] Changed how the whole command prefix system works, default prefix is now the bot's name.
+ [~] Migrated from a set prefix to a global prefix.
+ [~] Redesigned the whole database schema to fit with the new bind/exclude system.
+ [~] Changed how the correct command is selected, making it exact and not /kinda/.
+ [~] Altered [__linestatus__] to account for a rare condition with uppercase text, also to use HTTPS.
+ [~] Altered [__play__] embed to display correctly if no image is found.
+ [~] Major restructure of project packages, refactored music into audio and runescape into games.
+ [~] Refactoring some of the audio classes to make them more efficient, added some error messages.
+ [~] Dealt with a YouTube search bug that throws exceptions instead of intended functionality.
+ [~] Changed return type of executeCommand from boolean to void to avoid jumping through hoops.
+ [~] Updated the database to comply with refactoring and removed the dev setting.
+ [~] Changed database from H2 to MySQL for production.
+ [~] Split the database class into a connection class and a function class.
+ [~] Resolved the oldest database 'bug' I've had which turned out to be a WHERE clause overlook. *doh!*
+ [~] Fixed the [__modules__] command, issue was a late side effect of the many refactors.
+ [~] Resolved issue with [__search__] where a search would return less than 10 results.
+ [-] Became headless, as a result removed the Monitor class as it isn't used.
+ [-] Removed custom module because it wasn't being used and it was a pain to maintain.
+ [-] Stopped playing with lottie's tits. (Unset the default status)

## 1.8.2

+ [~] Changed the structure of [__currenttrack__], [__lasttrack__] and [__play__] to give a little more info.
+ [~] Altered the Monitor and TimeKeeper to reduce rate limiting.
+ [~] Fixed [__lasttrack__] to actually show the last track and not the current.
+ [~] Attempted to fix an uncommon bug where a track is queued as another finishes.
+ [~] Fixed [__search__] cancelling not actually working.

## 1.8.1

+ [~] Refactored UserInterface to Monitor since I only use it to monitor...
+ [~] Changed field names in class M to match those of class C.
+ [-] Removed obsolete end of constructor warnings for modules.

## 1.8.0

+ [+] Added search command for music. [__search__]
+ [+] Added about command. [__about__]
+ [+] Added a check to the [__linestatus__] command that allows for a minified version 
+ [~] Changed the [__linestatus__] command to add more details for services that are not 'good'.
+ [~] Another large refactor for modules similar to that of the commands refactor.
+ [~] Rewrite of the controller; module selection down from 120 lines to 9. (Yes, 9)

## 1.7.0

+ [+] Added a notice a the bottom of the README.md concerning a verified voice bug.
+ [+] Added developer only command to change presence, finished refactor.
+ [~] Moved all hardcoded configurations to program arguments. [args[]]
+ [~] Changed [__linestatus__] embed format to inline fields, also added support for multiple statuses.
+ [~] Heavily modified the TFL classes, removing roughly 90% since they were not being used.
+ [~] Resolved the rest of the compiler warning I felt needed attention.

## 1.6.0

+ [+] TFL London Tube service status command. [__linestatus__]
+ [+] Last track command to retrieve the last played track. [__lasttrack__]
+ [+] Repeat command to repeat the watermelon song over and over. [__togglerepeat__]
+ [~] Included user's display picture in the [__user__] command embed thumbnail.
+ [~] Resolved a ton of compiler warnings, including all performance warnings.

## 1.5.0

+ [+] Added Changelog to actually keep track of what has happened throughout versions. 

 