# Changelog

Key: [+] added, [-] removed, [~] modified, [!] important.

## Y-1.1.0 (The Big Refactor)
+ [!] Removed the Yuuko class self event manager and event code, creating a dedicated events package to put in it's place.
+ [+] Added the Interaction module which as the name suggests adds interactions.
+ [+] Added interactions: [__poke__], [__hug__], [__attack__], [__bite__], [__angry__], [__cry__], [__laugh__], [__pout__], [__shrug__], [__sleep__], [__tickle__].
+ [+] Added a simple [__choice__] command which takes an undefiled number of parameters separated by commas and selects one of the choices.
+ [~] Improved the [__roll__] command to give a better indication on what the roll is against.
+ [~] Refactored Math module into Fun module to allow for a wider range of commands to be encompassed such as coin flips and other such commands.
+ [~] Refactored utils package to utilities to actively try and make things more complete and professional.
+ [~] Refactored the Statistics class to Metrics since they're the latter and not the former.
+ [~] Refactored the scheduled events from the main class to their own scheduler package based on Avaire which is 1000% better.
+ [~] Refactored database connections into their own package, and also started sending metrics to a separate server. (including commands)
+ [~] Refactored both the Module and Command superclasses, changing method names and also changing the module field from String to Class<?> to improve efficiency all around.
+ [~] Refactored every command and module for a better naming convention overall.
+ [~] Refactored methods from the Utils class to their own respective MessageUtility and TextUtility classes.
+ [~] Fixed a bunch of new and old bugs that have been discovered since the last release.
+ [-] Removed M and C classes, instead using the Reflections package to improve command modularity greatly.

## Y-1.0.2
+ [~] General code premature-optimisations here and there, mostly conversions from verbose loops to streams.
+ [~] Upgraded the [__nuke__] command to conform with the new standard which was overlooked in Y-1.0.1.
+ [~] Slightly optimised both the CommandExecutor and the GenericMessageController classes.
+ [~] Greatly improved the nuking speed of messages older than 2 weeks. (In preliminary testing, more testing needed in real world.)
+ [~] Increased [__nuke__] command's max channels from `5` to `10` since the lower bound was the victim of some premature optimisations.
+ [~] Moved dev checking from CommandExecutor into the ModuleDeveloper class itself to save some precious nanoseconds.
+ [~] Updated the [__user__] command to improve code quality and include some rich presence information where possible.
+ [~] Updated the [__queue__] command to improve code quality and show some additional information about the queue such as total length and items in the queue. (much at the dismay to a certain man-child) 
+ [~] Updated dependencies to their latest versions and removed obsolete dependencies.

## Y-1.0.1
+ [~] Refactored Sanitise to Sanitiser for peace of mind.
+ [~] Refactored isNumber() from Utils to Sanitiser.
+ [~] Updated [__osu__], removing signature image because of hassle and processing time, also removing POJO classes in exchange for the easier implementation added in Y-1.0.0.
+ [~] Updated the [__mute__], [__kick__] and [__ban__] commands to correctly interpret messages, improve feedback and not throw unnecessary exceptions.
+ [~] Upgraded the code quality of the [__mute__] command in particular, giving it a long overdue lambda overhaul, setting a new standard for code on the bot.

## Y-1.0.0 (The Re-brand)
+ [!] Completely rebranded the bot into something more personable instead of a random anecdotal name that only a few people will understand.
+ [!] Bumped JDK version to 12 (early release) because why wouldn't you live life dangerously in production. (also, jdk 11 ssl is broken)
+ [+] Put in place measures to allow for a web console to edit settings and also see status updates.
+ [+] Added developer [__reloadapi__] command to be able to switch out API keys when needed.
+ [+] Added new [__tesco__] command that allows you to lookup and price check items from the store (kind of a meme)
+ [+] Added new [__welcomeMembers__] setting that currently sends a message when a new member joins and greets them.
+ [+] New database stuff including join date, command logs (for debugging), lastUpdated (for debugging) and a few other minor things.
+ [~] Modified the JsonBuffer class to use Gson and return JsonObject objects, allowing for cleaner code, also removing any previous POJO objects.
+ [~] Modified Permission field in the Command class to be an array, allowing multiple permissions per command, modified related classes to accommodate for the change.
+ [~] Heavily modified the database logic, tidied it up a bunch and now use pooled connections to increase database speeds. (12-20ms to 2-8ms @ about)
+ [~] Heavily modified the API system, now allowing API keys to be reloaded and added on the fly.
+ [~] Modified the [__nuke__] command. You can now tag channels instead of a value and those channels will be duplicated and the old versions deleted.
+ [~] Updated the system clock to keep time in a more meaningful way that can be used and isn't pre-formatted.
+ [~] Updated exception util to print the full stack trace so that debugging can be done more efficiently.
+ [~] Updated JsonBuffer class to allow for extra additional headers.
+ [~] Shrunk the [__announceNowPlaying__] setting to simply [__nowPlaying__] because it's easier to type and remember.
+ [~] Fixed a number of old bugs the haunted the previous versions, obviously leaving a few behind (x3).
+ [~] Cleaned up a LOT of the code, making it more readable and less redundant, moved Ping into Statistics from Cache, changed consoleOutput function to show more commands but less other information.
+ [~] Moved duplicate code from the Moderation module into it's own method in the Utils class.
+ [~] Updated the about section since everyone already knows the context of a discord bot...
+ [~] Changed most of the embeds to include information about who used the command and bot version.
+ [~] Increased complexity of the CommandExecutor class to take burden from other classes such as GenericMessageController and remove duplicate code.
+ [~] Ensured that the M and C classes are reflection safe to stop exceptions & complaints at runtime.
+ [-] Removed SystemClock class and replaced it with a ScheduledExecutor in the Yuuko class and increased update time to every 1 second. (takes up only 7 lines total)
+ [-] Removed documentation about the [__setup__] command since people keep using it despite the bot already being setup and kicking it when it fails.
+ [-] Finally removed the [__unmute__] command, combining it with the [__mute__] command as a toggle.
+ [-] Removed [__exclusion__] command because it isn't used compared to the superior bind.

