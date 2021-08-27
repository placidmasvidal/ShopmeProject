package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTests {

  @Autowired CartItemRepository sut;
  @Autowired TestEntityManager entityManager;

  @Test
  public void testSaveItem() {
    Integer customerId = 1;
    Integer productId = 1;

    Customer customer = entityManager.find(Customer.class, customerId);
    Product product = entityManager.find(Product.class, productId);

    CartItem newItem = new CartItem();
    newItem.setCustomer(customer);
    newItem.setProduct(product);
    newItem.setQuantity(1);

    CartItem savedItem = sut.save(newItem);

    assertThat(savedItem.getId()).isGreaterThan(0);
  }

  @Test
  public void testSave2Items() {
    Integer customerId = 10;
    Integer productId = 10;

    Customer customer = entityManager.find(Customer.class, customerId);
    Product product = entityManager.find(Product.class, productId);

    CartItem item1 = new CartItem();
    item1.setCustomer(customer);
    item1.setProduct(product);
    item1.setQuantity(2);

    CartItem item2 = new CartItem();
    item2.setCustomer(new Customer(customerId));
    item2.setProduct(new Product(8));
    item2.setQuantity(3);

    Iterable<CartItem> iterable = sut.saveAll(List.of(item1, item2));

    assertThat(iterable).size().isGreaterThan(0);
  }

  @Test
  public void testFindByCustomer() {
    Integer customerId = 10;
    List<CartItem> cartItems = sut.findByCustomer(new Customer(customerId));
    cartItems.forEach(System.out::println);
    assertThat(cartItems.size()).isEqualTo(2);
  }

  @Test
  public void testFindByCustomerAndProduct() {
    Integer customerId = 10;
    Integer productId = 10;

    CartItem item = sut.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
    assertThat(item).isNotNull();
    System.out.println("item = " + item);
  }

  @Test
  public void testUpdateQuantity(){
    Integer customerId = 1;
    Integer productId = 1;
    Integer quantity = 4;

    sut.updateQuantity(quantity, customerId, productId);
    CartItem item = sut.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

    assertThat(item.getQuantity()).isEqualTo(4);
  }

  @Test
  public void testDeleteByCustomerAndProduct(){
    Integer customerId = 10;
    Integer productId = 10;

    sut.deleteByCustomerAndProduct(customerId, productId);

    CartItem item = sut.findByCustomerAndProduct(new Customer(customerId), new Product(productId));

    assertThat(item).isNull();
  }
}
