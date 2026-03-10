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
            categoryRepository.save(new Category(UUID.randomUUID().toString(), "Điện thoại"));
            categoryRepository.save(new Category(UUID.randomUUID().toString(), "Laptop"));
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