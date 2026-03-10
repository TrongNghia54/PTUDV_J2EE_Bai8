package phattrienungdungvoij2ee.bai4_qlsp.model;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
}
