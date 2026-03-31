package phattrienungdungvoij2ee.bai4_qlsp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private String productId;
    private String productName;
    private long price;
    private int quantity;
    private long subtotal;
}
