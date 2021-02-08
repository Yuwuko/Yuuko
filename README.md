![Yuuko Banner](https://i.imgur.com/3Aihicb.jpg)

![Status](https://top.gg/api/widget/status/420682957007880223.svg)
![Servers](https://top.gg/api/widget/servers/420682957007880223.svg)
![Servers](https://top.gg/api/widget/upvotes/420682957007880223.svg)
[![CodeFactor](https://www.codefactor.io/repository/github/yuwuko/yuuko/badge)](https://www.codefactor.io/repository/github/yuwuko/yuuko)
[![GitHub release](https://img.shields.io/github/release/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko)
[![GitHub stars](https://img.shields.io/github/stars/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko/stargazers)
[![GitHub issues](https://img.shields.io/github/issues/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko/issues)
[![GitHub license](https://img.shields.io/github/license/Yuuko-oh/Yuuko.svg)](https://github.com/Yuuko-oh/Yuuko/blob/master/LICENSE)

# Yuuko 202102r1

Yuuko, programmed in [Java](https://www.oracle.com/uk/java/index.html) way back in mid-2018. Slowly growing into what is has become today. This project always was, and always will be a labour of love.

If you want Yuuko on your server, follow [this](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) link to invite her... be sure to give all the appropriate permissions, to not run into issues. Visit the [website](https://yuwuko.github.io) if you need support or you want to support Yuuko on pateron.

## Key Features

#### Music
Liven up your Discord server with music from a number of sources including YouTube, Twitch, SoundCloud or any HTTP source of audio such as online radio stations.

#### Reaction Role
Want your users to be able to select roles simply by reacting to a message? With the reaction role feature you can assign server roles to reacts on messages!

#### Interaction
Have you ever wanted to spice up your chatting experience with interactions? Yuuko has a range of different interaction commands available that allows you to express yourself in more than just words.

#### Events
Take full advantage of Yuuko's event system, with modifiable title, description, scheduled time and number of slots, plan events and get an optional reminder 10 minutes before it starts.

## Commands

* **Core** <br>
`help` `shards` `module` `about` `vote` `permissions` `bind`

* **Animal** <br>
`cat` `fox` `dog` `bird`

* **NSFW** <br>
`efukt` `neko` `rule34`

* **Moderation** <br>
`nuke` `kick` `mute` `ban`

* **Interaction** <br>
`pout` `shrug` `angry` `cry` `kiss` `poke` `sleep` `attack` `bite` `blush` `hug` `laugh` `tickle` `pet` `pat` `kill` `dance`

* **Utility** <br>
`guild` `ping` `roles` `avatar` `user` `reactrole` `event`

* **Developer** <br>
`setstatus` `reloadapi` `lavalink` `syncguilds` `restart` `shutdown` `logmetrics`

* **Audio** <br>
`play` `playnext` `last` `clear` `skip` `seek` `pause` `search` `current` `stop` `background` `loop` `shuffle` `lyrics` `queue`

* **Media** <br>
`osu` `github` `anime` `natgeo` `underground` `petition` `weather` `tesco` `urban`

* **Fun** <br>
`8ball` `spoilerify` `roll` `choose` `flip` `advice` `joke` `horoscope`

* **Setting** <br>
`settings` `prefix` `starboard` `commandlog` `moderationlog` `djmode` `cleanupcommands` `playnotifications` `eventchannel`

---

## Self-host 
***[THIS GUIDE MAY BE INCOMPLETE]***

Like Yuuko but want to host her yourself, or simply want a private music bot? Work has been done **[Nov 2020]** to retroactively make Yuuko as easy as possible to self-host. Below are instructions on how you can do just that.

## Self-hosting

### Quick Start Requirements [No APIs] [No Music]
- Java 15+ ~ [download link](https://jdk.java.net/15/)
- Yuuko.jar ~ [download link](https://github.com/Yuuko-oh/Yuuko/raw/master/out/artifacts/yuuko/Yuuko.jar)
- Discord Application ~ [create here](https://discord.com/developers/applications)
- MySQL Database ~ [local xampp for testing](https://www.apachefriends.org/download.html)

### Setup

0) Ensure that you have a MySQL database with collation of `utf8mb4_general_ci` named `dbyuuko` and user with permissions available.

1) Make sure Java is installed and is the correct version. You can do this by opening a command prompt and typing `java -version`. You should see an output that looks similar to `openjdk version "15.0.1" 2020-10-20`.

2) Place `Yuuko.jar` somewhere safe, I recommend putting it in a directory of its own since it will generate files on first launch.

3) Open a command prompt where you placed `Yuuko.jar`, and type `java -jar Yuuko.jar`. This will start the bot and generate a new directory called `config` in the directory `Yuuko.jar` is placed, before terminating.

4) Open the `config` directory and edit `config.yaml`. Here you must enter the `Client Id` and `Token` found on your Discord application page. The client id can be found under `General Information` and the token can be found under `Bot`.

5) Also in the `config` directory, open the `hikari` directory and edit `externaldb.yaml` and replace the relevant credentials if necessary.

6) Go back to the a command prompt and run the original `java -jar Yuuko.jar` command again, and the bot should spring to life... albeit with slightly reduced functionality.

### Adding API Keys

1) Open the `config` directory, you will see a list of pre-generated api configuration files.

2) Obtain API information from the services that you wish to enable and add them to the corresponding config file.

3) If already running, restart the bot.

### Enabling Music

1) See [lavalink repo](https://github.com/Frederikam/Lavalink) and create node(s).

2) Go to `config` directory and edit `lavalink.yaml`, following instructions.

3) If already running, restart the bot.

*(pro-tip: In Windows 10 you can click on the location bar in a directory and type `cmd` to open one directly to that directory!)*

---

<p align="center">
  <a href="https://discord.gg/VsM25fN"><img src="https://discordapp.com/api/guilds/368094427089993729/widget.png?style=banner3" alt="Discord Server"></a>
  <br>Support server: <a href="https://discord.gg/VsM25fN">https://discord.gg/VsM25fN</a>
</p>

---