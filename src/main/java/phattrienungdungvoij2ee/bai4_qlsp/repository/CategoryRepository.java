package phattrienungdungvoij2ee.bai4_qlsp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import phattrienungdungvoij2ee.bai4_qlsp.model.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findByName(String name);
}
