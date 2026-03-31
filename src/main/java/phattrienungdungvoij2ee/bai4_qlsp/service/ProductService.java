package phattrienungdungvoij2ee.bai4_qlsp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import phattrienungdungvoij2ee.bai4_qlsp.model.Category;
import phattrienungdungvoij2ee.bai4_qlsp.model.Product;
import phattrienungdungvoij2ee.bai4_qlsp.repository.ProductRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product get(String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public void add(Product newProduct) {
        if (newProduct.getId() == null || newProduct.getId().isEmpty()) {
            long seqId = sequenceGenerator.generateSequence("products_sequence");
            newProduct.setId(String.valueOf(seqId));
        }
        productRepository.save(newProduct);
    }

    public void update(Product editProduct) {
        Product find = get(editProduct.getId());
        if (find != null) {
            find.setPrice(editProduct.getPrice());
            find.setName(editProduct.getName());
            if (editProduct.getImage() != null && !editProduct.getImage().isEmpty()) {
                find.setImage(editProduct.getImage());
            }
            find.setCategory(editProduct.getCategory());
            productRepository.save(find);
        }
    }

    public void delete(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchByName(String query) {
        if (query == null || query.isBlank()) {
            return getAll();
        }
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public Page<Product> searchProducts(String keyword, Category category, String sort, int page, int size) {
        Sort sortObj = Sort.unsorted();
        if ("price_asc".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.ASC, "price");
        } else if ("price_desc".equals(sort)) {
            sortObj = Sort.by(Sort.Direction.DESC, "price");
        }

        Pageable pageable = PageRequest.of(Math.max(page, 0), size, sortObj);
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasCategory = category != null;

        if (hasKeyword && hasCategory) {
            return productRepository.findByNameContainingIgnoreCaseAndCategory(keyword, category, pageable);
        } else if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (hasCategory) {
            return productRepository.findByCategory(category, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public void updateImage(Product newProduct, MultipartFile imageProduct) {
        String contentType = imageProduct.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tệp tải lên không phải là hình ảnh!");
        }
        if (!imageProduct.isEmpty()) {
            try {
                // lưu ảnh vào thư mục "static/images" bên ngoài jar/war để có thể truy cập khi chạy
                Path dirImages = Paths.get("static/images");
                if (!Files.exists(dirImages)) {
                    Files.createDirectories(dirImages);
                }
                String newFileName = UUID.randomUUID() + "_" + imageProduct.getOriginalFilename();
                Path pathFileUpload = dirImages.resolve(newFileName);
                Files.copy(imageProduct.getInputStream(), pathFileUpload, StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}