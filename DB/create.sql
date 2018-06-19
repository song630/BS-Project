drop user;

create table user (
	username	varchar(20),
	password	varchar(20),
	email		varchar(30),
	phone		varchar(11),
	education	enum('primary', 'junior', 'senior', 'undergraduate', 'graduate'),
	primary key (username)
);
