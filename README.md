![Status](https://discordbots.org/api/widget/status/420682957007880223.svg)
![Servers](https://discordbots.org/api/widget/servers/420682957007880223.svg)
![Servers](https://discordbots.org/api/widget/upvotes/420682957007880223.svg)
![Library](https://discordbots.org/api/widget/lib/420682957007880223.svg)
[![CodeFactor](https://www.codefactor.io/repository/github/Yuuko-oh/yuuko/badge)](https://www.codefactor.io/repository/github/yuuko-oh/yuuko)
[![GitHub release](https://img.shields.io/github/release/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko)
[![GitHub stars](https://img.shields.io/github/stars/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko/stargazers)
[![GitHub issues](https://img.shields.io/github/issues/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko/issues)
[![GitHub license](https://img.shields.io/github/license/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko/blob/master/LICENSE)

# Yuuko 13-01-2019_1

Yuuko, programmed in [Java](https://www.oracle.com/uk/java/index.html) using [Gradle](https://gradle.org/) for dependencies, utilising the [JDA](https://github.com/DV8FromTheWorld/JDA) and [LavaPlayer](https://github.com/sedmelluq/lavaplayer) libraries.

If you want to use the bot on your own server, follow [this](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) link or if you have any feature requests, feel free to post them in my Discord server [here](https://discord.gg/VsM25fN).

Dashboard: You can visit https://www.yuuko.info to see this full list of commands and gain access to some metrics related to the bot.

## Commands

The global invocation/prefix is `@Yuuko` (mention) and the custom prefix is automatically set to `-`, but can be changed. Using `-settings commandPrefix !` will change the prefix and `-help [command]` will show you usage for the given command. 

### Core

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| module | Toggles a module on or off based on it's current value. | -module [module] | `-module audio` | MANAGE_SERVER|
| modules | Lists all of modules, separated by their on/off state. | -modules | `-modules` |
| help | Sends a private message to the user with a link to the GitHub repository where this list is located, or sends usage information about the given command. | -help &#124; [command] | `-help`, `-help play` |
| about | Returns some technical information about Yuuko, such as uptime, ping and server count. | -about | `-about` |
| settings | Gives the ability to display or set a variety of server settings. | -settings &#124; [setting] [value] | `settings`, `-settings deleteExecuted true` | MANAGE_SERVER |

### Moderation

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| kick | Kicks the provided user from, with an optional reason. | -kick @user &#124; [reason]| `-kick @Yuuko`, `-kick @Yuuko not very nice.` | KICK_MEMBERS |
| ban | Bans the provided user for the given amount of time in days, with an optional reason. | -ban @user [days] &#124; [reason] | `-ban @Yuuko`, `-ban @Yuuko 7 test reason` | BAN_MEMBERS |
| mute | Mutes the provided user from both voice and text chat on the server, with an optional reason (Toggle) | -mute @user &#124; [reason] | `-mute @Yuuko`, `-mute @Yuuko test reason` | MUTE_MEMBERS |
| nuke | Deletes the provided number of messages from a text channel **OR** if tagged, the whole channel. Max Channels `10`, Max messages `100`. (Warning: Nuking via #channel will break any bindings you have created!) | -nuke [value] &#124; #channel | `-nuke 50`, `-nuke #nsfw` | MESSAGE_MANAGE, MANAGE_CHANNEL |

### Utility

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| user | Returns information about the provided user, such as join date, online status and roles. | -user @user | `-user @Yuuko` |
| server | Returns information about the current server. | -server | `-server` |
| channel | Adds or removes a channel to/from the server. *Note: You cannot have NSFW voice channels, even if you tried.* | -channel [action] [type] [name] &#124; [nsfw] | `-channel add text cool-text-channel nsfw` | MANAGE_CHANNELS |
| bind | Binds a module to a text channel preventing commands from being executed outside of that channel. Modules can be bound to multiple channels and unbound by retyping the command. (A toggle) | -bind [module] &#124; #channel... | `-bind audio`, `-bind audio #test-channel #another-channel` | ADMINISTRATOR  |

### World

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| linestatus | Returns full line coverage for the London Underground which is accurate to command execution, with optional `min` argument to return a minified version. | -linestatus &#124; [min] | `-linestatus`, `-linestatus min` |
| weather | Allows you to look up the weather in the given city *Note: Currently limited to a city name, without country code.* | -weather [city] | `-weather London` |
| tesco | Returns product information about any item sold by Tesco PLC | -tesco [product] | `-tesco mount gay` |

### Interaction

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| poke | Pokes the tagged user. | -poke @user | `-poke @Yuuko#2525` |
| bite | Bites the tagged user. | -bite @user | `-bite @Yuuko#2525` |
| hug | Hugs the tagged user. | -hug @user | `-hug @Yuuko#2525` |
| attack | Attacks the tagged user. | -attack @user | `-attack @Yuuko#2525` |
| angry | Displays that you are angry, or angry at the tagged user. | -angry &#124; @user | `-angry`, `-angry @Yuuko#2525` |
| cry | Displays that you are crying. | -cry | `-cry` |
| laugh | Displays that you are laughing, or laughing at the tagged user. | -laugh &#124; @user | `-laugh`, `laugh @Yuuko#2525` |
| pout | Displays that you are pouting, or pouts at the tagged user. | -pout &#124; @user | `-pout`, `-pout @Yuuko#2525` |
| shrug | Displays that you are shrugging, or shrugs at the tagged user. | -shrug &#124; @user | `-shrug`, `-shrug @Yuuko#2525` |
| sleep | Displays that you are sleeping. | -sleep | `-sleep` |
| tickle | Tickles the tagged user. | -tickle @user | `-tickle @Yuuko#2525` |

### Fun

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| roll | Rolls a die with the given value and returns the result. Rolling `00` will return a multiple of 10 between `0` and `100`. | -roll [value] &#124; [00] | `-roll 42` |
| choose | Takes a list of options, separated by a comma and returns an option at random. | -choose [choice], [choice]... | `-choose sweet, savoury` |

### Media

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| osu | Returns information about an osu! player on a specified mode. (Modes: 0 = Osu, 1 = Taiko, 2 = CtB, 3 = Mania) | -osu [username] &#124; [mode] | `-osu galaxiosaurus`, `-osu galaxiosaurus 3` |
| kitsu | Returns information about the given anime. (Types: show) (Character information to come soon!) | -kitsu [name] | `-kitsu naruto` |

### Audio

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| play | Starts playback of the given audio track through either URL or search term. Will ask Yuuko to join the voice channel of the command issuer and if a track is already playing, queue it instead. Using the command without arguments will resume a paused player. | -play &#124; [url] &#124; [term] | `-play https://www.youtube.com/watch?v=DDW4hTWbRYs`, `-play something` |
| pause | Pauses playback of the current track. | -pause | `-pause` |
| stop | Stops playback, clearing the queue and removing the background track. | -stop | `-stop` |
| skip | Skips the currently playing track, if there is one. | -skip | `-skip` |
| shuffle | Shuffles the queue. | -shuffle | `-shuffle` |
| current | Returns information about the currently playing track such as current time, artist and source. | -current | `-current` |
| last | Returns information about the last played track such as artist and source. | -last | `-last` | 
| queue | Returns the first 10 tracks in the queue or however many there are if under 10. | -queue | `-queue` |
| clear | Clears the current queue of all of the current tracks, or clears a single track from the given position in the queue. | -clear &#124; [position] | `-clear`, `-clear 4` |
| background | Sets the background track and starts playback. Background tracks will play if there is nothing in queue and queued tracks will play instead of the background track. Use the command with no parameters to unset a set background. | -background [url] &#124; [term] | `-background https://www.youtube.com/watch?v=va3Dj_sUCJs`, `-background cool music`  |
| repeat | Toggles a track to repeat. | -repeat | `-repeat` |
| search | Searches YouTube and returns the first 10 results, a choice is made by typing the number and the selected track will be queued. | -search [term] | `-search funky beats` |

### NSFW

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| efukt | Returns a random image/gif/video from eFukt. | -efukt | `-efukt` |
| neko | Returns a random lewd neko image by default or another type with a given parameter | -neko &#124; [type] | <code>-neko &#124; boobs</code> |

## Settings

| Setting | Description | Values | Usage |
|---------|-------------|-------|---------|
| commandPrefix | Changes the custom prefix for the server. (Characters: Min `1`, Max `5`) | String | `-settings commandPrefix !` |
| deleteExecuted | Deletes the command message after it has been executed. | TRUE, FALSE | `-settings deleteExecuted true` |
| commandLogging | Logs any executed command into a logging channel. | TRUE, FALSE | `-settings commandLogging true` |
| nowPlaying | Announces the next track in the queue when the previous finishes. | TRUE, FALSE | `-settings nowPlaying true` |
| djMode | Toggles whether or not the role of `DJ` is required to use key audio module commands. | TRUE, FALSE | `-settings djMode true` |
| welcomeMembers | Whether or not to welcome members with a predefined message in the first channel that contains the name 'general' or 'primary'. (customisation soon) | TRUE, FALSE | `-settings welcomeMembers true` | 

## Other features

As a part of the __utility__ module, reacting with :pushpin: (\:pushpin\:) will automatically pin the post as such, removing it will unpin the post. However if there are multiple of the react, they will all need to be removed before the post is unpinned.

## Notes

For the welcome message a basic 'general' text-channel is used. If that does or doesn't exist the bot will also send a PM to the server owner but not attempt to send the message anywhere else.

The logging setting currently requires a text-channel named 'command-log' to work correctly. If this doesn't exist and the module is active, the bot will remind you that it is needed. (this will be changed in the future)
