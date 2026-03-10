package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment paymentVoucher;
    Payment paymentBank;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Map<String, String> paymentDataVoucher = new HashMap<>();
        paymentDataVoucher.put("voucherCode", "ESHOP1234ABC5678");
        paymentVoucher = new Payment("1", "VOUCHER_CODE", paymentDataVoucher);

        Map<String, String> paymentDataBank = new HashMap<>();
        paymentDataBank.put("bankName", "BCA");
        paymentDataBank.put("referenceCode", "1234567890");
        paymentBank = new Payment("2", "BANK_TRANSFER", paymentDataBank);
    }

    @Test
    void testSaveCreate() {
        Payment result = paymentRepository.save(paymentVoucher);
        assertEquals(paymentVoucher.getId(), result.getId());
        assertEquals(paymentVoucher.getMethod(), result.getMethod());
        assertEquals(paymentVoucher.getStatus(), result.getStatus());
        assertEquals(paymentVoucher.getPaymentData(), result.getPaymentData());
    }

    @Test
    void testSaveUpdate() {
        paymentRepository.save(paymentVoucher);
        Map<String, String> newData = new HashMap<>();
        newData.put("voucherCode", "ESHOP8765CBA4321");
        Payment newPayment = new Payment(paymentVoucher.getId(), "VOUCHER_CODE", newData);

        Payment result = paymentRepository.save(newPayment);

        assertEquals(newPayment.getId(), result.getId());
        assertEquals(newPayment.getPaymentData(), result.getPaymentData());
    }

    @Test
    void testFindByIdIfFound() {
        paymentRepository.save(paymentVoucher);
        Payment result = paymentRepository.findById(paymentVoucher.getId());

        assertNotNull(result);
        assertEquals(paymentVoucher.getId(), result.getId());
    }

    @Test
    void testFindByIdIfNotFound() {
        paymentRepository.save(paymentVoucher);
        Payment result = paymentRepository.findById("InvalidId");

        assertNull(result);
    }

    @Test
    void testGetAllPayments() {
        paymentRepository.save(paymentVoucher);
        paymentRepository.save(paymentBank);

        List<Payment> result = paymentRepository.getAllPayments();
        assertEquals(2, result.size());
        assertTrue(result.contains(paymentVoucher));
        assertTrue(result.contains(paymentBank));
    }
}
