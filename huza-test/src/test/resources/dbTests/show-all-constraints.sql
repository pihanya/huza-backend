SELECT con.*
FROM pg_catalog.pg_constraint con
         INNER JOIN pg_catalog.pg_class rel
                    ON rel.oid = con.conrelid
         INNER JOIN pg_catalog.pg_namespace nsp
                    ON nsp.oid = connamespace
WHERE nsp.nspname = 'public'
  AND rel.relname = 'service_user'
UNION
SELECT con.*
FROM pg_catalog.pg_constraint con
         INNER JOIN pg_catalog.pg_class rel
                    ON rel.oid = con.conrelid
         INNER JOIN pg_catalog.pg_namespace nsp
                    ON nsp.oid = connamespace
WHERE nsp.nspname = 'public'
  AND rel.relname = 'asset'
UNION
SELECT con.*
FROM pg_catalog.pg_constraint con
         INNER JOIN pg_catalog.pg_class rel
                    ON rel.oid = con.conrelid
         INNER JOIN pg_catalog.pg_namespace nsp
                    ON nsp.oid = connamespace
WHERE nsp.nspname = 'public'
  AND rel.relname = 'asset_def'
UNION
SELECT con.*
FROM pg_catalog.pg_constraint con
         INNER JOIN pg_catalog.pg_class rel
                    ON rel.oid = con.conrelid
         INNER JOIN pg_catalog.pg_namespace nsp
                    ON nsp.oid = connamespace
WHERE nsp.nspname = 'public'
  AND rel.relname = 'build-order'
UNION
SELECT con.*
FROM pg_catalog.pg_constraint con
         INNER JOIN pg_catalog.pg_class rel
                    ON rel.oid = con.conrelid
         INNER JOIN pg_catalog.pg_namespace nsp
                    ON nsp.oid = connamespace
WHERE nsp.nspname = 'public'
  AND rel.relname = 'revinfo';