## 3.4.0
+ [+] Introduced the profile module which is going to be the bases of the bots new level system and user commands. (Incubating)
+ [+] Added [__djMode__] setting which allows servers to lock down key audio commands to those with the DJ role.
+ [~] Edited the [__kitsu__] command so that the default functionality is show so users no longer need to type "show" as a parameter.
+ [-] Removed [__runescape__] command since it isn't used and hasn't actually worked correctly for a while and just degrades the quality of the bot.
+ [-] Removed [__sum__] command since it was just as poor as the runescape command and wasn't used anyway, who doesn't have a calculator?
+ [-] Removed [__wow__] command for the same reason as the other two and it was a huge undertaking just to have it work correctly.

## 3.3.1
+ [~] Fixed some minor bugs in track scheduler and settings classes.
+ [~] Updated NSFW neko command api after it was recently changed and caused the command to stop working. Fix should be much more robust.

## 3.3.0
+ [+] Added [__announceNowPlaying__] setting which allows users to set whether or not the next track is announced.
+ [~] Refactored all boolean based settings into a single class using parameters instead.
+ [~] Altered how database values are represented on the output, changed from camel case to uppercase.

## 3.2.2
+ [+] Added cache to store miscellaneous pieces of data, starting to refactor the main class to decrease the complexity.
+ [~] Added missing information from some of the commands.
+ [~] Fixed problem where embed author doesn't allow markdown. (changed to title)
+ [~] Removed bold text from majority of output, leaving it in only for distinguishing values.
+ [~] Altered audio handlers to hopefully fix a long term issue without things breaking.

## 3.2.1
+ [~] Modified all output and converted them into embeds since they look much 'richer'.
+ [~] Refactored text related methods from the Utils class and moved them to their own MessageHandler class.
+ [~] Updated the checking involved with the audio module, not allowing commands while not connected or allowing commands such as queue unless the bot is connected too.

## 3.2.0
+ [+] Added NSFW [__neko__] command from neko.life, because that's just what some people like I guess.
+ [+] Created CommandExecutor class to be the one stop shot for all command executions so they're guaranteed to be the same.
+ [+] Created SystemVariables class to store certain command variables such as users who have started a search. (soon to be cache)
+ [~] Completely overhauled the command/module system, shrinking the current line count by 6%. (400-ish lines)
+ [~] Refactored ModuleTransport into ModuleWorld and shifted the weather command over to that module.
+ [~] Overhauled the help command to actually be helpful, which is helpful.
+ [~] Updated server command to include the server emotes (up to 1024 chars).
+ [~] Changed formatting for a lot of the commands to make them look somewhat nicer.
+ [~] Altered the way commands are checked, prefixes and commands can be capitalised however possible now and it'll still work fine.
+ [~] Shrunk the play command down to make it more consistent for regular plays or through searching. (possible due to overhaul)
+ [-] Removed the logging module and merged it into the settings command.

## 3.1.0
+ [+] Added [__settings__] command... yep, there are finally server settings that you can toggle on/off, with the first being if the command message is deleted.
+ [+] Added more shiny badges to the README.md part of the documentation.
+ [+] Added a sanitiser utility class to check parameters of commands to ensure they meet the minimum number to correctly execute. (stops a lot of exceptions...)
+ [~] Refactor [__togglerepeat__] to [__repeat__] because it is shorter, easier to type and simply a better alias for the command.
+ [~] Refactor [__setbackground__] to simply [__background__] which is shorter and easier to remember with the former being unnecessarily long.
+ [~] Changed class names for the commands changed in `2.2.0` to reflect their new command
+ [~] Finished the remaining exceptions so that they're all uniform and will send information to the correct place.
+ [~] Altered the output of the console to happen once every 5 seconds instead of when message/react events are intercepted.
+ [~] Fully altered the database to prevent SQL injections, since I wasn't happy with the previous solution. (kinda)
+ [~] Changed all of the non-informative embeds to a grey colour since it doesn't blind you with brightness. (hopefully)
+ [-] Removed [__unsetbackground__] and merged it into setbackground so it acts as a toggle instead of having 2 commands.
+ [-] Removed [__setprefix__] and merged it into the settings command, also removing associated database functions.

