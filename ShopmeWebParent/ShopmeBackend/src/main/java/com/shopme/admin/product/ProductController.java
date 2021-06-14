package com.shopme.admin.product;

import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

  private ProductService productService;

  private BrandService brandService;

  @Autowired
  public ProductController(ProductService productService, BrandService brandService) {
    this.productService = productService;
    this.brandService = brandService;
  }

  @GetMapping("/products")
  public String listAll(Model model) {

    List<Product> listProducts = productService.listAll();

    model.addAttribute("listProducts", listProducts);
    return "products/products";
  }

  @GetMapping("/products/new")
  public String newProduct(Model model) {
    List<Brand> listBrands = brandService.listAll();

    Product product = new Product();
    product.setEnabled(true);
    product.setInStock(true);

    model.addAttribute("listBrands", listBrands);
    model.addAttribute("product", product);
    model.addAttribute("pageTitle", "Create New Product");

    return "products/product_form";
  }

  @PostMapping("/products/save")
  public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
    System.out.println("Product name: " + product.getName());
    System.out.println("Brand ID: " + product.getBrand().getId());
    System.out.println("Category ID: " + product.getCategory().getId());

    return "redirect:/products";
  }
}
