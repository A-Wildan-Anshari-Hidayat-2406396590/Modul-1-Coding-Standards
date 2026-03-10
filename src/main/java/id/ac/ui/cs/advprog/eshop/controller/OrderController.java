package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/create")
    public String createOrderPage(Model model) {
        Product dummyProduct = new Product();
        dummyProduct.setProductId("dummy");
        dummyProduct.setProductName("dummy");
        dummyProduct.setProductQuantity(1);

        Order order = new Order(
                UUID.randomUUID().toString(),
                Collections.singletonList(dummyProduct),
                System.currentTimeMillis(),
                "");
        model.addAttribute("order", order);
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@ModelAttribute Order order, Model model) {
        Product dummyProduct = new Product();
        dummyProduct.setProductId("dummy");
        dummyProduct.setProductName("dummy");
        dummyProduct.setProductQuantity(1);

        Order newOrder = new Order(
                UUID.randomUUID().toString(),
                Collections.singletonList(dummyProduct),
                System.currentTimeMillis(),
                order.getAuthor());
        orderService.createOrder(newOrder);
        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "orderHistory";
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "orderHistory";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "paymentOrder"; 
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId, Model model) {
        return "redirect:/payment/detail/temp-id";
    }
}
