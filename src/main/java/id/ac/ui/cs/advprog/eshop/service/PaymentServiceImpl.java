package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(order.getId(), method, paymentData);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        Payment result = paymentRepository.findById(payment.getId());
        if (result == null) {
            throw new NoSuchElementException();
        }

        Payment newPayment = new Payment(payment.getId(), payment.getMethod(), payment.getPaymentData(), status);
        paymentRepository.save(newPayment);

        Order order = orderRepository.findById(payment.getId());
        if (order != null) {
            if (PaymentStatus.SUCCESS.getValue().equals(status)) {
                order.setStatus(OrderStatus.SUCCESS.getValue());
            } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
                order.setStatus(OrderStatus.FAILED.getValue());
            }
            orderRepository.save(order);
        }
        return newPayment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }
}
