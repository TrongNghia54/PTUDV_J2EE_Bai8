package phattrienungdungvoij2ee.bai4_qlsp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private String productId;
    private String productName;
    private String image;
    private long price;
    private int quantity;

    public long getSubtotal() {
        return price * quantity;
    }
}
