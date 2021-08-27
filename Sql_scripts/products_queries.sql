#set foreign_key_checks = 0;
#truncate table shopmedb.product_details;
#truncate table shopmedb.product_images;
#truncate table shopmedb.products;
#set foreign_key_checks = 1;

#SELECT * FROM shopmedb.products;
#SELECT * FROM shopmedb.product_images;
#SELECT * FROM shopmedb.product_details;

SELECT p.name, c.name FROM products p JOIN categories c ON p.category_id=c.id
WHERE c.id=4 OR c.all_parent_ids LIKE '%-4-%';