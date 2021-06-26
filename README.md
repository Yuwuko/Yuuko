![Yuuko Banner](https://i.imgur.com/3Aihicb.jpg)

<p align="center">
    <a href="https://github.com/Yuwuko/Yuuko/actions/workflows/gradle.yml">
        <img alt="Build" src="https://github.com/Yuwuko/Yuuko/actions/workflows/gradle.yml/badge.svg">
    </a>
    <a href="https://www.codefactor.io/repository/github/yuwuko/yuuko">
        <img alt="CodeFactor" src="https://www.codefactor.io/repository/github/yuwuko/yuuko/badge">
    </a>
    <a href="https://github.com/Yuuko-oh/Yuuko">
        <img alt="GitHubRelease" src="https://img.shields.io/github/release/Yuuko-oh/Yuuko.svg">
    </a>
    <a href="https://github.com/Yuuko-oh/Yuuko/stargazers">
        <img alt="GitHubStars" src="https://img.shields.io/github/stars/Yuuko-oh/Yuuko.svg">
    </a>
    <a href="https://github.com/Yuuko-oh/Yuuko/forks">
        <img alt="GitHubForks" src="https://img.shields.io/github/forks/Yuuko-oh/Yuuko.svg">
    </a>
    <a href="https://github.com/Yuuko-oh/Yuuko/issues">
        <img alt="GitHubIssues" src="https://img.shields.io/github/issues/Yuuko-oh/Yuuko.svg">
    </a>
    <a href="https://github.com/Yuuko-oh/Yuuko/blob/master/LICENSE">
        <img alt="GitHubLicense" src="https://img.shields.io/github/license/Yuuko-oh/Yuuko.svg">
    </a>
</p>

<p align="center">
    <img alt="Servers" src="https://top.gg/api/widget/servers/420682957007880223.svg">
    <img alt="Upvotes" src="https://top.gg/api/widget/upvotes/420682957007880223.svg">
</p>

# Yuuko 21w25b

Yuuko, programmed in [Java](https://www.oracle.com/uk/java/index.html) way back in mid-2018. Slowly growing into what is has become today. This project always was, and always will be a labour of love.

If you want Yuuko on your server, follow [this](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) link to invite her... be sure to give all the appropriate permissions, to not run into issues. Visit the [website](https://yuwuko.github.io) if you need support or you want to support Yuuko on pateron.

## Key Features

#### Language
Yuuko currently fully supports English and (very) partially supports French (bad google translation). If you want your language implemented, let me know by joining the support server. Any help with translation will be greatly appreciated!

#### Music
Liven up your Discord server with music from a number of sources including YouTube, Twitch, SoundCloud or any HTTP source of audio such as online radio stations.

#### Reaction Role
Want your users to be able to select roles simply by reacting to a message? With the reaction role feature you can assign server roles to reacts on messages!

#### Interaction
Have you ever wanted to spice up your chatting experience with interactions? Yuuko has a range of different interaction commands available that allows you to express yourself in more than just words.

#### Events
Take full advantage of Yuuko's event system, with modifiable title, description, scheduled time and number of slots, plan events and get an optional reminder 10 minutes before it starts.

## Commands

| Module      | Commands                                                                                                                        | 
| ----------- | ------------------------------------------------------------------------------------------------------------------------------- |
| Core        | `help` `shards` `module` `about` `vote` `permissions` `bind`                                                                    |
| Animal      | `cat` `fox` `dog` `bird`                                                                                                        |
| NSFW        | `efukt` `neko` `rule34`                                                                                                         |
| Moderation  | `nuke` `kick` `mute` `ban`                                                                                                      |
| Interaction | `pout` `shrug` `angry` `cry` `kiss` `poke` `sleep` `attack` `bite` `blush` `hug` `laugh` `tickle` `pet` `pat` `kill` `dance`    |
| Utility     | `guild` `ping` `roles` `avatar` `user` `reactrole` `event`                                                                      |
| Developer   | `setstatus` `reloadapi` `lavalink` `syncguilds` `restart` `shutdown` `logmetrics`                                               |
| Audio       | `play` `playnext` `last` `clear` `skip` `seek` `pause` `search` `current` `stop` `background` `loop` `shuffle` `lyrics` `queue` |
| Media       | `osu` `github` `anime` `natgeo` `underground` `petition` `weather` `tesco` `urban` `osrs`                                       |
| Fun         | `8ball` `spoilerify` `roll` `choose` `flip` `advice` `joke` `horoscope`                                                         |
| Setting     | `settings` `language` `prefix` `starboard` `commandlog` `moderationlog` `djmode` `cleanupcommands` `playnotifications` `eventchannel`      |

## Self-host 

***[THIS GUIDE MAY BE INCOMPLETE]***

Work has been done **[Nov 2020]** to retroactively make Yuuko as easy as possible to self-host. Below are instructions on how you can do just that. Basic setup excludes music and commands that rely on API keys however these can be setup with

### Requirements
- Java 16+ ~ [download link](https://jdk.java.net/16/)
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

6) Go back to the command prompt and run the original `java -jar Yuuko.jar` command again, and the bot should spring to life... albeit with slightly reduced functionality.

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

## Dependencies

| Name                                                            | Version          |
| --------------------------------------------------------------- | ---------------- |
| [reflections](https://github.com/ronmamo/reflections)           | 0.9.12           |
| [JDA](https://github.com/DV8FromTheWorld/JDA)                   | 4.3.0_281        |
| [Lavalink-Client](https://github.com/Yuwuko/Lavalink-Client)    | 0409d4cf2b       |
| [DBL-Java-Library](https://github.com/top-gg/java-sdk)          | 2.0.1            |
| [Google-Api-Youtube](https://github.com/googleapis)             | v3-rev222-1.25.0 |
| [Gson](https://github.com/google/gson)                          | 2.8.7            |
| [Jackson-Annotations](https://github.com/FasterXML/jackson)     | 2.12.3           |
| [Jackson-Databind](https://github.com/FasterXML/jackson)        | 2.12.3           |
| [SnakeYAML](https://github.com/asomov/snakeyaml)                | 1.29             |
| [OkHttp](https://github.com/square/okhttp)                      | 5.0.0-alpha.2    |
| [jsoup](https://github.com/jhy/jsoup)                           | 1.13.1           |
| [MySQL Connector/J](https://github.com/mysql/mysql-connector-j) | 8.0.25           |
| [HikariCP](https://github.com/brettwooldridge/HikariCP)         | 4.0.3            |
| [SLF4J-Api](https://github.com/qos-ch/slf4j)                    | 2.0.0-alpha1     |
| [logback](https://github.com/qos-ch/logback)                    | 1.3.0-alpha5     |
| [RuneAPI.java](https://github.com/BasketBandit/RuneAPI.java)    | 0.4.4            |

---

<p align="center">
  <a href="https://discord.gg/VsM25fN"><img src="https://discordapp.com/api/guilds/368094427089993729/widget.png?style=banner3" alt="Discord Server"></a>
  <br>Support server: <a href="https://discord.gg/VsM25fN">https://discord.gg/VsM25fN</a>
</p>