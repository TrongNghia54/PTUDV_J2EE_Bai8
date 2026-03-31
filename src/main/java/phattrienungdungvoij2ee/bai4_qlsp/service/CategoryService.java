package phattrienungdungvoij2ee.bai4_qlsp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phattrienungdungvoij2ee.bai4_qlsp.model.Category;
import phattrienungdungvoij2ee.bai4_qlsp.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Khởi tạo dữ liệu mẫu
    @PostConstruct
    public void initSampleData() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("1", "Điện thoại"));
            categoryRepository.save(new Category("2", "Laptop"));
            categoryRepository.save(new Category("3", "Sách"));
            categoryRepository.save(new Category("4", "Tranh"));
            categoryRepository.save(new Category("5", "Tủ lạnh"));
            categoryRepository.save(new Category("6", "Máy giặt"));
            categoryRepository.save(new Category("7", "Nồi cơm điện"));
            categoryRepository.save(new Category("8", "Quạt điện"));
            categoryRepository.save(new Category("9", "Bếp từ"));
            categoryRepository.save(new Category("10", "Máy hút bụi"));
        } else {
            // Thêm danh mục mới nếu chưa có
            String[][] newCategories = {
                {"3", "Sách"}, {"4", "Tranh"}, {"5", "Tủ lạnh"},
                {"6", "Máy giặt"}, {"7", "Nồi cơm điện"}, {"8", "Quạt điện"},
                {"9", "Bếp từ"}, {"10", "Máy hút bụi"}
            };
            for (String[] cat : newCategories) {
                if (!categoryRepository.existsById(cat[0]) && categoryRepository.findByName(cat[1]) == null) {
                    categoryRepository.save(new Category(cat[0], cat[1]));
                }
            }
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category get(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.orElse(null);
    }

    public Category getByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category add(Category category) {
        if (category.getId() == null || category.getId().isEmpty()) {
            category.setId(UUID.randomUUID().toString());
        }
        return categoryRepository.save(category);
    }

    public void delete(String id) {
        categoryRepository.deleteById(id);
    }
}