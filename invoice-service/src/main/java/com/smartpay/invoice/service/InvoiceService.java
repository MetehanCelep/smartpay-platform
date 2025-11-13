package com.smartpay.invoice.service;

import com.smartpay.invoice.dto.InvoiceResponse;
import com.smartpay.invoice.entity.Invoice;
import com.smartpay.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PdfService pdfService;

    public InvoiceService(InvoiceRepository invoiceRepository, PdfService pdfService) {
        this.invoiceRepository = invoiceRepository;
        this.pdfService = pdfService;
    }

    public Invoice createInvoice(String transactionId, String merchantId, String email, Double amount, String status) {
        
        // Check if invoice already exists for this transaction
        if (invoiceRepository.findByTransactionId(transactionId).isPresent()) {
            System.out.println("Invoice already exists for transaction: " + transactionId);
            return invoiceRepository.findByTransactionId(transactionId).get();
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setTransactionId(transactionId);
        invoice.setMerchantId(merchantId);
        invoice.setMerchantEmail(email);
        invoice.setAmount(amount);
        invoice.setStatus(status);
        invoice.setCreatedAt(LocalDateTime.now());

        // Save invoice first
        invoice = invoiceRepository.save(invoice);

        // Generate PDF
        String pdfPath = pdfService.generateInvoicePdf(invoice);
        invoice.setPdfPath(pdfPath);

        // Update with PDF path
        invoice = invoiceRepository.save(invoice);

        System.out.println("✅ Invoice created: " + invoice.getInvoiceNumber());
        
        return invoice;
    }

    public List<InvoiceResponse> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public InvoiceResponse getInvoiceByNumber(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return mapToResponse(invoice);
    }

    public List<InvoiceResponse> getInvoicesByMerchant(String merchantId) {
        return invoiceRepository.findByMerchantId(merchantId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<InvoiceResponse> getInvoicesByEmail(String email) {
        return invoiceRepository.findByMerchantEmail(email).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String generateInvoiceNumber() {
        return "INV-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        InvoiceResponse response = new InvoiceResponse();
        response.setId(invoice.getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setTransactionId(invoice.getTransactionId());
        response.setMerchantId(invoice.getMerchantId());
        response.setMerchantEmail(invoice.getMerchantEmail());
        response.setAmount(invoice.getAmount());
        response.setStatus(invoice.getStatus());
        response.setCreatedAt(invoice.getCreatedAt());
        response.setPdfPath(invoice.getPdfPath());
        return response;
    }
}