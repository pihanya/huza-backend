----table service_user
--valid insert
insert into service_user (audit_date, creation_date, version, email, password, role, username)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0 , 'testuser1@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser1');
--check unique username
insert into service_user (audit_date, creation_date, version, email, password, role, username)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0 , 'testuser2@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser1');
--check unique email
insert into service_user (audit_date, creation_date, version, email, password, role, username)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0 , 'testuser2@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser2');

--empty name
insert into service_user (audit_date, creation_date, version, email, password, role, username)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0 , 'testuser3@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', '');
--empty email
insert into service_user (audit_date, creation_date, version, email, password, role, username)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0 , '', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'wizard', 'testuser3');

----table asset_def
--valid insert
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Test asset', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', 'Тестовое определение', 'TEST TYPE');
--unique name
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Test asset 1', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', 'Тестовое определение', 'TEST TYPE');
--unique code
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Test asset', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', 'Тестовое определение 1', 'TEST TYPE');
--empty name
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Test asset 2', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', '', 'TEST TYPE');
--empty type
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Test asset 2', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', 'Тестовое определение 2', '');
--empty code
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, '', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', 'Тестовое определение 2', 'TEST TYPE');
--null name
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Test asset 2', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', null, 'TEST TYPE');
--null type
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Test asset 2', 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', 'Тестовое определение', null);
--null code
insert into asset_def (audit_date, creation_date, version, code, description, img_orig_url, name, type)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, 'test asset description...', 'https://www.meme-arsenal.com/memes/f617aa0a967ad16f1c96627b6c66d59b.jpg', 'Тестовое определение', 'TEST TYPE');

----table asset
--valid insert
insert into asset (audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, 250, 1);
--null quantity
insert into asset (audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, null, 1);
--check non-negative quantity
insert into asset (audit_date, creation_date, version, code, description, name, quantity, asset_def_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, -250, 1);
--empty fk
insert into asset (audit_date, creation_date, version, code, description, name, quantity)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, null, null, null, -250);

----table build_order
--valid insert
insert into build_order (audit_date, creation_date, version, comment, ordinal, status, asset_def_id, created_asset_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1, 1);
--null ordinal
insert into build_order (audit_date, creation_date, version, comment, ordinal, status, asset_def_id, created_asset_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', null, 0, 1, 1);
--null status
insert into build_order (audit_date, creation_date, version, comment, ordinal, status, asset_def_id, created_asset_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, null, 1, 1);
--empty asset_def fk
insert into build_order (audit_date, creation_date, version, comment, ordinal, status, created_asset_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1);
--empty created_asset fk
insert into build_order (audit_date, creation_date, version, comment, ordinal, status, asset_def_id)
    values (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'Something...', 1, 0, 1);

----table revinfo
--valid insert
insert into revinfo (revtstmp) values (1676124382938);
--not null revtstmp
insert into revinfo (revtstmp) values (null);

