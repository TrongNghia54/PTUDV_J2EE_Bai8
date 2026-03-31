package phattrienungdungvoij2ee.bai4_qlsp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import phattrienungdungvoij2ee.bai4_qlsp.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}
