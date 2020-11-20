-- drop table if exists projects;
create table projects(
    id int primary key auto_increment,
    description varchar(100) not NULL
);