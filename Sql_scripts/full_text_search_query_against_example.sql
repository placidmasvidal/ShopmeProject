SELECT * FROM products WHERE MATCH (name, short_description, full_description)
AGAINST ('iphone 8');