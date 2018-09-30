![Status](https://discordbots.org/api/widget/status/420682957007880223.svg)
![Servers](https://discordbots.org/api/widget/servers/420682957007880223.svg)
![Servers](https://discordbots.org/api/widget/upvotes/420682957007880223.svg)
![Library](https://discordbots.org/api/widget/lib/420682957007880223.svg)
[![CodeFactor](https://www.codefactor.io/repository/github/basketbandit/basketbandit-java/badge)](https://www.codefactor.io/repository/github/basketbandit/basketbandit-java)
[![GitHub release](https://img.shields.io/github/release/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java)
[![GitHub stars](https://img.shields.io/github/stars/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java/stargazers)
[![GitHub issues](https://img.shields.io/github/issues/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java/issues)
[![GitHub license](https://img.shields.io/github/license/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java/blob/master/LICENSE)

# BasketBandit 3.1.0 (30/09/2018) 

BasketBandit is a multi-purpose bot for Discord programmed in [Java](https://www.oracle.com/uk/java/index.html) using [Maven](https://maven.apache.org/) for dependencies, utilising the [JDA](https://github.com/DV8FromTheWorld/JDA) and [LavaPlayer](https://github.com/sedmelluq/lavaplayer) libraries.

If you want to use the bot on your own server, follow [this](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) link or if you have any feature requests, feel free to post them in my Discord server [here](https://discord.gg/QcwghsA).

## Commands

The global invocation/prefix is `@BasketBandit` (mention) and the custom prefix is automatically set to `-` but can be changed. This is used to prefix the below commands so the bot recognises that it is a command. You can also set a custom prefix using the `setprefix` command below which I recommend to make everything quicker. E.g. `@BasketBandit setprefix !` or `-setprefix !`.

__PLEASE READ:__ In most cases, the bot sets itself up correctly. The easiest way to check is by typing the command given above to change the prefix. If you do not wish to do this, ensure that there is only a _single_ space between `@BasketBandit` and the rest of the command.

### Core

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| setup | Execute if the bot didn't initialise correctly, but shouldn't need to be used. (If it says unsuccessful, the setup was already performed successfully) | -setup | `-setup` | ADMINISTRATOR |
| module | Toggles a module on or off based on it's current value. | -module [module] | `-module audio` | MANAGE_SERVER|
| modules | Lists all of modules, separated by their on/off state. | -modules | `-modules` |
| help | Sends a private message to the user with a link to the GitHub repository where this list is located, or sends usage information about the given command. | -help &#124; [command] | <code>-help &#124; [command]</code> |
| about | Returns some technical information about BasketBandit, such as uptime, ping and server count. | -about | `-about` |
| setprefix | Sets the server's command prefix. (Global prefix will still work.) | -setprefix [prefix] | `-setprefix !` | MANAGE_SERVER |
| settings | Gives the ability to display or set a variety of server settings. | -settings &#124; [setting] [value] | <code>-settings &#124; deleteExecuted true</code> | MANAGE_SERVER |

### Moderation

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| kick | Kicks the provided user from, with an optional reason. | -kick @user &#124; [reason]| <code>-kick @BasketBandit &#124; not very nice.</code> | KICK_MEMBERS |
| ban | Bans the provided user for the given amount of time in days, with an optional reason. | -ban @user [days] &#124; [reason] | <code>-ban @BasketBandit 7 &#124;  not cool, bro.</code> | BAN_MEMBERS |
| mute | Mutes the provided user from both voice and text chat on the server, with an optional reason | -mute @user &#124; [reason] | <code>-mute @BasketBandit &#124;  mic spamming.</code> | MUTE_MEMBERS |
| unmute | Unmutes the provided user. | -unmute @user | `-unmute @BasketBandit` | MUTE_MEMBERS |
| nuke | Deletes the provided number of messages from a text channel. Min: `1`, Max `100`. | -nuke [value] | `-nuke 25` | MANAGE_MESSAGES |

### Utility

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| user | Returns information about the provided user, such as join date, online status and roles. | -user @user | `-user @BasketBandit` |
| server | Returns information about the current server. | -server | `-server` |
| channel | Adds or removes a channel to/from the server. *Note: You cannot have NSFW voice channels, even if you tried.* | -channel [action] [type] [name] &#124; [nsfw] | <code>-channel add text cool-text-channel &#124; nsfw</code> | MANAGE_CHANNELS |
| bind | Binds a module to a text channel preventing commands from being executed outside of that channel. Modules can be bound to multiple channels. Modules can be unbound by retyping the command. | -bind [module] &#124; [channel] | <code>-bind audio &#124; test-channel</code> | ADMINISTRATOR  |
| exclude | Excludes a module from a text channel, preventing commands from being executed inside that channel. Modules can be excluded from multiple channels. Modules can be included by retyping the command. | -exclude [module] &#124; [channel] | <code>-exclude audio &#124; test-channel</code> | ADMINISTRATOR  |
| weather | Allows you to look up the weather in the given city *Note: Currently limited to a city name, without country code.* | -weather [city] | `-weather London` |

### Transport

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| linestatus | Returns full line coverage for the London Underground which is accurate to command execution, with optional `min` argument to return a minified version. | -linestatus &#124; [min] | <code>-linestatus &#124; min</code> |

### Math

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| roll | Rolls a die with the given value and returns the result. Rolling `00` will return a multiple of 10 between `0` and `100`. | -roll [value] &#124; [00] | `-roll 42` |
| sum |  Calculates and returns the result to simple 2 variable sums, accepting the `+`, `-`, `*`, `/`, `^` and `%` operators. | -sum [value] [operator] [value] | `-sum 400 + 20` |

### Media

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| runescape | Returns a list of RuneScape stats for the given version and player. | -runescape [game] [player] | `-runescape os white cat22` |
| wow | Returns information about a given World of Warcraft character. | -wow [character] [realm] | `-wow porcus silvermoon` |
| osu | Returns a small signature image for an Osu player on the specified mode. (Modes: 0 = Osu, 1 = Taiko, 2 = CtB, 3 = Mania) | -osu [username] &#124; [mode] | `-osu galaxiosaurus 3` |
| kitsu | Returns information about the given anime. (Types: show) (Character information to come soon!) | -kitsu [type] [name] | `-kitsu show naruto` |

### Music

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| play | Starts playback of the given audio track through either URL or search term. Will ask BasketBandit to join the voice channel of the command issuer and if a track is already playing, queue it instead. Using the command without arguments will resume a paused player. | -play &#124; [url] &#124; [term] | <code>-play &#124; https://www.youtube.com/watch?v=DDW4hTWbRYs &#124; something </code> |
| pause | Pauses playback of the current track. | -pause | `-pause` |
| stop | Stops playback, clearing the queue and removing the background track. | -stop | `-stop` |
| skip | Skips the currently playing track, if there is one. | -skip | `-skip` |
| shuffle | Shuffles the queue. | -shuffle | `-shuffle` |
| current | Returns information about the currently playing track such as current time, artist and source. | -current | `-current` |
| last | Returns information about the last played track such as artist and source. | -last | `-last` | 
| queue | Returns the first 10 tracks in the queue or however many there are if under 10. | -queue | `-queue` |
| clear | Clears the current queue of all of the current tracks, or clears a single track from the given position in the queue. | -clear &#124; [position] | <code>-clear &#124; 4</code> |
| background | Sets the background track and starts playback. Background tracks will play if there is nothing in queue and queued tracks will play instead of the background track. Use the command with no parameters to unset a set background. | -background [url] &#124; [term] | <code>-background https://www.youtube.com/watch?v=va3Dj_sUCJs &#124; cool music</code>  |
| repeat | Toggles a track to repeat. | -repeat | `-repeat` |
| search | Searches YouTube and returns the first 10 results, a choice is made by typing the number and the selected track will be queued. | -search [term] | `-search funky beats` |

### NSFW

| Command | Description | Usage | Example | Permission |
|---------|-------------|-------|---------|------------|
| efukt | Returns a random image/gif/video from eFukt. (Requires NSFW flagged channel to work) | -efukt | `-efukt` |


## Other features

As a part of the __utility__ module, reacting with :pushpin: (\:pushpin\:) will automatically pin the post as such, removing it will unpin the post. However if there are multiple of the react, they will all need to be removed before the post is unpinned.

## Known issues

__P:__ I still hear audio after I have muted the bot and restarted my client or switched channels. 

__S:__ This is a [verified bug](https://trello.com/c/UkNEavqc), and there isn't anything I can do about it.

__P:__ There's no audio playing with using the __-play__ command?

__S:__ There have been issues connecting to some EU discord servers lately, a quick workout for this is to change the server location to US East or elsewhere by 'Clicking server name -> Server settings -> Server Region -> Change'.


## Notes

For the welcome message a basic 'general' text-channel is used. If that does or doesn't exist the bot will also send a PM to the server owner but not attempt to send the message anywhere else.

The logging module requires a text-channel named 'command-log' to work correctly. If this doesn't exist and the module is active, the bot will remind you that it is needed


