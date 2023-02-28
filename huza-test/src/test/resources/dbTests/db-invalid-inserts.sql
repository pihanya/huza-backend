----table service_user
--valid insert
insert into service_user (id, audit_date, creation_date, version, auth_date, avatar_url, email, password, role, username)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null,
            'https://www.gravatar.com/avatar/1ca58d0a4a10ea07fabd4e4d32968982?s=256&d=identicon&r=PG',
            'testuser1@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser1');
--duplicate pkey
insert into service_user (id, audit_date, creation_date, version, auth_date, avatar_url, email, password, role, username)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null,
            'https://www.gravatar.com/avatar/1ca58d0a4a10ea07fabd4e4d32968982?s=256&d=identicon&r=PG',
            'testuser1@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser1');
--check unique username
insert into service_user (id, audit_date, creation_date, version, auth_date, avatar_url, email, password, role, username)
    values (101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null,
            'https://www.gravatar.com/avatar/1ca58d0a4a10ea07fabd4e4d32968982?s=256&d=identicon&r=PG',
            'testuser2@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser1');
----table asset_def
--valid insert
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'test', null, null, null,
            'http://localhost:4242/api/files/download/resource_gold', null, null, 'test', 'SPELL');
--duplicate pkey
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'test1', null, null, null,
        'http://localhost:4242/api/files/download/resource_gold', null, null, 'test', 'SPELL');
--duplicate code
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
    values (101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'test', null, null, null,
        'http://localhost:4242/api/files/download/resource_gold', null, null, 'test', 'SPELL');
--null code
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
    values (102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, null,
        'http://localhost:4242/api/files/download/resource_gold', null, null, 'test', 'SPELL');
--null img_orig_url
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
    values (103, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'test2', null, null, null,
        null, null, null, 'test', 'SPELL');
--null name
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
    values (104, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'test3', null, null, null,
        'http://localhost:4242/api/files/download/resource_gold', null, null, null, 'SPELL');
--null type
insert into asset_def (id, audit_date, creation_date, version, code, cost, description, fraction, img_orig_url, level, magic_school, name, type)
    values (105, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'test4', null, null, null,
        'http://localhost:4242/api/files/download/resource_gold', null, null, 'test', null);

----table asset
--valid insert
insert into asset (id, audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, 250, 1);
--duplicate pkey
insert into asset (id, audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, 250, 1);
--null quantity
insert into asset (id, audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
    values (101, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, null, 1);
--check non-negative quantity
insert into asset (id, audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
    values (102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, -250, 1);
--empty fk
insert into asset (id, audit_date, creation_date, version, code, description, name, quantity)
    values (103, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, -250);

----table build_order
--valid insert
insert into build_order (id, audit_date, creation_date, version, comment, ordinal, status, asset_def_id, created_asset_id)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1, 1);
--duplicate pkey
insert into build_order (id, audit_date, creation_date, version, comment, ordinal, status, asset_def_id, created_asset_id)
    values (100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1, 1);
--null status
insert into build_order (id, audit_date, creation_date, version, comment, ordinal, status, asset_def_id, created_asset_id)
    values (102, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, null, 1, 1);
--empty asset_def fk
insert into build_order (id, audit_date, creation_date, version, comment, ordinal, status, created_asset_id)
    values (103, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1);
--empty created_asset fk
insert into build_order (id, audit_date, creation_date, version, comment, ordinal, status, asset_def_id)
    values (104, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1);

----table revinfo
--valid insert
insert into revinfo (rev, revtstmp) values (100, 1676124382938);
--duplicate pkey
insert into revinfo (rev, revtstmp) values (100, 1676124382939);
--not null revtstmp
insert into revinfo (rev, revtstmp) values (100, null);

