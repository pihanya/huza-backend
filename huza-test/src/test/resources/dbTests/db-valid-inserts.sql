----table service_user
--valid insert
insert into service_user (id, audit_date, creation_date, version, auth_date, avatar_url, email, password, role, username)
values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null,
        'https://www.gravatar.com/avatar/1ca58d0a4a10ea07fabd4e4d32968982?s=256&d=identicon&r=PG',
        'testuser1@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser1');

----table asset_def
--valid insert
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'test', null, null, null,
        'http://localhost:4242/api/files/download/resource_gold', null, null, 'test', 'SPELL');

----table asset
--valid insert
insert into asset (id, audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, 250, 1);

----table build_order
--valid insert
insert into build_order (id, audit_date, creation_date, version, comment, ordinal, status, asset_def_id, created_asset_id)
values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1, 1);

----table revinfo
--valid insert
insert into revinfo (rev, revtstmp) values (100, 1676124382938);

delete from service_user where id = 100;

delete from asset_def where id = 100;

delete from asset where id = 100;

delete from build_order where id = 100;

delete from revinfo where rev = 100;