## 3.0.1
+ [~] Further added to the exception message to get concise and valuable data.
+ [~] Added more checks to stop bot actions wasting CPU cycles.
+ [~] Minor streamlining in the main class, very very slightly reducing size.
+ [~] Updated dependencies, removing some of the obsolete packages that were still being packaged with the bot, reducing overall size by roughly 400kb.

## 3.0.0
+ [+] Upgraded project to Java 11 since Java 10 has just become obsolete.
+ [+] Added [__addservers__] dev command to quickly re-add all of the servers to the database in the event of a data loss.
+ [+] Added SystemInformation class to store things such as ping, guild count, user count, message processed to prevent unnecessary REST calls.
+ [+] Added [__weather__] command which allows users to return information about the weather in the given locale.
+ [+] Added [__kitsu__] command which currently allows users to search up and return information about different animes.
+ [~] Moved from a docker container of MySQL to an installation on the OS, which is much quicker.
+ [~] Transitioned from setting all api keys and such as arguments to the .jar to a much easier to ready configuration file.
+ [~] Edited formatting of module disabled message to give the user a better understand of which module it is.
+ [~] Made GenericMessageController more efficient by doing a bot check immediately instead of after a database call.
+ [~] Moved all System.out.println(); calls to a Utils function to make things easier to handle uniformly.
+ [~] Edited the [__efukt__] command to remove an enormous description with every command.
+ [~] Moved JsonBuffer from Utils to it's own class and package for related things.
+ [~] Renamed TimeKeeper to SystemClock and removed some of the stuff unrelated to system uptime such as ping.
+ [~] Refactored the game module into what is now a media module to allow for the new anime command and others in the future.
+ [~] Increased the number of try/catches to help with further debugging.
+ [~] Changed error messages from being sent in the console to being sent into the discord support server.
+ [-] Removed [__unbind__] command and merged it into the bind command to reduce code duplication.
+ [-] Removed [__unexclude__] command and merged it into the exclude command to reduce code duplication. (haha)
+ [-] Removed [__delchannel__] command and merged it into the addchannel command to reduce code duplication. (sigh)

## 2.2.1
+ [~] Changed/Updated depreciated code to fit in with current versions.
+ [~] Updated dependencies to their latest versions to keep on to of everything.
+ [~] Cleaned up some of the code to make it slightly more readable.

## 2.2.0
+ [+] Added [__osu__] command, which allows the user to retrieve stats from either of the 4 modes.
+ [+] Added a shiny new badge to README.md (yay)
+ [~] Refactored [__currenttrack__] to [__current__] for ease of use.
+ [~] Refactored [__lasttrack__] to [__last__] for ease of use.
+ [~] Refactored [__clearqueue__] to [__clear__] for ease of use.
+ [~] Refactored [__wowcharacter__] to [__wow__] for ease of use.
+ [~] Refactored [__rsstats__] to [__runescape__] for ease of use.
+ [~] Refactored World of Warcraft class to reflect changed.
+ [~] Changed unicode to pasted emoji for some reason.

## 2.1.1
+ [+] Added try/catch for message sending so things don't break if no write permission is given.
+ [~] Edited a lot of the documentation to make the bot easier to use in the initial setup phase.
+ [~] Changed the default custom prefix from nothing to `-` to aid in the initial setup and ease of use.
+ [~] Enhanced the console prints to give more valuable information about where things are going wrong.
+ [~] Added a check to see and report to the user if the bot has the necessary permissions to work correctly. x2
+ [~] Updated the [__user__] command to conform with nicer looking style format.
+ [~] Fixed discordbots.org integration issue so it updates correctly.

## 2.1.0
+ [+] Added NSFW module with an [__efukt__] command, forcing me to adopt Jsoup which is actually pretty good. 
+ [+] Added [__clearqueue__] command to easily clear the queue without having to remove and recall the bot.
+ [+] Added [__wowcharacter__] command to allow character lookup in the format of 'realm -> character'
+ [~] Updated dependencies to their latest respective versions.
+ [~] Updated the help command and the join message to encourage users to join the support server if something goes wrong.
+ [~] Updated the [__clearqueue__] command to allow for an optional parameter that clears a single track anywhere in the queue.
+ [~] Updated the [__server__] command to conform with other similar commands.

## 2.0.0

+ [+] Forced into 2.0.0 due to classpath change from e.g. yuuko.core -\> com.yuuko.core
+ [+] Added utils package to help break up the various utility classes.
+ [~] Separated the controller class into separate generic event classes to isolate things a bit more.
+ [~] Added missing permissions that weren't being checked, also added checks for overrides.
+ [~] Finished and integrated the [__mute__] and [__unmute__] commands.

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

 