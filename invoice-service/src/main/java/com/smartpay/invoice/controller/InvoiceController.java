package com.smartpay.invoice.controller;

import com.smartpay.invoice.dto.InvoiceResponse;
import com.smartpay.invoice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Invoice Service is UP");
    }

    @GetMapping("/all")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(invoiceService.getInvoiceByNumber(invoiceNumber));
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByMerchant(@PathVariable String merchantId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByMerchant(merchantId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByEmail(@PathVariable String email) {
        return ResponseEntity.ok(invoiceService.getInvoicesByEmail(email));
    }
}