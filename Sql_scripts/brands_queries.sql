#set foreign_key_checks = 0;
#truncate table shopmedb.brands;
#truncate table shopmedb.brands_categories;
#set foreign_key_checks = 1;

SELECT * FROM shopmedb.brands;
SELECT * FROM shopmedb.brands_categories;