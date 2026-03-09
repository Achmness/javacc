package PrescriptionPrinter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Text; // Important!
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;

import java.io.File;

public class pres {

public void generatePDF(String fileName, String patient, String owner, String species, 
                        String date, String diagnosis, String medication, String instructions) {
    try {
        PdfWriter writer = new PdfWriter(new File(fileName));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 1. Define our fonts explicitly
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // --- Header Section ---
        document.add(new Paragraph(new Text("MCA Animal Care Clinic").setFont(bold))
                .setTextAlignment(TextAlignment.RIGHT).setFontSize(10));
        
        document.add(new Paragraph("South road, Brgy. Tuyan, Sitio Botong, Naga, Cebu 6037")
                .setFont(regular).setTextAlignment(TextAlignment.RIGHT).setFontSize(8));

        // --- Title ---
        document.add(new Paragraph(new Text("PRESCRIPTION SLIP").setFont(bold).setUnderline())
                .setTextAlignment(TextAlignment.CENTER));

        // --- Table Section ---
        float[] columnWidths = {1, 1};
        Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
        
        // Add cells using the .setFont(bold) method on the labels
        table.addCell(new Cell().add(new Paragraph().add(new Text("Patient: ").setFont(bold)).add(new Text(patient).setFont(regular))));
        table.addCell(new Cell().add(new Paragraph().add(new Text("Owner: ").setFont(bold)).add(new Text(owner).setFont(regular))));
        table.addCell(new Cell().add(new Paragraph().add(new Text("Species: ").setFont(bold)).add(new Text(species).setFont(regular))));
        table.addCell(new Cell().add(new Paragraph().add(new Text("Date: ").setFont(bold)).add(new Text(date).setFont(regular))));
        
        document.add(table.setMarginTop(10));

        // --- Rx Section ---
        document.add(new Paragraph(new Text("Rx").setFont(bold).setFontSize(22)));
        document.add(new Paragraph(new Text(medication).setFont(bold)).setMarginLeft(30));
        document.add(new Paragraph().add(new Text("Sig: ").setFont(bold)).add(new Text(instructions).setFont(regular)).setMarginLeft(30).setFontSize(10));

        // --- Diagnosis ---
        document.add(new Paragraph().add(new Text("\nDiagnosis: ").setFont(bold)).add(new Text(diagnosis).setFont(regular)).setMarginTop(20));

        // --- Signature ---
        document.add(new Paragraph("\n\n__________________________").setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph(new Text("Dr. Diana Dawn Minoza").setFont(bold))
                .add(new Text("\nVeterinarian").setFont(regular))
                .setTextAlignment(TextAlignment.RIGHT).setFontSize(10));

        document.close();
        System.out.println("Success! Prescription saved: " + fileName);

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
}
}