drop table daily;

create table daily (
	username	varchar(20),
	status		varchar(4),
	word		varchar(20),
	id			numeric(4, 0),
	primary key (username, id)
);

delete from daily;
