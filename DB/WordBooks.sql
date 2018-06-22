drop table WordBooks;

create table WordBooks (
	title	varchar(20),
	num		numeric(4, 0),
	primary key (title)
);

delete from WordBooks;

insert into WordBooks values ('TOEFLWordBook', '1300');
insert into WordBooks values ('CET6WordBook', '2083');
insert into WordBooks values ('GREWordBook', '3063');
