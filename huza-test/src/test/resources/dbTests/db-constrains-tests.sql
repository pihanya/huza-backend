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


----table revinfo
--valid insert
insert into revinfo (revtstmp) values (1676124382938);
--not null revtstmp
insert into revinfo (revtstmp) values (null);

