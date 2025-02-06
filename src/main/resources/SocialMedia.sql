drop table if exists message;
drop table if exists account;
create table account (
    account_id int primary key auto_increment,
    username varchar(255) unique,
    password varchar(255)
);
create table message (
    message_id int primary key auto_increment,
    posted_by int,
    message_text varchar(255),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)
);

INSERT INTO account (account_id, username, password) VALUES (1, 'testuser', 'password123');
INSERT INTO message (message_id, posted_by, message_text, time_posted_epoch) VALUES (1, 1, 'Hello World', 1707100000);


