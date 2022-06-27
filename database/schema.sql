create table expo_hall
(
    exp_id  bigint not null,
    hall_id bigint not null,
    primary key (exp_id, hall_id)
) engine = InnoDB;
create table expos
(
    id_expo      bigint            not null auto_increment,
    expo_date    date              not null,
    expo_time    time              not null,
    name         varchar(255),
    price        decimal(19, 2),
    status_id    integer default 1 not null,
    statistic_id bigint,
    theme_id     bigint,
    primary key (id_expo)
) engine = InnoDB;
create table expos_users
(
    user_id bigint not null,
    expo_id bigint not null
) engine = InnoDB;
create table halls
(
    id_hall bigint       not null auto_increment,
    name    varchar(255) not null,
    primary key (id_hall)
) engine = InnoDB;
create table statistic
(
    id      bigint not null auto_increment,
    sold    bigint default 0,
    tickets bigint,
    primary key (id)
) engine = InnoDB;
create table themes
(
    id_theme bigint       not null auto_increment,
    name     varchar(255) not null,
    primary key (id_theme)
) engine = InnoDB;
create table user_role
(
    user_id bigint not null,
    role    varchar(255)
) engine = InnoDB;
create table users
(
    id        bigint       not null auto_increment,
    balance   decimal(19, 2) default 0,
    email     varchar(255) not null,
    name      varchar(255) not null,
    password  varchar(255) not null,
    phone     varchar(255),
    status_id bigint,
    surname   varchar(255) not null,
    primary key (id)
) engine = InnoDB;
alter table expos
    add constraint UK_k5iw8tix13b803kngeb65hghh unique (name);
alter table halls
    add constraint UK_sadg6dwhsocpkayx2i7dy032t unique (name);
alter table themes
    add constraint UK_3estny12ybh85k7y8j6gyyrep unique (name);
alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table expo_hall
    add constraint FKbqtmhjw3jp81n2w8keinn0ib6 foreign key (hall_id) references halls (id_hall);
alter table expo_hall
    add constraint FKojsh6ypyphoqyi42gh5a0lai8 foreign key (exp_id) references expos (id_expo);
alter table expos
    add constraint FKptagf0yhhl2xoolmdi7iq8uxr foreign key (statistic_id) references statistic (id);
alter table expos
    add constraint FK8wmuqfcmiuemy9ly6e01fg47 foreign key (theme_id) references themes (id_theme);
alter table expos_users
    add constraint FKp9ywmhg9etdxoyfu0ls2brim5 foreign key (expo_id) references expos (id_expo);
alter table expos_users
    add constraint FK61i2352yhup5p5m961ixhyys foreign key (user_id) references users (id);
alter table user_role
    add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users (id);
