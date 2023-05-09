drop table if exists items;
drop table if exists products;
drop table if exists bills;

create table products(id int auto_increment,
 name varchar(50) not null,
 description varchar(50) not null,
 price float not null,
 constraint pk_products primary key (id),
 constraint unq_products unique (name)
 );


CREATE table bills(
                      id int auto_increment,
                      date datetime,
                      number int not null,
                      customer varchar(50),
                      constraint pk_bills primary key (id),
                      constraint unq_bills_number unique (number));


CREATE table items (id_product int not null,
                    id_bill int not null,
                    qty float not null,
                    price float not null,
                    constraint pk_items primary key (id_product, id_bill),
                    constraint fk_item_product foreign key (id_product) references products(id),
                    constraint fk_item_bill foreign key (id_bill) references bills(id));


