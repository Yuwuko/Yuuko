# Changelog

Key: [+] added, [-] removed, [~] modified, [!] important.

## 2019-06-10
+ [~] Updated dependencies to newest possible without breaking other dependencies @JDA && OkHttp
+ [~] Refactored ApplicationProgrammingInterface to simply Api and put it into its own package along with an ApiManager class.
+ [~] Refactored RequestHandler from the utilities package to its own IO package along with RequestProperty
+ [~] Refactored MessageEvent package from extension to entity.

## 2019-06-06
+ [+] Added an audio timeout feature which will make Yuuko leave an audio channel after 5 minutes of nothing being played.
+ [~] Refactored all of the audio commands to remove some unnecessary method calls.
+ [~] Refactored JsonBuffer to RequestHandler since it doesn't necessarily return Json.
+ [~] Refactored Metrics packages to make more sense.
+ [~] Took care of some of the long term warnings IntelliJ was throwing at me.
+ [~] Rewrote the [__efukt__] command after further analysing some html source, appearing to fix a malformed URL bug.

## 2019-05-29
+ [!] Refactored the core GenericMessageController to GenericGuildMessageController to stop Yuuko listening to non-guild messages.
+ [!] Refactored the core GenericMessageReactionController to GenericGuildReactionController to stop Yuuko listening to non-guild reacts.
+ [~] Changed commandlog to comlog on the backend to match with the modlog setting.
+ [~] Updated multiple methods parameters to reflect an old change that didn't previously need modification.

