package phattrienungdungvoij2ee.bai4_qlsp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import phattrienungdungvoij2ee.bai4_qlsp.model.CartItem;
import phattrienungdungvoij2ee.bai4_qlsp.model.Order;
import phattrienungdungvoij2ee.bai4_qlsp.model.Product;
import phattrienungdungvoij2ee.bai4_qlsp.service.OrderService;
import phattrienungdungvoij2ee.bai4_qlsp.service.ProductService;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @SuppressWarnings("unchecked")
    private Map<String, CartItem> getCart(HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        Product product = productService.get(productId);
        if (product != null && quantity > 0) {
            Map<String, CartItem> cart = getCart(session);
            if (cart.containsKey(productId)) {
                CartItem item = cart.get(productId);
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                CartItem item = new CartItem(
                        productId,
                        product.getName(),
                        product.getImage(),
                        product.getPrice(),
                        quantity
                );
                cart.put(productId, item);
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/products";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<String, CartItem> cart = getCart(session);
        Collection<CartItem> cartItems = cart.values();
        long totalAmount = cartItems.stream().mapToLong(CartItem::getSubtotal).sum();
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "cart/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam String productId,
                             @RequestParam int quantity,
                             HttpSession session) {
        Map<String, CartItem> cart = getCart(session);
        if (cart.containsKey(productId) && quantity > 0) {
            cart.get(productId).setQuantity(quantity);
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable String productId, HttpSession session) {
        Map<String, CartItem> cart = getCart(session);
        cart.remove(productId);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam(value = "selectedItems", required = false) List<String> selectedItems,
                           HttpSession session, Model model, Principal principal) {
        Map<String, CartItem> cart = getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }

        // Nếu không chọn sản phẩm nào thì quay lại giỏ hàng
        if (selectedItems == null || selectedItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Chỉ lấy các sản phẩm được chọn
        Collection<CartItem> selectedCartItems = cart.values().stream()
                .filter(item -> selectedItems.contains(item.getProductId()))
                .collect(Collectors.toList());

        if (selectedCartItems.isEmpty()) {
            return "redirect:/cart";
        }

        String customerName = principal != null ? principal.getName() : "Guest";
        Order order = orderService.createOrder(selectedCartItems, customerName);

        // Xóa các sản phẩm đã đặt khỏi giỏ hàng
        selectedItems.forEach(cart::remove);
        session.setAttribute("cart", cart);

        model.addAttribute("order", order);
        return "cart/checkout";
    }
}
