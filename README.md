# BasketBandit 1.5.0 (19/05/2018) 
BasketBandit is a Discord bot that I am developing based on the JDA libraries and LavaPlayer for music.

It is a modular bot which means that you can enable or disable certain sets of commands where you see fit. The current modules are core (can't be disabled), custom, fun, logging, math, utility and runescape.
To enable or disable a module you type the bot invocation (at current '?') module and the name of the module, e.g. '?module fun'

Note: The logging module requires a text-channel named command-log to work correctly. If this doesn't exist and the module is active, the bot will remind you that it is needed.

## Commands

The invocation for the bot at this time is '?', this is used to prefix the below commands so the bot recognises that it is a command. Double invocation is used for custom commands, meaning that any custom commands should be prefixed with '??' instead.

### Core

- __setup__ this command needs to be run before the bot can be used. It will initialise the default settings for the bot. (__single use__)

- __module \<name\>__ will toggle a module on or off based on it's current value.

- __modules__ will list all of the bots modules, noting which are currently enabled and which are disabled. 

- __help__ will private message the user a list of these commands and some other information about BasketBandit.

### Moderation

- __kick \<userID (18 digit)\> \<reason\> (optional)__ will kick the given user from the server with optional reason.

- __ban \<userID (18 digit)\> \<days\> \<reason\> (optional)__ will ban the given user for the the given amount of time in days with optional reason.

- __nuke \<amount\>__ will delete the given number of previous messages, up to 100.

- __addchannel \<type\> \<name\> \<nsfw\>__ creates a new channel. (Type is "text" or "voice", NSFW (optional))

- __remchannel \<type\> \<idLong\>__ removes a channel. (Type is "text" or "voice")

### Utility

- __user \<name\>__ will give account information about the user given, such as join date, online status and guild roles. 

- __server__ will give information about the current server, such as region and owner.

### Fun

- __overreact__ will react with 20 random guild emotes to the previous message in the text channel.

- __insult__ will randomly insult a user in the guild.

### Math

- __roll \<value\>__ will roll a set die. [d6, d8, d10, d12, d20, d00] 

- __sum \<value\> \<operator\> \<value\>__ will calculate and return a simple 2 variable sum. Supported operations: [+, -, *, /, ^, %]

### RuneScape

- __rsstats \<name\>__ will return the RuneScape 3 stats for the given user.

- __osstats \<name\>__ will return the OldSchool Runescape stats for the given user.

### Custom

- __newcc \<name\> \<contents\>__ will create a new custom command.

- __delcc \<name\>__ will delete a custom command.

- __\<command\>__ will execute a custom command. (__double invocation__)

### Music

- __play \<track\>__ will automatically make the bot join the voice channel you are in (must be in one) and start playback. If there is currently a track playing, it will instead add it to the queue. If you provide a playlist link, it will add the whole playlist to the queue. (track is optional and issuing this command without one will resume playback if paused)

- __pause__ will pause playback.

- __stop__ will stop playback, remove the queue and ask the bot to leave the channel.

- __skip__ will skip the current track.

- __shuffle__ will shuffle the current queue.

- __track__ will give information about the current track.

- __queue__ will display the current queue.

- __setbackground \<track\>__ will set the background track which will play when nothing is in the queue. (Good for music streams!)

- __unsetbackground__ will unset the background track.

## Other features

As a part of the __utility__ module, reacting with :pushpin: (\:pushpin\:) will automatically pin the post as such, removing it will unpin the post. However if there are multiple of the react, they will all need to be removed before the post is unpinned.

## Notes

If you want to use the bot on your own server, follow [this](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) link or if you have any suggestions, feel free to post them in my Discord server [here](https://discord.gg/QcwghsA).