package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        if (paymentData == null || paymentData.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (!PaymentMethod.contains(method)) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.status = calculateStatus(method, paymentData);
    }

    public Payment(String id, String method, Map<String, String> paymentData, String status) {
        this(id, method, paymentData);
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    private String calculateStatus(String method, Map<String, String> paymentData) {
        if (PaymentMethod.VOUCHER_CODE.getValue().equals(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode != null && voucherCode.length() == 16 &&
                    voucherCode.startsWith("ESHOP")) {
                int numCount = 0;
                for (char c : voucherCode.toCharArray()) {
                    if (Character.isDigit(c))
                        numCount++;
                }
                if (numCount == 8)
                    return PaymentStatus.SUCCESS.getValue();
            }
            return PaymentStatus.REJECTED.getValue();
        } else if (PaymentMethod.BANK_TRANSFER.getValue().equals(method)) {
            String bankName = paymentData.get("bankName");
            String referenceCode = paymentData.get("referenceCode");

            if (bankName == null || bankName.trim().isEmpty() ||
                    referenceCode == null || referenceCode.trim().isEmpty()) {
                return PaymentStatus.REJECTED.getValue();
            }
            return PaymentStatus.SUCCESS.getValue();
        }
        return PaymentStatus.REJECTED.getValue();
    }
}
