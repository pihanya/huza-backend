insert into service_user (id, audit_date, creation_date, version, email, password, role, username)
    values (nextval('service_user_seq'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0 , 'testadmin@itmo.ru', '$2a$12$37nUwqRyoOozJMDo42CoqOWEFebcnqXs6PE8WbA.UF6OMJKuQcD9G', 'admin', 'testadmin');
