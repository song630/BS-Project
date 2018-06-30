drop table privatebooks;

create table privatebooks (
	username	varchar(20),
	origin		varchar(20),
	id			numeric(4, 0),
	word		varchar(20),
	primary key (username, origin, id)
);

delete from privatebooks;
