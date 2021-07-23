package com.shopme.admin.product.controller;

import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.product.ProductService;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.util.List;

@Controller
public class ProductController {

  private ProductService productService;

  private BrandService brandService;

  private CategoryService categoryService;

  @Autowired
  public ProductController(
      ProductService productService, BrandService brandService, CategoryService categoryService) {
    this.productService = productService;
    this.brandService = brandService;
    this.categoryService = categoryService;
  }

  @GetMapping("/products")
  public String listFirstPage(Model model) {
    // return defaultRedirectURL;
    return "redirect:/products/page/1?sortField=name&sortDir=asc";
  }

  @GetMapping("/products/page/{pageNum}")
  public String listByPage(
      @PagingAndSortingParam(listName = "listProducts", moduleURL = "/products")
          PagingAndSortingHelper pagingAndSortingHelper,
      @PathVariable(name = "pageNum") int pageNum,
      Model model,
      Integer categoryId) {

    productService.listByPage(pageNum, pagingAndSortingHelper, categoryId);

    List<Category> listCategories = categoryService.listCategoriesUsedInForm();

    if (categoryId != null) model.addAttribute("categoryId", categoryId);
    model.addAttribute("listCategories", listCategories);

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
    model.addAttribute("numberOfExistingExtraImages", 0);

    return "products/product_form";
  }

  @PostMapping("/products/save")
  public String saveProduct(
      Product product,
      RedirectAttributes redirectAttributes,
      @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
      @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
      @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
      @RequestParam(name = "detailNames", required = false) String[] detailNames,
      @RequestParam(name = "detailValues", required = false) String[] detailValues,
      @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
      @RequestParam(name = "imageNames", required = false) String[] imageNames,
      @AuthenticationPrincipal ShopmeUserDetails loggedUser)
      throws IOException {

    if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
      if (loggedUser.hasRole("Salesperson")) {
        productService.saveProductPrice(product);
        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
        return "redirect:/products";
      }
    }

    ProductSaveHelper.setMainImageName(mainImageMultipart, product);
    ProductSaveHelper.setExistingExtraImagesNames(imageIDs, imageNames, product);
    ProductSaveHelper.setNewExtraImageNames(extraImageMultiparts, product);
    ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

    Product savedProduct = productService.save(product);

    ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

    ProductSaveHelper.deleteExtraImagesRemovedOnForm(product);

    redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");

    return "redirect:/products";
  }

  @GetMapping("/products/{id}/enabled/{status}")
  public String updateProductEnabledStatus(
      @PathVariable("id") Integer id,
      @PathVariable("status") boolean enabled,
      RedirectAttributes redirectAttributes) {
    productService.updateProductEnabledStatus(id, enabled);
    String status = enabled ? "enabled" : "disabled";
    String message = "The product ID " + id + " has been " + status;

    redirectAttributes.addFlashAttribute("message", message);

    return "redirect:/products";
  }

  @GetMapping("/products/delete/{id}")
  public String deleteProduct(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      productService.delete(id);
      String productExtraImagesDir = "../product-images/" + id + "/extras";
      String productImagesDir = "../product-images/" + id;

      FileUploadUtil.removeDir(productExtraImagesDir);
      FileUploadUtil.removeDir(productImagesDir);

      redirectAttributes.addFlashAttribute(
          "message", "The product ID: " + id + " has been deleted successfully");
    } catch (ProductNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
    }

    return "redirect:/products";
  }

  @GetMapping("/products/edit/{id}")
  public String editProduct(
      @PathVariable("id") Integer id,
      Model model,
      RedirectAttributes redirectAttributes,
      @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
    try {
      Product product = productService.get(id);

      List<Brand> listBrands = brandService.listAll();

      Integer numberOfExistingExtraImages = product.getImages().size();

      boolean isReadOnlyForSalesperson = false;
      if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
        if (loggedUser.hasRole("Salesperson")) {
          isReadOnlyForSalesperson = true;
        }
      }

      model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
      model.addAttribute("product", product);
      model.addAttribute("pageTitle", "Edit product (ID: " + id + ")");
      model.addAttribute("listBrands", listBrands);
      model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

      return "products/product_form";
    } catch (ProductNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return "redirect:/products";
    }
  }

  @GetMapping("/products/detail/{id}")
  public String viewProductDetails(
      @PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
    try {
      Product product = productService.get(id);

      model.addAttribute("product", product);

      return "products/product_detail_modal";
    } catch (ProductNotFoundException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getMessage());
      return "redirect:/products";
    }
  }
}
