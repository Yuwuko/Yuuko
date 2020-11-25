create table if not exists dbyuuko.metrics_audio
(
    players int(3) null,
    activePlayers int(3) null,
    queueSize int(4) null,
    dateInserted timestamp default CURRENT_TIMESTAMP null
)
    charset=utf8;

create table if not exists dbyuuko.metrics_cache
(
    trackIdMatch int(13) not null,
    trackIdSize int(13) not null,
    dateInserted timestamp default CURRENT_TIMESTAMP not null
)
    charset=utf8;

create table if not exists dbyuuko.guilds
(
    guildId varchar(18) not null,
    primary key (guildId)
)
    charset=utf8;

create table if not exists dbyuuko.guilds_data
(
    guildId varchar(18) not null,
    guildName varchar(255) null,
    guildRegion varchar(32) null,
    guildIcon varchar(255) null,
    guildSplash varchar(255) null,
    dateAdded timestamp default CURRENT_TIMESTAMP null,
    lastUpdated timestamp default CURRENT_TIMESTAMP null,
    primary key (guildId),
    constraint guilds_data_guilds_guildId_fk
        foreign key (guildId) references dbyuuko.guilds (guildId)
            on update cascade on delete cascade
)
    charset=utf8;

create table if not exists dbyuuko.guilds_settings
(
    guildId varchar(18) not null,
    prefix varchar(5) default '-' not null,
    deleteExecuted tinyint(1) default 0 not null,
    nowPlaying tinyint(1) default 1 not null,
    djMode tinyint(1) default 0 not null,
    starboard varchar(18) null,
    comLog varchar(18) null,
    modLog varchar(18) null,
    lastUpdated timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    primary key (guildId),
    constraint guilds_settings_guilds_guildId_fk
        foreign key (guildId) references dbyuuko.guilds (guildId)
            on update cascade on delete cascade
)
    charset=utf8;

create table if not exists dbyuuko.guilds_module_binds
(
    guildId varchar(18) not null,
    channelId varchar(18) not null,
    moduleName varchar(128) not null,
    dateInserted timestamp default CURRENT_TIMESTAMP null,
    constraint ModuleBindings_guildId_channelId_moduleName_pk
        unique (guildId, channelId, moduleName),
    constraint guilds_module_binds_guilds_guildId_fk
        foreign key (guildId) references dbyuuko.guilds (guildId)
            on update cascade on delete cascade
)
    charset=utf8;

create table if not exists dbyuuko.guilds_module_settings
(
    guildId varchar(18) not null,
    animal tinyint(1) default 1 not null,
    moderation tinyint(1) default 1 not null,
    audio tinyint(1) default 1 not null,
    utility tinyint(1) default 1 not null,
    interaction tinyint(1) default 1 not null,
    fun tinyint(1) default 1 not null,
    media tinyint(1) default 1 not null,
    nsfw tinyint(1) default 1 not null,
    lastUpdated timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    primary key (guildId),
    constraint guilds_module_settings_guilds_guildId_fk
        foreign key (guildId) references dbyuuko.guilds (guildId)
            on update cascade on delete cascade
)
    charset=utf8;

create table if not exists dbyuuko.guilds_reaction_roles
(
    guildId varchar(18) not null,
    messageId varchar(18) not null,
    emoteId varchar(64) not null,
    roleId varchar(18) not null,
    constraint message
        unique (messageId, emoteId),
    constraint guilds_reaction_roles_guilds_guildId_fk
        foreign key (guildId) references dbyuuko.guilds (guildId)
            on update cascade on delete cascade
)
    charset=utf8;

create table if not exists dbyuuko.shards
(
    shardId int not null,
    status varchar(32) default 'unknown' not null,
    guilds int default 0 not null,
    gatewayPing double(11,1) default 0.0 not null,
    restPing double(11,1) default 0.0 not null,
    restartSignal tinyint default 0 not null,
    shutdownSignal tinyint default 0 not null,
    shardAssigned timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint shardId
        unique (shardId)
)
    charset=utf8;

create table if not exists dbyuuko.metrics_command
(
    shardId int(3) null,
    guildId varchar(18) null,
    command varchar(32) null,
    executionTime double(8,2) null,
    dateInserted timestamp default CURRENT_TIMESTAMP null,
    constraint metrics_command_shards_shardId_fk
        foreign key (shardId) references dbyuuko.shards (shardId)
            on update cascade on delete cascade
)
    charset=utf8;

create table if not exists dbyuuko.metrics_discord
(
    shardId int(3) not null,
    gatewayPing double(11,1) not null,
    restPing double(11,1) not null,
    guildCount int not null,
    dateInserted timestamp default CURRENT_TIMESTAMP null,
    constraint metrics_discord_shards_shardId_fk
        foreign key (shardId) references dbyuuko.shards (shardId)
            on update cascade on delete cascade
)
    charset=utf8;

create table if not exists dbyuuko.metrics_system
(
    shardId int(3) not null,
    uptime bigint(32) not null,
    memoryTotal int not null,
    memoryUsed int not null,
    dateInserted timestamp default CURRENT_TIMESTAMP null,
    constraint metrics_system_shards_shardId_fk
        foreign key (shardId) references dbyuuko.shards (shardId)
            on update cascade on delete cascade
)
    charset=utf8;

