package phattrienungdungvoij2ee.bai4_qlsp.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import phattrienungdungvoij2ee.bai4_qlsp.model.Category;
import phattrienungdungvoij2ee.bai4_qlsp.model.Product;
import phattrienungdungvoij2ee.bai4_qlsp.service.CategoryService;
import phattrienungdungvoij2ee.bai4_qlsp.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Hiển thị danh sách sản phẩm với tìm kiếm, phân trang, sắp xếp, lọc
    @GetMapping()
    public String Index(@RequestParam(defaultValue = "") String keyword,
                        @RequestParam(defaultValue = "") String categoryId,
                        @RequestParam(defaultValue = "") String sort,
                        @RequestParam(defaultValue = "0") int page,
                        Model model) {
        Category category = null;
        if (categoryId != null && !categoryId.isBlank()) {
            category = categoryService.get(categoryId);
        }

        Page<Product> productPage = productService.searchProducts(keyword, category, sort, page, 5);

        model.addAttribute("listproduct", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/products";
    }

    // Hiển thị form tạo mới sản phẩm
    @GetMapping("/create")
    public String Create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/create";
    }

    // hiển thị danh sách (đã có trước)


    // Xử lý lưu sản phẩm mới
    @PostMapping("/create")
    public String Create(@Valid Product newProduct,
                         BindingResult result,
                         @RequestParam("category.id") String categoryId,
                         @RequestParam("imageProduct") MultipartFile imageProduct,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/create";
        }

        // Xử lý lưu ảnh và gán danh mục
        productService.updateImage(newProduct, imageProduct);
        Category selectedCategory = categoryService.get(categoryId);
        newProduct.setCategory(selectedCategory);

        productService.add(newProduct);
        return "redirect:/products";
    }

    // Xử lý xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String Delete(@PathVariable String id) {
        productService.delete(id);
        return "redirect:/products";
    }

    // Hiển thị form chỉnh sửa sản phẩm
    @GetMapping("/edit/{id}")
    public String Edit(@PathVariable String id, Model model) {
        Product find = productService.get(id);
        if (find == null) {
            return "error/404"; // Trang lỗi nếu không tìm thấy ID
        }
        model.addAttribute("product", find);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product/edit";
    }

    // Xử lý cập nhật sản phẩm
    @PostMapping("/edit")
    public String Edit(@Valid Product editProduct,
                       BindingResult result,
                       @RequestParam("imageProduct") MultipartFile imageProduct,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product/edit";
        }

        // Cập nhật ảnh mới nếu người dùng có chọn file
        if (imageProduct != null && !imageProduct.isEmpty()) {
            productService.updateImage(editProduct, imageProduct);
        }

        productService.update(editProduct);
        return "redirect:/products";
    }
}