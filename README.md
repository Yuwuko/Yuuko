# BasketBandit 2.0 (02/05/2018) 
BasketBandit is a Discord bot that I am developing based on the JDA libraries for JAVA.

It is a modular bot which means that you can enable or disable certain sets of commands where you see fit. The current modules are core (can't be disabled), custom, fun, logging, math, utility and runescape.
To enable or disable a module you type the bot invocation (at current '?') module and the name of the module, e.g. '?module fun'

Note: The logging module requires a text-channel named command-log to work correctly. If this doesn't exist and the module is active, the bot will remind you that it is needed.

![Build Status](https://travis-ci.org/Galaxiosaurus/BasketBandit.svg?branch=master)

## Current Commands

The invocation for the bot at this time is '?', this is used to prefix the below commands so the bot recognises that it is a command. Double invocation is used for custom commands, meaning that any custom commands should be prefixed with '??' instead.

- __setup__ this command needs to be run before the bot can be used. It will initialise the default settings for the bot. (__core__, __single use__)

- __module \<name\>__ will toggle a module on or off based on it's current value. (__core__)

- __modules__ will list all of the bots modules, noting which are currently enabled and which are disabled. (__core__)

- __help__ will private message the user a list of these commands and some other information about BasketBandit. (__core__)

- __info \<name\>__ will give account information about the user given, such as join date, online status and guild roles. (__utility__)

- __nuke \<amount\>__ will delete the given number of previous messages, up to 100. (__utility__, __admin__)

- __rsstats \<name\>__ will return the RuneScape 3 stats for the given user. (__runescape__)

- __osstats \<name\>__ will return the OldSchool Runescape stats for the given user. (__runescape__)

- __overreact__ will react with 20 random guild emotes to the previous message in the text channel. (__fun__)

- __insult__ will randomly insult a user in the guild. (__fun__)

- __roll \<value\>__ will roll a set die. [d6, d8, d10, d12, d20, d00] (__math__)

- __sum \<value\> \<operator\> \<value\>__ will calculate and return a simple 2 variable sum. Supported operations: [+, -, *, /, ^, %] (__math__)

- __newcc \<name\> \<contents\>__ will create a new custom command. (__custom__)

- __delcc \<name\>__ will delete a custom command. (__custom__)

- __\<command\>__ will execute a custom command. (__custom__, __double invocation__)

## Other features

As a part of the __utility__ module, reacting with :pushpin: (📌) will automatically pin the post as such, removing it will unpin the post. However if there are multiple of the react, they will all need to be removed before the post is unpinned.

## Notes

If you want to use the bot on your own server, follow [this](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) link or if you have any suggestions, feel free to post them in my Discord server [here](https://discord.gg/QcwghsA).