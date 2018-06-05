[![GitHub release](https://img.shields.io/github/release/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java)
[![Github All Releases](https://img.shields.io/github/downloads/BasketBandit/BasketBandit-Java/total.svg)](https://github.com/BasketBandit/BasketBandit-Java)
[![GitHub stars](https://img.shields.io/github/stars/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java/stargazers)
[![GitHub issues](https://img.shields.io/github/issues/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java/issues)
[![GitHub license](https://img.shields.io/github/license/BasketBandit/BasketBandit-Java.svg)](https://github.com/BasketBandit/BasketBandit-Java/blob/master/LICENSE)

# BasketBandit 1.9.2 (05/06/2018) 

BasketBandit is a multi-purpose bot for Discord programmed in [Java](https://www.oracle.com/uk/java/index.html) using [Maven](https://maven.apache.org/) for dependencies, utilising the [JDA](https://github.com/DV8FromTheWorld/JDA) and [LavaPlayer](https://github.com/sedmelluq/lavaplayer) libraries.

If you want to use the bot on your own server, follow [this](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) link or if you have any feature requests, feel free to post them in my Discord server [here](https://discord.gg/QcwghsA).

## Commands

The global invocation/prefix is @BasketBandit (mention), this is used to prefix the below commands so the bot recognises that it is a command. E.g. @BasketBanditabout (without a space) You can also set a custom prefix using the __setprefix__ command below which I recommend to make everything quicker.

### Core

- __setup__ this command needs to be run before the bot can be used. It will initialise the default settings for the bot. (__single use__)

- __module \<name\>__ will toggle a module on or off based on it's current value.

- __modules__ will list all of the bots modules, noting which are currently enabled and which are disabled. 

- __help__ will private message the user a list of these commands and some other information about BasketBandit.

- __about__ will return some technical information about the bot such as uptime and ping.

- __setprefix__ will set the server's prefix (__admin only__)

### Moderation

- __kick \<userID (18 digit)\> \<reason\> (optional)__ will kick the given user from the server with optional reason. (relevant permission required)

- __ban \<userID (18 digit)\> \<days\> \<reason\> (optional)__ will ban the given user for the the given amount of time in days with optional reason. (relevant permission required)

- __nuke \<amount\>__ will delete the given number of previous messages, up to 100. (relevant permission required)

- __addchannel \<type\> \<name\> \<nsfw\>__ creates a new channel. (Type is "text" or "voice", NSFW (optional)) (relevant permission required)

- __delchannel \<type\> \<idLong\>__ removes a channel. (Type is "text" or "voice") (relevant permission required)

### Utility

- __user \<name\>__ will give account information about the user given, such as join date, online status and guild roles. 

- __server__ will give information about the current server, such as region and owner.

- __bind \<module\> \<channel\>__ will bind a module to a text channel, only allowing commands to be executed there. (__admin only__)

- __unbind \<module\> \<channel\>__ will unbind a bound module from a text channel. (__admin only__)

- __exclude \<module\> \<channel\>__ will exclude a module from a text channel, not allowing commands to be executed there. (__admin only__)

- __unexclude \<module\> \<channel\>__ will unexclude an excluded module from a text channel. (__admin only__)

### Transport

- __linestatus \<min\>__ will return full line coverage for London Underground, accurate to command execution. (__min__ is optional!)

### Math

- __roll \<value\>__ will roll a set die. [d6, d8, d10, d12, d20, d00] 

- __sum \<value\> \<operator\> \<value\>__ will calculate and return a simple 2 variable sum. Supported operations: [+, -, *, /, ^, %]

### RuneScape

- __rsstats \<name\>__ will return the RuneScape 3 stats for the given user.

- __osstats \<name\>__ will return the OldSchool Runescape stats for the given user.

### Music

- __play \<track\>__ will automatically make the bot join the voice channel you are in (must be in one) and start playback. If there is currently a track playing, it will instead add it to the queue. If you provide a playlist link, it will add the whole playlist to the queue. (track is optional and issuing this command without one will resume playback if paused)

- __pause__ will pause playback.

- __stop__ will stop playback, remove the queue and ask the bot to leave the channel.

- __skip__ will skip the current track.

- __shuffle__ will shuffle the current queue.

- __currenttrack__ will give information about the current track.

- __lasttrack__ will give information about the previous track.

- __queue__ will display the current queue.

- __setbackground \<track\>__ will set the background track which will play when nothing is in the queue. (Good for music streams!)

- __unsetbackground__ will unset the background track.

- __togglerepeat__ will toggle repeat for the current song.

- __search \<track\>__ will search for and return the first 10 tracks from the given string.

## Other features

As a part of the __utility__ module, reacting with :pushpin: (\:pushpin\:) will automatically pin the post as such, removing it will unpin the post. However if there are multiple of the react, they will all need to be removed before the post is unpinned.

## Notes

For the welcome message a basic 'general' text-channel is used. If that does or doesn't exist the bot will also send a PM to the server owner but not attempt to send the message anywhere else.

The logging module requires a text-channel named 'command-log' to work correctly. If this doesn't exist and the module is active, the bot will remind you that it is needed

Can still hear audio after you have muted the bot and restarted your client or switched channels? This is a [verified bug](https://trello.com/c/UkNEavqc), and there isn't anything I can do about it.
