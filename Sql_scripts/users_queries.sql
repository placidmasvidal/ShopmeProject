#set foreign_key_checks = 0;
#truncate table shopmedb.users_roles;
#truncate table shopmedb.users;
#truncate table shopmedb.roles;
set foreign_key_checks = 1;

#SELECT * FROM shopmedb.users WHERE first_name LIKE '%bruce%' OR last_name LIKE '%bruce%';
#SELECT * FROM shopmedb.users WHERE email = 'mikegates2012@gmail.com';
SELECT * FROM shopmedb.users;
SELECT * FROM shopmedb.users_roles;