package com.shopme.shoppingcart;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import com.shopme.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

  private CartItemRepository cartItemRepository;

  private ProductRepository productRepository;

  @Autowired
  public ShoppingCartServiceImpl(
      CartItemRepository cartItemRepository, ProductRepository productRepository) {
    this.cartItemRepository = cartItemRepository;
    this.productRepository = productRepository;
  }

  @Override
  public Integer addProduct(Integer productId, Integer quantity, Customer customer)
      throws ShoppingCartException {
    Integer updatedQuantity = quantity;
    Product product = new Product(productId);

    CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

    if (cartItem != null) {
      updatedQuantity += cartItem.getQuantity();

      if (updatedQuantity > 5) {
        throw new ShoppingCartException(
            "Could not add more "
                + quantity
                + " item(s) "
                + "because there's already "
                + cartItem.getQuantity()
                + " items "
                + "in your shopping cart. Maximum allowed quantity is 5.");
      }
    } else {
      cartItem = new CartItem();
      cartItem.setCustomer(customer);
      cartItem.setProduct(product);
    }

    cartItem.setQuantity(updatedQuantity);

    cartItemRepository.save(cartItem);

    return updatedQuantity;
  }

  @Override
  public List<CartItem> listCartItems(Customer customer) {
    return cartItemRepository.findByCustomer(customer);
  }

  @Override
  public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
    cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
    Product product = productRepository.findById(productId).get();
    float subtotal = product.getDiscountPrice() * quantity;
    return subtotal;
  }
}
