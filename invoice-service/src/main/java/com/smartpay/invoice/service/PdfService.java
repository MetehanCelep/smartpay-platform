package com.smartpay.invoice.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.smartpay.invoice.entity.Invoice;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private static final String PDF_DIR = "invoices/pdf/";

    public String generateInvoicePdf(Invoice invoice) {
        try {
            // Create directory if not exists
            File directory = new File(PDF_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = "invoice_" + invoice.getInvoiceNumber() + ".pdf";
            String filePath = PDF_DIR + fileName;

            // Create PDF
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add content
            document.add(new Paragraph("SMARTPAY INVOICE")
                    .setFontSize(20)
                    .setBold());
            
            document.add(new Paragraph("\n"));
            
            document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber()));
            document.add(new Paragraph("Transaction ID: " + invoice.getTransactionId()));
            document.add(new Paragraph("Merchant ID: " + invoice.getMerchantId()));
            document.add(new Paragraph("Merchant Email: " + invoice.getMerchantEmail()));
            document.add(new Paragraph("Amount: " + invoice.getAmount() + " TL"));
            document.add(new Paragraph("Status: " + invoice.getStatus()));
            document.add(new Paragraph("Date: " + invoice.getCreatedAt()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));

            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Thank you for using SmartPay!"));

            document.close();

            System.out.println("📄 PDF generated: " + filePath);
            return filePath;

        } catch (Exception e) {
            System.err.println("Failed to generate PDF: " + e.getMessage());
            throw new RuntimeException("PDF generation failed", e);
        }
    }
}