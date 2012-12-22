--This script adds the default items into the database.
--Many of these items are used for testing purposes and are fake data.
insert into ProductCategory values ('veggie', 0.00);
insert into ProductCategory values ('fruit', 0.10);
insert into ProductCategory values ('candy', 0.08);
insert into ProductCategory values ('utensil', 0.05);
insert into ProductCategory values ('chocolate', 0.15);
insert into ProductCategory values ('magazine', 0.13);
insert into ProductCategory values ('prepared food', 0.15);

insert into BulkProduct values ('11111', 'Banana', 0.69, 'veggie', 0.49);
insert into BulkProduct values ('22222', 'Orange', 0.99, 'fruit', 0.69);
insert into BulkProduct values ('33333', 'Spinach', 0.99, 'veggie', 0.55);
insert into BulkProduct values ('44444', 'Fuji Apple', 2.79, 'fruit', 1.99);
insert into BulkProduct values ('55555', 'Kiwi', 1.29, 'fruit', 0.85);
insert into BulkProduct values ('66666', 'Jellybean', 0.05, 'candy', 0.02);
insert into BulkProduct values ('77777', 'Plastic fork', 0.25, 'utensil', 0.10);
insert into BulkProduct values('88888', 't-bone steak',9.99,'prepared food', 6.78); --sale
insert into BulkProduct values('99999', 'liver', 10, 'prepared food', 8.69); --sale
insert into BulkProduct values('12345', 'chicken', 4.50, 'prepared food', 4.00); --sale
insert into BulkProduct values('12121', 'fries', 10, 'prepared food', 8.69); --sale
insert into BulkProduct values('21212', 'stale fries', 2.00, 'prepared food', 1.00); --sale
insert into BulkProduct values('06660', 'fries', 10, 'prepared food', 8.69); --sale
insert into BulkProduct values('06661', 'stale fries', 2.00, 'prepared food', 1.00); --sale

insert into PackagedProduct values ('786936224306', 'Kellogg Cereal', 3.52, 1.35, 'prepared food', 3.0);
insert into PackagedProduct values ('717951000842', 'Coca Cola (12 pack)', 3.20, 4, 'prepared food', 3.0);
insert into PackagedProduct values ('024543213710', 'Ice Cream', 4.00, 2.2, 'prepared food', 3.23);
insert into PackagedProduct values ('085392132225', 'Oreo Cookies', 3.50, 0.8, 'prepared food', 2.0);
insert into PackagedProduct values ('737666003167', 'Sees Chocolate', 4.50, 1, 'chocolate', 4.0);
insert into PackagedProduct values ('780166035718', 'Wired', 3.33, 0.6, 'magazine', 1.25); --sale
insert into PackagedProduct values ('796030114977', 'Brownies', 6.00, 2, 'prepared food', 4.50); --sale
insert into PackagedProduct values ('712345678904', 'Bread', 10, 4, 'prepared food', 5.00); --sale
insert into PackagedProduct values ('086637677174', 'mystery box', 99.99, 2, 'prepared food', 10); --sale
insert into PackagedProduct values ('012345678905', 'ketchup', 2, 4, 'prepared food', 1.00); --sale

insert into ImpulseProducts values (1, 'Sees Chocolate', '737666003167', 1, 0.5, '2012-10-14');
insert into ImpulseProducts values (1, 'Wired', '780166035718', 2, 4.16, '2012-10-20');
insert into ImpulseProducts values (1, 'Jellybean', '66666', 5, 0.15, '2012-10-10');
insert into ImpulseProducts values (1, 'Plastic fork', '77777', 14, 2.1, '2012-10-20');
insert into ImpulseProducts values (2, 'Sees Chocolate', '737666003167', 27, 0.5, '2012-10-14');
insert into ImpulseProducts values (2, 'Wired', '780166035718', 2, 4.16, '2012-10-11');
insert into ImpulseProducts values (2, 'Jellybean', '66666', 3, 0.09, '2012-10-12');
insert into ImpulseProducts values (2, 'Plastic fork', '77777', 57, 8.55, '2012-10-13');
insert into ImpulseProducts values (3, 'Sees Chocolate', '737666003167', 4, 2.0, '2012-09-14');
insert into ImpulseProducts values (3, 'Wired', '780166035718', 53, 110.24, '2012-05-14');
insert into ImpulseProducts values (3, 'Jellybean', '66666', 53, 1.59, '2012-08-14');
insert into ImpulseProducts values (3, 'Plastic fork', '77777', 33, 4.95, '2012-09-28');

insert into Sale values(null,'88888','1000-01-01','9999-01-01',0.20,0);
insert into Sale values(null,'780166035718','1000-00-01','9999-01-01',0.20,0);
insert into Sale values(null,'796030114977','1000-00-01','9999-01-01',0.0, 2.00);
insert into Sale values(null,'99999','1000-00-01','9999-01-01',0.0, 2.00);
insert into Sale values(null,'12345','1000-00-01','9999-01-01',0.2, 2.00);
insert into Sale values(null,'712345678904','1000-00-01','9999-01-01',0.2, 2.00);
insert into Sale values(null,'12121','1000-00-01','9999-01-01',1.0, 0);
insert into Sale values(null,'21212','1000-00-01','9999-01-01', 0, 2.00);
insert into Sale values(null,'086637677174','1000-00-01','9999-01-01', 1.0 , 0);
insert into Sale values(null,'012345678905','1000-00-01','9999-01-01',0, 2.00);
insert into Sale values(null,'06660','1000-00-01','9999-01-01', 1.5 , 0);
insert into Sale values(null,'06661','1000-00-01','9999-01-01',0, -200.00);
