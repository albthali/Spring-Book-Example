create table if not exists users
(
	username varchar(50) not null
		primary key,
	password varchar(255) not null,
	enabled tinyint(1) not null
);

create table if not exists authorities
(
	username varchar(50) not null,
	authority varchar(50) not null,
	constraint ix_auth_username
		unique (username, authority),
	constraint fk_authorities_users
		foreign key (username) references users (username)
);

