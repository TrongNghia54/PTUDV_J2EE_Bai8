package phattrienungdungvoij2ee.bai4_qlsp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import phattrienungdungvoij2ee.bai4_qlsp.model.CartItem;
import phattrienungdungvoij2ee.bai4_qlsp.model.Order;
import phattrienungdungvoij2ee.bai4_qlsp.model.OrderDetail;
import phattrienungdungvoij2ee.bai4_qlsp.repository.OrderRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Collection<CartItem> cartItems, String customerName) {
        List<OrderDetail> orderDetails = cartItems.stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setProductId(item.getProductId());
            detail.setProductName(item.getProductName());
            detail.setPrice(item.getPrice());
            detail.setQuantity(item.getQuantity());
            detail.setSubtotal(item.getSubtotal());
            return detail;
        }).collect(Collectors.toList());

        long totalAmount = orderDetails.stream().mapToLong(OrderDetail::getSubtotal).sum();

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setCustomerName(customerName);
        order.setOrderDetails(orderDetails);
        order.setTotalAmount(totalAmount);
        order.setOrderDate(new Date());

        return orderRepository.save(order);
    }
}
