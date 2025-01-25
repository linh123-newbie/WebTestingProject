create database userTest;
use userTest;
#Users
create table if not exists users(
	userName varchar(30) not null,
    password varchar(30) not null,
    id int not null primary key auto_increment,
    firstName varchar(30),
    lastName varchar(30),
    telephone varchar(30)
);
describe users;
insert into users values("gialinh2025@gmail.com","12345",1, "Linh", "Nguyen", "0907065927");
insert into users values("gialinh0167@gmail.com","01656107662a",2, "Linh", "Nguyen", "0907065927");
insert into users values("gialinh2024@gmail.com","12345",3, "Linh", "Nguyen", "01656107662");
select * from users;
#Orders
create table if not exists orders(
    userId int not null,
    address varchar(30) not null,
    city varchar(30) not null,
    postCode varchar(30),
    country varchar(30) not null,
    region varchar(30) not null,
    comments varchar(100),
    numberOfProducts int not null,
    orderDate timestamp default current_timestamp,
    status varchar(30) not null,
    foreign key (userId) references users(id)
);
describe orders;
insert into orders values (2,"15 Bay Street","Chiba","17982","Japan","Osaka","They are useful products and affordable for everyone."
,(SELECT COUNT(*)
FROM orderItems
join products on products.productId=orderItems.productId
WHERE orderItems.idUser = 2 and products.stockQuantity>=orderItems.quantity),CURRENT_TIMESTAMP,"Pending"
);
select * from orders;

#Products
create table if not exists products(
	productId int auto_increment primary key,
    name varchar(30) not null,
    price decimal(10,2) not null,
    stockQuantity int not null
);
insert into products values(1,"iPod Nano",100.00,525);
insert into products values(2,"HTC Touch HD",146.00,39100);
insert into products values(3,"	iPod Shuffle",182.00,0);
insert into products values(4,"	iPod Touch",194.00,0);
insert into products values(5,"	iPod Classic",100.00,625);
insert into products values(6,"MacBook Pro",2000.00,0);
insert into products values(7,"	Apple Cinema 30",77.00,14);
insert into products values(8,"	MacBook",500.00,90);
select * from products;

#OrderItems
create table orderItems(
	idUser int not null,
    productId int not null,
    quantity int not null,
    foreign key (idUser) references users(id),
    foreign key (productId) references products(productId)
);

insert into orderItems values(1,1,5);
insert into orderItems values(1,2,1);
insert into orderItems values(1,3,1);
insert into orderItems values(1,5,10);
insert into orderItems values(1,6,1);
insert into orderItems values(2,1,2);
insert into orderItems values(2,8,1);
insert into orderItems values(2,5,3);
insert into orderItems values(2,7,1);
insert into orderItems values(2,3,1);
select * from orderItems;
describe orderItems; 

#Query infor to buy product
select
	users.userName,
    users.password,
    products.name,
    orderItems.quantity,
    products.stockQuantity,
    users.lastName,
    users.firstName,
    users.telephone,
    orders.address,
    orders.city,
    orders.postCode,
    orders.country,
    orders.region,
    orders.comments,
    orders.numberOfProducts,
    orders.status
from orderItems
join users on orderItems.idUser=users.id
join products on products.productId=orderItems.productId
join orders on orderItems.idUser=orders.userId
where users.id=2;

select userName, password  from users where id=3;

