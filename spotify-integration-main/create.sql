create table artists (user_id integer not null, id bigint not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
create table playback_history (user_id integer not null, id bigint not null auto_increment, played_at datetime(6) not null, album_image_url varchar(255), artist_name varchar(255), track_name varchar(255), primary key (id)) engine=InnoDB;
create table playlists (number_of_tracks integer, user_id integer not null, id bigint not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
create table tracks (user_id integer not null, id bigint not null auto_increment, album_name varchar(255), artist_name varchar(255), name varchar(255), primary key (id)) engine=InnoDB;
create table users (id integer not null auto_increment, access_token varchar(255), email varchar(255), ref_id varchar(255), refresh_token varchar(255), username varchar(255), primary key (id)) engine=InnoDB;
alter table if exists artists add constraint FK6nh88dso8f1xjofmrwy0bf7qf foreign key (user_id) references users (id);
alter table if exists playback_history add constraint FK9tfjafkfatlk0i5cqnj3odraq foreign key (user_id) references users (id);
alter table if exists playlists add constraint FKtgjwvfg23v990xk7k0idmqbrj foreign key (user_id) references users (id);
alter table if exists tracks add constraint FKqqlc9twwk0l5crobhwmsnx7cn foreign key (user_id) references users (id);