## 2019-05-23
+ [~] Modified the about command to include the 5 latest commits from GitHub to show progress.
+ [~] Gave some of the core commands a visual revamp, commands that have been improved are [__about__], [__help__], [__settings__] and [__shards__]
+ [~] Improved the starboard setting to list the number of stars that the message has received. x2
+ [~] Fixed a legacy bug which caused the [__nuke__] command to remove 1 less message than expected.
+ [~] Plugged up the final okhttp leak which was caused by when things went wrong and didn't close properly.
+ [-] Depreciated the legacy :pinpush: pinning feature for the starboard setting. (they're functionally the same thing)

## 2019-05-20
+ [~] Added command execution time to metrics for extra stats. (Ooo)
+ [~] Fixed reoccurring bug involving case sensitivity and commands.
+ [~] Fixed bug which would cause exceptions when using the play command while not in an audio channel.

## 2019-05-18
+ [~] Modified how the [__urban__] command is categorised, making it nsfw.
+ [~] Refactored string formatter into appropriate package.
+ [~] Converted most of the string arrays (modules, commands, permissions) to lists, in most cases being more efficient.

## 2019-05-16
+ [~] Updated [__github__], [__kitsu__] and [__osu__] commands to experiment with new formatting.
+ [~] Added new experimental String formatter to avoid multiple method calls for formatting strings.
+ [~] Updated dependencies and updated code to prevent potential okhttp leaking.

## 2019-05-15
+ [+] Added [__dance__] interaction command as request by YuukoKanoe#2043
+ [+] Added [__pat__] interaction command as request by YuukoKanoe#2043
+ [+] Added [__kill__] interaction command as request by YuukoKanoe#2043

## 2019-05-14
+ [~] Refactored packages for DatabaseConnections and DatabaseFunctions.
+ [~] Updated required permissions for [__reactrole__] to require MESSAGE_HISTORY.

## 2019-05-13
+ [!] Switched version formatting to have the most significant number first.
+ [~] Refactored some of the newest code to be less verbose.
+ [~] Added guards for null pointer exceptions where reacts come but the message is somehow null.

## 11-05-2019
+ [+] Added react role database functions and event hooks.
+ [~] Finished [__reactrole__] command main code and guards.

## 08-05-2019
+ [~] Updated [__urban__] command to present the data in a better less intrusive way.
+ [~] Updated documentation to match the current state of Yuuko.
+ [+] Added base code for the [__reactrole__] command and feature in general.

## 30-04-2019
+ [~] Updated [__syncguilds__] to purge guilds from the database which are failed to sync within 24 hours (indicates redundant data)
+ [~] Refactored [__londonunderground__] to just [__underground__] for pretty obvious reasons. (invocation was too long)
+ [~] Who uses the main class anyway? (moved the remaining config code out of the main class)

## 29-04-2019
+ [~] Changed internals of how Modules and Commands are stored from List to Map which is much faster in all scenarios tested.
+ [~] Fixed small bug which caused bot lists to be a single event behind the actual state of the bot.
+ [~] Slightly refactored and modified several classes to make them more efficient.

## 27-04-2019
+ [~] Cleaned up the scheduler, combining some tasks and removing others altogether.
+ [~] Added function to sync the guilds database on startup in case the bot was added to a server in downtime.
+ [~] Migrated from HttpsUrlConnection to OkHttp for all API calls since it is regularly updated, modern and there and JDA already uses it.

## 24-04-2019
+ [+] Added DivineDiscordBotList integration.
+ [~] Updated the shard statistics task to keep more accurate information since there were some discrepancies
+ [~] Added extra shard information such as channels and ping to increase the quality of the information on the website.

## 19-04-2019
+ [+] Added lyrics command to enable users to lookup lyrics.
+ [+] Added urban dictionary command because it's both free and easy.
+ [~] Fixed some older bugs and dodgy commands.

## 10-04-2019
+ [+] Added new database connection class for the automatic provisioning of shards.
+ [+] Added task to renew the shard's ID every 30 seconds to prevent it being cleaned up.
+ [~] Migrated settings database from the main Yuuko application server to the web server where the metrics database is hosted to prevent duplicate databases.

## 09-04-2019
+ [~] Refactored AudioManagerManager to AudioManagerController for better naming conventions.
+ [~] Changed the way shards are provisioned to allow dockerisation of the whole process.

## 31-03-2019
+ [~] Updated [__choose__] command to better represent a users choice and a basic statistic of that being chosen.
+ [~] Updated dependencies to their latest versions.
+ [~] Updated [__help__] to dynamically show the correct prefix for command usage and other such messages.

## 28-03-2019
+ [!] Removed dbModuleName field from module superclass, renamed all of the columns in the database, removing the module part. (saves space)
+ [~] Updated [__help__] to show disabled commands as well as modules.
+ [~] Updated [__command__] to allow resetting of individual commands instead of just all of them.

## 27-03-2019
+ [!] Added a subclass class to extend MessageReceivedEvent name MessageEvent allowing for much more powerful manipulation of events.
+ [~] Refactored CommandExecutor, every command, module and relevant utility to work with the new wrapper class.
+ [~] Refactored all module classes to be more memory aware instead of just arbitrarily creating new objects.
+ [~] Fixed [__command__] which was suffering from the after effects of a previous refactor that went unnoticed.
+ [~] Tweaked GenericGuildVoiceController to try and fix a bug which stops audio players from correctly being destroyed.

## 22-03-2019
+ [+] Added [__petition__] command to return UK Parliament petitions by given petition ID.
+ [+] Added [__natgeo__] command to return the top headlines from National Geographic. (cause science and nature are rad)
+ [~] Refactored MessageHandler to core package and out of utilities.
+ [~] Refactored certain utilities from the misc util class to their own DiscordUtilities class.
+ [~] Updated Event Metrics to include more specific data about which types of user is behind each event, also added messages sent metrics.
+ [~] Added new job/task to clear the database of anything over 6 hours old, after 6 hours of being online. (and then every 1 hour);

## 19-03-2019
+ [~] Removed listening for numeric values between 1-10 and the word cancel in GenericMessageController and refactored the class.
+ [~] Altered how [__search__] works, you now need to use -search <value> or -search cancel to make the input less confusing.
+ [~] Refactored [__linestatus__] to [__londonunderground__], although it's longer, it's less ambiguous.

## 17-03-2019
+ [~] Modified structure of settings, making them more like a command or module.
+ [~] Improved the JsonBuffer class to support unlimited extra headers, this required adding a RequestProperty class.
+ [~] Added a send permission check utility to MessageUtilities and every method in MessageHandler to prevent `WRITE_PERMISSION` exceptions.
+ [~] Finished translating MessageHandler.sendException() to log.error();.
+ [-] Removed unnecessary methods from MessageHandler, including sendException since it is now depreciated.

## 13-03-2019
+ [+] Added support for custom welcome messages using the newMember setting.
+ [~] Refactored the command executor to make it easier to read and maintain, being such a core part of functionality.
+ [~] Added TextUtility function to be able to resolve tokenized messages containing the %user% and %guild% tokens.

## 10-03-2019
+ [+] Added [__countdown__] command which allows you to check how long it is until a certain date. `format dd/MM/yyyy`
+ [~] Implemented new sanitiser function which allows date checking.
+ [~] Implemented new text utility which returns a verbose timestamp.

## 09-03-2019
+ [~] Automated the [__help__] command so it doesn't need to be manually updated again.
+ [~] Changed the formatting for command usages.
+ [~] Made small changes to satisfy good practice and to improve code quality, such as parsing booleans instead of .equals("1");

## 08-03-2019
+ [~] Refactored DatabaseFunctions into CommandFunctions, GuildFunctions and ModuleFunctions respectively to break up the code.
+ [~] Refactored key method names to be more consistent with my usual code style.

## 06-03-2019
+ [+] Added [__command__] command which allows users to disable/enable individual commands either by channel or globally on a server.
+ [+] Added the base of a [__reddit__] command which will enable users to grab the latest post from any subreddit. (Soon)
+ [~] Refactored [__server__] to [__guild__] to reflect how it's officially named.
+ [~] Modified ping command to say pong like every other ping command ever.
+ [~] Fixed a small issue with stop commands being issued by the lonely check.

## 03-03-2019
+ [+] Added [__ping__] command, because it seems like users appreciate small commands as much as larger ones with more information.
+ [~] Minor refactoring to metrics classes to make things a little more concise.

## 28-02-2019-1
+ [+] Added [__seek__] to allow seeking of the currently playing track.
+ [~] Updated Lavalink utilities to add quality of life changes.
+ [~] Fixed [__current__] command to actually show correct timestamp.

## 24-02-2019
+ [~] Minor fixes to the spoilerify and eightball commands.
+ [~] Updated dependencies to their latest versions.

## 17-02-2019_2
+ [+] Added [__8ball__] command so people can get some more RNG in their lives.
+ [+] Added [__flip__] command for more RNG, because they're free.
+ [~] Updated dependencies to their latest versions.
+ [~] Minor refactoring to various commands.

## 16-02-2019
+ [~] Fixed some minor bugs with the recent changes.
+ [~] Altered the new [__roles__] command to give it better guarding in guilds with many, many roles.
+ [~] Updated help command to reflect recent changes.

## 14-02-2019
+ [+] Added [__roles__] command to return a list of the guild's roles to the user.
+ [~] Refactored moderation log setting to be more generic internally.

## 12-02-2019
+ [+] Added [__avatar__] command so users can look at other user's avatars... but bigger.
+ [~] Updated moderation commands to check role hierarchy before trying to do anything.
+ [~] Changed Sanitiser to take an optional parameter to silence feedback from its results.

## 11-02-2019
+ [~] Changed how the [__ban__] command works, it turns out I didn't realise what one of the ban parameters was this whole time.
+ [~] Updated dependencies, namely JDA 3.8.2 and okhttp 3.13.1.

## 08-02-2019_2
+ [+] Added [__github__] command which allows users to lookup github repositories and return some information about them.
+ [+] Added [__spoilerify__] command which returns the raw string for those really annoying 'every character' spoiler messages.
+ [~] Improved the Configuration class, adding everything that doesn't require JDA to be built first and logging each step to console.
+ [-] Removed the Cache class and moved everything into more appropriate places, with 90% of it going into the Configuration class.

## 07-02-2019_2
+ [~] Refactored executeCommand to onCommand to be more consistent with naming conventions.
+ [~] Finished mod log, hooking nuke, ban, kick and mute instead of relying on JDA events.
+ [~] General refactoring and code maintenance.

## 05-02-2019
+ [+] Added mod log setting, to log bans, unbans, message deletes, etc.
+ [~] Modified command log from the old inline `version` to use embeds instead.
+ [~] Changed timestamp of several commands to use embed timestamp feature instead of formatting own timestamp.

## 02-02-2019
+ [~] Modified the [__ban__], [__kick__] and [__muted__] commands to allow input of ID strings instead of always tagging the user.
+ [~] Fixed small issue with the track scheduler which wouldn't reset the looping status when the player is destroyed and recreated.

## 31-01-2019
+ [+] Added a vote command to show users where they can vote for the bot.
+ [~] Refactored [__embarrassed__] to [__blush__] for fairly obvious reasons.
+ [~] Refactored [__repeat__] command to [__loop__], also changed the functionality to loop the whole queue instead of just the current song.
+ [~] Updated the help command to reflect changes.

## 26-01-2019
+ [~] Fixed a bug that stopped the modules command from working correctly. (just another missed rs.next() *sigh*)
+ [-] Removed the [__modules__] command and combined it with the regular module command to achieve dual functionality.

## 25-01-2019
+ [+] Added [__kiss__], [__pet__], [__embarrassed__] interactions.
+ [+] Added an NSFW tag on a command by command basis instead of just per module.
+ [+] Added Lavalink utilities class to cut down on verbose chained methods.
+ [~] Fixed bug present since 21-01-2019_3 where bot wouldn't leave channel when everyone else has.

## 22-01-2019
+ [+] Added [__lavalink__] command to be able to add/remove lavalink nodes on demand.
+ [~] General fixes, structural improvements and comments.

## 21-01-2019_3
+ [+] Added [__shards__] command to give the user some basic information about the bot's current shards.
+ [~] Migrated from pure Lavaplayer to using Lavalink for audio, in preparation for when the bot is sharded (not necessary but wanted)
+ [~] Migrated from JDABuilder to DefaultShardManagerBuilder.
+ [~] Fixed some minor bugs.

## 19-01-2019_2
+ [+] Added a starboard feature... everyone loves those right?
+ [~] Refactored all of the non-boolean settings into their own settings classes and also refactored the database to allow the commandLog and member welcoming to be customised.
+ [~] Moved settings from being stored in the Cache class and initiated in the main class to being parked in the CommandSettings class.
+ [~] Shortened commandPrefix in the database to simply prefix since there is no other type of prefix to describe.
+ [-] Trimmed some excess fat that was no longer needed.

## 18-01-2019_2
+ [~] Added a GenericTextChannelController to check when channels are removed to update bindings appropriately.
+ [~] Refactored parts of the DatabaseConnection subclasses into a DatabaseConnection superclass.
+ [~] Made all of the methods in the SettingsDatabaseFunctions class static since individual instances of database classes were unnecessary.
+ [~] Moved all of the connection logic for all database functions into a try with resources structure to ensure the prevention of database lockups. (which happened today somehow)
+ [~] Migrated the GenericEventManager to use the correct logger.

## 17-01-2019
+ [~] Refactored module package to commands to keep the intent of the package clear. (everyone knows what a command is, but maybe not module in this context)
+ [~] Refactored the isChannelNSFW function from the Module super class to the Utils class.
+ [~] Updated dependencies to their latest versions.

## 15-01-2019
+ [~] Modified the [__bind__] command, where giving no parameters will returns a list of binds.
+ [~] Refactored all of the database bind functions from the DatabaseFunctions class into it's own respective class.

## 13-01-2019_2
+ [~] Updated the [__bind__] command to make it less scuffed, properly checking parameters, also now allowing multiple channels to be bound at once.
+ [~] Updated the CommandExecutor, moving key logic from the GenericMessageHandler class.
+ [-] Removed the last remnants of the setup command, since it wasn't actually used at all.
+ [-] Removed all command logging functions that send data to the support server directly. (everything now goes to the metrics database)

## 12-01-2019_3
+ [!] Instead of using classical version numbers, from now on I'll be using the date, followed by a build number for that date. (trying to keep semantic versions is a hassle)
+ [!] Migrated from Maven to Gradle, for the purposes of learning and increased build speeds. (apparently)
+ [~] Made a number of changes to the database and it's internal logic.
+ [~] Small number of formatting changes and other general house keeping.

## Y-1.1.0 (The Big Refactor)
+ [!] Removed the Yuuko class self event manager and event code, creating a dedicated events package to put in it's place.
+ [+] Added the Interaction module which as the name suggests adds interactions.
+ [+] Added interactions: [__poke__], [__hug__], [__attack__], [__bite__], [__angry__], [__cry__], [__laugh__], [__pout__], [__shrug__], [__sleep__], [__tickle__].
+ [+] Added a simple [__choice__] command which takes an undefiled number of parameters separated by commas and selects one of the choices.
+ [~] Improved the [__roll__] command to give a better indication on what the roll is against.
+ [~] Refactored Math module into Fun module to allow for a wider range of commands to be encompassed such as coin flips and other such commands.
+ [~] Refactored utils package to utilities to actively try and make things more complete and professional.
+ [~] Refactored the Statistics class to Metrics since they're the latter and not the former and give them an overhaul.
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

 