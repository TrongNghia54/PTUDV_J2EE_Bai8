package phattrienungdungvoij2ee.bai4_qlsp.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import phattrienungdungvoij2ee.bai4_qlsp.model.Account;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    @Query("{ 'login_name': ?0 }")
    Optional<Account> findByLoginName(String login_name);
}
