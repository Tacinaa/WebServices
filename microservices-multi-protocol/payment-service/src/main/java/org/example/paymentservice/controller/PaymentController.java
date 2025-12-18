package org.example.paymentservice.controller;

import org.example.paymentservice.dto.PaymentMapper;
import org.example.paymentservice.dto.PaymentResponseDTO;
import org.example.paymentservice.dto.ProcessPaymentRequestDTO;
import org.example.paymentservice.model.Payment;
import org.example.paymentservice.model.PaymentStatus;
import org.example.paymentservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // GET /api/payments
    @GetMapping
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentService.getAllPayments()
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    // GET /api/payments/{id}
    @GetMapping("/{id}")
    public PaymentResponseDTO getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return PaymentMapper.toResponse(payment);
    }

    // GET /api/payments/order/{orderId}
    @GetMapping("/order/{orderId}")
    public List<PaymentResponseDTO> getPaymentsByOrder(@PathVariable Long orderId) {
        return paymentService.getPaymentsByOrderId(orderId)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    // GET /api/payments/status/{status}
    @GetMapping("/status/{status}")
    public List<PaymentResponseDTO> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        return paymentService.getPaymentsByStatus(status)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    // POST /api/payments/process
    @PostMapping("/process")
    public PaymentResponseDTO processPayment(@RequestBody ProcessPaymentRequestDTO request) {
        return paymentService.processPayment(request);
    }

    // PATCH /api/payments/{id}/status?status=COMPLETED
    @PatchMapping("/{id}/status")
    public PaymentResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status
    ) {
        Payment updated = paymentService.updatePaymentStatus(id, status);
        return PaymentMapper.toResponse(updated);
    }

    // POST /api/payments/{id}/refund
    @PostMapping("/{id}/refund")
    public PaymentResponseDTO refundPayment(@PathVariable Long id) {
        Payment refunded = paymentService.refundPayment(id);
        return PaymentMapper.toResponse(refunded);
    }
}