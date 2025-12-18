package org.example.paymentservice.controller;

import org.example.paymentservice.dto.PaymentMapper;
import org.example.paymentservice.dto.PaymentResponseDTO;
import org.example.paymentservice.dto.ProcessPaymentRequestDTO;
import org.example.paymentservice.model.Payment;
import org.example.paymentservice.model.PaymentStatus;
import org.example.paymentservice.service.PaymentService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments() {
        List<PaymentResponseDTO> result = paymentService.getAllPayments()
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    // GET /api/payments/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }

    // GET /api/payments/order/{orderId}
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByOrderId(@PathVariable Long orderId) {
        List<PaymentResponseDTO> result = paymentService.getPaymentsByOrderId(orderId)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    // GET /api/payments/status/{status}
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponseDTO> result = paymentService.getPaymentsByStatus(status)
                .stream()
                .map(PaymentMapper::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    // POST /api/payments/process
    @PostMapping("/process")
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody ProcessPaymentRequestDTO request) {
        Payment saved = paymentService.processPayment(
                request.getOrderId(),
                request.getAmount(),
                request.getCurrency(),
                request.getPaymentMethod()
        );
        return ResponseEntity.ok(PaymentMapper.toResponse(saved));
    }

    // PATCH /api/payments/{id}/status
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentResponseDTO> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status
    ) {
        Payment updated = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(PaymentMapper.toResponse(updated));
    }

    // POST /api/payments/{id}/refund
    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@PathVariable Long id) {
        Payment refunded = paymentService.refundPayment(id);
        return ResponseEntity.ok(PaymentMapper.toResponse(refunded));
    }
}