create database feeds;

use feeds;

create table posts(
	post_id			varchar(8) not null,
    comments		mediumtext,
    picture			mediumblob,
    constraint		posts_pk primary key (post_id)
);