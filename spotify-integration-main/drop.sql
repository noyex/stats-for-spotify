alter table if exists artists drop foreign key if exists FK6nh88dso8f1xjofmrwy0bf7qf;
alter table if exists playback_history drop foreign key if exists FK9tfjafkfatlk0i5cqnj3odraq;
alter table if exists playlists drop foreign key if exists FKtgjwvfg23v990xk7k0idmqbrj;
alter table if exists tracks drop foreign key if exists FKqqlc9twwk0l5crobhwmsnx7cn;
drop table if exists artists;
drop table if exists playback_history;
drop table if exists playlists;
drop table if exists tracks;
drop table if exists users;
