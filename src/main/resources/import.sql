insert into client(id,name,surname,email,create_on)values(1,'john','lennon','john@lennon.com','2018-07-15');
insert into client(id,name,surname,email,create_on)values(2,'paul','mccartney','paul@mccartney.com','2018-07-15');
insert into client(id,name,surname,email,create_on)values(3,'george','harrison','george@harrison.com','2018-07-15');
insert into client(id,name,surname,email,create_on)values(4,'ringo','starr','ringo@starr.com','2018-07-15');
insert into client(id,name,surname,email,create_on)values(5,'julian','lennon','julian@lennon.com','2018-07-15');

insert into product(id,name,price,create_on)values(1,'guitar',100.0,'2018-07-15');
insert into product(id,name,price,create_on)values(2,'bass',50.0,'2018-07-15');
insert into product(id,name,price,create_on)values(3,'trumpet',1000.0,'2018-07-15');
insert into product(id,name,price,create_on)values(4,'drum',200.0,'2018-07-15');
insert into product(id,name,price,create_on)values(5,'piano',90.0,'2018-07-15');
insert into product(id,name,price,create_on)values(6,'cymbal',30.0,'2018-07-15');
insert into product(id,name,price,create_on)values(7,'triangle',60.0,'2018-07-15');
insert into product(id,name,price,create_on)values(8,'marimba',70.0,'2018-07-15');

insert into bill(id,description,client_id,create_on)values(1,'sgt peppers',1,'2018-07-15');
insert into bill(id,description,client_id,create_on)values(2,'abbey road',1,'2018-07-15');
insert into bill(id,description,client_id,create_on)values(3,'white album',1,'2018-07-15');
insert into bill(id,description,client_id,create_on)values(4,'revolver',2,'2018-07-15');
insert into bill(id,description,client_id,create_on)values(5,'rubber soul',3,'2018-07-15');
insert into bill(id,description,client_id,create_on)values(6,'help',3,'2018-07-15');
insert into bill(id,description,client_id,create_on)values(7,'let it be',1,'2018-07-15');
insert into bill(id,description,client_id,create_on)values(8,'please please me',4,'2018-07-15');

insert into line(id,quantity,product_id,bill_id,create_on)values(1,4,1,1,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(2,2,2,1,'2018-07-15');

insert into line(id,quantity,product_id,bill_id,create_on)values(3,1,8,2,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(4,1,7,2,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(5,2,2,2,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(6,2,1,2,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(7,2,3,3,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(8,2,4,4,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(9,2,5,4,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(10,2,1,5,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(11,2,1,6,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(12,2,1,7,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(13,2,1,8,'2018-07-15');
insert into line(id,quantity,product_id,bill_id,create_on)values(14,1,5,8,'2018-07-15');