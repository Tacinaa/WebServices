package org.example.paymentservice.service;

import org.example.paymentservice.model.Payment;
import org.example.paymentservice.model.PaymentStatus;
import org.example.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // GET /api/payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // GET /api/payments/{id}
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
    }

    // GET /api/payments/order/{orderId}
    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    // GET /api/payments/status/{status}
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    // POST /api/payments/process
    public Payment processPayment(Long orderId, Double amount, String currency, String paymentMethod) {
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setPaymentMethod(paymentMethod);

        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    // PATCH /api/payments/{id}/status
    public Payment updatePaymentStatus(Long id, PaymentStatus newStatus) {
        Payment payment = getPaymentById(id);

        PaymentStatus current = payment.getStatus();

        boolean allowed =
                (current == PaymentStatus.PENDING && (newStatus == PaymentStatus.COMPLETED || newStatus == PaymentStatus.FAILED))
                        || (current == PaymentStatus.COMPLETED && newStatus == PaymentStatus.REFUNDED);

        if (!allowed) {
            throw new RuntimeException("Transition de statut invalide : " + current + " -> " + newStatus);
        }

        payment.setStatus(newStatus);
        payment.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    // POST /api/payments/{id}/refund
    public Payment refundPayment(Long id) {
        Payment payment = getPaymentById(id);

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Seuls les paiements terminés peuvent être remboursés");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setUpdatedAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
}