package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    Map<String, String> paymentDataVoucher;
    Map<String, String> paymentDataBank;

    @BeforeEach
    void setUp() {
        paymentDataVoucher = new HashMap<>();
        paymentDataVoucher.put("voucherCode", "ESHOP1234ABC5678");

        paymentDataBank = new HashMap<>();
        paymentDataBank.put("bankName", "BCA");
        paymentDataBank.put("referenceCode", "1234567890");
    }

    @Test
    void testCreatePaymentVoucherSuccess() {
        Payment payment = new Payment("1", "VOUCHER_CODE", paymentDataVoucher);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedCodeLengthNot16() {
        paymentDataVoucher.put("voucherCode", "ESHOP1234ABC567");
        Payment payment = new Payment("1", "VOUCHER_CODE", paymentDataVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNotStartWithEshop() {
        paymentDataVoucher.put("voucherCode", "BSHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER_CODE", paymentDataVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherRejectedNot8Numerical() {
        paymentDataVoucher.put("voucherCode", "ESHOP1234ABC567A");
        Payment payment = new Payment("1", "VOUCHER_CODE", paymentDataVoucher);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentBankSuccess() {
        Payment payment = new Payment("2", "BANK_TRANSFER", paymentDataBank);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testCreatePaymentBankRejectedEmptyBankName() {
        paymentDataBank.put("bankName", "");
        Payment payment = new Payment("2", "BANK_TRANSFER", paymentDataBank);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentBankRejectedNullReferenceCode() {
        paymentDataBank.put("referenceCode", null);
        Payment payment = new Payment("2", "BANK_TRANSFER", paymentDataBank);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testCreatePaymentInvalidMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("3", "INVALID", paymentDataVoucher);
        });
    }

    @Test
    void testCreatePaymentEmptyPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("4", "VOUCHER_CODE", new HashMap<>());
        });
    }

    @Test
    void testCreatePaymentWithStatusConstructor() {
        Payment payment = new Payment("5", "BANK_TRANSFER", paymentDataBank, "SUCCESS");
        assertEquals("5", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(paymentDataBank, payment.getPaymentData());
    }
}
