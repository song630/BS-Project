drop table user;

create table user (
	username	varchar(20),
	password	varchar(20),
	email		varchar(30),
	phone		varchar(11),
	education	enum('primary', 'junior', 'senior', 'undergraduate', 'graduate'),
	studying	varchar(15),
	studied		numeric(4, 0),
	plan 		numeric(3, 0),
	lastDate 	varchar(11),
	day1		numeric(4, 0),
	day2		numeric(4, 0),
	day3		numeric(4, 0),
	day4		numeric(4, 0),
	day5		numeric(4, 0),
	day6		numeric(4, 0),
	day7		numeric(4, 0),
	primary key (username)
);

delete from user;

insert into user values ('user01', '20072324', 'songcx2211@sina.com', '18867107419', 'undergraduate', 'TOEFLWordBook', '40', '20', '', '0', '0', '0', '0', '0', '0', '0');
insert into user values ('user02', '20072324', 'aaa@sina.com', '18867107419', 'undergraduate', 'none', '0', '20', '', '0', '0', '0', '0', '0', '0', '0');
insert into user values ('user03', '123456', 'bbb@ina.com', '18812341234', 'undergraduate', 'none', '0', '20', '', '0', '0', '0', '0', '0', '0', '0');
insert into user values ('user04', '123456', 'ccc@sna.com', '18812341234', 'senior', 'none', '0', '20', '', '0', '0', '0', '0', '0', '0', '0');
insert into user values ('user05', '654321', 'ddd@sina.com', '18812341234', 'senior', 'none', '0', '20', '', '0', '0', '0', '0', '0', '0', '0');
insert into user values ('user06', '654321', 'eee@sina.com', '18812341234', 'senior', 'none', '0', '20', '', '0', '0', '0', '0', '0', '0', '0');
insert into user values ('user07', '111111', 'fff@sina.com', '18812341234', 'senior', 'none', '0', '20', '', '0', '0', '0', '0', '0', '0', '0');

drop table daily;

create table daily (
	username	varchar(20),
	yes			varchar(4),
	word		varchar(20),
	id			numeric(4, 0),
	primary key (username, id)
);

delete from daily;

drop table privatebooks;

create table privatebooks (
	username	varchar(20),
	title		varchar(20),
	origin		varchar(20),
	id			numeric(4, 0),
	word		varchar(20),
	primary key (username, title, origin, id)
);

delete from privatebooks;
