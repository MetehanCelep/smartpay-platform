package com.smartpay.invoice.listener;

import com.smartpay.invoice.event.PaymentEvent;
import com.smartpay.invoice.service.InvoiceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private final InvoiceService invoiceService;

    public PaymentEventListener(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @RabbitListener(queues = "payment.events")
    public void handlePaymentEvent(PaymentEvent event) {
        System.out.println("📄 Received payment event for invoice: " + event.getTransactionId());

        // Only create invoice for successful payments
        if ("SUCCESS".equals(event.getStatus())) {
            invoiceService.createInvoice(
                event.getTransactionId(),
                event.getMerchantId(),
                event.getEmail(),
                event.getAmount(),
                event.getStatus()
            );
        } else {
            System.out.println("⏭️  Skipping invoice creation for non-successful payment");
        }
    }
}