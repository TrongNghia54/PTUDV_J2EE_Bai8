package phattrienungdungvoij2ee.bai4_qlsp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import phattrienungdungvoij2ee.bai4_qlsp.model.Account;
import phattrienungdungvoij2ee.bai4_qlsp.model.Role;
import phattrienungdungvoij2ee.bai4_qlsp.repository.AccountRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MongoTemplate mongoTemplate;
    
    @Override
    public void run(String... args) throws Exception {
        // Only initialize if no accounts exist
        if (accountRepository.count() == 0) {
            // Create and save roles first
            Role userRole = new Role();
            userRole.setId("1");
            userRole.setName("ROLE_USER");
            mongoTemplate.save(userRole);
            
            Role adminRole = new Role();
            adminRole.setId("2");
            adminRole.setName("ROLE_ADMIN");
            mongoTemplate.save(adminRole);
            
            // Create user account
            Account userAccount = new Account();
            userAccount.setLogin_name("user");
            userAccount.setPassword(passwordEncoder.encode("123456"));
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            userAccount.setRoles(userRoles);
            
            // Create admin account
            Account adminAccount = new Account();
            adminAccount.setLogin_name("admin");
            adminAccount.setPassword(passwordEncoder.encode("admin123"));
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminAccount.setRoles(adminRoles);
            
            accountRepository.save(userAccount);
            accountRepository.save(adminAccount);
        }
    }
}
