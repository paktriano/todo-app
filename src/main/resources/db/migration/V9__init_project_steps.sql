-- drop table if exists projects;
create table project_steps(
    id int primary key auto_increment,
    description varchar(100) not NULL,
    project_id  int not null,
    days_to_deadline int not null,
    foreign key (project_id) references PROJECTS(id)
);